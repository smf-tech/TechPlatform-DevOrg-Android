package com.octopusbjsindia.presenter.MissionRahat;

import com.android.volley.VolleyError;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.view.activities.MissionRahat.OxyMachineListActivity;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class OxyMachineListActivityPresenter implements APIPresenterListener {

    private WeakReference<OxyMachineListActivity> mContext;
    private final String TAG = OxyMachineListActivityPresenter.class.getName();

    public static final String GET_MACHINE_LIST = "machineList";

    public OxyMachineListActivityPresenter(OxyMachineListActivity mContext) {
        this.mContext = new WeakReference<>(mContext);
    }

//    public void getOxyMachineList(String url){
//        mContext.get().showProgressBar();
//
//        //final String url = "https://run.mocky.io/v3/09b8651a-7f95-456e-9b8c-546bb33a2c82";
//        APIRequestCall requestCall = new APIRequestCall();
//        requestCall.setApiPresenterListener(this);
//        requestCall.getDataApiCall(GET_MACHINE_LIST, url);
//    }

    public void getOxyMachineList(String url, HashMap<String, Object> map) {
        mContext.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        //final String Url = BuildConfig.BASE_URL + Urls.MissionRahat.GET_ALL_OXYMACHINE_LIST;
        requestCall.postDataApiCall(GET_MACHINE_LIST, new JSONObject(map).toString(), url);
    }


    @Override
    public void onFailureListener(String requestID, String message) {
        if (mContext != null && mContext.get() != null) {
            mContext.get().hideProgressBar();
            mContext.get().onFailureListener(requestID, message);
        }
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (mContext != null && mContext.get() != null) {
            mContext.get().hideProgressBar();
            if (error != null) {
                mContext.get().onErrorListener(requestID, error);
            }
        }
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        if (response != null) {
            if (requestID.equalsIgnoreCase(OxyMachineListActivityPresenter.GET_MACHINE_LIST)) {
                mContext.get().onSuccessListener(requestID,response);
            }
        }
    }
}
