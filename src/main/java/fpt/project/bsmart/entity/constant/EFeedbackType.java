package fpt.project.bsmart.entity.constant;

public enum EFeedbackType {
    FEEDBACK("Feedback"), REPORT("Report");
    private String name;

    public String getName() {
        return name;
    }

    EFeedbackType(final String name) {
        this.name = name;
    }
}
