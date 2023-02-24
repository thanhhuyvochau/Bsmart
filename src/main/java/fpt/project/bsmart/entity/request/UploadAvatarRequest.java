package fpt.project.bsmart.entity.request;

import org.springframework.web.multipart.MultipartFile;

public class UploadAvatarRequest {

    private MultipartFile file ;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
