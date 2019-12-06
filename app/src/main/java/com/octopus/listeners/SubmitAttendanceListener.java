package com.octopus.listeners;

import com.octopus.models.attendance.AttendaceData;

public interface SubmitAttendanceListener {
    void onSuccess(int id, String response, AttendaceData attendaceData);
    void onError(int id,String error);
}
