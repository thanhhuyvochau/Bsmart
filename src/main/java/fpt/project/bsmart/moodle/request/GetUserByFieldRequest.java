package fpt.project.bsmart.moodle.request;

import java.util.ArrayList;
import java.util.List;

public class GetUserByFieldRequest {
    private String field = "username";
    private List<String> values = new ArrayList<>();

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
