package fpt.project.bsmart.entity.constant;

public enum EReferralType {
    DISCOUNT_LINK("link giới thiệu") ;

    private final String name;

    EReferralType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
