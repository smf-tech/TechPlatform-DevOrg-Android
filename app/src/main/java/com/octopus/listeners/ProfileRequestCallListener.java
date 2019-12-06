package com.octopus.listeners;

import com.android.volley.VolleyError;

public interface ProfileRequestCallListener {

    void onProfileUpdated(String response);

    void onOrganizationsFetched(String response);

    void onJurisdictionFetched(String response, String level);

    void onOrganizationProjectsFetched(String orgId, String response);

    void onOrganizationRolesFetched(String orgId, String response);

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);

}
