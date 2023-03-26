package fpt.project.bsmart.entity.constant;

public enum EDayOfWeekCode {
    SUNDAY("Chủ nhật"),
    MONDAY("Thứ hai"),
    TUESDAY("Thứ ba"),
    WEDNESDAY("Thứ tư"),
    THURSDAY("Thứ năm"),
    FRIDAY("Thứ sáu"),
    SATURDAY("Thứ bảy");
    private final String name;

    EDayOfWeekCode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
