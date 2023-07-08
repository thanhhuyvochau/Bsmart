package fpt.project.bsmart.entity.response.Avtivity;

import java.util.List;

public class MentorDeleteSectionForCourse {
    
    private Long id ;
    private List<MentorDeleteLessonForCourse> lessons;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<MentorDeleteLessonForCourse> getLessons() {
        return lessons;
    }

    public void setLessons(List<MentorDeleteLessonForCourse> lessons) {
        this.lessons = lessons;
    }
}
