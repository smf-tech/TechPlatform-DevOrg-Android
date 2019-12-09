package com.octopusbjsindia.listeners;

import com.octopusbjsindia.models.forms.Elements;

@SuppressWarnings("unused")
public interface FormDataTaskListener {

    void showProgressBar();

    void hideProgressBar();

    <T> void showNextScreen(T data);

    void showErrorMessage(String result);

    void showChoicesByUrlAsync(String result, Elements elements);

}
