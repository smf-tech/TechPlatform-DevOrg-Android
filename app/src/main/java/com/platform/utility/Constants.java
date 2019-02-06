package com.platform.utility;

public class Constants {

    public static final int SMS_RECEIVE_REQUEST = 1;
    public static final int CAMERA_REQUEST = 2;
    public static final int IS_ROLE_CHANGE = 3;

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    public static final String PENDING = "pending";
    public static final String RESULT_CODE = "ResultCode";

    public static final Integer CHOOSE_IMAGE_FROM_CAMERA = 100;
    public static final Integer CHOOSE_IMAGE_FROM_GALLERY = 101;

    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final String SMS_RECEIVE_IDENTIFIER = "android.provider.Telephony.SMS_RECEIVED";

    public static final String ONLINE_SUBMIT_FORM_TYPE = "Online";
    public static final String OFFLINE_SUBMIT_FORM_TYPE = "Offline";

    public static final String LIST_DATE_FORMAT = "dd MMM yyyy";

    public static class App {
        static final String APP_DATA = "AppData";
        public static final String BJS_MODE = "BJS";
        static final String USER_LOC_OBJ = "userLocationObj";
        static final String JURISDICTION_LEVEL = "jurisdictionLevel";
        static final String JURISDICTION_LEVEL_DATA = "jurisdictionLevelData";

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
    }

    public static class Login {
        public static final String LOGIN_OTP_VERIFY_DATA = "loginOtpData";
        static final String USER_MOBILE_NO = "userMobile";
        static final String LOGIN_OBJ = "loginObj";
        static final String USER_OBJ = "userObj";

        static final String AUTHORIZATION = "Authorization";

        public static final String USER_F_NAME = "firstName";
        public static final String USER_M_NAME = "middleName";
        public static final String USER_L_NAME = "lastName";
        public static final String USER_BIRTH_DATE = "dob";
        public static final String USER_EMAIL = "email";
        public static final String USER_GENDER = "gender";
        public static final String USER_ROLE_ID = "role_id";
        public static final String USER_ORG_ID = "org_id";
        public static final String USER_PROJECTS = "projects";
        public static final String USER_LOCATION = "location";

        public static final String ACTION = "action";
        public static final String ACTION_EDIT = "edit";
    }

    public static class Location {
        public static final String STATE = "state";
        public static final String DISTRICT = "district";
        public static final String TALUKA = "taluka";
        public static final String VILLAGE = "village";
        public static final String CLUSTER = "cluster";
    }

    public static class JurisdictionLevelName {
        public static final String DISTRICT_LEVEL = "District";
        public static final String TALUKA_LEVEL = "Taluka";
        public static final String CLUSTER_LEVEL = "Cluster";
        public static final String VILLAGE_LEVEL = "Village";
    }

    public static class Home {
        public static final String HOME_DATA = "homeObj";
        public static final String PROGRAMME_MANAGEMENT = "Forms";
        public static final String TEAM_MANAGEMENT = "Team Management";
    }

    public static class PM {
        public static final String PROCESS_NAME = "processName";
        public static final String PROCESS_ID = "processId";
        public static final String RESPONSE = "response";
    }

    public static class TM {
        public static final String UPDATE_STATUS = "update_status";
    }

    public static class RequestStatus {
        public static final String PENDING = "pending";
        public static final String APPROVED = "approved";
        public static final String REJECTED = "rejected";
    }

    public static class FormsFactory {
        public static final String TEXT_TEMPLATE = "text";
        public static final String DROPDOWN_TEMPLATE = "dropdown";
    }

    public static final String playStoreLink = "https://play.google.com/store/apps/details?id=com.bjs.ss&hl=en";
}
