package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.request.ConfigReferralCodeRequest;
import fpt.project.bsmart.entity.response.ConfigReferralCodeResponse;

public interface ConfigReferralCodeService {
    ConfigReferralCodeResponse getActiveConfig();
    ConfigReferralCodeResponse config(ConfigReferralCodeRequest request);
}
