package fpt.project.bsmart.entity.response;


import fpt.project.bsmart.entity.constant.ETypeLearn;

public class CourseSubCourseResponse {

    private Long subCourseId;

    private Long courseId;
    private String imageUrl;

    private String courseName;
    private String courseCode;

    private String courseDescription;

    private String categoryName;

    private String subjectName;


    private String mentorName;

    private ETypeLearn typeLearn ;


    public Long getSubCourseId() {
        return subCourseId;
    }

    public void setSubCourseId(Long subCourseId) {
        this.subCourseId = subCourseId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getMentorName() {
        return mentorName;
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public ETypeLearn getTypeLearn() {
        return typeLearn;
    }

    public void setTypeLearn(ETypeLearn typeLearn) {
        this.typeLearn = typeLearn;
    }
}
