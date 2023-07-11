package fpt.project.bsmart.entity.request.activity;

import fpt.project.bsmart.entity.request.ActivityRequest;
import org.springframework.web.multipart.MultipartFile;

public class MentorCreateResourceRequest extends ActivityRequest {
    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
