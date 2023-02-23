package fpt.project.bsmart.moodle.request;

import java.io.Serializable;
import java.util.List;

public class CreateCategoryRequest implements Serializable {



    private List<CreateCategoryBody> categories ;

    public static class CreateCategoryBody {

        private String name;

        private Long  parent;

        private String idnumber;

        private String  description;

        private Long descriptionformat;

//        private String  theme;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getParent() {
            return parent;
        }

        public void setParent(Long parent) {
            this.parent = parent;
        }

        public String getIdnumber() {
            return idnumber;
        }

        public void setIdnumber(String idnumber) {
            this.idnumber = idnumber;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Long getDescriptionformat() {
            return descriptionformat;
        }

        public void setDescriptionformat(Long descriptionformat) {
            this.descriptionformat = descriptionformat;
        }

//        public String getTheme() {
//            return theme;
//        }
//
//        public void setTheme(String theme) {
//            this.theme = theme;
//        }
    }

    public List<CreateCategoryBody> getCategories() {
        return categories;
    }

    public void setCategories(List<CreateCategoryBody> categories) {
        this.categories = categories;
    }
}
