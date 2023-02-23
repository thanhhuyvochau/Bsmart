package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.entity.common.EEvaluateType;


public class FeedBacClassDto {
    private Long studentId ;

    private String content ;

    private EEvaluateType eEvaluateType ;


    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public EEvaluateType geteEvaluateType() {
        return eEvaluateType;
    }

    public void seteEvaluateType(EEvaluateType eEvaluateType) {
        this.eEvaluateType = eEvaluateType;
    }
}
