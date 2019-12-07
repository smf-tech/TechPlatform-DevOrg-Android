package com.octopusbjsindia.presenter;

import com.android.volley.VolleyError;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.view.fragments.SiltTransportationRecordFragment;

import java.lang.ref.WeakReference;

public class SiltTransportationRecordFragmentPresenter implements APIPresenterListener {
    private WeakReference<SiltTransportationRecordFragment> fragmentWeakReference;

    public SiltTransportationRecordFragmentPresenter(SiltTransportationRecordFragment tmFragment) {
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
