package com.platform.syncAdapter;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.platform.request.OrgRolesRequestCall;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = SyncAdapter.class.getSimpleName();
    private ContentResolver mContentResolver;

    SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        mContentResolver = context.getContentResolver();
    }

    public SyncAdapter(final Context context, final boolean autoInitialize,
                             final boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);

        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s,
                              ContentProviderClient contentProviderClient,
                              SyncResult syncResult) {

        getRolesDetails();
    }

    private static void getRolesDetails() {
        Log.e(TAG, "getRolesDetails: ");
        OrgRolesRequestCall orgRolesRequestCall = new OrgRolesRequestCall();
        orgRolesRequestCall.syncRoles();
    }

}