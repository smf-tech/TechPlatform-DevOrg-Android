package com.octopusbjsindia.presenter;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.profile.MultyProjectResponse;
import com.octopusbjsindia.models.profile.OrganizationResponse;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.view.activities.ProfileActivity;

public class ProfileActivityPresenter implements APIPresenterListener {

    ProfileActivity activity;

    public ProfileActivityPresenter(ProfileActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        activity.hideProgressBar();
        activity.onFailureListener(requestID,message);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        activity.hideProgressBar();
        activity.onErrorListener(requestID,error);;
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        MultyProjectResponse multyProjectResponse = new Gson().fromJson(response, MultyProjectResponse.class);
        activity.displayProjects(multyProjectResponse.getData());;
    }

    public void getMultProfile(){
        activity.showProgressBar();
        final String checkProfileUrl = BuildConfig.BASE_URL + Urls.Profile.GET_MULTIPAL_PROFILE;
        activity.showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall("multi_profile",checkProfileUrl);
    }

}
