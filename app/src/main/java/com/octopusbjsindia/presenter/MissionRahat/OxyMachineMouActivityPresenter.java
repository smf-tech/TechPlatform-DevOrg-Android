package com.octopusbjsindia.presenter.MissionRahat;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.MissionRahat.MouRequestModel;
import com.octopusbjsindia.models.events.CommonResponseStatusString;
import com.octopusbjsindia.models.ssgp.VdcBdRequestModel;
import com.octopusbjsindia.presenter.CreateStructureActivityPresenter;
import com.octopusbjsindia.presenter.ssgp.VDCBDFormFragmentPresenter;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.view.activities.MissionRahat.CreateMachineActivity;
import com.octopusbjsindia.view.activities.MissionRahat.OxyMachineMouActivity;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class OxyMachineMouActivityPresenter implements APIPresenterListener {

    private WeakReference<OxyMachineMouActivity> mContext;
    private final String TAG = OxyMachineMouActivityPresenter.class.getName();

    private static final String KEY_SUBMIT_MOU = "submitmoudata";

    public OxyMachineMouActivityPresenter(OxyMachineMouActivity mContext) {
        this.mContext = new WeakReference<>(mContext);
    }

    public void submitMouData(String paramjson) {

        mContext.get().showProgressBar();

        final String url = BuildConfig.BASE_URL + Urls.SSGP.BENEFICIARY_DETAILS_REPORT;
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(KEY_SUBMIT_MOU, paramjson, url);
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
            if (requestID.equalsIgnoreCase(OxyMachineMouActivityPresenter.KEY_SUBMIT_MOU)) {
                mContext.get().onSuccessListener(requestID,response);
            }
        }
    }
}
