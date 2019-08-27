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
        public static final String GET_ORGANIZATION_ROLES = "api/roles/%1$s";
        public static final String GET_JURISDICTION_LEVEL_DATA = "api/location/level/%1$s/%2$s/%3$s";
        public static final String UPLOAD_IMAGE = "api/upload-image";
    }

    public class Home {
        public static final String GET_MODULES = "api/modules/%1$s/%2$s";
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
        public static final String MATRIMONY_MEET_TYPES = "api/meet_types";
        public static final String MEET_REFERENCES_LIST = "api/";
        public static final String MEET_ORGANIZERS_LIST = "api/";
        public static final String SUBMIT_MEET = "api/";
    }
}
