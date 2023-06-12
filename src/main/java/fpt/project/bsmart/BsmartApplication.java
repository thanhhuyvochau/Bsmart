package fpt.project.bsmart;

import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.ActivityType;
import fpt.project.bsmart.entity.DayOfWeek;
import fpt.project.bsmart.entity.Role;
import fpt.project.bsmart.entity.constant.EDayOfWeekCode;
import fpt.project.bsmart.entity.constant.EUserRole;
import fpt.project.bsmart.repository.ActivityTypeRepository;
import fpt.project.bsmart.repository.DayOfWeekRepository;
import fpt.project.bsmart.repository.RoleRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(title = "Apply Default Global SecurityScheme in springdoc-openapi", version = "1.0.0"),
        security = {@SecurityRequirement(name = "Bearer Authentication")})
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class BsmartApplication {


    public static void main(String[] args) {

        SpringApplication.run(BsmartApplication.class, args);
    }
// commit 1

}





