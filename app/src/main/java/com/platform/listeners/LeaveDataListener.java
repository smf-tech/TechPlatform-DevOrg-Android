package com.platform.listeners;


import com.android.volley.VolleyError;

public interface LeaveDataListener {

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);

    void onSuccessListener(String response);

    void showProgressBar();

    void hideProgressBar();

    void closeCurrentActivity();

}