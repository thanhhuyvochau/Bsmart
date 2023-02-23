package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.ArchetypeTeacherDto;
import fpt.project.bsmart.service.IArchetypeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/archetype")
public class ArchetypeController {

 private final IArchetypeService iArchetypeService ;


    public ArchetypeController(IArchetypeService iArchetypeService) {
        this.iArchetypeService = iArchetypeService;
    }

    @Operation(summary = "Lấy thời khóa biểu có sẵn của giáo viên ne")
    @GetMapping("/{teacherId}/archetype")
    @PreAuthorize("hasAnyAuthority('MANAGER','TEACHER')")
    public ResponseEntity<ApiResponse<List<ArchetypeTeacherDto>>> getArchetypeOfTeacher(@PathVariable long teacherId) {
        return ResponseEntity.ok(ApiResponse.success(iArchetypeService.getArchetypeOfTeacher(teacherId)));
    }

}
