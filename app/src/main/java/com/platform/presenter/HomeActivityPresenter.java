package com.platform.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.listeners.UserRequestCallListener;
import com.platform.models.home.Home;
import com.platform.models.user.User;
import com.platform.models.user.UserInfo;
import com.platform.request.HomeRequestCall;
import com.platform.request.LoginRequestCall;
import com.platform.utility.Util;
import com.platform.view.fragments.HomeFragment;

import java.lang.ref.WeakReference;

@SuppressWarnings("CanBeFinal")
public class HomeActivityPresenter implements UserRequestCallListener {

    private final String TAG = ProfileActivityPresenter.class.getName();
    private WeakReference<HomeFragment> homeFragment;

    public HomeActivityPresenter(HomeFragment activity) {
        homeFragment = new WeakReference<>(activity);
    }

    public void getUserProfile() {
        LoginRequestCall loginRequestCall = new LoginRequestCall();
        loginRequestCall.setListener(this);
        loginRequestCall.getUserProfile();
    }

    public void getModules(UserInfo user) {
        HomeRequestCall requestCall = new HomeRequestCall();
        requestCall.setListener(this);

        homeFragment.get().showProgressBar();
        requestCall.getHomeModules(user);
    }

    @Override
    public void onSuccessListener(String response) {
        Log.i(TAG, "Success: " + response);
        Home models = new Gson().fromJson(response, Home.class);
        homeFragment.get().showNextScreen(models);
    }

    @Override
    public void onUserProfileSuccessListener(String response) {
        User user = new Gson().fromJson(response, User.class);
        if (response != null && user.getUserInfo() != null) {
            Util.saveUserObjectInPref(new Gson().toJson(user.getUserInfo()));
        }

        getModules(user.getUserInfo());
    }

    @Override
    public void onFailureListener(String message) {
        Log.i(TAG, "Fail: " + message);
    }

    @Override
    public void onErrorListener(VolleyError error) {
        Log.i(TAG, "Error: " + error);
    }
}
