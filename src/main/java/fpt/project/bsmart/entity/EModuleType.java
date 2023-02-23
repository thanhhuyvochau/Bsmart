package fpt.project.bsmart.entity;

public enum EModuleType {
    QUIZ("quiz"), LESSON("lesson") ,  ASSIGN("assign")  ;

    EModuleType(String label) {
        this.label = label;
    }

    private final String label;

    public String getLabel() {
        return label;
    }
}
