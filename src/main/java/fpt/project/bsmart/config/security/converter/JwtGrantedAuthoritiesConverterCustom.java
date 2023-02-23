package fpt.project.bsmart.config.security.converter;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Component
public class JwtGrantedAuthoritiesConverterCustom implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Value("${keycloak.app-client}")
    String appClientId;

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String authority : getAuthorities(jwt)) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority));
        }
        return grantedAuthorities;
    }

    @Override
    public <U> Converter<Jwt, U> andThen(Converter<? super Collection<GrantedAuthority>, ? extends U> after) {
        return Converter.super.andThen(after);
    }

    private Collection<String> getAuthorities(Jwt jwt) {
        String claimName = "resource_access";
        Map<String, Object> resourceAccess = jwt.getClaimAsMap(claimName);
        Map<String, Object> vuonDauService = (Map<String, Object>) resourceAccess.get(appClientId);
        Collection<String> roles = (Collection<String>) vuonDauService.get("roles");
        if (!roles.isEmpty()) {
            return roles;
        }
        return Collections.emptyList();
    }
}
