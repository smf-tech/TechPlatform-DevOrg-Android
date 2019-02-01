package com.platform.utility;

public class Urls {

    public static final String BASE_URL = "http://10.53.17.35/";

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
        public static final String GET_MODULES = "api/modules/%1$s/%2$s";
    }

    public class PM {
        public static final String GET_PROCESS = "api/forms/schema";
        public static final String GET_PROCESS_DETAILS = "api/forms/schema/%1$s";
        public static final String CREATE_FORM = "api/forms/result/%1$s";
    }

    public class TM {
        public static final String GET_PENDING_REQUESTS = "api/users/approvals";
    }
}
