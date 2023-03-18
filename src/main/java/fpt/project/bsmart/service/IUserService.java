package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.dto.UserDto;
import fpt.project.bsmart.entity.request.CreateAccountRequest;
import fpt.project.bsmart.entity.request.JwtResponse;
import fpt.project.bsmart.entity.request.User.AccountProfileEditRequest;
import fpt.project.bsmart.entity.request.User.MentorPersonalProfileEditRequest;
import fpt.project.bsmart.entity.request.User.PersonalProfileEditRequest;
import fpt.project.bsmart.entity.request.User.SocialProfileEditRequest;
import fpt.project.bsmart.entity.request.UploadImageRequest;

import java.util.Optional;


public interface IUserService {
    UserDto getUserById(Long id);
    UserDto getLoginUser();

    Long removeSocialLink(String link);

    Long editUserSocialProfile(SocialProfileEditRequest socialProfileEditRequest);

    Long editUserAccountProfile(AccountProfileEditRequest accountProfileEditRequest);

    Long editUserPersonalProfile(PersonalProfileEditRequest personalProfileEditRequest);

    Long uploadImageProfile(Long id, UploadImageRequest uploadImageRequest);

    //    Long saveUser(CreateAccountRequest createAccountRequest);
    Long registerAccount(CreateAccountRequest createAccountRequest);

    Long  editMentorPersonalProfile(MentorPersonalProfileEditRequest mentorPersonalProfileEditRequest);

//    Optional<User> findByUsername(String username);
}
