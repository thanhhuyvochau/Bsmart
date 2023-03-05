package fpt.project.bsmart.moodle.request;

import java.io.Serializable;
import java.util.List;

public class GetCategoryRequest implements Serializable {


    private List<MoodleCategoryBody> criteria;

    public static class MoodleCategoryBody {

        private String key;

        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public List<MoodleCategoryBody> getCriteria() {
        return criteria;
    }

    public void setCriteria(List<MoodleCategoryBody> criteria) {
        this.criteria = criteria;
    }
}
