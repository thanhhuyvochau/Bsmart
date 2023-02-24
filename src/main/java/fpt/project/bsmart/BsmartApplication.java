package fpt.project.bsmart;

import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.Role;
import fpt.project.bsmart.entity.common.EAccountRole;
import fpt.project.bsmart.repository.RoleRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;


@SpringBootApplication
public class BsmartApplication {


    private final RoleRepository roleRepository;

    public BsmartApplication(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    public static void main(String[] args) throws ParseException {

        SpringApplication.run(BsmartApplication.class, args);


    }


    @EventListener(ApplicationReadyEvent.class)
    public void intiDataRole() throws JsonProcessingException {

        List<Role> roles = roleRepository.findAll();
        Map<EAccountRole, Role> roleMap = roles.stream().collect(Collectors.toMap(Role::getCode, Function.identity()));
        for (EAccountRole value : EAccountRole.values()) {
            Role role = roleMap.get(value);
            Role newRole = new Role();
            newRole.setCode(value);
            newRole.setName(value.getLabel());

            if (role == null) {
                roles.add(newRole);
            } else if (!Objects.equals(newRole, role)) {
                role.setCode(value);
                role.setName(value.getLabel());
            }
        }
        roleRepository.saveAll(roles);
//        moodleService.synchronizedRoleFromMoodle();
    }

}