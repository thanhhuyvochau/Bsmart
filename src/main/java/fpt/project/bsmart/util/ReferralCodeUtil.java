package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.OrderDetail;
import fpt.project.bsmart.entity.ReferralCode;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.repository.ReferralCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Component
public class ReferralCodeUtil {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static ReferralCodeRepository referralCodeRepository;

    @Autowired
    public static void setReferralCodeRepository(ReferralCodeRepository referralCodeRepository) {
        ReferralCodeUtil.referralCodeRepository = referralCodeRepository;
    }

//    public static boolean checkCourseToGenerateReferral(SubCourse subCourse) {
//        List<OrderDetail> orderDetails = subCourse.getOrderDetails();
//        return orderDetails.size() < subCourse.getNumberReferralCode() && subCourse.isHasReferralCode();
//    }


    public static void generateRandomReferralCode(OrderDetail orderDetail, User owner) {
        String code = generateCode();
        Optional<ReferralCode> existReferral = Optional.empty();
        do {
            existReferral = referralCodeRepository.findByCode(code);
        } while (existReferral.isPresent());

        ReferralCode referralCode = new ReferralCode();
        referralCode.setCode(code);
        referralCode.setDiscountPercent(10);
        referralCode.setUsageLimit(10);
        referralCode.setUser(owner);
        referralCode.setOrderDetail(orderDetail);
        referralCode.setExpiredAt(add30DaysToDate());
        referralCodeRepository.save(referralCode);
    }

    public static String generateCode() {
        StringBuilder sb = new StringBuilder(20);
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public static Date add30DaysToDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 30);
        return calendar.getTime();
    }
}
