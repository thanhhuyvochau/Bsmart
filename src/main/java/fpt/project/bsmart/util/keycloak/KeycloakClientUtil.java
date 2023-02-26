package fpt.project.bsmart.util.keycloak;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeycloakClientUtil {
    @Value("${keycloak.app-client}")
    private String appClientId;

//    protected ClientResource getClientResource(RealmResource resource) {
//        ClientRepresentation clientRepresentation = resource.clients().findByClientId(appClientId).stream().findFirst().orElse(null);
//        if (clientRepresentation != null) {
//            return resource.clients().get(clientRepresentation.getId());
//        }
//        return null;
//    }

}
