package fpt.project.bsmart.util.adapter;


import org.springframework.stereotype.Service;

@Service
public class MinioAdapter {

//    private final MinioClient minioClient;
//
//    public MinioAdapter(MinioClient minioClient) {
//        this.minioClient = minioClient;
//    }
//
//    @Value("${minio.bucket}")
//    String defaultBucketName;
//
//    public ObjectWriteResponse uploadFile(String name, String contentType, InputStream content, long size) {
//        try {
//            PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(defaultBucketName)
//                    .stream(content, size, -1)
//                    .contentType(contentType)
//                    .object(name)
//                    .build();
//            return minioClient.putObject(objectArgs);
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage());
//        }
//
//    }
}