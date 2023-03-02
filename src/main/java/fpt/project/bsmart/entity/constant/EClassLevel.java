package fpt.project.bsmart.entity.constant;

public enum EClassLevel {
    FOUNDATION("Căn bản"), ELEMENTARY("Sơ cấp"), INTERMEDIATE("Trung cấp"), ADVANCED("Nâng cao");

    private final String name;

    EClassLevel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
