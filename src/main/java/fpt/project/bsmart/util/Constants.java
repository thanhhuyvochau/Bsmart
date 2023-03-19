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
        public static final String SKILL_NOT_FOUND_BY_ID = "SKILL_NOT_FOUND_BY_ID";
        public static final String MENTOR_PROFILE_NOT_FOUND_BY_USER = "MENTOR_PROFILE_NOT_FOUND_BY_USER";

        public static class Invalid{

            public static final String USERNAME_PASSWORD_INCORRECT = "USERNAME_PASSWORD_INCORRECT";
            public static final String INVALID_FACEBOOK_LINK = "INVALID_FACEBOOK_LINK";
            public static final String INVALID_INSTAGRAM_LINK = "INVALID_INSTAGRAM_LINK";
            public static final String INVALID_TWITTER_LINK = "INVALID_TWITTER_LINK";
            public static final String INVALID_BIRTHDAY = "INVALID_BIRTHDAY";
            public static final String INVALID_PHONE_NUMBER = "INVALID_PHONE_NUMBER";
            public static final String INVALID_PASSWORD = "INVALID_PASSWORD";
            public static final String INVALID_SOCIAL_LINK = "INVALID_SOCIAL_LINK";
        }
        public static class Empty{
            public static final String EMPTY_FULL_NAME = "EMPTY_FULL_NAME";
            public static final String EMPTY_ADDRESS = "EMPTY_ADDRESS";
            public static final String EMPTY_PASSWORD = "EMPTY_PASSWORD";
            public static final String EMPTY_INTRODUCE = "EMPTY_INTRODUCE";
            public static final String EMPTY_YEAR_OF_EXPERIENCE = "EMPTY_YEAR_OF_EXPERIENCE";
            public static final String EMPTY_SKILL = "EMPTY_SKILL";
            public static final String EMPTY_BIRTHDAY = "EMPTY_BIRTHDAY";
        }
        public static String OLD_PASSWORD_MISMATCH = "OLD_PASSWORD_MISMATCH";
        public static String NEW_PASSWORD_DUPLICATE = "NEW_PASSWORD_DUPLICATE";
        public static String FORBIDDEN = "FORBIDDEN";
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