package com.platform.listeners;


import com.android.volley.VolleyError;

public interface LeavePresenterListener {

    void onFailureListener(String requestID,String message);

    void onErrorListener(String requestID,VolleyError error);

    void onSuccessListener(String requestID,String response);

}