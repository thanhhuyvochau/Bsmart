package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.request.CreateAccountRequest;
import fpt.project.bsmart.entity.request.User.SocialProfileEditRequest;

import java.util.Optional;


public interface IUserService {
    Long saveUser(CreateAccountRequest createAccountRequest);
    Optional<User> findByUsername(String username);
    Long editUserSocialProfile(Long id, SocialProfileEditRequest socialProfileEditRequest);
}
