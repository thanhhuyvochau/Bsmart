package fpt.project.bsmart.moodle.response;

public class MoodleCategoryResponse {


    private Long id;
    private String name;
    private String idnumber;
    private String  description;
    private Long descriptionformat;
    private Long parent;
    private Long sortorder;

    private Long coursecount;
    private Long visible;
    private String visibleold;
    private Long timemodified;
    private Long depth;
    private String path;

    private String theme;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public Long getSortorder() {
        return sortorder;
    }

    public void setSortorder(Long sortorder) {
        this.sortorder = sortorder;
    }

    public Long getCoursecount() {
        return coursecount;
    }

    public void setCoursecount(Long coursecount) {
        this.coursecount = coursecount;
    }

    public Long getVisible() {
        return visible;
    }

    public void setVisible(Long visible) {
        this.visible = visible;
    }

    public String getVisibleold() {
        return visibleold;
    }

    public void setVisibleold(String visibleold) {
        this.visibleold = visibleold;
    }

    public Long getTimemodified() {
        return timemodified;
    }

    public void setTimemodified(Long timemodified) {
        this.timemodified = timemodified;
    }

    public Long getDepth() {
        return depth;
    }

    public void setDepth(Long depth) {
        this.depth = depth;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
