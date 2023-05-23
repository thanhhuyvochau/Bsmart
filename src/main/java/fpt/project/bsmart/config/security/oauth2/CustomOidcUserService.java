package fpt.project.bsmart.config.security.oauth2;


import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOidcUserService extends OidcUserService {

    @Autowired
    private IUserService userService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        try {
            return userService.processUserRegistration(userRequest.getClientRegistration().getRegistrationId(), oidcUser.getAttributes(), oidcUser.getIdToken(),
                    oidcUser.getUserInfo());
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            // Throwing an instance of AuthenticationException will trigger the
            // OAuth2AuthenticationFailureHandler
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(ex.getMessage());
        }
    }
}