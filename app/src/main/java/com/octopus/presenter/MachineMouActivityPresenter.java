package com.octopus.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.octopus.BuildConfig;
import com.octopus.listeners.APIPresenterListener;
import com.octopus.models.SujalamSuphalam.MachineDetailsAPIResponse;
import com.octopus.request.APIRequestCall;
import com.octopus.utility.PlatformGson;
import com.octopus.utility.Urls;
import com.octopus.view.activities.MachineMouActivity;

import java.lang.ref.WeakReference;

public class MachineMouActivityPresenter implements APIPresenterListener {
    private WeakReference<MachineMouActivity> fragmentWeakReference;
    private final String TAG = MachineMouActivity.class.getName();
    public static final String GET_MACHINE_DETAILS ="getMachineDetails";

    public MachineMouActivityPresenter(MachineMouActivity tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }

    public void getMachineDetails(String machineId, Integer statusCode){
        fragmentWeakReference.get().showProgressBar();
        final String getMachineDetailsUrl = BuildConfig.BASE_URL
                + String.format(Urls.SSModule.GET_MACHINE_DETAILS, machineId, statusCode);
        Log.d(TAG, "getMatrimonyMeetTypesUrl: url" + getMachineDetailsUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall(GET_MACHINE_DETAILS, getMachineDetailsUrl);
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
                if(requestID.equalsIgnoreCase(MachineMouActivityPresenter.GET_MACHINE_DETAILS)){
                    MachineDetailsAPIResponse machineDetailsResponse = PlatformGson.getPlatformGsonInstance().fromJson(response,
                            MachineDetailsAPIResponse.class);
                    fragmentWeakReference.get().setMachineDetailData(machineDetailsResponse.getData());
                }
            }
        }catch (Exception e){
                fragmentWeakReference.get().onFailureListener(requestID,e.getMessage());
            }
    }
}
