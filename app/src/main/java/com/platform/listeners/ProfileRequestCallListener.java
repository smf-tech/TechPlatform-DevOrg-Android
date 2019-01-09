package com.platform.listeners;

import com.android.volley.VolleyError;

public interface ProfileRequestCallListener {

    void onProfileUpdated(String response);

    void onOrganizationsFetched(String response);

    void onStatesFetched(String response);

    void onJurisdictionFetched(String response);

    void onOrganizationProjectsFetched(String response);

    void onOrganizationRolesFetched(String response);

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);

}
