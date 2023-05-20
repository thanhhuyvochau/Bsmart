package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.dto.UserDto;
import fpt.project.bsmart.entity.request.CreateAccountRequest;
import fpt.project.bsmart.entity.request.UploadImageRequest;
import fpt.project.bsmart.entity.request.User.ChangePasswordRequest;
import fpt.project.bsmart.entity.request.User.MentorPersonalProfileEditRequest;
import fpt.project.bsmart.entity.request.User.PersonalProfileEditRequest;
import fpt.project.bsmart.entity.request.User.SocialProfileEditRequest;
import fpt.project.bsmart.entity.response.VerifyResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface IUserService {
    UserDto getUserById(Long id);

    UserDto getLoginUser();

    Long removeSocialLink(String link);

    Long editUserSocialProfile(SocialProfileEditRequest socialProfileEditRequest);

    Long changePassword(ChangePasswordRequest changePasswordRequest);

    Long editUserPersonalProfile(PersonalProfileEditRequest personalProfileEditRequest);

    Long uploadImageProfile(UploadImageRequest uploadImageRequest) throws IOException;

    //    Long saveUser(CreateAccountRequest createAccountRequest);
    Long registerAccount(CreateAccountRequest createAccountRequest);

    Long editMentorPersonalProfile(MentorPersonalProfileEditRequest mentorPersonalProfileEditRequest);

    Boolean  uploadDegree( List<Long> degreeIdsToDelete,MultipartFile[] files) throws IOException;

    VerifyResponse verifyAccount(String code);

    Boolean resendVerifyEmail();
}
