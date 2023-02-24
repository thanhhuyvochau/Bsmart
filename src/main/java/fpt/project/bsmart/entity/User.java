package fpt.project.bsmart.entity;


import javax.persistence.*;


@Entity
@Table(name = "user")
public class User extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id_generator")
    @SequenceGenerator(name = "account_id_generator", sequenceName = "account_id_generator")
    private Long id;

    @Column(name = "user_name")
    private String username;

    @Column(name = "is_active")
    private Boolean isActive = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "is_keycloak")
    private Boolean isKeycloak;
    @Column(name = "keycloak_user_id")
    private String keycloakUserId;

    @Column(name = "password")
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getKeycloak() {
        return isKeycloak;
    }

    public void setKeycloak(Boolean keycloak) {
        isKeycloak = keycloak;
    }

    public String getKeycloakUserId() {
        return keycloakUserId;
    }

    public void setKeycloakUserId(String keycloakUserId) {
        this.keycloakUserId = keycloakUserId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
