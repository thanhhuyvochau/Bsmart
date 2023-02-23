package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.entity.common.EForumType;
import fpt.project.bsmart.entity.response.SubjectSimpleResponse;

import java.util.ArrayList;
import java.util.List;


public class ForumDto {

    private Long id;

    private String name;

    private String code;


    private EForumType type;


    private SubjectSimpleResponse subject;

    private List<ForumLessonDto> forumLessonDtos = new ArrayList<>();

    private ClassDto aClass;
    private List<QuestionSimpleDto> questions = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public EForumType getType() {
        return type;
    }

    public void setType(EForumType type) {
        this.type = type;
    }

    public SubjectSimpleResponse getSubject() {
        return subject;
    }

    public void setSubject(SubjectSimpleResponse subject) {
        this.subject = subject;
    }

    public ClassDto getaClass() {
        return aClass;
    }

    public void setaClass(ClassDto aClass) {
        this.aClass = aClass;
    }

    public List<QuestionSimpleDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionSimpleDto> questions) {
        this.questions = questions;
    }

    public List<ForumLessonDto> getForumLessonDtos() {
        return forumLessonDtos;
    }

    public void setForumLessonDtos(List<ForumLessonDto> forumLessonDtos) {
        this.forumLessonDtos = forumLessonDtos;
    }
}
