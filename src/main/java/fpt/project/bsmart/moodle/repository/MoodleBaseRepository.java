package fpt.project.bsmart.moodle.repository;

import fpt.project.bsmart.moodle.config.Caller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MoodleBaseRepository {
    @Value("${moodle.url.base}")
    private String baseUrl;

    @Value("${moodle.url.category.get}")
    private String getCategoryUrl;

    @Value("${moodle.url.resource.get}")
    private String getResourceUrl;

    @Value("${moodle.url.category.create}")
    private String createCategoryUrl;

    @Value("${moodle.url.course.create}")
    private String createCourseUrl;
    @Value("${moodle.url.course.get}")
    private String getCourseUrl;
    @Value("${moodle.url.course.content.get}")
    private String getCourseContentUrl;
    @Value("${moodle.url.role.get}")
    private String getRolesUrl;

    @Value("${moodle.url.course.member.enrol}")
    private String enrolUserUrl;
    @Value("${moodle.url.course.member.unenrol}")
    private String unenrolUserUrl;
    @Value("${moodle.url.user.get}")
    private String getUserUrl;

    @Value("${moodle.url.course.delete}")
    private String deleteCourse;
    @Value("${moodle.url.course.grade.get}")
    private String getCourseGrade;

    @Value("${moodle.url.assignments.get}")
    private String getAssignments;

    @Value("${moodle.url.root}")
    private String rootUrl;

    protected final Caller caller;

    public MoodleBaseRepository(Caller caller) {
        this.caller = caller;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Caller getCaller() {
        return caller;
    }

    public String getGetResourceUrl() {
        return baseUrl + getResourceUrl;
    }

    public void setGetResourceUrl(String getResourceUrl) {
        this.getResourceUrl = getResourceUrl;
    }

    public String getCreateCourseUrl() {
        return baseUrl + createCourseUrl;
    }

    public void setCreateCourseUrl(String createCourseUrl) {
        this.createCourseUrl = createCourseUrl;
    }

    public String getGetCategoryUrl() {
        return baseUrl + getCategoryUrl;
    }

    public void setGetCategoryUrl(String getCategoryUrl) {
        this.getCategoryUrl = getCategoryUrl;
    }

    public String getCreateCategoryUrl() {
        return baseUrl + createCategoryUrl;
    }

    public void setCreateCategoryUrl(String createCategoryUrl) {
        this.createCategoryUrl = createCategoryUrl;
    }

    public String getGetCourseUrl() {
        return baseUrl + getCourseUrl;
    }

    public void setGetCourseUrl(String getCourseUrl) {
        this.getCourseUrl = getCourseUrl;
    }

    public String getGetCourseContentUrl() {
        return baseUrl + getCourseContentUrl;
    }

    public void setGetCourseContentUrl(String getCourseContentUrl) {
        this.getCourseContentUrl = getCourseContentUrl;
    }

    public String getGetRolesUrl() {
        return baseUrl + getRolesUrl;
    }

    public void setGetRolesUrl(String getRolesUrl) {
        this.getRolesUrl = getRolesUrl;
    }

    public String getEnrolUserUrl() {
        return baseUrl + enrolUserUrl;
    }

    public void setEnrolUserUrl(String enrolUserUrl) {
        this.enrolUserUrl = enrolUserUrl;
    }

    public String getUnenrolUserUrl() {
        return baseUrl + unenrolUserUrl;
    }

    public void setUnenrolUserUrl(String unenrolUserUrl) {
        this.unenrolUserUrl = unenrolUserUrl;
    }

    public String getGetUserUrl() {
        return baseUrl + getUserUrl;
    }

    public void setGetUserUrl(String getUserUrl) {
        this.getUserUrl = getUserUrl;
    }

    public String getDeleteCourse() {
        return baseUrl + deleteCourse;
    }

    public void setDeleteCourse(String deleteCourse) {
        this.deleteCourse = deleteCourse;
    }

    public String getGetCourseGrade() {
        return baseUrl + getCourseGrade;
    }

    public void setGetCourseGrade(String getCourseGrade) {
        this.getCourseGrade = getCourseGrade;
    }

    public String getGetAssignments() {
        return baseUrl+getAssignments;
    }

    public void setGetAssignments(String getAssignments) {
        this.getAssignments = getAssignments;
    }

    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }
}