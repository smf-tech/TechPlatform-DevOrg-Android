package com.platform.view.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.platform.R;
import com.platform.listeners.LeaveDataListener;
import com.platform.models.events.CommonResponse;
import com.platform.models.leaves.LeaveBalanceResponse;
import com.platform.models.leaves.LeaveData;
import com.platform.models.leaves.LeaveDetail;
import com.platform.models.leaves.LeaveType;
import com.platform.models.leaves.UserLeaves;
import com.platform.presenter.LeavesPresenter;
import com.platform.utility.Constants;
import com.platform.utility.PlatformGson;
import com.platform.utility.PreferenceHelper;
import com.platform.utility.Util;
import com.platform.view.activities.GeneralActionsActivity;
import com.platform.view.adapters.LeaveBalanceAdapter;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.platform.presenter.LeavesPresenter.GET_LEAVE_BALANCE;
import static com.platform.utility.Constants.DAY_MONTH_YEAR;
import static com.platform.utility.Util.getDateFromTimestamp;

public class LeaveApplyFragment extends Fragment implements View.OnClickListener, LeaveDataListener,
        RadioGroup.OnCheckedChangeListener {

    RecyclerView rvLeaveBalance;
    RecyclerView rvLeaveCategory;
    LeaveBalanceAdapter LeaveAdapterCategory;
    RecyclerView.LayoutManager mLayoutManagerLeaveCategory;

    private RadioButton btnHalfDay;
    private RadioButton btnFullDay;
    private RadioGroup rbGroup;
    private Button btnApplyLeaves;

    private EditText edtReason, edtRejectedReason;
    private TextInputLayout elyRejectedReason;
    private EditText btnStartDate;
    private EditText btnEndDate;
    private TextView tvCategoryLabel;
    private final Calendar c = Calendar.getInstance();
    private String applyType;
    private int dayLeaveType = -1;
    public boolean isEdit = false;
    private LeavesPresenter presenter;
    private ArrayList<LeaveDetail> leaveBalance = new ArrayList<>();
    public static ArrayList<Integer> leaveBackground = new ArrayList<>();
    public String selectedLeaveCatgory;
    public double selectedLeaveCatgoryBalance = 0;
    private RelativeLayout progressBarLayout;
    private ProgressBar progressBar;

    public LeaveApplyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_apply_leaves, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvLeaveBalance = view.findViewById(R.id.rv_leave_balance);
        rvLeaveCategory = view.findViewById(R.id.rv_leave_category);

        edtReason = view.findViewById(R.id.edt_reason);
        edtRejectedReason = view.findViewById(R.id.edt_rejected_reason);
        elyRejectedReason= view.findViewById(R.id.tly_rejected_reason);
        tvCategoryLabel = view.findViewById(R.id.tv_category);

        rbGroup = view.findViewById(R.id.radio_group_filter);
        btnHalfDay = view.findViewById(R.id.rb_half_day);

        btnFullDay = view.findViewById(R.id.rb_full_day);

        btnStartDate = view.findViewById(R.id.btn_start_date);

        btnEndDate = view.findViewById(R.id.btn_end_date);

        btnApplyLeaves = view.findViewById(R.id.btn_apply_leave);
        btnApplyLeaves.setOnClickListener(this);
        progressBarLayout = view.findViewById(R.id.profile_act_progress_bar);
        progressBar = view.findViewById(R.id.pb_profile_act);
        presenter = new LeavesPresenter(this);

        setUIData();
    }

    private void setUIData() {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String leaveDetail = bundle.getString("leaveDetail");
            isEdit = bundle.getBoolean("isEdit");
            applyType = bundle.getString("apply_type");
            leaveBalance.clear();
            leaveBackground.clear();
            if (bundle.getSerializable("leaveBalance") != null) {
                leaveBalance.addAll((ArrayList<LeaveDetail>) bundle.getSerializable("leaveBalance"));
                for (LeaveDetail l : leaveBalance) {
                    leaveBackground.add(R.drawable.bg_rect_primary_border);
                }
                RecyclerView.LayoutManager mLayoutManagerLeave = new LinearLayoutManager(getActivity(),
                        LinearLayoutManager.HORIZONTAL, true);
                LeaveBalanceAdapter LeaveAdapter = new LeaveBalanceAdapter(
                        leaveBalance, "LeaveBalance");
                rvLeaveBalance.setLayoutManager(mLayoutManagerLeave);
                rvLeaveBalance.setAdapter(LeaveAdapter);

                if (applyType.equalsIgnoreCase("Leave")) {
                    mLayoutManagerLeaveCategory = new LinearLayoutManager(getActivity(),
                            LinearLayoutManager.HORIZONTAL, true);
                    LeaveAdapterCategory = new LeaveBalanceAdapter(this,
                            leaveBalance, leaveBackground, "Category");
                    rvLeaveCategory.setLayoutManager(mLayoutManagerLeaveCategory);
                    rvLeaveCategory.setAdapter(LeaveAdapterCategory);
                }
            } else {
                presenter.getLeavesBalance();
            }
            if (isEdit) {
                LeaveData userLeaveDetail = (LeaveData) bundle.getSerializable("userLeaveDetails");
                if (userLeaveDetail != null) {
                    if (userLeaveDetail != null) {
                        setUserDataForEdit(userLeaveDetail);
                    }
                }
                btnApplyLeaves.setVisibility(View.GONE);
                edtReason.setEnabled(false);
                edtRejectedReason.setEnabled(false);
                btnHalfDay.setEnabled(false);
                btnFullDay.setEnabled(false);
            } else {
                rbGroup.setOnCheckedChangeListener(this);
                btnStartDate.setOnClickListener(this);
                btnEndDate.setOnClickListener(this);
            }
            if (applyType.equalsIgnoreCase("Comp-Off")) {
                rvLeaveCategory.setVisibility(View.GONE);
                tvCategoryLabel.setVisibility(View.GONE);
                btnApplyLeaves.setText(getString(R.string.request_comp_off));
            }
        }
    }

    private void setUserDataForEdit(LeaveData leaveDetailModel) {

        edtReason.setText(leaveDetailModel.getReason());
        if (leaveDetailModel.getRejectionReason() != null && leaveDetailModel.getRejectionReason().length() > 0) {
            elyRejectedReason.setVisibility(View.VISIBLE);
            edtRejectedReason.setText(leaveDetailModel.getRejectionReason());
        }
        btnStartDate.setText(getDateFromTimestamp(leaveDetailModel.getStartdate(), DAY_MONTH_YEAR));
        btnEndDate.setText(getDateFromTimestamp(leaveDetailModel.getEnddate(), DAY_MONTH_YEAR));

        String isHalfDay = leaveDetailModel.getFullHalfDay();
        if (isHalfDay.equalsIgnoreCase("half day")) {
            btnHalfDay.setChecked(true);
        } else {
            btnFullDay.setChecked(true);
        }

        String leaveCategory = leaveDetailModel.getLeaveType();
        for (int i = 0; i < leaveBalance.size(); i++) {
            if (leaveBalance.get(i).getType().equalsIgnoreCase(leaveCategory)) {
                leaveBackground.remove(i);
                leaveBackground.add(i, R.drawable.bg_rect_primary_field);
                selectedLeaveCatgory = leaveBalance.get(i).getType();
                selectedLeaveCatgoryBalance = leaveBalance.get(i).getBalance();
            }
        }
        LeaveAdapterCategory.notifyDataSetChanged();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (presenter != null) {
            presenter.clearData();
            presenter = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_apply_leave:
                if (applyType.equalsIgnoreCase("Leave")) {
                    applyForLeave();
                } else if (applyType.equalsIgnoreCase("Comp-Off")) {
                    applyForCompOff();
                }

                break;

            case R.id.btn_start_date:
                showDateDialogMin(getActivity(), btnStartDate, "StartDate");
                break;

            case R.id.btn_end_date:
                if (btnStartDate.getText().length() > 0) {
                    showDateDialogMin(getActivity(), btnEndDate, "EndDate");
                } else {
                    Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                    .findViewById(android.R.id.content), getString(R.string.enter_start_date),
                            Snackbar.LENGTH_LONG);
                }
                break;
        }
    }

    private void applyForCompOff() {
        if (dayLeaveType == -1 || TextUtils.isEmpty(btnStartDate.getText().toString())
                || TextUtils.isEmpty(btnEndDate.getText().toString()) || TextUtils.isEmpty(edtReason.getText().toString().trim())) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.enter_correct_details),
                    Snackbar.LENGTH_LONG);
        } else {
            LeaveData leaveData = new LeaveData();
            leaveData.setUserId(Util.getUserObjectFromPref().getId());
            leaveData.setStartdate(Util.dateTimeToTimeStamp(btnStartDate.getText().toString(), "00:00"));

            leaveData.setEnddate(Util.dateTimeToTimeStamp(btnEndDate.getText().toString(), "00:00"));
            if (dayLeaveType == 0) {
                leaveData.setFullHalfDay("half day");
                leaveData.setHalfFullDay(0);
            } else if (dayLeaveType == 1) {
                leaveData.setFullHalfDay("full day");
                leaveData.setHalfFullDay(1);
            }
            leaveData.setReason(edtReason.getText().toString());

            presenter.requestCompOff(leaveData);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_full_day:
                dayLeaveType = 1;
                btnEndDate.setEnabled(true);
                break;

            case R.id.rb_half_day:
                dayLeaveType = 0;
                btnEndDate.setEnabled(false);
                if (!btnStartDate.getText().toString().equalsIgnoreCase("Start Date")) {
                    btnEndDate.setText(btnStartDate.getText().toString());
                }
                break;
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void applyForLeave() {
        if (selectedLeaveCatgory == null || dayLeaveType == -1 || TextUtils.isEmpty(btnStartDate.getText().toString())
                || TextUtils.isEmpty(btnEndDate.getText().toString()) || TextUtils.isEmpty(edtReason.getText().toString().trim())) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.enter_correct_details),
                    Snackbar.LENGTH_LONG);
        } else if (selectedLeaveCatgoryBalance == 0) {
//            getDaysBetween(btnStartDate.getText().toString(),btnEndDate.getText().toString())
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.insufficisnt_leave_balance),
                    Snackbar.LENGTH_LONG);
        } else {
            LeaveData leaveData = new LeaveData();
            leaveData.setUserId(Util.getUserObjectFromPref().getId());
            leaveData.setLeaveType(selectedLeaveCatgory);
            leaveData.setStartdate(Util.dateTimeToTimeStamp(btnStartDate.getText().toString(), "00:00"));
            leaveData.setEnddate(Util.dateTimeToTimeStamp(btnEndDate.getText().toString(), "00:00"));
            if (dayLeaveType == 0) {
                leaveData.setFullHalfDay("half day");
                leaveData.setHalfFullDay(0);
            } else if (dayLeaveType == 1) {
                leaveData.setFullHalfDay("full day");
                leaveData.setHalfFullDay(1);
            }
            leaveData.setReason(edtReason.getText().toString());

            presenter.postUserLeave(leaveData);
        }
    }

    private int getDaysBetween(String start, String end) {
        int days = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDate = sdf.parse(start);
            Date endDate = sdf.parse(end);

            long diff = endDate.getTime() - startDate.getTime();
            days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;
    }

    private void showDateDialogMin(Context context, final EditText editText, String dateType) {
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dateDialog
                = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {

            String date = String.format(Locale.getDefault(), "%s", year) + "-" +
                    String.format(Locale.getDefault(), "%s", Util.getTwoDigit(monthOfYear + 1)) + "-" +
                    String.format(Locale.getDefault(), "%s", Util.getTwoDigit(dayOfMonth));

            editText.setText(date);
            if (dateType.equalsIgnoreCase("StartDate")) {
                if (dayLeaveType == 0) {
                    btnEndDate.setEnabled(false);
                    btnEndDate.setText(btnStartDate.getText().toString());
                } else if (dayLeaveType == 1) {
                    btnEndDate.setEnabled(true);
                    btnEndDate.setText("");
                }
            } else if (dateType.equalsIgnoreCase("EndDate")) {
                if (!isDatesAreValid(btnStartDate.getText().toString(), btnEndDate.getText().toString())) {
                    btnEndDate.setText("");
                    Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                    .findViewById(android.R.id.content), getString(R.string.enter_proper_date_range),
                            Snackbar.LENGTH_LONG);
                }
            }
        }, mYear, mMonth, mDay);

        dateDialog.setTitle(context.getString(R.string.select_date_title));
        dateDialog.show();
    }

    @SuppressWarnings("SameParameterValue")
    private void showAlertDialog(String dialogTitle, String message, String btn1String, String
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
                closeCurrentActivity();
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
        dialog.show();
    }

    @SuppressLint("SimpleDateFormat")
    private boolean isDatesAreValid(String startDate, String endDate) {
        try {
            DateFormat formatter;
            Date fromDate, toDate;
            formatter = new SimpleDateFormat("yyyy-MM-dd");
            fromDate = formatter.parse(startDate);
            toDate = formatter.parse(endDate);

            if (fromDate.before(toDate) || fromDate.equals(toDate)) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
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

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onSuccessListener(String requestID, String response) {
        if (requestID.equalsIgnoreCase(LeavesPresenter.POST_USER_DETAILS)) {
            try {
                CommonResponse responseOBJ = new Gson().fromJson(response, CommonResponse.class);
                showAlertDialog("Status", responseOBJ.getMessage(), getString(R.string.ok), "");
            } catch (Exception e) {
                Log.e("TAG", "Exception");
            }
        } else if (requestID.equals(GET_LEAVE_BALANCE)) {
            LeaveBalanceResponse leaveBalanceResponse = PlatformGson.getPlatformGsonInstance().fromJson(response, LeaveBalanceResponse.class);
            if (leaveBalanceResponse.getStatus() == 200) {
                leaveBalance.clear();
                leaveBalance.addAll(leaveBalanceResponse.getData());

                //////////////////////////////////////////////
                for (LeaveDetail l : leaveBalance) {
                    leaveBackground.add(R.drawable.bg_rect_primary_border);
                }
                RecyclerView.LayoutManager mLayoutManagerLeave = new LinearLayoutManager(getActivity(),
                        LinearLayoutManager.HORIZONTAL, true);
                LeaveBalanceAdapter LeaveAdapter = new LeaveBalanceAdapter(
                        leaveBalance, "LeaveBalance");
                rvLeaveBalance.setLayoutManager(mLayoutManagerLeave);
                rvLeaveBalance.setAdapter(LeaveAdapter);

                if (applyType.equalsIgnoreCase("Leave")) {
                    mLayoutManagerLeaveCategory = new LinearLayoutManager(getActivity(),
                            LinearLayoutManager.HORIZONTAL, true);
                    LeaveAdapterCategory = new LeaveBalanceAdapter(this,
                            leaveBalance, leaveBackground, "Category");
                    rvLeaveCategory.setLayoutManager(mLayoutManagerLeaveCategory);
                    rvLeaveCategory.setAdapter(LeaveAdapterCategory);
                }
                //////////////////////////////////////////////

            } else {
                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                .findViewById(android.R.id.content), leaveBalanceResponse.getMessage(),
                        Snackbar.LENGTH_LONG);
            }

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
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }
}
