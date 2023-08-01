package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.entity.constant.EFeedbackType;

import java.util.ArrayList;
import java.util.List;

public class FeedbackTemplateRequest {
    private String name;
    private EFeedbackType type;
    private List<FeedbackQuestionRequest> questions = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EFeedbackType getType() {
        return type;
    }

    public void setType(EFeedbackType type) {
        this.type = type;
    }

    public List<FeedbackQuestionRequest> getQuestions() {
        return questions;
    }

    public void setQuestions(List<FeedbackQuestionRequest> questions) {
        this.questions = questions;
    }

    public static class FeedbackQuestionRequest {
        private String question;
        private List<FeedbackAnswerRequest> answers = new ArrayList<>();

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public List<FeedbackAnswerRequest> getAnswers() {
            return answers;
        }

        public void setAnswers(List<FeedbackAnswerRequest> answers) {
            this.answers = answers;
        }
    }

    public static class FeedbackAnswerRequest {
        private String answer;

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }
    }
}
