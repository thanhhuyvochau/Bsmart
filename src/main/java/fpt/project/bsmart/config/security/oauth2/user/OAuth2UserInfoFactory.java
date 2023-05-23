package fpt.project.bsmart.config.security.oauth2.user;


import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.SocialProvider;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(SocialProvider.GOOGLE.getProviderType())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else {
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}