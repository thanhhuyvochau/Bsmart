package fpt.project.bsmart.director;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.constant.ENotificationEntity;
import fpt.project.bsmart.entity.constant.ENotificationType;
import fpt.project.bsmart.util.MessageUtil;
import fpt.project.bsmart.util.TextUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationDirector {
    public static MessageUtil staticMessageUtil;

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
        return builder
                .viTitle(title)
                .viContent(content)
                .notifiers(order.getUser())
                .type(ENotificationType.PERSONAL)
                .entity(ENotificationEntity.TRANSACTION)
                .entityId(transaction.getId())
                .build();
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

    public static Notification buildApprovalClass(Class clazz, ECourseStatus status) {
        String title = staticMessageUtil.getLocalMessage(NotificationConstant.CLASS_APPROVAL_TITLE);
        String content = staticMessageUtil.getLocalMessage(NotificationConstant.CLASS_APPROVAL_CONTENT);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("className", clazz.getCode());
        parameters.put("status", status.name());
        content = TextUtil.format(content, parameters);
        Notification.NotificationBuilder builder = Notification.getBuilder();
        return builder
                .viTitle(title)
                .viContent(content)
                .notifiers(clazz.getMentor())
                .type(ENotificationType.PERSONAL)
                .entity(ENotificationEntity.CLASS)
                .entityId(clazz.getId())
                .build();
    }

    public static Notification buildRegisterSuccessAccount(User user) {
        String title = staticMessageUtil.getLocalMessage(NotificationConstant.ACCOUNT_REGISTER_SUCCESSFUL_TITLE);
        String content = staticMessageUtil.getLocalMessage(NotificationConstant.ACCOUNT_REGISTER_SUCCESSFUL_CONTENT);
        Map<String, String> parameters = new HashMap<>();
        content = TextUtil.format(content, parameters);
        Notification.NotificationBuilder builder = Notification.getBuilder();
        return builder
                .viTitle(title)
                .viContent(content)
                .notifiers(user)
                .type(ENotificationType.PERSONAL)
                .entity(ENotificationEntity.ACCOUNT)
                .entityId(user.getId())
                .build();
    }

    public static Notification buildCourseTransferMoneyToMentor(Class clazz, BigDecimal amount){
        String title = staticMessageUtil.getLocalMessage(NotificationConstant.COURSE_TRANSFER_MONEY_TITLE);
        String content = staticMessageUtil.getLocalMessage(NotificationConstant.COURSE_TRANSFER_MONEY_CONTENT);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("classCode", clazz.getCode());
        parameters.put("amount", amount.toPlainString());
        content = TextUtil.format(content, parameters);
        Notification.NotificationBuilder builder = Notification.getBuilder();
        return builder
                .viTitle(title)
                .viContent(content)
                .notifiers(clazz.getMentor())
                .type(ENotificationType.PERSONAL)
                .entity(ENotificationEntity.ACCOUNT)
                .entityId(clazz.getMentor().getId())
                .build();
    }
}
