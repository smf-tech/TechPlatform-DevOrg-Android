package com.octopusbjsindia.presenter;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.models.events.CommonResponseStatusString;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.view.fragments.NewOtpFragment;
import com.octopusbjsindia.view.fragments.smartgirlfragment.MemberListFragment;

import java.lang.ref.WeakReference;

public class MemberListFragmentPresenter implements APIPresenterListener {

    private final String SEND_FEEDBACK_EMAIL = "sendfeedbackemail";

    private WeakReference<MemberListFragment> memberListFragment;

    public MemberListFragmentPresenter(MemberListFragment memberListFragment) {
        this.memberListFragment = new WeakReference<>(memberListFragment);
    }

    @Override
    public void onFailureListener(String requestID, String message) {

    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {

        try {
            if (response != null) {
                if (requestID.equalsIgnoreCase(SEND_FEEDBACK_EMAIL)) {
                    CommonResponseStatusString commonResponse = new Gson().fromJson(response, CommonResponseStatusString.class);
                    memberListFragment.get().showToastMessage(commonResponse.getMessage());
                }
            }
        }catch (Exception e){

        }

    }

    public void sendFeedbackEmail(String requestJson){
        final String url  = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.SEND_FEEDBACK_EMAIL);
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(SEND_FEEDBACK_EMAIL,requestJson,url);

    }
}
