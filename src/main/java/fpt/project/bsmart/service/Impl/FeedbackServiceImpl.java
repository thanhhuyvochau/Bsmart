package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.EFeedbackType;
import fpt.project.bsmart.entity.constant.EQuestionType;
import fpt.project.bsmart.entity.dto.FeedbackTemplateDto;
import fpt.project.bsmart.entity.request.feedback.AddFeedbackTemplateRequest;
import fpt.project.bsmart.entity.request.feedback.AddQuestionRequest;
import fpt.project.bsmart.entity.request.feedback.SubCourseFeedbackRequest;
import fpt.project.bsmart.repository.*;
import fpt.project.bsmart.service.IFeedbackService;
import fpt.project.bsmart.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackServiceImpl implements IFeedbackService {
    private final MessageUtil messageUtil;
    private final FeedbackQuestionRepository feedbackQuestionRepository;
    private final RoleRepository roleRepository;
    private final FeedbackTemplateRepository feedbackTemplateRepository;
    private final SubCourseFeedbackRepository subCourseFeedbackRepository;

    private final SubCourseRepository subCourseRepository;

    public FeedbackServiceImpl(MessageUtil messageUtil, FeedbackQuestionRepository feedbackQuestionRepository, RoleRepository roleRepository, FeedbackTemplateRepository feedbackTemplateRepository, SubCourseFeedbackRepository subCourseFeedbackRepository, SubCourseRepository subCourseRepository) {
        this.messageUtil = messageUtil;
        this.feedbackQuestionRepository = feedbackQuestionRepository;
        this.roleRepository = roleRepository;
        this.feedbackTemplateRepository = feedbackTemplateRepository;
        this.subCourseFeedbackRepository = subCourseFeedbackRepository;
        this.subCourseRepository = subCourseRepository;
    }

    @Override
    public Long addNewQuestion(AddQuestionRequest addQuestionRequest) {
        if (addQuestionRequest == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
        }

        if (addQuestionRequest.getNewQuestion().getQuestion() == null
                || addQuestionRequest.getNewQuestion().getQuestion().trim().isEmpty()) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
        }

        if (addQuestionRequest.getNewQuestion().getQuestionType() == EQuestionType.MULTIPLE_CHOICE) {
            if (addQuestionRequest.getNewQuestion().getPossibleAnswer().isEmpty()
                    || addQuestionRequest.getNewQuestion().getPossibleAnswer().size() < QuestionUtil.MIN_ANSWER_IN_QUESTION
                    || addQuestionRequest.getNewQuestion().getPossibleAnswer().size() > QuestionUtil.MAX_ANSWER_IN_QUESTION) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
            }
            for (Long score : addQuestionRequest.getNewQuestion().getPossibleAnswer().values()) {
                if (score < QuestionUtil.MIN_QUESTION_SCORE || score > QuestionUtil.MAX_QUESTION_SCORE) {
                    throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
                }
            }
        }

        FeedbackQuestion question = new FeedbackQuestion();
        question.setQuestion(addQuestionRequest.getNewQuestion().getQuestion());
        question.setQuestionType(addQuestionRequest.getNewQuestion().getQuestionType());

        if (addQuestionRequest.getNewQuestion().getQuestionType() == EQuestionType.MULTIPLE_CHOICE) {
            question.setPossibleAnswer(QuestionUtil.convertAnswersToAnswerString(new ArrayList<>(addQuestionRequest.getNewQuestion().getPossibleAnswer().keySet())));
            question.setPossibleScore(QuestionUtil.convertScoresToScoreString(new ArrayList<>(addQuestionRequest.getNewQuestion().getPossibleAnswer().values())));
        }

        return feedbackQuestionRepository.save(question).getId();
    }

    @Override
    public Long addNewFeedbackTemplate(AddFeedbackTemplateRequest addFeedbackTemplateRequest) {
        if (addFeedbackTemplateRequest == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
        }

        if (addFeedbackTemplateRequest.getTemplateName() == null || addFeedbackTemplateRequest.getTemplateName().trim().isEmpty()) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
        }

        if (addFeedbackTemplateRequest.getQuestionList() == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
        }

        if (addFeedbackTemplateRequest.getQuestionList().isEmpty()
                || addFeedbackTemplateRequest.getQuestionList().size() < QuestionUtil.MIN_QUESTION_IN_TEMPLATE
                || addFeedbackTemplateRequest.getQuestionList().size() > QuestionUtil.MAX_QUESTION_IN_TEMPLATE) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("") + addFeedbackTemplateRequest.getQuestionList().size());
        }

        Role permission = roleRepository.findRoleByCode(addFeedbackTemplateRequest.getPermission())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(""));
        Long count = feedbackQuestionRepository.countByIdIn(addFeedbackTemplateRequest.getQuestionList());

        if (count != addFeedbackTemplateRequest.getQuestionList().size()) {
            for (Long id : addFeedbackTemplateRequest.getQuestionList()) {
                if (!feedbackQuestionRepository.existsById(id)) {
                    throw ApiException.create(HttpStatus.NOT_FOUND)
                            .withMessage(messageUtil.getLocalMessage("") + id);
                }
            }
        }

        List<FeedbackQuestion> feedbackQuestionList = feedbackQuestionRepository.findAllById(addFeedbackTemplateRequest.getQuestionList());
        if(!addFeedbackTemplateRequest.getFeedbackType().equals(EFeedbackType.REPORT)){
            long multipleQuestionCount = feedbackQuestionList.stream()
                    .filter(x -> x.getQuestionType().equals(EQuestionType.MULTIPLE_CHOICE))
                    .count();
            if(multipleQuestionCount == 0){
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
            }
        }
        FeedbackTemplate feedbackTemplate = new FeedbackTemplate();
        feedbackTemplate.setQuestions(feedbackQuestionList);
        feedbackTemplate.setFeedbackType(addFeedbackTemplateRequest.getFeedbackType());
        feedbackTemplate.setPermission(permission);

        return feedbackTemplateRepository.save(feedbackTemplate).getId();
    }

    @Override
    public FeedbackTemplateDto getFeedbackTemplateById(Long id) {
        if(id == null){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
        }
        FeedbackTemplate feedbackTemplate = feedbackTemplateRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("") + id));
        return ConvertUtil.convertTemplateToTemplateDto(feedbackTemplate);
    }

    @Override
    public Long addNewSubCourseFeedback(SubCourseFeedbackRequest subCourseFeedbackRequest) {
        User user = SecurityUtil.getCurrentUser();
        if(subCourseFeedbackRequest.getFeedbackType().equals(EFeedbackType.REPORT)){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
        }
        SubCourse subCourse = subCourseRepository.findById(subCourseFeedbackRequest.getSubCourseId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("")));
        FeedbackTemplate feedbackTemplate = feedbackTemplateRepository.findById(subCourseFeedbackRequest.getFeedbackAnswer().getTemplateId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("")));
        //check tiến độ
        //call API check tiến độ
        //
        String feedbackAnswerString = "";
        Long totalScore = 0L;
        int multipleQuestionCount = 0;
        for (int i = 0; i < feedbackTemplate.getQuestions().size(); i++){
            String answer = subCourseFeedbackRequest.getFeedbackAnswer().getAnswer().get(i);
            if(feedbackTemplate.getQuestions().get(i).getQuestionType().equals(EQuestionType.MULTIPLE_CHOICE)){
                if(StringUtil.isNullOrEmpty(answer)){
                    throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
                }
                int answerIndex;
                try{
                    answerIndex = Integer.parseInt(answer);
                }catch (NumberFormatException e){
                    throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
                }
                List<Long> possibleScore = QuestionUtil.convertScoreStringToScoreList(feedbackTemplate.getQuestions().get(i).getPossibleScore());
                totalScore += possibleScore.get(answerIndex);
                multipleQuestionCount++;
            }
            QuestionUtil.addNewAnswerToAnswerString(feedbackAnswerString, answer);
        }

        FeedbackAnswer feedbackAnswer = new FeedbackAnswer();
        feedbackAnswer.setFeedbackUser(user);
        feedbackAnswer.setFeedbackTemplate(feedbackTemplate);
        feedbackAnswer.setAnswer(feedbackAnswerString);

        SubCourseFeedback subCourseFeedback = new SubCourseFeedback();
        subCourseFeedback.setSubCourse(subCourse);
        subCourseFeedback.setFeedbackAnswer(feedbackAnswer);
        subCourseFeedback.setScore(multipleQuestionCount == 0 ? null : (double) (totalScore/multipleQuestionCount));
        subCourseFeedback.setOpinion(subCourseFeedback.getOpinion());
        subCourseFeedback.setFeedbackType(subCourseFeedback.getFeedbackType());
        return null;
    }

}
