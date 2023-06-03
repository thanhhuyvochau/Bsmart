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
        public static final String SUB_COURSE_STATUS_NOT_ALLOW = "SUB_COURSE_STATUS_NOT_ALLOW";
        public static final String SECTION_NOT_FOUND_BY_ID = "SECTION_NOT_FOUND_BY_ID";
        public static final String ACTIVITY_TYPE_NOT_FOUND_BY_ID = "ACTIVITY_TYPE_NOT_FOUND_BY_ID";
        public static final String ACTIVITY_NOT_FOUND_BY_ID = "ACTIVITY_NOT_FOUND_BY_ID";
        public static final String SUB_COURSE_NOT_FOUND_BY_ID = "SUB_COURSE_NOT_FOUND_BY_ID";
        public static final String CART_ITEM_NOT_FOUND_BY_ID = "CART_ITEM_NOT_FOUND_BY_ID";
        public static final String FEEDBACK_QUESTION_NOT_FOUND_BY_ID = "FEEDBACK_QUESTION_NOT_FOUND_BY_ID";
        public static final String FEEDBACK_TEMPLATE_NOT_FOUND_BY_ID = "FEEDBACK_TEMPLATE_NOT_FOUND_BY_ID";
        public static final String CLASS_NOT_FOUND_BY_ID = "CLASS_NOT_FOUND_BY_ID";

        public static final String ACCOUNT_IS_NOT_MENTOR = "ACCOUNT_IS_NOT_MENTOR";


        public static final String  COURSE_CODE_ALREADY_EXISTS= "COURSE_CODE_ALREADY_EXISTS";

        public static final String  COURSE_STATUS_NOT_ALLOW= "COURSE_STATUS_NOT_ALLOW";

        public static final String  COURSE_DOES_NOT_BELONG_TO_THE_TEACHER= "COURSE_DOES_NOT_BELONG_TO_THE_TEACHER";

        public static final String  SCHEDULE_AND_SLOT_HAVE_BEEN_OVERLAPPED= "SCHEDULE_AND_SLOT_HAVE_BEEN_OVERLAPPED";

        public static final String  DAY_OF_WEEK_COULD_NOT_BE_FOUND= "DAY_OF_WEEK_COULD_NOT_BE_FOUND";
        public static final String  SLOT_COULD_NOT_BE_FOUND= "SLOT_COULD_NOT_BE_FOUND";
        public static final String  PLEASE_SELECT_THE_CATEGORY_FOR_THE_COURSE= "PLEASE_SELECT_THE_CATEGORY_FOR_THE_COURSE";
        public static final String  PLEASE_SELECT_THE_SUBJECT_FOR_THE_COURSE= "PLEASE_SELECT_THE_SUBJECT_FOR_THE_COURSE";
        public static final String  PLEASE_ENTER_THE_PRICE_FOR_THE_COURSE= "PLEASE_ENTER_THE_PRICE_FOR_THE_COURSE";

        public static final String USER_NOT_HAVE_PERMISSION_TO_VIEW_THIS_COURSE = "USER_NOT_HAVE_PERMISSION_TO_VIEW_THIS_COURSE";

        public static final String  INVALID_COURSE_APPROVAL_STATUS= "INVALID_COURSE_APPROVAL_STATUS";

        public static final String  ACCOUNT_STATUS_NOT_ALLOW= "ACCOUNT_STATUS_NOT_ALLOW";
        public static class Invalid{

            public static final String USERNAME_PASSWORD_INCORRECT = "USERNAME_PASSWORD_INCORRECT";
            public static final String INVALID_FACEBOOK_LINK = "INVALID_FACEBOOK_LINK";
            public static final String INVALID_INSTAGRAM_LINK = "INVALID_INSTAGRAM_LINK";
            public static final String INVALID_TWITTER_LINK = "INVALID_TWITTER_LINK";
            public static final String INVALID_DAY = "INVALID_DAY";
            public static final String INVALID_PHONE_NUMBER = "INVALID_PHONE_NUMBER";
            public static final String INVALID_PASSWORD = "INVALID_PASSWORD";
            public static final String INVALID_SOCIAL_LINK = "INVALID_SOCIAL_LINK";
            public static final String NEGATIVE_YEAR_OF_EXPERIENCES = "NEGATIVE_YEAR_OF_EXPERIENCES";
            public static final String INVALID_YEAR_OF_EXPERIENCES = "INVALID_YEAR_OF_EXPERIENCES";
            public static final String INVALID_ACTIVITY_TYPE = "INVALID_ACTIVITY_TYPE";
            public static final String INVALID_NUMBER_OF_ANSWER_IN_FEEDBACK_QUESTION = "INVALID_NUMBER_OF_ANSWER_IN_FEEDBACK_QUESTION";
            public static final String INVALID_SCORE_IN_FEEDBACK_QUESTION = "INVALID_SCORE_IN_FEEDBACK_QUESTION";
            public static final String INVALID_QUESTION_LIST_SIZE = "INVALID_QUESTION_LIST_SIZE";
            public static final String INVALID_FEEDBACK_TIME = "INVALID_FEEDBACK_TIME";
            public static final String INVALID_FEEDBACK_TYPE = "INVALID_FEEDBACK_TYPE";
        }
        public static class Empty{
            public static final String EMPTY_FULL_NAME = "EMPTY_FULL_NAME";
            public static final String EMPTY_ADDRESS = "EMPTY_ADDRESS";
            public static final String EMPTY_PASSWORD = "EMPTY_PASSWORD";
            public static final String EMPTY_INTRODUCE = "EMPTY_INTRODUCE";
            public static final String EMPTY_SKILL = "EMPTY_SKILL";
            public static final String EMPTY_YEAR_OF_EXPERIENCES = "EMPTY_YEAR_OF_EXPERIENCES";
            public static final String EMPTY_BIRTHDAY = "EMPTY_BIRTHDAY";
            public static final String EMPTY_FEEDBACK_QUESTION = "EMPTY_FEEDBACK_QUESTION";
            public static final String EMPTY_FEEDBACK_TEMPLATE_NAME = "EMPTY_FEEDBACK_TEMPLATE_NAME";
            public static final String EMPTY_QUESTION_LIST = "EMPTY_QUESTION_LIST";
            public static final String EMPTY_FEEDBACK_TEMPLATE_ID = "EMPTY_FEEDBACK_TEMPLATE_ID";
            public static final String EMPTY_FEEDBACK_ANSWER = "EMPTY_FEEDBACK_ANSWER";
        }
        public static final String MISSING_MULTI_CHOICE_FEEDBACK_QUESTION = "MISSING_MULTI_CHOICE_FEEDBACK_QUESTION";
        public static final String MISSING_MAX_SCORE = "MISSING_MAX_SCORE";
        public static final String DUPLICATE_SCORE_IN_FEEDBACK_QUESTION = "DUPLICATE_SCORE_IN_FEEDBACK_QUESTION";
        public static final String DUPLICATE_SUB_COURSE_IN_CART_ITEM = "DUPLICATE_SUB_COURSE_IN_CART_ITEM";
        public static final String OLD_PASSWORD_MISMATCH = "OLD_PASSWORD_MISMATCH";
        public static final String NEW_PASSWORD_DUPLICATE = "NEW_PASSWORD_DUPLICATE";
        public static final String SUBJECT_ID_DUPLICATE = "SUBJECT_ID_DUPLICATE";
        public static final String FORBIDDEN = "FORBIDDEN";
        public static final String ANSWER_FORMAT_EXCEPTION = "ANSWER_FORMAT_EXCEPTION";
        public static final String CAN_NOT_UPLOAD_ASSIGNMENT = "CAN_NOT_UPLOAD_ASSIGNMENT";
        public static final String STUDENT_NOT_BELONG_TO_CLASS = "STUDENT_NOT_BELONG_TO_CLASS";
        public static final String STUDENT_ALREADY_FEEDBACK = "STUDENT_ALREADY_FEEDBACK";
        public static final String FEEDBACK_TEMPLATE_ATTACHED_TO_SUB_COURSE = "FEEDBACK_TEMPLATE_ATTACHED_TO_SUB_COURSE";
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