package fpt.project.bsmart.util;


import fpt.project.bsmart.entity.ActivityHistory;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.Course;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.constant.EActivityAction;
import fpt.project.bsmart.entity.constant.EActivityType;
import fpt.project.bsmart.repository.ActivityHistoryRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;

@Component
public class ActivityHistoryUtil {


    private static ActivityHistoryRepository staticActivityHistoryRepository;

    public ActivityHistoryUtil(ActivityHistoryRepository activityHistoryRepository) {
        staticActivityHistoryRepository = activityHistoryRepository;
    }


//    public static void logHistoryForCourseApprove(Long userId, Course course, String message) {
//        logHistoryForCourse(userId,EActivityAction.APPROVE,course, message);
//    }

    public static void logHistoryForMentorSendRequestClass(Long userId,Class clazz) {
        ActivityHistory activityHistory = new ActivityHistory();
        activityHistory.setType(EActivityType.ClASS);
        activityHistory.setAction( EActivityAction.REQUEST );
        activityHistory.setActivityTime(LocalDateTime.now());
        activityHistory.setActivityId(clazz.getId());
        activityHistory.setActivityName(clazz.getCode());
        activityHistory.setUserId(userId);
        activityHistory.setDetail("Đã gửi yêu cầu phê duyệt lớp hoc!");
        staticActivityHistoryRepository.save(activityHistory);
    }



    public static void logHistoryForMentorSendRequestCourse(Long userId,Course course) {
        ActivityHistory activityHistory = new ActivityHistory();
        activityHistory.setType(EActivityType.COURSE);
        activityHistory.setAction( EActivityAction.REQUEST);
        activityHistory.setActivityTime(LocalDateTime.now());
        activityHistory.setActivityId(course.getId());
        activityHistory.setActivityName(course.getCode());
        activityHistory.setUserId(userId);
        activityHistory.setCount(1);
        activityHistory.setDetail("Đã gửi yêu cầu phê duyệt khóa học !");
        staticActivityHistoryRepository.save(activityHistory);
    }
    public static void logHistoryForAccountSendRequestApprove(User user) {
        ActivityHistory activityHistory = new ActivityHistory();
        activityHistory.setType(EActivityType.USER);
        activityHistory.setAction(EActivityAction.REQUEST);
        activityHistory.setActivityTime(LocalDateTime.now());
        activityHistory.setActivityId(user.getId());
        activityHistory.setActivityName(user.getEmail());
        activityHistory.setUserId(user.getId());
        activityHistory.setCount(1);
        activityHistory.setDetail("Đã gửi yêu cầu phê duyệt tài khoản.");
        activityHistory.setLastModified(Instant.now());
        staticActivityHistoryRepository.save(activityHistory);
    }
//
//    public static void logHistoryForCourseDeleted(Long userId, SubCourse subCourse) {
//        logHistoryForCourse(userId,EActivityAction.DELETED , subCourse,  "Đã xóa khóa học!");
//    }

    private static void logHistoryForCourse(Long userId, EActivityAction action ,Class clazz, String detail) {
        ActivityHistory activityHistory = new ActivityHistory();
        activityHistory.setType(EActivityType.ClASS);
        activityHistory.setAction(action);
        activityHistory.setActivityTime(LocalDateTime.now());

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



}