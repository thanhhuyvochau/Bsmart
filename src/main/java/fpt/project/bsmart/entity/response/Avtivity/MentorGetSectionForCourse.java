package fpt.project.bsmart.entity.response.Avtivity;

import fpt.project.bsmart.entity.request.activity.MentorCreateLessonForCourse;

import java.util.List;

public class MentorGetSectionForCourse {
    
    private Long id ;
    private String name;

    private List<MentorGetLessonForCourse> lessons;


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

    public List<MentorGetLessonForCourse> getLessons() {
        return lessons;
    }

    public void setLessons(List<MentorGetLessonForCourse> lessons) {
        this.lessons = lessons;
    }
}
