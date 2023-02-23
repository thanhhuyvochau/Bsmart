package fpt.project.bsmart.service;


import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.dto.PanoDto;
import fpt.project.bsmart.entity.request.PanoRequest;
import fpt.project.bsmart.entity.response.GetPanoResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PanoService {


    GetPanoResponse createNewPano(PanoRequest panoRequest);


    ApiPage<GetPanoResponse> getAllPano(Pageable pageable);

    List<GetPanoResponse> getPanos();

    Long deletePano(Long id);

    GetPanoResponse updatePano(Long id, PanoDto panoDto);
}
