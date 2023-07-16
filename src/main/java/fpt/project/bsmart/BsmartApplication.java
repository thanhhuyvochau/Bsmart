package fpt.project.bsmart;

import fpt.project.bsmart.entity.Role;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.constant.EUserRole;
import fpt.project.bsmart.repository.RoleRepository;
import fpt.project.bsmart.repository.UserRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Optional;


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
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public BsmartApplication(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    public static void main(String[] args) {

        SpringApplication.run(BsmartApplication.class, args);
    }
//    @EventListener(ContextRefreshedEvent.class)
//    public void initAdmin() {
//        User admin = new User();
//        admin.setFullName("Admin");
//        admin.setStatus(true);
//        admin.setPassword(encoder.encode("Qw#45678"));
//        Optional<Role> roleByCode = roleRepository.findRoleByCode(EUserRole.ADMIN);
//        admin.getRoles().add(roleByCode.get());
//        userRepository.save(admin);
//    }
}





