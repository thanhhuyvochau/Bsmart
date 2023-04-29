package fpt.project.bsmart.controller;


import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.ClassProgressTimeDto;
import fpt.project.bsmart.entity.request.category.CreateClassRequest;
import fpt.project.bsmart.service.IClassService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/classes")
public class ClassController {

    private final IClassService iClassService;

    public ClassController(IClassService iClassService) {
        this.iClassService = iClassService;
    }

    @Operation(summary = "admin duyệt mở lớp cho mentor => tự generate tkb - attendance ")
    @PostMapping
    public ResponseEntity<ApiResponse<Boolean>> adminApproveClass(@Valid @RequestBody CreateClassRequest request) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.createClass(request)));
    }

    @Operation(summary = "Lấy tiến trình của một lớp")
    @GetMapping("/progression")
    public ResponseEntity<ApiResponse<ClassProgressTimeDto>> getClassProgression(@Valid @RequestParam Long classId) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.getClassProgression(classId)));
    }


}
