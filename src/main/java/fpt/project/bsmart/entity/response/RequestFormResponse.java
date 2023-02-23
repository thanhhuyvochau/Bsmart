package fpt.project.bsmart.entity.response;

import fpt.project.bsmart.entity.common.ERequestStatus;
import fpt.project.bsmart.entity.dto.RequestTypeDto;

import java.time.Instant;

public class RequestFormResponse {


    private Long id ;
    private String title;

    private String reason;

    private String name ;

    private String url ;

    private RequestTypeDto requestType;

    private AccountResponse student ;

    private ERequestStatus status ;

    private Instant created ;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ERequestStatus getStatus() {
        return status;
    }

    public void setStatus(ERequestStatus status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RequestTypeDto getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestTypeDto requestType) {
        this.requestType = requestType;
    }

    public AccountResponse getStudent() {
        return student;
    }

    public void setStudent(AccountResponse student) {
        this.student = student;
    }
}
