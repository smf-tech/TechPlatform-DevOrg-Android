package com.platform.listeners;

@SuppressWarnings("unused")
public interface FormDataTaskListener {

    void showProgressBar();

    void hideProgressBar();

    <T> void showNextScreen(T data);

    void showErrorMessage(String result);

    void showChoicesByUrl(String result);
}
