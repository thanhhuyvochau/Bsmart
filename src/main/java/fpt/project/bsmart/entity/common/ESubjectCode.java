package fpt.project.bsmart.entity.common;

public enum ESubjectCode {


    Toan("Toán Học"),
    VatLy("Vật Lý"),
    HoaHoc("Hoá Học"),
    TiengAnh("Tiếng Anh"),
    SinhHoc("Sinh học"),
    NguVan("Ngữ Văn"),
    TinHoc("Tin học"),

    All("Tất cả");

    public final String label;

    ESubjectCode(String label) {
        this.label = label;
    }
}
