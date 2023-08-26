package fpt.project.bsmart;

import fpt.project.bsmart.repository.RoleRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;


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
@Transactional
public class BsmartApplication {
    @Autowired
    private PasswordEncoder encoder;
    private final RoleRepository roleRepository;

    public BsmartApplication(PasswordEncoder encoder, RoleRepository roleRepository) {
        this.encoder = encoder;
        this.roleRepository = roleRepository;
    }

    public static void main(String[] args) {

        SpringApplication.run(BsmartApplication.class, args);

    }
}





