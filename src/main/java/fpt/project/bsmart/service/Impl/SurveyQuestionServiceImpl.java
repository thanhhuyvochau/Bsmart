package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Account;
import fpt.project.bsmart.entity.Answer;
import fpt.project.bsmart.entity.StudentAnswer;
import fpt.project.bsmart.entity.SurveyQuestion;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.EQuestionType;
import fpt.project.bsmart.entity.dto.AnswerDto;
import fpt.project.bsmart.entity.dto.SurveyQuestionAnswerDto;
import fpt.project.bsmart.entity.request.StudentSurveyRequest;
import fpt.project.bsmart.entity.request.SurveyQuestionRequest;
import fpt.project.bsmart.repository.AccountRepository;
import fpt.project.bsmart.repository.AnswerRepository;
import fpt.project.bsmart.repository.StudentAnswerRepository;
import fpt.project.bsmart.repository.SurveyQuestionRepository;
import fpt.project.bsmart.service.ISurveyQuestionService;
import fpt.project.bsmart.util.MessageUtil;
import fpt.project.bsmart.util.ObjectUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SurveyQuestionServiceImpl implements ISurveyQuestionService {
    private final AccountRepository accountRepository;

    private final MessageUtil messageUtil;
    private final SurveyQuestionRepository surveyQuestionRepository;

    private final AnswerRepository answerRepository;

    private final StudentAnswerRepository studentAnswerRepository;


    public SurveyQuestionServiceImpl(AccountRepository accountRepository, MessageUtil messageUtil, SurveyQuestionRepository surveyQuestionRepository, AnswerRepository answerRepository, StudentAnswerRepository studentAnswerRepository) {
        this.accountRepository = accountRepository;
        this.messageUtil = messageUtil;
        this.surveyQuestionRepository = surveyQuestionRepository;
        this.answerRepository = answerRepository;
        this.studentAnswerRepository = studentAnswerRepository;
    }

    @Override
    public Boolean adminCreateSurveyQuestion(List<SurveyQuestionRequest> surveyQuestionRequest) {
        List<SurveyQuestion> surveyQuestionRequestList = new ArrayList<>();
        for (SurveyQuestionRequest question : surveyQuestionRequest) {
            SurveyQuestion surveyQuestion = new SurveyQuestion();
            surveyQuestion.setQuestion(question.getQuestion());
            surveyQuestion.setVisible(question.getVisible());
            List<Answer> answerList = new ArrayList<>();
            if (question.getQuestionType().equals(EQuestionType.MULTIPLE_CHOICE)) {

                for (AnswerDto answerMultipleChoice : question.getAnswersMultipleChoice()) {
                    Answer answer = new Answer();
                    answer.setAnswer(answerMultipleChoice.getAnswer());
                    answer.setSurveyQuestion(surveyQuestion);
                    answer.setVisible(answerMultipleChoice.isVisible());
                    answerList.add(answer);
                }
                surveyQuestion.setQuestionType(EQuestionType.MULTIPLE_CHOICE);
            } else {
                surveyQuestion.setQuestionType(EQuestionType.ESSAY);
            }
            surveyQuestion.setAnswers(answerList);
            surveyQuestion.setQuestionType(question.getQuestionType());

            surveyQuestionRequestList.add(surveyQuestion);
        }
        surveyQuestionRepository.saveAll(surveyQuestionRequestList);

        return true;
    }

    @Override
    public Boolean studentSubmitSurvey(Long studentId, List<StudentSurveyRequest> studentSurveyRequests) {
        Account account = accountRepository.findById(studentId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("Khong tim thay account") + studentId));

        List<SurveyQuestion> allSurveyQuestion = surveyQuestionRepository.findAll();

        List<StudentAnswer> studentAnswerList = new ArrayList<>();
        for (StudentSurveyRequest studentSurveyRequest : studentSurveyRequests) {
            SurveyQuestion survey = allSurveyQuestion.stream().filter(eachSurveyQuestion -> eachSurveyQuestion.getId().equals(studentSurveyRequest.getQuestionId())).findFirst()
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                            .withMessage(messageUtil.getLocalMessage("Survey Question khong tim thay") + studentSurveyRequest.getQuestionId()));

            StudentAnswer studentAnswer = new StudentAnswer();
            studentAnswer.setStudent(account);
            studentAnswer.setSurveyQuestion(survey);
            if (survey.getQuestionType().equals(EQuestionType.ESSAY)) {
                studentAnswer.setOpenAnswer(studentSurveyRequest.getEssayAnswer());
            } else {
                Answer answer = answerRepository.findByIdAndSurveyQuestion(studentSurveyRequest.getAnswerId(), survey)
                        .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("Khong tim thay answer") + studentId));
                studentAnswer.setAnswer(answer);
            }
            studentAnswerList.add(studentAnswer);

        }
        if (studentAnswerList.size() < allSurveyQuestion.size()) {
            throw ApiException.create(HttpStatus.METHOD_NOT_ALLOWED).withMessage(messageUtil.getLocalMessage("Bạn chưa khảo sát hết cau hỏi"));
        }

        // Lưu db to suggest subject


        studentAnswerRepository.saveAll(studentAnswerList);
        return true;
    }

    @Override
    public List<SurveyQuestionAnswerDto> listSurveyQuestion() {
        List<SurveyQuestion> allSurveyQuestion = surveyQuestionRepository.findAll();
        List<SurveyQuestionAnswerDto> questionAnswerDtoList = new ArrayList<>();
        allSurveyQuestion.stream().map(surveyQuestion -> {
            SurveyQuestionAnswerDto surveyQuestionAnswer = new SurveyQuestionAnswerDto();
            surveyQuestionAnswer.setQuestionId(surveyQuestion.getId());
            surveyQuestionAnswer.setQuestion(surveyQuestion.getQuestion());


            List<AnswerDto> answerDtoList = new ArrayList<>() ;
            List<Answer> answers = surveyQuestion.getAnswers();
                answers.stream().map(answer -> {
                    AnswerDto answerDto = ObjectUtil.copyProperties(answer , new AnswerDto() , AnswerDto.class) ;
                    answerDtoList.add(answerDto) ;
                    return answerDto ;
                }).collect(Collectors.toList());
            surveyQuestionAnswer.setAnswersMultipleChoice(answerDtoList);
            surveyQuestionAnswer.setQuestionType(surveyQuestion.getQuestionType());
            questionAnswerDtoList.add(surveyQuestionAnswer);
            return surveyQuestion ;
         }).collect(Collectors.toList());

        return questionAnswerDtoList;
    }


}
