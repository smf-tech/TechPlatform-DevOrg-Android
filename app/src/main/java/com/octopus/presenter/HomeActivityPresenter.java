package com.octopus.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopus.BuildConfig;
import com.octopus.Platform;
import com.octopus.R;
import com.octopus.listeners.APIPresenterListener;
import com.octopus.listeners.UserRequestCallListener;
import com.octopus.models.home.Home;
import com.octopus.models.home.RoleAccessAPIResponse;
import com.octopus.models.user.User;
import com.octopus.models.user.UserInfo;
import com.octopus.request.APIRequestCall;
import com.octopus.request.HomeRequestCall;
import com.octopus.request.LoginRequestCall;
import com.octopus.utility.Constants;
import com.octopus.utility.PlatformGson;
import com.octopus.utility.Urls;
import com.octopus.utility.Util;
import com.octopus.view.fragments.HomeFragment;

import java.lang.ref.WeakReference;

@SuppressWarnings("CanBeFinal")
public class HomeActivityPresenter implements UserRequestCallListener, APIPresenterListener {

    private final String TAG = HomeActivityPresenter.class.getName();
    private WeakReference<HomeFragment> homeFragment;
    public static final String GET_ROLE_ACCESS ="getRoleAccesss";

    public HomeActivityPresenter(HomeFragment activity) {
        homeFragment = new WeakReference<>(activity);
    }

    public void getUserProfile() {
        LoginRequestCall loginRequestCall = new LoginRequestCall();
        loginRequestCall.setListener(this);
        loginRequestCall.getUserProfile();
    }

    public void getModules(UserInfo user) {
        HomeRequestCall requestCall = new HomeRequestCall();
        requestCall.setListener(this);

        homeFragment.get().showProgressBar();
        requestCall.getHomeModules(user);
    }

    public void getRoleAccess() {
        final String getRoleAccessUrl = BuildConfig.BASE_URL
                + String.format(Urls.Home.GET_ROLE_ACCESS);
        Log.d(TAG, "getRoleAccessUrl: url" + getRoleAccessUrl);
        homeFragment.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall(GET_ROLE_ACCESS, getRoleAccessUrl);
    }

    @Override
    public void onSuccessListener(String response) {
        Home models = PlatformGson.getPlatformGsonInstance().fromJson(response, Home.class);
        homeFragment.get().showNextScreen(models);
    }

    @Override
    public void onUserProfileSuccessListener(String response) {
        User user = new Gson().fromJson(response, User.class);
        if (response != null && user.getUserInfo() != null) {
            Util.saveUserObjectInPref(new Gson().toJson(user.getUserInfo()));
        }
        getModules(user.getUserInfo());
    }

    @Override
    public void onFailureListener(String message) {
        if (!TextUtils.isEmpty(message)) {
            Log.e(TAG, "onFailureListener :" + message);
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {
        Log.e(TAG, "onErrorListener :" + error);

        if (error.networkResponse != null) {
            if (error.networkResponse.statusCode == Constants.TIMEOUT_ERROR_CODE) {
                if (error.networkResponse.data != null) {
                    String json = new String(error.networkResponse.data);
                    json = Util.trimMessage(json);
                    if (json != null) {
                        Util.showToast(json, homeFragment.get().getActivity());
                    } else {
                        Util.showToast(Platform.getInstance().getString(R.string.msg_slow_network),
                                homeFragment.get().getActivity());
                    }
                } else {
                    Util.showToast(Platform.getInstance().getString(R.string.msg_slow_network),
                            homeFragment.get().getActivity());
                }
            } else {
                Util.showToast(homeFragment.get().getString(R.string.unexpected_error_occurred), homeFragment.get().getActivity());
                Log.e("onErrorListener",
                        "Unexpected response code " + error.networkResponse.statusCode);
            }
        }
    }

    @Override
    public void onFailureListener(String requestID, String message) {

    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        if (homeFragment == null) {
            return;
        }
        homeFragment.get().hideProgressBar();
        try {
            if (response != null) {
                if (requestID.equalsIgnoreCase(HomeActivityPresenter.GET_ROLE_ACCESS)) {
                    RoleAccessAPIResponse roleAccessAPIResponse = new Gson().fromJson(response, RoleAccessAPIResponse.class);
                    if(roleAccessAPIResponse.getStatus() == 1000) {
                        Util.logOutUser(homeFragment.get().getActivity());
                    }else if(roleAccessAPIResponse.getStatus() == 200 && roleAccessAPIResponse.getData() != null) {
                        Util.saveRoleAccessObjectInPref(response);
                    }
                }
            }
        } catch (Exception e) {
            homeFragment.get().showErrorMessage(homeFragment.get().getResources().getString(R.string.msg_something_went_wrong));
        }
    }
}
