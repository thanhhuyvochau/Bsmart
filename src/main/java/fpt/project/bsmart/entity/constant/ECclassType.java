package fpt.project.bsmart.entity.constant;

public enum ECclassType {
    BEGINNER("Căn bản"), INTERMEDIATE("Sơ cấp"), ADVANCED("Trung cấp"), EXPERT("Nâng cao");

    private final String name;

    ECclassType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
