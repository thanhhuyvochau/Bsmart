package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Course;
import fpt.project.bsmart.entity.ReferralCode;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.response.ReferralCodeResponse;
import fpt.project.bsmart.repository.CourseRepository;
import fpt.project.bsmart.repository.ReferralCodeRepository;
import fpt.project.bsmart.service.ReferralCodeService;
import fpt.project.bsmart.util.MessageUtil;
import fpt.project.bsmart.util.ObjectUtil;
import fpt.project.bsmart.validator.ReferralCodeValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static fpt.project.bsmart.util.Constants.ErrorMessage.COURSE_NOT_FOUND_BY_ID;

@Service
public class ReferralCodeServiceImpl implements ReferralCodeService {
    private final ReferralCodeRepository referralCodeRepository;
    private final CourseRepository courseRepository;
    private final MessageUtil messageUtil;

    public ReferralCodeServiceImpl(ReferralCodeRepository referralCodeRepository, CourseRepository courseRepository, MessageUtil messageUtil) {
        this.referralCodeRepository = referralCodeRepository;
        this.courseRepository = courseRepository;
        this.messageUtil = messageUtil;
    }

    @Override
    public ReferralCodeResponse getReferralCodeToApplyDiscount(String code, Long courseId) {
        Optional<ReferralCode> optionalReferralCode = referralCodeRepository.findByCode(code);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + courseId));

        if (optionalReferralCode.isPresent()) {
            ReferralCode referralCode = optionalReferralCode.get();
            ReferralCodeValidator.validateReferralCode(referralCode, course);
            return ObjectUtil.copyProperties(optionalReferralCode.get(), new ReferralCodeResponse(), ReferralCodeResponse.class, true);
        } else {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Mã giới thiệu không tồn tại vui lòng thử lại sau");
        }
    }
}
