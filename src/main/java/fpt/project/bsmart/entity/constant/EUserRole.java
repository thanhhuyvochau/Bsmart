package fpt.project.bsmart.entity.constant;

public enum EUserRole {
    TEACHER("Giáo viên", "teacher", "ROLE_TEACHER"),
    STUDENT("Học sinh", "student", "ROLE_STUDENT"),
    MANAGER("Quản lý", "manager", "ROLE_MANAGER"),
    ADMIN("Quản trị viên", "admin", "ROLE_ADMIN");


    private final String label;
    private final String name;

    private final String keycloakRole;

    EUserRole(String label, String name, String keycloakRole) {
        this.label = label;
        this.name = name;
        this.keycloakRole = keycloakRole;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name;
    }

    public String getKeycloakRole() {
        return keycloakRole;
    }
}
