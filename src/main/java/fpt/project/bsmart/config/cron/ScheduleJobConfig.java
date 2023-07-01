package fpt.project.bsmart.config.cron;

import fpt.project.bsmart.entity.DayOfWeek;
import fpt.project.bsmart.entity.Role;
import fpt.project.bsmart.entity.constant.EDayOfWeekCode;
import fpt.project.bsmart.entity.constant.EUserRole;

import fpt.project.bsmart.repository.DayOfWeekRepository;
import fpt.project.bsmart.repository.RoleRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
public class ScheduleJobConfig {

    private final DayOfWeekRepository dayOfWeekRepository;



    private final RoleRepository roleRepository;

    public ScheduleJobConfig(DayOfWeekRepository dayOfWeekRepository, RoleRepository roleRepository) {
        this.dayOfWeekRepository = dayOfWeekRepository;
        this.roleRepository = roleRepository;
    }


    @Scheduled(fixedDelay = 20 * 60 * 1000)
    private void initializeDayOfWeekIfNotExist() {
        System.out.println("START INITIALIZE DAY OF WEEK");
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

//    @Scheduled(fixedDelay = 20 * 60 * 1000)
//    private void initializeActivityTypeIfNotExist() {
//
//        System.out.println("START INITIALIZE ACTIVITY TYPE");
//        List<ActivityType> newActivityType = Arrays.asList(
//                new ActivityType("QUIZ", "quiz"),
//                new ActivityType("ASSIGNMENT", "assignment")
//        );
//        newActivityType.stream()
//                .filter(activityType -> activityTypeRepository.findByCode(activityType.getCode()) == null)
//                .forEach(activityTypeRepository::save);
//    }

    @Scheduled(fixedDelay = 20 * 60 * 1000)
    private void initializeRoleIfNotExist() {

        System.out.println("START INITIALIZE ROLE");
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


}
