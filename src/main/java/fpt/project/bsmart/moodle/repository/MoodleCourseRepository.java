package fpt.project.bsmart.moodle.repository;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import fpt.project.bsmart.moodle.config.Caller;
import fpt.project.bsmart.moodle.request.*;
import fpt.project.bsmart.moodle.response.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MoodleCourseRepository extends MoodleBaseRepository {


    public MoodleCourseRepository(Caller caller) {
        super(caller);
    }

    public List<MoodleCourseResponse> createCourse(S1CourseRequest request) throws JsonProcessingException {
        TypeReference<List<MoodleCourseResponse>> typeReference = new TypeReference<List<MoodleCourseResponse>>() {
        };
        return caller.post(getCreateCourseUrl(), request, typeReference);
    }


    public List<MoodleCourseResponse> getCourses(MoodleMasterDataRequest request) throws JsonProcessingException {
        TypeReference<List<MoodleCourseResponse>> typeReference = new TypeReference<List<MoodleCourseResponse>>() {
        };
        return caller.get(getGetCourseUrl(), request, typeReference);
    }


    public List<MoodleCategoryResponse> getCategories(GetCategoryRequest request) throws JsonProcessingException {
        TypeReference<List<MoodleCategoryResponse>> typeReference = new TypeReference<List<MoodleCategoryResponse>>() {
        };
        return caller.get(getGetCategoryUrl(), request, typeReference);
    }


    public List<MoodleCourseResponse> createCategory(CreateCategoryRequest request) throws JsonProcessingException {
        TypeReference<List<MoodleCourseResponse>> typeReference = new TypeReference<List<MoodleCourseResponse>>() {
        };
        return caller.post(getCreateCategoryUrl(), request, typeReference);
    }


    public List<MoodleSectionResponse> getResourceCourse(GetMoodleCourseRequest classId) throws JsonProcessingException {
        TypeReference<List<MoodleSectionResponse>> typeReference = new TypeReference<List<MoodleSectionResponse>>() {
        };
        return caller.post(getGetResourceUrl(), classId, typeReference);
    }

    public List<MoodleAssignmentsResponse> getAssignmentsResourceCourse(GetMoodleAssignmentIdsCourseRequest  instanceId) throws JsonProcessingException {
        TypeReference<List<MoodleAssignmentsResponse>> typeReference = new TypeReference<List<MoodleAssignmentsResponse>>() {
        };
        return caller.post(getGetAssignments(), instanceId, typeReference);
    }

    public String enrolUser(CreateEnrolCourseRequest request) throws JsonProcessingException {
        TypeReference<String> typeReference = new TypeReference<String>() {
        };
        return caller.post(getEnrolUserUrl(), request, typeReference);
    }

    public String unenrolUser(CreateEnrolCourseRequest request) throws JsonProcessingException {
        TypeReference<String> typeReference = new TypeReference<String>() {
        };
        return caller.post(getUnenrolUserUrl(), request, typeReference);
    }

    public List<MoodleCourseResponse> deleteCourse(DeleteMoodleCourseRequest request) throws JsonProcessingException {
        TypeReference<List<MoodleCourseResponse>> typeReference = new TypeReference<List<MoodleCourseResponse>>() {
        };
        return caller.post(getDeleteCourse(), request, typeReference);
    }

    public CourseGradeResponse getCourseGrade(GetCourseGradeRequest request) throws JsonProcessingException {
        TypeReference<CourseGradeResponse> typeReference = new TypeReference<CourseGradeResponse>() {
        };
        return caller.post(getGetCourseGrade(), request, typeReference);
    }
}
