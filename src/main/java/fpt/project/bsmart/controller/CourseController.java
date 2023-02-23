package fpt.project.bsmart.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.entity.response.ClassCourseResponse;
import fpt.project.bsmart.entity.response.ClassSubjectResponse;
import fpt.project.bsmart.entity.response.CourseDetailResponse;
import fpt.project.bsmart.entity.response.CourseResponse;
import fpt.project.bsmart.moodle.response.MoodleSectionResponse;
import fpt.project.bsmart.service.ICourseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/courses")
public class CourseController {


    private final ICourseService courseService;

    public CourseController(ICourseService courseService) {
        this.courseService = courseService;
    }


    @Operation(summary = "giáo viên request các topic cho subject")
    @PostMapping("/{teacherId}/create-topics-subject")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ApiResponse<TopicsSubjectRequest>> teacherCreateTopicInSubject(@PathVariable Long teacherId, @RequestBody TopicsSubjectRequest topicsSubjectRequest) {
        System.out.println(teacherId);
        return ResponseEntity.ok(ApiResponse.success(courseService.teacherCreateTopicInSubject(teacherId, topicsSubjectRequest)));
    }


    @Operation(summary = "Giáo viên đăng ký subject")
    @PostMapping({"/{teacherId}"})
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ApiResponse<ClassSubjectResponse>> createRegisterSubject(@PathVariable Long teacherId, Long subjectId, @RequestBody ClassRequest classRequest) {

        return ResponseEntity.ok(ApiResponse.success(courseService.createRegisterSubject(teacherId, subjectId, classRequest)));
    }

    // MANGER COURSE
    @Operation(summary = "Tìm Kiếm course")
    @GetMapping("/search-cource")
    @PreAuthorize("hasAnyAuthority('STUDENT','MANAGER','TEACHER')")
    public ResponseEntity<ApiResponse<ApiPage<CourseResponse>>> searchCourse(@Nullable CourseSearchRequest query,
                                                                             Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(courseService.searchCourse(query, pageable)));
    }

    @Operation(summary = "Lấy tất cả course ")
    @GetMapping("/get-all-course")
    public ResponseEntity<ApiResponse<ApiPage<CourseResponse>>> viewAllCourse(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(courseService.viewAllCourse(pageable)));
    }

    @Operation(summary = "xem chi tiết course ")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseDetailResponse>> viewSubjectCourse(@PathVariable long id) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(courseService.viewCourseDetail(id)));
    }

    @Operation(summary = "Get resource course từ moodle")
    @GetMapping("get-resource-from-moodle")
    @PreAuthorize("hasAnyAuthority('MANAGER','TEACHER')")
    public ResponseEntity<ApiResponse<List<MoodleSectionResponse>>> synchronizedResource(@RequestParam Long classId) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(courseService.synchronizedResource(classId)));
    }

    @Operation(summary = "sửa course")
    @PutMapping("/{id}/update-course")
    @PreAuthorize("hasAnyAuthority('MANAGER','ROOT')")
    public ResponseEntity<ApiResponse<CourseDetailResponse>> updateCourse(@PathVariable long id, @RequestBody CourseRequest subjectRequest) {
        return ResponseEntity.ok(ApiResponse.success(courseService.updateCourse(id, subjectRequest)));
    }

    @Operation(summary = "Học sinh xem lich sử course")
    @GetMapping("/{studentId}/histoty-course")
    public ResponseEntity<ApiResponse<List<ClassCourseResponse>>> viewHistoryCourse(@PathVariable long studentId) {
        return ResponseEntity.ok(ApiResponse.success(courseService.viewHistoryCourse(studentId)));
    }

    @Operation(summary = "Học sinh enroll vao class - course")
    @PostMapping("/{studentId}/enroll-course")
    public ResponseEntity<ApiResponse<ClassCourseResponse>> studentEnrollCourse(@PathVariable long studentId, long courseId, long classId) {
        return ResponseEntity.ok(ApiResponse.success(courseService.studentEnrollCourse(studentId, courseId, classId)));
    }

    @Operation(description = "Tạo khóa học")
    @PostMapping("/create-course")
    public ResponseEntity<ApiResponse<CourseResponse>> createCourse(@RequestBody CourseRequest courseRequest) {
        return ResponseEntity.ok(ApiResponse.success(courseService.createCourse(courseRequest)));
    }


    @Operation(summary = "lấy course theo subject có phân trang")
    @GetMapping("/{subjectId}/subject")
    public ResponseEntity<ApiResponse<ApiPage<CourseDetailResponse>>> getCourseBySubject(@PathVariable long subjectId , Pageable pageable) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(courseService.getCourseBySubject(subjectId, pageable)));
    }

    @Operation(summary = "lấy course theo subject khong phân trang")

    @GetMapping("/{subjectId}/courses-subject")
    public ResponseEntity<ApiResponse<List<CourseDetailResponse>>> getListCourseBySubject(@PathVariable long subjectId) {
        return ResponseEntity.ok(ApiResponse.success(courseService.getListCourseBySubject(subjectId)));

    }


}
