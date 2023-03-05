package fpt.project.bsmart.config.minio;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Value("${minio.url}")
    private String minioUrl;

//    @Bean
//    @Primary
//    public MinioClient minioClient() {
//        return new MinioClient.Builder()
//                .credentials(accessKey, secretKey)
//                .endpoint(minioUrl)
//                .build();
//    }

}