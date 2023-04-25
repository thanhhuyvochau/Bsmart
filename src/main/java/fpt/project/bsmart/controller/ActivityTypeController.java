package fpt.project.bsmart.controller;


import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.ActivityTypeDto;
import fpt.project.bsmart.service.ActivityTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/activity-types")
public class ActivityTypeController {


    private final ActivityTypeService activityTypeService;

    public ActivityTypeController(ActivityTypeService activityTypeService) {
        this.activityTypeService = activityTypeService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ActivityTypeDto>>> getAllActivityTypes() {


        return ResponseEntity.ok(ApiResponse.success(activityTypeService.getAllActivityTypes()));
    }
}
