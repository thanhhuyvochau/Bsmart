package fpt.project.bsmart.entity.constant;

public enum EImageType {

    COURSE("Ảnh khoa hoc"),
    AVATAR("Ảnh đại diện"),
    FRONTCI("Mặt trước Căn cước công dân") ,
    BACKCI("Mặt sau ăn cước công dân"),

    DEGREE("bằng cấp") ;
    private final String name;

    EImageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
