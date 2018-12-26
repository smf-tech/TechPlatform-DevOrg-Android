package com.platform.listeners;

import android.support.v4.app.FragmentActivity;

public interface OtpFragmentListener extends PlatformTaskListener {

    FragmentActivity getActivity();

    boolean isAllFieldsValid();

    void startOtpTimer();

    void registerOtpSmsReceiver();

    void deRegisterOtpSmsReceiver();

    void setMobileNumber(String mobileNumber);
}
