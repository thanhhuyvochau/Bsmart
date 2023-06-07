package fpt.project.bsmart.entity.response;


import fpt.project.bsmart.entity.constant.ECourseLevel;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.dto.CategoryDto;
import fpt.project.bsmart.entity.dto.ClassDto;
import fpt.project.bsmart.entity.dto.ImageDto;
import fpt.project.bsmart.entity.dto.SubjectDto;

import java.util.ArrayList;
import java.util.List;


public class CourseDetailResponse {

    private Long id;



    private ECourseStatus status;

    private ECourseLevel level;

    private ImageDto image;

    private SubjectDto subject;

    private List<CategoryDto> categoryDtoList = new ArrayList<>();

    private Long mentorId;

    private ClassDto clazz ;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public ECourseStatus getStatus() {
        return status;
    }

    public void setStatus(ECourseStatus status) {
        this.status = status;
    }

    public ECourseLevel getLevel() {
        return level;
    }

    public void setLevel(ECourseLevel level) {
        this.level = level;
    }

    public ImageDto getImage() {
        return image;
    }

    public void setImage(ImageDto image) {
        this.image = image;
    }

    public SubjectDto getSubject() {
        return subject;
    }

    public List<CategoryDto> getCategoryDtoList() {
        return categoryDtoList;
    }

    public void setCategoryDtoList(List<CategoryDto> categoryDtoList) {
        this.categoryDtoList = categoryDtoList;
    }

    public void setSubject(SubjectDto subject) {
        this.subject = subject;
    }

    public Long getMentorId() {
        return mentorId;
    }

    public void setMentorId(Long mentorId) {
        this.mentorId = mentorId;
    }

    public ClassDto getClazz() {
        return clazz;
    }

    public void setClazz(ClassDto clazz) {
        this.clazz = clazz;
    }
}
