package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.dto.ResourceDto;
import fpt.project.bsmart.entity.request.UploadAvatarRequest;
import fpt.project.bsmart.service.IResourceService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/resource")
public class ResourceController {
    private final IResourceService iResourceService ;

    public ResourceController(IResourceService iResourceService) {
        this.iResourceService = iResourceService;
    }

    @Operation(summary = "Cập nhật hồ sơ học sinh")
    @PostMapping ("/upload-file")
    public ResponseEntity<ResourceDto> uploadFile(@ModelAttribute UploadAvatarRequest uploadFile) {
        return ResponseEntity.ok(iResourceService.uploadFile( uploadFile));
    }
}
