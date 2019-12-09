package com.octopusbjsindia.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.listeners.ProfileDetailRequestCallListener;
import com.octopusbjsindia.request.MatrimonyProfileDetailRequestCall;
import com.octopusbjsindia.view.activities.MatrimonyProfileDetailsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

@SuppressWarnings("CanBeFinal")
public class MatrimonyProfilesDetailsActivityPresenter implements ProfileDetailRequestCallListener {

    private final String TAG = this.getClass().getName();
    private WeakReference<MatrimonyProfileDetailsActivity> fragmentWeakReference;

    public MatrimonyProfilesDetailsActivityPresenter(MatrimonyProfileDetailsActivity tmFiltersListActivity) {
        fragmentWeakReference = new WeakReference<>(tmFiltersListActivity);
    }

  /*  public void getAllFiltersRequests() {
        MatrimonyProfileListRequestCall requestCall = new MatrimonyProfileListRequestCall();
        requestCall.setListener(this);

        //fragmentWeakReference.get().showProgressBar();
        requestCall.getAllPendingRequests();
    }*/

    public void markAttendanceRequest(JSONObject requestObject, int position, String requestType) {
        MatrimonyProfileDetailRequestCall requestCall = new MatrimonyProfileDetailRequestCall();
        requestCall.setListener(this);

        //fragmentWeakReference.get().showProgressBar();
        requestCall.approveRejectRequest(requestObject, position, requestType);
    }

   /* @Override
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
    }*/

    @Override
    public void onRequestStatusChanged(String response, int position, String requestType) {
        fragmentWeakReference.get().updateRequestStatus(response, position,requestType);
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

    public JSONObject createBodyParams(String meetid, String userid, String approval_type) {
        JSONObject requestObject = new JSONObject();
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson("");
        Log.d("JsonObjRequestfilter", "SubmitRequest: " + json);

        try {
            requestObject.put("meet_id", meetid);
            requestObject.put("type", approval_type);
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
