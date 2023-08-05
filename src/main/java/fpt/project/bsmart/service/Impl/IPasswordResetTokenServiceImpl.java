package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.PasswordResetToken;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.request.ResetPasswordRequest;
import fpt.project.bsmart.repository.PasswordResetTokenRepository;
import fpt.project.bsmart.repository.UserRepository;
import fpt.project.bsmart.service.IPasswordResetTokenService;
import fpt.project.bsmart.util.MessageUtil;
import fpt.project.bsmart.util.PasswordUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static fpt.project.bsmart.util.Constants.ErrorMessage.*;
import static fpt.project.bsmart.util.Constants.ErrorMessage.Invalid.INVALID_PASSWORD;

@Service
public class IPasswordResetTokenServiceImpl implements IPasswordResetTokenService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final MessageUtil messageUtil;
    private final PasswordEncoder passwordEncoder;

    public IPasswordResetTokenServiceImpl(PasswordResetTokenRepository passwordResetTokenRepository, UserRepository userRepository, MessageUtil messageUtil, PasswordEncoder passwordEncoder) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
        this.messageUtil = messageUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Boolean generateResetToken(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(USER_NOT_FOUND_BY_EMAIL) + email));
        Optional<PasswordResetToken> resetToken = passwordResetTokenRepository.findByUser(user);
        if(resetToken.isPresent()){
            boolean isExpired = resetToken.get().getExpirationDate().isBefore(Instant.now());
            if (!isExpired){
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(PASSWORD_RESET_TOKEN_IS_EXIST));
            }
            passwordResetTokenRepository.delete(resetToken.get());
        }
        Instant expirationDate = Instant.now().plus(300, ChronoUnit.SECONDS);
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setUser(user);
        passwordResetToken.setExpirationDate(expirationDate);
        passwordResetToken.setToken(token);
        passwordResetTokenRepository.save(passwordResetToken);
        String returnUrl = "" + token;
        //send mail include returnUrl
        return true;
    }

    @Override
    public Boolean resetPassword(String resetToken, ResetPasswordRequest request){
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(resetToken)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(PASSWORD_RESET_TOKEN_NOT_FOUND)));
        boolean isExpired = passwordResetToken.getExpirationDate().isBefore(Instant.now());
        if(isExpired){
            passwordResetTokenRepository.delete(passwordResetToken);
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(PASSWORD_RESET_TOKEN_IS_EXIST));
        }
        User user = passwordResetToken.getUser();
        if(!PasswordUtil.isValidPassword(request.getPassword())){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_PASSWORD));
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        passwordResetTokenRepository.delete(passwordResetToken);
        return true;
    }
}
