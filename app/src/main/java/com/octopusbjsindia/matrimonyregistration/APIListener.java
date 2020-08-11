package com.octopusbjsindia.matrimonyregistration;

public interface APIListener {

    void showMessage(String requestID, String message, int code);

    void showProgressBar();

    void hideProgressBar();


}
