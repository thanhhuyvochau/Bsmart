package fpt.project.bsmart.entity.dto;


import fpt.project.bsmart.entity.constant.ECourseLevel;
import fpt.project.bsmart.entity.constant.ECourseStatus;


public class CourseDto {

    private Long id;

    private String code ;
    private String name ;
    private String description ;

//    private ECourseStatus status;

//    private ECourseLevel level;

//    private Double referenceDiscount = 0.0;

    private SubjectDto subject;

//    private Long mentorId;
//
//    private UserDto mentor;


//    private ImageDto image;


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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



//    public ECourseStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(ECourseStatus status) {
//        this.status = status;
//    }



    public SubjectDto getSubject() {
        return subject;
    }

    public void setSubject(SubjectDto subject) {
        this.subject = subject;
    }



//    public ImageDto getImage() {
//        return image;
//    }
//
//    public void setImage(ImageDto image) {
//        this.image = image;
//    }


}
