package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.request.AddActivityRequest;
import fpt.project.bsmart.entity.request.AddAssignmentRequest;
import fpt.project.bsmart.service.IActivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {
    private final IActivityService activityService;

    public ActivityController(IActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Boolean>> addAssignmentActivity(@ModelAttribute AddAssignmentRequest request) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(activityService.addActivity(request)));
    }
}
