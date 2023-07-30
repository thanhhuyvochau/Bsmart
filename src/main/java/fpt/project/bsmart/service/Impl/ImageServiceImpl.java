package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.EImageType;
import fpt.project.bsmart.entity.dto.ImageDto;
import fpt.project.bsmart.entity.request.ImageRequest;
import fpt.project.bsmart.repository.ClassImageRepository;
import fpt.project.bsmart.repository.ImageRepository;
import fpt.project.bsmart.repository.UserImageRepository;
import fpt.project.bsmart.service.ImageService;
import fpt.project.bsmart.util.MessageUtil;
import fpt.project.bsmart.util.ObjectUtil;
import fpt.project.bsmart.util.SecurityUtil;
import fpt.project.bsmart.util.UrlUtil;
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

import static fpt.project.bsmart.util.Constants.ErrorMessage.IMAGE_NOT_FOUND_BY_ID;

@Service
@Transactional
public class ImageServiceImpl implements ImageService {

    @Value("${minio.endpoint}")
    String minioUrl;

    private final ImageRepository imageRepository;

    private final ClassImageRepository classImageRepository;

    private final UserImageRepository userImageRepository;

    private final MinioAdapter minioAdapter;
    private final MessageUtil messageUtil;

    public ImageServiceImpl(ImageRepository imageRepository, ClassImageRepository classImageRepository, UserImageRepository userImageRepository, MinioAdapter minioAdapter, MessageUtil messageUtil) {
        this.imageRepository = imageRepository;
        this.classImageRepository = classImageRepository;
        this.userImageRepository = userImageRepository;

        this.minioAdapter = minioAdapter;
        this.messageUtil = messageUtil;
    }

    @Override
    public ImageDto uploadImage(ImageRequest imageRequest) {
        try {
            MultipartFile file = imageRequest.getFile();
            String name = file.getOriginalFilename() + "_" + Instant.now().toString();
            ObjectWriteResponse objectWriteResponse = minioAdapter.uploadFile(name, file.getContentType(), file.getInputStream(), file.getSize());
            Image image = new Image();
            image.setName(objectWriteResponse.object());
            image.setUrl(UrlUtil.buildUrl(minioUrl, objectWriteResponse));
            Image persistedImage = imageRepository.save(image);
            image.setType(imageRequest.getType());
            return ObjectUtil.copyProperties(persistedImage, new ImageDto(), ImageDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ImageDto uploadImageForClass(ImageRequest imageRequest) {
        try {
            MultipartFile file = imageRequest.getFile();
            String name = file.getOriginalFilename() + "_" + Instant.now().toString();
            ObjectWriteResponse objectWriteResponse = minioAdapter.uploadFile(name, file.getContentType(), file.getInputStream(), file.getSize());
            ClassImage image = new ClassImage();
            image.setName(objectWriteResponse.object());
            image.setUrl(UrlUtil.buildUrl(minioUrl, objectWriteResponse));
            image.setStatus(true);
            ClassImage persistedImage = classImageRepository.save(image);

            image.setType(EImageType.CLASS);
            return ObjectUtil.copyProperties(persistedImage, new ImageDto(), ImageDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<ImageDto> getAllImage() {
        return imageRepository.findAll().stream().map(image -> ObjectUtil.copyProperties(image, new ImageDto(), ImageDto.class)).collect(Collectors.toList());
    }

    @Override
    public ImageDto getImageById(Long id) {
        Image image = imageRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(IMAGE_NOT_FOUND_BY_ID) + id));
        return ObjectUtil.copyProperties(image, new ImageDto(), ImageDto.class);
    }

    @Override
    public ImageDto uploadDegree(ImageRequest imageRequest) {
        User currentUserAccountLogin = SecurityUtil.getCurrentUser();
        MentorProfile mentorProfile = currentUserAccountLogin.getMentorProfile();
        try {
            MultipartFile file = imageRequest.getFile();
            String name = file.getOriginalFilename() + "_" + Instant.now().toString();
            ObjectWriteResponse objectWriteResponse = minioAdapter.uploadFile(name, file.getContentType(), file.getInputStream(), file.getSize());
            UserImage image = new UserImage();
            image.setName(objectWriteResponse.object());
            image.setType(EImageType.DEGREE);
            image.setStatus(false);
            image.setVerified(false);
            image.setUrl(UrlUtil.buildUrl(minioUrl, objectWriteResponse));
            image.setUser(currentUserAccountLogin);
            UserImage save = userImageRepository.save(image);


            return ObjectUtil.copyProperties(save, new ImageDto(), ImageDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
