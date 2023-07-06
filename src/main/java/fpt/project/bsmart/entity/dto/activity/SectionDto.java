package fpt.project.bsmart.entity.dto.activity;

import fpt.project.bsmart.entity.request.activity.MentorCreateLessonForCourse;

import java.util.List;

public class SectionDto {

    private String name;

    private List<MentorCreateLessonForCourse> lessons;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MentorCreateLessonForCourse> getLessons() {
        return lessons;
    }

    public void setLessons(List<MentorCreateLessonForCourse> lessons) {
        this.lessons = lessons;
    }
}
