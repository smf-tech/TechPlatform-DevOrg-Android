package com.platform.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.BuildConfig;
import com.platform.listeners.APIPresenterListener;
import com.platform.models.events.CommonResponse;
import com.platform.request.APIRequestCall;
import com.platform.utility.Urls;
import com.platform.view.fragments.MachineShiftingFormFragment;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class MachineShiftingFormFragmentPresenter implements APIPresenterListener {
    private WeakReference<MachineShiftingFormFragment> fragmentWeakReference;
    private final String TAG = MachineShiftingFormFragmentPresenter.class.getName();
    public static final String SUBMIT_MACHINE_SHIFTING_FORM ="submitMachineShiftingForm";
    private static final String CURRENT_STRUCTURE_ID = "current_structure_id";
    private static final String KEY_MACHINE_ID = "machine_id";
    private static final String NEW_STRUCTURE_ID = "new_structure_id";
    private static final String TRAVEL_DISTANCE = "travel_distance";
    private static final String DIESEL_QUANTITY = "diesel_filled_quantity";
    private static final String TRAVEL_TIME = "travel_time";
    private static final String METER_REDAING = "meter_reading";
    private static final String PROVIDED_BY = "diesel_provided_by";
    private static final String IS_DIESEL_FILLED = "is_diesel_filled";

    public MachineShiftingFormFragmentPresenter(MachineShiftingFormFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }

    public void shiftMachine(String isDieselFilled, String providedBy, String dieselQuantity,
                             String startMeterReading, String travelDistance, String travelTime,
                             String machineId, String currentStructureId, String newStructureId){
        HashMap<String,String> map=new HashMap<>();
        map.put(KEY_MACHINE_ID, machineId);
        map.put(CURRENT_STRUCTURE_ID, currentStructureId);
        map.put(NEW_STRUCTURE_ID, newStructureId);
        map.put(IS_DIESEL_FILLED, isDieselFilled);
        map.put(PROVIDED_BY, providedBy);
        map.put(DIESEL_QUANTITY, dieselQuantity);
        map.put(METER_REDAING, startMeterReading);
        map.put(TRAVEL_DISTANCE, travelDistance);
        map.put(TRAVEL_TIME, travelTime);

        final String machineShiftUrl = BuildConfig.BASE_URL
                + String.format(Urls.SSModule.SHIFT_MACHINE);
        Log.d(TAG, "machineShiftUrl: url" + machineShiftUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(SUBMIT_MACHINE_SHIFTING_FORM, new JSONObject(map).toString(), machineShiftUrl);
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
                if (requestID.equalsIgnoreCase(MachineShiftingFormFragmentPresenter.SUBMIT_MACHINE_SHIFTING_FORM)) {
                    CommonResponse responseOBJ = new Gson().fromJson(response, CommonResponse.class);
                    fragmentWeakReference.get().showResponse(responseOBJ.getMessage(),
                            MachineShiftingFormFragmentPresenter.SUBMIT_MACHINE_SHIFTING_FORM, responseOBJ.getStatus());
                }
            }
        }catch (Exception e) {
            fragmentWeakReference.get().onFailureListener(requestID, e.getMessage());
        }
    }
}
