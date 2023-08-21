package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.response.ReferralCodeResponse;

public interface ReferralCodeService {
    ReferralCodeResponse getReferralCodeToApplyDiscount(String code, Long courseId);
}
