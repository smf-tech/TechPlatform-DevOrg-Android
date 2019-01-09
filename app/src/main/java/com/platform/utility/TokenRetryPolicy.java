package com.platform.utility;

import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.platform.Platform;
import com.platform.request.RefreshToken;

import java.net.HttpURLConnection;

@SuppressWarnings("CanBeFinal")
public class TokenRetryPolicy implements RetryPolicy {

    private GsonRequestFactory gsonFactory;
    private int timeout;
    private int maxNumRetries;
    private int currentRetryCount;

    TokenRetryPolicy(GsonRequestFactory gsonRequestFactory) {
        this.gsonFactory = gsonRequestFactory;
        this.timeout = gsonFactory.currentTimeout;
        this.maxNumRetries = gsonFactory.maxNumRetries;
    }

    @SuppressWarnings("unchecked")
    public void onRefreshTokenUpdate(String token) {
        try {
            if (token.equalsIgnoreCase(Constants.SUCCESS)) {
                if (gsonFactory != null && gsonFactory.getHeaders() != null) {
                    gsonFactory.getHeaders().put(Constants.Login.AUTHORIZATION, "Bearer " + token);
                    Platform.getInstance().getVolleyRequestQueue().add(gsonFactory);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
