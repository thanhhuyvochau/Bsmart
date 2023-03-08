package fpt.project.bsmart.controller;


import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.request.CreateClassRequest;
import fpt.project.bsmart.entity.request.category.CategoryRequest;
import fpt.project.bsmart.service.IClassService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "admin tao class")
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> adminCreateClass(@Valid @RequestBody CreateClassRequest createClassRequest) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.adminCreateClass(createClassRequest)));
    }


}
