package com.platform.presenter;

import com.android.volley.VolleyError;
import com.platform.listeners.APIPresenterListener;
import com.platform.view.fragments.MachineMouFirstFragment;

import java.lang.ref.WeakReference;

public class MachineMouFragmentPresenter  implements APIPresenterListener {
    private WeakReference<MachineMouFirstFragment> fragmentWeakReference;
    private final String TAG = StructureMachineListFragmentPresenter.class.getName();

    public MachineMouFragmentPresenter(MachineMouFirstFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }
    @Override
    public void onFailureListener(String requestID, String message) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            fragmentWeakReference.get().onFailureListener(requestID,message);
        }
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            if (error != null) {
                fragmentWeakReference.get().onErrorListener(requestID,error);
            }
        }
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        if (fragmentWeakReference == null) {
            return;
        }
        fragmentWeakReference.get().hideProgressBar();
        try {
            if (response != null) {

            }
        } catch (Exception e) {
            fragmentWeakReference.get().onFailureListener(requestID, e.getMessage());
        }
    }
}
