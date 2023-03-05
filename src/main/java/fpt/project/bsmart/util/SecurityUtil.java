package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class SecurityUtil {

    private final UserRepository userRepository;


    public SecurityUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUserThrowNotFoundException() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt principal = (Jwt) authentication.getPrincipal();
        String username = principal.getClaimAsString("preferred_username");
//        User currentUser = Optional.ofNullable(accountRepository.findByUsername(username))
//                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Student not found by username"));
//        return currentUser;
        return null;
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

    public static Jwt getCurrentPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw ApiException.create(HttpStatus.CONFLICT).withMessage("User principal is null!");
        }
        return (Jwt) authentication.getPrincipal();
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.equals(authentication.getPrincipal(), "anonymousUser")) {
            return null;
        } else {
            Jwt principal = (Jwt) authentication.getPrincipal();
            String username = principal.getClaimAsString("preferred_username");
//            return accountRepository.findByUsername(username);
            return null;
        }
    }
}
