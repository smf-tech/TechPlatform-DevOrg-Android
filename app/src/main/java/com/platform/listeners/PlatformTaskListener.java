package com.platform.listeners;

@SuppressWarnings("unused")
public interface PlatformTaskListener {

    void showProgressBar();

    void hideProgressBar();

    <T> void gotoNextScreen(T data);

    void showErrorMessage(String result);
}
