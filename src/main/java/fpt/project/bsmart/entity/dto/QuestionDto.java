package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.entity.Answer;
import fpt.project.bsmart.entity.BaseEntity;
import fpt.project.bsmart.entity.constant.QuestionType;

import java.util.ArrayList;
import java.util.List;


public class QuestionDto extends BaseEntity {
    private Long id;
    private String mentorName;
    private String question;
    private QuestionType questionType;
    private Integer numberUsed = 0;
    private Boolean isShared = false;
    private Boolean isDeleted = false;
    private List<AnswerDto> answers = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMentorName() {
        return mentorName;
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public Integer getNumberUsed() {
        return numberUsed;
    }

    public void setNumberUsed(Integer numberUsed) {
        this.numberUsed = numberUsed;
    }

    public Boolean getIsShared() {
        return isShared;
    }

    public void setIsShared(Boolean shared) {
        isShared = shared;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public List<AnswerDto> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDto> answers) {
        this.answers = answers;
    }
}
