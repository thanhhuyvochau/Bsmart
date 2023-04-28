package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Answer;
import fpt.project.bsmart.entity.Question;
import fpt.project.bsmart.entity.Subject;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.constant.EUserRole;
import fpt.project.bsmart.entity.constant.QuestionType;
import fpt.project.bsmart.entity.dto.QuestionDto;
import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.repository.QuestionRepository;
import fpt.project.bsmart.repository.SubjectRepository;
import fpt.project.bsmart.service.IQuestionService;
import fpt.project.bsmart.util.ConvertUtil;
import fpt.project.bsmart.util.PageUtil;
import fpt.project.bsmart.util.QuestionUtil;
import fpt.project.bsmart.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuestionServiceImpl implements IQuestionService {
    private final QuestionRepository questionRepository;
    private final SubjectRepository subjectRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository, SubjectRepository subjectRepository) {
        this.questionRepository = questionRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public Boolean importQuestionToQuestionBank(MultipartFile file, Long subjectId) throws IOException {
        User currentUser = SecurityUtil.getCurrentUser();
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy môn mà bạn muốn import câu hỏi, vui lòng thử lại!"));
        List<Question> questions = QuestionUtil.convertAikenToQuestion(file, currentUser, subject);
        questionRepository.saveAll(questions);
        return true;
    }

    @Override
    public Boolean addQuestionToQuestionBank(AddQuestionRequest request) {
        QuestionType questionType = request.getQuestionType();
        switch (questionType) {
            case SINGLE:
                return addSingleChoiceQuestionBank(request);
            case MULTIPLE:
                return addMultipleChoiceQuestionBank(request);
            default:
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Không có loại câu hỏi, vui lòng thêm vào!");
        }
    }


    private Boolean addSingleChoiceQuestionBank(AddQuestionRequest request) {
        User owner = SecurityUtil.getCurrentUser();
        Subject subject = subjectRepository.findById(request.getSubjectId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy môn mà bạn muốn import câu hỏi, vui lòng thử lại!"));
        Question question = new Question();
        question.setQuestion(request.getQuestion());
        question.setQuestionType(request.getQuestionType());
        question.setMentor(owner);
        question.setSubject(subject);
        question.setIsShared(request.getIsShared());
        boolean isHasRightQuestion = false;
        for (AddAnswerRequest answerRequest : request.getAnswers()) {
            Answer answer = new Answer();
            answer.setAnswer(answerRequest.getAnswer());
            answer.setKey(answerRequest.getKey());
            answer.setIsRight(answerRequest.getIsRight());
            answer.setQuestion(question);
            question.getAnswers().add(answer);
            // Kiểm tra đã có câu trả lời đúng cho câu hỏi chưa - chỉ cho phép duy nhất 1 câu trả lời đúng
            if (answer.getIsRight() && !isHasRightQuestion) {
                isHasRightQuestion = true;
            } else if (answer.getIsRight() && isHasRightQuestion) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Không thể có 2 câu hỏi đúng trong loại SINGLE");
            }
        }
        if (!isHasRightQuestion) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Bạn chưa chọn câu trả lời chính xác cho câu hỏi!");
        }
        questionRepository.save(question);
        return true;
    }


    private Boolean addMultipleChoiceQuestionBank(AddQuestionRequest request) {
        User owner = SecurityUtil.getCurrentUser();
        Subject subject = subjectRepository.findById(request.getSubjectId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy môn mà bạn muốn import câu hỏi, vui lòng thử lại!"));
        Question question = new Question();
        question.setQuestion(request.getQuestion());
        question.setQuestionType(request.getQuestionType());
        question.setMentor(owner);
        question.setSubject(subject);
        question.setIsShared(request.getIsShared());
        boolean isHasRightQuestion = false;
        for (AddAnswerRequest answerRequest : request.getAnswers()) {
            Answer answer = new Answer();
            answer.setAnswer(answerRequest.getAnswer());
            answer.setKey(answerRequest.getKey());
            answer.setIsRight(answerRequest.getIsRight());
            answer.setQuestion(question);
            question.getAnswers().add(answer);
            // Kiểm tra đã có câu trả lời đúng cho câu hỏi chưa - cho phép nhiều câu đúng
            if (answer.getIsRight() && !isHasRightQuestion) {
                isHasRightQuestion = true;
            }
        }
        if (!isHasRightQuestion) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Bạn chưa chọn câu trả lời chính xác cho câu hỏi!");
        }
        questionRepository.save(question);
        return true;
    }

    @Override
    public ApiPage<QuestionDto> getQuestions(QuestionFilter filter, Pageable pageable) {
        Subject subject = subjectRepository.findById(filter.getSubjectId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy môn mà bạn muốn import câu hỏi, vui lòng thử lại!"));
        Page<Question> subjectPages = questionRepository.findAllBySubject(subject, pageable);
        return PageUtil.convert(subjectPages.map(ConvertUtil::convertQuestionToQuestionDto));
    }

    @Override
    public QuestionDto getQuestion(Long id) {
        Question question = questionRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy câu hỏi bạn muốn chỉnh sửa"));
        return ConvertUtil.convertQuestionToQuestionDto(question);
    }


    @Override
    public Boolean editQuestionToQuestionBank(Long id, EditQuestionRequest request) {
        Question question = questionRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy câu hỏi bạn muốn chỉnh sửa"));
        Optional<User> currentUserOptional = Optional.ofNullable(SecurityUtil.getCurrentUser());
        boolean isValidToEditQuestion = false;
        if (currentUserOptional.isPresent()) {
            User user = currentUserOptional.get();
            isValidToEditQuestion = user.getRoles().stream().anyMatch(role -> Objects.equals(role.getCode(), EUserRole.MANAGER)) || Objects.equals(user.getId(), question.getMentor().getId());
        }

        if (isValidToEditQuestion) {
            QuestionType questionType = request.getQuestionType();
            switch (questionType) {
                case SINGLE:
                    return editSingleChoiceQuestionBank(question, request);
                case MULTIPLE:
                    return editMultipleChoiceQuestionBank(question, request);
                default:
                    throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Không có loại câu hỏi, vui lòng thêm vào!");
            }
        } else {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Bạn không có quyền sửa đối với câu hỏi này!");
        }
    }

    private Boolean editSingleChoiceQuestionBank(Question question, EditQuestionRequest request) {
        Subject subject = subjectRepository.findById(request.getSubjectId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy môn mà bạn muốn import câu hỏi, vui lòng thử lại!"));
        question.setQuestion(request.getQuestion());
        question.setQuestionType(request.getQuestionType());
        question.setSubject(subject);
        question.setIsShared(request.getIsShared());
        List<Answer> existedAnswers = question.getAnswers();
        Map<Long, Answer> existedAnswerMap = existedAnswers.stream().collect(Collectors.toMap(Answer::getId, Function.identity()));
        boolean isHasRightQuestion = false;
        List<Long> editedExistedAnswerIds = new ArrayList<>();
        for (EditAnswerRequest answerRequest : request.getAnswers()) {
            Answer answer = null;
            if (answerRequest.getId() != null) {
                answer = Optional.ofNullable(existedAnswerMap.get(answerRequest.getId())).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Câu trả lời sửa đổi không thuộc câu hỏi này!"));
                answer.setAnswer(answerRequest.getAnswer());
                answer.setKey(answerRequest.getKey());
                answer.setIsRight(answerRequest.getIsRight());
                editedExistedAnswerIds.add(answer.getId());
            } else {
                answer = new Answer();
                answer.setAnswer(answerRequest.getAnswer());
                answer.setKey(answerRequest.getKey());
                answer.setIsRight(answerRequest.getIsRight());
                answer.setQuestion(question);
                existedAnswers.add(answer);
            }
            // Kiểm tra đã có câu trả lời đúng cho câu hỏi chưa - chỉ cho phép duy nhất 1 câu trả lời đúng
            if (answer.getIsRight() && !isHasRightQuestion) {
                isHasRightQuestion = true;
            } else if (answer.getIsRight() && isHasRightQuestion) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Không thể có 2 câu hỏi đúng trong loại SINGLE");
            }

        }
        if (!isHasRightQuestion) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Bạn chưa chọn câu trả lời chính xác cho câu hỏi!");
        }
        List<Answer> unusedAnswer = existedAnswers.stream().filter(answer -> !editedExistedAnswerIds.contains(answer.getId()) && answer.getId() != null).collect(Collectors.toList());
        existedAnswers.removeAll(unusedAnswer);
        questionRepository.save(question);
        return true;
    }


    private Boolean editMultipleChoiceQuestionBank(Question question, EditQuestionRequest request) {
        Subject subject = subjectRepository.findById(request.getSubjectId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy môn mà bạn muốn import câu hỏi, vui lòng thử lại!"));
        question.setQuestion(request.getQuestion());
        question.setQuestionType(request.getQuestionType());
        question.setSubject(subject);
        question.setIsShared(request.getIsShared());
        List<Answer> existedAnswers = question.getAnswers();
        Map<Long, Answer> existedAnswerMap = existedAnswers.stream().collect(Collectors.toMap(Answer::getId, Function.identity()));
        boolean isHasRightQuestion = false;
        List<Long> editedExistedAnswerIds = new ArrayList<>();
        for (EditAnswerRequest answerRequest : request.getAnswers()) {
            Answer answer = null;
            if (answerRequest.getId() != null) {
                answer = Optional.ofNullable(existedAnswerMap.get(answerRequest.getId())).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Câu trả lời sửa đổi không thuộc câu hỏi này!"));
                answer.setAnswer(answerRequest.getAnswer());
                answer.setKey(answerRequest.getKey());
                answer.setIsRight(answerRequest.getIsRight());
                editedExistedAnswerIds.add(answer.getId());
            } else {
                answer = new Answer();
                answer.setAnswer(answerRequest.getAnswer());
                answer.setKey(answerRequest.getKey());
                answer.setIsRight(answerRequest.getIsRight());
                answer.setQuestion(question);
                existedAnswers.add(answer);
            }
            // Kiểm tra đã có câu trả lời đúng cho câu hỏi chưa - chỉ cho phép duy nhất 1 câu trả lời đúng
            if (answer.getIsRight() && !isHasRightQuestion) {
                isHasRightQuestion = true;
            }
        }
        if (!isHasRightQuestion) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Bạn chưa chọn câu trả lời chính xác cho câu hỏi!");
        }
        List<Answer> unusedAnswer = existedAnswers.stream().filter(answer -> !editedExistedAnswerIds.contains(answer.getId()) && answer.getId() != null).collect(Collectors.toList());
        existedAnswers.removeAll(unusedAnswer);
        questionRepository.save(question);
        return true;
    }
}
