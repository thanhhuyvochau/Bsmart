package fpt.project.bsmart.moodle.response;

import java.util.ArrayList;

public class MoodleModuleResponse {
    private Long id;
    private String url;
    private String name;
    private Long instance;
    private int contextid;
    private int visible;
    private boolean uservisible;
    private int visibleoncoursepage;
    private String modicon;
    private String modname;
    private String modplural;
    private Object availability;
    private int indent;
    private String onclick;
    private Object afterlink;
    private String customdata;
    private boolean noviewlink;
    private int completion;
    private ArrayList<MoodleDate> dates;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getInstance() {
        return instance;
    }

    public void setInstance(Long instance) {
        this.instance = instance;
    }

    public int getContextid() {
        return contextid;
    }

    public void setContextid(int contextid) {
        this.contextid = contextid;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public boolean isUservisible() {
        return uservisible;
    }

    public void setUservisible(boolean uservisible) {
        this.uservisible = uservisible;
    }

    public int getVisibleoncoursepage() {
        return visibleoncoursepage;
    }

    public void setVisibleoncoursepage(int visibleoncoursepage) {
        this.visibleoncoursepage = visibleoncoursepage;
    }

    public String getModicon() {
        return modicon;
    }

    public void setModicon(String modicon) {
        this.modicon = modicon;
    }

    public String getModname() {
        return modname;
    }

    public void setModname(String modname) {
        this.modname = modname;
    }

    public String getModplural() {
        return modplural;
    }

    public void setModplural(String modplural) {
        this.modplural = modplural;
    }

    public Object getAvailability() {
        return availability;
    }

    public void setAvailability(Object availability) {
        this.availability = availability;
    }

    public int getIndent() {
        return indent;
    }

    public void setIndent(int indent) {
        this.indent = indent;
    }

    public String getOnclick() {
        return onclick;
    }

    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }

    public Object getAfterlink() {
        return afterlink;
    }

    public void setAfterlink(Object afterlink) {
        this.afterlink = afterlink;
    }

    public String getCustomdata() {
        return customdata;
    }

    public void setCustomdata(String customdata) {
        this.customdata = customdata;
    }

    public boolean isNoviewlink() {
        return noviewlink;
    }

    public void setNoviewlink(boolean noviewlink) {
        this.noviewlink = noviewlink;
    }

    public int getCompletion() {
        return completion;
    }

    public void setCompletion(int completion) {
        this.completion = completion;
    }

    public ArrayList<MoodleDate> getDates() {
        return dates;
    }

    public void setDates(ArrayList<MoodleDate> dates) {
        this.dates = dates;
    }
}
