//package fpt.project.bsmart.config.cron;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//
//@Configuration
//@EnableScheduling
//public class ScheduleJobConfig {
//    private final IClassService classService;
//
//    public ScheduleJobConfig(IClassService classService) {
//        this.classService = classService;
//    }
//
//    @Scheduled(cron = "0 0 0 * * *") // Sẽ chạy vào lúc 0 giờ mỗi ngày
//    public void detectExpireRecruitedClass() {
//        classService.detectExpireRecruitingClass();
//    }
//
//    @Scheduled(cron = "0 0 0 * * *") // Sẽ chạy vào lúc 0 giờ mỗi ngày
//    public void detectExpireConfirmationClass() {
//        classService.detectExpireConfirmation();
//    }
//}
