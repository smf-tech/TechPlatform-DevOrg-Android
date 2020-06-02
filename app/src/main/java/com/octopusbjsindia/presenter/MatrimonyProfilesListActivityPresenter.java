package com.octopusbjsindia.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.listeners.TMFilterListRequestCallListener;
import com.octopusbjsindia.models.Matrimony.AllUserResponse;
import com.octopusbjsindia.models.Matrimony.MatrimonyUserProfileRequestModel;
import com.octopusbjsindia.models.Matrimony.NewRegisteredUserResponse;
import com.octopusbjsindia.models.stories.FeedListResponse;
import com.octopusbjsindia.models.tm.PendingRequest;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.request.MatrimonyProfileListRequestCall;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.MatrimonyProfileListActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("CanBeFinal")
public class MatrimonyProfilesListActivityPresenter implements TMFilterListRequestCallListener, APIPresenterListener {

    private final String TAG = this.getClass().getName();
    private final String GET_ALL_USER = "GetAllUserList";
    private WeakReference<MatrimonyProfileListActivity> mContax;

    public MatrimonyProfilesListActivityPresenter(MatrimonyProfileListActivity tmFiltersListActivity) {
        mContax = new WeakReference<>(tmFiltersListActivity);
    }

    public void getAllFiltersRequests(String meetId) {
        MatrimonyProfileListRequestCall requestCall = new MatrimonyProfileListRequestCall();
        requestCall.setListener(this);

        //mContax.get().showProgressBar();
        requestCall.getAllPendingRequests(meetId);
    }

    public void approveRejectRequest(JSONObject requestObject, int position,String requestType) {
        MatrimonyProfileListRequestCall requestCall = new MatrimonyProfileListRequestCall();
        requestCall.setListener(this);

        //mContax.get().showProgressBar();
        requestCall.approveRejectRequest(requestObject, position,requestType);
    }

    @Override
    public void onFilterListRequestsFetched(String response) {
        //mContax.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);

            String status = jsonObject.getString("status");
            String msg = jsonObject.getString("message");
            if (Integer.parseInt(status) == 200) {
                MatrimonyUserProfileRequestModel pendingRequestsResponse
                        = new Gson().fromJson(response, MatrimonyUserProfileRequestModel.class);
                if (pendingRequestsResponse != null && pendingRequestsResponse.getUserProfileList() != null
                        && !pendingRequestsResponse.getUserProfileList().isEmpty()
                        && pendingRequestsResponse.getUserProfileList().size() > 0) {
                    mContax.get().showUserProfileList(pendingRequestsResponse.getUserProfileList());
                }
            }else {

            }
            } catch (JSONException e) {
                e.printStackTrace();
                mContax.get().setTxt_no_data();
            }
        }
    }


    @Override
    public void onRequestStatusChanged(String response, PendingRequest pendingRequest) {
        //mContax.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            //  mContax.get().updateRequestStatus(response, pendingRequest);
        }
    }

    @Override
    public void onRequestStatusChanged(String response, int position) {
        if (!TextUtils.isEmpty(response)) {
            mContax.get().updateRequestStatus(response, position);
        }
    }

    @Override
    public void onFailureListener(String message) {
        //mContax.get().hideProgressBar();
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

        //mContax.get().hideProgressBar();
    }


    //---------Approve Reject Request-----------

    public JSONObject createBodyParams(String meetid,String type,String userid,String approval_type,String strReason){
        JSONObject requestObject = new JSONObject();
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson("");
        Log.d("JsonObjRequestfilter", "SubmitRequest: " + json);

        try {
            requestObject.put("meet_id", meetid);
            requestObject.put("type", type);
            requestObject.put("approval", approval_type);
            requestObject.put("user_id", userid);
            requestObject.put("rejection_reason",strReason);
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

    // as per the new implimentetion
    public void getAllUserList(String url,String params) {
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        mContax.get().showProgressBar();

        requestCall.postDataApiCall(GET_ALL_USER, params,url);
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        mContax.get().onFailureListener(requestID, message);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        mContax.get().onFailureListener(requestID, error.getMessage());
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        mContax.get().hideProgressBar();
        Gson gson = new Gson();
        if(requestID.equalsIgnoreCase(GET_ALL_USER)){
            AllUserResponse responseData = gson.fromJson(response, AllUserResponse.class);
            if (responseData.getStatus() == 1000) {
                Util.logOutUser(mContax.get());
                mContax.get().finish();
            } else if (responseData.getStatus() == 200){
//                NewRegisteredUserResponse newUserResponse = new Gson().fromJson(responseData, NewRegisteredUserResponse.class);
                mContax.get().onNewProfileFetched(requestID, responseData.getData());
            } else {
                mContax.get().onFailureListener(requestID, responseData.getMessage());
            }
        }
    }
}
