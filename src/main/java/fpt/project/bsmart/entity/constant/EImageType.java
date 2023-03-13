package fpt.project.bsmart.entity.constant;

public enum EImageType {

    COURSE("Ảnh khoa hoc"),
    AVATAR("Ảnh đại diện"),
    CI("Căn cước công dân");
    private final String name;

    EImageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
