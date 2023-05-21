package fpt.project.bsmart.entity.request.feedback;



import fpt.project.bsmart.entity.constant.EQuestionType;

import java.util.HashMap;

public class AddFeedbackQuestionRequest {
    private Question newQuestion;

    public Question getNewQuestion() {
        return newQuestion;
    }

    public void setNewQuestion(Question newQuestion) {
        this.newQuestion = newQuestion;
    }

      public class Question {
        private String question;
        private HashMap<String, Long> possibleAnswer;
        private EQuestionType questionType;

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public HashMap<String, Long> getPossibleAnswer() {
            return possibleAnswer;
        }

        public void setPossibleAnswer(HashMap<String, Long> possibleAnswer) {
            this.possibleAnswer = possibleAnswer;
        }

        public EQuestionType getQuestionType() {
            return questionType;
        }

        public void setQuestionType(EQuestionType questionType) {
            this.questionType = questionType;
        }
    }
}
