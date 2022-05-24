package com.octopusbjsindia.view.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.SujalamSuphalam.OpratorListData;
import com.octopusbjsindia.presenter.SupervisorListFragmentPresenter;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.OperatorsListAdapter;

import java.util.List;
import java.util.Objects;

public class SupervisorListFragment extends Fragment implements APIDataListener {

    private View view;
    private RelativeLayout progressBarLayout;
    private SupervisorListFragmentPresenter presenter;
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

        presenter = new SupervisorListFragmentPresenter(this);

        machineId = getActivity().getIntent().getStringExtra("machineId");
        machineCode = getActivity().getIntent().getStringExtra("machineCode");

        if (Util.isConnected(getActivity())) {
            presenter.getSupervisors();
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

    public void assignOperatorsSuccess(String msg) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogs_leave_layout);

        TextView title = dialog.findViewById(R.id.tv_dialog_title);
        title.setText(R.string.alert);
        title.setVisibility(View.VISIBLE);

        TextView text = dialog.findViewById(R.id.tv_dialog_subtext);
        text.setText(msg);
        text.setVisibility(View.VISIBLE);

        Button button = dialog.findViewById(R.id.btn_dialog);
        button.setText(R.string.ok);
        button.setVisibility(View.GONE);
        button.setOnClickListener(v -> {
            // Close dialog
            dialog.dismiss();
        });

        Button button1 = dialog.findViewById(R.id.btn_dialog_1);
        button1.setText(R.string.ok);
        button1.setVisibility(View.VISIBLE);
        button1.setOnClickListener(v -> {
            // Close dialog
            getActivity().finish();
            dialog.dismiss();
        });

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
