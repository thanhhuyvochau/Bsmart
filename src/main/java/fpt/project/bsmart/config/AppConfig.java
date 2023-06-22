package fpt.project.bsmart.config;

import net.sourceforge.tess4j.Tesseract;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public String verifyAccountTemplate() {
        return ResourceReader.readFileToString("verify-account.txt");
    }
    @Bean
    public String orderTemplate() {
        return ResourceReader.readFileToString("order.txt");
    }

    @Bean
    Tesseract getTesseract(){
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
        return tesseract;
    }
}