package fpt.project.bsmart.entity.response;


public class RequestFormReplyResponse {
    private Long id;

    private Long requestId ;

    private Long replierId ;

    private String content ;

    private String url ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getReplierId() {
        return replierId;
    }

    public void setReplierId(Long replierId) {
        this.replierId = replierId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
