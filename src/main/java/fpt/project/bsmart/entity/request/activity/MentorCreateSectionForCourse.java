package fpt.project.bsmart.entity.request.activity;

import java.util.List;

public class MentorCreateSectionForCourse {
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
