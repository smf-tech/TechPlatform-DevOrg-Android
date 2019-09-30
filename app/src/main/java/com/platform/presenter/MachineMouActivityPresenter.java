package com.platform.presenter;

import com.android.volley.VolleyError;
import com.platform.listeners.APIPresenterListener;

public class MachineMouActivityPresenter implements APIPresenterListener {
    @Override
    public void onFailureListener(String requestID, String message) {

    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {

    }
}
