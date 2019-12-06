package com.octopus.utility;

import android.util.Log;

import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopus.Platform;
import com.octopus.models.login.Login;
import com.octopus.request.RefreshToken;

import java.net.HttpURLConnection;

@SuppressWarnings("CanBeFinal")
public class TokenRetryPolicy implements RetryPolicy {

    private GsonRequestFactory gsonFactory;
    private int timeout;
    private int maxNumRetries;
    private int currentRetryCount;
    private final String TAG = TokenRetryPolicy.class.getSimpleName();

    TokenRetryPolicy(GsonRequestFactory gsonRequestFactory) {
        this.gsonFactory = gsonRequestFactory;
        this.timeout = gsonFactory.currentTimeout;
        this.maxNumRetries = gsonFactory.maxNumRetries;
    }

    @SuppressWarnings("unchecked")
    public void onRefreshTokenUpdate(String status, String response) {
        try {
            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                if (gsonFactory != null && gsonFactory.getHeaders() != null) {
                    Login login = new Gson().fromJson(response, Login.class);
                    if (login.getStatus().equalsIgnoreCase(Constants.SUCCESS)) {
                        Util.saveLoginObjectInPref(response);
                        gsonFactory.getHeaders().put(Constants.Login.AUTHORIZATION,
                                "Bearer " + login.getLoginData().getAccessToken());
                        Platform.getInstance().getVolleyRequestQueue().add(gsonFactory);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public int getCurrentTimeout() {
        return gsonFactory.currentTimeout;
    }

    @Override
    public int getCurrentRetryCount() {
        if (this.timeout == 0) {
            return 0;
        }

        return this.currentRetryCount;
    }

    @Override
    public void retry(VolleyError error) throws VolleyError {
        currentRetryCount++;

        if (error != null && error.networkResponse != null &&
                error.networkResponse.statusCode == HttpURLConnection.HTTP_UNAUTHORIZED &&
                currentRetryCount <= maxNumRetries) {

            currentRetryCount = maxNumRetries + 1;
            new RefreshToken().refreshAccessToken(this);
        }

        if (this.currentRetryCount >= this.maxNumRetries) {
            if (error != null) {
                throw error;
            } else {
                throw new VolleyError();
            }
        }
    }
}
