package fpt.project.bsmart.moodle.response;


import java.util.List;

public class MoodleSubmissionsResponse {
    private Long id;
    private Long userid;
    private Long attemptnumber;
    private Long timecreated;
    private Long timemodified;
    private String status;
    private Long groupid;

    private List<MoodleSubmissionsResponse.plugins> plugins;


    public static class plugins {
        private String type;
        private String name;

        private List<plugins.fileareas> fileareas;

        public static class fileareas {

            private String area;

            public class files {
                private String filename;
                private String filepath;
                private Long filesize;
                private String fileurl;
                private String timemodified;
                private String mimetype;
                private Boolean isexternalfile;

                public String getFilename() {
                    return filename;
                }

                public void setFilename(String filename) {
                    this.filename = filename;
                }

                public String getFilepath() {
                    return filepath;
                }

                public void setFilepath(String filepath) {
                    this.filepath = filepath;
                }

                public Long getFilesize() {
                    return filesize;
                }

                public void setFilesize(Long filesize) {
                    this.filesize = filesize;
                }

                public String getFileurl() {
                    return fileurl;
                }

                public void setFileurl(String fileurl) {
                    this.fileurl = fileurl;
                }

                public String getTimemodified() {
                    return timemodified;
                }

                public void setTimemodified(String timemodified) {
                    this.timemodified = timemodified;
                }

                public String getMimetype() {
                    return mimetype;
                }

                public void setMimetype(String mimetype) {
                    this.mimetype = mimetype;
                }

                public Boolean getIsexternalfile() {
                    return isexternalfile;
                }

                public void setIsexternalfile(Boolean isexternalfile) {
                    this.isexternalfile = isexternalfile;
                }
            }

            public String getArea() {
                return area;
            }

            public void setArea(String area) {
                this.area = area;
            }
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<MoodleSubmissionsResponse.plugins.fileareas> getFileareas() {
            return fileareas;
        }

        public void setFileareas(List<MoodleSubmissionsResponse.plugins.fileareas> fileareas) {
            this.fileareas = fileareas;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getAttemptnumber() {
        return attemptnumber;
    }

    public void setAttemptnumber(Long attemptnumber) {
        this.attemptnumber = attemptnumber;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getGroupid() {
        return groupid;
    }

    public void setGroupid(Long groupid) {
        this.groupid = groupid;
    }

    public List<MoodleSubmissionsResponse.plugins> getPlugins() {
        return plugins;
    }

    public void setPlugins(List<MoodleSubmissionsResponse.plugins> plugins) {
        this.plugins = plugins;
    }
}
