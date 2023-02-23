package fpt.project.bsmart.entity.request;


import java.io.Serializable;

public class ChangeInfoClassRequest implements Serializable {


    private Long id;
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
