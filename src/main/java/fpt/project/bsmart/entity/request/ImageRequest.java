package fpt.project.bsmart.entity.request;


import fpt.project.bsmart.entity.constant.EImageType;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

public class ImageRequest implements Serializable {

    private EImageType type;
    private MultipartFile file;

    public EImageType getType() {
        return type;
    }

    public void setType(EImageType type) {
        this.type = type;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
