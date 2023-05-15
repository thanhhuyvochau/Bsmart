package fpt.project.bsmart.util;

import fpt.project.bsmart.config.security.service.UserDetailsImpl;
import fpt.project.bsmart.entity.Cart;
import fpt.project.bsmart.entity.Role;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.Wallet;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.EUserRole;
import fpt.project.bsmart.repository.CartRepository;
import fpt.project.bsmart.repository.UserRepository;
import fpt.project.bsmart.repository.WalletRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SecurityUtil {
    private static MessageUtil messageUtil;
    private static UserRepository staticUserRepository;
    private static WalletRepository staticWalletRepository;
    private static CartRepository staticCartRepository;

    public SecurityUtil(MessageUtil messageUtil, UserRepository userRepository, WalletRepository walletRepository, CartRepository cartRepository) {
        this.messageUtil = messageUtil;
        staticUserRepository = userRepository;
        staticWalletRepository = walletRepository;
        staticCartRepository = cartRepository;
    }

    public static User getCurrentUser() {
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
        User currentUserAccountLogin = getCurrentUser();
        Wallet wallet = currentUserAccountLogin.getWallet();
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setOwner(currentUserAccountLogin);
            staticWalletRepository.save(wallet);
        }
        return wallet;
    }

    public static Cart getCurrentUserCart() {
        User currentUserAccountLogin = getCurrentUser();
        Cart cart = currentUserAccountLogin.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(currentUserAccountLogin);
            staticCartRepository.save(cart);
        }
        return cart;
    }

    public static Optional<String> getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.empty();
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return Optional.ofNullable(userDetails.getEmail());
    }

    public static Boolean isHasAnyRole(User user, EUserRole... checkedRoleCodes) {
        List<EUserRole> userRoleCodes = user.getRoles().stream().map(Role::getCode).collect(Collectors.toList());
        for (EUserRole checkedRoleCode : checkedRoleCodes) {
            if (userRoleCodes.contains(checkedRoleCode)) {
                return true;
            }
        }
        return false;
    }
}
