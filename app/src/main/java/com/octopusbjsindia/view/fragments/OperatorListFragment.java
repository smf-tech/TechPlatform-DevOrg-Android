package com.octopusbjsindia.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.SujalamSuphalam.OpratorListData;
import com.octopusbjsindia.presenter.OperatorListFragmentPresenter;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.OperatorsListAdapter;

import java.util.List;

public class OperatorListFragment extends Fragment implements APIDataListener {

    private View view;
    private RelativeLayout progressBarLayout;
    private OperatorListFragmentPresenter presenter;
    Context activity;
    RecyclerView rvOperator;
    String machineId;
    String machineCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_operator_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBarLayout = view.findViewById(R.id.progress_bar);


        presenter = new OperatorListFragmentPresenter(this);

        machineId = getActivity().getIntent().getStringExtra("machineId");
        machineCode = getActivity().getIntent().getStringExtra("machineCode");

        if (Util.isConnected(getActivity())) {
            presenter.getOperators();
        } else {
            Util.showToast(activity.getString(R.string.msg_no_network), activity);
        }

        rvOperator = view.findViewById(R.id.rv_operator);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        Util.showToast(message, activity);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        Util.showToast(error.getMessage(), activity);
    }

    @Override
    public void onSuccessListener(String requestID, String response) {

    }

    @Override
    public void showProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null) {
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null) {
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void closeCurrentActivity() {

    }

    public void populateOpratorList(List<OpratorListData> data) {

        rvOperator.setLayoutManager(new LinearLayoutManager(getActivity()));
        OperatorsListAdapter ssMachineListAdapter = new OperatorsListAdapter(machineId, data, getActivity(), presenter);
        rvOperator.setAdapter(ssMachineListAdapter);

    }
}
