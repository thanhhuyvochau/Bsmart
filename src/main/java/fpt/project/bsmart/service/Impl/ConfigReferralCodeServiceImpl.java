package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.ConfigReferralCode;
import fpt.project.bsmart.entity.request.ConfigReferralCodeRequest;
import fpt.project.bsmart.entity.response.ConfigReferralCodeResponse;
import fpt.project.bsmart.repository.ConfigReferralCodeRepository;
import fpt.project.bsmart.service.ConfigReferralCodeService;
import fpt.project.bsmart.util.ObjectUtil;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ConfigReferralCodeServiceImpl implements ConfigReferralCodeService {
    private final ConfigReferralCodeRepository configReferralCodeRepository;

    public ConfigReferralCodeServiceImpl(ConfigReferralCodeRepository configReferralCodeRepository) {
        this.configReferralCodeRepository = configReferralCodeRepository;
    }

    @Override
    public ConfigReferralCodeResponse getActiveConfig() {
        ConfigReferralCode activeConfig = configReferralCodeRepository.findByActive(true).stream().findFirst().orElse(null);
        if (activeConfig != null) {
            return ObjectUtil.copyProperties(activeConfig, new ConfigReferralCodeResponse(), ConfigReferralCodeResponse.class, true);
        }
        return null;
    }

    @Override
    public ConfigReferralCodeResponse config(ConfigReferralCodeRequest request) {
        configReferralCodeRepository.deactivateAllConfigs();
        ConfigReferralCode config = new ConfigReferralCode();
        config.setUsageLimit(request.getUsageLimit());
        config.setDiscountPercent(request.getDiscountPercent());
        config.setExpiredLaterDay(request.getExpiredLaterDay());
        config.setActive(true);
        ConfigReferralCode savedConfig = configReferralCodeRepository.save(config);
        return ObjectUtil.copyProperties(savedConfig, new ConfigReferralCodeResponse(), ConfigReferralCodeResponse.class, true);
    }
}
