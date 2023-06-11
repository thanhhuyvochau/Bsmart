package fpt.project.bsmart.util;


import fpt.project.bsmart.entity.MentorProfile;
import fpt.project.bsmart.entity.Role;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.EAccountStatus;
import fpt.project.bsmart.entity.constant.EUserRole;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static fpt.project.bsmart.util.Constants.ErrorMessage.ACCOUNT_IS_NOT_MENTOR;


@Component
public class MentorUtil {


    private static MessageUtil staticMessageUtil;

    public MentorUtil(MessageUtil messageUtil) {
        staticMessageUtil = messageUtil;

    }

    public static MentorProfile getCurrentUserMentorProfile(User currentUserAccountLogin) {
        MentorUtil.checkIsMentor();
        return currentUserAccountLogin.getMentorProfile();
    }



    public static User checkIsMentor() {
        User currentUserAccountLogin = SecurityUtil.getCurrentUser();
        MentorProfile mentorProfile = currentUserAccountLogin.getMentorProfile();
        List<Role> roles = currentUserAccountLogin.getRoles();
        List<Boolean> checkRoleTeacher = roles.stream().map(role -> role.getCode().equals(EUserRole.TEACHER)).collect(Collectors.toList());

        if (checkRoleTeacher.isEmpty()) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(staticMessageUtil.getLocalMessage(ACCOUNT_IS_NOT_MENTOR));
        }
        if (mentorProfile == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(staticMessageUtil.getLocalMessage(ACCOUNT_IS_NOT_MENTOR));
        }

        if (!mentorProfile.getStatus().equals(EAccountStatus.STARTING)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(staticMessageUtil.getLocalMessage(ACCOUNT_IS_NOT_MENTOR));
        }

        return currentUserAccountLogin;
    }


}
