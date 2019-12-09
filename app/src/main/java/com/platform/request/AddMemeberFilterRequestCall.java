package com.platform.request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.BuildConfig;
import com.platform.Platform;
import com.platform.R;
import com.platform.listeners.ProfileRequestCallListener;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Urls;
import com.platform.utility.Util;

import org.json.JSONObject;

public class AddMemeberFilterRequestCall {

    private Gson gson;
    private ProfileRequestCallListener listener;
    private final String TAG = ProfileRequestCall.class.getName();

    public void setListener(ProfileRequestCallListener listener) {
        this.listener = listener;
    }

    public void getOrganizationRoles(String orgId) {
        Response.Listener<JSONObject> orgRolesSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "getOrganizationRoles - Resp: " + res);
                    listener.onOrganizationRolesFetched(orgId, res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(Platform.getInstance().getString(R.string.msg_failure));
            }
        };

        Response.ErrorListener orgRolesErrorListener = error -> listener.onErrorListener(error);

        final String getOrgProjectUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.GET_ORGANIZATION_ROLES, orgId);
//        final String getOrgProjectUrl = BuildConfig.BASE_URL
//                + Urls.Events.GET_ORGANIZATION_ROLES;

        Log.d(TAG, "OrganizationRoles API: " + getOrgProjectUrl);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getOrgProjectUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                orgRolesSuccessListener,
                orgRolesErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }


}
