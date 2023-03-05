package fpt.project.bsmart.entity.constant;

public enum EImageType {
    AVATAR("Ảnh đại diện");

    private final String name;

    EImageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
