package fpt.project.bsmart.controller;


import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.DayOfWeekDTO;
import fpt.project.bsmart.service.IDayOfWeekService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/day-of-week")
public class DayOfWeekController {
    private final IDayOfWeekService dayOfWeekService;

    public DayOfWeekController(IDayOfWeekService dayOfWeekService) {
        this.dayOfWeekService = dayOfWeekService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DayOfWeekDTO>>> getAllDaysOfWeek() {
        List<DayOfWeekDTO> dayOfWeeks = dayOfWeekService.getAllDaysOfWeek();
        return ResponseEntity.ok(ApiResponse.success(dayOfWeeks));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DayOfWeekDTO>> getDayOfWeekById(@PathVariable Long id) {
        DayOfWeekDTO dayOfWeek = dayOfWeekService.getDayOfWeekById(id);
        if (dayOfWeek == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ApiResponse.success(dayOfWeek));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DayOfWeekDTO>> createDayOfWeek(@Valid @RequestBody DayOfWeekDTO request) {
        DayOfWeekDTO createdDayOfWeek = dayOfWeekService.createDayOfWeek(request);
        return ResponseEntity.ok(ApiResponse.success(createdDayOfWeek));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DayOfWeekDTO>> updateDayOfWeek(@PathVariable Long id,@Valid @RequestBody DayOfWeekDTO request) {
        DayOfWeekDTO updatedDayOfWeek = dayOfWeekService.updateDayOfWeek(id, request);
        if (updatedDayOfWeek == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ApiResponse.success(updatedDayOfWeek));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteDayOfWeek(@PathVariable Long id) {
        dayOfWeekService.deleteDayOfWeek(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}