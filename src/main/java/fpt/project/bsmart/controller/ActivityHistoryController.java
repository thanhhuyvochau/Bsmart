package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.response.ActivityHistoryResponse;
import fpt.project.bsmart.service.ActivityHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static fpt.project.bsmart.util.Constants.UrlConstants.COMMON_ACTIVITY_HISTORY;
import static fpt.project.bsmart.util.Constants.UrlConstants.COMMON_ROOT;

@RestController
@RequestMapping(COMMON_ROOT + COMMON_ACTIVITY_HISTORY)
public class ActivityHistoryController {


    private final ActivityHistoryService activityHistoryService ;

    public ActivityHistoryController(ActivityHistoryService activityHistoryService) {
        this.activityHistoryService = activityHistoryService;
    }

    @Operation(summary = "lấy lịch sử hoạt động")
    @PreAuthorize("hasAnyRole('TEACHER' , 'STUDENT')")
    @GetMapping
    public ResponseEntity<ApiResponse<ApiPage<ActivityHistoryResponse>>> getHistory(@PageableDefault(sort = "created", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(activityHistoryService.getHistory(pageable)));
    }

}
