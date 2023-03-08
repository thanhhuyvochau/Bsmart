package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.TimeInWeekDTO;
import fpt.project.bsmart.entity.request.TimeInWeekRequest;
import fpt.project.bsmart.service.ITimeInWeekService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-in-week")
public class TimeInWeekController {
    private final ITimeInWeekService timeInWeekService;

    public TimeInWeekController(ITimeInWeekService timeInWeekService) {
        this.timeInWeekService = timeInWeekService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TimeInWeekDTO>>> getAllTimeInWeeks(@RequestParam Long clazzId) {
        List<TimeInWeekDTO> timeInWeeks = timeInWeekService.getAllTimeInWeeks(clazzId);
        return ResponseEntity.ok(ApiResponse.success(timeInWeeks));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TimeInWeekDTO>> createTimeInWeek(@RequestBody TimeInWeekRequest request) {
        TimeInWeekDTO timeInWeek = timeInWeekService.createTimeInWeek(request);
        return ResponseEntity.ok(ApiResponse.success(timeInWeek));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TimeInWeekDTO>> updateTimeInWeek(@PathVariable Long id, @RequestBody TimeInWeekRequest request) {
        TimeInWeekDTO timeInWeek = timeInWeekService.updateTimeInWeek(id, request);
        return ResponseEntity.ok(ApiResponse.success(timeInWeek));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteTimeInWeek(@PathVariable Long id) {
        boolean deleted = timeInWeekService.deleteTimeInWeek(id);
        return ResponseEntity.ok(ApiResponse.success(deleted));
    }
}
