package fpt.project.bsmart.moodle.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MoodleMasterDataRequest {
    private String token;
    private String type;

    private String Host;


    @JsonProperty("Content-Type")
    private String contentType;

    @JsonProperty("Accept")
    private String accept;


    @JsonProperty("Authorization")
    private String authorization;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return Host;
    }

    public void setHost(String host) {
        Host = host;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }
}
