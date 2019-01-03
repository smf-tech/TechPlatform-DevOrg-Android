package com.platform.utility;

public class Constants {

    public static final int SMS_RECEIVE_REQUEST = 1;
    public static final int CAMERA_REQUEST = 2;

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    public static final String RESULT_CODE = "ResultCode";

    public static final Integer CHOOSE_IMAGE_FROM_CAMERA = 100;
    public static final Integer CHOOSE_IMAGE_FROM_GALLERY = 101;

    public static final String SMS_RECEIVE_IDENTIFIER = "android.provider.Telephony.SMS_RECEIVED";

    public static class App {
        static final String APP_DATA = "AppData";
        public static final String BJS_MODE = "BJS";

        // public static final String SMF_MODE = "SMF";
        // public static final String PASSWORD = "password";

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
        public static final String LOGIN_VERIFIED_DATA = "loginVerifiedData";
        public static final String LOGIN_OBJ = "loginObj";

        // public static final String USERNAME = "username";
        // public static final String OTP = "otp";
        static final String AUTHORIZATION = "Authorization";

        public static final String USER_F_NAME = "firstName";
        public static final String USER_M_NAME = "middleName";
        public static final String USER_L_NAME = "lastName";
        public static final String USER_BIRTH_DATE = "dob";
        public static final String USER_EMAIL_ID = "email";
        public static final String USER_GENDER = "gender";
    }

    public static class FormsFactory {
        public static final String TEXT_TEMPLATE = "text";
    }
}
