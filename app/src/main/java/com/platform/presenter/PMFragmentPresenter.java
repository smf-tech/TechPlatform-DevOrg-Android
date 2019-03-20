package com.platform.presenter;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.platform.database.DatabaseManager;
import com.platform.listeners.PlatformRequestCallListener;
import com.platform.models.forms.FormResult;
import com.platform.models.pm.Processes;
import com.platform.request.PMRequestCall;
import com.platform.utility.PlatformGson;
import com.platform.view.fragments.PMFragment;

import java.lang.ref.WeakReference;
import java.util.List;

@SuppressWarnings("CanBeFinal")
public class PMFragmentPresenter implements PlatformRequestCallListener {

    private final String TAG = this.getClass().getName();
    private static WeakReference<PMFragment> fragmentWeakReference;

    public PMFragmentPresenter(PMFragment pmFragment) {
        fragmentWeakReference = new WeakReference<>(pmFragment);
    }

    public void getAllProcess() {
        PMRequestCall requestCall = new PMRequestCall();
        requestCall.setListener(this);

        fragmentWeakReference.get().showProgressBar();
        requestCall.getAllProcess();
    }

    public static List<FormResult> getAllNonSyncedSavedForms(Context context) {
        return DatabaseManager.getDBInstance(context).getNonSyncedPendingForms();
    }

    @Override
    public void onSuccessListener(String response) {
        Log.i(TAG, "Success: " + response);

        Processes data = PlatformGson.getPlatformGsonInstance().fromJson(response, Processes.class);

        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            fragmentWeakReference.get().showNextScreen(data);
        }
    }

    @Override
    public void onFailureListener(String message) {
        Log.i(TAG, "Fail: " + message);
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {
        Log.i(TAG, "Error: " + error);
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
        }
    }
}
