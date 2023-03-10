package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.request.CreateAccountRequest;
import fpt.project.bsmart.entity.request.User.AccountProfileEditRequest;
import fpt.project.bsmart.entity.request.User.PersonalProfileEditRequest;
import fpt.project.bsmart.entity.request.User.SocialProfileEditRequest;
import fpt.project.bsmart.entity.request.UploadImageRequest;

import java.util.Optional;


public interface IUserService {
    User getUserById(Long id);
    Long editUserSocialProfile(Long id, SocialProfileEditRequest socialProfileEditRequest);
    Long editUserAccountProfile(Long id, AccountProfileEditRequest accountProfileEditRequest);
    Long editUserPersonalProfile(Long id, PersonalProfileEditRequest personalProfileEditRequest);
    Long uploadImageProfile(Long id, UploadImageRequest uploadImageRequest);
//    Long saveUser(CreateAccountRequest createAccountRequest);
    Long registerAccount(CreateAccountRequest createAccountRequest);

//    Optional<User> findByUsername(String username);
}
