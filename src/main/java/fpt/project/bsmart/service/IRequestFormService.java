package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.dto.RequestFormDto;
import fpt.project.bsmart.entity.dto.RequestFormReplyDto;
import fpt.project.bsmart.entity.dto.RequestTypeDto;
import fpt.project.bsmart.entity.request.RequestFormSearchRequest;
import fpt.project.bsmart.entity.response.RequestFormReplyResponse;
import fpt.project.bsmart.entity.response.RequestFormResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRequestFormService {

    RequestFormResponse uploadRequestForm(RequestFormDto requestFormDto);

    ApiPage<RequestFormResponse> getAllRequestForm(Pageable pageable);

    RequestFormResponse getRequestDetail(Long id);

    List<RequestTypeDto> getRequestType();

    ApiPage<RequestFormResponse> searchRequestForm(RequestFormSearchRequest searchRequestForm, Pageable pageable);

    RequestFormReplyResponse replyRequest(Long id , RequestFormReplyDto requestFormReplyDto );
}
