package fpt.project.bsmart.entity.constant;

public enum ETypeLearn {
    DIRECT("Trực tiếp"), ONLINE("trực tuyến") ;

    private final String name;

    ETypeLearn(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
