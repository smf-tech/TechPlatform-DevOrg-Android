package com.octopusbjsindia.presenter;


import android.util.Log;

import com.airbnb.lottie.utils.Utils;
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

import java.util.HashMap;
import java.util.Map;

public class StoriesFragmentPresenter implements APIPresenterListener {

    private static final String GET_FEED = "getFeed";
    private static final String FEED_DELETE = "feedDelete";
    StoriesFragment mContax;

    public StoriesFragmentPresenter(StoriesFragment fragment) {
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
        if(requestID.equalsIgnoreCase(GET_FEED)){
            FeedListResponse responseData = gson.fromJson(response, FeedListResponse.class);
            if (responseData.getStatus() == 1000) {
                Util.logOutUser(mContax.getActivity());
                mContax.onFailureListener(requestID, responseData.getMessage());
                mContax.getActivity().finish();
            } else if (responseData.getStatus() == 200){
                mContax.setAdapter(responseData);
            } else {
                mContax.onFailureListener(requestID, responseData.getMessage());
            }
        }  else if(requestID.equalsIgnoreCase(FEED_DELETE)){
            CommonResponse responseData = gson.fromJson(response, CommonResponse.class);
            if (responseData.getStatus() == 1000) {
                mContax.onFailureListener(requestID, responseData.getMessage());
                mContax.getActivity().finish();
                Util.logOutUser(mContax.getActivity());
            } else if (responseData.getStatus() == 200){
                mContax.onFailureListener(requestID, responseData.getMessage());
                mContax.feedDeleted();
            } else {
                mContax.onFailureListener(requestID, responseData.getMessage());
            }
        }
    }

    public void getFeedLest() {
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        mContax.showProgressBar();
        final String meetArchiveDeleteUrl = BuildConfig.BASE_URL + String.format(Urls.Feeds.FEED_LIST);
        requestCall.getDataApiCall(GET_FEED, meetArchiveDeleteUrl);
    }

    public void deleteFeed(String feedId) {
        APIRequestCall requestCall = new APIRequestCall();

        Gson gson = new GsonBuilder().create();
        Map<String,String> map = new HashMap<>();
        map.put("feed_id",feedId);
        String params = gson.toJson(map);

        requestCall.setApiPresenterListener(this);
        mContax.showProgressBar();
        final String url = BuildConfig.BASE_URL + String.format(Urls.Feeds.DELETE_FEED);
        requestCall.postDataApiCall(FEED_DELETE, params, url);
    }
}
