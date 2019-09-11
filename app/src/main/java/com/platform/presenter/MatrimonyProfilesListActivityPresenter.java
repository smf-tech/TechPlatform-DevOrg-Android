package com.platform.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.platform.listeners.TMFilterListRequestCallListener;
import com.platform.models.Matrimony.MatrimonyUserProfileRequestModel;
import com.platform.models.tm.PendingRequest;
import com.platform.models.tm.SubFilterset;
import com.platform.models.tm.TMFilterlistRequestsResponse;
import com.platform.request.MatrimonyProfileListRequestCall;
import com.platform.request.TMFiltersListRequestCall;
import com.platform.utility.Util;
import com.platform.view.activities.MatrimonyProfileListActivity;
import com.platform.view.activities.TMFiltersListActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

@SuppressWarnings("CanBeFinal")
public class MatrimonyProfilesListActivityPresenter implements TMFilterListRequestCallListener {

    private final String TAG = this.getClass().getName();
    private WeakReference<MatrimonyProfileListActivity> fragmentWeakReference;

    public MatrimonyProfilesListActivityPresenter(MatrimonyProfileListActivity tmFiltersListActivity) {
        fragmentWeakReference = new WeakReference<>(tmFiltersListActivity);
    }

    public void getAllFiltersRequests(String meetId) {
        MatrimonyProfileListRequestCall requestCall = new MatrimonyProfileListRequestCall();
        requestCall.setListener(this);

        //fragmentWeakReference.get().showProgressBar();
        requestCall.getAllPendingRequests(meetId);
    }

    public void approveRejectRequest(JSONObject requestObject, int position,String requestType) {
        MatrimonyProfileListRequestCall requestCall = new MatrimonyProfileListRequestCall();
        requestCall.setListener(this);

        //fragmentWeakReference.get().showProgressBar();
        requestCall.approveRejectRequest(requestObject, position,requestType);
    }

    @Override
    public void onFilterListRequestsFetched(String response) {
        //fragmentWeakReference.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            MatrimonyUserProfileRequestModel pendingRequestsResponse
                    = new Gson().fromJson(response, MatrimonyUserProfileRequestModel.class);
            if (pendingRequestsResponse != null && pendingRequestsResponse.getUserProfileList() != null
                    && !pendingRequestsResponse.getUserProfileList().isEmpty()
                    && pendingRequestsResponse.getUserProfileList().size() > 0) {
                fragmentWeakReference.get().showPendingApprovalRequests(pendingRequestsResponse.getUserProfileList());
            }
        }
    }


    @Override
    public void onRequestStatusChanged(String response, PendingRequest pendingRequest) {
        //fragmentWeakReference.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            //  fragmentWeakReference.get().updateRequestStatus(response, pendingRequest);
        }
    }

    @Override
    public void onRequestStatusChanged(String response, int position) {
        if (!TextUtils.isEmpty(response)) {
            fragmentWeakReference.get().updateRequestStatus(response, position);
        }
    }

    @Override
    public void onFailureListener(String message) {
        //fragmentWeakReference.get().hideProgressBar();
        if (!TextUtils.isEmpty(message)) {
            Log.e(TAG, "onFailureListener :" + message);
        }
    }

    @Override
    public void onErrorListener(VolleyError volleyError) {

        if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
            String message = error.getMessage();
            Log.i(TAG, "Error: " + message);
        }

        //fragmentWeakReference.get().hideProgressBar();
    }


    //---------Approve Reject Request-----------

    public JSONObject createBodyParams(String meetid,String type,String userid,String approval_type){
        JSONObject requestObject = new JSONObject();
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson("");
        Log.d("JsonObjRequestfilter", "SubmitRequest: " + json);

        try {
            requestObject.put("meet_id", "5d6f90c25dda765c2f0b5dd4");
            requestObject.put("type", type);
            requestObject.put("approval", approval_type);
            requestObject.put("user_id", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            return requestObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
