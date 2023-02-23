package fpt.project.bsmart.controller;


import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.InfoFindTutorDto;
import fpt.project.bsmart.entity.response.InfoFindTutorResponse;
import fpt.project.bsmart.service.IInfoFindTutorService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register-find-tutor")
public class InfoFindTutorController {

    private final IInfoFindTutorService iInfoFindTutorService;

    public InfoFindTutorController(IInfoFindTutorService iInfoFindTutorService) {
        this.iInfoFindTutorService = iInfoFindTutorService;
    }

    @Operation(summary = "dang-ky-tim-gia-su")
    @PostMapping
    public ResponseEntity<ApiResponse<Boolean>> registerFindTutor(@Nullable @RequestBody InfoFindTutorDto infoFindTutorDto) {
        return ResponseEntity.ok(ApiResponse.success(iInfoFindTutorService.registerFindTutor(infoFindTutorDto)));
    }

    @Operation(summary = "admin lấy form  dang-ky-tim-gia-su")
    @GetMapping
    public ResponseEntity<ApiResponse<ApiPage<InfoFindTutorResponse>>> getAllRegisterFindTutor(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iInfoFindTutorService.getAllRegisterFindTutor(pageable)));
    }
}
