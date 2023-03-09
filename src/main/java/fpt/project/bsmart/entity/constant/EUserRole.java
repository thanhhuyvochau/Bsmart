package fpt.project.bsmart.entity.constant;

public enum EUserRole {
    TEACHER("Giáo viên", "teacher"),
    STUDENT("Học sinh", "student"),
    MANAGER("Quản lý", "manager");



    private final String label;

    EUserRole(String label, String name) {
        this.label = label;
    }


    public String getLabel() {
        return label;
    }
}
