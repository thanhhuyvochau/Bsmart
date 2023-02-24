package fpt.project.bsmart.entity.common;

public enum EUserRole {
    TEACHER("Giáo viên", "teacher"),
    STUDENT("Học sinh", "student"),
    MANAGER("Quản lý", "manager"),
    ACCOUNTANT("Kế toán", ""),
    ROOT("Root User", "");

    EUserRole(String label, String moodleName) {
        this.label = label;
        this.moodleName = moodleName;
    }

    private final String label;
    private final String moodleName;

    public String getMoodleName() {
        return moodleName;
    }

    public String getLabel() {
        return label;
    }
}
