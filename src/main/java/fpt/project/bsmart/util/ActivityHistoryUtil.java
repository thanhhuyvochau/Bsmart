package fpt.project.bsmart.util;


import fpt.project.bsmart.entity.ActivityHistory;
import fpt.project.bsmart.entity.constant.EActivityType;
import fpt.project.bsmart.repository.ActivityHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ActivityHistoryUtil {


    private static ActivityHistoryRepository staticActivityHistoryRepository;

    public ActivityHistoryUtil(ActivityHistoryRepository activityHistoryRepository) {
        staticActivityHistoryRepository = activityHistoryRepository ;
    }
    public static void logHistoryForAccountApprove(Long userId, String message) {
        ActivityHistory activityHistory = new ActivityHistory();
        activityHistory.setType(EActivityType.USER);
        activityHistory.setActivityTime(LocalDateTime.now());
        activityHistory.setActivityId(userId);
        activityHistory.setDetail(message);
        staticActivityHistoryRepository.save(activityHistory);
    }


    public static void logHistoryForCourseApprove(Long userId,Long subCourseId, String message) {
        logHistoryForCourse(userId,subCourseId, message);
    }

    public static void logHistoryForCourseCreated(Long userId ,Long subCourseId) {
        logHistoryForCourse(userId,subCourseId, "Đã tạo khóa học !");
    }

    public static void logHistoryForCourseDeleted(Long userId,Long subCourseId) {
        logHistoryForCourse( userId ,subCourseId, "Đã xóa khóa học!");
    }

    private static void logHistoryForCourse(Long userId ,Long subCourseId, String detail) {
        ActivityHistory activityHistory = new ActivityHistory();
        activityHistory.setType(EActivityType.SUBCOURSE);
        activityHistory.setActivityTime(LocalDateTime.now());
        activityHistory.setActivityId(subCourseId);
        activityHistory.setUserId(userId);
        activityHistory.setDetail(detail);
        staticActivityHistoryRepository.save(activityHistory);
    }


    public static void logHistoryForOrderCourse(Long userId , Long orderId) {
        ActivityHistory activityHistory = new ActivityHistory() ;
        activityHistory.setType(EActivityType.USER);
        activityHistory.setActivityTime(LocalDateTime.now());
        activityHistory.setActivityId(orderId);
        activityHistory.setUserId(userId);
        activityHistory.setDetail("Bạn đã tham gia khóa học!");

        staticActivityHistoryRepository.save(activityHistory) ;
    }
}