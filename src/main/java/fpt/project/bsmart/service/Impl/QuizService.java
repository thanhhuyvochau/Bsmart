package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.repository.ActivityRepository;
import fpt.project.bsmart.repository.QuizRepository;
import fpt.project.bsmart.service.IQuizService;
import fpt.project.bsmart.util.*;
import org.springframework.security.crypto.password.PasswordEncoder;

public class QuizService implements IQuizService {
    private final MessageUtil messageUtil;
    private final PasswordEncoder encoder;
    private final ActivityRepository activityRepository;
    private final QuizRepository quizRepository;

    public QuizService(MessageUtil messageUtil, PasswordEncoder encoder, ActivityRepository activityRepository, QuizRepository quizRepository) {
        this.messageUtil = messageUtil;
        this.encoder = encoder;
        this.activityRepository = activityRepository;
        this.quizRepository = quizRepository;
    }


    private Long studentSubmitQuiz(SubmittedQuestionRequest request){
        return null;
    }


}
