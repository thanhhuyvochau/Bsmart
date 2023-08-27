package fpt.project.bsmart.config.cron;

import fpt.project.bsmart.director.NotificationDirector;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.constant.ECourseClassStatus;
import fpt.project.bsmart.entity.constant.EDayOfWeekCode;
import fpt.project.bsmart.entity.constant.EUserRole;
import fpt.project.bsmart.repository.ClassRepository;
import fpt.project.bsmart.repository.DayOfWeekRepository;
import fpt.project.bsmart.repository.NotificationRepository;
import fpt.project.bsmart.repository.RoleRepository;
import fpt.project.bsmart.service.Impl.TransactionService;
import fpt.project.bsmart.util.ClassUtil;
import fpt.project.bsmart.util.TimeInWeekUtil;
import fpt.project.bsmart.util.WebSocketUtil;
import fpt.project.bsmart.util.email.EmailUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
@Transactional
public class ScheduleJobConfig {

    private final DayOfWeekRepository dayOfWeekRepository;

    private final ClassRepository classRepository;

    private final RoleRepository roleRepository;
    private final TransactionService transactionService;
    private final EmailUtil emailUtil;
    private final NotificationRepository notificationRepository;
    private final WebSocketUtil webSocketUtil;

    public ScheduleJobConfig(DayOfWeekRepository dayOfWeekRepository, ClassRepository classRepository, RoleRepository roleRepository, TransactionService transactionService, EmailUtil emailUtil, NotificationRepository notificationRepository, WebSocketUtil webSocketUtil) {
        this.dayOfWeekRepository = dayOfWeekRepository;
        this.classRepository = classRepository;
        this.roleRepository = roleRepository;
        this.transactionService = transactionService;
        this.emailUtil = emailUtil;
        this.notificationRepository = notificationRepository;
        this.webSocketUtil = webSocketUtil;
    }


    @Scheduled(fixedDelay = 20 * 60 * 1000)
    public void initializeDayOfWeekIfNotExist() {
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
    public void initializeRoleIfNotExist() {

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

    @Scheduled(cron = "0 0 0 * * *")
    /** Tự động mở lớp 12h đêm mỗi ngày  */
    public void openClassAutomatic() {
        Instant now = Instant.now();
        List<Class> classesStartToday = classRepository.findByStartDate(now.truncatedTo(ChronoUnit.DAYS));
        List<Class> satisfyMinClasses = classesStartToday.stream().filter(clazz -> clazz.getMinStudent() < clazz.getStudentClasses().size()).collect(Collectors.toList());
        // Auto open class if satisfy min number of students
        for (Class satisfyMinClass : satisfyMinClasses) {
            List<TimeTable> timeTables = TimeInWeekUtil.generateTimeTable(satisfyMinClass.getTimeInWeeks(), satisfyMinClass.getNumberOfSlot(), satisfyMinClass.getStartDate(), satisfyMinClass);
            satisfyMinClass.getTimeTables().clear();
            satisfyMinClass.getTimeTables().addAll(timeTables);
            satisfyMinClass.setStatus(ECourseClassStatus.STARTING);
            emailUtil.sendStartClass(satisfyMinClass);
            List<User> studentOfClass = satisfyMinClass.getStudentClasses().stream().map(StudentClass::getStudent).collect(Collectors.toList());
            Notification notification = NotificationDirector.buildStartClass(satisfyMinClass, satisfyMinClass.getCourse().getCreator(), studentOfClass);
            notificationRepository.save(notification);
            webSocketUtil.sendPrivateNotification(notification);
        }
        List<Class> unSatisfyMinClasses = classesStartToday.stream().filter(clazz -> clazz.getMinStudent() > clazz.getStudentClasses().size()).collect(Collectors.toList());
        for (Class unSatisfyMinClass : unSatisfyMinClasses) {
            unSatisfyMinClass.setStatus(ECourseClassStatus.UNSATISFY);
            emailUtil.sendEndClassToMentor(unSatisfyMinClass);
            Notification notification = NotificationDirector.buildCancelClass(unSatisfyMinClass);
            notificationRepository.save(notification);
            webSocketUtil.sendPrivateNotification(notification);
        }
        /**Refund for all student of unstatisfy min student of class */
        transactionService.refundClassFeeToStudentWallet(unSatisfyMinClasses);
        // Send notification for teacher if not satisfy and student to inform class is closed
        classRepository.saveAll(classesStartToday);
        System.out.println("---------Task executed at 12 AM (midnight) for Open Class-----------");
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void closeClassAutomatic() {
        Instant yesterday = Instant.now().minus(1, ChronoUnit.DAYS);
        List<Class> classesEndToday = classRepository.findByEndDateAndStatus(yesterday, ECourseClassStatus.STARTING);
        classesEndToday.forEach(ClassUtil::handleCloseClassEvent);
        classRepository.saveAll(classesEndToday);
    }
}
