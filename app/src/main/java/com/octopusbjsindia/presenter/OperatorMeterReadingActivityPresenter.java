package com.octopusbjsindia.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.listeners.TMFilterListRequestCallListener;
import com.octopusbjsindia.models.Operator.OperatorMachineData;
import com.octopusbjsindia.models.Operator.OperatorMachineDataResponseModel;
import com.octopusbjsindia.models.tm.PendingRequest;
import com.octopusbjsindia.models.tm.SubFilterset;
import com.octopusbjsindia.request.OperatorMeterReadingRequestCall;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.OperatorMeterReadingActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

@SuppressWarnings("CanBeFinal")
public class OperatorMeterReadingActivityPresenter implements TMFilterListRequestCallListener {

    private final String TAG = this.getClass().getName();
    private WeakReference<OperatorMeterReadingActivity> fragmentWeakReference;

    public OperatorMeterReadingActivityPresenter(OperatorMeterReadingActivity tmFiltersListActivity) {
        fragmentWeakReference = new WeakReference<>(tmFiltersListActivity);
    }

    public void getAllFiltersRequests() {
        OperatorMeterReadingRequestCall requestCall = new OperatorMeterReadingRequestCall();
        requestCall.setListener(this);

        //fragmentWeakReference.get().showProgressBar();
        requestCall.getAllPendingRequests();
    }

    /*public void approveRejectRequest(String requestStatus, PendingRequest pendingRequest) {
        TMPendingRequestCall requestCall = new TMPendingRequestCall();
        requestCall.setListener(this);

        //fragmentWeakReference.get().showProgressBar();
        requestCall.approveRejectRequest(requestStatus, pendingRequest);
    }*/

    @Override
    public void onFilterListRequestsFetched(String response) {
        //fragmentWeakReference.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            OperatorMachineData pendingRequestsResponse
                    = new Gson().fromJson(response, OperatorMachineData.class);
            if (pendingRequestsResponse != null && pendingRequestsResponse.getOperatorMachineCodeDataModel() != null )
            {
                fragmentWeakReference.get().showPendingApprovalRequests(pendingRequestsResponse.getOperatorMachineCodeDataModel());
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

    //create request object according to filters
    public JSONObject createBodyParams(String type, String startDate, String endDate, ArrayList<SubFilterset> subFiltersets, String approval_type) {
        JSONObject requestObject = new JSONObject();
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson("");
        Log.d("JsonObjRequestfilter", "SubmitRequest: " + json);

        try {
            requestObject.put("type", type);
            requestObject.put("approval_type", approval_type);
            requestObject.put("filterSet", getFilterObject(subFiltersets, startDate, endDate));  // new JSONArray().put(getFilterObject()));
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

    private JSONObject getFilterObject(ArrayList<SubFilterset> subFiltersets, String startDate, String endDate) {
        JSONObject requestObject = new JSONObject();

        try {

            requestObject.put("start_date", "" + Util.getDateInepoch(startDate));
            requestObject.put("end_date", "" + Util.getDateInepoch(endDate));
            //requestObject.put("start_date","1558959956");//""+ Util.getDateInepoch(startDate)); //1558959956
            // requestObject.put("end_date","1560924451");//""+Util.getDateInepoch(endDate));//1558960046

            //requestObject.put("start_date","1556792226");//""+ Util.getDateInepoch(startDate)); //1558959956
            //requestObject.put("end_date","1557051426");//""+Util.getDateInepoch(endDate));//1558960046

            requestObject.put("filterType", "");
            requestObject.put("id", "");
            if (subFiltersets != null) {
                if (subFiltersets.size() > 0) {
                    requestObject.put("filterType", "category");
                    requestObject.put("id", getidObject(subFiltersets));  // "5c6bbf3dd503a3057867cf24");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestObject;
    }

    private JSONArray getidObject(ArrayList<SubFilterset> subFiltersets) {
        JSONArray requestObject = new JSONArray();

        try {
            for (int i = 0; i < subFiltersets.size(); i++) {
                if (subFiltersets.get(i).isSelected()) {
                    requestObject.put(subFiltersets.get(i).get_id());
                }
            }
            //requestObject.put("5c6bbf3dd503a3057867cf24");
            //requestObject.put("5c6bbf07d503a30a5e724eab");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestObject;
    }
}
