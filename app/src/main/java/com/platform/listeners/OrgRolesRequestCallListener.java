package com.platform.listeners;

import com.android.volley.VolleyError;

public interface OrgRolesRequestCallListener {

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);

    void onSuccessListener(String response);
}
