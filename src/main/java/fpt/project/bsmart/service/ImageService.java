package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.dto.ImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface ImageService {
    ImageDto uploadImage(MultipartFile file);

    List<ImageDto> getAllImage();

    ImageDto getImageById(Long id);
}
