package com.project.Bsmart.util.common;

public class Constants {

    public static final String ANONYMOUS_USER = "anonymousUser";
    public static final String OK = "OK";
    public static final String Y = "Y";
    public static final String N = "N";
    public static final String P = "P";
    public static final String S = "S";
    public static final String FAILED = "FAILED";

    public static final String DELETED = "DELETED";
    public static final String VND = "VND";
    public static final String S1_DATE_FORMAT = "yyyy-MM-dd";

    public static class Regex {
        public static final String USERNAME_REGEX = "^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$";
        public static final String CODE_REGEX = "[a-zA-Z0-9_]+";
    }

    public static class ErrorMessage {
        public static final String CUSTOMER_NOT_FOUND_BY_ID = "CUSTOMER_NOT_FOUND_BY_ID";
        public static final String FEATURE_NOT_FOUND_BY_ID = "FEATURE_NOT_FOUND_BY_ID";
        public static final String FUNCTION_NOT_FOUND_BY_ID = "FUNCTION_NOT_FOUND_BY_ID";
        public static final String DEPARTMENT_NOT_FOUND_BY_ID = "DEPARTMENT_NOT_FOUND_BY_ID";
        public static final String STORE_NOT_FOUND_BY_ID = "STORE_NOT_FOUND_BY_ID";
        public static final String ACCOUNT_NOT_FOUND_BY_ID = "ACCOUNT_NOT_FOUND_BY_ID";
        public static final String ACCOUNT_NOT_FOUND_BY_USERNAME = "ACCOUNT_NOT_FOUND_BY_USERNAME";
        public static final String ACTION_NOT_FOUND_BY_ID = "ACTION_NOT_FOUND_BY_ID";
        public static final String PERMISSION_NOT_FOUND_BY_ID = "PERMISSION_NOT_FOUND_BY_ID";
        public static final String STAFF_NOT_FOUND_BY_ID = "STAFF_NOT_FOUND_BY_ID";
        public static final String STAFF_HAS_NO_ACCOUNT = "STAFF_HAS_NO_ACCOUNT";
        public static final String ROLE_NOT_FOUND_BY_ID = "ROLE_NOT_FOUND_BY_ID";
        public static final String USER_DISABLED = "USER_DISABLED";
        public static final String USERNAME_OR_PASSWORD_INCORRECT = "USERNAME_OR_PASSWORD_INCORRECT";
        public static final String USER_NOT_FOUND_BY_USERNAME = "USER_NOT_FOUND_BY_USERNAME";
        public static final String CODE_ALREADY_EXISTED = "CODE_ALREADY_EXISTED";
        public static final String USERNAME_ALREADY_EXISTED = "USERNAME_ALREADY_EXISTED";
        public static final String FUNCTION_AND_ACTION_ALREADY_EXISTED = "FUNCTION_AND_ACTION_ALREADY_EXISTED";
        public static final String PASSWORD_INCORRECT = "PASSWORD_INCORRECT";
        public static final String ORDER_NOT_FOUND_BY_ID = "ORDER_NOT_FOUND_BY_ID";
        public static final String DOCUMENT_NOT_FOUND_BY_ID = "DOCUMENT_NOT_FOUND_BY_ID";
        public static final String POST_NOT_FOUND = "POST_NOT_FOUND";
        public static final String PROMOTION_TYPE_NOT_FOUND_BY_ID = "PROMOTION_TYPE_NOT_FOUND_BY_ID";
        public static final String PROMOTION_CAMPAIGN_NOT_FOUND_BY_ID = "PROMOTION_CAMPAIGN_NOT_FOUND_BY_ID";
        public static final String PROMOTION_NOT_FOUND_BY_ID = "PROMOTION_NOT_FOUND_BY_ID";

        public static final String UNIT_NOT_FOUND_BY_ID = "UNIT_NOT_FOUND_BY_ID";
        public static final String OPERATION_TYPE_NOT_FOUND_BY_ID = "OPERATION_TYPE_NOT_FOUND_BY_ID";
        public static final String ROUNDING_TYPE_NOT_FOUND_BY_ID = "ROUNDING_TYPE_NOT_FOUND_BY_ID";

        public static final String ORDER_POST_NOT_FOUND_BY_ID = "ORDER_POST_NOT_FOUND_BY_ID";
        public static final String ORDER_ITEM_NOT_FOUND = "ORDER_ITEM_NOT_FOUND";
        public static final String BRANCH_NOT_FOUND_BY_ID = "BRANCH_NOT_FOUND_BY_ID";
        public static final String WAREHOUSE_NOT_FOUND_BY_ID = "WAREHOUSE_NOT_FOUND_BY_ID";
        public static final String PRODUCT_NOT_FOUND_BY_ID = "PRODUCT_NOT_FOUND_BY_ID";
        public static final String VOUCHER_TYPE_NOT_FOUND_BY_ID = "VOUCHER_TYPE_NOT_FOUND_BY_ID";
        public static final String PAYMENT_METHOD_NOT_FOUND_BY_ID = "PAYMENT_METHOD_NOT_FOUND_BY_ID";
        public static final String PAYMENT_UNIT_NOT_FOUND_BY_ID = "PAYMENT_UNIT_NOT_FOUND_BY_ID";
        public static final String EXCHANGE_RATE_NOT_FOUND_BY_ID = "EXCHANGE_RATE_NOT_FOUND_BY_ID";
        public static final String INPUT_METHOD_NOT_FOUND_BY_ID = "INPUT_METHOD_NOT_FOUND_BY_ID";
        public static final String ORDER_NOT_ALLOW_UPDATE_STATUS = "ORDER_NOT_ALLOW_UPDATE_STATUS";

        public static final String ORDER_NOT_ALLOW_UPDATE_DISCOUNT = "ORDER_NOT_ALLOW_UPDATE_DISCOUNT";

        public static final String ORDER_NOT_ALLOW_PRICE_QUOTE = "ORDER_NOT_ALLOW_PRICE_QUOTE";

        public static final String ORDER_NOT_ALLOW_CANCEL_ALLOCATION = "ORDER_NOT_ALLOW_CANCEL_ALLOCATION";
        public static final String ORDER_NOT_ALLOW_CANCEL_COMPLETE = "ORDER_NOT_ALLOW_CANCEL_COMPLETE";
        public static final String CART_NOT_FOUND_BY_ID = "CART_NOT_FOUND_BY_ID";
        public static final String POST_NOT_EXIST_IN_CART = "POST_NOT_EXIST_IN_CART";
        public static final String NOT_ALLOWED_EDIT_ORDER_BY_STATUS = "NOT_ALLOWED_EDIT_ORDER_BY_STATUS";
        public static final String NOT_ALLOWED_EDIT_CART_ITEM_STATUS = "NOT_ALLOWED_EDIT_CART_ITEM_STATUS";
        public static final String NOT_ALLOWED_DELETE_CART_ITEM_STATUS = "NOT_ALLOWED_DELETE_CART_ITEM_STATUS";
        public static final String NOT_ALLOWED_PROMOTION_FOR_CART_ITEM = "NOT_ALLOWED_PROMOTION_FOR_CART_ITEM";

        public static final String NOT_ALLOWED_POST_QUANTITY = "NOT_ALLOWED_POST_QUANTITY";
        public static final String CATEGORY_NOT_FOUND_BY_ID = "CATEGORY_NOT_FOUND_BY_ID:";
        public static final String STAFF_NOT_FOUND_BY_S1_ID = "STAFF_NOT_FOUND_BY_S1_ID";

        public static final String IMAGE_NOT_FOUND_BY_ID = "IMAGE_NOT_FOUND_BY_ID";
        public static final String DISPLAY_GROUP_NOT_FOUND_BY_ID = "DISPLAY_GROUP_NOT_FOUND_BY_ID";
        public static final String CUSTOMER_ALREADY_HAD_ACCOUNT = "CUSTOMER_ALREADY_HAD_ACCOUNT";

        public static final String POST_NOT_FOUND_BY_ID = "POST_NOT_FOUND_BY_ID";

        public static final String CART_NAME_MUST_NOT_EMPTY = "CART_NAME_MUST_NOT_EMPTY";
        public static final String CART_ITEM_NOT_FOUND_BY_ID = "CART_ITEM_NOT_FOUND_BY_ID";

        public static final String PRODUCT_TYPE_NOT_FOUND = "PRODUCT_TYPE_NOT_FOUND";
        public static final String POST_TYPE_NOT_FOUND_BY_ID = "POST_TYPE_NOT_FOUND_BY_ID";

        public static final String AVATAR_IMAGE_TYPE_NOT_FOUND = "AVATAR_IMAGE_TYPE_NOT_FOUND";
        public static final String IMAGE_TYPE_NOT_FOUND = "IMAGE_TYPE_NOT_FOUND";

        public static final String SUB_OPTION_TYPE_NOT_FOUND = "SUB_OPTION_TYPE_NOT_FOUND";

        public static final String NOT_FOUND_CUSTOMER_BY_ACCOUNT = "NOT_FOUND_CUSTOMER_BY_ACCOUNT";
        public static final String POST_TYPE_NOT_FOUND_BY_CODE = "POST_TYPE_NOT_FOUND_BY_CODE";

        public static final String DATA_ROLE_NOT_FOUND_BY_ID = "DATA_ROLE_NOT_FOUND_BY_ID";

        public static final String INVALID_CART_ITEM = "INVALID_CART_ITEM";

        public static final String INVALID_TO_CREATE_ORDER = "INVALID_TO_CREATE_ORDER";
        public static final String BAD_TOKEN = "BAD_TOKEN";
        public static final String POST_DISPLAY_GROUP_NOT_FOUND = "POST_DISPLAY_GROUP_NOT_FOUND";
        public static final String POST_DISPLAY_GROUP_EXISTED = "POST_DISPLAY_GROUP_EXISTED";

        public static final String NOT_ALLOWED_POST_DISPLAY_GROUP = "NOT_ALLOWED_POST_DISPLAY_GROUP";
        public static final String CART_ITEM_NOT_ALLOW_MODIFY_QUANTITY = "CART_ITEM_NOT_ALLOW_MODIFY_QUANTITY";
        public static final String CONTRACT_CART_ITEM_CONSTRAINT = "CONTRACT_CART_ITEM_CONSTRAINT";
        public static final String CONTRACT_PRODUCT_NOT_FOUND_BY_ID = "CONTRACT_PRODUCT_NOT_FOUND_BY_ID";
        public static final String CONTRACT_PRODUCT_IN_CART_ITEM_NOT_FOUND = "CONTRACT_PRODUCT_IN_CART_ITEM_NOT_FOUND";
        public static final String CONTRACT_PRODUCT_REMAIN_NOT_ENOUGH = "CONTRACT_PRODUCT_REMAIN_NOT_ENOUGH";
        public static final String NOT_HAVE_POST_SUB_POST = "NOT_HAVE_POST_SUB_POST";
        public static final String INVALID_QUANTITY = "INVALID_QUANTITY";
        public static final String ONLY_SINGLE_POST_ALLOWED = "ONLY_SINGLE_POST_ALLOWED";
        public static final String NOT_ALLOWED_ONLY_BUY_SUB_OPTION = "NOT_ALLOWED_ONLY_BUY_SUB_OPTION";
        public static final String DATABASE_ERROR = "DATABASE_ERROR";
        public static final String NOT_RIGHT_SUB_POST = "NOT_RIGHT_SUB_POST";
        public static final String NOT_ENOUGH_ITEM_IN_COMBO = "NOT_ENOUGH_ITEM_IN_COMBO";
        public static final String PRODUCT_NOT_FOUND_WHEN_SYNCHRONIZE = "PRODUCT_NOT_FOUND_WHEN_SYNCHRONIZE";
        public static final String POST_NOT_HAVE_PROMOTION = "POST_NOT_HAVE_PROMOTION";
        public static final String POST_QUANTITY_NOT_ENOUGH_TO_APPLY_PROMOTION = "POST_QUANTITY_NOT_ENOUGH_TO_APPLY_PROMOTION";
        public static final String CONTRACT_NOT_FOUND_BY_ABSID = "CONTRACT_NOT_FOUND_BY_ABSID";
        public static final String PROMOTION_GIFT_EMPTY = "PROMOTION_GIFT_EMPTY";
        public static final String PROMOTION_GIFT_NOT_FOUND = "PROMOTION_GIFT_NOT_FOUND";
        public static final String INVALID_QUANTITY_GIFT = "INVALID_QUANTITY_GIFT";
        public static final String PROMOTION_GIFT_DUPLICATE = "PROMOTION_GIFT_DUPLICATE";
        public static final String EXCEED_QUANTITY_GIFT = "EXCEED_QUANTITY_GIFT";
        public static final String PROMOTION_GIFT_CHANGED = "PROMOTION_GIFT_CHANGED";
        public static final String PROMOTION_NOT_SATISFIED = "PROMOTION_NOT_SATISFIED";
        public static final String SUB_OPTION_DUPLICATE = "SUB_OPTION_DUPLICATE";
        public static final String EXCEED_MAX_QUANTITY = "EXCEED_MAX_QUANTITY";
        public static final String SUB_OPTION_RADIO_TYPE_INVALID = "SUB_OPTION_RADIO_TYPE_INVALID";
        public static final String SUB_OPTION_OBLIGATORY_CONSTRAINT = "SUB_OPTION_OBLIGATORY_CONSTRAINT";
        public static final String SUB_OPTION_COMBO_TYPE_INVALID = "SUB_OPTION_COMBO_TYPE_INVALID";
        public static final String CONTRACT_NOT_FOUND_BY_ID = "CONTRACT_NOT_FOUND_BY_ID";
        public static final String PROMOTION_APPLIED = "PROMOTION_APPLIED";
        public static final String CUSTOMER_ID_NULL = "CUSTOMER_ID_NULL";
        public static final String DISPLAY_GROUP_NOT_ALLOWED_DELETED = "DISPLAY_GROUP_NOT_ALLOWED_DELETED";
        public static final String CUSTOMER_NOT_FOUND_BY_BPCODE = "CUSTOMER_NOT_FOUND_BY_BPCODE";
        public static final String CART_ITEM_NOT_ALLOWED_NULL = "CART_ITEM_NOT_ALLOWED_NULL";
        public static final String CATEGORY_ORDER_CONSTRAINT = "CATEGORY_ORDER_CONSTRAINT";
        public static final String CART_ITEM_ORDER_IS_NOT_ROOT = "CART_ITEM_ORDER_IS_NOT_ROOT";
        public static final String CART_ITEM_ORDER_DUPLICATE = "CART_ITEM_ORDER_DUPLICATE";
        public static final String CART_ITEM_GROUP_POST_TYPE_EMPTY = "CART_ITEM_GROUP_POST_TYPE_EMPTY";
        public static final String DOCUMENT_TYPE_NOT_FOUND = "DOCUMENT_TYPE_NOT_FOUND";
        public static final String CART_ITEM_INVALID = "CART_ITEM_INVALID";
        public static final String CONTRACT_NOT_CONSISTENT = "CONTRACT_NOT_CONSISTENT";
        public static final String PROMOTION_POST_NOT_FOUND = "PROMOTION_POST_NOT_FOUND";
        public static final String POST_COMBO_AND_GROUP_COMBO_MUST_HAVE_POST_SUB_POST = "POST_COMBO_AND_GROUP_COMBO_MUST_HAVE_POST_SUB_POST";
        public static final String PAGE_CONTENT_NOT_FOUND_BY_ID = "PAGE_CONTENT_NOT_FOUND_BY_ID";
        public static final String HAS_UNIT_PRICE_PRODUCT_TYPE_NOT_FOUND = "HAS_UNIT_PRICE_PRODUCT_TYPE_NOT_FOUND";
        public static final String NO_UNIT_PRICE_PRODUCT_TYPE_NOT_FOUND = "NO_UNIT_PRICE_PRODUCT_TYPE_NOT_FOUND";
        public static final String POST_EXISTED_IN_DISPLAY_GROUP = "POST_EXISTED_IN_DISPLAY_GROUP";
        public static final String ID_NULL_NOT_ALLOWED = "ID_NULL_NOT_ALLOWED";
        public static final String OLD_DISPLAY_GROUP_NOT_EXISTED = "OLD_DISPLAY_GROUP_NOT_EXISTED";
        public static final String COMBO_SUB_OPTION_ITEM_DUPLICATE = "COMBO_SUB_OPTION_ITEM_DUPLICATE";
        public static final String ACCOUNT_IS_INACTIVE = "ACCOUNT_IS_INACTIVE";
        public static final String PROMOTION_APPLYING_CONSTRAINT = "PROMOTION_APPLYING_CONSTRAINT";
        public static final String USERNAME_FORMAT_WRONG = "USERNAME_FORMAT_WRONG";
        public static final String WRONG_CODE_FORMAT = "WRONG_CODE_FORMAT";
        public static final String CODE_NULL = "CODE_NULL";
        public static final String SUB_OPTION_EMPTY = "SUB_OPTION_EMPTY";
        public static final String CHILD_MUS_NOT_EMPTY = "CHILD_MUS_NOT_EMPTY";
        public static final String PROMOTION_CAMPAIGN_PANO_TYPE_NOT_FOUND = "PROMOTION_CAMPAIGN_PANO_TYPE_NOT_FOUND";
        public static final String TOTAL_DISCOUNT_PRICE_NOT_SUPPORT = "TOTAL_DISCOUNT_PRICE_NOT_SUPPORT";
        public static final String POST_QUANTITY_MUST_A_NUMBER = "POST_QUANTITY_MUST_A_NUMBER";
        public static final String CUSTOMER_DO_NOT_HAS_SALE = "CUSTOMER_DO_NOT_HAS_SALE";
        public static final String DOCUMENT_NOT_FOUND_IN_ORDER = "DOCUMENT_NOT_FOUND_IN_ORDER";
        public static final String CUSTOMER_MUST_HAS_SALE = "CUSTOMER_MUST_HAS_SALE";
        public static final String DOCUMENT_ALREADY_IN_S1 = "DOCUMENT_ALREADY_IN_S1";
        public static final String PROMOTION_CAMPAIGN_PANO_NOT_FOUND = "PROMOTION_CAMPAIGN_PANO_NOT_FOUND";
        public static final String PRICE_MUST_IS_A_POSITIVE_NUMBER = "PRICE_MUST_IS_A_POSITIVE_NUMBER";
        public static final String CART_ITEM_NOT_FOUND_IN_CART = "CART_ITEM_NOT_FOUND_IN_CART";
        public static final String NEED_CHANGE_PASSWORD = "NEED_CHANGE_PASSWORD";
    }

    public static class S1Endpoint {
        public static final String LOGIN = "/login";
        public static final String LOGOUT = "/logout";
        public static final String MASTERDATA = "/masterdata";
    }

    public static class S1MasterDataType {

        public static final String CUSTOMER = "customer";
        public static final String BRANCH = "branch";
        public static final String PRODUCT = "item";
        public static final String VOUCHER_TYPE = "vouchertypeso";
        public static final String MANUFACTURER_TYPE = "manufacturer";
        public static final String USER = "user";

        public static final String STAFF = "saleman";
        public static final String USER_MENU = "usermenu";
        public static final String CATEGORY = "itemgroup";
        public static final String EXCHANGE_RATE = "exchangerate";
        public static final String PAYMENT = "paymentterm";
        public static final String INPUT_METHOD = "inputmethod";
        public static final String WAREHOUSE = "warehouse";
        public static final String ALTER_ITEM_DETAIL = "alteritemdetail";
        public static final String ALTER_ITEM = "alteritem";

        public static final String PRODUCT_PRICE = "itemprice";

        public static final String PROMOTION_CAMPAIGN = "list";
        public static final String PROMOTION = "detail";

        public static final String DEBT_CUSTOMER = "8. Biên bản đối chiếu công nợ";
    }

    public static class DefaultData {
        public static final String USER_ROLE_NAME = "S1_ROLE_USER_";
        public static final String PASSWORD = "123@";
        public static final String FEATURE = "Menu";
        public static final String DEFAULT_WAREHOUSE_CODE = "SGTONGST";

        public static final String DEFAULT_ROLE_CODE = "General";

        public static final String RETAIL_CODE_DOCUMENT_TYPE = "1006";
        public static final String CONTRACT_CODE_DOCUMENT_TYPE = "1003";

        public static final String UPTO = "Upto %s%%";

        public static final String DISCOUNT = "%s%% off";

        public static final String BUY_GIFT = "Mua %s tặng %s";
        public static final String FR_DATE = "1753-01-01";
        public static final String TO_DATE = "9999-12-31";
    }

    public static class EntityGraphName {

        public static final String WAREHOUSE_BRANCH_GRAPH = "WAREHOUSE_BRANCH_GRAPH";
        public static final String STAFF_DEPARTMENT_STORE_ACCOUNT_GRAPH = "STAFF_DEPARTMENT_STORE_ACCOUNT_GRAPH";

        public static final String PERMISSION_FUNCTION_ACTION_S1PERMISSION_GRAPH = "PERMISSION_FUNCTION_ACTION_S1PERMISSION_GRAPH";
        public static final String ORDER_CUSTOMER_STAFF_VOUCHER_PAYMENT_EXCHANGE_BRANCH_INPUT_WORK_WAREHOUSE_GRAPH = "ORDER_CUSTOMER_STAFF_VOUCHER_PAYMENT_EXCHANGE_BRANCH_INPUT_WORK_WAREHOUSE_GRAPH";
        public static final String ORDER_MESSAGE_ORDER_GRAPH = "ORDER_MESSAGE_ORDER_GRAPH";
        public static final String ORDER_ITEM_ORDER_PRODUCT_GRAPH = "ORDER_ITEM_ORDER_PRODUCT_GRAPH";
        public static final String NOTIFICATION_TOKEN_ACCOUNT = "NOTIFICATION_TOKEN_ACCOUNT";
        public static final String NOTIFICATION_ACCOUNT_GRAPH = "NOTIFICATION_ACCOUNT_GRAPH";
        public static final String MANUFACTURER_PRODUCT_GRAPH = "MANUFACTURER_PRODUCT_GRAPH";
        public static final String FUNCTION_FEATURE_GRAPH = "FUNCTION_FEATURE_GRAPH";
        public static final String FEATURE_FUNCTION_GRAPH = "FEATURE_FUNCTION_GRAPH";
        public static final String DISPLAY_GROUP_AVATAR_GRAPH = "DISPLAY_GROUP_AVATAR_PRODUCT_GRAPH";
        public static final String CUSTOMER_ACCOUNT_PAYMENT_TERRITORY_GRAPH = "CUSTOMER_ACCOUNT_PAYMENT_TERRITORY_BRANCH_GRAPH";
        public static final String CATEGORY_AVATAR_IMAGE_GRAPH = "CATEGORY_AVATAR_IMAGE_GRAPH";
        public static final String CART_CUSTOMER_GRAPH = "CART_CUSTOMER_GRAPH";
        public static final String ACCOUNT_CUSTOMER_STAFF_GRAPH = "ACCOUNT_CUSTOMER_STAFF_GRAPH";
        public static final String CART_ITEM_POST_CART_GRAPH = "CART_ITEM_POST_CART_GRAPH";
        public static final String PRODUCT_IMAGE_UNIT_CATEGORY_MANUFACTURER_GRAPH = "PRODUCT_IMAGE_UNIT_CATEGORY_MANUFACTURER_GRAPH";
        public static final String POST_POST_TYPE_CATEGORY_AVATAR_IMAGE_GRAPH = "POST_POST_TYPE_CATEGORY_AVATAR_IMAGE_GRAPH";
        public static final String CART_ITEM_PRODUCT_POST_CART_PRODUCT_GRAPH = "CART_ITEM_PRODUCT_POST_CART_PRODUCT_GRAPH";
        public static final String ORDER_ITEM_PRODUCT_ORDER_PRODUCT_POST_GRAPH = "ORDER_ITEM_PRODUCT_ORDER_PRODUCT_POST_GRAPH";
        public static final String POST_PRODUCT_PRODUCT_POST_GRAPH = "POST_PRODUCT_PRODUCT_POST_GRAPH";
        public static final String SUB_POST_PARENT_POST_SUB_POST = "SUB_POST_PARENT_POST_SUB_POST";
        public static final String PROMOTION_GRAPH = "PROMOTION_GRAPH";

        public static final String POST_DISPLAY_GROUP_GRAPH = "POST_DISPLAY_GROUP_GRAPH";
        public static final String PRICE_QUOTE_GRAPH = "PRICE_QUOTE_GRAPH";
        public static final String PRICE_QUOTE_LOG_GRAPH = "PRICE_QUOTE_LOG_GRAPH";
        public static final String SUB_OPTION_POST_GRAPH = "SUB_OPTION_POST_GRAPH";
        public static final String SUB_OPTION_POST_SUB_OPTION_POST_GRAPH = "SUB_OPTION_POST_SUB_OPTION_POST_GRAPH";
        public static final String POST_IMAGE_GRAPH = "POST_IMAGE_GRAPH";
        public static final String PANO_GRAPH = "PANO_GRAPH";
        public static final String CART_ITEM_PROMOTION_GRAPH = "CART_ITEM_PROMOTION_GRAPH";
        public static final String ORDER_ITEM_PROMOTION_GRAPH = "ORDER_ITEM_PROMOTION_GRAPH";

        public static final String ORDER_POST_PROMOTION_GRAPH = "ORDER_POST_PROMOTION_GRAPH";
        public static final String PROMOTION_POST_GRAPH = "PROMOTION_POST_GRAPH";
        public static final String PROMOTION_GIFT_GRAPH = "PROMOTION_GIFT_GRAPH";
        public static final String CART_ITEM_PROMOTION_GIFT_GRAPH = "CART_ITEM_PROMOTION_GIFT_GRAPH";
        public static final String CONTRACT_PRODUCT_GRAPH = "CONTRACT_PRODUCT_GRAPH";

        public static final String DOCUMENT_GRAPH = "DOCUMENT_GRAPH";
        public static final String CUSTOMER_CATEGORY_GRAPH = "CUSTOMER_CATEGORY_GRAPH";
        public static final String CART_ITEM_CONTRACT_GRAPH = "CART_ITEM_CONTRACT_GRAPH";
        public static final String ORDER_QUOTE_ITEM_CART_GRAPH = "ORDER_QUOTE_ITEM_CART_GRAPH";
        public static final String ORDER_QUOTE_ITEM_PROMOTION_GIFT_GRAPH = "ORDER_QUOTE_ITEM_PROMOTION_GIFT_GRAPH";
        public static final String PRICE_QUOTE_ORDER_GRAPH = "PRICE_QUOTE_ORDER_GRAPH";
    }

    public static class S1FieldData {
        public static final String HANG_TANG_HANG = "Hàng tặng hàng";
        public static final String GIAM_GIA_HANG_BAN = "Giảm giá hàng bán";

        public static final String GIA_TRI_TANG_HANG = "Giá trị tặng hàng";
    }

    public static class S1DefaultData {
        public static final String CONTRACT_TYPE_VALUE = "1250000025";
        public static final String BANHANG = "BANHANG";
        public static final String COGSACCT = "6321";
        public static final String ACCTCODE = "5111";

        public static final String NOTE_SALE = "Bán hàng %s";

        public static final String CREATED_BY_VIETDANG_APP = "CREATED_BY_VIETDANG_APP";

        public static final String DOCUMENT_STATUS = "2";

    }

    public static class S1PostTypeCode {
        public static final String BT = "BT";
        public static final String BK = "BK";
        public static final String KM = "KM";
    }

    public static class S1DocumentAction {
        public static final String A = "A";
        public static final String L = "L";
    }

    public static class UserNotification {
        // has new order
        public static final String TITLE_HAS_NEW_ORDER = "TITLE_HAS_NEW_ORDER";
        public static final String CONTENT_HAS_NEW_ORDER = "CONTENT_HAS_NEW_ORDER";

        // has new quote order
        public static final String TITLE_HAS_NEW_QUOTE_PRICE = "TITLE_HAS_NEW_QUOTE_PRICE";
        public static final String CONTENT_HAS_NEW_QUOTE_PRICE = "CONTENT_HAS_NEW_QUOTE_PRICE";

        // sent quote price
        public static final String TITLE_SENT_QUOTE_PRICE = "TITLE_SENT_QUOTE_PRICE";
        public static final String CONTENT_SENT_QUOTE_PRICE = "CONTENT_SENT_QUOTE_PRICE";

        // cancel quote price
        public static final String TITLE_CANCEL_QUOTE_PRICE = "TITLE_CANCEL_QUOTE_PRICE";
        public static final String CONTENT_CANCEL_QUOTE_PRICE = "CONTENT_CANCEL_QUOTE_PRICE";

        // negotiate quote price
        public static final String TITLE_NEGOTIATE_QUOTE_PRICE = "TITLE_NEGOTIATE_QUOTE_PRICE";
        public static final String CONTENT_NEGOTIATE_QUOTE_PRICE = "CONTENT_NEGOTIATE_QUOTE_PRICE";

        // confirm quote price
        public static final String TITLE_APPROVE_QUOTE_PRICE = "TITLE_APPROVE_QUOTE_PRICE";
        public static final String CONTENT_APPROVE_QUOTE_PRICE = "CONTENT_APPROVE_QUOTE_PRICE";

        // sale cancel order
        public static final String TITLE_SALE_CANCEL_ORDER = "TITLE_SALE_CANCEL_ORDER";
        public static final String CONTENT_SALE_CANCEL_ORDER = "CONTENT_SALE_CANCEL_ORDER";

        // Change status
        public static final String TITLE_CHANGE_QUOTE_STATUS = "TITLE_CHANGE_QUOTE_STATUS";
        public static final String CONTENT_CHANGE_QUOTE_STATUS = "CONTENT_CHANGE_QUOTE_STATUS";

        // Change status
        public static final String TITLE_CHANGE_PASSWORD = "TITLE_CHANGE_PASSWORD";
        public static final String CONTENT_CHANGE_PASSWORD = "CONTENT_CHANGE_PASSWORD";

    }

    public static class SaleNotification {
        // has new order
        public static final String TITLE_HAS_NEW_ORDER_SALE = "TITLE_HAS_NEW_ORDER_SALE";
        public static final String CONTENT_HAS_NEW_ORDER_SALE = "CONTENT_HAS_NEW_ORDER_SALE";

        // has new quote order
        public static final String TITLE_HAS_NEW_QUOTE_PRICE_SALE = "TITLE_HAS_NEW_QUOTE_PRICE_SALE";
        public static final String CONTENT_HAS_NEW_QUOTE_PRICE_SALE = "CONTENT_HAS_NEW_QUOTE_PRICE_SALE";

        // sent quote price
        public static final String TITLE_SENT_QUOTE_PRICE_SALE = "TITLE_SENT_QUOTE_PRICE_SALE";
        public static final String CONTENT_SENT_QUOTE_PRICE_SALE = "CONTENT_SENT_QUOTE_PRICE_SALE";

        // cancel quote price
        public static final String TITLE_CANCEL_QUOTE_PRICE_SALE = "TITLE_CANCEL_QUOTE_PRICE_SALE";
        public static final String CONTENT_CANCEL_QUOTE_PRICE_SALE = "CONTENT_CANCEL_QUOTE_PRICE_SALE";

        // negotiate quote price
        public static final String TITLE_NEGOTIATE_QUOTE_PRICE_SALE = "TITLE_NEGOTIATE_QUOTE_PRICE_SALE";
        public static final String CONTENT_NEGOTIATE_QUOTE_PRICE_SALE = "CONTENT_NEGOTIATE_QUOTE_PRICE_SALE";

        // confirm quote price
        public static final String TITLE_APPROVE_QUOTE_PRICE_SALE = "TITLE_APPROVE_QUOTE_PRICE_SALE";
        public static final String CONTENT_APPROVE_QUOTE_PRICE_SALE = "CONTENT_APPROVE_QUOTE_PRICE_SALE";

        // sale cancel order
        public static final String TITLE_SALE_CANCEL_ORDER_SALE = "TITLE_SALE_CANCEL_ORDER_SALE";
        public static final String CONTENT_SALE_CANCEL_ORDER_SALE = "CONTENT_SALE_CANCEL_ORDER_SALE";

        // Change status
        public static final String TITLE_CHANGE_QUOTE_STATUS_SALE = "TITLE_CHANGE_QUOTE_STATUS_SALE";
        public static final String CONTENT_CHANGE_QUOTE_STATUS_SALE = "CONTENT_CHANGE_QUOTE_STATUS_SALE";
    }

    public static class EntityField {
        public static final String DOCUMENT_ID = "documentId";
        public static final String ORDER_ID = "orderId";
        public static final String MESSAGE = "message";

        public static final String TOTAL_PRICE = "totalPrice";
        public static final String CUSTOMER_CODE = "customerCode";

        public static final String USERNAME = "username";
    }
}
