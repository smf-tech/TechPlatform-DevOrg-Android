package com.platform.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.platform.R;
import com.platform.listeners.TMTaskListener;
import com.platform.models.tm.PendingRequest;
import com.platform.presenter.ApprovedFragmentPresenter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

@SuppressWarnings("WeakerAccess")
public class TMUserApprovedFragment extends Fragment implements TMTaskListener {

    private ApprovedFragmentPresenter pendingFragmentPresenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connect, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pendingFragmentPresenter = new ApprovedFragmentPresenter(this);
        pendingFragmentPresenter.getAllApprovedRequests();
    }

    @Override
    public void showPendingRequests(List<PendingRequest> pendingRequestList) {

    }

    @Override
    public void updateRequestStatus(String response, PendingRequest pendingRequest) {

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public <T> void showNextScreen(T data) {

    }

    @Override
    public void showErrorMessage(String result) {

    }
}
