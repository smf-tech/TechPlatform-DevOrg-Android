package com.octopusbjsindia.request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.PlannerFragmetListener;
import com.octopusbjsindia.presenter.PlannerFragmentPresenter;
import com.octopusbjsindia.utility.GsonRequestFactory;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;

import org.json.JSONObject;

import static androidx.constraintlayout.motion.widget.MotionScene.TAG;

public class PlannerDashboardCall {
    private Gson gson;
    PlannerFragmetListener plannerFragmetListener;

    public void setPlannerFragmentListener(PlannerFragmentPresenter listener) {
        plannerFragmetListener=listener;
    }

    public void getPlannerData() {
        Response.Listener<JSONObject> orgSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "GET_PLANNER_DASHBOARD - Resp: " + res);
                    plannerFragmetListener.onPlannerDashboardDataFetched(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                plannerFragmetListener.onFailureListener(Platform.getInstance().getString(R.string.msg_failure));
            }
        };

        Response.ErrorListener orgErrorListener = error -> plannerFragmetListener.onErrorListener(error);

        final String getOrgUrl = BuildConfig.BASE_URL + String.format(Urls.PlannerDashboard.GET_PLANNER_DASHBOARD);

        Log.d(TAG, "GET_PLANNER_DASHBOARD: " + getOrgUrl);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getOrgUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                orgSuccessListener,
                orgErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }
}
