package com.platform.utility;

public class Constants {

    public static final int GPS_REQUEST = 1;
    public static final int CAMERA_REQUEST = 2;
    public static final int IS_ROLE_CHANGE = 3;

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    public static final String ERROR = "error";
    public static final String RESPONSE_DATA = "data";
    public static final String KEY_SEPARATOR = ".";

    public static final Integer CHOOSE_IMAGE_FROM_CAMERA = 100;
    public static final Integer CHOOSE_IMAGE_FROM_GALLERY = 101;

    public static final String PUSH_NOTIFICATION = "pushNotification";
//    public static final String SMS_RECEIVE_IDENTIFIER = "android.provider.Telephony.SMS_RECEIVED";

    public static final String ONLINE_SUBMIT_FORM_TYPE = "Online";
    public static final String OFFLINE_SUBMIT_FORM_TYPE = "Offline";
    public static final String ONLINE_UPDATE_FORM_TYPE = "OnlineUpdate";
    public static final String OFFLINE_UPDATE_FORM_TYPE = "OfflineUpdate";

    public static final String FORM_DATE = "yyyy-MM-dd";
    public static final String FORM_DATE_FORMAT = "dd MMM yyyy @ hh:mm ";
    static final String LIST_DATE_FORMAT = "dd MMM yyyy hh:mm:ss.SSS";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSSSS";
    public static final String MONTH_DAY_FORMAT = "MMMM dd";
    public static final String TIME_FORMAT = "hh:mm aa";

    public static class App {
        static final String APP_DATA = "AppData";
        public static final String BJS_MODE = "BJS";
        static final String USER_LOC_OBJ = "userLocationObj";
        public static final String DATABASE_NAME = "TechPlatform";

        public static final String CLIENT_SECRET = "client_secret";
        public static final String CLIENT_ID = "client_id";
        public static final String GRANT_TYPE = "grant_type";
        public static final String REFRESH_TOKEN = "refresh_token";
        public static final String SCOPE = "scope";

        static final String FIRST_TIME_KEY = "FirstTimeKey";
        static final String FIRST_TIME_CODE = "FirstTimeCode";

        static final String LANGUAGE_LOCALE = "LanguageLocale";
        static final String LANGUAGE_CODE = "languageCode";

        public static final String LANGUAGE_ENGLISH = "en";
        public static final String LANGUAGE_MARATHI = "mr";
        public static final String LANGUAGE_HINDI = "hi";
        public static final String LANGUAGE_DEFAULT = "default";

        static final String UNREAD_NOTIFICATION_COUNT = "unreadCount";
        static final String FILE_EXTENSION = ".txt";
    }

    public static class AssociatesType {
        public static final String ORG = "organisation";
    }

    public static class Login {
        public static final String LOGIN_OTP_VERIFY_DATA = "loginOtpData";
        static final String USER_MOBILE_NO = "userMobile";
        static final String LOGIN_OBJ = "loginObj";
        static final String USER_OBJ = "userObj";
        static final String USER_ORG = "userOrgObj";
        static final String USER_ROLE = "userRoleObj";
        static final String USER_PROJECT = "userProjectsObj";

        public static final String AUTHORIZATION = "Authorization";

        public static final String MALE = "male";
        public static final String FEMALE = "female";
        public static final String OTHER = "other";

        public static final String USER_NAME = "name";
        public static final String USER_F_NAME = "firstName";
        public static final String USER_M_NAME = "middleName";
        public static final String USER_L_NAME = "lastName";
        public static final String USER_BIRTH_DATE = "dob";
        public static final String USER_EMAIL = "email";
        public static final String USER_GENDER = "gender";
        public static final String USER_ROLE_ID = "role_id";
        public static final String USER_ORG_ID = "org_id";
        public static final String USER_ASSOCIATE_TYPE = "type";
        public static final String USER_PROJECTS = "project_id";
        public static final String USER_LOCATION = "location";
        public static final String USER_FIREBASE_ID = "firebase_id";
        public static final String USER_PROFILE_PIC = "profile_pic";

        public static final String USER_PHONE = "phone";
        public static final String USER_OTP = "otp";

        public static final String ACTION = "action";
        public static final String ACTION_EDIT = "edit";
    }

    public static class Location {
        public static final String STATE = "state";
        public static final String DISTRICT = "district";
        public static final String TALUKA = "taluka";
        public static final String VILLAGE = "village";
        public static final String CLUSTER = "cluster";
        public static final String LATITUDE = "lat";
        public static final String LONGITUDE = "long";
    }

    public static class JurisdictionLevelName {
        public static final String STATE_LEVEL = "State";
        public static final String DISTRICT_LEVEL = "District";
        public static final String TALUKA_LEVEL = "Taluka";
        public static final String CLUSTER_LEVEL = "Cluster";
        public static final String VILLAGE_LEVEL = "Village";
    }

    public static class Home {
        public static final String HOME_DATA = "homeObj";
        public static final String FORMS = "Forms";
        public static final String MEETINGS = "Meetings";
        //public static final String PLANNER = "Planner";
        public static final String APPROVALS = "Approvals";
        public static final String REPORTS = "Reports";
        //public static final String PROGRAM_MANAGEMENT = "Program Management";
        //public static final String MY_CONTENT= "MV Content";
        //public static final String TEAM_MANAGEMENT = "Team Management";
        //public static final String PLANNER = "Planner";
    }

    public static class PM {
        public static final String PROCESS_NAME = "processName";
        public static final String PROCESS_ID = "processId";
        public static final String FORM_ID = "formId";
        public static final String EDIT_MODE = "editMode";
        public static final String PARTIAL_FORM = "partialForm";
    }

    public static class TM {
        public static final String UPDATE_STATUS = "update_status";
        public static final String REJECTION_REASON = "reason";
        public static final String USER_APPROVALS = "User Approvals";
//        public static final String FORM_APPROVALS = "Form Approvals";
    }

    public static class RequestStatus {
        public static final String PENDING = "pending";
        public static final String APPROVED = "approved";
        public static final String REJECTED = "rejected";
        public static final String APPROVED_MODULE = "approved";
        public static final String DEFAULT_MODULE = "default";
    }

    public static class FormsFactory {
        public static final String TEXT_TEMPLATE = "text";
        public static final String COMMENT_TEMPLATE = "comment";
        public static final String DROPDOWN_TEMPLATE = "dropdown";
        public static final String RADIO_GROUP_TEMPLATE = "radiogroup";
        public static final String FILE_TEMPLATE = "file";
        public static final String PANEL = "panel";
        public static final String MATRIX_DYNAMIC = "matrixdynamic";
    }

    public static final String callUsNumber = "+9120-660-50226";
    // public static final String hangoutLink = "https://hangouts.google.com/group/AXhIbyg2tO8QkfDY2";
    public static final String playStoreLink = "https://play.google.com/store/apps/details?id=com.bjs.ss&hl=en";

    public static class Form {
        public static final String EXTRA_FORM_ID = "form_id";
        static final String GET_SUBMITTED_FORMS_FIRST_TIME = "firstTime";
    }

    public class MultiSelectSpinnerType {
        public static final String SPINNER_ORGANIZATION = "Organization";
        public static final String SPINNER_PROJECT = "Project";
        public static final String SPINNER_ROLE = "Role";
        public static final String SPINNER_STATE = "State";
        public static final String SPINNER_DISTRICT = "District";
        public static final String SPINNER_TALUKA = "Taluka";
        public static final String SPINNER_CLUSTER = "Cluster";
        public static final String SPINNER_VILLAGE = "Village";
        public static final String SPINNER_WHICH_DAYS = "WhichDays";
    }

    public class FormInputType {
        public static final String INPUT_TYPE_DATE = "date";
        static final String INPUT_TYPE_TIME = "time";
        static final String INPUT_TYPE_NUMBER = "number";
        static final String INPUT_TYPE_TELEPHONE = "tel";
        static final String INPUT_TYPE_NUMERIC = "numeric";
        static final String INPUT_TYPE_DECIMAL = "decimal";
        static final String INPUT_TYPE_ALPHABETS = "alphabets";
        static final String INPUT_TYPE_TEXT = "text";
    }

    public class Image {
        public static final String FILE_SEP = "/";
        public static final String IMAGE_PREFIX = "picture_";
        public static final String IMAGE_SUFFIX = ".jpg";
        public static final String IMAGE_STORAGE_DIRECTORY = "/MV/Image/profile";
        public static final String IMAGE_TYPE_FILE = "form";
        public static final String IMAGE_TYPE_PROFILE = "profile";
    }

    public class Notification {
        public static final String NOTIFICATION = "notificationClicked";
    }

    public class FormDynamicKeys {
        public static final String FORM_ID = "form_id";
        public static final String FORM_TITLE = "form_title";
        public static final String METADATA = "metadata";
        public static final String FORM = "form";
        public static final String SUBMIT_COUNT = "submit_count";
        public static final String VALUES = "values";
        public static final String _ID = "_id";
        public static final String OID = "$oid";
        public static final String UPDATED_DATE_TIME = "updatedDateTime";
        public static final String CREATED_DATE_TIME = "createdDateTime";
    }

    class Expression {
        static final String LESS_THAN_EQUALS = "<=";
        static final String GREATER_THAN_EQUALS = ">=";
        static final String EQUALS = "=";
        static final String SUBTRACTION = "-";
    }

    public class ValidationType {
        public static final String REGEX_TYPE = "regex";
        public static final String EXPRESSION_TYPE = "expression";
    }

    public class Action {
        public static final String ACTION_ADD = "Add";
        public static final String ACTION_DELETE = "Delete";
    }

    public static class Planner {

        public static final Integer REPEAT_EVENT = 001;

        public static final String KEY_IS_DASHBOARD = "isDashboard";
        public static final String TO_OPEN = "ToOpan";
        public static final String EVENT_DETAIL = "EventDetail";
        public static final String MEMBERS_LIST = "MembersList";
        public static final String IS_NEW_MEMBERS_LIST = "IsNewMembersList";
        public static final String TASK_DETAIL = "TaskDetail";
        public static final String EVENTS_LABEL = "EVENTS";
        public static final String TASKS_LABEL = "TASKS";
        public static final String PLANNED_STATUS = "PLANNED";
        public static final String COMPLETED_STATUS = "COMPLETED";
    }
}
