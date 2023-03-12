package fpt.project.bsmart.util;

import io.minio.ObjectWriteResponse;

public class ImageUrlUtil {

    public static String buildUrl(String minioUrl, ObjectWriteResponse objectWriteResponse) {
        return minioUrl.concat("/").concat(objectWriteResponse.bucket()).concat("/").concat(objectWriteResponse.object());
    }
}
