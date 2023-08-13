package fpt.project.bsmart.config;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

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
    public String resetPasswordTemplate() {
        return ResourceReader.readFileToString("reset-password.txt");
    }

    @Bean
    public String orderTemplate() {
        return ResourceReader.readFileToString("order.txt");
    }

    @Bean
    public ArrayList<String> offensiveWord() {
        String offensiveWordString = ResourceReader.readFileToString("offensive-word.txt");
        String[] elements = offensiveWordString.split(",");
        ArrayList<String> offensiveWords = new ArrayList<>();
        for (String element : elements) {
            String trimmedElement = element.trim();
            if (trimmedElement.startsWith("'") && trimmedElement.endsWith("'")) {
                offensiveWords.add(trimmedElement.substring(1, trimmedElement.length() - 1));
            }
        }
        System.out.println("Generate offensive word list success, total element: " + offensiveWords.size());
        return offensiveWords;
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {

        JavaTimeModule module = new JavaTimeModule();
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);

        objectMapper.registerModule(module);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


        return objectMapper;
    }
}