package com.octopusbjsindia.presenter;

import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.profile.OrganizationRolesResponse;
import com.octopusbjsindia.models.support.TicketAssingnRequest;
import com.octopusbjsindia.models.support.TicketResponse;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.PlatformGson;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.fragments.TicketDetailFragment;
import com.octopusbjsindia.view.fragments.TicketListFragment;

import org.json.JSONObject;

public class TicketDetailFragmentPresenter implements APIPresenterListener {

    TicketDetailFragment mContext;

    public TicketDetailFragmentPresenter(TicketDetailFragment mContext) {
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
        mContext.hideProgressBar();
        if(requestID.equalsIgnoreCase("Roles")){
            OrganizationRolesResponse orgRolesResponse
                    = new Gson().fromJson(response, OrganizationRolesResponse.class);

            if (orgRolesResponse != null && orgRolesResponse.getData() != null &&
                    !orgRolesResponse.getData().isEmpty() && orgRolesResponse.getData().size() > 0) {
                mContext.setRoles(orgRolesResponse.getData());
            }
        } else  if(requestID.equalsIgnoreCase("ASSIGN_TICKETS")){
            OrganizationRolesResponse orgRolesResponse
                    = new Gson().fromJson(response, OrganizationRolesResponse.class);

            if (orgRolesResponse != null && orgRolesResponse.getData() != null &&
                    !orgRolesResponse.getData().isEmpty() && orgRolesResponse.getData().size() > 0) {
                mContext.setRoles(orgRolesResponse.getData());
            }
        }
    }

    public void getRoles(String orgId, String projectId) {
        mContext.showProgressBar();
        final String url = BuildConfig.BASE_URL
                + String.format(Urls.Profile.GET_ORGANIZATION_ROLES, orgId, projectId);
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall("Roles", url);
    }

    public void assinged(TicketAssingnRequest request) {
        mContext.showProgressBar();
        final String url = BuildConfig.BASE_URL+Urls.Support.ASSIGN_TICKETS;

        Gson gson = new Gson();
        String jsonRequest = gson.toJson(request);

        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall("ASSIGN_TICKETS", jsonRequest, url);
    }
}
