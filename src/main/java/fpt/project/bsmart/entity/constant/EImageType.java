package fpt.project.bsmart.entity.constant;

public enum EImageType {
    DEFAULT("Ảnh khoa hoc mặc định"),

    COURSE("Ảnh khoa hoc"),
    AVATAR("Ảnh đại diện"),
    FRONTCI("Mặt trước Căn cước công dân") ,
    BACKCI("Mặt sau ăn cước công dân"),

    DEGREE("bằng cấp") ,

    CLASS("Ảnh lớp học") ;
    private final String name;

    EImageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
