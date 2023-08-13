package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Answer;
import fpt.project.bsmart.entity.Question;
import fpt.project.bsmart.entity.Subject;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.constant.EUserRole;
import fpt.project.bsmart.entity.constant.QuestionType;
import fpt.project.bsmart.entity.dto.AnswerDto;
import fpt.project.bsmart.entity.dto.QuestionDto;
import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.repository.QuestionRepository;
import fpt.project.bsmart.repository.SubjectRepository;
import fpt.project.bsmart.service.IQuestionService;
import fpt.project.bsmart.util.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static fpt.project.bsmart.util.Constants.ErrorMessage.*;
import static fpt.project.bsmart.util.Constants.ErrorMessage.Invalid.INVALID_QUESTION_TYPE;

@Service
@Transactional
public class QuestionServiceImpl implements IQuestionService {
    private final QuestionRepository questionRepository;
    private final SubjectRepository subjectRepository;
    private final MessageUtil messageUtil;

    public QuestionServiceImpl(QuestionRepository questionRepository, SubjectRepository subjectRepository, MessageUtil messageUtil) {
        this.questionRepository = questionRepository;
        this.subjectRepository = subjectRepository;
        this.messageUtil = messageUtil;
    }

    @Override
    public Boolean importQuestionToQuestionBank(MultipartFile file, Long subjectId) throws IOException {
        User currentUser = SecurityUtil.getCurrentUser();
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(SUBJECT_NOT_FOUND_BY_ID) + subjectId));
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
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_QUESTION_TYPE));
        }
    }


    private Boolean addSingleChoiceQuestionBank(AddQuestionRequest request) {
        User owner = SecurityUtil.getCurrentUser();
        Subject subject = subjectRepository.findById(request.getSubjectId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(SUBJECT_NOT_FOUND_BY_ID) + request.getSubjectId()));
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
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(MULTIPLE_RIGHT_ANSWER_IN_SINGLE_TYPE_QUESTION));
            }
        }
        if (!isHasRightQuestion) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(MISSING_RIGHT_ANSWER_IN_QUESTION));
        }
        questionRepository.save(question);
        return true;
    }


    private Boolean addMultipleChoiceQuestionBank(AddQuestionRequest request) {
        User owner = SecurityUtil.getCurrentUser();
        Subject subject = subjectRepository.findById(request.getSubjectId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(SUBJECT_NOT_FOUND_BY_ID) + request.getSubjectId()));
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
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(MISSING_RIGHT_ANSWER_IN_QUESTION));
        }
        questionRepository.save(question);
        return true;
    }

    @Override
    public ApiPage<QuestionDto> getQuestions(QuestionFilter filter, Pageable pageable) {
        Subject subject = subjectRepository.findById(filter.getSubjectId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(SUBJECT_NOT_FOUND_BY_ID) + filter.getSubjectId()));
        Page<Question> subjectPages = questionRepository.findAllBySubject(subject, pageable);
        return PageUtil.convert(subjectPages.map(ConvertUtil::convertQuestionToQuestionDto));
    }

    @Override
    public QuestionDto getQuestion(Long id) {
        Question question = questionRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(QUESTION_NOT_FOUND_BY_ID) + id));
        return ConvertUtil.convertQuestionToQuestionDto(question);
    }


    @Override
    public Boolean editQuestionToQuestionBank(Long id, EditQuestionRequest request) {
        Question question = questionRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(QUESTION_NOT_FOUND_BY_ID) + id));
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
                    throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_QUESTION_TYPE));
            }
        } else {
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(FORBIDDEN));
        }
    }

    private Boolean editSingleChoiceQuestionBank(Question question, EditQuestionRequest request) {
        Subject subject = subjectRepository.findById(request.getSubjectId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(SUBJECT_NOT_FOUND_BY_ID) + request.getSubjectId()));
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
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(MULTIPLE_RIGHT_ANSWER_IN_SINGLE_TYPE_QUESTION));
            }

        }
        if (!isHasRightQuestion) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(MISSING_RIGHT_ANSWER_IN_QUESTION));
        }
        List<Answer> unusedAnswer = existedAnswers.stream().filter(answer -> !editedExistedAnswerIds.contains(answer.getId()) && answer.getId() != null).collect(Collectors.toList());
        existedAnswers.removeAll(unusedAnswer);
        questionRepository.save(question);
        return true;
    }


    private Boolean editMultipleChoiceQuestionBank(Question question, EditQuestionRequest request) {
        Subject subject = subjectRepository.findById(request.getSubjectId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(SUBJECT_NOT_FOUND_BY_ID) + request.getSubjectId()));
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
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(MISSING_RIGHT_ANSWER_IN_QUESTION));
        }
        List<Answer> unusedAnswer = existedAnswers.stream().filter(answer -> !editedExistedAnswerIds.contains(answer.getId()) && answer.getId() != null).collect(Collectors.toList());
        existedAnswers.removeAll(unusedAnswer);
        questionRepository.save(question);
        return true;
    }

    @Override
    public List<QuestionDto> readQuestionFromFile(MultipartFile multipartFile) {
        return getQuestionFromExcel(multipartFile);
    }

    private List<QuestionDto> getQuestionFromExcel(MultipartFile file) {
        List<QuestionDto> questions = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook;
            if (file.getOriginalFilename().endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(inputStream);
            } else if (file.getOriginalFilename().endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream);
            } else {
                throw new IllegalArgumentException("Unsupported file format");
            }

            Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet
            Iterator<Row> rowIterator = sheet.iterator();
            int rowIndex = 0;
            while (rowIterator.hasNext()) {
                if (rowIndex == 0) {
                    rowIterator.next();
                    rowIndex++;
                    continue;
                }
                Row row = rowIterator.next();
                boolean isHasRightAnswer = false;
                Cell questionCell = row.getCell(0);
                Cell optionsCell = row.getCell(1);
                Cell answerCell = row.getCell(2);
                Cell typeCell = row.getCell(3); // Assuming column 3 contains question type

                String question = questionCell.getStringCellValue();
                List<String> optionsArray = Arrays.asList(optionsCell.getStringCellValue().split(";"));
                List<String> correctAnswers = Arrays.asList(answerCell.getStringCellValue().split(";"));
                String questionType = typeCell.getStringCellValue(); // "single" or "multiple"
                if (questionType.equals("single") && correctAnswers.size() != 1) {
                    throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Tệp tin câu hỏi có câu hỏi không có đáp án đúng hoặc đáp án không hợp lệ với loại câu hỏi");
                }
                QuestionDto questionDto = new QuestionDto();
                questionDto.setQuestion(question);
                if (questionType.equals("single")) {
                    questionDto.setQuestionType(QuestionType.SINGLE);
                } else if (questionType.equals("multiple")) {
                    questionDto.setQuestionType(QuestionType.MULTIPLE);
                }
                for (String optionAnswer : optionsArray) {
                    AnswerDto answerDto = new AnswerDto();
                    answerDto.setAnswer(optionAnswer);
                    if (correctAnswers.stream().filter(correctAnswer -> correctAnswer.equalsIgnoreCase(optionAnswer)).count() == 1) {
                        answerDto.setIsRight(true);
                        isHasRightAnswer = true;
                    } else {
                        answerDto.setIsRight(false);
                    }
                    questionDto.getAnswers().add(answerDto);
                }
                if (!isHasRightAnswer) {
                    throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Tệp tin câu hỏi có câu hỏi không có đáp án đúng hoặc đáp án không hợp lệ với loại câu hỏi");
                }
                questions.add(questionDto);
                rowIndex++;
            }
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }
}
