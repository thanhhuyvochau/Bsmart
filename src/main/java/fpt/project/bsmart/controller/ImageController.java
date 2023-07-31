package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.ImageDto;
import fpt.project.bsmart.entity.request.ImageRequest;
import fpt.project.bsmart.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<ImageDto>> uploadImage(@ModelAttribute ImageRequest ImageRequest) {
        return ResponseEntity.ok(ApiResponse.success(imageService.uploadImage(ImageRequest)));
    }

    @PostMapping("/class/upload")
    public ResponseEntity<ApiResponse<ImageDto>> uploadImageForClass(@ModelAttribute ImageRequest ImageRequest) {
        return ResponseEntity.ok(ApiResponse.success(imageService.uploadImageForClass(ImageRequest)));
    }

    @PreAuthorize("hasAnyRole('TEACHER')")
    @PostMapping("/upload/degree")
    public ResponseEntity<ApiResponse<ImageDto>> uploadDegree(@ModelAttribute ImageRequest ImageRequest) {
        return ResponseEntity.ok(ApiResponse.success(imageService.uploadDegree(ImageRequest)));
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<ImageDto>>> getAllImage() {
        return ResponseEntity.ok(ApiResponse.success(imageService.getAllImage()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ImageDto>> getImageById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(imageService.getImageById(id)));
    }
}
