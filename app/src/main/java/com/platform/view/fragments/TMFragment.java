package com.platform.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.platform.R;
import com.platform.listeners.TMTaskListener;
import com.platform.models.tm.PendingRequest;
import com.platform.presenter.TMFragmentPresenter;
import com.platform.view.adapters.NewTMAdapter;

import java.util.List;

public class TMFragment extends Fragment implements View.OnClickListener, TMTaskListener {

    private View tmFragmentView;
    private RecyclerView rvPendingRequests;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        tmFragmentView = inflater.inflate(R.layout.fragment_dashboard_tm, container, false);
        return tmFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TMFragmentPresenter tmFragmentPresenter = new TMFragmentPresenter(this);
        tmFragmentPresenter.getAllPendingRequests();

        init();
    }

    private void init() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvPendingRequests = tmFragmentView.findViewById(R.id.rv_dashboard_tm);
        rvPendingRequests.setLayoutManager(layoutManager);
        rvPendingRequests.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onClick(View v) {

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

    @Override
    public void showPendingRequests(List<PendingRequest> pendingRequestList) {
        if (pendingRequestList != null && !pendingRequestList.isEmpty()) {
            NewTMAdapter newTMAdapter = new NewTMAdapter(pendingRequestList);
            rvPendingRequests.setAdapter(newTMAdapter);
        }
    }
}
