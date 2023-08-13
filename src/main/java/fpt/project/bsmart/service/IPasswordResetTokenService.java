package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.request.ResetPasswordRequest;

public interface IPasswordResetTokenService {
    Boolean generateResetToken(String email);
    Boolean resetPassword(String resetToken, ResetPasswordRequest request);
    Boolean checkResetTokenExist(String resetToken);
}
