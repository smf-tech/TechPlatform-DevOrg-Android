package com.octopus.listeners;

import com.android.volley.VolleyError;

public interface AddMemberRequestCallListener extends ProfileRequestCallListener{

    void onMembersFetched(String response);

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);
}
