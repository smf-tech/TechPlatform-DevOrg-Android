package com.platform.listeners;

import com.android.volley.NetworkError;

public interface SubmitAttendanceListener {
    void onSuccess(int id,String response);
    void onError(int id,String error);
}
