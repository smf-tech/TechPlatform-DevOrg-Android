package com.octopusbjsindia.presenter.MissionRahat;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.MissionRahat.SearchListResponse;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataResponse;
import com.octopusbjsindia.models.events.CommonResponseStatusString;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.MissionRahat.OxyMachineDailyReportActivity;
import com.octopusbjsindia.view.activities.MissionRahat.PatientInfoActivity;

import java.lang.ref.WeakReference;

public class PatientInfoPresenter implements APIPresenterListener {

    private WeakReference<PatientInfoActivity> mContext;
    private final String TAG = PatientInfoPresenter.class.getName();

    public static final String KEY_SUBMIT_PATIENTS_DATA = "submitpatientsdata";
    public static final String KEY_CREATE_MACHINE_MASTER_DATA = "getMasterData";

    public PatientInfoPresenter(PatientInfoActivity mContext) {
        this.mContext = new WeakReference<>(mContext);
    }


    public void getMasterData() {
        mContext.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        final String Url = BuildConfig.BASE_URL + Urls.MissionRahat.GET_CREATE_MACHINE_MASTER_DATA;
        requestCall.getDataApiCall(KEY_CREATE_MACHINE_MASTER_DATA, Url);
    }


    public void getPatientInfo() {
        mContext.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        final String Url = BuildConfig.BASE_URL + Urls.MissionRahat.HOSPITAL_LIST;
        requestCall.getDataApiCall("GET_HOSPITALS", Url);
    }

    public void submitDailyReportData(String paramjson) {
        mContext.get().showProgressBar();
        final String url = BuildConfig.BASE_URL + Urls.MissionRahat.SAVE_PATIENT_DETAILS;
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(KEY_SUBMIT_PATIENTS_DATA, paramjson, url);
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
                if (requestID.equalsIgnoreCase(PatientInfoPresenter.KEY_SUBMIT_PATIENTS_DATA)) {
                    CommonResponseStatusString commonResponse = new Gson().fromJson(response, CommonResponseStatusString.class);
                    if (commonResponse.getCode() == 200) {
                        mContext.get().onSuccessListener(KEY_SUBMIT_PATIENTS_DATA, response);
                    } else {
                        mContext.get().onFailureListener(KEY_SUBMIT_PATIENTS_DATA, commonResponse.getMessage());
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
                }if (requestID.equalsIgnoreCase("GET_HOSPITALS")) {

                    //SearchListResponse responseData = new Gson().fromJson(response, SearchListResponse.class);
                    mContext.get().setPatientInfo("GET_HOSPITALS", response);
                }
            }
        }
    }
}
