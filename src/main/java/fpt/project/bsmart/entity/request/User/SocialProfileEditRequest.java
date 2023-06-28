package fpt.project.bsmart.entity.request.User;

public class SocialProfileEditRequest {
    private String linkedinLink;
    private String facebookLink;


    public String getLinkedinLink() {
        return linkedinLink;
    }

    public void setLinkedinLink(String linkedinLink) {
        this.linkedinLink = linkedinLink;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }


}
