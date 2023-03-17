package fpt.project.bsmart.entity.response;


import fpt.project.bsmart.entity.constant.ECourseLevel;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.dto.ImageDto;
import fpt.project.bsmart.entity.dto.SubjectDto;
import fpt.project.bsmart.entity.dto.UserDto;

import java.util.ArrayList;
import java.util.List;


public class CourseCartResponse {

    private Long id;

    private ECourseStatus status;

    private ECourseLevel level;

    private Double referenceDiscount = 0.0;

    private SubjectDto subject;

    private UserDto mentor;

    private ImageDto image;

    private List<SubCourseCartResponse> subCourses = new ArrayList<>();

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

    public Double getReferenceDiscount() {
        return referenceDiscount;
    }

    public void setReferenceDiscount(Double referenceDiscount) {
        this.referenceDiscount = referenceDiscount;
    }

    public SubjectDto getSubject() {
        return subject;
    }

    public void setSubject(SubjectDto subject) {
        this.subject = subject;
    }

    public UserDto getMentor() {
        return mentor;
    }

    public void setMentor(UserDto mentor) {
        this.mentor = mentor;
    }

    public ImageDto getImage() {
        return image;
    }

    public void setImage(ImageDto image) {
        this.image = image;
    }

    public List<SubCourseCartResponse> getSubCourses() {
        return subCourses;
    }

    public void setSubCourses(List<SubCourseCartResponse> subCourses) {
        this.subCourses = subCourses;
    }
}