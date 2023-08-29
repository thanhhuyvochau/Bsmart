package fpt.project.bsmart.util;

import fpt.project.bsmart.config.security.oauth2.dto.LocalUser;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SecurityUtil {
    private static MessageUtil messageUtil;
    private static UserRepository staticUserRepository;
    private static WalletRepository staticWalletRepository;
    private static CartRepository staticCartRepository;
    private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

    public SecurityUtil(MessageUtil messageUtil, UserRepository userRepository, WalletRepository walletRepository, CartRepository cartRepository) {
        this.messageUtil = messageUtil;
        staticUserRepository = userRepository;
        staticWalletRepository = walletRepository;
        staticCartRepository = cartRepository;
    }

    public static User getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Object principal = securityContext.getAuthentication().getPrincipal();
        User user = null;
        String email = getEmailCurrentUser(principal);
        user = staticUserRepository.findByEmail(email)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage("Tài khoản đăng nhập hiện tại không tìm thấy " + email == null ? "": email));
        return user;
    }

    public static Optional<User> getCurrentUserOptional() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Object principal = securityContext.getAuthentication().getPrincipal();
        User user = null;
        String email = getEmailCurrentUser(principal);
        user = staticUserRepository.findByEmail(email)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage("Tài khoản đăng nhập hiện tại không tìm thấy " + email == null ? "" : email));
        return Optional.ofNullable(user);
    }

    public static User hasCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Object principal = securityContext.getAuthentication().getPrincipal();
        String email = getEmailCurrentUser(principal);
        User user = staticUserRepository.findByEmail(email).orElse(null);

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
        if (authentication == null) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(getEmailCurrentUser(authentication.getPrincipal()));
        }
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

    public static User getUserOrThrowException(Optional<User> userOptional) {
        return userOptional.orElseThrow(() -> ApiException.create(HttpStatus.UNAUTHORIZED).withMessage("Người dùng chưa đăng nhập hoặc không tồn tại"));
    }

    public static String getEmailCurrentUser(Object principal) {
        String email = null;
        if (principal instanceof UserDetailsImpl) {
            UserDetailsImpl userPrincipal = (UserDetailsImpl) principal;
            email = userPrincipal.getEmail();
        } else if (principal instanceof LocalUser) {
            LocalUser userPrincipal = (LocalUser) principal;
            Map<String, Object> attributes = userPrincipal.getAttributes();
            email = (String) attributes.get("email");
        } else {
            logger.error("Cannot cast Authentication to Classes is supported by application");
        }
        return email;
    }
}
