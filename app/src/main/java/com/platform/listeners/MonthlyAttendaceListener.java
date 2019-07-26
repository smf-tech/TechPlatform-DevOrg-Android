package com.platform.listeners;

public interface MonthlyAttendaceListener  {
    void onSuccess(String response);
    void onError(String error);
    void OnErrorResponse(String errorRes);
}
