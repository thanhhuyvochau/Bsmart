package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Pano;
import fpt.project.bsmart.entity.Resource;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.EResourceType;
import fpt.project.bsmart.entity.dto.PanoDto;
import fpt.project.bsmart.entity.request.PanoRequest;
import fpt.project.bsmart.entity.response.GetPanoResponse;
import fpt.project.bsmart.repository.PanoRepository;
import fpt.project.bsmart.repository.ResourceRepository;
import fpt.project.bsmart.service.PanoService;
import fpt.project.bsmart.util.MessageUtil;
import fpt.project.bsmart.util.PageUtil;
import fpt.project.bsmart.util.RequestUrlUtil;
import fpt.project.bsmart.util.adapter.MinioAdapter;
import io.minio.ObjectWriteResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class PanoServiceImpl implements PanoService {

    private final ResourceRepository resourceRepository;

    private final MessageUtil messageUtil;

    private final PanoRepository panoRepository;

    private final MinioAdapter minioAdapter;
    @Value("${minio.url}")
    String minioUrl;

    public PanoServiceImpl(ResourceRepository resourceRepository, MessageUtil messageUtil, PanoRepository panoRepository, MinioAdapter minioAdapter) {
        this.resourceRepository = resourceRepository;
        this.messageUtil = messageUtil;
        this.panoRepository = panoRepository;
        this.minioAdapter = minioAdapter;
    }

    @Override
    public GetPanoResponse createNewPano(PanoRequest panoRequest) {
        Pano pano = new Pano();
        if (panoRepository.existsByName(panoRequest.getName())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Name đã tồn tại"));
        }
        int value = panoRequest.getPublishDate().compareTo(panoRequest.getExpirationDate());
        if (value > 0 || value == 0) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Ngay publish không thể lớn hơn ngày hết hạn"));
        }
        pano.setName(panoRequest.getName());
        pano.setPublishDate(panoRequest.getPublishDate());
        pano.setExpirationDate(panoRequest.getExpirationDate());


        try {
            String name = panoRequest.getFile().getOriginalFilename() + "-" + Instant.now().toString();
            ObjectWriteResponse objectWriteResponse = minioAdapter.uploadFile(name, panoRequest.getFile().getContentType(),
                    panoRequest.getFile().getInputStream(), panoRequest.getFile().getSize());

            Resource resource = new Resource();
            resource.setName(name);
            resource.setUrl(RequestUrlUtil.buildUrl(minioUrl, objectWriteResponse));
            resource.setResourceType(EResourceType.PANO);
            resourceRepository.save(resource);
            pano.setLinkUrl(RequestUrlUtil.buildUrl(minioUrl, objectWriteResponse));
            pano.setResource(resource);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        pano.setVisible(panoRequest.getVisible());


        Pano panoSaved = panoRepository.save(pano);


        return convertPanoToPanoResponse(panoSaved);
    }

    @Override
    public ApiPage<GetPanoResponse> getAllPano(Pageable pageable) {
        Page<Pano> allPano = panoRepository.findAll(pageable);
        return PageUtil.convert(allPano.map(PanoServiceImpl::convertPanoToPanoResponse
        ));

    }

    @Override
    public List<GetPanoResponse> getPanos() {
        List<Pano> all = panoRepository.findAll();
        List<GetPanoResponse> panoResponses = new ArrayList<>();
        all.forEach(pano -> {
            panoResponses.add(convertPanoToPanoResponse(pano));
        });
        return panoResponses;
    }

    public static GetPanoResponse convertPanoToPanoResponse(Pano pano) {
        GetPanoResponse getPanoResponse = new GetPanoResponse();
        getPanoResponse.setId(pano.getId());
        getPanoResponse.setName(pano.getName());
        getPanoResponse.setPublishDate(pano.getPublishDate());
        getPanoResponse.setExpirationDate(pano.getExpirationDate());
        getPanoResponse.setVisible(pano.getVisible());
        getPanoResponse.setLinkUrl(pano.getLinkUrl());
        getPanoResponse.setStatus(getStatus(pano));
        return getPanoResponse;
    }

    private static String getStatus(Pano pano) {
        Instant now = Instant.now();
        Instant expirationDate = pano.getExpirationDate();
        Instant publishDate = pano.getPublishDate();
        if (publishDate != null && now.isBefore(publishDate)) {
            return "WAITING_PUBLISH";
        }
        if (expirationDate != null && now.isAfter(expirationDate)) {
            return "EXPIRED";
        }
        if (publishDate != null && expirationDate != null) {
            if (now.isAfter(publishDate) && now.isBefore(expirationDate)) {
                return "PUBLISHING";
            }
        }
        return null;
    }

    @Override
    public Long deletePano(Long panoId) {
        Pano pano = panoRepository.findById(panoId).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                .withMessage(messageUtil.getLocalMessage("Không tìm thấy Pano") + panoId));
        panoRepository.delete(pano);
        return panoId;
    }

    @Override
    public GetPanoResponse updatePano(Long id, PanoDto panoDto) {
        Pano oldPano = panoRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("Không tìm thấy Pano") + id));
        oldPano.setName(panoDto.getName());

        if (panoRepository.existsByName(panoDto.getName()) && !oldPano.getName().equals(panoDto.getName())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Name đã tồn tại"));
        }

        if (panoDto.getPublishDate() != null) {
            if (panoDto.getExpirationDate() == null) {
                if (panoDto.getPublishDate().compareTo(oldPano.getExpirationDate()) >= 0)
                    throw ApiException.create(HttpStatus.BAD_REQUEST)
                            .withMessage(messageUtil.getLocalMessage("Ngay publish không thể lớn hơn ngày hết hạn"));
            }
            oldPano.setPublishDate(panoDto.getPublishDate());
        }
        if (panoDto.getExpirationDate() != null) {
            if (panoDto.getPublishDate() == null) {
                if (panoDto.getExpirationDate().compareTo(oldPano.getPublishDate()) <= 0)
                    throw ApiException.create(HttpStatus.BAD_REQUEST)
                            .withMessage(messageUtil.getLocalMessage("Ngay hết không thể nhỏ hơn ngày công bố"));
            }
            oldPano.setExpirationDate(panoDto.getExpirationDate());
        }


        if (panoDto.getPublishDate() != null && panoDto.getExpirationDate() != null) {
            int value = panoDto.getPublishDate().compareTo(panoDto.getExpirationDate());
            if (value > 0 || value == 0) {
                throw ApiException.create(HttpStatus.BAD_REQUEST)
                        .withMessage(messageUtil.getLocalMessage("Ngay publish không thể lớn hơn ngày hết hạn"));
            }
        }

        if (panoDto.getFile() != null) {
            try {
                String name = panoDto.getFile().getOriginalFilename() + "-" + Instant.now().toString();
                ObjectWriteResponse objectWriteResponse = minioAdapter.uploadFile(name, panoDto.getFile().getContentType(),
                        panoDto.getFile().getInputStream(), panoDto.getFile().getSize());

                Resource resource = new Resource();
                resource.setName(name);
                resource.setUrl(RequestUrlUtil.buildUrl(minioUrl, objectWriteResponse));
                resource.setResourceType(EResourceType.PANO);
                resourceRepository.save(resource);
                oldPano.setLinkUrl(RequestUrlUtil.buildUrl(minioUrl, objectWriteResponse));
                oldPano.setResource(resource);


            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        if (panoDto.getVisible() != null) {
            oldPano.setVisible(panoDto.getVisible());
        }

        Pano newPano = panoRepository.save(oldPano);
        return convertPanoToPanoResponse(newPano);

    }

}

