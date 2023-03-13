package fpt.project.bsmart.util;


public class Constants {

    public static final String ANONYMOUS_USER = "anonymousUser";

    public static final String OK = "OK";
    public static final String FAILED = "FAILED";
    public static final String DELETED = "DELETED";

    public static class ErrorMessage {
        public static final String SUBJECT_NOT_FOUND_BY_ID = "SUBJECT_NOT_FOUND_BY_ID";

        public static final String COURSE_NOT_FOUND_BY_ID = "COURSE_NOT_FOUND_BY_ID";
        public static final String CATEGORY_NOT_FOUND_BY_ID = "CATEGORY_NOT_FOUND_BY_ID";
        public static final String ROLE_NOT_FOUND_BY_ID = "ROLE_NOT_FOUND_BY_ID";
        public static final String USER_NOT_FOUND_BY_ID = "USER_NOT_FOUND_BY_ID";

        public static final String IMAGE_NOT_FOUND_BY_ID = "IMAGE_NOT_FOUND_BY_ID";
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