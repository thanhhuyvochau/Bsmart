package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.request.CreateAccountRequest;
import fpt.project.bsmart.entity.request.UploadImageRequest;

import java.util.Optional;


public interface IUserService {
    Long uploadImageProfile(Long id, UploadImageRequest uploadImageRequest);
//    Integer saveUser(CreateAccountRequest createAccountRequest);

//    Optional<User> findByUsername(String username);
}
