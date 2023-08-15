package fpt.project.bsmart.config.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import fpt.project.bsmart.config.AppProperties;
import fpt.project.bsmart.config.security.jwt.JwtUtils;
import fpt.project.bsmart.config.security.oauth2.dto.LocalUser;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.request.JwtResponse;
import fpt.project.bsmart.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils tokenProvider;

    private final AppProperties appProperties;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    OAuth2AuthenticationSuccessHandler(JwtUtils tokenProvider, AppProperties appProperties,
                                       HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository, ObjectMapper objectMapper) {
        this.tokenProvider = tokenProvider;
        this.appProperties = appProperties;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME).map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        String token = tokenProvider.generateJwtToken(authentication);
        LocalUser localUser = (LocalUser) authentication.getPrincipal();
        User user = localUser.getUser();
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getCode().name())
                .collect(Collectors.toList());
        JwtResponse jwtResponse = new JwtResponse(token, user.getId(), user.getEmail(), roles);
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            response.getWriter().write(objectMapper.writeValueAsString(jwtResponse));
            response.getWriter().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        return UriComponentsBuilder.fromUriString(targetUrl).queryParam("token", token).build().toUriString();
        return UriComponentsBuilder.fromUriString(targetUrl).build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris().stream().anyMatch(authorizedRedirectUri -> {
            // Only validate host and port. Let the clients use different paths if they want
            // to
            URI authorizedURI = URI.create(authorizedRedirectUri);
            if (authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost()) && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                return true;
            }
            return false;
        });
    }
}