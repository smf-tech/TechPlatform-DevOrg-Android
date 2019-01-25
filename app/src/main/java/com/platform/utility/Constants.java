package com.platform.utility;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.platform.syncAdapter.GenericAccountService;

public class Constants {

    public static final int SMS_RECEIVE_REQUEST = 1;
    public static final int CAMERA_REQUEST = 2;

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    public static final String PENDING = "pending";
    public static final String RESULT_CODE = "ResultCode";

    public static final Integer CHOOSE_IMAGE_FROM_CAMERA = 100;
    public static final Integer CHOOSE_IMAGE_FROM_GALLERY = 101;

    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final String SMS_RECEIVE_IDENTIFIER = "android.provider.Telephony.SMS_RECEIVED";

    public static class App {
        static final String APP_DATA = "AppData";
        public static final String BJS_MODE = "BJS";
        static final String USER_LOC_OBJ = "userLocationObj";
        static final String JURISDICTION_LEVEL = "jurisdictionLevel";
        static final String JURISDICTION_LEVEL_DATA = "jurisdictionLevelData";

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
        static final String USER_MOBILE_NO = "userMobile";
        static final String LOGIN_OBJ = "loginObj";
        static final String USER_OBJ = "userObj";

        static final String AUTHORIZATION = "Authorization";

        public static final String USER_F_NAME = "firstName";
        public static final String USER_M_NAME = "middleName";
        public static final String USER_L_NAME = "lastName";
        public static final String USER_BIRTH_DATE = "dob";
        public static final String USER_EMAIL = "email";
        public static final String USER_GENDER = "gender";
        public static final String USER_ROLE_ID = "role_id";
        public static final String USER_ORG_ID = "org_id";
        public static final String USER_PROJECTS = "projects";
        public static final String USER_LOCATION = "location";
    }

    public static class Location {
        public static final String STATE = "state";
        public static final String DISTRICT = "district";
        public static final String TALUKA = "taluka";
        public static final String VILLAGE = "village";
        public static final String CLUSTER = "cluster";
    }

    public static class JurisdictionLevelName {
        public static final String DISTRICT_LEVEL = "District";
        public static final String TALUKA_LEVEL = "Taluka";
        public static final String CLUSTER_LEVEL = "Cluster";
        public static final String VILLAGE_LEVEL = "Village";
    }

    public static class Home {
        public static final String Programme_Management = "Forms";
    }

    @SuppressWarnings("CanBeFinal")
    public static class PM {
        public static String PROCESS_NAME = "processName";
        public static String PROCESS_ID = "processId";
        public static String RESPONSE = "response";
    }

    public static class FormsFactory {
        public static final String TEXT_TEMPLATE = "text";
        public static final String DROPDOWN_TEMPLATE = "dropdown";
    }

    public static final String playStoreLink = "https://play.google.com/store/apps/details?" +
            "id=com.bjs.ss&hl=en";

    public static class SyncAdapter {
        static final long SYNC_FREQUENCY = 60 * 60;
        static final String PREF_SETUP_COMPLETE = "setup_complete";
        public static final String ACCOUNT = "TechPlatform-DevOrg";
        public static final String AUTHORITY = "com.platform";
        public static final String ACCOUNT_TYPE = "com.platform.account";

        public static final int COMPLETE = 1;
        public static final int PENDING = 2;
        public static final int ERROR = 3;
        public static final int NOT_STARTED = -1;
        public static final int STARTED = 0;

        /**
         * This method creates sync account
         * @param context - Context
         */
        public static void createSyncAccount(Context context) {
            boolean newAccount = false;
            boolean setupComplete = PreferenceManager
                    .getDefaultSharedPreferences(context).getBoolean(PREF_SETUP_COMPLETE, false);

            Account account = GenericAccountService.GetAccount(ACCOUNT, ACCOUNT_TYPE);

            AccountManager accountManager = (AccountManager)
                    context.getSystemService(Context.ACCOUNT_SERVICE);

            if (accountManager != null &&
                    accountManager.addAccountExplicitly(account, null, null)) {

                ContentResolver.setIsSyncable(account, AUTHORITY, 1);
                ContentResolver.setSyncAutomatically(account, AUTHORITY, true);
                // FIXME: 24-01-2019 change sync frequency
                ContentResolver.addPeriodicSync(account, AUTHORITY, new Bundle(), SYNC_FREQUENCY);
                newAccount = true;
            }

            // Schedule an initial sync if we detect problems with either our account or our local
            // data has been deleted. (Note that it's possible to clear app data WITHOUT affecting
            // the account list, so wee need to check both.)
            if (newAccount || !setupComplete) {
                manualRefresh();
                PreferenceManager.getDefaultSharedPreferences(context).edit()
                        .putBoolean(PREF_SETUP_COMPLETE, true).commit();
            }

        }

        /**
         * Helper method to trigger an immediate sync ("refresh").
         *
         * <p>This should only be used when we need to preempt the normal sync schedule. Typically, this
         * means the user has pressed the "refresh" button.
         *
         * Note that SYNC_EXTRAS_MANUAL will cause an immediate sync, without any optimization to
         * preserve battery life. If you know new data is available (perhaps via a GCM notification),
         * but the user is not actively waiting for that data, you should omit this flag; this will give
         * the OS additional freedom in scheduling your sync request.
         */
        public static void manualRefresh() {
            Bundle b = new Bundle();
            // Disable sync backoff and ignore sync preferences. In other words...perform sync NOW!
            b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
            b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            ContentResolver.requestSync(
                    GenericAccountService.GetAccount(ACCOUNT, ACCOUNT_TYPE), // Sync account
                    AUTHORITY,
                    b);                                             // Extras
        }
    }
}
