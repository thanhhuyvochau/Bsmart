package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.EAccountDetailStatus;
import fpt.project.bsmart.entity.dto.ResourceDto;
import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.entity.response.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAccountDetailService {


    Long registerTutor(AccountDetailRequest accountDetailRequest);

    List<ResourceDto> uploadImageRegisterProfile(Long id,  UploadAvatarRequest uploadImageRequest);

    ResponseAccountDetailResponse approveRegisterAccount(RequestEditAccountDetailRequest editAccountDetailRequest);

    ApiPage<AccountDetailResponse> getRequestToActiveAccount(EAccountDetailStatus status , Pageable pageable);


    AccountDetailResponse getAccountDetail(Long accountId);

    ResponseAccountDetailResponse teacherGetInfo();

    ResponseAccountDetailResponse requestEditRegisterAccount(RequestEditAccountDetailRequest editAccountDetailReques);

    ResponseAccountDetailResponse refuseRegisterAccount(RequestEditAccountDetailRequest editAccountDetailRequest);

    Long teacherUpdateProfileForAdmin(AccountDetailEditRequest editAccountDetailRequest);
}