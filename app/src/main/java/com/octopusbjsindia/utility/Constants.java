package com.octopusbjsindia.utility;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static boolean GET_MODELS = false;
    public static final String MARK_ATTENDANCE = "Attendance";
    public static final String MARK_INTERVIEW = "Interview";
    public static final String APPROVE = "approved";
    public static final String REJECT = "rejected";
    public static final int GPS_REQUEST = 1;
    public static final int CAMERA_REQUEST = 2;
    public static final int IS_ROLE_CHANGE = 3;
    public static final int READ_EXTERNAL_STORAGE=4;
    public static final int CALL_PHONE=5;
    public static final int READ_PHONE_STORAGE = 1;
    public static final int WRITE_EXTERNAL_STORAGE = 5;

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    public static final String ERROR = "error";
    public static final String RESPONSE_DATA = "data";
    public static final String KEY_SEPARATOR = ".";
    public static final String KEY_ATTENDANCDE="attendace_id";

    public static final Integer CHOOSE_IMAGE_FROM_CAMERA = 100;
    public static final Integer CHOOSE_IMAGE_FROM_GALLERY = 101;
    public static final Integer CHOOSE_PDF_FROM_STORAGE = 111;
    public static final Integer TIMEOUT_ERROR_CODE = 504;
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final String AND = "and";
    public static final String AND_CHARACTER = "&";

    public static final String ONLINE_SUBMIT_FORM_TYPE = "Online";
    public static final String OFFLINE_SUBMIT_FORM_TYPE = "Offline";
    public static final String ONLINE_UPDATE_FORM_TYPE = "OnlineUpdate";
    public static final String OFFLINE_UPDATE_FORM_TYPE = "OfflineUpdate";

    public static final String FORM_DATE = "yyyy-MM-dd";
    public static final String DAY_MONTH_YEAR = "dd MMM yyyy";
    public static final String FORM_DATE_FORMAT = "dd MMM yyyy @ hh:mm aa";
    public static final String LIST_DATE_FORMAT = "dd MMM yyyy hh:mm:ss.SSS";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSSSS";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm";
    public static final String DATE_TIME_FORMAT_ = "dd-MMM-YY hh:mm aa";
    public static final String DAY_MONTH_FORMAT = "dd MMM";
    public static final String EVENT_DATE_FORMAT = "dd MMM yyyy";
    public static final String TIME_FORMAT_ = "HH:mm";
    public static final String TIME_FORMAT = "hh:mm aa";
    public static final String TIME_FORMAT_ATT = "HH:mm:ss aa";

    public static final String WEEK_FORMAT = "EEEE";
    public static final String ATTENDANCE_DATE="yyyy/MM/dd HH:mm:ss";
    public static final String BATCH_CREATED_DATE="yyyy-MM-dd HH:mm:ss";
    public static final String DECIMAL_FORMAT = "#0.00";

    public static class App {
        static final String APP_DATA = "AppData";
        public static final String BJS_MODE = "BJS";
        static final String USER_LOC_OBJ = "userLocationObj";
        public static final String DATABASE_NAME = "TechPlatform";
        public static final String CHANNEL_ID = "BJS";

        public static final String CLIENT_SECRET = "client_secret";
        public static final String CLIENT_ID = "client_id";
        public static final String GRANT_TYPE = "grant_type";
        public static final String REFRESH_TOKEN = "refresh_token";
        public static final String SCOPE = "scope";

        public static final String FIRST_TIME_KEY = "FirstTimeKey";

        static final String FIRST_TIME_CODE = "FirstTimeCode";

        static final String LANGUAGE_LOCALE = "LanguageLocale";
        static final String LANGUAGE_CODE = "languageCode";

        public static final String[] APP_LANGUAGE = {"English", "मराठी", "हिंदी"};

        public static final String LANGUAGE_ENGLISH = "en";
        public static final String LANGUAGE_MARATHI = "mr";
        public static final String LANGUAGE_HINDI = "hi";
        public static final String LANGUAGE_DEFAULT = "default";

        static final String UNREAD_NOTIFICATION_COUNT = "unreadCount";
        static final String FILE_EXTENSION = ".txt";

        public static final String FirebaseTopicProjectWise = "ProjectWise";
        public static final String FirebaseTopicProjectRoleWise = "ProjectRoleWise";
        public static final String deviceId = "Device_Id";

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
        static final String USER_ROLE_ACCESS_OBJ = "userRoleAccessObj";
        static final String USER_DEVICE_MATCH = "userDeviceMatch";

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
        public static final String KEY_ATTENDACE_ID="attendace_id";
    }

    public static class Location {
        public static final String COUNTRY = "country";
        public static final String STATE = "state";
        public static final String DISTRICT = "district";
        public static final String CITY = "city";
        public static final String TALUKA = "taluka";
        public static final String VILLAGE = "village";
        public static final String CLUSTER = "cluster";
        public static final String SCHOOL = "school";
        public static final String GRAM_PANCHAYAT = "gram_panchayat";
        public static final String LEARNING_CENTER = "learning_center";
        public static final String LATITUDE = "lat";
        public static final String LONGITUDE = "long";
    }

    public static class JurisdictionLevelName {
        public static final String COUNTRY_LEVEL = "Country";
        public static final String STATE_LEVEL = "State";
        public static final String DISTRICT_LEVEL = "District";
        // added city level for matrimony project
        public static final String CITY_LEVEL = "City";
        public static final String TALUKA_LEVEL = "Taluka";
        public static final String CLUSTER_LEVEL = "Cluster";
        public static final String GRAM_PANCHAYAT = "Gram_Panchayat";
        public static final String VILLAGE_LEVEL = "Village";
        public static final String SCHOOL_LEVEL = "School";
        public static final String LEARNING_CENTER = "Learning_Center";
    }

    public static class Home {
        public static final String HOME_DATA = "homeObj";
        public static final String FORMS = "Forms";
        public static final String MEETINGS = "Meetings";
        public static final String PLANNER = "Planner";
        public static final String APPROVALS = "Approvals";
        public static final String REPORTS = "Reports";
        public static final String WEBMODULE = "Webmodule";
        public static final String WEBMODULE_NAME = "webmodule";
        public static final String CONTENT="Content";
        public static final String HOME="Home";
        public static final String STORIES="Stories";
        public static final String CONNECT="Connect";
        public static final String TO_OPEN="ToOpen";
        public static final Integer NEVIGET_TO= 001;
        public static final String MATRIMONY="Matrimony";
        public static final String SUPPORT="Support";
        public static final String SUJALAM_SUPHALAM = "SujalamSuphalam";
        public static final String SUJALAM_SUPHALAM_GP = "SS_GP_MODULE";
        public static final String SMARTGIRL = "Smart Girl";
        public static final String MV_SEL = "MVSel";
        public static final String MISSION_RAHAT = "MRahat";


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
        public  static final String NO_FILTER = "No_Filter";
        public  static final String PENDING_STATUS = "Pending";
        public  static final String APPROVED_STATUS = "Approved";
        public  static final String REJECTED_STATUS = "Rejected";
        public  static final String SAVED_STATUS = "Saved";
        public  static final String UNSYNC_STATUS = "Unsync";
        public  static final String SYNC_STATUS = "Sync";
        public static final String VISIBLE_IF_OR_CONDITION = "OR";
        public static final String VISIBLE_IF_AND_CONDITION = "AND";
        public static final String VISIBLE_IF_NO_CONDITION = "NO";
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
        public static final String LOCATION_TEMPLATE = "location";
        public static final String RATING_TEMPLATE = "rating";
        public static final String COMMENT_TEMPLATE = "comment";
        public static final String DROPDOWN_TEMPLATE = "dropdown";
        public static final String RADIO_GROUP_TEMPLATE = "radiogroup";
        public static final String CHECKBOX_TEMPLATE = "checkbox";
        public static final String FILE_TEMPLATE = "file";

        public static final String MULTI_TEXT_TEMPLATE = "multipletext";
        public static final String PANEL = "panel";
        public static final String MATRIX_DYNAMIC = "matrixdynamic";
        public static final String MATRIX_DROPDOWN = "matrixdropdown";
        public static final String IMAGE_PICKER = "imagepicker";

    }

    public static final String callUsNumber = "+9120-660-50154";
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
        public static final String SPINNER_CITY = "City";
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
        public static final String IMAGE_STORAGE_DIRECTORY = "/Octopus/Image/profile";
        public static final String PDF_STORAGE_DIRECTORY = "/Octopus/DOC";
        public static final String IMAGE_TYPE_FILE = "form";
        public static final String IMAGE_TYPE_PROFILE = "profile";
        public static final String IMAGE_TYPE_EVENT = "event";
        public static final String PDF = "pdf";
        //-------
        public static final String IMAGE_TYPE_ADHARCARD = "ADHAR";
        public static final String IMAGE_TYPE_EDUCATION = "EDUCATION";
        public static final String IMAGE_TYPE_MARITAL_CERTIFICATE = "SUPPORTDOC";
    }

    public class Notification {
        public static final String NOTIFICATION = "notificationClicked";
    }

    public class FormDynamicKeys {
        public static final String FORM_ID = "form_id";
        public static final String FORM_TITLE = "form_title";
        public static final String DATA = "data";
        public static final String METADATA = "metadata";
        public static final String FORM = "form";
        public static final String SUBMIT_COUNT = "submit_count";
        public static final String VALUES = "values";
        public static final String _ID = "_id";
        public static final String OID = "$oid";
        public static final String UPDATED_DATE_TIME = "updatedDateTime";
        public static final String CREATED_DATE_TIME = "createdDateTime";
        public static final String STATUS = "status";
        public static final String REJECTION_REASON = "rejection_reason";
        public static final String RESULT = "result";
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
        public static final Integer MEMBER_LIST = 002;
        public static final String SPINNER_ADD_FORMS = "Add forms";
        public static final String KEY_IS_DASHBOARD = "isDashboard";
        public static final String TO_OPEN = "ToOpen";
        public static final String EVENT_DETAIL = "EventDetail";
        public static final String MEMBERS_LIST = "MembersList";
        public static final String IS_NEW_MEMBERS_LIST = "IsNewMembersList";
        public static final String IS_DELETE_VISIBLE= "IsDeleteVisible";
        public static final String EVENT_TASK_ID = "EventTaskID";
        public static final String EVENTS_LABEL = "Event";
        public static final String TASKS_LABEL = "Task";
        public static final String REPEAT_EVENT_DATA = "RepeatEventDta";
        public static final String MEMBER_LIST_DATA = "MemberListData";
        public static final String MEMBER_LIST_COUNT = "MemberListCount";
        public static final String PLANNED_STATUS = "planned";
        public static final String COMPLETED_STATUS = "completed";
        public static final String LEAVE_TYPE_CL = "CL";
        public static final String LEAVE_TYPE_PAID = "Paid";
        public static final String LEAVE_TYPE_COMP_OFF = "CompOff";
        public static final String EVENTS_KEY_ = "Events";
        public static final String TASKS_KEY_ = "Tasks";
        public static final String ATTENDANCE_KEY_ = "Attendance";
        public static final String LEAVES_KEY_ = "Leaves";
        public static final String EVENTS_KEY = "event";
        public static final String TASKS_KEY = "task";
        public static final String ATTENDANCE_KEY = "attendance";
        public static final String LEAVES_KEY = "leave";
    }

    public static class Leave {
        public static final String PENDING_STATUS = "Pending";
        public static final String APPROVED_STATUS = "Approved";
        public static final String REJECTED_STATUS = "Rejected";
    }

    public static class ContentManagement{
        public static final String CONTENT_TOOLBAR_TEXT="ContentManagement";
    }

    public static class SSModule{
        public static final String MACHINE_TYPE = "machine";
        public static final String STRUCTURE_TYPE = "structure";
        public static final Integer MACHINE_CREATE_STATUS_CODE = 100;
        public static final Integer MACHINE_NEW_STATUS_CODE = 101;
        public static final Integer MACHINE_ELIGIBLE_STATUS_CODE = 102;
        public static final Integer MACHINE_NON_ELIGIBLE_STATUS_CODE = 103;
        public static final Integer MACHINE_MOU_DONE_STATUS_CODE = 104;
        public static final Integer MACHINE_MOU_TERMINATED_STATUS_CODE = 105;
        public static final Integer MACHINE_AVAILABLE_STATUS_CODE = 106;
        public static final Integer MACHINE_DEPLOYED_STATUS_CODE = 107;
        public static final Integer MACHINE_WORKING_STATUS_CODE = 108;
        public static final Integer MACHINE_BREAK_STATUS_CODE = 109;
        public static final Integer MACHINE_STOPPED_STATUS_CODE = 110;
        public static final Integer MACHINE_HALTED_STATUS_CODE = 111;
        public static final Integer MACHINE_MOU_EXPIRED_STATUS_CODE = 114;
        public static final Integer MACHINE_REALEASED_STATUS_CODE = 115;
        public static final Integer MACHINE_PAUSE_STATUS_CODE = 113;
        public static final Integer ACCESS_CODE_VIEW_STRUCTURES = 101;
        public static final Integer ACCESS_CODE_ADD_MACHINE = 108;
        public static final Integer ACCESS_CODE_VIEW_MACHINES = 109;
        public static final Integer ACCESS_CODE_MOU_MACHINE = 110;
        public static final Integer ACCESS_CODE_ELIGIBLE_MACHINE = 111;
        public static final Integer ACCESS_CODE_MOU_TERMINATE = 112;
        public static final Integer ACCESS_CODE_AVAILABLE_MACHINE = 113;
        public static final Integer ACCESS_CODE_DEPLOY_MACHINE = 114;
        public static final Integer ACCESS_CODE_MACHINE_VISIT_VALIDATION_FORM = 115;
        public static final Integer ACCESS_CODE_SILT_TRANSPORT_FORM = 116;
        public static final Integer ACCESS_CODE_DIESEL_RECORD_FORM = 117;
        public static final Integer ACCESS_CODE_MACHINE_SHIFT_FORM = 118;
        public static final Integer ACCESS_CODE_MACHINE_RELEASE = 119;
        public static final Integer ACCESS_CODE_ADD_STRUCTURE = 100;
        public static final Integer ACCESS_CODE_SAVE_OFFLINE_STRUCTURE = 102;
        public static final Integer ACCESS_CODE_PREPARED_STRUCTURE = 103;
        public static final Integer ACCESS_CODE_COMMUNITY_MOBILISATION = 105;
        public static final Integer ACCESS_CODE_VISIT_MONITORTNG = 106;
        public static final Integer ACCESS_CODE_STRUCTURE_COMPLETE = 107;
        public static final Integer ROLE_CODE_SS_OPERATOR = 113;
        public static final Integer ROLE_CODE_MR_HOSPITAL_INCHARGE = 135;
        public static final Integer STRUCTURE_APPROVED = 115;
        public static final Integer STRUCTURE_PREPARED = 116;
        public static final Integer STRUCTURE_IN_PROGRESS =	117;
        public static final Integer STRUCTURE_HALTED = 118;
        public static final Integer STRUCTURE_PARTIALLY_COMPLETED = 119;
        public static final Integer STRUCTURE_COMPLETED = 120;
        public static final Integer STRUCTURE_CLOSED = 121;
        public static final Integer STRUCTURE_NON_COMPLIANT = 122;
        public static final Integer STRUCTURE_PARTIALLY_CLOSED = 123;
        public static final Integer ACCESS_CODE_STATE = 120;
        public static final Integer ACCESS_CODE_DISTRICT = 121;
        public static final Integer ACCESS_CODE_TALUKA = 122;
        public static final Integer ACCESS_CODE_VILLAGE = 123;
        public static final Integer ACCESS_CODE_STRUCTURE_CLOSE = 124;
        public static final Integer ACCESS_CODE_CREATE_FEED = 125;
        public static final Integer ACCESS_CODE_DELETE_FEED = 126;
        public static final Integer ACCESS_CODE_STRUCTURE_BOUNDARY = 127;
        public static final Integer ACCESS_CODE_MACHINE_MOU_UPLOAD = 128;
        public static final Integer ACCESS_CODE_MACHINE_EDIT_READING = 130;
        public static final Integer ACCESS_CODE_MACHINE_SIGN_OFF = 129;
        public static final Integer ACCESS_CODE_ADD_OPERATOR = 131;
        public static final Integer ACCESS_CODE_ASSIGN_OPERATOR = 132;
        public static final Integer ACCESS_CODE_REALISE_OPERATOR = 133;
        public static final Integer ACCESS_CODE_OFFLINE_LOCATION_ALLOWED = 134;
        public static final Integer ACCESS_CODE_CREATE_MEET = 154;
        public static final Integer ACCESS_CODE_MY_SABORDINET = 155;
        public static final Integer ACCESS_CODE_BENEFICIARY_DETAILS = 156;
        public static final Integer ACCESS_CODE_COMMUNITY_MOBILIZATION = 157;
        public static final Integer ACCESS_CODE_VILLAGE_DEMAND = 158;
        public static final Integer ACCESS_CODE_DALLY_PROGRESS = 159;
        public static final Integer ACCESS_CODE_DALLY_PROGRESS_VALIDATION = 160;
        public static final Integer ACCESS_CODE_STRUCTURE_MASTER_GP = 161;
        public static final Integer ACCESS_CODE_DAILY_MACHINE_RECORD = 134;
        public static final Integer ACCESS_CODE_ADD_DONOR = 182;
    }

    public static class OperatorModule {
        public static final String MACHINE_START_READING = "machinestartreading";
        public static final String MACHINE_END_READING = "machineendreading";
        public static final String MACHINE_START_IMAGE = "machinestartimage";
        public static final String MACHINE_END_IMAGE = "machineendimage";
        public static final String PROJECT_RELEVENT_LOGO = "projectlogo";
        public static final String APP_CONFIG_RESPONSE = "appconfigresponse";
    }
    public static class SmartGirlModule {
        public static final int CODE_IS_DATE_NEEDED = 303;

        public static final String BENEFICIARY_ID = "beneficiary_id";
        public static final String TRAINER_ID = "trainer_id";
        public static final String BATCH_ID = "batch_id";
        public static final String WORKSHOP_ID = "workshop_id";
        public static final String TRAINER_lIST = "trainerlist";
        public static final String BENEFICIARY_lIST = "beneficiarylist";
        public static final Integer BATCH_WORKSHOP_RESULT= 111;

        public static final String FORM_STATUS = "formStatus";
        //Access code

        public static final int ACCESS_CODE_CREATE_BATCH = 135;
        public static final int ACCESS_CODE_EDIT_BATCH = 136;
        public static final int ACCESS_CODE_CREATE_WORKSHOP = 137;
        public static final int ACCESS_CODE_EDIT_WORKSHOP = 138;
        public static final int ACCESS_CODE_ADD_TRAINER = 139;
        public static  final int ACCESS_CODE_ADD_BENEFICIARY = 140;
        public static final int ACCESS_CODE_REGISTER_BATCH = 141;
        public static final int ACCESS_CODE_REGISTER_WORKSHOP = 142;

        public static final int ACCESS_CODE_PRE_TEST = 143;
        public static final int ACCESS_CODE_PRE_FEEDBACK = 144;
        public static final int ACCESS_CODE_POST_FEEDBACK = 145;
        public static final int ACCESS_CODE_PRE_FEEDBACK_WORKSHOP = 146;
        public static final int ACCESS_CODE_POST_FEEDBACK_WORKSHOP = 147;

        public static final int ACCESS_CODE_ORGANIZER_FEEDBACK = 148;
        public static final int ACCESS_CODE_PARENTS_FEEDBACK = 149;
        public static final int ACCESS_CODE_MOCK_TEST = 150;
        public static final int ACCESS_CODE_VIEW_PROFILE = 151;

        public static final int ACCESS_CODE_CANCEL_BATCH = 152;
        public static final int ACCESS_CODE_CANCEL_WORKSHOP = 153;

        // TODO
        public static final int ACCESS_CODE_WORKSHOP_SUPPORT = 158;
        public static final int ACCESS_CODE_BATCH_SUPPORT = 157;
        public static final int ACCESS_CODE_EMAIL_DASHBOARD = 162;
        public static final int ACCESS_CODE_EMAIL_FEEDBACK = 163;
        public static final int ACCESS_CODE_EMAIL_BATCH_FEEDBACK = 164;


//prod

        public static final String MOCK_TEST_FORM = "5f33e9da9f8b7d2ba4493e93";
        public static final String PRE_TEST_FORM = "5f33e8f69f8b7d2ba4493e92";

        public static final String ORGANISER_FEEDBACK_FORM = "5f33e4c75b5f3a3fb534e224";
        public static final String PARENTS_FEEDBACK_FORM = "5f33e5c95b5f3a3fb534e225";


        public static final String POST_FEEDBACK_FORM = "5f33e88128979a23351b8ec4";
        public static final String PRE_FEEDBACK_FORM = "5f33ea53da9b5736bf1d1fb5";



        public static final String PRE_FEEDBACK_WORKSHOP_FORM = "5f33e971da9b5736bf1d1fb4";
        public static final String POST_FEEDBACK_WORKSHOP_FORM = "5f33eafb9f8b7d2ba4493e94";


        public static final String BATCH_SUPPORT_DOC_FORM = "5faa228906398b0e09183e44";
        public static final String WORKSHOP_SUPPORT_DOC_FORM = "5faa222e244ba177f315802a";

//stage
     /*   public static final String MOCK_TEST_FORM = "5ea82d7b2c81b43be40b21f7";
        public static final String PRE_TEST_FORM = "5ea6a6b385046668f74b7f97";

        public static final String ORGANISER_FEEDBACK_FORM = "5ea7cb4849c5d7695b51203f";
        public static final String PARENTS_FEEDBACK_FORM = "5ea7cfa1d2e7326994634a32";


        public static final String POST_FEEDBACK_FORM = "5ea6ae606d30f831af2bfc74";
        public static final String PRE_FEEDBACK_FORM = "5ea7d58689bf971e0b5fd9a2";



        public static final String PRE_FEEDBACK_WORKSHOP_FORM = "5ea7bf66ba182a798b313c48";
        public static final String POST_FEEDBACK_WORKSHOP_FORM = "5ea7c6ad49c5d7695b51203e";

        public static final String BATCH_SUPPORT_DOC_FORM = "5fa64c482d366d1445425ff3";
        public static final String WORKSHOP_SUPPORT_DOC_FORM = "5fa6468e2d366d1445425ff2";
*/

    }

    public static class MatrimonyModule {
        public static final String NEWLY_JOINED_SECTION = "newly_joined";
        public static final String VERIFICATION_PENDING_SECTION = "verification_pending";
        public static final String ALL_USERS_SECTION = "all_users";
        public static final String MEET_USERS_SECTION = "meet_users";
        public static final String BLOCKED_USER_SECTION = "blockUsers";
        public static final String BANGED_USER_SECTION = "isBan";
        public static final Integer FLAG_UPDATE_RESULT = 110;
        public static final String MALE = "male";
        public static final String FEMALE = "female";
        public static final String OTHER = "other";
    }

    public static class MissionRahat {
        public static final int ACCESS_CODE_MACHINE_CREATE = 165;
        public static final int ACCESS_CODE_HOSPTAL_CREATE = 166;
        public static final int ACCESS_CODE_REQUIREMENT_APPROVAL = 167;
        public static final int ACCESS_CODE_REQUIREMENT_FORM = 170;
        public static final int ACCESS_CODE_DAILY_REPORT = 171;
        public static final int ACCESS_CODE_VIEW_MACHINE_LIST = 172;
        public static final int ACCESS_CODE_DOWNLOAD_MOU = 173;
        public static final int ACCESS_CODE_NEW_PATIENT = 174;
        public static final int ACCESS_CODE_SUBMIT_MOU = 175;
        public static final int ACCESS_CODE_VIEW_REQUIREMENT_LIST = 176;
        public static final int ACCESS_CODE_ASSIGN_MACHINES_DISTRICT = 177;
        public static final int ACCESS_CODE_ASSIGN_MACHINES_TALUKA = 178;
        public static final int ACCESS_ADD_HOSPITAL = 179;

        public static final int RECORD_UPDATE = 1101;
        public static final int HOSPITAL_SELECTION_FOR_INCHARGE = 2;
        public static final int HOSPITAL_SELECTION_FOR_RESULT = 1;

        public static final int ACCESS_CODE_MACHINE_TAKEOVER = 180;
        public static final int ACCESS_CODE_MACHINE_HANDOVER = 181;

        public static final int HANDOVER = 2;
        public static final int TAKEOVER = 1;

    }

    public static class VideoTutorialModule {
        public static final String VIDEO_SEEN = "video_seen";
        public static final String VIDEO_NOT_SEEN = "video_not_seen";
    }

    public static class HeightMasterData {
        public static Map<String, String> heightMap = new HashMap<String, String>() {{
            put("4", "4'");
            put("4.01", "4' 1\"");
            put("4.02", "4' 2\"");
            put("4.03", "4' 3\"");
            put("4.04", "4' 4\"");
            put("4.05", "4' 5\"");
            put("4.06", "4' 6\"");
            put("4.07", "4' 7\"");
            put("4.08", "4' 8\"");
            put("4.09", "4' 9\"");
            put("4.10", "4' 10\"");
            put("4.11", "4' 11\"");
            put("5", "5'");
            put("5.01", "5' 1\"");
            put("5.02", "5' 2\"");
            put("5.03", "5' 3\"");
            put("5.04", "5' 4\"");
            put("5.05", "5' 5\"");
            put("5.06", "5' 6\"");
            put("5.07", "5' 7\"");
            put("5.08", "5' 8\"");
            put("5.09", "5' 9\"");
            put("5.10", "5' 10\"");
            put("5.11", "5' 11\"");
            put("6", "6'");
            put("6.01", "6' 1\"");
            put("6.02", "6' 2\"");
            put("6.03", "6' 3\"");
            put("6.04", "6' 4\"");
            put("6.05", "6' 5\"");
            put("6.06", "6' 6\"");
            put("6.07", "6' 7\"");
            put("6.08", "6' 8\"");
            put("6.09", "6' 9\"");
            put("6.10", "6' 10\"");
            put("6.11", "6' 11\"");
            put("7", "7'");
        }};
    }
}
