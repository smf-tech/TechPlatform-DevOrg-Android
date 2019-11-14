package com.platform.utility;

import java.util.Locale;

public class Urls {

    public class Login {
        public static final String GENERATE_OTP = "api/message/otp?phone=%1$s";
        public static final String GENERATE_TOKEN = "api/token";
        public static final String REFRESH_TOKEN = "api/refreshtoken";
    }

    public class Profile {
        public static final String SUBMIT_PROFILE = "api/users/%1$s";
        public static final String GET_PROFILE = "api/user";
        public static final String GET_ORGANIZATION = "api/organizations";
        public static final String GET_ORGANIZATION_PROJECTS = "api/projects/%1$s";
        public static final String GET_ORGANIZATION_ROLES = "api/roles/%1$s/%2$s";
        public static final String GET_JURISDICTION_LEVEL_DATA = "api/location/level/%1$s/%2$s/%3$s";
        public static final String UPLOAD_IMAGE = "api/upload-image";
    }

    public class Home {
        public static final String GET_MODULES = "api/modules/%1$s/%2$s";
        public static final String GET_ROLE_ACCESS = "api/roleAccess";
    }

    public class PM {
        public static final String GET_DASHBOARD_DETAILS = "api/statuscount/%1$s/%2$s";
        public static final String GET_PROCESS = "api/forms/schema";
        public static final String GET_PROCESS_DETAILS = "api/forms/schema/%1$s";
    }

    public class TM {
        // approved|pending|rejected
        public static final String GET_PENDING_REQUESTS = "api/user/approvals?status=pending";
        public static final String GET_APPROVED_REQUESTS = "api/user/approvals?status=approved";
        public static final String GET_REJECTED_REQUESTS = "api/user/approvals?status=rejected";
        public static final String APPROVE_REJECT_REQUEST = "api/users/approval/%1$s";
        //landing page summary
        public static final String GET_APPROVALS_SUMMARY_REQUESTS = "api/teammanagmentsummary";
        public static final String GET_PENDING_APPROVAL_REQUESTS ="api/getlistbyfilter";
        public static final String GET_TM_FILTERS_REQUESTS ="api/teammanagmentfilter";
        public static final String GET_TM_USER_DETAILS_REQUESTS ="api/getuserbyfilter";
        public static final String GET_TM_USER_APPROVE_REJECT_REQUEST ="api/applicationapproval";


        public static final String PUT_UPDATE_FIREBASEID_TO_SERVER ="api/updateFirebaseId";
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
        public static final String APPLY_LEAVE= "api/createLeave";
        public static final String REQUEST_COMPOFF_= "api/applyCompoff";
        public static final String GET_MONTHLY_LEAVES = "api/getLeavesSummary/%1$s/%2$s";
        public static final String DELETE_USER_LEAVE = "api/deleteLeave/%1$s";
        public static final String HOLIDAY_LIST= "api/getYearHolidayList";
        public static final String LEAVE_BALANCE= "api/getUserLeaveBalance";
    }

    public class Attendance
    {
        public static final String GET_ALL_MONTH="api/getuserattendance/";
        public static final String SUBMIT_ATTENDANCE="/api/insertAttendance";
        public static final String GET_TEAM_ATTENDANCE="/api/getTeamAttendance";
    }
    public class ContentManagement{
        public static final String GET_CONTENT_DATA="api/contentDashboard";
    }

    public class Matrimony {
        //userRegistration
        public static final String USER_REGI_MASTER = "api/masterData";
        public static final String USER_REGI_SUBMIT_API = "api/insertUser";
        public static final String GET_MATRIMONY_PROFILE_API = "api/getMeetUsers/%1$s";
        public static final String USER_APPROVAL_API = "api/userApproval";
        public static final String MARK_ATTENDANCE_INTERVIEW_API = "api/markAttendance_interview";
        public static final String MATRIMONY_MEETS = "api/getMeet";
        public static final String MATRIMONY_MEET_TYPES = "api/meet_types";
        public static final String MATRIMONY_USERS_LIST = "api/getMatrimonyRoleUsers";
        public static final String SUBMIT_MEET = "api/insertMeet";
        public static final String MEET_ARCHIVE_DELETE = "api/archiveMeet/%1$s/%2$s";
        public static final String MEET_ALLOCATE_BADGES = "api/allocateBadge/%1$s/%2$s";
        public static final String MEET_FINALISE_BADGES = "api/isFinalize/%1$s";

        //for batches
        public static final String SHOW_MEET_BACHES = "api/group_batches/%1$s";

        public static final String PUBLISH_SAVED_MEET = "api/meetpublished/%1$s";
        public static final String DOWNLOAD_MEET_BOOKLET = "api/downloadBooklet/%1$s/%2$s";
        public static final String CHECK_USER_CREATED = "api/checkProfile/%1$s/%2$s";

        public static final String REGISTER_USER_TO_MEET = "api/registration_meet";
    }

    public class SSModule {
        public static final String GET_SS_STRUCTURE_ANALYTICS = "api/structureAnalyst";
        public static final String GET_SS_MACHINE_ANALYTICS = "api/getMachineAnalytics";
        public static final String GET_SS_MASTER_DATA = "api/masterDataList";
        public static final String GET_SS_STRUCTURE_LIST = "api/structureList";
        public static final String GET_SS_MACHINE_LIST = "api/machineList";
        public static final String CREATE_MACHINE = "api/createMachine";
        public static final String UPDATE_STRUCTURE_MACHINE_STATUS = "api/statusChange/%1$s/%2$s/%3$s/%4$s";
        public static final String SUBMIT_MOU = "api/machineMou";
        public static final String GET_MACHINE_DETAILS = "api/machineDetails/%1$s/%2$s";
        public static final String GET_MACHINE_WORKING_HOURS_RECORD = "api/workLog";
        public static final String MOU_TERMINATE_DEPLOY = "api/MOUTerminateDeployed";
        public static final String DEPLOY_MACHINE = "api/machineDeployed";
        public static final String SHIFT_MACHINE = "api/machineShift";
        public static final String SUBMIT_MACHINE_VISIT = "api/machineVisit";
        public static final String CREATE_STRUCTURE = "api/createStructure";
        public static final String STRUCTURE_VISITE_MONITORING = "api/structureVisit";
        public static final String STRUCTURE_PREPARATION = "api/prepareStructure";
        public static final String STRUCTURE_COMPLETION = "api/sowStructure";
        public static final String COMMUNITY_MOBILISATION = "api/communityMobilisation";
        public static final String NON_UTILIZATION_URL = "api/machineNonUtilization/%1$s/%2$s/%3$s";
        public static final String CATCHMENT_VILLAGES = "api/catchmentVillages";
    }
}
