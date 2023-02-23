package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.SurveyQuestionAnswerDto;
import fpt.project.bsmart.entity.request.StudentSurveyRequest;
import fpt.project.bsmart.entity.request.SurveyQuestionRequest;
import fpt.project.bsmart.service.ISurveyQuestionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/survey-question")
public class SurveyQuestionController {
    private final ISurveyQuestionService iSurveyQuestionService ;

    public SurveyQuestionController(ISurveyQuestionService iSurveyQuestionService) {
        this.iSurveyQuestionService = iSurveyQuestionService;
    }


    @Operation(summary = "Admin tạo bộ câu hỏi khảo sát cho hoc sinh")
    @PostMapping("/create-survey-question")
    public ResponseEntity<ApiResponse<Boolean>> adminCreateSurveyQuestion(@RequestBody List<SurveyQuestionRequest> surveyQuestionRequest) {
        return ResponseEntity.ok(ApiResponse.success(iSurveyQuestionService.adminCreateSurveyQuestion(surveyQuestionRequest)));
    }

    @Operation(summary = "Hoc sinh  khảo sát bộ câu hỏi")
    @PostMapping("{studentId}/student-submit-survey")
    public ResponseEntity<ApiResponse<Boolean>> studentSubmitSurvey(@PathVariable Long studentId, @RequestBody List<StudentSurveyRequest> studentSurveyRequests) {
        return ResponseEntity.ok(ApiResponse.success(iSurveyQuestionService.studentSubmitSurvey(studentId,studentSurveyRequests)));
    }

    @Operation(summary = "Danh sách câu hỏi khảo sát cho học sinh")
    @GetMapping
    public ResponseEntity<ApiResponse<List<SurveyQuestionAnswerDto>>> listSurveyQuestion () {
        return ResponseEntity.ok(ApiResponse.success(iSurveyQuestionService.listSurveyQuestion()));
    }

}
