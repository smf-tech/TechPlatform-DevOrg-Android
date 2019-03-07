package com.platform.view.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.platform.R;
import com.platform.listeners.TMTaskListener;
import com.platform.models.tm.PendingRequest;
import com.platform.presenter.RejectedFragmentPresenter;
import com.platform.view.adapters.TMRejectedAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

@SuppressWarnings("WeakerAccess")
public class TMUserRejectedFragment extends Fragment implements TMTaskListener {

    private View tmFragmentView;
    private TextView txtNoData;
    private RecyclerView rvApprovedRequests;
    private RejectedFragmentPresenter presenter;

    private boolean isVisible;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        tmFragmentView = inflater.inflate(R.layout.fragment_dashboard_tm, container, false);
        return tmFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new RejectedFragmentPresenter(this);
        presenter.getAllRejectedRequests();

        init();
    }

    private void init() {
        txtNoData = tmFragmentView.findViewById(R.id.txt_no_data);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvApprovedRequests = tmFragmentView.findViewById(R.id.rv_dashboard_tm);
        rvApprovedRequests.setLayoutManager(layoutManager);
        rvApprovedRequests.setItemAnimator(new DefaultItemAnimator());

        tmFragmentView.findViewById(R.id.txt_view_all_approvals).setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        isVisible = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && isVisible) {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                if (presenter == null) {
                    presenter = new RejectedFragmentPresenter(TMUserRejectedFragment.this);
                }
                presenter.getAllRejectedRequests();
            }, 100);
        }
    }

    @Override
    public void showPendingRequests(List<PendingRequest> pendingRequestList) {
        if (pendingRequestList != null && !pendingRequestList.isEmpty()) {
            txtNoData.setVisibility(View.GONE);
            rvApprovedRequests.setVisibility(View.VISIBLE);

            TMRejectedAdapter newTMAdapter = new TMRejectedAdapter(pendingRequestList, presenter);
            rvApprovedRequests.setAdapter(newTMAdapter);
        } else {
            txtNoData.setVisibility(View.VISIBLE);
            rvApprovedRequests.setVisibility(View.GONE);
        }
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
