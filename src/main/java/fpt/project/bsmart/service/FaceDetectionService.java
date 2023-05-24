package fpt.project.bsmart.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FaceDetectionService {
    File detectAndCropFace(MultipartFile imageFile) throws IOException;
}
