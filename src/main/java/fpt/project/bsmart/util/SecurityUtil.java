package fpt.project.bsmart.util;

import fpt.project.bsmart.config.security.service.UserDetailsImpl;
import fpt.project.bsmart.config.security.service.UserDetailsServiceImpl;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.Wallet;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.repository.UserRepository;
import fpt.project.bsmart.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {
    private static MessageUtil messageUtil;
    private static UserRepository staticUserRepository;
    private static WalletRepository staticWalletRepository;

    public SecurityUtil(MessageUtil messageUtil, UserRepository userRepository, WalletRepository walletRepository) {
        this.messageUtil = messageUtil;
        staticUserRepository = userRepository;
        staticWalletRepository = walletRepository;
    }

    @Autowired
    private static UserDetailsServiceImpl userDetailsService;

    public static User getCurrentUserAccountLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Object principal = securityContext.getAuthentication().getPrincipal();
        UserDetails userDetails1 = null;
        User user = null;
        if (principal instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) principal;
            String email = userDetails.getEmail();
            user = staticUserRepository.findByEmail(email)
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                            .withMessage(messageUtil.getLocalMessage("Tài khoản đăng nhập hiện tại không tìm thấy") + email));
        }
        return user;
    }

    public static Wallet getCurrentUserWallet() {
        User currentUserAccountLogin = getCurrentUserAccountLogin();
        Wallet wallet = currentUserAccountLogin.getWallet();
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setOwner(currentUserAccountLogin);
            staticWalletRepository.save(wallet);
        }
        return wallet;
    }
}
