package com.platform.utility;

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
    }

    public class Report {
        public static final String GET_ALL_REPORTS = "api/reports";
    }

    public class PlannerDashboard {
        public static final String GET_PLANNER_DASHBOARD = "api/plannersummary";
    }

    public class Events {
        public static final String GET_EVENTS = "api/events?status=%1$s";
        public static final String GET_MEMBERS_LIST = "api/addmember";
        public static final String GET_FORMS_LIST= "api/addform";
        public static final String SUBMIT_EVENT= "api/createEvent";
        public static final String GET_ATTENDANCE_CODE = "api/generateAttendanceCode";
    }
}
