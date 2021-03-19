package com.octopusbjsindia.presenter.ssgp;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.listeners.ImageRequestCallListener;
import com.octopusbjsindia.models.events.CommonResponseStatusString;
import com.octopusbjsindia.models.profile.JurisdictionLevelResponse;
import com.octopusbjsindia.models.ssgp.VDFFRequest;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.request.ImageRequestCall;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.view.fragments.ssgp.VDFFormFragment;

import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class VDFFormFragmentPresenter implements APIPresenterListener, ImageRequestCallListener {

    private WeakReference<VDFFormFragment> fragmentWeakReference;
    private final String TAG = VDFFormFragmentPresenter.class.getName();
    private static final String KEY_SELECTED_ID = "selected_location_id";
    private static final String KEY_JURIDICTION_TYPE_ID = "jurisdictionTypeId";
    private static final String KEY_LEVEL = "jurisdictionLevel";
    public static final String GET_TALUKAS = "getTalukas";
    public static final String GET_DISTRICTS = "getDistricts";
    public static final String GET_VILLAGES = "getVillages";
    public static final String SUBMIT_VDF_FORM = "submitVDFForm";

    public VDFFormFragmentPresenter(VDFFormFragment tmFragment) {
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
                if (requestID.equalsIgnoreCase(VDFFormFragmentPresenter.SUBMIT_VDF_FORM)) {
                    CommonResponseStatusString responseOBJ = new Gson().fromJson(response, CommonResponseStatusString.class);
                    if(responseOBJ.getCode()==200){
                        fragmentWeakReference.get().onSuccessListener("StructureMaster",responseOBJ.getMessage());
                    } else {
                        fragmentWeakReference.get().onFailureListener("StructureMaster",responseOBJ.getMessage());
                    }
                } else if (requestID.equalsIgnoreCase(GET_TALUKAS) ||
                        requestID.equalsIgnoreCase(GET_DISTRICTS)  ||
                        requestID.equalsIgnoreCase(GET_VILLAGES)) {
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

    public void submitVDFF(VDFFRequest request) {
        Gson gson = new GsonBuilder().create();
        fragmentWeakReference.get().showProgressBar();
        String paramjson = gson.toJson(request);
        final String url = BuildConfig.BASE_URL + Urls.SSGP.SUBMIT_VILLAGE_DEMND;
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(SUBMIT_VDF_FORM, paramjson, url);
    }

    public void uploadImage(File file, String type, final String formName) {
        ImageRequestCall requestCall = new ImageRequestCall();
        requestCall.setListener(this);
        fragmentWeakReference.get().showProgressBar();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... voids) {
                requestCall.uploadImageUsingHttpURLEncoded(file, type, formName, null, null);
                return null;
            }
        }.execute();

    }
    @Override
    public void onImageUploadedListener(String response, String formName) {
        Log.e(TAG, "onImageUploadedListener:\n" + response);

        fragmentWeakReference.get().hideProgressBar();

        try {
            if (new JSONObject(response).has("data")) {
                JSONObject data = new JSONObject(response).getJSONObject("data");
                String url = (String) data.get("url");
                Log.e(TAG, "onPostExecute: Url: " + url);
                Map<String, String> mUploadedImageUrlList = new HashMap<>();
                mUploadedImageUrlList.put(formName, url);

                fragmentWeakReference.get().onImageUploaded(formName,url);
            } else {
                Log.e(TAG, "onPostExecute: Invalid response");
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onFailureListener(String error) {

    }
}
