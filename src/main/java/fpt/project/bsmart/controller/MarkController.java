package fpt.project.bsmart.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.entity.response.MarkResponse;
import fpt.project.bsmart.service.IMarkService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mark")
public class MarkController {

    private final IMarkService markService;

    public MarkController(IMarkService markService) {
        this.markService = markService;
    }


    @Operation(summary = "Lấy điểm của học sinh trong lớp")
    @GetMapping({"/{studentId}"})
    @PreAuthorize("hasAnyAuthority('MANAGER','STUDENT','TEACHER')")
    public ResponseEntity<ApiResponse<List<MarkResponse>>> getClassRequesting(@RequestParam Long classId, @PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success(markService.getStudentMark(classId, studentId)));
    }
    @Operation(summary = "Đồng bộ điểm của học sinh")
    @GetMapping({"/synchronize"})
    public ResponseEntity<ApiResponse<Boolean>> synchronizeMarks() throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(markService.synchronizeMark()));
    }

}
