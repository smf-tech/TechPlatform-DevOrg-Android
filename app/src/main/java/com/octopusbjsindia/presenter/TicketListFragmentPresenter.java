package com.octopusbjsindia.presenter;

import android.content.Context;

import com.android.volley.VolleyError;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.SujalamSuphalam.MachineListAPIResponse;
import com.octopusbjsindia.models.support.TicketResponse;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.PlatformGson;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.view.fragments.TicketListFragment;

import org.json.JSONObject;

public class TicketListFragmentPresenter implements APIPresenterListener {

    TicketListFragment mContext;

    public TicketListFragmentPresenter(TicketListFragment mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onFailureListener(String requestID, String message) {

    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        TicketResponse respons = PlatformGson.getPlatformGsonInstance().fromJson(response, TicketResponse.class);
        if (respons.getCode() == 200) {
            mContext.setTicket(respons.getData());
        } else {
            mContext.showMessage(requestID,respons.getMessage(),respons.getCode());
        }
    }

    public void getTickets() {
        mContext.showProgressBar();
        final String url = BuildConfig.BASE_URL + Urls.Support.GET_TICKETS;
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall("TicketsList", url);
    }
}
