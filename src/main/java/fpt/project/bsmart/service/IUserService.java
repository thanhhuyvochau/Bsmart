package fpt.project.bsmart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.config.security.oauth2.dto.LocalUser;
import fpt.project.bsmart.config.security.oauth2.dto.SignUpRequest;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.dto.UserDto;
import fpt.project.bsmart.entity.request.CreateAccountRequest;
import fpt.project.bsmart.entity.request.UploadImageRequest;
import fpt.project.bsmart.entity.request.User.*;
import fpt.project.bsmart.entity.request.mentorprofile.UserDtoRequest;
import fpt.project.bsmart.entity.response.VerifyResponse;
import fpt.project.bsmart.entity.response.member.MemberDetailResponse;
import fpt.project.bsmart.entity.response.mentor.MentorEditProfileResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;


public interface IUserService {
    UserDto getUserById(Long id);
    ApiPage<UserDto> adminGetAllUser(UserSearchRequest request, Pageable pageable);

    UserDto getLoginUser();
    UserDto getUserProfileForMentorPage(Long id);
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

    LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) throws IOException;

    User registerNewUser(final SignUpRequest signUpRequest);

    User getUserByEmail(String username);

    UserDto managerGetMentorDetail(Long id);

    MemberDetailResponse managerGetMemberDetail(Long id);

    MentorEditProfileResponse getProfileEdit() throws JsonProcessingException;
}