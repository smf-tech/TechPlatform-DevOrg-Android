package com.platform.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.BuildConfig;
import com.platform.R;
import com.platform.listeners.APIPresenterListener;
import com.platform.models.events.CommonResponse;
import com.platform.request.MatrimonyMeetRequestCall;
import com.platform.utility.PlatformGson;
import com.platform.utility.Urls;
import com.platform.view.fragments.MatrimonyMeetFragment;

import java.lang.ref.WeakReference;

public class MatrimonyMeetFragmentPresenter implements APIPresenterListener {

    private WeakReference<MatrimonyMeetFragment> fragmentWeakReference;
    public static final String MATRIMONY_MEET_ARCHIVE ="matrimonyMeetArchive";
    public static final String MEET_ALLOCATE_FINALIZE_BADGES ="meetAllocateFinalize";
    private final String TAG = MatrimonyMeetFragmentPresenter.class.getName();

    public MatrimonyMeetFragmentPresenter(MatrimonyMeetFragment mFragment){
        fragmentWeakReference = new WeakReference<>(mFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
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
                if(requestID.equalsIgnoreCase(MatrimonyMeetFragmentPresenter.MATRIMONY_MEET_ARCHIVE)){
                    try {
                        CommonResponse responseOBJ = new Gson().fromJson(response, CommonResponse.class);
                        fragmentWeakReference.get().showResponse(responseOBJ.getMessage());
                    } catch (Exception e) {
                        Log.e("TAG", "Exception");
                    }
                    //AllMatrimonyMeetsAPIResponse allMeets = PlatformGson.getPlatformGsonInstance().fromJson(response, AllMatrimonyMeetsAPIResponse.class);
                    //fragmentWeakReference.get().setMatrimonyMeets(allMeets.getData());
                }
                if(requestID.equalsIgnoreCase(MatrimonyMeetFragmentPresenter.MEET_ALLOCATE_FINALIZE_BADGES)){
                    try {
                        CommonResponse responseOBJ = new Gson().fromJson(response, CommonResponse.class);
                        fragmentWeakReference.get().showResponse(responseOBJ.getMessage());
                    } catch (Exception e) {
                        Log.e("TAG", "Exception");
                    }
                    //AllMatrimonyMeetsAPIResponse allMeets = PlatformGson.getPlatformGsonInstance().fromJson(response, AllMatrimonyMeetsAPIResponse.class);
                    //fragmentWeakReference.get().setMatrimonyMeets(allMeets.getData());
                }
            }
        } catch (Exception e) {
            fragmentWeakReference.get().onFailureListener(requestID,e.getMessage());
        }
    }

    public void meetArchiveDelete(String meetId, String type) {
        MatrimonyMeetRequestCall requestCall = new MatrimonyMeetRequestCall();
        requestCall.setApiPresenterListener(this);
        fragmentWeakReference.get().showProgressBar();
        final String meetArchiveDeleteUrl = BuildConfig.BASE_URL
                + String.format(Urls.Matrimony.MEET_ARCHIVE_DELETE, meetId, type);
        requestCall.getDataApiCall(MATRIMONY_MEET_ARCHIVE, meetArchiveDeleteUrl);
    }
    public void meetAllocateBadges(String meetId, String type) {
        MatrimonyMeetRequestCall requestCall = new MatrimonyMeetRequestCall();
        requestCall.setApiPresenterListener(this);
        fragmentWeakReference.get().showProgressBar();
        String meetAllocateFinalizeBadgesUrl = null;
        if(type.equals("finalizeBadges")){
            meetAllocateFinalizeBadgesUrl = BuildConfig.BASE_URL
                    + String.format(Urls.Matrimony.MEET_FINALISE_BADGES, meetId);
        }else if(type.equals("allocateBadges")){
            meetAllocateFinalizeBadgesUrl = BuildConfig.BASE_URL
                    + String.format(Urls.Matrimony.MEET_ALLOCATE_BADGES, meetId);
        }
        requestCall.getDataApiCall(MEET_ALLOCATE_FINALIZE_BADGES, meetAllocateFinalizeBadgesUrl);
    }
}
