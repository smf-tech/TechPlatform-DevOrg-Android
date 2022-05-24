package com.octopusbjsindia.presenter.MissionRahat;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataResponse;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.models.events.CommonResponseStatusString;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.MissionRahat.OxyMachineDailyReportActivity;
import com.octopusbjsindia.view.activities.MissionRahat.OxyMachineMouActivity;

import java.lang.ref.WeakReference;

public class OxyMachineDailyReportPresenter implements APIPresenterListener {

    private WeakReference<OxyMachineDailyReportActivity> mContext;
    private final String TAG = OxyMachineDailyReportPresenter.class.getName();

    public static final String KEY_SUBMIT_DAILY_REPORT = "submitReportData";
    public static final String KEY_CREATE_MACHINE_MASTER_DATA = "getMasterData";

    public OxyMachineDailyReportPresenter(OxyMachineDailyReportActivity mContext) {
        this.mContext = new WeakReference<>(mContext);
    }


    public void getMasterData() {
        mContext.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        final String Url = BuildConfig.BASE_URL + Urls.MissionRahat.GET_CREATE_MACHINE_MASTER_DATA;
        requestCall.getDataApiCall(KEY_CREATE_MACHINE_MASTER_DATA, Url);
    }


    public void submitDailyReportData(String paramjson) {
        mContext.get().showProgressBar();
        final String url = BuildConfig.BASE_URL + Urls.MissionRahat.SEND_MACHINE_DAILY_REPORT;
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(KEY_SUBMIT_DAILY_REPORT, paramjson, url);
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
        if (mContext != null && mContext.get() != null) {
            mContext.get().hideProgressBar();
            if (response != null) {
                if (requestID.equalsIgnoreCase(OxyMachineDailyReportPresenter.KEY_SUBMIT_DAILY_REPORT)) {
                    CommonResponseStatusString commonResponse = new Gson().fromJson(response, CommonResponseStatusString.class);
                    if (commonResponse.getCode() == 200) {
                        mContext.get().onSuccessListener(KEY_SUBMIT_DAILY_REPORT, response);
                    } else {
                        mContext.get().onFailureListener(KEY_SUBMIT_DAILY_REPORT, commonResponse.getMessage());
                    }

                } else if (requestID.equalsIgnoreCase(KEY_CREATE_MACHINE_MASTER_DATA)) {

                    // Please note, here we have used same model class for data parsing as SS master data model class.
                    // So, dont do any change in model class for Mission Rahat project as it can affect to SS.
                    // If need change, create another model class for Mission Rahat.

                    MasterDataResponse masterDataResponse = new Gson().fromJson(response, MasterDataResponse.class);
                    if (masterDataResponse.getStatus() == 1000) {
                        Util.logOutUser(mContext.get());
                    } else {
                        mContext.get().setMasterData(masterDataResponse);
                    }
                }
            }
        }
    }
}
