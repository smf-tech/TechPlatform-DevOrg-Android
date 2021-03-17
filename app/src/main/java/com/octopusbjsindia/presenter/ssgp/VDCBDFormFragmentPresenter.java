package com.octopusbjsindia.presenter.ssgp;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.models.profile.JurisdictionLevelResponse;
import com.octopusbjsindia.models.ssgp.VdcBdRequestModel;
import com.octopusbjsindia.models.ssgp.VdcCmRequestModel;
import com.octopusbjsindia.presenter.MachineMouFragmentPresenter;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.view.fragments.ssgp.VDCBDFormFragment;
import com.octopusbjsindia.view.fragments.ssgp.VDFFormFragment;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class VDCBDFormFragmentPresenter implements APIPresenterListener {

    private WeakReference<VDCBDFormFragment> fragmentWeakReference;
    private final String TAG = VDCBDFormFragmentPresenter.class.getName();
    private static final String KEY_SELECTED_ID = "selected_location_id";
    private static final String KEY_JURIDICTION_TYPE_ID = "jurisdictionTypeId";
    private static final String KEY_LEVEL = "jurisdictionLevel";
    public static final String GET_TALUKAS = "getTalukas";
    public static final String GET_DISTRICTS = "getDistricts";
    public static final String GET_VILLAGES = "getVillages";
    public static final String BENEFICIARY_DETAIL_REPORT = "beneficiarydetailreport";
    public static final String GET_GP_STRUCURE_LIST = "GETGPMACHINELIST";


    public VDCBDFormFragmentPresenter(VDCBDFormFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }

    public void getLocationData(String selectedLocationId, String jurisdictionTypeId, String levelName) {
        HashMap<String, String> map = new HashMap<>();
        map.put(KEY_SELECTED_ID, selectedLocationId);
        map.put(KEY_JURIDICTION_TYPE_ID, jurisdictionTypeId);
        map.put(KEY_LEVEL, levelName);

        fragmentWeakReference.get().showProgressBar();
        final String getLocationUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.GET_LOCATION_DATA);
        Log.d(TAG, "getLocationUrl: url" + getLocationUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        if (levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.TALUKA_LEVEL)) {
            requestCall.postDataApiCall(GET_TALUKAS, new JSONObject(map).toString(), getLocationUrl);
        } else if (levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.DISTRICT_LEVEL)) {
            requestCall.postDataApiCall(GET_DISTRICTS, new JSONObject(map).toString(), getLocationUrl);
        } else if (levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.VILLAGE_LEVEL)) {
            requestCall.postDataApiCall(GET_VILLAGES, new JSONObject(map).toString(), getLocationUrl);
        }
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            fragmentWeakReference.get().onFailureListener(requestID, message);
        }
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            if (error != null) {
                fragmentWeakReference.get().onErrorListener(requestID, error);
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

                if (requestID.equalsIgnoreCase(VDCBDFormFragmentPresenter.GET_GP_STRUCURE_LIST)) {
                    fragmentWeakReference.get().setStructurelist(response);
                } else if (requestID.equalsIgnoreCase(VDCBDFormFragmentPresenter.BENEFICIARY_DETAIL_REPORT)) {
                    CommonResponse responseOBJ = new Gson().fromJson(response, CommonResponse.class);
                    fragmentWeakReference.get().showResponse(responseOBJ.getMessage(),
                            VDCBDFormFragmentPresenter.BENEFICIARY_DETAIL_REPORT, responseOBJ.getStatus());
                } else if (requestID.equalsIgnoreCase(MachineMouFragmentPresenter.GET_TALUKAS) ||
                        requestID.equalsIgnoreCase(MachineMouFragmentPresenter.GET_DISTRICTS) || requestID.equalsIgnoreCase(GET_VILLAGES)) {
                    JurisdictionLevelResponse jurisdictionLevelResponse
                            = new Gson().fromJson(response, JurisdictionLevelResponse.class);
                    if (jurisdictionLevelResponse != null && jurisdictionLevelResponse.getData() != null
                            && !jurisdictionLevelResponse.getData().isEmpty()
                            && jurisdictionLevelResponse.getData().size() > 0) {
                        if (requestID.equalsIgnoreCase(GET_TALUKAS)) {
                            fragmentWeakReference.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                                    Constants.JurisdictionLevelName.TALUKA_LEVEL);
                        } else if (requestID.equalsIgnoreCase(GET_DISTRICTS)) {
                            fragmentWeakReference.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                                    Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                        } else if (requestID.equalsIgnoreCase(GET_VILLAGES)) {
                            fragmentWeakReference.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                                    Constants.JurisdictionLevelName.VILLAGE_LEVEL);
                        }
                    }
                }
            }
        } catch (Exception e) {
            fragmentWeakReference.get().onFailureListener(requestID, e.getMessage());
        }
    }
    public void submitBdData(VdcBdRequestModel vdcBdRequestModel) {
        Gson gson = new GsonBuilder().create();
        fragmentWeakReference.get().showProgressBar();
        String paramjson = gson.toJson(vdcBdRequestModel);
        final String url = BuildConfig.BASE_URL + Urls.SSGP.BENEFICIARY_DETAILS_REPORT;
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(BENEFICIARY_DETAIL_REPORT, paramjson, url);
    }

    public void GetGpStrucureList(){
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        String getSSMasterDaraUrl = BuildConfig.BASE_URL
                + String.format(Urls.SSGP.GET_GP_STRUCURE_LIST);
        Log.d(TAG, "getSSMasterDaraUrl: url" + getSSMasterDaraUrl);
        requestCall.getDataApiCall(GET_GP_STRUCURE_LIST, getSSMasterDaraUrl);
    }
}
