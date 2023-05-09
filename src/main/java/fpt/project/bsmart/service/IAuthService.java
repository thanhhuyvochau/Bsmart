package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.request.JwtResponse;
import fpt.project.bsmart.entity.request.LoginRequest;

public interface IAuthService {
    JwtResponse userLogin(LoginRequest loginRequest);
}
