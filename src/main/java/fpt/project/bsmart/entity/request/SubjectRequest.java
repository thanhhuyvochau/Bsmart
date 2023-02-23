package fpt.project.bsmart.entity.request;


import fpt.project.bsmart.entity.common.ESubjectCode;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;

public class SubjectRequest implements Serializable {


    @Schema(description = "Mã  subject")
    private ESubjectCode code;
    @Schema(description = "Tên subject")
    private String name ;
    @Schema(description = "id cua course ")
    private List<Long> courseIds ;


    public ESubjectCode getCode() {
        return code;
    }

    public void setCode(ESubjectCode code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getCourseIds() {
        return courseIds;
    }

    public void setCourseIds(List<Long> courseIds) {
        this.courseIds = courseIds;
    }
}
