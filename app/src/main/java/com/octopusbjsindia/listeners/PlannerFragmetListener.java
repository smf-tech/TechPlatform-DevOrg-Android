package com.octopusbjsindia.listeners;

import com.android.volley.VolleyError;

public interface PlannerFragmetListener {

    void onPlannerDashboardDataFetched(String response);

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);
}
