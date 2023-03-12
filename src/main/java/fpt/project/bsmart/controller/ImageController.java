package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.ImageDto;
import fpt.project.bsmart.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<ImageDto>> uploadImage(@ModelAttribute("file") MultipartFile file) {
        return ResponseEntity.ok(ApiResponse.success(imageService.uploadImage(file)));
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
