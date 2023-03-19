package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.config.security.jwt.JwtUtils;
import fpt.project.bsmart.config.security.service.UserDetailsImpl;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.request.JwtResponse;
import fpt.project.bsmart.entity.request.LoginRequest;
import fpt.project.bsmart.service.IAuthService;
import fpt.project.bsmart.util.MessageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static fpt.project.bsmart.util.Constants.ErrorMessage.Invalid.USERNAME_PASSWORD_INCORRECT;

@Service
public class AuthServiceImpl implements IAuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final MessageUtil messageUtil;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtUtils jwtUtils, MessageUtil messageUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.messageUtil = messageUtil;
    }

    @Override
    public JwtResponse userLogin(LoginRequest loginRequest) {
        Authentication authentication = null;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        } catch (Exception e) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(USERNAME_PASSWORD_INCORRECT));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getEmail(),
                roles);

    }

}
