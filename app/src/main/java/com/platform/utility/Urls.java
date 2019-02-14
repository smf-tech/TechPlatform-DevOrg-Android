package com.platform.utility;

public class Urls {

    public class Login {
        public static final String GENERATE_OTP = "api/message/otp?phone=%1$s";
        public static final String GENERATE_TOKEN = "api/token?phone=%1$s&otp=%2$s";
        public static final String REFRESH_TOKEN = "oauth/token";
    }

    public class Profile {
        public static final String SUBMIT_PROFILE = "api/users/%1$s";
        public static final String GET_ORGANIZATION = "api/organizations";
        public static final String GET_ORGANIZATION_PROJECTS = "api/projects/%1$s";
        public static final String GET_ORGANIZATION_ROLES = "api/roles/%1$s";
        public static final String GET_STATES = "api/states";
        public static final String GET_JURISDICTION_LEVEL_DATA = "api/location/level/%1$s/%2$s";
    }

    public class Home {
        public static final String GET_MODULES = "api/modules/5c1b940ad503a31f360e1252/5c36ed50d503a3403f237f45";
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public class Roles {
        public static final String GET_ROLES = "api/roles/%1$s";
    }

    public class PM {
        public static final String GET_PROCESS = "api/forms/schema";
        public static final String GET_PROCESS_DETAILS = "api/forms/schema/%1$s";
        public static final String CREATE_FORM = "api/forms/result/%1$s";
        public static final String GET_FORM = "api/forms/result/%1$s";
    }

    public class TM {
        public static final String GET_PENDING_REQUESTS = "api/users/approvals";
        public static final String APPROVE_REJECT_REQUEST = "api/users/approval/%1$s";
    }

    public class Report {
        public static final String GET_ALL_REPORTS = "api/reports";
    }
}
