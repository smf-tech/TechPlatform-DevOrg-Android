package com.octopusbjsindia.presenter;


import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.models.stories.FeedListResponse;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.fragments.StoriesFragment;
import com.octopusbjsindia.view.fragments.formComponents.TextFragment;

import java.util.HashMap;
import java.util.Map;

public class TextFragmentPresenter implements APIPresenterListener {

    private static final String GET_MV_USER_INFO = "getMvUserInfo";

    TextFragment mContax;

    public TextFragmentPresenter(TextFragment fragment) {
        this.mContax = fragment;
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        mContax.hideProgressBar();
        mContax.onFailureListener(requestID, message);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        mContax.hideProgressBar();
        mContax.onFailureListener(requestID, error.getMessage());
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        mContax.hideProgressBar();
        Gson gson = new Gson();
         if(requestID.equalsIgnoreCase(GET_MV_USER_INFO)){
            CommonResponse responseData = gson.fromJson(response, CommonResponse.class);
            if (responseData.getStatus() == 1000) {
                mContax.onFailureListener(requestID, responseData.getMessage());
                mContax.getActivity().finish();
                Util.logOutUser(mContax.getActivity());
            } else if (responseData.getStatus() == 200){
                mContax.onSuccessListener(requestID,response);
            } else {
                mContax.onFailureListener(requestID, responseData.getMessage());
            }
        }
    }

    public void GET_MV_USER_INFO(String phone) {
        APIRequestCall requestCall = new APIRequestCall();

        Gson gson = new GsonBuilder().create();
        Map<String,String> map = new HashMap<>();
        map.put("phone",phone);
        String params = gson.toJson(map);

        requestCall.setApiPresenterListener(this);
        mContax.showProgressBar();
        final String url = BuildConfig.BASE_URL + String.format(Urls.PM.GET_MV_USER_INFO);
        requestCall.postDataApiCall(GET_MV_USER_INFO, params, url);
    }
}
