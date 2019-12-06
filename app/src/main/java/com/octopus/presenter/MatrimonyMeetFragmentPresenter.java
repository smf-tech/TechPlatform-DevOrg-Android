package com.octopus.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopus.BuildConfig;
import com.octopus.listeners.APIPresenterListener;
import com.octopus.models.events.CommonResponse;
import com.octopus.request.APIRequestCall;
import com.octopus.utility.Urls;
import com.octopus.view.fragments.MatrimonyMeetFragment;

import java.lang.ref.WeakReference;

public class MatrimonyMeetFragmentPresenter implements APIPresenterListener {

    private WeakReference<MatrimonyMeetFragment> fragmentWeakReference;
    public static final String MATRIMONY_MEET_ARCHIVE ="matrimonyMeetArchive";
    public static final String MATRIMONY_MEET_DELETE ="matrimonyMeetDelete";
    public static final String MEET_ALLOCATE_BADGES ="meetAllocateBadges";
    public static final String MEET_FINALIZE_BADGES ="meetFinalizeBadges";
    public static final String MEET_ALLOCATE_FINALIZE_BADGES = "meetAllocateFinalize";
    public static final String SHOW_BATCHES_FOR_MEET = "SHOW_BATCHES_FOR_MEET";
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
                        fragmentWeakReference.get().showResponse(responseOBJ.getMessage(),
                                MatrimonyMeetFragmentPresenter.MATRIMONY_MEET_ARCHIVE, responseOBJ.getStatus());
                    } catch (Exception e) {
                        Log.e("TAG", "Exception");
                    }
                }
                if(requestID.equalsIgnoreCase(MatrimonyMeetFragmentPresenter.MATRIMONY_MEET_DELETE)){
                    try {
                        CommonResponse responseOBJ = new Gson().fromJson(response, CommonResponse.class);
                        fragmentWeakReference.get().showResponse(responseOBJ.getMessage(),
                                MatrimonyMeetFragmentPresenter.MATRIMONY_MEET_DELETE, responseOBJ.getStatus());
                    } catch (Exception e) {
                        Log.e("TAG", "Exception");
                    }
                }
                if(requestID.equalsIgnoreCase(MatrimonyMeetFragmentPresenter.MEET_ALLOCATE_BADGES)){
                    try {
                        CommonResponse responseOBJ = new Gson().fromJson(response, CommonResponse.class);
                        fragmentWeakReference.get().showResponse(responseOBJ.getMessage(),
                                MatrimonyMeetFragmentPresenter.MEET_ALLOCATE_BADGES, responseOBJ.getStatus());
                    } catch (Exception e) {
                        Log.e("TAG", "Exception");
                    }
                }
                if(requestID.equalsIgnoreCase(MatrimonyMeetFragmentPresenter.MEET_FINALIZE_BADGES)){
                    try {
                        CommonResponse responseOBJ = new Gson().fromJson(response, CommonResponse.class);
                        fragmentWeakReference.get().showResponse(responseOBJ.getMessage(),
                                MatrimonyMeetFragmentPresenter.MEET_FINALIZE_BADGES, responseOBJ.getStatus());
                    } catch (Exception e) {
                        Log.e("TAG", "Exception");
                    }
                }
                if (requestID.equalsIgnoreCase(MatrimonyMeetFragmentPresenter.SHOW_BATCHES_FOR_MEET))
                {
                    fragmentWeakReference.get().showBachesResponse(response);
                }

            }
        } catch (Exception e) {
            fragmentWeakReference.get().onFailureListener(requestID,e.getMessage());
        }
    }

    public void meetArchiveDelete(String meetId, String type) {
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        fragmentWeakReference.get().showProgressBar();
        final String meetArchiveDeleteUrl = BuildConfig.BASE_URL
                + String.format(Urls.Matrimony.MEET_ARCHIVE_DELETE, meetId, type);
        Log.d(TAG, "meetArchiveDeleteUrl: url" + meetArchiveDeleteUrl);
        fragmentWeakReference.get().showProgressBar();
        if(type.equals("Deleted")){
            requestCall.getDataApiCall(MATRIMONY_MEET_DELETE, meetArchiveDeleteUrl);
        }
        if(type.equals("Archive")){
            requestCall.getDataApiCall(MATRIMONY_MEET_ARCHIVE, meetArchiveDeleteUrl);
        }
    }
    public void meetAllocateBadges(String meetId, String type) {
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        fragmentWeakReference.get().showProgressBar();
        String meetAllocateFinalizeBadgesUrl = null;
        if(type.equals("finalizeBadges")){
            meetAllocateFinalizeBadgesUrl = BuildConfig.BASE_URL
                    + String.format(Urls.Matrimony.MEET_ALLOCATE_BADGES, meetId,"finalizeBadge");
            Log.d(TAG, "meetAllocateFinalizeBadgesUrl: url" + meetAllocateFinalizeBadgesUrl);
            fragmentWeakReference.get().showProgressBar();
            requestCall.getDataApiCall(MEET_FINALIZE_BADGES, meetAllocateFinalizeBadgesUrl);
        }else if(type.equals("allocateBadges")){
            meetAllocateFinalizeBadgesUrl = BuildConfig.BASE_URL
                    + String.format(Urls.Matrimony.MEET_ALLOCATE_BADGES, meetId,"allocateBadge");
            Log.d(TAG, "meetAllocateFinalizeBadgesUrl: url" + meetAllocateFinalizeBadgesUrl);
            fragmentWeakReference.get().showProgressBar();
            requestCall.getDataApiCall(MEET_ALLOCATE_BADGES, meetAllocateFinalizeBadgesUrl);
        }
    }

    public void showMeetBaches(String meetId, String type) {
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        fragmentWeakReference.get().showProgressBar();
        String meetAllocateFinalizeBadgesUrl = null;
        //if (type.equals("allocateBadges"))
        {
            meetAllocateFinalizeBadgesUrl = BuildConfig.BASE_URL
                    + String.format(Urls.Matrimony.SHOW_MEET_BACHES, meetId);
        }
        requestCall.getDataApiCall(SHOW_BATCHES_FOR_MEET, meetAllocateFinalizeBadgesUrl);
    }
}
