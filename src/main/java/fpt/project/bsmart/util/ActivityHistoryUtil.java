package fpt.project.bsmart.util;


import fpt.project.bsmart.entity.ActivityHistory;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.Course;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.constant.EActivityAction;
import fpt.project.bsmart.entity.constant.EActivityType;
import fpt.project.bsmart.repository.ActivityHistoryRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ActivityHistoryUtil {


    private static ActivityHistoryRepository staticActivityHistoryRepository;

    public ActivityHistoryUtil(ActivityHistoryRepository activityHistoryRepository) {
        staticActivityHistoryRepository = activityHistoryRepository;
    }


    public static void logHistoryForCourseApprove(Long userId, Course course, String message) {
        logHistoryForCourse(userId,EActivityAction.APPROVE,course, message);
    }

    public static void logHistoryForCourseCreated(Long userId, Course course) {
        logHistoryForCourse(userId, EActivityAction.CREATED  ,course, "Đã tạo khóa học !");
    }
//
//    public static void logHistoryForCourseDeleted(Long userId, SubCourse subCourse) {
//        logHistoryForCourse(userId,EActivityAction.DELETED , subCourse,  "Đã xóa khóa học!");
//    }

    private static void logHistoryForCourse(Long userId, EActivityAction action ,Course course, String detail) {
        ActivityHistory activityHistory = new ActivityHistory();
        activityHistory.setType(EActivityType.SUBCOURSE);
        activityHistory.setAction(action);
        activityHistory.setActivityTime(LocalDateTime.now());
        activityHistory.setActivityId(course.getId());
        activityHistory.setActivityName(course.getCode());
        activityHistory.setUserId(userId);
        activityHistory.setDetail(detail);
        staticActivityHistoryRepository.save(activityHistory);
    }


//    public static void logHistoryForMemberOrderCourse(Long userId, Long orderId, SubCourse subCourse) {
//        ActivityHistory activityHistory = new ActivityHistory();
//        activityHistory.setType(EActivityType.ORDER);
//        activityHistory.setActivityTime(LocalDateTime.now());
//        activityHistory.setAction(EActivityAction.ENROLL);
//        activityHistory.setActivityId(orderId);
//        activityHistory.setActivityName(subCourse.getTitle());
//        activityHistory.setUserId(userId);
//        activityHistory.setDetail("Bạn đã tham gia khóa học!");
//        staticActivityHistoryRepository.save(activityHistory);
//    }

    public static void logHistoryForAccountApprove(User user, String message) {
        ActivityHistory activityHistory = new ActivityHistory();
        activityHistory.setType(EActivityType.USER);
        activityHistory.setAction(EActivityAction.APPROVE);
        activityHistory.setActivityTime(LocalDateTime.now());
        activityHistory.setActivityId(user.getId());
        activityHistory.setActivityName(user.getEmail());
        activityHistory.setDetail(message);
        staticActivityHistoryRepository.save(activityHistory);
    }
}