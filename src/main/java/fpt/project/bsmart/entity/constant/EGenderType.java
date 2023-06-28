package fpt.project.bsmart.entity.constant;

public enum EGenderType {
    MALE("Nam"), FEMALE("Nữ") ;

    EGenderType(String label) {
        this.label = label;
    }

    private final String label;

    public String getLabel() {
        return label;
    }
}
