package com.platform.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.listeners.PlatformRequestCallListener;
import com.platform.models.home.Home;
import com.platform.request.HomeRequestCall;
import com.platform.view.activities.HomeActivity;

import java.lang.ref.WeakReference;

public class HomeActivityPresenter implements PlatformRequestCallListener {

    @SuppressWarnings("CanBeFinal")
    private final String TAG = ProfileActivityPresenter.class.getName();
    @SuppressWarnings("CanBeFinal")
    private WeakReference<HomeActivity> homeActivity;

    public HomeActivityPresenter(HomeActivity activity) {
        homeActivity = new WeakReference<>(activity);
    }

    public void getModules() {
        HomeRequestCall requestCall = new HomeRequestCall();
        requestCall.setListener(this);

        homeActivity.get().showProgressBar();
        requestCall.getHomeModules();
    }

    @Override
    public void onSuccessListener(String response) {
        Log.i(TAG, "Success: " + response);
        Home models = new Gson().fromJson(response, Home.class);
        homeActivity.get().showNextScreen(models);
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
