package com.platform.view.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.platform.R;
import com.platform.listeners.APIDataListener;
import com.platform.models.events.CommonResponse;
import com.platform.presenter.MachineDetailsFragmentPresenter;
import com.platform.presenter.MachineMouFourthFragmentPresenter;
import com.platform.utility.Util;
import com.platform.view.activities.MachineMouActivity;

import java.util.Objects;

public class MachineMouFourthFragment extends Fragment implements View.OnClickListener, APIDataListener {
    private View machineMouFourthFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private Button btnFourthPartMou;
    private MachineMouFourthFragmentPresenter machineMouFourthFragmentPresenter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        machineMouFourthFragmentView = inflater.inflate(R.layout.fragment_machine_mou_fourth, container, false);
        return machineMouFourthFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        progressBarLayout = machineMouFourthFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = machineMouFourthFragmentView.findViewById(R.id.pb_profile_act);
        btnFourthPartMou = machineMouFourthFragmentView.findViewById(R.id.btn_fourth_part_mou);
        btnFourthPartMou.setOnClickListener(this);
        machineMouFourthFragmentPresenter = new MachineMouFourthFragmentPresenter(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (machineMouFourthFragmentPresenter != null) {
            machineMouFourthFragmentPresenter.clearData();
            machineMouFourthFragmentPresenter = null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_fourth_part_mou:
                if(isAllDataValid()) {
                    updateMouData();
                }
                break;
        }
    }

    private void updateMouData() {
        //((MachineMouActivity) getActivity()).getMachineDetailData().setMeetOrganizers(selectedOrganizersList);
        machineMouFourthFragmentPresenter.submitMouData(((MachineMouActivity) getActivity()).getMachineDetailData());
    }

    public boolean isAllDataValid() {
        return true;
    }

    private void showResponseDialog(String dialogTitle, String message, String btn1String, String
            btn2String) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogs_leave_layout);

        if (!TextUtils.isEmpty(dialogTitle)) {
            TextView title = dialog.findViewById(R.id.tv_dialog_title);
            title.setText(dialogTitle);
            title.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(message)) {
            TextView text = dialog.findViewById(R.id.tv_dialog_subtext);
            text.setText(message);
            text.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(btn1String)) {
            Button button = dialog.findViewById(R.id.btn_dialog);
            button.setText(btn1String);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(v -> {
                // Close dialog
                dialog.dismiss();
            });
        }

        if (!TextUtils.isEmpty(btn2String)) {
            Button button1 = dialog.findViewById(R.id.btn_dialog_1);
            button1.setText(btn2String);
            button1.setVisibility(View.VISIBLE);
            button1.setOnClickListener(v -> {
                // Close dialog
            });
        }

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        if (getActivity() != null) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.msg_failure),
                    Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (getActivity() != null) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.msg_failure),
                    Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        if (requestID.equalsIgnoreCase(MachineMouFourthFragmentPresenter.SUBMIT_MOU)) {
            CommonResponse responseOBJ = new Gson().fromJson(response, CommonResponse.class);
            showResponseDialog("Confirmation", responseOBJ.getMessage(), "OK", "");
        }
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
        getActivity().finish();
    }
}
