package fpt.project.bsmart.controller;


import fpt.project.bsmart.service.FaceDetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/face-detection/")
public class FaceDetectionController {
    @Autowired
    private FaceDetectionService faceDetectionService;

    @PostMapping("/detect-face")
    public ResponseEntity<String> detectAndCropFace(@RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        File croppedFace = faceDetectionService.detectAndCropFace(imageFile);
        if (croppedFace == null) {
            return ResponseEntity.badRequest().body("No face detected in the input image");
        }
        return ResponseEntity.badRequest().body("successful");

    }
}