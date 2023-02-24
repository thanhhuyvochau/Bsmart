package fpt.project.bsmart.util;

import org.springframework.stereotype.Component;

@Component
public class AccountUtil {
    private final MoodleUtil moodleUtil;
    private final SecurityUtil securityUtil;
    public AccountUtil(MoodleUtil moodleUtil, SecurityUtil securityUtil) {
        this.moodleUtil = moodleUtil;
        this.securityUtil = securityUtil;
    }


}
