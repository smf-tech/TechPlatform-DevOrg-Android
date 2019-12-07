package com.octopusbjsindia.listeners;

public interface MonthlyAttendaceListener  {
    void onTeamAttendance(String response);
    void onSuccess(String response);
    void onError(String error);
    void OnErrorResponse(String errorRes);
}
