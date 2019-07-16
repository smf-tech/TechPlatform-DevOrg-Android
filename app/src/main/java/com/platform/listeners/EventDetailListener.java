package com.platform.listeners;

import com.android.volley.VolleyError;

public interface EventDetailListener {
    void onAttendanceCodeFetched(String response);

    void onAttendanceCodeSubmitted(String response);

    void onParticipantsListFetched(String response);

    void onTaskMarkComplete(String response);

    void onDeleted(String response);

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);
}
