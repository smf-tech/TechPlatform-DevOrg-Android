package com.octopusbjsindia.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.listeners.UserRequestCallListener;
import com.octopusbjsindia.models.home.Home;
import com.octopusbjsindia.models.home.RoleAccessAPIResponse;
import com.octopusbjsindia.models.user.User;
import com.octopusbjsindia.models.user.UserInfo;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.request.HomeRequestCall;
import com.octopusbjsindia.request.LoginRequestCall;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.PlatformGson;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.fragments.HomeFragment;

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
        if(models.getStatus() == 1000) {
            Util.logOutUser(homeFragment.get().getActivity());
        }else if (models.getStatus() == 200) {
            homeFragment.get().showNextScreen(models);
        }else {
            Util.showToast(models.getMessage(),homeFragment);
        }
        }


    @Override
    public void onUserProfileSuccessListener(String response) {
        User user = new Gson().fromJson(response, User.class);
        if (response != null && user.getUserInfo() != null) {
            Util.saveUserObjectInPref(new Gson().toJson(user.getUserInfo()));
        }
        homeFragment.get().getdynamicLogo();
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
        if (homeFragment != null && homeFragment.get() != null) {
            homeFragment.get().initiateViewPager();
            homeFragment.get().hideProgressBar();
            homeFragment.get().onFailureListener(requestID,message);
        }
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (homeFragment != null && homeFragment.get() != null) {
            homeFragment.get().hideProgressBar();
            homeFragment.get().initiateViewPager();
            if (error != null) {
                homeFragment.get().onErrorListener(requestID,error);
            }
        }
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
                        homeFragment.get().initiateViewPager();
                    } else {
                        homeFragment.get().initiateViewPager();
                    }
                }
            }
        } catch (Exception e) {
//            homeFragment.get().initiateViewPager();
            homeFragment.get().showErrorMessage(homeFragment.get().getResources().getString(R.string.msg_something_went_wrong));
        }
    }
}
