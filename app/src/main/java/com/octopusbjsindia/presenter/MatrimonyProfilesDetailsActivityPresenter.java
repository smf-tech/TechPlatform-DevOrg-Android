package com.octopusbjsindia.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.listeners.ProfileDetailRequestCallListener;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.request.MatrimonyProfileDetailRequestCall;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.view.activities.MatrimonyProfileDetailsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

@SuppressWarnings("CanBeFinal")
public class MatrimonyProfilesDetailsActivityPresenter implements ProfileDetailRequestCallListener,
        APIPresenterListener {

    private final String TAG = this.getClass().getName();
    private WeakReference<MatrimonyProfileDetailsActivity> fragmentWeakReference;

    public MatrimonyProfilesDetailsActivityPresenter(MatrimonyProfileDetailsActivity tmFiltersListActivity) {
        fragmentWeakReference = new WeakReference<>(tmFiltersListActivity);
    }

    public void markAttendanceRequest(JSONObject requestObject, int position, String requestType) {
        MatrimonyProfileDetailRequestCall requestCall = new MatrimonyProfileDetailRequestCall();
        requestCall.setListener(this);

        requestCall.approveRejectRequest(requestObject, position, requestType);
    }

    @Override
    public void onRequestStatusChanged(String response, int position, String requestType) {
        fragmentWeakReference.get().updateRequestStatus(response, position, requestType);
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

    public void userAction(String paramjson) {
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        String url = BuildConfig.BASE_URL + Urls.Matrimony.BLOCK_UNBLOCK_USER;
        requestCall.postDataApiCall("BLOCK_UNBLOCK_USER", paramjson, url);
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        fragmentWeakReference.get().hideProgressBar();
        fragmentWeakReference.get().onFailureListener(requestID, message);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        fragmentWeakReference.get().hideProgressBar();
        fragmentWeakReference.get().onFailureListener(requestID, error.getMessage());
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        fragmentWeakReference.get().hideProgressBar();
        if (requestID.equals("BLOCK_UNBLOCK_USER")) {
            CommonResponse commonResponse = new Gson().fromJson(response, CommonResponse.class);
            if (commonResponse.getStatus() == 200) {
                fragmentWeakReference.get().onFailureListener(requestID, commonResponse.getMessage());
            }
        }
    }
}
