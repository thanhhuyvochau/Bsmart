package fpt.project.bsmart.entity.dto;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

public class RequestFormDto  implements Serializable {

    private String title;

    private String reason;

    private Long requestTypeId ;

     private   MultipartFile file ;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
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

    public Long getRequestTypeId() {
        return requestTypeId;
    }

    public void setRequestTypeId(Long requestTypeId) {
        this.requestTypeId = requestTypeId;
    }
}
