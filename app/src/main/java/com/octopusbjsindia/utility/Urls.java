package com.octopusbjsindia.utility;

public class Urls {

    public class Login {
        public static final String GENERATE_OTP = "api/message/otp?phone=%1$s";
        public static final String GENERATE_TOKEN = "api/token";
        public static final String REFRESH_TOKEN = "api/refreshtoken";
    }

    public class Profile {
        public static final String SUBMIT_PROFILE = "api/updateUser/%1$s";
        public static final String GET_PROFILE = "api/getUserDetails";
        public static final String GET_ORGANIZATION = "api/organizations";
        public static final String GET_ORGANIZATION_PROJECTS = "api/projects/%1$s";
        public static final String GET_ORGANIZATION_ROLES = "api/roles/%1$s/%2$s";
        public static final String GET_JURISDICTION_LEVEL_DATA = "api/location/level/%1$s/%2$s/%3$s";
        public static final String GET_LOCATION_DATA = "api/locationV2/level";
        public static final String GET_LOCATION_DATA3 = "api/locationV3/level";
        public static final String UPLOAD_IMAGE = "api/upload-image";
        public static final String GET_MULTIPAL_PROFILE = "api/getUserProfileDetails";
        public static final String GET_ALL_LOCATION_DATA = "api/selectedLocationData/level";
        public static final String GET_ALL_LOCATION_DATA_V2 = "api/selectedLocationData/levelV2";
    }

    public class Home {
        public static final String GET_MODULES = "api/modules/%1$s/%2$s";
        public static final String GET_ROLE_ACCESS = "api/roleAccess";
    }

    public class PM {
        public static final String GET_DASHBOARD_DETAILS = "api/statuscount/%1$s/%2$s";
        public static final String GET_PROCESS = "api/forms/schema";
        public static final String GET_PROCESS_DETAILS = "api/forms/schema/%1$s";
        public static final String SET_PROCESS_RESULT = "api/forms/result/%1$s";
        public static final String GET_MV_USER_INFO = "api/mvUserInfo";
        public static final String GET_SUBMITTED_FORMS = "api/forms/%1$s";
    }

    public class TM {
        // approved|pending|rejected
        public static final String GET_PENDING_REQUESTS = "api/user/approvals?status=pending";
        public static final String GET_APPROVED_REQUESTS = "api/user/approvals?status=approved";
        public static final String GET_REJECTED_REQUESTS = "api/user/approvals?status=rejected";
        public static final String APPROVE_REJECT_REQUEST = "api/users/approval/%1$s";
        //landing page summary
        public static final String GET_APPROVALS_SUMMARY_REQUESTS = "api/teammanagmentsummary";
        public static final String GET_PENDING_APPROVAL_REQUESTS = "api/getlistbyfilter";
        public static final String GET_TM_FILTERS_REQUESTS = "api/teammanagmentfilter";
        public static final String GET_TM_USER_DETAILS_REQUESTS = "api/getuserbyfilter";
        public static final String GET_TM_USER_APPROVE_REJECT_REQUEST = "api/applicationapproval";

        public static final String GET_USER_FORM_RECORD = "api/forms/getUserFormRecords";


        public static final String PUT_UPDATE_FIREBASEID_TO_SERVER = "api/updateFirebaseId";
    }


    public class Report {
        public static final String GET_ALL_REPORTS = "api/reports";
    }

    public class PlannerDashboard {
        public static final String GET_PLANNER_DASHBOARD = "api/plannersummary";
    }

    public class Events {
        public static final String GET_EVENTS_BY_DAY = "api/getEventByDay";
        public static final String GET_EVENTS_BY_MONTH = "api/getEventByMonth";
        public static final String GET_MEMBERS_LIST = "api/addmember";
        public static final String GET_FORMS_LIST = "api/addform";
        public static final String SUBMIT_EVENT_TASK = "api/event_task";
        public static final String DELETE_EVENT_TASK = "api/deleteTask";
        public static final String GET_ATTENDANCE_CODE = "api/generateAttendanceCode";
        public static final String SET_ATTENDANCE_CODE = "api/submitAttendanceEvent";
        public static final String GET_EVENT_TASK_MEMBERS_LIST = "api/getEventMembers";
        public static final String GET_TASK_MEMBERS_LIST = "api/addmembertask";
        public static final String UPDATE_MEMBER_LIST = "api/addmembertoevent";
        public static final String SET_TASK_MARK_COMPLETE = "api/taskMarkComplete";
        public static final String DELETE_EVENT_TASK_MEMBERS = "api/deletemember";
        public static final String GET_ORGANIZATION_ROLES = "api/roleEvent";
    }

    public class Leaves {
        public static final String APPLY_LEAVE = "api/createLeave";
        public static final String REQUEST_COMPOFF_ = "api/applyCompoff";
        public static final String GET_MONTHLY_LEAVES = "api/getLeavesSummary/%1$s/%2$s";
        public static final String DELETE_USER_LEAVE = "api/deleteLeave/%1$s";
        public static final String HOLIDAY_LIST = "api/getYearHolidayList";
        public static final String LEAVE_BALANCE = "api/getUserLeaveBalance";
    }

    public class Attendance {
        public static final String GET_ALL_MONTH = "api/getuserattendance/";
        public static final String SUBMIT_ATTENDANCE = "api/insertAttendance";
        public static final String GET_TEAM_ATTENDANCE = "api/getTeamAttendance";
        public static final String GET_TEAM_USER_ATTENDANCE = "api/getTeamUserAttendance";
    }

    public class ContentManagement {
        public static final String GET_CONTENT_DATA = "api/contentDashboard/%1$s";
        public static final String SEND_DOWNLOADED_CONTENT_DETAILS = "api/insertContentDownloadedHistory";
    }

    public class Matrimony {
        //userRegistration
        public static final String USER_REGI_MASTER = "api/masterData";
        public static final String USER_REGI_SUBMIT_API = "api/insertUser";
        public static final String USER_UPDATE_SUBMIT_API = "api/bjsUserUpdate";
        public static final String GET_MATRIMONY_PROFILE_API = "api/getMeetUsers/%1$s";
        public static final String USER_APPROVAL_API = "api/userApproval";
        public static final String MARK_ATTENDANCE_INTERVIEW_API = "api/markAttendance_interview";
        public static final String MATRIMONY_MEETS = "api/getMeet";
        public static final String MATRIMONY_MEETS_UPCOMING = "api/getUpcomingMeet";
        public static final String MATRIMONY_MEET_TYPES = "api/meet_types";
        //public static final String MATRIMONY_USERS_LIST = "api/getMatrimonyRoleUsers";
        public static final String SUBMIT_MEET = "api/insertMeet";
        public static final String MEET_ARCHIVE_DELETE = "api/archiveMeet/%1$s/%2$s";
        public static final String MEET_ALLOCATE_BADGES = "api/allocateBadge/%1$s/%2$s";
        public static final String MEET_FINALISE_BADGES = "api/isFinalize/%1$s";
        public static final String ALL_FILTER_USERS = "api/getAllFilterUsers";
        public static final String GET_FILTER_MASTER_DATA = "api/matrimonyUserFilterMasterData";
        //added new filter education level in master data
        public static final String GET_FILTER_MASTER_DATA_V2 = "api/matrimonyUserFilterMasterDataV2";

        public static final String USER_DOC_VERIFY_API = "api/documentVerificaton";
        public static final String CRITERIA_MASTER_DATA = "api/masterDataMeetCriteria";
        public static final String BANNED_USERS = "api/bannedUsers";
        public static final String CREATE_SUBORDINATE = "api/createSubordinate";

        //for batches
        public static final String SHOW_MEET_BACHES = "api/group_batches/%1$s";

        public static final String PUBLISH_SAVED_MEET = "api/meetpublished/%1$s";
        public static final String DOWNLOAD_MEET_BOOKLET = "api/downloadBooklet/%1$s/%2$s";
        public static final String CHECK_USER_CREATED = "api/checkProfile/%1$s/%2$s";

        public static final String REGISTER_USER_TO_MEET = "api/registration_meet";
        public static final String MATRIMONY_NEW_USER = "api/newUserRegister";
        public static final String MATRIMONY_FIELD_MASTER = "api/MatrimonialFieldMaster";
        public static final String MISSING_FIELD_REQ = "api/MissingFields";
        public static final String BLOCK_UNBLOCK_USER = "api/adminBanUser";

        public static final String MATRIMONY_RECENTELY_JOINED_USERS = "api/newUserRegister";
        public static final String MATRIMONY_VERIFICATION_PENDING_USERS = "api/verificationPendingUser";
        public static final String MATRIMONY_SUBORDINATE_USERS = "api/getSubordinateRoleUsers";
        public static final String MATRIMONY_MASTER = "api/masterData";
        public static final String GET_PROFILE_DETAILS = "api/userProfileDetails";
        public static final String MEET_TRANSECTION_DETAILS = "api/transactionDetails";

    }

    public class SSModule {
        public static final String GET_SS_STRUCTURE_ANALYTICS = "api/getStructureAnalystV2";
        public static final String GET_SS_MACHINE_ANALYTICS = "api/getMachineAnalyticsV2";
        public static final String GET_SS_MASTER_DATA = "api/masterDataList";
        public static final String GET_GP_MASTER_DATA = "api/gpMasterdata";
        public static final String GET_SS_STRUCTURE_LIST = "api/structureList";
        public static final String GET_SS_MACHINE_LIST = "api/machineList";
        public static final String CREATE_MACHINE = "api/createMachine";
        public static final String UPDATE_STRUCTURE_MACHINE_STATUS = "api/statusChange/%1$s/%2$s/%3$s/%4$s";
        public static final String SEND_MACHINE_SIGN_OFF = "api/machineSignOff";
        public static final String UPDATE_MACHINE_STATUS_TO_AVAILABLE = "api/machineAvailable";
        public static final String SUBMIT_MOU = "api/machineMou";
        public static final String GET_MACHINE_DETAILS = "api/machineDetails/%1$s/%2$s";
        public static final String GET_MACHINE_WORKING_HOURS_RECORD = "api/workLog";
        public static final String MOU_TERMINATE_DEPLOY = "api/MOUTerminateDeployed";
        public static final String DEPLOY_MACHINE = "api/machineDeployed";
        public static final String SHIFT_MACHINE = "api/machineShift";
        public static final String CREATE_STRUCTURE = "api/createStructure";
        public static final String STRUCTURE_VISITE_MONITORING = "api/structureVisit";
        public static final String STRUCTURE_PREPARATION = "api/prepareStructure";
        public static final String STRUCTURE_COMPLETION = "api/sowStructure";
        public static final String COMMUNITY_MOBILISATION = "api/communityMobilisation";
        public static final String NON_UTILIZATION_URL = "api/machineNonUtilization/%1$s/%2$s/%3$s";
        public static final String CATCHMENT_VILLAGES = "api/catchmentVillages";
        public static final String MACHINE_DIESEL_RECORD_FORM = "api/dieselRecord";
        public static final String MACHINE_VISIT_VALIDATION_FORM = "api/machineVisit";
        public static final String MACHINE_SILT_TRANSPORT_RECORD_FORM = "api/siltDetails";
        public static final String MACHINE_MOU_FORM = "api/machineMou";
        public static final String MACHINE_MOU_UPLOAD = "api/machineMouUpload";
        public static final String STRUCTURE_BOUNDARY = "api/structureBoundary";
        public static final String OPERATORS_LIST = "api/getOperatorList";
        public static final String ASSIGN_OPERATORS = "api/assignOperator";
        public static final String RELEASE_OPERATORS = "api/releaseOperator";
        public static final String CREATE_OPERATOR = "api/createOperator";
        public static final String GET_GP_STRUCTURE_ANALYTICS = "api/GPStructureAnalyst";
        public static final String GET_GP_MACHINE_ANALYTICS = "api/GPMachineAnalytics";
    }

    public class OperatorApi {
        public static final String MACHINE_WORKLOG = "api/machineWorkLog";
        public static final String MACHINE_DATA = "api/getMachineData";
        public static final String MACHINE_DATA_WORKLOG = "api/getMachineWorkLogData";
        public static final String MACHINE_WORKLOG__DETAILS = "api/logDetails";
        public static final String MACHINE_WORKLOG_EDIT = "api/editWorkLog";
        public static final String MACHINE_DATA_SYNC = "api/dataSynch";

    }

    public class Configuration {
        public static final String API_CONFIG = "api/configuration";
    }

    public class Feeds {
        public static final String NEW_FEED = "api/newFeed";
        public static final String FEED_LIST = "api/getFeedList";
        public static final String DELETE_FEED = "api/deleteFeed";
        public static final String COMMENT_LIST = "api/commentList";
    }

    public class SmartGirl {
        public static final String GET_MASTER_TRAINER_DASHBOARD = "api/masterTrainerDashboard";
        public static final String GET_BATCH_CATEGORY = "api/getbatchCategory";
        public static final String CREATE_BATCH_API = "api/createBatch";
        public static final String EDIT_BATCH_API = "api/editBatch";
        public static final String GET_MASTER_TRAINERS_SG = "api/getAdditionalMasterTrainers";
        public static final String GET_BATCH_LIST_API = "api/getBatchList";
        public static final String ADD_TRAINER_TO_BATCH = "api/addTrainerToBatch";
        public static final String REGISTER_AS_TRAINER_TO_BATCH = "api/registerToBatch";
        public static final String CANCEL_BATCH_API = "api/cancelBatch";
        public static final String COMPLETE_BATCH_API = "api/completeBatch";

        public static final String CANCEL_WORKSHOP_API = "api/cancelWorkshop";
        public static final String COMPLETE_WORKSHOP_API = "api/completeWorkshop";
        public static final String TRAINER_PRE_TEST = "api/trainerPreTest";
        public static final String TRAINER_BATCH_FEEDBACK = "api/trainerBatchFeedback";

        public static final String TRAINER_MOCKTEST_TEST = "api/submitTrainerMockTest";

        public static final String GET_ALL_TRAINER_LIST_API = "api/getTrainers";
        public static final String GET_ALL_MASTER_LIST_API = "api/getMasterTrainers";
        public static final String GET_ALL_BENEFICIARY_LIST_API = "api/getBeneficiaries";

        public static final String GET_COMMUNITY_USER_INFO = "api/communityUserInfo";


        //workshops
        public static final String CREATE_WORKSHOP_API = "api/createWorkshop";
        public static final String EDIT_WORKSHOP_API = "api/editWorkshop";
        public static final String CREATE_WORKSHOP_LIST_API = "api/workshopList";

        public static final String GET_DAHSBOARDS_LIST_API = "api/masterTrainerDashboardList";

        public static final String REGISTER_TO_WORKSHOP = "api/registerToWorkshop";
        public static final String ADD_BENEFICIARY_TO_WORKSHOP = "api/addBeneficiaryToWorkshop";

        public static final String GET_BENEFICIARY_API = "api/getBeneficiaries";
        public static final String BENEFICIARY_WORKSHOP_FEEDBACK_API = "api/beneficiaryWorkshopFeedback";


        //user name from mobile
        public static final String GET_USER_NAME_FROM_MOBILE = "api/mvUserInfo";

        //get
        public static final String GET_MASTER_TRAINERS_API = "api/getMasterTrainers";
        public static final String GET_USER_PROJECT_PROFILE_API = "api/getUserProjectProfile";

        public static final String SEND_FEEDBACK_EMAIL = "api/sendFormData";
        public static final String GET_FILTER_TRAINER_LIST = "api/getFilterTrainerList";
        public static final String SEND_WORKSHOP_DATA_EMAIL = "api/downloadSMDashboard";
    }

    public class Support {
        public static final String GET_TICKETS = "api/getTicketList";
        public static final String ASSIGN_TICKETS = "api/assignTicket";
        public static final String CHANGE_STATUS = "api/changeStatus";
    }

    public class SEL {
        public static final String GET_SEL_CONTENT = "api/get_training_videos";
        public static final String SEND_VIDEO_STATUS = "api/send_video_status";
    }

    public class SSGP {
        public static final String CREATE_STRUCTURE = "api/createGPStructure";
        public static final String DAILY_PROGRESS_REPORT = "api/dailyProgessReport";
        public static final String DAILY_PROGRESS_REPORT_VALIDATION = "api/dmValidateMachineReport";
        public static final String COMMUNITY_MOBILIZATION_REPORT = "api/GPCommunityMobilisation";
        public static final String BENEFICIARY_DETAILS_REPORT = "api/GPbeneficiaryform";
        public static final String GET_GP_MACHINE_LIST = "api/GPMachineCodeList";
        public static final String GET_GP_STRUCURE_LIST = "api/GPStructureCodeList";
        public static final String SUBMIT_VILLAGE_DEMND = "api/villageDemndForm";
        public static final String GET_GP_STRUCTURE_ANALYTICS = "api/getGPStructureList";
        public static final String GET_GP_MACHINE_ANALYTICS = "api/GPmachineList";
    }

    public class MissionRahat {
        public static final String GET_CREATE_MACHINE_MASTER_DATA = "api/getMasterDataList";
        public static final String CREATE_MACHINE = "api/insertMachine";
        public static final String CREATE_HOSPITAL = "api/addHospital";
        public static final String GET_MR_ANALYTICS = "api/getMRAnalystics";
        public static final String HOSPITAL_LIST = "api/getHospitalList";
        public static final String MACHINE_LIST = "api/getMRMachineList";
        public static final String CONCENTRATOR_REQUEST = "api/addRequirmentForm";
        public static final String ADD_INCHARGE_REQUEST = "api/addInchargeHospital";
        public static final String CONCENTRATOR_REQUEST_LIST = "api/getAllRequirmentFormsList";
        public static final String CONCENTRATOR_REQUEST_DETAILS = "api/getReuirmentDetails";
        public static final String CONCENTRATOR_REQUEST_ACTION = "api/requirmentAction";
        public static final String SAVE_MACHINE_MOU = "api/saveMachineMOU";
        public static final String SEND_MACHINE_DAILY_REPORT = "api/machineDailyReport";
        //        public static final String GET_ALL_OXYMACHINE_LIST = "api/getAllOxyMachineList";
        public static final String GET_ALL_OXYMACHINE_LIST = "api/getAllOxyMachineListV2";
        public static final String SAVE_PATIENT_DETAILS = "api/saveMRPatientDetails";
        public static final String GET_REQUIREMENT_MOU_DETAILS = "api/getReuirmentMOUDetails";
        public static final String MOU_ON_MAIL = "api/sendMOUMailPDF";
        public static final String TERMS_AND_CONDITIONS_MISSION_RAHAT = "api/termPage";
        public static final String ASSIGN_MACHINE = "api/addMachineLocations";
        public static final String GET_MACHINE_PATIENT_INFO = "api/getMachinePatientData";
        public static final String MACHINE_HANDTAKEOVER_FORM = "api/machineHandTakeOverForm";


    }
}
