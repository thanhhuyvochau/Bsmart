package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.dto.UserDto;
import fpt.project.bsmart.entity.request.CreateAccountRequest;
import fpt.project.bsmart.entity.request.JwtResponse;
import fpt.project.bsmart.entity.request.LoginRequest;
import fpt.project.bsmart.entity.request.UploadImageRequest;
import fpt.project.bsmart.entity.request.User.AccountProfileEditRequest;
import fpt.project.bsmart.entity.request.User.MentorPersonalProfileEditRequest;
import fpt.project.bsmart.entity.request.User.PersonalProfileEditRequest;
import fpt.project.bsmart.entity.request.User.SocialProfileEditRequest;


public interface IAuthService {

    JwtResponse userLogin(LoginRequest loginRequest);
}
