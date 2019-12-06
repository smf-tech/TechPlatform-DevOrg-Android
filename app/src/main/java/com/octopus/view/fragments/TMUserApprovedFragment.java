package com.octopus.view.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.octopus.R;
import com.octopus.listeners.TMTaskListener;
import com.octopus.models.tm.PendingRequest;
import com.octopus.presenter.ApprovedFragmentPresenter;
import com.octopus.view.adapters.TMApprovedAdapter;

import java.util.HashMap;
import java.util.List;

import static com.octopus.utility.Constants.TM.USER_APPROVALS;

public class TMUserApprovedFragment extends Fragment implements TMTaskListener {

    private View tmFragmentView;
    private TextView txtNoData;
    private ExpandableListView rvApprovedRequests;
    private ApprovedFragmentPresenter presenter;

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

        presenter = new ApprovedFragmentPresenter(this);
        presenter.getAllApprovedRequests();

        init();
    }

    private void init() {
        txtNoData = tmFragmentView.findViewById(R.id.txt_no_data);
        rvApprovedRequests = tmFragmentView.findViewById(R.id.rv_dashboard_tm);

        tmFragmentView.findViewById(R.id.txt_view_all_approvals).setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        isVisible = true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && isVisible) {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                if (presenter == null) {
                    presenter = new ApprovedFragmentPresenter(TMUserApprovedFragment.this);
                }
                presenter.getAllApprovedRequests();
            }, 100);
        }
    }

    @Override
    public void showPendingRequests(List<PendingRequest> pendingRequestList) {
        if (pendingRequestList != null && !pendingRequestList.isEmpty()) {
            txtNoData.setVisibility(View.GONE);
            rvApprovedRequests.setVisibility(View.VISIBLE);

            HashMap<String, List<PendingRequest>> approvedRequestMap = new HashMap<>();
            approvedRequestMap.put(USER_APPROVALS, pendingRequestList);
//            approvedRequestMap.put(FORM_APPROVALS, pendingRequestList);
            TMApprovedAdapter newTMAdapter = new TMApprovedAdapter(getContext(),
                    approvedRequestMap, presenter);
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
