package com.octopusbjsindia.syncAdapter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

@SuppressWarnings("unused")
public class SyncAdapterUtils {

    private static final String PREF_SETUP_COMPLETE = "setup_complete";
    public static final String ACCOUNT = "TechPlatform-DevOrg";
    public static final String AUTHORITY = "com.octopusbjsindia";
    public static final String ACCOUNT_TYPE = "com.octopusbjsindia.account";
    public static final String EVENT_SYNC_COMPLETED = "SyncCompleted";
    public static final String EVENT_SYNC_FAILED = "SyncFailed";
    public static final String EVENT_FORM_ADDED = "formAdded";
    public static final String EXTRA_TEXT = "SyncStatus";
    public static final String SYNCING_PENDING = "Syncing Pending";
    public static final String SUBMITTED_AND_SYNCED = "Submitted & Synced";
    public static final String PARTIAL_FORM_ADDED = "PartialFormAdded";
    public static final String PARTIAL_FORM_REMOVED = "PartialFormRemoved";
    public static final String EVENT_FORM_SUBMITTED = "formSubmitted";

    public class FormStatus {
        public static final int SYNCED = 1;
        public static final int UN_SYNCED = 0;
        public static final int PARTIAL = 2;
        static final int DELETED = -1;
    }

    /**
     * This method creates sync account
     *
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
//            ContentResolver.addPeriodicSync(account, AUTHORITY, new Bundle(), SYNC_FREQUENCY);
            newAccount = true;
        }

        // Schedule an initial sync if we detect problems with either our account or our local
        // data has been deleted. (Note that it's possible to clear app data WITHOUT affecting
        // the account list, so wee need to check both.)
        if (newAccount || !setupComplete) {
            manualRefresh();
            PreferenceManager.getDefaultSharedPreferences(context).edit()
                    .putBoolean(PREF_SETUP_COMPLETE, true).apply();
        }

    }

    /**
     * Helper method to trigger an immediate sync ("refresh").
     *
     * <p>This should only be used when we need to preempt the normal sync schedule. Typically, this
     * means the user has pressed the "refresh" button.
     * <p>
     * Note that SYNC_EXTRAS_MANUAL will cause an immediate sync, without any optimization to
     * preserve battery life. If you know new data is available (perhaps via a GCM notification),
     * but the user is not actively waiting for that data, you should omit this flag; this will give
     * the OS additional freedom in scheduling your sync request.
     */
    public static void manualRefresh() {
        Bundle b = new Bundle();
        //Disable sync backoff and ignore sync preferences. In other words...perform sync NOW!
        b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
       b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(
                GenericAccountService.GetAccount(ACCOUNT, ACCOUNT_TYPE), // Sync account
                AUTHORITY,
                b);                                             // Extras

    }

    public static void periodicSyncRequest() {
        Bundle b = new Bundle();
        ContentResolver.addPeriodicSync (GenericAccountService.GetAccount(ACCOUNT, ACCOUNT_TYPE),
                AUTHORITY,
                b,901);
        Log.d("periodicSyncRequest","called");
    }


}
