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
    }
}
