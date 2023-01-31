package com.octopusbjsindia.presenter;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.appconfig.AppConfigResponseModel;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.request.SplashActivityRequestCall;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.SplashActivity;

public class SplashActivityPresenter implements APIDataListener {


    private final String GET_APP_CONFIG = "getappconfig";

    private SplashActivity mContext;

    public SplashActivityPresenter(SplashActivity mContext) {
        this.mContext = mContext;
    }

    /*@Override
    public void onFailureListener(String requestID, String message) {
        //mContext.hideProgressBar();
        mContext.showMessage(requestID, message,300 );
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        //mContext.hideProgressBar();
        mContext.showMessage(requestID, error.getMessage(), 400);
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        //mContext.hideProgressBar();
        Gson gson = new Gson();
        CommonResponse commonResponse = gson.fromJson(response, CommonResponse.class);
        if(commonResponse.getCode()==1000){
            Utils.signOut(mContext);
            mContext.showMessage(requestID, commonResponse.getMessage(), commonResponse.getCode());
            mContext.finish();
            return;
        } else if (commonResponse.getCode() == 200) {
            switch (requestID){
                case GET_APP_CONFIG:
                    mContext.showMessage(requestID,response, commonResponse.getCode());
                    break;
            }

        } else {
            mContext.showMessage(requestID, commonResponse.getMessage(), 100);
        }
    }*/

    public void getAppConfig(String mobileNumber) {
        //mContext.showProgressBar();
        final String url = BuildConfig.BASE_URL + String.format(Urls.Configuration.API_CONFIG);
        SplashActivityRequestCall requestCall = new SplashActivityRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getApiCall(GET_APP_CONFIG, url);

    }

    @Override
    public void onFailureListener(String requestID, String message) {
        Util.showToast(message,mContext);
        mContext.GotoNextScreen();
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        //Util.showToast("Not able to get Config Parameters",mContext);
        mContext.GotoNextScreen();
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        AppConfigResponseModel appConfigResponseModel
                = new Gson().fromJson(response, AppConfigResponseModel.class);
        Gson gson = new Gson();
        CommonResponse commonResponse = gson.fromJson(response, CommonResponse.class);
        mContext.checkForceUpdate(requestID,response, commonResponse.getStatus());
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void closeCurrentActivity() {

    }
}
