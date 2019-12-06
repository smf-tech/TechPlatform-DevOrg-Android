package com.octopus.listeners;

import com.android.volley.VolleyError;

public interface CreateEventListener {

    void onEventsFetchedOfDay(String response);

    void onEventsFetchedOfMonth(String response);

    void onFormsFetched(String response);

    void onTaskMembersFetched(String response);

    void onEventSubmitted(String response);

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);
}
