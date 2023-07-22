package fpt.project.bsmart.util;


import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.EImageType;
import fpt.project.bsmart.entity.constant.EMentorProfileStatus;
import fpt.project.bsmart.entity.constant.EUserRole;
import fpt.project.bsmart.entity.dto.ImageDto;
import fpt.project.bsmart.entity.dto.MentorSkillDto;
import fpt.project.bsmart.entity.dto.mentor.MentorDto;
import fpt.project.bsmart.entity.response.mentor.CompletenessMentorProfileResponse;
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

    public static MentorDto convertUserToMentorDto(User user) {
        MentorProfile mentorProfile = user.getMentorProfile();
        MentorDto mentorDto = ObjectUtil.copyProperties(mentorProfile, new MentorDto(), MentorDto.class);
        if (user.getFullName() != null) {
            mentorDto.setName(user.getFullName());
        }
        if (user.getEmail()!= null){
            mentorDto.setEmail(user.getEmail());
        }
        List<UserImage> userImages = user.getUserImages();
        List<UserImage> avatar = userImages.stream().filter(userImage -> userImage.getType().equals(EImageType.AVATAR)).collect(Collectors.toList());
        if (userImages != null && avatar.size() > 0) {
            UserImage userImage = avatar.stream().findFirst().get();
            ImageDto imageDto = ConvertUtil.convertUserImageToUserImageDto(userImage);
            mentorDto.setAvatar(imageDto);
        }

        List<MentorSkill> skills = mentorProfile.getSkills();
        List<MentorSkillDto> mentorSkills = new ArrayList<>();
        skills.forEach(mentorSkill -> {
            mentorSkills.add(ConvertUtil.convertMentorSkillToMentorSkillDto(mentorSkill));
        });
        mentorDto.setMentorSkills(mentorSkills);
        return mentorDto;
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
            fieldRequired.setName("Địa chỉ");
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

        }

        if (user.getFacebookLink() == null || user.getFacebookLink().isEmpty()) {
            fieldOption = new CompletenessMentorProfileResponse.MissingInformation.OptionalInfo.Field();
            fieldOption.setField("facebookLink");
            fieldOption.setName("facebook");
            optionalInfoFiled.add(fieldOption);

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

        } else {
            completionPercentage++;
            completionPercentage++;
            completionPercentage++;
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

        } else {
            completionPercentage++;
            completionPercentage++;
            completionPercentage++;
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
        response.setAllowSendingApproval(completionPercentage == 100 && requiredInfo.getFields().isEmpty());


        return response;
    }
}
