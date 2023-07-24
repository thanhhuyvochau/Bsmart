package fpt.project.bsmart.entity.response.course;

import fpt.project.bsmart.entity.constant.ECourseLevel;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.dto.CategoryDto;
import fpt.project.bsmart.entity.dto.SubjectDto;
import fpt.project.bsmart.entity.dto.mentor.MentorDto;

public class ManagerGetCourse {
    private Long id;
    private String code;
    private String name;

    private String description;

    private ECourseLevel level ;
    private CategoryDto categoryResponse;

    private SubjectDto subjectResponse;


    private ECourseStatus status;


    private MentorDto mentor;

    private Integer totalClass ;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ECourseLevel getLevel() {
        return level;
    }

    public void setLevel(ECourseLevel level) {
        this.level = level;
    }

    public CategoryDto getCategoryResponse() {
        return categoryResponse;
    }

    public void setCategoryResponse(CategoryDto categoryResponse) {
        this.categoryResponse = categoryResponse;
    }

    public SubjectDto getSubjectResponse() {
        return subjectResponse;
    }

    public void setSubjectResponse(SubjectDto subjectResponse) {
        this.subjectResponse = subjectResponse;
    }

    public ECourseStatus getStatus() {
        return status;
    }

    public void setStatus(ECourseStatus status) {
        this.status = status;
    }

    public MentorDto getMentor() {
        return mentor;
    }

    public void setMentor(MentorDto mentor) {
        this.mentor = mentor;
    }

    public Integer getTotalClass() {
        return totalClass;
    }

    public void setTotalClass(Integer totalClass) {
        this.totalClass = totalClass;
    }
}
