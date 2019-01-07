package com.platform.listeners;

@SuppressWarnings("unused")
public interface PlatformTaskListener {

    void showProgressBar();

    void hideProgressBar();

    <T> void showNextScreen(T data);

    void showErrorMessage(String result);
}
