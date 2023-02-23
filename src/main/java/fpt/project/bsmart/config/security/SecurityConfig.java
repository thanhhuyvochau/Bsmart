package fpt.project.bsmart.config.security;

import fpt.project.bsmart.config.security.converter.JwtGrantedAuthoritiesConverterCustom;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;


@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /**Config for development */

//
//        http.cors().configurationSource(corsConfigurationSource())
//                .and().csrf().disable()
//                .authorizeRequests().anyRequest().permitAll();

        /**Config for deployment */
        http.cors().configurationSource(corsConfigurationSource())
                .and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/*").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/*").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/*").authenticated()
                .antMatchers("/api/accounts").authenticated()
                .antMatchers(HttpMethod.GET, "/api/*").permitAll()
                .antMatchers(HttpMethod.POST, "api/teachers/account").permitAll()
                .antMatchers(HttpMethod.POST, "api/students/account").permitAll()
                .and().oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter())
                .and().authenticationEntryPoint(new BasicAuthenticationEntryPoint());

    }


    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverterCustom grantedAuthoritiesConverterCustom = grantedAuthoritiesConverterCustom();
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverterCustom);
        return jwtAuthenticationConverter;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));

        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public JwtGrantedAuthoritiesConverterCustom grantedAuthoritiesConverterCustom() {
        return new JwtGrantedAuthoritiesConverterCustom();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}