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



    }

    public class Report {
        public static final String GET_ALL_REPORTS = "api/reports";
    }

    public class Events {
        public static final String GET_CATEGORY = "api/event-types";
        public static final String GET_EVENTS = "api/events?status=%1$s";
        public static final String GET_MEMBERS_LIST = "api/users";
        public static final String SUBMIT_EVENT= "api/events";
    }
}
