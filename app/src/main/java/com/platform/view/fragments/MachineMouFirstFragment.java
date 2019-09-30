package com.platform.view.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.platform.R;
import com.platform.listeners.APIDataListener;
import com.platform.listeners.CustomSpinnerListener;
import com.platform.models.common.CustomSpinnerObject;
import com.platform.presenter.MachineMouFragmentPresenter;
import com.platform.utility.Util;
import com.platform.view.activities.MachineMouActivity;
import com.platform.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;

public class MachineMouFirstFragment extends Fragment  implements APIDataListener, CustomSpinnerListener, View.OnClickListener {
    private View machineMouFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private MachineMouFragmentPresenter machineMouFragmentPresenter;
    String machineId;
    private ArrayList<CustomSpinnerObject> mOwnerTypeList = new ArrayList<>();
    private EditText editOwnerType;
    private Button btnFirstPartMou;
    private LinearLayout llEligible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        machineMouFragmentView = inflater.inflate(R.layout.fragment_machine_mou, container, false);
        return machineMouFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();
//        machineId = bundle.getString("machineId");
        init();
    }

    private void init() {
        progressBarLayout = machineMouFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = machineMouFragmentView.findViewById(R.id.pb_profile_act);
        btnFirstPartMou = machineMouFragmentView.findViewById(R.id.btn_first_part_mou);
        btnFirstPartMou.setOnClickListener(this);
        llEligible = machineMouFragmentView.findViewById(R.id.ll_eligible);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (machineMouFragmentPresenter != null) {
            machineMouFragmentPresenter.clearData();
            machineMouFragmentPresenter = null;
        }
    }

    @Override
    public void onFailureListener(String requestID, String message) {

    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {

    }

    @Override
    public void showProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void closeCurrentActivity() {
//        if (getActivity() != null) {
//            getActivity().onBackPressed();
//        }
        getActivity().finish();
    }

    @Override
    public void onCustomSpinnerSelection(String type) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edt_contract_date:
                CustomSpinnerDialogClass cddCity = new CustomSpinnerDialogClass(getActivity(), this, "Select Owner Type",
                        mOwnerTypeList, false);
                cddCity.show();
                cddCity.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.btn_first_part_mou:
                ((MachineMouActivity) getActivity()).openFragment("MachineMouSecondFragment");
                break;
        }
    }
}
