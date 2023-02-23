package fpt.project.bsmart.entity.dto;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

public class RequestFormReplyDto implements Serializable {

    private String content;


    private MultipartFile file;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
