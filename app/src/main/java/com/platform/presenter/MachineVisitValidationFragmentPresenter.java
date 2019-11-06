package com.platform.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.platform.BuildConfig;
import com.platform.listeners.APIPresenterListener;
import com.platform.models.SujalamSuphalam.MachineWorkingHoursRecord;
import com.platform.models.SujalamSuphalam.StructurePripretionRequest;
import com.platform.request.APIRequestCall;
import com.platform.utility.Constants;
import com.platform.utility.Urls;
import com.platform.utility.VolleyMultipartRequest;
import com.platform.view.fragments.MachineVisitValidationFragment;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MachineVisitValidationFragmentPresenter implements APIPresenterListener {
    private WeakReference<MachineVisitValidationFragment> fragmentWeakReference;
    private final String TAG = MachineVisitValidationFragmentPresenter.class.getName();
    public static final String GET_WORKING_HOURS_RECORD ="getWorkingHoursRecord";
    public static final String SUBMIT_MACHINE_VISIT_VALIDATION_FORM ="submitMachineShiftingForm";

    public MachineVisitValidationFragmentPresenter(MachineVisitValidationFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }

    public void getWorkingHourDetails(long selectedDate, String machineId){
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        fragmentWeakReference.get().showProgressBar();
        final String getWorkingHoursRecordUrl = BuildConfig.BASE_URL
                + String.format(Urls.SSModule.GET_MACHINE_WORKING_HOURS_RECORD, selectedDate, machineId);
        Log.d(TAG, "getWorkingHoursRecordUrl: url " + getWorkingHoursRecordUrl);
        fragmentWeakReference.get().showProgressBar();
            requestCall.getDataApiCall(GET_WORKING_HOURS_RECORD, getWorkingHoursRecordUrl);
    }

    public void submitWorkingHours(){
//        APIRequestCall requestCall = new APIRequestCall();
//        requestCall.setApiPresenterListener(this);
//        Gson gson = new GsonBuilder().create();
//        String paramjson =gson.toJson(getWorkingHoursJson(meetId,userId,mobilenumber));
//        fragmentWeakReference.get().showProgressBar();
//        final String submitMachineVisitUrl = BuildConfig.BASE_URL
//                + String.format(Urls.SSModule.SUBMIT_MACHINE_VISIT);
//        Log.d(TAG, "submitMachineVisitRecordUrl: url " + submitMachineVisitUrl);
//        fragmentWeakReference.get().showProgressBar();
//        requestCall.postDataApiCall(GET_WORKING_HOURS_RECORD, paramjson, submitMachineVisitUrl);
    }

    public void submitVisitMonitoring(ArrayList<MachineWorkingHoursRecord> requestData, HashMap<String, Bitmap> imageHashmap){
        Gson gson = new GsonBuilder().create();

        String paramjson =gson.toJson(requestData);

        final String checkProfileUrl = BuildConfig.BASE_URL
                + String.format(Urls.SSModule.SUBMIT_MACHINE_VISIT);//, mobilenumber,meetId);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
//        drawable = new BitmapDrawable(activity.getResources(), imageHashmap.get(key));
        requestCall.multipartApiCall("MACHINE_VISIT",paramjson,imageHashmap,
                fragmentWeakReference.get().getResources(),checkProfileUrl);
    }

    public JsonObject getWorkingHoursJson(String meetId, String userId, String mobilenumber){

        HashMap<String,String> map=new HashMap<>();
        map.put("meet_id", meetId);
        map.put("user_id", userId);
        map.put("mobile", mobilenumber);

        JsonObject requestObject = new JsonObject();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            requestObject.addProperty(key, value);
        }
        return requestObject;
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            fragmentWeakReference.get().onFailureListener(requestID,message);
        }
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            if (error != null) {
                fragmentWeakReference.get().onErrorListener(requestID,error);
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
                if (requestID.equalsIgnoreCase(MachineVisitValidationFragmentPresenter.SUBMIT_MACHINE_VISIT_VALIDATION_FORM)) {
//                    CommonResponse responseOBJ = new Gson().fromJson(response, CommonResponse.class);
//                    fragmentWeakReference.get().showResponse(responseOBJ.getMessage(),
//                            MachineVisitValidationFragmentPresenter.SUBMIT_MACHINE_VISIT_VALIDATION_FORM, responseOBJ.getStatus());
                } else if(requestID.equalsIgnoreCase(MachineVisitValidationFragmentPresenter.GET_WORKING_HOURS_RECORD)) {
                    fragmentWeakReference.get().setData();
                }
            }
        }catch (Exception e) {
            fragmentWeakReference.get().onFailureListener(requestID, e.getMessage());
        }
    }
}
