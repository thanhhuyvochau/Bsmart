package fpt.project.bsmart.moodle.response;

public class MoodleUserResponse {
    private int id;
    private String username;
    private String firstname;
    private String lastname;
    private String fullname;
    private String email;
    private String department;
    private int firstaccess;
    private int lastaccess;
    private String auth;
    private boolean suspended;
    private boolean confirmed;
    private String lang;
    private String theme;
    private String timezone;
    private int mailformat;
    private String profileimageurlsmall;
    private String profileimageurl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getFirstaccess() {
        return firstaccess;
    }

    public void setFirstaccess(int firstaccess) {
        this.firstaccess = firstaccess;
    }

    public int getLastaccess() {
        return lastaccess;
    }

    public void setLastaccess(int lastaccess) {
        this.lastaccess = lastaccess;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public int getMailformat() {
        return mailformat;
    }

    public void setMailformat(int mailformat) {
        this.mailformat = mailformat;
    }

    public String getProfileimageurlsmall() {
        return profileimageurlsmall;
    }

    public void setProfileimageurlsmall(String profileimageurlsmall) {
        this.profileimageurlsmall = profileimageurlsmall;
    }

    public String getProfileimageurl() {
        return profileimageurl;
    }

    public void setProfileimageurl(String profileimageurl) {
        this.profileimageurl = profileimageurl;
    }
}
