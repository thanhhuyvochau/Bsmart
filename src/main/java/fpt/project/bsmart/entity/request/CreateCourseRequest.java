package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.entity.constant.ECourseLevel;
import fpt.project.bsmart.entity.constant.EUserRole;

import java.time.Instant;
import java.util.List;

public class CreateCourseRequest {

    private String name;
    private ECourseLevel level;

    private Long categoryId ;

    private Long subjectId;

    private String description;

    private List<CourseSectionRequest> sections  ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ECourseLevel getLevel() {
        return level;
    }

    public void setLevel(ECourseLevel level) {
        this.level = level;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CourseSectionRequest> getSections() {
        return sections;
    }

    public void setSections(List<CourseSectionRequest> sections) {
        this.sections = sections;
    }
}
