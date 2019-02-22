package com.platform.listeners;

import com.platform.models.forms.Elements;

@SuppressWarnings("unused")
public interface FormDataTaskListener {

    void showProgressBar();

    void hideProgressBar();

    <T> void showNextScreen(T data);

    void showErrorMessage(String result);

    void showChoicesByUrl(String result, Elements elements);
}
