package fpt.project.bsmart;

import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.DayOfWeek;
import fpt.project.bsmart.entity.Role;
import fpt.project.bsmart.entity.constant.EDayOfWeekCode;
import fpt.project.bsmart.entity.constant.EUserRole;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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


    private final RoleRepository roleRepository;
    private final DayOfWeekRepository dayOfWeekRepository;

    public BsmartApplication(RoleRepository roleRepository, DayOfWeekRepository dayOfWeekRepository) {
        this.roleRepository = roleRepository;
        this.dayOfWeekRepository = dayOfWeekRepository;
    }


    public static void main(String[] args) throws ParseException {

        SpringApplication.run(BsmartApplication.class, args);


    }

    @EventListener(ApplicationReadyEvent.class)
    public void intiDataRole() throws JsonProcessingException {

        List<Role> roles = roleRepository.findAll();
        Map<EUserRole, Role> roleMap = roles.stream().collect(Collectors.toMap(Role::getCode, Function.identity()));
        for (EUserRole value : EUserRole.values()) {
            Role role = roleMap.get(value);
            Role newRole = new Role();
            newRole.setCode(value);


            if (role == null) {
                roles.add(newRole);
            } else if (!Objects.equals(newRole, role)) {
                role.setCode(value);

            }
        }
        roleRepository.saveAll(roles);

    }

    @EventListener(ApplicationReadyEvent.class)
    public void intiDayOfWeek() throws JsonProcessingException {
        List<DayOfWeek> dayOfWeeks = new ArrayList<>();
        EDayOfWeekCode[] values = EDayOfWeekCode.values();
        Map<EDayOfWeekCode, DayOfWeek> dayOfWeekMap = dayOfWeekRepository.findAll().stream().collect(Collectors.toMap(DayOfWeek::getCode, Function.identity()));
        for (EDayOfWeekCode value : values) {
            DayOfWeek dayOfWeek = dayOfWeekMap.get(value);
            if (dayOfWeek == null) {
                dayOfWeek = new DayOfWeek();
                dayOfWeek.setCode(value);
                dayOfWeek.setName(value.getName());
                dayOfWeeks.add(dayOfWeek);
            }
        }
        if (!dayOfWeeks.isEmpty()) {
            dayOfWeekRepository.saveAll(dayOfWeeks);
        }
    }


}