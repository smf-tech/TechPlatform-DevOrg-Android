package com.octopusbjsindia.presenter.MissionRahat;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.MissionRahat.HospitalModel;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataResponse;
import com.octopusbjsindia.models.events.CommonResponseStatusString;
import com.octopusbjsindia.models.profile.JurisdictionLevelResponse;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.MissionRahat.CreateHospitalActivity;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class CreateHospitalActivityPresenter implements APIPresenterListener {

    final String GET_STATE = "getState";
    final String GET_DISTRICT = "getDistrict";
    final String GET_TALUKA = "getTaluka";
    final String CREATE_HOSPITAL = "createHospital";
    private WeakReference<CreateHospitalActivity> mContext;
    private final String TAG = CreateHospitalActivityPresenter.class.getName();

    private static final String KEY_SELECTED_ID = "selected_location_id";
    private static final String KEY_JURIDICTION_TYPE_ID = "jurisdictionTypeId";
    private static final String KEY_LEVEL = "jurisdictionLevel";

    public CreateHospitalActivityPresenter(CreateHospitalActivity mContext) {
        this.mContext = new WeakReference<>(mContext);
    }

    public void getLocationData(String selectedLocationId, String jurisdictionTypeId, String levelName) {
        HashMap<String, String> map = new HashMap<>();
        map.put(KEY_SELECTED_ID, selectedLocationId);
        map.put(KEY_JURIDICTION_TYPE_ID, jurisdictionTypeId);
        map.put(KEY_LEVEL, levelName);

        mContext.get().showProgressBar();
        final String getLocationUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.GET_LOCATION_DATA);
        Log.d(TAG, "getLocationUrl: url" + getLocationUrl);
        mContext.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        if (levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.STATE_LEVEL)) {
            requestCall.postDataApiCall(GET_STATE, new JSONObject(map).toString(), getLocationUrl);
        } else if (levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.DISTRICT_LEVEL)) {
            requestCall.postDataApiCall(GET_DISTRICT, new JSONObject(map).toString(), getLocationUrl);
        } else if (levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.TALUKA_LEVEL)) {
            requestCall.postDataApiCall(GET_TALUKA, new JSONObject(map).toString(), getLocationUrl);
        }
    }

    public void submitHospital(HospitalModel hospitalModel) {
        mContext.get().showProgressBar();
        Gson gson = new GsonBuilder().create();
        String params = gson.toJson(hospitalModel);
        final String addHospitalUrl = BuildConfig.BASE_URL + Urls.MissionRahat.CREATE_HOSPITAL;
        Log.d(CREATE_HOSPITAL, " url: " + addHospitalUrl);
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(CREATE_HOSPITAL, params, addHospitalUrl);
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
        if (mContext == null) {
            return;
        }
        mContext.get().hideProgressBar();
        try {
            if (response != null) {
                if (requestID.equalsIgnoreCase(GET_STATE) || requestID.equalsIgnoreCase(GET_DISTRICT)
                        || requestID.equalsIgnoreCase(GET_TALUKA)) {

                    JurisdictionLevelResponse jurisdictionLevelResponse
                            = new Gson().fromJson(response, JurisdictionLevelResponse.class);

                    if (jurisdictionLevelResponse != null && jurisdictionLevelResponse.getData() != null
                            && !jurisdictionLevelResponse.getData().isEmpty()
                            && jurisdictionLevelResponse.getData().size() > 0) {
                        if (requestID.equalsIgnoreCase(GET_STATE)) {
                            mContext.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                                    Constants.JurisdictionLevelName.STATE_LEVEL);
                        } else if (requestID.equalsIgnoreCase(GET_DISTRICT)) {
                            mContext.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                                    Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                        } else if (requestID.equalsIgnoreCase(GET_TALUKA)) {
                            mContext.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                                    Constants.JurisdictionLevelName.TALUKA_LEVEL);
                        }
                    }
                } else if (requestID.equalsIgnoreCase(CREATE_HOSPITAL)) {
                    CommonResponseStatusString commonResponse = new Gson().fromJson(response, CommonResponseStatusString.class);
                    if (commonResponse.getCode() == 200) {
                        mContext.get().onSuccessListener(CREATE_HOSPITAL, commonResponse.getMessage());
                        mContext.get().closeCurrentActivity();
                    } else {
                        mContext.get().onFailureListener(CREATE_HOSPITAL, commonResponse.getMessage());
                    }
                } else if (requestID.equalsIgnoreCase("GET_CREATE_MACHINE_MASTER_DATA")) {

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
        } catch (Exception e) {
            mContext.get().onFailureListener(requestID, e.getMessage());
        }
    }

    public void getMasterData() {
        mContext.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        final String Url = BuildConfig.BASE_URL + Urls.MissionRahat.GET_CREATE_MACHINE_MASTER_DATA;
        requestCall.getDataApiCall("GET_CREATE_MACHINE_MASTER_DATA", Url);
    }
}
