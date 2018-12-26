package com.platform.listeners;

import com.android.volley.VolleyError;
import com.platform.models.login.Login;

public interface PlatformRequestCallListener {

    void onSuccessListener(Login login);

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);
}
