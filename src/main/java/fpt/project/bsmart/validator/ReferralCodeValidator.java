package fpt.project.bsmart.validator;

import fpt.project.bsmart.entity.Course;
import fpt.project.bsmart.entity.ReferralCode;
import fpt.project.bsmart.entity.common.ApiException;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class ReferralCodeValidator {
    public static boolean isReferralCodeValidWithCourse(ReferralCode referralCode, Course course) {
        Course appliedCourse = referralCode.getOrderDetail().getClazz().getCourse();
        return Objects.equals(course.getId(), appliedCourse.getId());
    }

    public static boolean isExpiredReferralCode(ReferralCode referralCode) {
        Instant now = Instant.now().truncatedTo(ChronoUnit.HOURS);
        Instant expiredAt = referralCode.getExpiredAt();
        return now.isAfter(expiredAt);
    }

    public static boolean isAvailableUsageNumber(ReferralCode referralCode) {
        return referralCode.getUsageCount().equals(referralCode.getUsageLimit());
    }

    public static void validateReferralCode(ReferralCode referralCode, Course course) {
        if (!isReferralCodeValidWithCourse(referralCode, course)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Mã giới thiệu không áp dụng với khóa học này");
        } else if (isExpiredReferralCode(referralCode)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Mã giới thiệu đã hết hạn");
        } else if (isAvailableUsageNumber(referralCode)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Mã giới thiệu đã hết lượt sử dụng");
        }
    }
}
