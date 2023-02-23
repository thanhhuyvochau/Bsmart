package fpt.project.bsmart.util.keycloak;

import fpt.project.bsmart.entity.Account;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.util.ObjectUtil;
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

    public Boolean create(Account account) {
        CredentialRepresentation credentialRepresentation = preparePasswordRepresentation(account.getPassword());
        UserRepresentation userRepresentation = prepareUserRepresentation(account, credentialRepresentation);
        RealmResource realmResource = keycloak.realm(realm);
        Response response = realmResource.users().create(userRepresentation);
        if (response.getStatus() == HttpStatus.CREATED.value()) {
            return true;
        }
        return false;
    }

    public Boolean update(Account account) {
        CredentialRepresentation credentialRepresentation = preparePasswordRepresentation(account.getPassword());
        try {
            UserRepresentation newUserRepresentation = prepareUserRepresentation(account, credentialRepresentation);
            UserRepresentation userRepresentation = Optional.ofNullable(getUserRepresentation(account))
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("User not found in Keycloak!!"));
            keycloak.realm(realm).users().get(userRepresentation.getId()).update(newUserRepresentation);
        } catch (Exception e) {
            throw ApiException.create(HttpStatus.CONFLICT).withMessage(e.getMessage());
        }
        return true;
    }

    protected UserRepresentation getUserRepresentation(Account account) {
        return keycloakRealmUtil.getRealmReSource().users().search(account.getUsername(), true).stream().findFirst().orElse(null);
    }

    protected UserResource getUserResource(Account account) {
        UserRepresentation userRepresentation = getUserRepresentation(account);
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

    private UserRepresentation prepareUserRepresentation(Account account, CredentialRepresentation credentialRepresentation) {
        UserRepresentation newUser = ObjectUtil.copyProperties(account, new UserRepresentation(), UserRepresentation.class, true);
        newUser.setCredentials(Collections.singletonList(credentialRepresentation));
        newUser.setEnabled(true);
        return newUser;
    }
}
