package fpt.project.bsmart.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;


public class CourseResponse {

    @Schema(description = "id course")
    private Long id;

    @Schema(description = "hình đại diện course")
    private String image;

    @Schema(description = "title của course")
    private String courseTitle;

    @Schema(description = "Têncủa course")
    private String courseName;

//    @Schema(description = "Giá tiền của course")
//    private BigDecimal unitPriceCourse;
//
//    @Schema(description = "Giá tiền của course")
//    private BigDecimal finalPriceCourse;

    private SubjectSimpleResponse subject ;


    private Long totalClass ;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }



//    public BigDecimal getUnitPriceCourse() {
//        return unitPriceCourse;
//    }
//
//    public void setUnitPriceCourse(BigDecimal unitPriceCourse) {
//        this.unitPriceCourse = unitPriceCourse;
//    }
//
//    public BigDecimal getFinalPriceCourse() {
//        return finalPriceCourse;
//    }
//
//    public void setFinalPriceCourse(BigDecimal finalPriceCourse) {
//        this.finalPriceCourse = finalPriceCourse;
//    }


    public SubjectSimpleResponse getSubject() {
        return subject;
    }

    public void setSubject(SubjectSimpleResponse subject) {
        this.subject = subject;
    }

    public Long getTotalClass() {
        return totalClass;
    }

    public void setTotalClass(Long totalClass) {
        this.totalClass = totalClass;
    }
}
