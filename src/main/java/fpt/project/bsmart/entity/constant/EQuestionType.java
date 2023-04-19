package fpt.project.bsmart.entity.constant;

public enum EQuestionType {
    MULTIPLE_CHOICE("M"),
    FILL_THE_ANSWER("F");
    private final String type;

    EQuestionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
