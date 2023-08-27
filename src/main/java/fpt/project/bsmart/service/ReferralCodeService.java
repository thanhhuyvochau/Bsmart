package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.response.ReferralCodeResponse;
import org.springframework.data.domain.Pageable;

public interface ReferralCodeService {
    ReferralCodeResponse getReferralCodeToApplyDiscount(String code, Long courseId);

    ApiPage<ReferralCodeResponse> getAllReferralCodeByCurrentUser(Pageable pageable);
}
