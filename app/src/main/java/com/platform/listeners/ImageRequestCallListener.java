package com.platform.listeners;

public interface ImageRequestCallListener {

    void onImageUploadedListener(String response);

    void onFailureListener(String error);

}
