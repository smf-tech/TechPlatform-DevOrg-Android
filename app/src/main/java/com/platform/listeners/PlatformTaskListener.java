package com.platform.listeners;

public interface PlatformTaskListener {

    void showProgressBar();

    void hideProgressBar();

    void gotoNextScreen(String response);
}
