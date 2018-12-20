package com.platform.listeners;

public interface LoginActivityListener extends PlatformTaskListener {

    boolean isAllInputsValid();

    interface ILoginInfo {
        String getMobileNumber();
    }
}
