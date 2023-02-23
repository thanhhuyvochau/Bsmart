package fpt.project.bsmart.moodle.response;

import java.util.List;

public class MoodleCourseResponse {


    private Long id;
    private String shortname;
    private Long categoryid;
    private Long categorysortorder;
    private String fullname;
    private String displayname;
    private String idnumber;

    private String summary;
    private Long summaryformat;
    private String format;
    private Long showgrades;
    private Long newsitems;
    private Long startdate;

    private Long enddate;
    private Long numsections;
    private Long maxbytes;
    private Long showreports;
    private Long visible;
    private Long groupmode;


    private Long groupmodeforce;
    private Long defaultgroupingid;
    private Long timecreated;
    private Long timemodified;
    private Long enablecompletion;
    private Long completionnotify;


    private String lang;
    private String forcetheme;

    private List<CourseFormatOption> courseformatoptions;


    private boolean showactivitydates;
    private String showcompletionconditions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public Long getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(Long categoryid) {
        this.categoryid = categoryid;
    }

    public Long getCategorysortorder() {
        return categorysortorder;
    }

    public void setCategorysortorder(Long categorysortorder) {
        this.categorysortorder = categorysortorder;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Long getSummaryformat() {
        return summaryformat;
    }

    public void setSummaryformat(Long summaryformat) {
        this.summaryformat = summaryformat;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Long getShowgrades() {
        return showgrades;
    }

    public void setShowgrades(Long showgrades) {
        this.showgrades = showgrades;
    }

    public Long getNewsitems() {
        return newsitems;
    }

    public void setNewsitems(Long newsitems) {
        this.newsitems = newsitems;
    }

    public Long getStartdate() {
        return startdate;
    }

    public void setStartdate(Long startdate) {
        this.startdate = startdate;
    }

    public Long getEnddate() {
        return enddate;
    }

    public void setEnddate(Long enddate) {
        this.enddate = enddate;
    }

    public Long getNumsections() {
        return numsections;
    }

    public void setNumsections(Long numsections) {
        this.numsections = numsections;
    }

    public Long getMaxbytes() {
        return maxbytes;
    }

    public void setMaxbytes(Long maxbytes) {
        this.maxbytes = maxbytes;
    }

    public Long getShowreports() {
        return showreports;
    }

    public void setShowreports(Long showreports) {
        this.showreports = showreports;
    }

    public Long getVisible() {
        return visible;
    }

    public void setVisible(Long visible) {
        this.visible = visible;
    }

    public Long getGroupmode() {
        return groupmode;
    }

    public void setGroupmode(Long groupmode) {
        this.groupmode = groupmode;
    }

    public Long getGroupmodeforce() {
        return groupmodeforce;
    }

    public void setGroupmodeforce(Long groupmodeforce) {
        this.groupmodeforce = groupmodeforce;
    }

    public Long getDefaultgroupingid() {
        return defaultgroupingid;
    }

    public void setDefaultgroupingid(Long defaultgroupingid) {
        this.defaultgroupingid = defaultgroupingid;
    }

    public Long getTimecreated() {
        return timecreated;
    }

    public void setTimecreated(Long timecreated) {
        this.timecreated = timecreated;
    }

    public Long getTimemodified() {
        return timemodified;
    }

    public void setTimemodified(Long timemodified) {
        this.timemodified = timemodified;
    }

    public Long getEnablecompletion() {
        return enablecompletion;
    }

    public void setEnablecompletion(Long enablecompletion) {
        this.enablecompletion = enablecompletion;
    }

    public Long getCompletionnotify() {
        return completionnotify;
    }

    public void setCompletionnotify(Long completionnotify) {
        this.completionnotify = completionnotify;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getForcetheme() {
        return forcetheme;
    }

    public void setForcetheme(String forcetheme) {
        this.forcetheme = forcetheme;
    }

    public List<CourseFormatOption> getCourseformatoptions() {
        return courseformatoptions;
    }

    public void setCourseformatoptions(List<CourseFormatOption> courseformatoptions) {
        this.courseformatoptions = courseformatoptions;
    }

    public boolean isShowactivitydates() {
        return showactivitydates;
    }

    public void setShowactivitydates(boolean showactivitydates) {
        this.showactivitydates = showactivitydates;
    }

    public String getShowcompletionconditions() {
        return showcompletionconditions;
    }

    public void setShowcompletionconditions(String showcompletionconditions) {
        this.showcompletionconditions = showcompletionconditions;
    }
}
