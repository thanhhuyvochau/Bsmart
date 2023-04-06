package fpt.project.bsmart.util.keycloak;

import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.Optional;

@Component
public class KeycloakUserUtil {
    private final Keycloak keycloak;
    private final KeycloakRealmUtil keycloakRealmUtil;
    @Value("${keycloak.realm}")
    private String realm;

    public KeycloakUserUtil(Keycloak keycloak, KeycloakRealmUtil keycloakRealmUtil) {
        this.keycloak = keycloak;
        this.keycloakRealmUtil = keycloakRealmUtil;
    }

    public Boolean create(User user,String password) {
        CredentialRepresentation credentialRepresentation = preparePasswordRepresentation(password);
        UserRepresentation userRepresentation = prepareUserRepresentation(user, credentialRepresentation);
        RealmResource realmResource = keycloak.realm(realm);
        Response response = realmResource.users().create(userRepresentation);
        if (response.getStatus() == HttpStatus.CREATED.value()) {
            return true;
        }
        return false;
    }

    public Boolean update(User user, String password) {
        CredentialRepresentation credentialRepresentation = preparePasswordRepresentation(password);
        try {
            UserRepresentation newUserRepresentation = prepareUserRepresentation(user, credentialRepresentation);
            UserRepresentation userRepresentation = Optional.ofNullable(getUserRepresentation(user))
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("User not found in Keycloak!!"));
            keycloak.realm(realm).users().get(userRepresentation.getId()).update(newUserRepresentation);
        } catch (Exception e) {
            throw ApiException.create(HttpStatus.CONFLICT).withMessage(e.getMessage());
        }
        return true;
    }

    protected UserRepresentation getUserRepresentation(User user) {
        UserRepresentation userRepresentation = keycloakRealmUtil.getRealmReSource().users().search(user.getEmail(), true).stream().findFirst().orElse(null);
        return userRepresentation;
    }

    protected UserResource getUserResource(User user) {
        UserRepresentation userRepresentation = getUserRepresentation(user);
        return keycloakRealmUtil.getRealmReSource().users().get(userRepresentation.getId());
    }

    protected UsersResource getUsersResource() {
        return keycloakRealmUtil.getRealmReSource().users();
    }

    private CredentialRepresentation preparePasswordRepresentation(String password) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(password);
        return credentialRepresentation;
    }

    private UserRepresentation prepareUserRepresentation(User user, CredentialRepresentation credentialRepresentation) {
        UserRepresentation newUser = new UserRepresentation();
        newUser.setUsername(user.getEmail());
        newUser.setEmail(user.getEmail());

        newUser.setCredentials(Collections.singletonList(credentialRepresentation));
        newUser.setEnabled(true);
        return newUser;
    }
}
