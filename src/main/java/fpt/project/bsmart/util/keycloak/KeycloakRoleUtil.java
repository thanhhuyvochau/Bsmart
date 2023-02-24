package fpt.project.bsmart.util.keycloak;

import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class KeycloakRoleUtil {
    private final Keycloak keycloak;
    @Value("${keycloak.realm}")
    private String realm;

    private final KeycloakClientUtil keycloakClientUtil;
    private final KeycloakRealmUtil keycloakRealmUtil;
    private final KeycloakUserUtil keycloakUserUtil;

    public KeycloakRoleUtil(Keycloak keycloak, KeycloakClientUtil keycloakClientUtil, KeycloakRealmUtil keycloakRealmUtil, KeycloakUserUtil keycloakUserUtil) {
        this.keycloak = keycloak;
        this.keycloakClientUtil = keycloakClientUtil;
        this.keycloakRealmUtil = keycloakRealmUtil;
        this.keycloakUserUtil = keycloakUserUtil;
    }

    public void create(String roleName) {
        RoleRepresentation roleRepresentation = new RoleRepresentation();
        roleRepresentation.setName(roleName);
        roleRepresentation.setClientRole(true);
        RealmResource realmReSource = keycloakRealmUtil.getRealmReSource();
        ClientResource clientResource = keycloakClientUtil.getClientResource(realmReSource);
        clientResource.roles().create(roleRepresentation);
    }

    public List<RoleRepresentation> findAllClientRoles() {
        RealmResource realmReSource = keycloakRealmUtil.getRealmReSource();
        ClientResource clientResource = keycloakClientUtil.getClientResource(realmReSource);
        return clientResource.roles().list();
    }

    public RoleRepresentation findByName(String roleName) {
        RealmResource realmReSource = keycloakRealmUtil.getRealmReSource();
        ClientResource clientResource = keycloakClientUtil.getClientResource(realmReSource);
        return clientResource.roles().get(roleName).toRepresentation();
    }

    public Boolean assignRoleToUser(String roleName, User user) {
        try {
            RealmResource realmReSource = keycloakRealmUtil.getRealmReSource();
            ClientResource clientResource = keycloakClientUtil.getClientResource(realmReSource);
            UserResource userResource = keycloakUserUtil.getUserResource(user);
            ClientRepresentation clientRepresentation = clientResource.toRepresentation();
            RoleRepresentation roleRepresentation = findByName(roleName);
            userResource.roles().clientLevel(clientRepresentation.getId()).add(Collections.singletonList(roleRepresentation));
        } catch (Exception e) {
            throw ApiException.create(HttpStatus.CONFLICT).withMessage(e.getMessage());
        }
        return true;
    }

}
