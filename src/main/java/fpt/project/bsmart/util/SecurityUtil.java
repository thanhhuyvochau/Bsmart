package fpt.project.bsmart.util;

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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            Jwt principal = (Jwt) authentication.getPrincipal();
            String username = principal.getClaimAsString("preferred_username");
            User currentUser = Optional.ofNullable(staticUserRepository.findByEmail(username))
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Student not found by username"));
            if (currentUser.getKeycloakUserId() == null) {
                currentUser.setKeycloakUserId(principal.getClaimAsString("id"));
            }
            return currentUser;
        } catch (Exception e) {
            return null;
        }
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
        Jwt principal = (Jwt) authentication.getPrincipal();
        String username = principal.getClaimAsString("preferred_username");
        return Optional.ofNullable(username);
    }

    public static Boolean isHasRole(User user, EUserRole roleCode) {
        if (user == null) return false;
        List<Role> roles = user.getRoles();
        return roles.stream().anyMatch(role -> Objects.equals(role.getCode(), roleCode));
    }
}
