package com.octopusbjsindia.presenter;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.SujalamSuphalam.OpratorListResponse;
import com.octopusbjsindia.models.stories.CommentResponse;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.PlatformGson;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.fragments.OperatorListFragment;

import java.util.HashMap;

public class OperatorListFragmentPresenter implements APIPresenterListener {

    OperatorListFragment mContext;

    public OperatorListFragmentPresenter(OperatorListFragment mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        if (mContext != null && mContext != null) {
            mContext.hideProgressBar();
            mContext.onFailureListener(requestID, message);
        }
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (mContext != null && mContext != null) {
            mContext.hideProgressBar();
            if (error != null) {
                mContext.onErrorListener(requestID, error);
            }
        }
    }


    @Override
    public void onSuccessListener(String requestID, String response) {
        mContext.hideProgressBar();
        if (requestID.equalsIgnoreCase("Operators")) {
            OpratorListResponse responseData = PlatformGson.getPlatformGsonInstance().fromJson(response, OpratorListResponse.class);
            if (responseData.getStatus() == 1000) {
                Util.logOutUser(mContext.getActivity());
                return;
            } else if (responseData.getCode() == 200) {
                mContext.populateOpratorList(responseData.getData());
            } else {
                mContext.onFailureListener(requestID, responseData.getMessage());
            }
        } else if (requestID.equalsIgnoreCase("assignOperators")) {
            CommentResponse responseData = PlatformGson.getPlatformGsonInstance().fromJson(response, CommentResponse.class);
            if (responseData.getStatus() == 1000) {
                Util.logOutUser(mContext.getActivity());
                return;
            } else {
                mContext.assignOperatorsSuccess(responseData.getMessage());
            }
        }

    }

    public void getOperators() {
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        mContext.showProgressBar();
        final String url = BuildConfig.BASE_URL + Urls.SSModule.OPERATORS_LIST;
        mContext.showProgressBar();
        requestCall.getDataApiCall("Operators", url);
    }

    public void assignOperators(String opretorId, String machineId) {


        HashMap<String, String> map = new HashMap<>();
        map.put("machine_id", machineId);
        map.put("operator_id", opretorId);
        String paramjson = new Gson().toJson(map);

        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        mContext.showProgressBar();
        final String url = BuildConfig.BASE_URL + Urls.SSModule.ASSIGN_OPERATORS;
        mContext.showProgressBar();
        requestCall.postDataApiCall("assignOperators", paramjson, url);
    }

}
