package fpt.project.bsmart.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class AccountUtil {
    private final MoodleUtil moodleUtil;
    private final SecurityUtil securityUtil;

    public AccountUtil(MoodleUtil moodleUtil, SecurityUtil securityUtil) {
        this.moodleUtil = moodleUtil;
        this.securityUtil = securityUtil;
    }

    public Boolean synchronizedCurrentAccountInfo() {
        User user = securityUtil.getCurrentUserThrowNotFoundException();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt principal = (Jwt) authentication.getPrincipal();
        if (user.getKeycloakUserId() == null) {
            user.setKeycloakUserId(principal.getClaimAsString("sub"));
        }
        return true;
    }
}
