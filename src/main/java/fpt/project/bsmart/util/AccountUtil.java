package fpt.project.bsmart.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.Account;
import fpt.project.bsmart.moodle.response.MoodleUserResponse;
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
        Account account = securityUtil.getCurrentUserThrowNotFoundException();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt principal = (Jwt) authentication.getPrincipal();
        try {
            if (account.getKeycloakUserId() == null) {
                account.setKeycloakUserId(principal.getClaimAsString("sub"));
            }
            if (account.getMoodleUserId() == null) {
                MoodleUserResponse moodleUserResponse = moodleUtil.getMoodleUserIfExistByKeycloakId(account.getKeycloakUserId());
                account.setMoodleUserId(moodleUserResponse.getId());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return true ;
    }
}
