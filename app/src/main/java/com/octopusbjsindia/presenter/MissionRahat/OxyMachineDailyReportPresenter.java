package com.octopusbjsindia.presenter.MissionRahat;

import android.util.Log;

import com.android.volley.VolleyError;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.view.activities.MissionRahat.OxyMachineDailyReportActivity;
import com.octopusbjsindia.view.activities.MissionRahat.OxyMachineMouActivity;

import java.lang.ref.WeakReference;

public class OxyMachineDailyReportPresenter implements APIPresenterListener {

    private WeakReference<OxyMachineDailyReportActivity> mContext;
    private final String TAG = OxyMachineDailyReportPresenter.class.getName();

    private static final String KEY_SUBMIT_DAILY_REPORT = "submitReportData";
    public static final String GET_MASTER_DATA = "getMasterData";

    public OxyMachineDailyReportPresenter(OxyMachineDailyReportActivity mContext) {
        this.mContext = new WeakReference<>(mContext);
    }


    public void getSSMasterData(){

        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        String getSSMasterDaraUrl = BuildConfig.BASE_URL
                + String.format(Urls.SSModule.GET_SS_MASTER_DATA);
        Log.d(TAG, "getSSMasterDaraUrl: url" + getSSMasterDaraUrl);
        requestCall.getDataApiCall(GET_MASTER_DATA, getSSMasterDaraUrl);
    }


    public void submitDailyReportData(String paramjson) {



        final String url = BuildConfig.BASE_URL + Urls.SSGP.BENEFICIARY_DETAILS_REPORT;
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
        if (response != null) {
            if (requestID.equalsIgnoreCase(OxyMachineDailyReportPresenter.KEY_SUBMIT_DAILY_REPORT)) {
                mContext.get().showSuccessResponse(response);
            }
        }
    }
}
