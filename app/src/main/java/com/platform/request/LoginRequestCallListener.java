package com.platform.request;

import com.android.volley.VolleyError;
import com.platform.models.login.Login;

public interface LoginRequestCallListener {

    void onSuccessListener(Login login);

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);
}
