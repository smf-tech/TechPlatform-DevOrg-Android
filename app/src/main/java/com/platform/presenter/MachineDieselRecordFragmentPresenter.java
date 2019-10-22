package com.platform.presenter;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.listeners.APIPresenterListener;
import com.platform.models.events.CommonResponse;
import com.platform.view.fragments.MachineDieselRecordFragment;

import java.lang.ref.WeakReference;

public class MachineDieselRecordFragmentPresenter implements APIPresenterListener {
    private WeakReference<MachineDieselRecordFragment> fragmentWeakReference;

    public MachineDieselRecordFragmentPresenter(MachineDieselRecordFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }
    @Override
    public void onFailureListener(String requestID, String message) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            fragmentWeakReference.get().onFailureListener(requestID,message);
        }
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            if (error != null) {
                fragmentWeakReference.get().onErrorListener(requestID,error);
            }
        }
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        if (fragmentWeakReference == null) {
            return;
        }
        fragmentWeakReference.get().hideProgressBar();
        try {
            if (response != null) {
//                if (requestID.equalsIgnoreCase(MachineDieselRecordFragmentPresenter.SUBMIT_MACHINE_SHIFTING_FORM)) {
//                    CommonResponse responseOBJ = new Gson().fromJson(response, CommonResponse.class);
//                    fragmentWeakReference.get().showResponse(responseOBJ.getMessage(),
//                            MachineDieselRecordFragmentPresenter.SUBMIT_MACHINE_SHIFTING_FORM, responseOBJ.getStatus());
//                }
            }
        }catch (Exception e) {
            fragmentWeakReference.get().onFailureListener(requestID, e.getMessage());
        }
    }
}
