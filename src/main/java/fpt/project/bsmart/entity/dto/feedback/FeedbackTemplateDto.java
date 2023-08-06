package fpt.project.bsmart.entity.dto.feedback;

import fpt.project.bsmart.entity.constant.EFeedbackAnswerType;
import fpt.project.bsmart.entity.constant.EFeedbackType;

import java.util.ArrayList;
import java.util.List;

public class FeedbackTemplateDto {
    private Long id;
    private String name;
    private EFeedbackType type;

    private int totalClassUsed = 0 ;
    private Boolean isDefault = false;
    private Boolean isFixed = false;
    private List<FeedbackQuestionDto> questions = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public int getTotalClassUsed() {
        return totalClassUsed;
    }

    public void setTotalClassUsed(int totalClassUsed) {
        this.totalClassUsed = totalClassUsed;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Boolean getFixed() {
        return isFixed;
    }

    public void setFixed(Boolean fixed) {
        isFixed = fixed;
    }

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

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Boolean getIsFixed() {
        return isFixed;
    }

    public void setIsFixed(Boolean fixed) {
        isFixed = fixed;
    }

    public List<FeedbackQuestionDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<FeedbackQuestionDto> questions) {
        this.questions = questions;
    }

    public static class FeedbackQuestionDto {

        private Long id;
        private String question;

        private EFeedbackAnswerType answerType;
        private List<FeedbackAnswerDto> answers = new ArrayList<>();

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public EFeedbackAnswerType getAnswerType() {
            return answerType;
        }

        public void setAnswerType(EFeedbackAnswerType answerType) {
            this.answerType = answerType;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public List<FeedbackAnswerDto> getAnswers() {
            return answers;
        }

        public void setAnswers(List<FeedbackAnswerDto> answers) {
            this.answers = answers;
        }
    }

    public static class FeedbackAnswerDto {

        private Long id;
        private String answer;


        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }
    }
}
