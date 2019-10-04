package com.platform.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.platform.BuildConfig;
import com.platform.listeners.APIPresenterListener;
import com.platform.request.APIRequestCall;
import com.platform.utility.Urls;
import com.platform.view.fragments.MachineDeployStructureListFragment;

import java.lang.ref.WeakReference;

public class MachineDeployStructureListFragmentPresenter implements APIPresenterListener {
    private WeakReference<MachineDeployStructureListFragment> fragmentWeakReference;
    private final String TAG = StructureMachineListFragmentPresenter.class.getName();
    public static final String GET_MACHINE_DEPLOY_STRUCTURE_LIST ="getMachineDeployStructuresList";

    public MachineDeployStructureListFragmentPresenter(MachineDeployStructureListFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }

    public void getDeployableStructuresList(){
        Gson gson = new GsonBuilder().create();
        fragmentWeakReference.get().showProgressBar();
        //String paramjson = gson.toJson(getDistrictMachineDataJson(stateId,districtId));
        final String getMachinesListUrl = BuildConfig.BASE_URL
                + String.format(Urls.SSModule.GET_SS_MACHINE_LIST);
        Log.d(TAG, "getDistrictMachineListUrl: url" + getMachinesListUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        //requestCall.postDataApiCall(GET_MACHINE_DEPLOY_STRUCTURE_LIST, paramjson, getMachinesListUrl);
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
                if (requestID.equalsIgnoreCase(MachineDeployStructureListFragmentPresenter.GET_MACHINE_DEPLOY_STRUCTURE_LIST)) {

                }
            }
        } catch (Exception e) {
            fragmentWeakReference.get().onFailureListener(requestID, e.getMessage());
        }
    }
}
