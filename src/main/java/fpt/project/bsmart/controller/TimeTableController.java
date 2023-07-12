package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.common.ValidationErrorsException;
import fpt.project.bsmart.entity.request.timetable.GenerateScheduleRequest;
import fpt.project.bsmart.entity.request.timetable.MentorCreateScheduleRequest;
import fpt.project.bsmart.entity.response.timetable.GenerateScheduleResponse;
import fpt.project.bsmart.service.ITimeTableService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/time-table")
public class TimeTableController {

    private final ITimeTableService timeTableService ;

    public TimeTableController(ITimeTableService timeTableService) {
        this.timeTableService = timeTableService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ApiResponse<List<GenerateScheduleResponse>>> generateScheduleForClass (@Valid @RequestBody GenerateScheduleRequest request) {
        return ResponseEntity.ok(ApiResponse.success(timeTableService.generateScheduleForClass(request)));
    }

    @PostMapping("/{classId}")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ApiResponse<Boolean>> mentorCreateScheduleForClass (@PathVariable Long classId , @Valid @RequestBody List<MentorCreateScheduleRequest> request) throws ValidationErrorsException {
        return ResponseEntity.ok(ApiResponse.success(timeTableService.mentorCreateScheduleForClass(classId ,request)));
    }
}
