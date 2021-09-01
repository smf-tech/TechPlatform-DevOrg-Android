package com.octopusbjsindia.presenter;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.content.ContentAPIResponse;
import com.octopusbjsindia.models.content.ContentData;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.PlatformGson;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.fragments.ContentManagementFragment;

import java.lang.ref.WeakReference;

public class ContentFragmentPresenter implements APIPresenterListener {

    private WeakReference<ContentManagementFragment> fragmentWeakReference;
    private final String TAG = ContentFragmentPresenter.class.getName();
    public static final String GET_CONTENT_DATA = "getContentData";
    public static final String SEND_CONTENT_DETAILS = "sendContentData";

    public ContentFragmentPresenter(ContentManagementFragment mFragment) {
        fragmentWeakReference = new WeakReference<>(mFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }

    public void getContentData() {
        final String getContentUrl = BuildConfig.BASE_URL
                + String.format(Urls.ContentManagement.GET_CONTENT_DATA, Util.getLocaleLanguageCode());
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall(GET_CONTENT_DATA, getContentUrl);
    }

    public void sendDownloadedContentDetails(ContentData contentData) {
        final String sendContentDetailsUrl = BuildConfig.BASE_URL
                + String.format(Urls.ContentManagement.SEND_DOWNLOADED_CONTENT_DETAILS);
        fragmentWeakReference.get().showProgressBar();
        Gson gson = new GsonBuilder().create();
        String params = gson.toJson(contentData);
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(SEND_CONTENT_DETAILS, params, sendContentDetailsUrl);
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            fragmentWeakReference.get().onFailureListener(requestID, message);
        }
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            if (error != null) {
                fragmentWeakReference.get().onErrorListener(requestID, error);
            }
        }
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        if (fragmentWeakReference == null) {
            return;
        }
        fragmentWeakReference.get().hideProgressBar();
        try {
            if (response != null) {
                if (requestID.equalsIgnoreCase(ContentFragmentPresenter.GET_CONTENT_DATA)) {
                    ContentAPIResponse allContent = PlatformGson.getPlatformGsonInstance().fromJson(response,
                            ContentAPIResponse.class);
                    if (allContent.getStatus() == 1000) {
                        fragmentWeakReference.get().logOutUser();
                    }
                    if (allContent.getStatus() == 200) {
                        fragmentWeakReference.get().saveContentData(allContent.getData());
                    } else {
                        fragmentWeakReference.get().showEmptyResponse(allContent.getMessage());
                    }
                }
//                else if (requestID.equalsIgnoreCase(ContentFragmentPresenter.SEND_CONTENT_DETAILS)) {
//                    CommonResponseStatusString responseStatusString = PlatformGson.getPlatformGsonInstance().fromJson(response,
//                            CommonResponseStatusString.class);
//                    if (responseStatusString.getStatus().equals("200")) {
//                        fragmentWeakReference.get().onSuccessListener(ContentFragmentPresenter.SEND_CONTENT_DETAILS,
//                                responseStatusString.getMessage());
//                    }
//                }
            }
        } catch (Exception e) {
            fragmentWeakReference.get().onFailureListener(requestID, e.getMessage());
        }
    }
}
