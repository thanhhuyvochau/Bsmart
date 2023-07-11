package fpt.project.bsmart.entity.request.activity;

import java.util.List;

public class MentorCreateSectionForCourse {
    private String name;

    private List<LessonDto> lessons;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LessonDto> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonDto> lessons) {
        this.lessons = lessons;
    }
}
