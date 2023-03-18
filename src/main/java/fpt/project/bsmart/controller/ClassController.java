package fpt.project.bsmart.controller;


import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.request.category.CreateClassRequest;
import fpt.project.bsmart.service.IClassService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/classes")
public class ClassController {

    private final IClassService iClassService;

    public ClassController(IClassService iClassService) {
        this.iClassService = iClassService;
    }

    @Operation(summary = "mentor tao lớp học")
    @PostMapping
    public ResponseEntity<ApiResponse<Boolean>> mentorCreateClass(@Valid @RequestBody CreateClassRequest request) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.createClass(request)));
    }


}
