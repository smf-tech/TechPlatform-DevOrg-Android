package com.platform.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.platform.BuildConfig;
import com.platform.listeners.APIPresenterListener;
import com.platform.models.SujalamSuphalam.MachineDetailData;
import com.platform.models.events.CommonResponse;
import com.platform.request.APIRequestCall;
import com.platform.utility.Urls;
import com.platform.view.fragments.MachineMouFourthFragment;

import java.lang.ref.WeakReference;

public class MachineMouFourthFragmentPresenter implements APIPresenterListener {
    private WeakReference<MachineMouFourthFragment> fragmentWeakReference;
    private final String TAG = CreateMeetFirstFragmentPresenter.class.getName();
    public static final String SUBMIT_MOU ="submitMou";

    public MachineMouFourthFragmentPresenter(MachineMouFourthFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }

    public void submitMouData(MachineDetailData machineDetailData) {
        Gson gson = new GsonBuilder().create();
        String paramjson = gson.toJson(machineDetailData);
        final String submitMouUrl = BuildConfig.BASE_URL
                + String.format(Urls.SSModule.SUBMIT_MOU);
        Log.d(TAG, "submitMouUrl: url" + submitMouUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(SUBMIT_MOU, paramjson, submitMouUrl);
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
                CommonResponse responseOBJ = new Gson().fromJson(response, CommonResponse.class);
                fragmentWeakReference.get().showResponse(responseOBJ.getMessage(),
                        MachineMouFourthFragmentPresenter.SUBMIT_MOU, responseOBJ.getStatus());
            }
        }catch (Exception e){
            fragmentWeakReference.get().onFailureListener(requestID,e.getMessage());
        }
    }
}
