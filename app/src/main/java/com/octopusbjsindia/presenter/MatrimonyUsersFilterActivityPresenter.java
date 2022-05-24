package com.octopusbjsindia.presenter;

import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.Matrimony.MatrimonyMasterRequestModel;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.view.activities.MatrimonyUsersFilterFragment;

import java.lang.ref.WeakReference;

public class MatrimonyUsersFilterActivityPresenter implements APIPresenterListener {

    private WeakReference<MatrimonyUsersFilterFragment> mContext;
    private final String GET_FILTER_MASTER_DATA = "getFilterMasterData";

    public MatrimonyUsersFilterActivityPresenter(MatrimonyUsersFilterFragment tmActivity) {
        this.mContext = new WeakReference<>(tmActivity);
    }

    public void clearData() {
        mContext = null;
    }

    public void getFilterMasterData(String url) {
        mContext.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall(GET_FILTER_MASTER_DATA, url);
    }

    @Override
    public void onFailureListener(String requestID, String message) {

    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        mContext.get().hideProgressBar();
        Gson gson = new Gson();
        if (!TextUtils.isEmpty(response)) {
            MatrimonyMasterRequestModel matrimonyMasterRequestModel
                    = new Gson().fromJson(response, MatrimonyMasterRequestModel.class);
            if (matrimonyMasterRequestModel != null && matrimonyMasterRequestModel.getData() != null
                    && !matrimonyMasterRequestModel.getData().isEmpty()
                    && matrimonyMasterRequestModel.getData().size() > 0) {
                if (matrimonyMasterRequestModel.getData().get(0).getMaster_data() != null &&
                        matrimonyMasterRequestModel.getData().get(0).getMaster_data().size() > 0) {
                    mContext.get().setMasterData(matrimonyMasterRequestModel.getData().get(0).getMaster_data());
                }
            }
        }
    }
}
