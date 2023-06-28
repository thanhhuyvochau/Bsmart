package fpt.project.bsmart.util;


import fpt.project.bsmart.entity.MentorProfile;
import fpt.project.bsmart.entity.Role;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.UserImage;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.EImageType;
import fpt.project.bsmart.entity.constant.EMentorProfileStatus;
import fpt.project.bsmart.entity.constant.EUserRole;
import fpt.project.bsmart.entity.response.Mentor.CompletenessMentorProfileResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static fpt.project.bsmart.util.Constants.ErrorMessage.ACCOUNT_IS_NOT_MENTOR;


@Component
public class MentorUtil {


    private static MessageUtil staticMessageUtil;

    public MentorUtil(MessageUtil messageUtil) {
        staticMessageUtil = messageUtil;

    }

    public static MentorProfile getCurrentUserMentorProfile(User currentUserAccountLogin) {
        MentorUtil.checkIsMentor();
        return currentUserAccountLogin.getMentorProfile();
    }


    public static User checkIsMentor() {
        User currentUserAccountLogin = SecurityUtil.getCurrentUser();
        MentorProfile mentorProfile = currentUserAccountLogin.getMentorProfile();
        List<Role> roles = currentUserAccountLogin.getRoles();
        List<Boolean> checkRoleTeacher = roles.stream().map(role -> role.getCode().equals(EUserRole.TEACHER)).collect(Collectors.toList());

        if (checkRoleTeacher.isEmpty()) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(staticMessageUtil.getLocalMessage(ACCOUNT_IS_NOT_MENTOR));
        }
        if (mentorProfile == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(staticMessageUtil.getLocalMessage(ACCOUNT_IS_NOT_MENTOR));
        }

        if (!mentorProfile.getStatus().equals(EMentorProfileStatus.STARTING)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(staticMessageUtil.getLocalMessage(ACCOUNT_IS_NOT_MENTOR));
        }
        return currentUserAccountLogin;
    }

    //  Kiểm tra tính đầy đủ của hồ sơ giáo viên
    public static CompletenessMentorProfileResponse checkCompletenessMentorProfile() {
        User user = SecurityUtil.getCurrentUser();
        int completionPercentage = 0;
        int totalFields = 13; // Tổng số trường thông tin của user

        List<CompletenessMentorProfileResponse.MissingInformation> missingInformations = new ArrayList<>();
        CompletenessMentorProfileResponse.MissingInformation missingInformation = new CompletenessMentorProfileResponse.MissingInformation();

        CompletenessMentorProfileResponse.MissingInformation.RequiredInfo requiredInfo = new CompletenessMentorProfileResponse.MissingInformation.RequiredInfo();
        CompletenessMentorProfileResponse.MissingInformation.OptionalInfo optionalInfo = new CompletenessMentorProfileResponse.MissingInformation.OptionalInfo();


        List<CompletenessMentorProfileResponse.MissingInformation.RequiredInfo.Field> requiredInfoFiled = new ArrayList<>();
        CompletenessMentorProfileResponse.MissingInformation.RequiredInfo.Field fieldRequired;

        List<CompletenessMentorProfileResponse.MissingInformation.OptionalInfo.Field> optionalInfoFiled = new ArrayList<>();
        CompletenessMentorProfileResponse.MissingInformation.OptionalInfo.Field fieldOption;
        // Danh sách các trường thông tin chưa có giá trị

        if (user.getFullName() == null || user.getFullName().isEmpty()) {
            fieldRequired = new CompletenessMentorProfileResponse.MissingInformation.RequiredInfo.Field();
            fieldRequired.setField("fullName");
            fieldRequired.setName("Họ tên");
            requiredInfoFiled.add(fieldRequired);
        } else {
            completionPercentage++;
        }

        if (user.getBirthday() == null) {
            fieldRequired = new CompletenessMentorProfileResponse.MissingInformation.RequiredInfo.Field();
            fieldRequired.setField("birthday");
            fieldRequired.setName("Ngày sinh");
            requiredInfoFiled.add(fieldRequired);
        } else {
            completionPercentage++;
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            fieldRequired = new CompletenessMentorProfileResponse.MissingInformation.RequiredInfo.Field();
            fieldRequired.setField("email");
            fieldRequired.setName("Địa chỉ email");
            requiredInfoFiled.add(fieldRequired);

        } else {
            completionPercentage++;
        }

        if (user.getAddress() == null || user.getAddress().isEmpty()) {
            fieldRequired = new CompletenessMentorProfileResponse.MissingInformation.RequiredInfo.Field();
            fieldRequired.setField("address");
            fieldRequired.setName("Đại chỉ");
            requiredInfoFiled.add(fieldRequired);
        } else {
            completionPercentage++;
        }


        if (user.getPhone() == null || user.getPhone().isEmpty()) {
            fieldRequired = new CompletenessMentorProfileResponse.MissingInformation.RequiredInfo.Field();
            fieldRequired.setField("phone");
            fieldRequired.setName("Số điện thoại");
            requiredInfoFiled.add(fieldRequired);

        } else {
            completionPercentage++;
        }


        if (user.getGender() == null) {
            fieldRequired = new CompletenessMentorProfileResponse.MissingInformation.RequiredInfo.Field();
            fieldRequired.setField("gender");
            fieldRequired.setName("Giới tính");
            requiredInfoFiled.add(fieldRequired);

        } else {
            completionPercentage++;
        }

        if (user.getLinkedinLink() == null || user.getLinkedinLink().isEmpty()) {
            fieldOption = new CompletenessMentorProfileResponse.MissingInformation.OptionalInfo.Field();
            fieldOption.setField("linkedInLink");
            fieldOption.setName("linkedIn");
            optionalInfoFiled.add(fieldOption);

        } else {
            completionPercentage++;
        }

        if (user.getFacebookLink() == null || user.getFacebookLink().isEmpty()) {
            fieldOption = new CompletenessMentorProfileResponse.MissingInformation.OptionalInfo.Field();
            fieldOption.setField("facebookLink");
            fieldOption.setName("facebook");
            optionalInfoFiled.add(fieldOption);

        } else {
            completionPercentage++;
        }

        if (user.getIsVerified()) {
            completionPercentage++;
        }
        List<UserImage> userImages = user.getUserImages();

        if (userImages != null) {
            Collection<EImageType> listTypeImage = userImages.stream().map(UserImage::getType).collect(Collectors.toList());
            if (!listTypeImage.contains(EImageType.AVATAR)) {
                fieldRequired = new CompletenessMentorProfileResponse.MissingInformation.RequiredInfo.Field();
                fieldRequired.setField("AVATAR");
                fieldRequired.setName("Ảnh đại diện");
                requiredInfoFiled.add(fieldRequired);
            } else {
                completionPercentage++;
            }

            if (!listTypeImage.contains(EImageType.BACKCI)) {
                fieldRequired = new CompletenessMentorProfileResponse.MissingInformation.RequiredInfo.Field();
                fieldRequired.setField("BACKCI");
                fieldRequired.setName("Căn cước công mặt sau");
                requiredInfoFiled.add(fieldRequired);
            } else {
                completionPercentage++;
            }
            if (!listTypeImage.contains(EImageType.FRONTCI)) {
                fieldRequired = new CompletenessMentorProfileResponse.MissingInformation.RequiredInfo.Field();
                fieldRequired.setField("FRONTCI");
                fieldRequired.setName("Căn cước công mặt trước");
                requiredInfoFiled.add(fieldRequired);
            } else {
                completionPercentage++;
            }

            if (!listTypeImage.contains(EImageType.DEGREE)) {
                fieldRequired = new CompletenessMentorProfileResponse.MissingInformation.RequiredInfo.Field();
                fieldRequired.setField("DEGREE");
                fieldRequired.setName("Bằng cấp");
                requiredInfoFiled.add(fieldRequired);
            } else {
                completionPercentage++;
            }

        }


        MentorProfile mentorProfile = user.getMentorProfile();
        if (mentorProfile != null) {
            if (mentorProfile.getIntroduce() == null || mentorProfile.getIntroduce().isEmpty()) {
                fieldRequired = new CompletenessMentorProfileResponse.MissingInformation.RequiredInfo.Field();
                fieldRequired.setField("introduce");
                fieldRequired.setName("Giới thiệu");
                requiredInfoFiled.add(fieldRequired);
            } else {
                completionPercentage++;
            }

            if (mentorProfile.getWorkingExperience() == null || mentorProfile.getWorkingExperience().isEmpty()) {
                fieldRequired = new CompletenessMentorProfileResponse.MissingInformation.RequiredInfo.Field();
                fieldRequired.setField("workingExperience");
                fieldRequired.setName("Số Năm kinh nghiệm");
                requiredInfoFiled.add(fieldRequired);
            } else {
                completionPercentage++;
            }

            if (mentorProfile.getSkills() == null || mentorProfile.getSkills().isEmpty()) {
                fieldRequired = new CompletenessMentorProfileResponse.MissingInformation.RequiredInfo.Field();
                fieldRequired.setField("skills");
                fieldRequired.setName("Kinh nghiệm");
                requiredInfoFiled.add(fieldRequired);
            } else {
                completionPercentage++;
            }

        }

        requiredInfo.setFields(requiredInfoFiled);
        optionalInfo.setFields(optionalInfoFiled);
        missingInformation.setRequiredInfo(requiredInfo);
        missingInformation.setOptionalInfo(optionalInfo);
        missingInformations.add(missingInformation);

        // Tính % hoàn thành dựa trên số lượng trường thông tin có giá trị
        completionPercentage = (int) Math.round(((double) completionPercentage / totalFields) * 100);
        CompletenessMentorProfileResponse response = new CompletenessMentorProfileResponse();
        response.setPercentComplete(completionPercentage);
        response.setMissingInformation(missingInformations);
        response.setAllowSendingApproval(false);
        return response;
    }
}
