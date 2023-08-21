package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.ConfigReferralCode;
import fpt.project.bsmart.entity.OrderDetail;
import fpt.project.bsmart.entity.ReferralCode;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.repository.ConfigReferralCodeRepository;
import fpt.project.bsmart.repository.ReferralCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
@Transactional
public class ReferralCodeUtil {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static ReferralCodeRepository referralCodeRepository;
    private static ConfigReferralCodeRepository configReferralCodeRepository;

    @Autowired
    public ReferralCodeUtil(ReferralCodeRepository referralCodeRepository, ConfigReferralCodeRepository configReferralCodeRepository) {
        ReferralCodeUtil.referralCodeRepository = referralCodeRepository;
        ReferralCodeUtil.configReferralCodeRepository = configReferralCodeRepository;
    }

//    public static boolean checkCourseToGenerateReferral(SubCourse subCourse) {
//        List<OrderDetail> orderDetails = subCourse.getOrderDetails();
//        return orderDetails.size() < subCourse.getNumberReferralCode() && subCourse.isHasReferralCode();
//    }


    public static void generateRandomReferralCode(OrderDetail orderDetail, User owner) {
        String code = generateCode(10);
        Optional<ReferralCode> existReferral;
        List<ConfigReferralCode> activeConfig = configReferralCodeRepository.findByActive(true);
        ConfigReferralCode configReferralCode = null;
        if (activeConfig.size() != 1) {
            throw ApiException.create(HttpStatus.CONFLICT).withMessage("Xảy ra lỗi về cấu hình mã giảm giá, vui lòng liên hệ quản lý");
        } else {
            configReferralCode = activeConfig.get(0);
        }
        do {
            existReferral = referralCodeRepository.findByCode(code);
        } while (existReferral.isPresent());

        ReferralCode referralCode = new ReferralCode();
        referralCode.setCode(code);
        referralCode.setDiscountPercent(configReferralCode.getDiscountPercent());
        referralCode.setUsageLimit(configReferralCode.getUsageLimit());
        referralCode.setUser(owner);
        referralCode.setOrderDetail(orderDetail);
        referralCode.setExpiredAt(Instant.now().plus(configReferralCode.getExpiredLaterDay(), ChronoUnit.DAYS).truncatedTo(ChronoUnit.HOURS));
        configReferralCode.getReferralCodeList().add(referralCode);
        referralCode.setConfigReferralCode(configReferralCode);
        orderDetail.setReferralCode(referralCode);
        referralCodeRepository.save(referralCode);
    }

    public static String generateCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public static Date addDaysToDate(int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, amount);
        return calendar.getTime();
    }
}
