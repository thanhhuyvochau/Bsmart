package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.dto.ImageDto;
import fpt.project.bsmart.entity.request.ImageRequest;

import java.util.List;


public interface ImageService {
    ImageDto uploadImage( ImageRequest ImageRequest);

    List<ImageDto> getAllImage();

    ImageDto getImageById(Long id);
}
