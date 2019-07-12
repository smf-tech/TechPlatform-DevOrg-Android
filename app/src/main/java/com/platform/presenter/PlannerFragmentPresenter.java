package com.platform.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.platform.listeners.PlannerFragmetListener;
import com.platform.models.events.EventsResponse;
import com.platform.models.events.Participant;
import com.platform.models.planner.PlannerSummerResopnse;
import com.platform.models.planner.SubmoduleData;
import com.platform.request.PlannerDashboardCall;
import com.platform.utility.PlatformGson;
import com.platform.view.fragments.PlannerFragment;

import java.util.ArrayList;

public class PlannerFragmentPresenter implements PlannerFragmetListener {

    PlannerFragment plannerFragment;

    public PlannerFragmentPresenter(PlannerFragment plannerFragment) {
        this.plannerFragment=plannerFragment;
    }

    public void getPlannerData() {
        PlannerDashboardCall requestCall = new PlannerDashboardCall();
        requestCall.setPlannerFragmentListener(this);
        plannerFragment.showProgressBar();
        requestCall.getPlannerData();
    }

    @Override
    public void onPlannerDashboardDataFetched(String response) {
        plannerFragment.hideProgressBar();
        if (!TextUtils.isEmpty(response)) {

            PlannerSummerResopnse data = PlatformGson.getPlatformGsonInstance().fromJson(response,PlannerSummerResopnse.class);

            if (data != null && data.getData() != null
                    && !data.getData().isEmpty()
                    && data.getData().size() > 0) {
                plannerFragment.showPlannerSummer((ArrayList<SubmoduleData>) data.getData());
            }
        }
        Log.i("Planner","111"+response);
    }

    @Override
    public void onFailureListener(String message) {
        if (plannerFragment != null) {
            plannerFragment.hideProgressBar();
            plannerFragment.showErrorMessage(message);
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {

    }


}
