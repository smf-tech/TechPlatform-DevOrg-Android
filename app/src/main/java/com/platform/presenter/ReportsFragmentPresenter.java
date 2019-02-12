package com.platform.presenter;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.listeners.PlatformRequestCallListener;
import com.platform.models.reports.Reports;
import com.platform.request.ReportsRequestCall;
import com.platform.view.fragments.ReportsFragment;

import java.lang.ref.WeakReference;

public class ReportsFragmentPresenter implements PlatformRequestCallListener {

    private static WeakReference<ReportsFragment> fragmentWeakReference;

    public ReportsFragmentPresenter(ReportsFragment reportsFragment) {
        fragmentWeakReference = new WeakReference<>(reportsFragment);
    }

    public void getAllReports() {
        ReportsRequestCall requestCall = new ReportsRequestCall();
        requestCall.setListener(this);

        fragmentWeakReference.get().showProgressBar();
        requestCall.getAllReports();
    }

    @Override
    public void onSuccessListener(String response) {
        fragmentWeakReference.get().hideProgressBar();

        Reports data = new Gson().fromJson(response, Reports.class);
        fragmentWeakReference.get().showNextScreen(data);
    }

    @Override
    public void onFailureListener(String message) {
        fragmentWeakReference.get().hideProgressBar();
    }

    @Override
    public void onErrorListener(VolleyError error) {
        fragmentWeakReference.get().hideProgressBar();
    }
}
