package com.platform.request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.platform.Platform;
import com.platform.listeners.OrgRolesRequestCallListener;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Urls;
import com.platform.utility.Util;
import com.platform.view.activities.HomeActivity;

import org.json.JSONObject;

import static com.platform.utility.Constants.SyncAdapter.COMPLETE;
import static com.platform.utility.Constants.SyncAdapter.ERROR;

public class OrgRolesRequestCall {

    private OrgRolesRequestCallListener listener;
    private final String TAG = OrgRolesRequestCallListener.class.getName();

    public void setListener(OrgRolesRequestCallListener listener) {
        this.listener = listener;
    }

    public void getRolesDetails() {
        Response.Listener<JSONObject> rolesResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.i(TAG, "getRolesDetails - Resp: " + res);
                    listener.onSuccessListener(res);
                }
            } catch (Exception e) {
                e.printStackTrace();
                listener.onFailureListener("");
            }
        };

        Response.ErrorListener rolesErrorListener = error -> listener.onErrorListener(error);

        String orgID = "5c1b940ad503a31f360e1252"; // FIXME: 25-01-2019 remove this hardcoded value
        Gson gson = new GsonBuilder().serializeNulls().create();
        final String getRolesUrl = Urls.BASE_URL + String.format(Urls.Roles.GET_ROLES, orgID);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getRolesUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                rolesResponseListener,
                rolesErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setBodyParams(new JsonObject());
        gsonRequest.setShouldCache(false);

        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    /**
     * This method will be called by SyncAdapter Framework
     * get roles by organization ID
     */
    public void syncRoles() {
        String TAG = "SyncAdapter";
        Response.Listener<JSONObject> rolesResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.i(TAG, "Roles synced\n" + res);
                    if (HomeActivity.sHandler != null) {
                        HomeActivity.sHandler.obtainMessage(COMPLETE, res).sendToTarget();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "syncRoles#Error:\n", e);
                if (HomeActivity.sHandler != null) {
                    HomeActivity.sHandler.obtainMessage(ERROR, e).sendToTarget();
                }
            }
        };

        Response.ErrorListener rolesErrorListener = error -> {
            if (HomeActivity.sHandler != null) {
                HomeActivity.sHandler.obtainMessage(ERROR, error + "").sendToTarget();
            }
            Log.i(TAG, "Roles#Error: \n" + error.getMessage());
        };

        String orgID = "5c1b940ad503a31f360e1252"; // FIXME: 25-01-2019 remove this hardcoded value
        Gson gson = new GsonBuilder().serializeNulls().create();
        final String getRolesUrl = Urls.BASE_URL + String.format(Urls.Roles.GET_ROLES, orgID);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getRolesUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                rolesResponseListener,
                rolesErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setBodyParams(new JsonObject());
        gsonRequest.setShouldCache(false);

        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

}
