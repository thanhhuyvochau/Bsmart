package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Image;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.dto.ImageDto;
import fpt.project.bsmart.entity.request.ImageRequest;
import fpt.project.bsmart.repository.ImageRepository;
import fpt.project.bsmart.service.ImageService;
import fpt.project.bsmart.util.Constants;
import fpt.project.bsmart.util.ImageUrlUtil;
import fpt.project.bsmart.util.MessageUtil;
import fpt.project.bsmart.util.ObjectUtil;
import fpt.project.bsmart.util.adapter.MinioAdapter;
import io.minio.ObjectWriteResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ImageServiceImpl implements ImageService {

    @Value("${minio.endpoint}")
    String minioUrl;

    private final ImageRepository imageRepository;

    private final MinioAdapter minioAdapter;
    private final MessageUtil messageUtil;

    public ImageServiceImpl(ImageRepository imageRepository, MinioAdapter minioAdapter, MessageUtil messageUtil) {
        this.imageRepository = imageRepository;
        this.minioAdapter = minioAdapter;
        this.messageUtil = messageUtil;
    }

    @Override
    public ImageDto uploadImage( ImageRequest imageRequest) {
        try {
            MultipartFile file = imageRequest.getFile();
            String name = file.getOriginalFilename() + "_" + Instant.now().toString();
            ObjectWriteResponse objectWriteResponse = minioAdapter.uploadFile(name, file.getContentType(), file.getInputStream(), file.getSize());
            Image image = new Image();
            image.setName(objectWriteResponse.object());
            image.setUrl(ImageUrlUtil.buildUrl(minioUrl, objectWriteResponse));
            Image persistedImage = imageRepository.save(image);
            image.setType(imageRequest.getType());
            return ObjectUtil.copyProperties(persistedImage, new ImageDto(), ImageDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ImageDto> getAllImage() {
        return imageRepository.findAll().stream().map(image -> ObjectUtil.copyProperties(image, new ImageDto(), ImageDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ImageDto getImageById(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("not found" + id));
        return ObjectUtil.copyProperties(image, new ImageDto(), ImageDto.class);
    }
}
