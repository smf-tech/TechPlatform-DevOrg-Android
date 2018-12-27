package com.platform.utility;

public class Constants {

    public static final int VERIFY_OTP_REQUEST = 1000;

    public static final int SMS_RECEIVE_REQUEST = 1;
    public static final String SMS_RECEIVE_IDENTIFIER = "android.provider.Telephony.SMS_RECEIVED";

    public static class App {
        // public static final String SMF_MODE = "SMF";
        public static final String BJS_MODE = "BJS";
        public static final String PASSWORD = "password";
        public static final String CLIENT_SECRET = "client_secret";
        public static final String CLIENT_ID = "client_id";
        public static final String GRANT_TYPE = "grant_type";

        public static final String FIRST_TIME_KEY = "FirstTimeKey";
        public static final String FIRST_TIME_CODE = "FirstTimeCode";

        public static final String LANGUAGE_LOCALE = "LanguageLocale";
        public static final String LANGUAGE_CODE = "languageCode";
        public static final String LANGUAGE_ENGLISH = "en";
        public static final String LANGUAGE_MARATHI = "mr";
        public static final String LANGUAGE_HINDI = "hi";
    }

    public static class Login {
        public static final String LOGIN_OTP_VERIFY_DATA = "loginOtpData";
        public static final String USERNAME = "username";
        public static final String USER_GENDER = "gender";
        public static final String OTP = "otp";
    }

    public static class FormsFactory {
        public static final String TEXT_TEMPLATE = "text";
    }
}
