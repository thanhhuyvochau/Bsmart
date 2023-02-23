package fpt.project.bsmart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.entity.response.*;
import fpt.project.bsmart.moodle.response.MoodleSectionResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICourseService {
    TopicsSubjectRequest teacherCreateTopicInSubject(Long teacherId, TopicsSubjectRequest topicsSubjectRequest);

    ClassSubjectResponse createRegisterSubject(Long teacherId, Long subjectId, ClassRequest classRequest);

    ApiPage<CourseResponse> searchCourse(CourseSearchRequest query, Pageable pageable);

    ApiPage<CourseResponse> viewAllCourse(Pageable pageable);

    CourseDetailResponse viewCourseDetail(long courseID) throws JsonProcessingException;

    CourseDetailResponse updateCourse(long courseID, CourseRequest subjectRequest);


    List<ClassCourseResponse> viewHistoryCourse(long studentId);

    ClassCourseResponse studentEnrollCourse(long id, long courseId, long classId);

    List<MoodleSectionResponse> synchronizedResource(Long classId) throws JsonProcessingException;

    CourseResponse createCourse(CourseRequest courseRequest);

    ApiPage<CourseDetailResponse> getCourseBySubject(long subjectId, Pageable pageable);

    List<CourseDetailResponse> getListCourseBySubject(long subjectId);
}
