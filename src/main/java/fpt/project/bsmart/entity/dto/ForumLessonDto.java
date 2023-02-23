package fpt.project.bsmart.entity.dto;

import java.util.List;


public class ForumLessonDto {

    private Long id;

    private String lessonName;

    private List<QuestionSimpleDto> questions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public List<QuestionSimpleDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionSimpleDto> questions) {
        this.questions = questions;
    }
}
