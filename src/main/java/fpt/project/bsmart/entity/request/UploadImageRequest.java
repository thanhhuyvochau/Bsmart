package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.entity.constant.EImageType;
import org.springframework.web.multipart.MultipartFile;

public class UploadImageRequest {
    private EImageType imageType;
    private MultipartFile file ;

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
}
