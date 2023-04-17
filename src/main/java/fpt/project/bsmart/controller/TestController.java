package fpt.project.bsmart.controller;

import fpt.project.bsmart.config.security.jwt.JwtUtils;
import fpt.project.bsmart.config.security.service.UserDetailsImpl;
import fpt.project.bsmart.entity.Role;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.constant.EUserRole;
import fpt.project.bsmart.entity.request.JwtResponse;
import fpt.project.bsmart.entity.request.LoginRequest;
import fpt.project.bsmart.entity.request.SignupRequest;
import fpt.project.bsmart.repository.RoleRepository;
import fpt.project.bsmart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public String userAccess() {
        return "User TEACHER.";
    }

}