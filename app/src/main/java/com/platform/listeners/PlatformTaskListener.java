package com.platform.listeners;

@SuppressWarnings("unused")
public interface PlatformTaskListener {

    void showProgressBar();

    void hideProgressBar();

    void gotoNextScreen(String response);
}
