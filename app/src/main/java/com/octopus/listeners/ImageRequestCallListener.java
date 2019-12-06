package com.octopus.listeners;

public interface ImageRequestCallListener {

    void onImageUploadedListener(String response, final String formName);

    void onFailureListener(String error);

}
