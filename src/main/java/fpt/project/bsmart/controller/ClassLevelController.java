package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.request.ClassLevelRequest;
import fpt.project.bsmart.entity.response.ClassLevelResponse;
import fpt.project.bsmart.service.IClassLevelService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/class-levels")
public class ClassLevelController {
    private final IClassLevelService IClassLevelService;

    public ClassLevelController(IClassLevelService IClassLevelService) {
        this.IClassLevelService = IClassLevelService;
    }

    @Operation(description = "Lấy tất cả loại lớp")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ClassLevelResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(IClassLevelService.getAll()));
    }
    @Operation(description = "Tạo loại lớp")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('MANAGER','ROOT')")
    public ResponseEntity<ApiResponse<ClassLevelResponse>> create(@RequestBody ClassLevelRequest classLevelRequest) {
        return ResponseEntity.ok(ApiResponse.success(IClassLevelService.create(classLevelRequest)));
    }
    @Operation(description = "Cập nhật lọai lớp")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER','ROOT')")
    public ResponseEntity<ApiResponse<ClassLevelResponse>> update(@RequestBody ClassLevelRequest classLevelRequest, @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(IClassLevelService.update(classLevelRequest, id)));
    }
    @Operation(description = "Xóa loại lớp")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER','ROOT')")
    public ResponseEntity<ApiResponse<Boolean>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(IClassLevelService.delete(id)));
    }
}
