package com.octopusbjsindia.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.SujalamSuphalam.Structure;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.models.profile.JurisdictionLevelResponse;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.view.activities.CreateStructureActivity;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class CreateStructureActivityPresenter implements APIPresenterListener {

    final String STRUCTURE_MASTER = "structureMasterData";
    final String CREATE_STRUCTURE = "createStructure";
    final String GET_DISTRICT = "getDistrict";
    final String GET_TALUKA = "getTaluka";
    final String GET_VILLAGE = "getvillage";

    private static final String KEY_SELECTED_ID = "selected_location_id";
    private static final String KEY_JURIDICTION_TYPE_ID = "jurisdictionTypeId";
    private static final String KEY_LEVEL = "jurisdictionLevel";
    private final String TAG = CreateStructureActivityPresenter.class.getName();

    private WeakReference<CreateStructureActivity> mContext;

    public CreateStructureActivityPresenter(CreateStructureActivity mContext) {
        this.mContext = new WeakReference<>(mContext);
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
                if (requestID.equalsIgnoreCase(CREATE_STRUCTURE)) {
                    CommonResponse commonResponse = new Gson().fromJson(response, CommonResponse.class);
                    if(commonResponse.getStatus()==200){
                        mContext.get().onFailureListener(CREATE_STRUCTURE,commonResponse.getMessage());
                        mContext.get().closeCurrentActivity();
                    } else {
                        mContext.get().onFailureListener(CREATE_STRUCTURE,commonResponse.getMessage());
                    }

                } else if (requestID.equalsIgnoreCase(GET_DISTRICT) ||
                        requestID.equalsIgnoreCase(GET_TALUKA) ||
                        requestID.equalsIgnoreCase(GET_VILLAGE)) {
                    JurisdictionLevelResponse jurisdictionLevelResponse
                            = new Gson().fromJson(response, JurisdictionLevelResponse.class);

                    if (jurisdictionLevelResponse != null && jurisdictionLevelResponse.getData() != null
                            && !jurisdictionLevelResponse.getData().isEmpty()
                            && jurisdictionLevelResponse.getData().size() > 0) {
                        if (requestID.equalsIgnoreCase(GET_DISTRICT)) {
                            mContext.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                                    Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                        } else if (requestID.equalsIgnoreCase(GET_TALUKA)) {
                            mContext.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                                    Constants.JurisdictionLevelName.TALUKA_LEVEL);
                        } else if (requestID.equalsIgnoreCase(GET_VILLAGE)) {
                            mContext.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                                    Constants.JurisdictionLevelName.VILLAGE_LEVEL);
                        }
                    }
                }
            }
        } catch (Exception e) {
            mContext.get().onFailureListener(requestID, e.getMessage());
        }
    }

//    public void getJurisdictionLevelData(String orgId, String jurisdictionTypeId, String levelName) {
//        APIRequestCall requestCall = new APIRequestCall();
//        requestCall.setApiPresenterListener(this);
//
//        final String getLocationUrl = BuildConfig.BASE_URL
//                + String.format(Urls.Profile.GET_JURISDICTION_LEVEL_DATA, orgId, jurisdictionTypeId, levelName);
//        Log.d("getLocationUrl", " url: " + getLocationUrl);
//        mContext.get().showProgressBar();
//
//        if (levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.DISTRICT_LEVEL)) {
//            requestCall.getDataApiCall(GET_DISTRICT, getLocationUrl);
//        } else if (levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.TALUKA_LEVEL)) {
//            requestCall.getDataApiCall(GET_TALUKA, getLocationUrl);
//        } else if (levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.VILLAGE_LEVEL)) {
//            requestCall.getDataApiCall(GET_VILLAGE, getLocationUrl);
//        }
//    }

    public void getLocationData(String selectedLocationId, String jurisdictionTypeId, String levelName) {
        HashMap<String,String> map=new HashMap<>();
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
        if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.DISTRICT_LEVEL)) {
            requestCall.postDataApiCall(GET_DISTRICT, new JSONObject(map).toString(), getLocationUrl);
        }else if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.TALUKA_LEVEL)){
            requestCall.postDataApiCall(GET_TALUKA, new JSONObject(map).toString(), getLocationUrl);
        }else if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.VILLAGE_LEVEL)){
            requestCall.postDataApiCall(GET_VILLAGE, new JSONObject(map).toString(), getLocationUrl);
        }
    }

    public void submitStructure(Structure structureData) {
        mContext.get().showProgressBar();
        Gson gson = new GsonBuilder().create();
        String params = gson.toJson(structureData);
        final String getMatrimonyMeetTypesUrl = BuildConfig.BASE_URL + Urls.SSModule.CREATE_STRUCTURE;
        Log.d(CREATE_STRUCTURE, " url: " + getMatrimonyMeetTypesUrl);
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(CREATE_STRUCTURE, params, getMatrimonyMeetTypesUrl);
    }
}
