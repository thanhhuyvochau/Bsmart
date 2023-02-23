package fpt.project.bsmart.service.Impl;


import fpt.project.bsmart.entity.Resource;
import fpt.project.bsmart.entity.common.EResourceType;
import fpt.project.bsmart.entity.dto.ResourceDto;
import fpt.project.bsmart.entity.request.UploadAvatarRequest;
import fpt.project.bsmart.repository.ResourceRepository;
import fpt.project.bsmart.service.IResourceService;
import fpt.project.bsmart.util.ObjectUtil;
import fpt.project.bsmart.util.RequestUrlUtil;
import fpt.project.bsmart.util.adapter.MinioAdapter;
import io.minio.ObjectWriteResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;

@Service
public class ResourceServiceImpl implements IResourceService {
    private final MinioAdapter minioAdapter;
    private final ResourceRepository resourceRepository;
    @Value("${minio.url}")
    String minioUrl;

    public ResourceServiceImpl(MinioAdapter minioAdapter, ResourceRepository resourceRepository) {
        this.minioAdapter = minioAdapter;
        this.resourceRepository = resourceRepository;
    }


    @Override
    public ResourceDto uploadFile(UploadAvatarRequest uploadFile) {


        Resource save;
        try {
            String name = uploadFile.getFile().getOriginalFilename() + "-" + Instant.now().toString();
            ObjectWriteResponse objectWriteResponse = minioAdapter.uploadFile(name, uploadFile.getFile().getContentType(),
                    uploadFile.getFile().getInputStream(), uploadFile.getFile().getSize());

            Resource resource = new Resource();
            resource.setName(name);
            resource.setUrl(RequestUrlUtil.buildUrl(minioUrl, objectWriteResponse));
            resource.setResourceType(EResourceType.CONTRACT);
            save = resourceRepository.save(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ObjectUtil.copyProperties(save, new ResourceDto(), ResourceDto.class);
    }
}
