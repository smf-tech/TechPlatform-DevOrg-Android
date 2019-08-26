package com.platform.listeners;

import com.android.volley.NetworkError;
import com.platform.models.attendance.AttendaceData;

public interface SubmitAttendanceListener {
    void onSuccess(int id, String response, AttendaceData attendaceData);
    void onError(int id,String error);
}
