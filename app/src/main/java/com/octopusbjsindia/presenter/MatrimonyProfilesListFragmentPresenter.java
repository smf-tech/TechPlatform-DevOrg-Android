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
import com.octopusbjsindia.models.Matrimony.MatrimonyUserFilterData;
import com.octopusbjsindia.models.Matrimony.MatrimonyUserProfileRequestModel;
import com.octopusbjsindia.models.tm.PendingRequest;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.request.MatrimonyProfileListRequestCall;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.fragments.MatrimonyProfileListFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

@SuppressWarnings("CanBeFinal")
public class MatrimonyProfilesListFragmentPresenter implements TMFilterListRequestCallListener, APIPresenterListener {

    private final String TAG = this.getClass().getName();
    private final String GET_ALL_USER = "GetAllUserList";
    private WeakReference<MatrimonyProfileListFragment> mContext;

    public MatrimonyProfilesListFragmentPresenter(MatrimonyProfileListFragment tmFragment) {
        mContext = new WeakReference<>(tmFragment);
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
                    //mContext.get().showUserProfileList(pendingRequestsResponse.getUserProfileList());
                }
            }
            } catch (JSONException e) {
                e.printStackTrace();
                //mContext.get().setTxt_no_data();
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
//        if (!TextUtils.isEmpty(response)) {
//            mContext.get().updateRequestStatus(response, position);
//        }
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

    @Override
    public void onFailureListener(String requestID, String message) {
        mContext.get().onFailureListener(requestID, message);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        mContext.get().onFailureListener(requestID, error.getMessage());
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        mContext.get().hideProgressBar();
        Gson gson = new Gson();
        if(requestID.equalsIgnoreCase(GET_ALL_USER)){
            AllUserResponse responseData = gson.fromJson(response, AllUserResponse.class);
            if (responseData.getStatus() == 1000) {
                Util.logOutUser(mContext.get().getActivity());
                mContext.get().getActivity().finish();
            } else if (responseData.getStatus() == 200){
                mContext.get().showUserProfileList(responseData.getData());
            } else if (responseData.getStatus() == 300) {
                mContext.get().dispayNoData(responseData.getMessage());
            }
        }
    }

    // as per the new implimentetion
    public void getAllUserList(MatrimonyUserFilterData matrimonyUserFilterData, String url) {
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        mContext.get().showProgressBar();
        Gson gson = new Gson();
        String paramJson = gson.toJson(matrimonyUserFilterData);
        requestCall.postDataApiCall(GET_ALL_USER, paramJson, url);
    }

}
