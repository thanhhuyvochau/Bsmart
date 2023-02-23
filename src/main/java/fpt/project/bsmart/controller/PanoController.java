package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.PanoDto;
import fpt.project.bsmart.entity.request.PanoRequest;
import fpt.project.bsmart.entity.response.GetPanoResponse;
import fpt.project.bsmart.service.PanoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/pano")
public class PanoController {

    private final PanoService panoService;


    public PanoController(PanoService panoService) {
        this.panoService = panoService;
    }

    @Operation(summary = "Tạo mới Pano")
    @PostMapping
    public ResponseEntity<ApiResponse<GetPanoResponse>> createNewPano(@Valid @ModelAttribute PanoRequest panoRequest) {
        return ResponseEntity.ok(ApiResponse.success(panoService.createNewPano(panoRequest), "Add Successful Pano"));
    }

    @GetMapping
    @Operation(summary = "lấy tất cả các pano")
    public ResponseEntity<ApiResponse<ApiPage<GetPanoResponse>>> getAllPano(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(panoService.getAllPano(pageable)));
    }

    @GetMapping("/panos")
    @Operation(summary = "lấy tất cả các pano khong phang trang")
    public ResponseEntity<ApiResponse<List<GetPanoResponse>>> getPanos() {
        return ResponseEntity.ok(ApiResponse.success(panoService.getPanos()));
    }

    @Operation(summary = "Xóa Pano bằng  Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> deleteRole(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(panoService.deletePano(id)));
    }

    @Operation(summary = "Sửa Pano bằng id ")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GetPanoResponse>> updatePano(@PathVariable Long id, @Nullable @ModelAttribute PanoDto panoDto) {
        return ResponseEntity.ok(ApiResponse.success(panoService.updatePano(id, panoDto), "Pano has been successfully updated with id :" + id));
    }


}
