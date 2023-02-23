package fpt.project.bsmart.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.moodle.response.MoodleAllClassRecourseDtoResponse;
import fpt.project.bsmart.moodle.response.MoodleRecourseClassesDtoResponse;
import fpt.project.bsmart.moodle.response.MoodleRecourseDtoResponse;
import fpt.project.bsmart.service.IExerciseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

private final IExerciseService iExerciseService ;

    public ExerciseController(IExerciseService iExerciseService) {
        this.iExerciseService = iExerciseService;
    }


    @Operation(summary = "hoc sinh xem chi tiết resource (bai tập) của lớp")
    @GetMapping("/{classId}/student")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<ApiResponse<List<MoodleRecourseDtoResponse>>> studentGetExerciseInClass(@PathVariable Long classId  ) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(iExerciseService.getExerciseInClass(classId)));
    }

    @Operation(summary = "Giao vien xem chi tiết resource (bai tập) của lớp")
    @GetMapping("/{classId}/teacher")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ApiResponse<List<MoodleRecourseDtoResponse>>> teacherGetExerciseInClass(@PathVariable Long classId) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(iExerciseService.teacherGetExerciseInClass(classId)));
    }

    @Operation(summary = "hoc sinh xem tất cả bài tập ở tất cả lớp")
    @GetMapping("student/classes")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<ApiResponse<ApiPage<MoodleRecourseClassesDtoResponse>>> studentGetAllExerciseAllClass(Pageable pageable) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(iExerciseService.studentGetAllExerciseAllClass(pageable)));
    }


    @Operation(summary = "giao viên xem tất cả bài tập ở tất cả lớp")
    @GetMapping("teacher/classes")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ApiResponse<ApiPage<MoodleAllClassRecourseDtoResponse>>> teacherGetAllExerciseAllClass(Pageable pageable) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(iExerciseService.teacherGetAllExerciseAllClass(pageable)));
    }

    @Operation(summary = "giao viên lấy tất cả học sinh và bài tập đã nộp trong lớp")
    @GetMapping("submit/{instanceId}/students")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ApiResponse<List<Long>>> teacherGetAllSubmitStudent(@PathVariable Long instanceId , Long classId ) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(iExerciseService.teacherGetAllSubmitStudent(instanceId, classId)));
    }


}
