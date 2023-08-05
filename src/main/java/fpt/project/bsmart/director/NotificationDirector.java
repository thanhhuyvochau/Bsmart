package fpt.project.bsmart.director;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.constant.ENotificationEntity;
import fpt.project.bsmart.entity.constant.ENotificationType;
import fpt.project.bsmart.util.MessageUtil;
import fpt.project.bsmart.util.TextUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;


public class NotificationDirector {
    public static MessageUtil staticMessageUtil;

    @Autowired
    public NotificationDirector(MessageUtil messageUtil) {
        staticMessageUtil = messageUtil;
    }

    public static Notification buildPaymentNotification(Order order, Transaction transaction) {
        String title = staticMessageUtil.getLocalMessage(NotificationConstant.TRANSACTION_PAYMENT_TITLE);
        String content = staticMessageUtil.getLocalMessage(NotificationConstant.TRANSACTION_PAYMENT_CONTENT);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("totalPrice", transaction.getAmount().toString());
        content = TextUtil.format(content, parameters);
        Notification.NotificationBuilder builder = Notification.getBuilder();
        Notification notification = builder
                .viTitle(title)
                .viContent(content)
                .notifiers(order.getUser())
                .type(ENotificationType.PERSONAL)
                .entity(ENotificationEntity.TRANSACTION)
                .entityId(transaction.getId())
                .build();
        return notification;
    }

    public static Notification buildApprovalCourse(Course course, ECourseStatus status) {
        String title = staticMessageUtil.getLocalMessage(NotificationConstant.COURSE_APPROVAL_TITLE);
        String content = staticMessageUtil.getLocalMessage(NotificationConstant.COURSE_APPROVAL_CONTENT);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("courseName", course.getName());
        parameters.put("status", status.name());
        content = TextUtil.format(content, parameters);
        Notification.NotificationBuilder builder = Notification.getBuilder();
        return builder
                .viTitle(title)
                .viContent(content)
                .notifiers(course.getCreator())
                .type(ENotificationType.PERSONAL)
                .entity(ENotificationEntity.COURSE)
                .entityId(course.getId())
                .build();
    }

    public static Notification buildApprovalMentorProfile(MentorProfile mentorProfile) {
        String title = staticMessageUtil.getLocalMessage(NotificationConstant.COURSE_APPROVAL_PROFILE_TITLE);
        String content = staticMessageUtil.getLocalMessage(NotificationConstant.COURSE_APPROVAL_PROFILE_CONTENT);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("status", mentorProfile.getStatus().name());
        content = TextUtil.format(content, parameters);
        Notification.NotificationBuilder builder = Notification.getBuilder();
        return builder
                .viTitle(title)
                .viContent(content)
                .notifiers(mentorProfile.getUser())
                .type(ENotificationType.PERSONAL)
                .entity(ENotificationEntity.MENTOR_PROFILE)
                .entityId(mentorProfile.getId())
                .build();
    }

    public static Notification buildEnrollClass(Class clazz, User user) {
        String title = staticMessageUtil.getLocalMessage(NotificationConstant.CLASS_ENROLL_SUCCESS_TITLE);
        String content = staticMessageUtil.getLocalMessage(NotificationConstant.CLASS_ENROLL_SUCCESS_CONTENT);
        Map<String, String> parameters = new HashMap<>();
        Course course = clazz.getCourse();
        parameters.put("classTitle", clazz.getCode());
        parameters.put("mentorName", course.getCreator().getFullName());
        parameters.put("startDate", clazz.getStartDate().toString());
        parameters.put("courseName", course.getName());
        content = TextUtil.format(content, parameters);
        Notification.NotificationBuilder builder = Notification.getBuilder();
        return builder
                .viTitle(title)
                .viContent(content)
                .notifiers(user)
                .type(ENotificationType.PERSONAL)
                .entity(ENotificationEntity.CLASS)
                .entityId(clazz.getId())
                .build();
    }
}
