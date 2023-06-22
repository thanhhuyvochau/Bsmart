package fpt.project.bsmart.util;

import fpt.project.bsmart.config.json.SecurityJsonViewControllerAdvice;
import fpt.project.bsmart.entity.constant.EUserRole;

public class ResponseUtil {
    public static void responseForRole(EUserRole role) {
        SecurityJsonViewControllerAdvice.setJsonRoleView(role);
    }
}
