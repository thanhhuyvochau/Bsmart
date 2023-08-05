package fpt.project.bsmart.entity.constant;

public enum EFeedbackAnswerType {


    ESSAY("Tự luân"),
    MULTIPLECHOICE("Trắc nghiệm");
    private String name;

    public String getName() {
        return name;
    }

    EFeedbackAnswerType(final String name) {
        this.name = name;
    }
}
