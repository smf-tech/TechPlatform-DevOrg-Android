package com.octopus.listeners;

import com.octopus.models.forms.Elements;

@SuppressWarnings("unused")
public interface FormDataTaskListener {

    void showProgressBar();

    void hideProgressBar();

    <T> void showNextScreen(T data);

    void showErrorMessage(String result);

    void showChoicesByUrlAsync(String result, Elements elements);

}
