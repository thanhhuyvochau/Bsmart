package fpt.project.bsmart.moodle.response;



import java.util.List;

public class MoodleResourceResponse {
    private Long id;


    private String url;
    private String name;

    private String type;


    private Long instance;

    private List<MoodleAssignmentsResponse.assignments.submissions> submissions;


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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getInstance() {
        return instance;
    }

    public void setInstance(Long instance) {
        this.instance = instance;
    }

    public List<MoodleAssignmentsResponse.assignments.submissions> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<MoodleAssignmentsResponse.assignments.submissions> submissions) {
        this.submissions = submissions;
    }
}
