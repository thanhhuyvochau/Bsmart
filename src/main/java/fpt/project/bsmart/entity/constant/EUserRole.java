package fpt.project.bsmart.entity.constant;

public enum EUserRole {
    TEACHER("Giáo viên", "teacher"),
    STUDENT("Học sinh", "student"),
    MANAGER("Quản lý", "manager");

    private final String label;
    private final String name;

    EUserRole(String label, String name) {
        this.label = label;
        this.name = name;
    }

    public String getLabel() {
        return label;
    }
    public String getName(){
        return  name;
    }
}
