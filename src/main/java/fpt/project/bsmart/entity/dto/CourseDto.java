package fpt.project.bsmart.entity.dto;


import fpt.project.bsmart.entity.constant.ECourseLevel;
import fpt.project.bsmart.entity.constant.ECourseStatus;


public class CourseDto {

    private Long id;


    private ECourseStatus status;

    private ECourseLevel level;

    private Double referenceDiscount = 0.0;

    private SubjectDto subject;

    private Long mentorId;

    private UserDto mentor;


    private ImageDto image;


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

    public Long getMentorId() {
        return mentorId;
    }

    public void setMentorId(Long mentorId) {
        this.mentorId = mentorId;
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


}
