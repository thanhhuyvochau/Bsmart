package fpt.project.bsmart.entity.common;

public enum EGenderType {
    MALE("Nam"), FEMALE("Nữ"), OTHER("Khác");

    EGenderType(String label) {
        this.label = label;
    }

    private final String label;

    public String getLabel() {
        return label;
    }
}
