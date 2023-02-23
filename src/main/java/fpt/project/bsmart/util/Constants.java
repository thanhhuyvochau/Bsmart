package fpt.project.bsmart.util;

public class Constants {

    public static final String ANONYMOUS_USER = "anonymousUser";

    public static final String OK = "OK";
    public static final String FAILED = "FAILED";
    public static final String DELETED = "DELETED";

    public static class ErrorMessage {
        public static final String CLASS_NOT_FOUND_BY_ID = "CLASS_NOT_FOUND_BY_ID";
        public static final String CODE_ALREADY_EXISTED = "CODE_ALREADY_EXISTED";

        public static final String CLASS_NOT_ALLOW_UPDATE = "CLASS_NOT_ALLOW_UPDATE";
        public static final String TOPIC_NOT_FOUND_BY_ID = "TOPIC_NOT_FOUND_BY_ID";

    }

    public static class MailMessage {

        public static final String SUBJECT_MAIL_EDIT_REQUEST_PROFILE = "SUBJECT_MAIL_EDIT_REQUEST_PROFILE";

        public static final String FOOTER_MAIL_EDIT_REQUEST_PROFILE = "FOOTER_MAIL_EDIT_REQUEST_PROFILE";

    }


    public static class DefaultData {
        public static final String STUDENT_ROLE_CODE = "STUDENT";
        public static final String MANAGER_ROLE_MANAGER = "MANAGER";
        public static final String TEACHER_ROLE_CODE = "TEACHER";
    }

    public static class Notification {
        public static final String TRANSACTION_PAY_SUCCESS = "transaction_pay_success";
    }

}
