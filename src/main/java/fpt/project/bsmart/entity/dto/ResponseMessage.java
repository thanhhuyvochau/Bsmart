package fpt.project.bsmart.entity.dto;

public class ResponseMessage {
    private String viTitle;

    private String viContent;

    private String data;


    public ResponseMessage() {
    }

    public ResponseMessage(String viTitle, String viContent, String data) {
        this.viTitle = viTitle;
        this.viContent = viContent;
        this.data = data;
    }

    public String getViTitle() {
        return viTitle;
    }

    public void setViTitle(String viTitle) {
        this.viTitle = viTitle;
    }

    public String getViContent() {
        return viContent;
    }

    public void setViContent(String viContent) {
        this.viContent = viContent;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
