package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.entity.constant.EImageType;
import org.springframework.web.multipart.MultipartFile;

public class UploadImageRequest {
    private EImageType imageType;
    private MultipartFile file;
    private Boolean status;

    public EImageType getImageType() {
        return imageType;
    }

    public void setImageType(EImageType imageType) {
        this.imageType = imageType;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
