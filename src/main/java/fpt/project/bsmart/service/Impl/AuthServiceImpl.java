package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.config.security.jwt.JwtUtils;
import fpt.project.bsmart.config.security.service.UserDetailsImpl;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.request.JwtResponse;
import fpt.project.bsmart.entity.request.LoginRequest;
import fpt.project.bsmart.repository.RoleRepository;
import fpt.project.bsmart.repository.UserRepository;
import fpt.project.bsmart.service.IAuthService;
import fpt.project.bsmart.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static fpt.project.bsmart.util.Constants.ErrorMessage.Invalid.USERNAME_PASSWORD_INCORRECT;
import static fpt.project.bsmart.util.Constants.ErrorMessage.USER_NOT_FOUND_BY_ID;

@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    MessageUtil messageUtil;

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

        Boolean firstLoginFlag = userRepository.callCheckFirstLoginProcedure(loginRequest.getEmail()) ;
//        Boolean firstLoginFlag = userRepository.getFirstLoginFlag(loginRequest.getEmail());

        System.out.println(firstLoginFlag);

        if (firstLoginFlag == null || !firstLoginFlag) {
            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(USER_NOT_FOUND_BY_ID) + loginRequest.getEmail()));
            user.setFirstLogin(true);
            userRepository.save(user);
        }

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getEmail(),
                roles);

    }

}
