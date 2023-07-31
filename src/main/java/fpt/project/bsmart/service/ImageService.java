package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.dto.ImageDto;
import fpt.project.bsmart.entity.request.ImageRequest;

import java.util.List;


public interface ImageService {
    ImageDto uploadImage(ImageRequest ImageRequest);

    ImageDto uploadImageForClass(ImageRequest ImageRequest);


    List<ImageDto> getAllImage();

    ImageDto getImageById(Long id);

    public List<ImageDto> uploadDegree(List<ImageRequest> imageRequests);
}
