package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.request.CreateAccountRequest;

import java.util.Optional;


public interface IUserService {
    Integer saveUser(CreateAccountRequest createAccountRequest);

    Optional<User> findByUsername(String username);
}
