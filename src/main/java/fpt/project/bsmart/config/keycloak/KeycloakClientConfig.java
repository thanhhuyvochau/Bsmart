//package fpt.project.bsmart.config.keycloak;
//
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class KeycloakClientConfig {
//    @Value("${keycloak.credentials.secret}")
//    private String secretKey;
//    @Value("${keycloak.resource}")
//    private String clientId;
//    @Value("${keycloak.auth-server-url}")
//    private String authUrl;
//    @Value("${keycloak.realm}")
//    private String realm;
//
////    @Bean
////    public Keycloak getKeycloak() {
////        return KeycloakBuilder.builder()
////                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
////                .serverUrl(authUrl)
////                .realm(realm)
////                .clientId(clientId)
////                .clientSecret(secretKey)
////                .build();
////    }
//}
