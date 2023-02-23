package fpt.project.bsmart.service;


import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.dto.InfoFindTutorDto;
import fpt.project.bsmart.entity.response.InfoFindTutorResponse;
import org.springframework.data.domain.Pageable;

public interface IInfoFindTutorService {


    Boolean registerFindTutor(InfoFindTutorDto infoFindTutorDto);

    ApiPage<InfoFindTutorResponse> getAllRegisterFindTutor(Pageable pageable);
}
