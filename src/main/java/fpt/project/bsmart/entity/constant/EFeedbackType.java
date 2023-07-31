package fpt.project.bsmart.entity.constant;

public enum EFeedbackType {
    COURSE("Course"), REPORT("Report");
    private String name;

    public String getName() {
        return name;
    }

    EFeedbackType(final String name){
        this.name = name;
    }
}
