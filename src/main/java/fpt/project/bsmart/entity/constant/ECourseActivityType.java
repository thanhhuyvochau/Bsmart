package fpt.project.bsmart.entity.constant;

public enum ECourseActivityType {
    QUIZ("quiz"), ASSIGNMENT("assignment"), LESSON("lesson"), RESOURCE("resource"), SECTION("section"), ANNOUNCEMENT("announcement");
    private final String label;
    ECourseActivityType(String label) {
        this.label = label;
    }
    }
