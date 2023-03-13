package fpt.project.bsmart.util;


import fpt.project.bsmart.entity.Image;
import fpt.project.bsmart.entity.dto.ImageDto;
import fpt.project.bsmart.entity.request.ImageRequest;
import fpt.project.bsmart.repository.ImageRepository;
import fpt.project.bsmart.util.adapter.MinioAdapter;
import io.minio.ObjectWriteResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;

public class ImageUtil {
    @Value("${minio.endpoint}")
    static String minioUrl;

    private static ImageRepository staticImageRepository;

    private static MinioAdapter staticMinioAdapter;


    public ImageUtil(ImageRepository imageRepository, MinioAdapter minioAdapter) {
        staticImageRepository = imageRepository;
        staticMinioAdapter = minioAdapter;

    }

    public static ImageDto uploadImage(ImageRequest imageRequest) {


        try {
            MultipartFile file = imageRequest.getFile();
            String name = file.getOriginalFilename() + "_" + Instant.now().toString();
            ObjectWriteResponse objectWriteResponse = staticMinioAdapter.uploadFile(name, file.getContentType(), file.getInputStream(), file.getSize());
            Image image = new Image();
            image.setName(objectWriteResponse.object());
            image.setUrl(ImageUrlUtil.buildUrl(minioUrl, objectWriteResponse));
            Image persistedImage = staticImageRepository.save(image);
            image.setType(imageRequest.getType());
            return ObjectUtil.copyProperties(persistedImage, new ImageDto(), ImageDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
