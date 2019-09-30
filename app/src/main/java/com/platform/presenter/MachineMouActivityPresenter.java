package com.platform.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.platform.BuildConfig;
import com.platform.listeners.APIPresenterListener;
import com.platform.models.Matrimony.MeetTypesAPIResponse;
import com.platform.models.SujalamSuphalam.MachineDetailData;
import com.platform.request.APIRequestCall;
import com.platform.utility.PlatformGson;
import com.platform.utility.Urls;
import com.platform.view.activities.MachineMouActivity;

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

//    public void getMachineDetails(){
//        final String getMatrimonyMeetTypesUrl = BuildConfig.BASE_URL
//                + String.format(Urls.Matrimony.MATRIMONY_MEET_TYPES);
//        Log.d(TAG, "getMatrimonyMeetTypesUrl: url" + getMatrimonyMeetTypesUrl);
//        fragmentWeakReference.get().showProgressBar();
//        APIRequestCall requestCall = new APIRequestCall();
//        requestCall.setApiPresenterListener(this);
//        requestCall.getDataApiCall(GET_MACHINE_DETAILS, getMatrimonyMeetTypesUrl);
//    }

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
                if(requestID.equalsIgnoreCase(CreateMeetFirstFragmentPresenter.GET_MATRIMONY_MEET_TYPES)){
                    //MeetTypesAPIResponse machineDetails = PlatformGson.getPlatformGsonInstance().fromJson(response, MachineDetailData.class);
                    //fragmentWeakReference.get().setMachineDetails(machineDetails);
                }
            }
        }catch (Exception e){
                fragmentWeakReference.get().onFailureListener(requestID,e.getMessage());
            }
    }
}
