package fpt.project.bsmart.util.keycloak;


import org.springframework.stereotype.Component;

@Component
public class KeycloakRoleUtil {

//    @Value("${keycloak.realm}")
//    private String realm;
//
//    private final KeycloakClientUtil keycloakClientUtil;
//    private final KeycloakRealmUtil keycloakRealmUtil;
//
//
//    public KeycloakRoleUtil(Keycloak keycloak, KeycloakClientUtil keycloakClientUtil, KeycloakRealmUtil keycloakRealmUtil) {
//        this.keycloak = keycloak;
//        this.keycloakClientUtil = keycloakClientUtil;
//        this.keycloakRealmUtil = keycloakRealmUtil;
//
//    }
//
//    public void create(String roleName) {
//        RoleRepresentation roleRepresentation = new RoleRepresentation();
//        roleRepresentation.setName(roleName);
//        roleRepresentation.setClientRole(true);
//        RealmResource realmReSource = keycloakRealmUtil.getRealmReSource();
//        ClientResource clientResource = keycloakClientUtil.getClientResource(realmReSource);
//        clientResource.roles().create(roleRepresentation);
//    }
//
//    public List<RoleRepresentation> findAllClientRoles() {
//        RealmResource realmReSource = keycloakRealmUtil.getRealmReSource();
//        ClientResource clientResource = keycloakClientUtil.getClientResource(realmReSource);
//        return clientResource.roles().list();
//    }
//
//    public RoleRepresentation findByName(String roleName) {
//        RealmResource realmReSource = keycloakRealmUtil.getRealmReSource();
//        ClientResource clientResource = keycloakClientUtil.getClientResource(realmReSource);
//        return clientResource.roles().get(roleName).toRepresentation();
//    }

//    public Boolean assignRoleToUser(String roleName, User user) {
//        try {
//            RealmResource realmReSource = keycloakRealmUtil.getRealmReSource();
//            ClientResource clientResource = keycloakClientUtil.getClientResource(realmReSource);
//            UserResource userResource =keycloakUserUtil.getUserResource(user);
//            ClientRepresentation clientRepresentation = clientResource.toRepresentation();
//            RoleRepresentation roleRepresentation = findByName(roleName);
//            userResource.roles().clientLevel(clientRepresentation.getId()).add(Collections.singletonList(roleRepresentation));
//        } catch (Exception e) {
//            throw ApiException.create(HttpStatus.CONFLICT).withMessage(e.getMessage());
//        }
//        return true;
//    }

}
