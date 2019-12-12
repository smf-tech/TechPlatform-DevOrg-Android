package com.octopusbjsindia.presenter;

import android.content.Context;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.content.Url;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.models.stories.CommentResponse;
import com.octopusbjsindia.models.stories.FeedListResponse;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.CommentActivity;

import org.json.JSONObject;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class CommentActivityPresenter implements APIPresenterListener {

    private final String COMMENT = "Comment";
    CommentActivity mContax;

    public CommentActivityPresenter(CommentActivity commentActivity) {
        mContax = commentActivity;
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
        if(requestID.equalsIgnoreCase(COMMENT)){
            CommentResponse responseData = gson.fromJson(response, CommentResponse.class);
            if (responseData.getStatus() == 1000) {
                Util.logOutUser(mContax);
                mContax.onFailureListener(requestID, responseData.getMessage());
                mContax.finish();
            } else if (responseData.getStatus() == 200){
                mContax.onCommentFetched(responseData.getData());
            } else {
                mContax.onFailureListener(requestID, responseData.getMessage());
            }
        }
    }

    public void getCommentList(String feedId) {
        mContax.showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();

        Gson gson = new GsonBuilder().create();
        Map<String,String> map = new HashMap<>();
        map.put("feed_id",feedId);
        String params = gson.toJson(map);

        requestCall.setApiPresenterListener(this);
        mContax.showProgressBar();
        final String url = BuildConfig.BASE_URL + String.format(Urls.Feeds.COMMENT_LIST);
        requestCall.postDataApiCall(COMMENT, params, url);


    }
}
