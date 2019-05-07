package com.platform.listeners;


import com.android.volley.VolleyError;

public interface LeaveDataListener {

    void onFailureListener(String requestID,String message);

    void onErrorListener(String requestID,VolleyError error);

    void onSuccessListener(String requestID,String response);

    void showProgressBar();

    void hideProgressBar();

    void closeCurrentActivity();

}