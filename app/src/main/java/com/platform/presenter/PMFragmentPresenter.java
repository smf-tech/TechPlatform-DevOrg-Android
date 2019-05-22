package com.platform.presenter;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.platform.Platform;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.listeners.PlatformRequestCallListener;
import com.platform.models.forms.FormResult;
import com.platform.models.pm.Processes;
import com.platform.request.PMRequestCall;
import com.platform.utility.PlatformGson;
import com.platform.utility.Util;
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

            if (error != null && error.networkResponse != null) {
                if (error.networkResponse.statusCode == 504) {
                    if (error.networkResponse.data != null) {
                        String json = new String(error.networkResponse.data);
                        json = Util.trimMessage(json);
                        if (json != null) {
                            Util.showToast(json, fragmentWeakReference.get().getActivity());
                        } else {
                            Util.showToast(Platform.getInstance().getString(R.string.msg_slow_network),
                                    fragmentWeakReference.get().getActivity());
                        }
                    } else {
                        Util.showToast(Platform.getInstance().getString(R.string.msg_slow_network),
                                fragmentWeakReference.get().getActivity());
                    }
                } else {
                    Util.showToast(fragmentWeakReference.get().getString(R.string.unexpected_error_occurred),
                            fragmentWeakReference.get().getActivity());
                    Log.e("onErrorListener",
                            "Unexpected response code " + error.networkResponse.statusCode);
                }
            }
        }
    }
}
