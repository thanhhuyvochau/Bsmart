package fpt.project.bsmart.service;

import fpt.project.bsmart.config.security.oauth2.dto.LocalUser;
import fpt.project.bsmart.config.security.oauth2.dto.SignUpRequest;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.dto.UserDto;
import fpt.project.bsmart.entity.request.CreateAccountRequest;
import fpt.project.bsmart.entity.request.UploadImageRequest;
import fpt.project.bsmart.entity.request.User.*;
import fpt.project.bsmart.entity.response.VerifyResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface IUserService {
    UserDto getUserById(Long id);
    ApiPage<UserDto> getAllUser(UserSearchRequest request, Pageable pageable);

    UserDto getLoginUser();

    Long removeSocialLink(String link);

    Long editUserSocialProfile(SocialProfileEditRequest socialProfileEditRequest);

    Long changePassword(ChangePasswordRequest changePasswordRequest);

    Long editUserPersonalProfile(PersonalProfileEditRequest personalProfileEditRequest);

    Long uploadImageProfile(UploadImageRequest uploadImageRequest) throws IOException;

    //    Long saveUser(CreateAccountRequest createAccountRequest);
    Long registerAccount(CreateAccountRequest createAccountRequest);

    Long editMentorPersonalProfile(MentorPersonalProfileEditRequest mentorPersonalProfileEditRequest);

    Boolean uploadDegree(List<Long> degreeIdsToDelete, MultipartFile[] files) throws IOException;

    VerifyResponse verifyAccount(String code);

    Boolean resendVerifyEmail();

    LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo);

    User registerNewUser(final SignUpRequest signUpRequest);

    User getUserByEmail(String username);
}
