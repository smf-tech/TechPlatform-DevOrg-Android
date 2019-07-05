package com.platform.view.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.platform.R;
import com.platform.listeners.LeaveDataListener;
import com.platform.models.leaves.LeaveData;
import com.platform.models.leaves.LeaveDetail;
import com.platform.models.leaves.LeaveType;
import com.platform.models.leaves.UserLeaves;
import com.platform.presenter.LeavesPresenter;
import com.platform.utility.Constants;
import com.platform.utility.PlatformGson;
import com.platform.utility.PreferenceHelper;
import com.platform.utility.Util;
import com.platform.view.adapters.LeaveBalanceAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.platform.utility.Constants.DAY_MONTH_YEAR;
import static com.platform.utility.Util.getDateFromTimestamp;

public class LeaveApplyFragment extends Fragment implements View.OnClickListener, LeaveDataListener {

    /*private TextView tvCLSLLeavesCount;
    private TextView tvPaidLeavesCount;
    private TextView tvCOffLeavesCount;
    private TextView tvTotalLeavesCount;*/
    RecyclerView rvLeaveBalance;
    RecyclerView rvLeaveCategory;
    LeaveBalanceAdapter LeaveAdapterCategory;
    RecyclerView.LayoutManager mLayoutManagerLeaveCategory;
//    private Button btnCategoryCL;
//    private Button btnCategoryPaid;
//    private Button btnCategoryCompOff;

    private Button btnHalfDay;
    private Button btnFullDay;

    private EditText edtReason;
    private EditText btnStartDate;
    private EditText btnEndDate;
    private final Calendar c = Calendar.getInstance();
    private String leaveTypeSelected = null;
    private int dayLeaveType = -1;
    private LeavesPresenter presenter;
    private ArrayList<LeaveDetail> leaveBalance = new ArrayList<>();
    public static ArrayList<Integer> leaveBackground = new ArrayList<>();
    public String selectedLeaveCatgory;
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
        /*tvCLSLLeavesCount = view.findViewById(R.id.tv_leaves_cl);
        tvPaidLeavesCount = view.findViewById(R.id.tv_leaves_paid);
        tvCOffLeavesCount = view.findViewById(R.id.tv_leaves_com_off);
        tvTotalLeavesCount = view.findViewById(R.id.tv_total_leaves_count);*/

        rvLeaveBalance = view.findViewById(R.id.rv_leave_balance);
        rvLeaveCategory = view.findViewById(R.id.rv_leave_category);

        edtReason = view.findViewById(R.id.edt_reason);

        /*btnCategoryCL = view.findViewById(R.id.btn_cl);
        btnCategoryCL.setOnClickListener(this);
        btnCategoryPaid = view.findViewById(R.id.btn_paid);
        btnCategoryPaid.setOnClickListener(this);
        btnCategoryCompOff = view.findViewById(R.id.btn_comp_off);
        btnCategoryCompOff.setOnClickListener(this);*/

        btnHalfDay = view.findViewById(R.id.btn_half_day);
        btnHalfDay.setOnClickListener(this);
        btnFullDay = view.findViewById(R.id.btn_full_day);
        btnFullDay.setOnClickListener(this);

        btnStartDate = view.findViewById(R.id.btn_start_date);
        btnStartDate.setOnClickListener(this);
        btnEndDate = view.findViewById(R.id.btn_end_date);
        btnEndDate.setOnClickListener(this);

        Button btnApplyLeaves = view.findViewById(R.id.btn_apply_leave);
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
            boolean isEdit = bundle.getBoolean("isEdit");
            leaveBalance.clear();
            leaveBackground.clear();
            if(bundle.getSerializable("leaveBalance")!=null) {
                leaveBalance.addAll((ArrayList<LeaveDetail>)bundle.getSerializable("leaveBalance"));
                for (LeaveDetail l : leaveBalance) {
                    leaveBackground.add(R.drawable.leave_form_view_unfocused);
                }
                RecyclerView.LayoutManager mLayoutManagerLeave = new LinearLayoutManager(getActivity(),
                        LinearLayoutManager.HORIZONTAL, true);
                LeaveBalanceAdapter LeaveAdapter = new LeaveBalanceAdapter(
                        leaveBalance, "LeaveBalance");
                rvLeaveBalance.setLayoutManager(mLayoutManagerLeave);
                rvLeaveBalance.setAdapter(LeaveAdapter);

                mLayoutManagerLeaveCategory = new LinearLayoutManager(getActivity(),
                        LinearLayoutManager.HORIZONTAL, true);
                LeaveAdapterCategory = new LeaveBalanceAdapter(this,
                        leaveBalance, leaveBackground,"Category");
                rvLeaveCategory.setLayoutManager(mLayoutManagerLeaveCategory);
                rvLeaveCategory.setAdapter(LeaveAdapterCategory);
            }

            if (leaveDetail != null) {
                LeaveDetail leaveDetailModel = PlatformGson.getPlatformGsonInstance().fromJson(leaveDetail, LeaveDetail.class);
                if (leaveDetailModel != null) {

                    if (leaveDetailModel.getBalanceLeaves() != null) {
                        int totalBalanceLeaves = leaveDetailModel.getBalanceLeaves();
                        //tvTotalLeavesCount.setText(String.valueOf(totalBalanceLeaves));
                    }

                    List<LeaveType> leaveTypes = leaveDetailModel.getLeaveTypes();
                    if (leaveTypes != null) {
                        for (LeaveType type : leaveTypes) {
                            if (type.getLeaveType().equalsIgnoreCase(Constants.Planner.LEAVE_TYPE_CL)) {
//                                tvCLSLLeavesCount.setText(TextUtils.isEmpty(String.valueOf(type.getAllocatedLeaves()))
//                                        ? "0" : String.valueOf(type.getAllocatedLeaves()));
                            } else if (type.getLeaveType().equalsIgnoreCase(Constants.Planner.LEAVE_TYPE_PAID)) {
//                                tvPaidLeavesCount.setText(TextUtils.isEmpty(String.valueOf(type.getAllocatedLeaves()))
//                                        ? "0" : String.valueOf(type.getAllocatedLeaves()));
                            } else if (type.getLeaveType().equalsIgnoreCase(Constants.Planner.LEAVE_TYPE_COMP_OFF)) {
//                                tvCOffLeavesCount.setText(TextUtils.isEmpty(String.valueOf(type.getAllocatedLeaves()))
//                                        ? "0" : String.valueOf(type.getAllocatedLeaves()));
                            }
                        }
                    }
                }
            }

            if (isEdit) {
                LeaveData userLeaveDetail = (LeaveData) bundle.getSerializable("userLeaveDetails");
                if (userLeaveDetail != null) {
                    //LeaveData leaveDetailModel = PlatformGson.getPlatformGsonInstance().fromJson(userLeaveDetail, LeaveData.class);
                    if (userLeaveDetail != null) {
                        setUserDataForEdit(userLeaveDetail);
                    }
                }
            }
        }
    }

    private void setUserDataForEdit(LeaveData leaveDetailModel) {

        edtReason.setText(leaveDetailModel.getReason());
        btnStartDate.setText(getDateFromTimestamp(leaveDetailModel.getStartdate(), DAY_MONTH_YEAR));
        btnEndDate.setText(getDateFromTimestamp(leaveDetailModel.getEnddate(), DAY_MONTH_YEAR));

        String isHalfDay = leaveDetailModel.getFullHalfDay();
        if (isHalfDay.equalsIgnoreCase("half day")) {
            onClick(btnHalfDay);
        } else {
            onClick(btnFullDay);
        }

        String leaveCategory = leaveDetailModel.getLeaveType();
        for (int i = 0; i<leaveBalance.size(); i++) {
            if(leaveBalance.get(i).getType().equalsIgnoreCase(leaveCategory)){
                leaveBackground.remove(i);
                leaveBackground.add(i, R.drawable.leave_form_view_focused);
                selectedLeaveCatgory = leaveBalance.get(i).getType();
            }else{
//                leaveBackground.remove(i);
//                leaveBackground.add(i, R.drawable.leave_form_view_unfocused);
            }
        }
        LeaveAdapterCategory.notifyDataSetChanged();

//        LeaveType leaveType = (leaveDetailModel.getLeaveTypes() != null &&
//                leaveDetailModel.getLeaveTypes().size() > 0) ? leaveDetailModel.getLeaveTypes().get(0) : null;
//
//        if (leaveType != null) {
//            String type = leaveType.getLeaveType();
//            if (type.equalsIgnoreCase(Constants.Planner.LEAVE_TYPE_CL)) {
//                onClick(btnCategoryCL);
//            } else if (type.equalsIgnoreCase(Constants.Planner.LEAVE_TYPE_PAID)) {
//                onClick(btnCategoryPaid);
//            } else if (type.equalsIgnoreCase(Constants.Planner.LEAVE_TYPE_COMP_OFF)) {
//                onClick(btnCategoryCompOff);
//            }
//        }
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
            /*case R.id.btn_cl:
                leaveTypeSelected = btnCategoryCL.getText().toString();
                btnCategoryCL.setBackgroundResource(R.drawable.leave_form_view_focused);
                btnCategoryPaid.setBackgroundResource(R.drawable.leave_form_view_unfocused);
                btnCategoryCompOff.setBackgroundResource(R.drawable.leave_form_view_unfocused);
                break;

            case R.id.btn_paid:
                leaveTypeSelected = btnCategoryPaid.getText().toString();
                btnCategoryCL.setBackgroundResource(R.drawable.leave_form_view_unfocused);
                btnCategoryPaid.setBackgroundResource(R.drawable.leave_form_view_focused);
                btnCategoryCompOff.setBackgroundResource(R.drawable.leave_form_view_unfocused);
                break;

            case R.id.btn_comp_off:
                leaveTypeSelected = btnCategoryCompOff.getText().toString();
                btnCategoryCL.setBackgroundResource(R.drawable.leave_form_view_unfocused);
                btnCategoryPaid.setBackgroundResource(R.drawable.leave_form_view_unfocused);
                btnCategoryCompOff.setBackgroundResource(R.drawable.leave_form_view_focused);
                break;*/

            case R.id.btn_half_day:
                dayLeaveType = 0;
                btnHalfDay.setBackgroundResource(R.drawable.leave_form_view_focused);
                btnFullDay.setBackgroundResource(R.drawable.leave_form_view_unfocused);
                break;

            case R.id.btn_full_day:
                dayLeaveType = 1;
                btnHalfDay.setBackgroundResource(R.drawable.leave_form_view_unfocused);
                btnFullDay.setBackgroundResource(R.drawable.leave_form_view_focused);
                break;

            case R.id.btn_apply_leave:
                applyForLeave();
                break;

            case R.id.btn_start_date:
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

//                DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()),
//                        (datePicker, year12, month12, day12) -> {
//                            Calendar calendar = Calendar.getInstance();
//                            calendar.set(year12, month12, day12);
//                            SimpleDateFormat format = new SimpleDateFormat(Constants.DAY_MONTH_YEAR, Locale.getDefault());
//                            btnStartDate.setText(format.format(calendar.getTime()));
//
//                        }, year, month, day);
//                datePickerDialog.show();
                Util.showDateDialogMin(getActivity(), btnStartDate);
                break;

            case R.id.btn_end_date:
//                int year1 = c.get(Calendar.YEAR);
//                int month1 = c.get(Calendar.MONTH);
//                int day1 = c.get(Calendar.DAY_OF_MONTH);
//
//                DatePickerDialog endDatePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()),
//                        (datePicker, year2, month2, day2) -> {
//
//                            Calendar calendar = Calendar.getInstance();
//                            calendar.set(year2, month2, day2);
//                            SimpleDateFormat format = new SimpleDateFormat(Constants.DAY_MONTH_YEAR, Locale.getDefault());
//                            btnEndDate.setText(format.format(calendar.getTime()));
//
//                        }, year1, month1, day1);
//
//                endDatePickerDialog.show();
                Util.showDateDialogMin(getActivity(), btnEndDate);
                break;
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void applyForLeave() {
        if (leaveTypeSelected != null && dayLeaveType != -1 && !TextUtils.isEmpty(btnStartDate.getText().toString())
                && !TextUtils.isEmpty(btnEndDate.getText().toString())) {
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DAY_MONTH_YEAR, Locale.getDefault());
        Date startDate = null;
        Date endDate = null;

        JsonObject jsonData = new JsonObject();
        jsonData.addProperty("userId", "12345");
        try {
            startDate = sdf.parse(btnStartDate.getText().toString());
        } catch (ParseException e) {
            Log.e("TAG", "ParseException");
        }
        try {
            endDate = sdf.parse(btnEndDate.getText().toString());
        } catch (ParseException e) {
            Log.e("TAG", "ParseException");
        }
//        jsonData.addProperty("fromDate", startDate != null ? startDate.toString() : btnStartDate.getText().toString());
//        jsonData.addProperty("toDate", endDate != null ? endDate.toString() : btnEndDate.getText().toString());
//        jsonData.addProperty("isHalfDay", "12345");
//        jsonData.addProperty("reason", edtReason.getText().toString());
//        jsonData.addProperty("numberOfDays", (endDate != null && startDate != null) ? endDate.getTime() - startDate.getTime() : 0);
//        JsonObject leave = new JsonObject();
//        leave.addProperty("leaveType", leaveTypeSelected);
//        leave.addProperty("allocatedLeaves", 5);
//        JsonArray jsonArray = new JsonArray();
//        jsonArray.add(leave);
//        jsonData.add("leaveTypes", jsonArray);
//        // jsonData.addProperty("status","pending");

        LeaveData leaveData = new LeaveData();
        leaveData.setUserId(Util.getUserObjectFromPref().getId());
        leaveData.setLeaveType(selectedLeaveCatgory);
        leaveData.setStartdate(Util.dateTimeToTimeStamp(btnStartDate.getText().toString(),"00:00"));

        leaveData.setEnddate(Util.dateTimeToTimeStamp(btnEndDate.getText().toString(),"00:00"));
        if(dayLeaveType==0){
            leaveData.setFullHalfDay("half day");
        }else if(dayLeaveType==1){
            leaveData.setFullHalfDay("full day");
        }
        leaveData.setReason(edtReason.getText().toString());

        presenter.postUserLeave(leaveData);
    }

    @SuppressWarnings("deprecation")
    private String getCurrentDateInSpecificFormat(String currentCalDate, boolean isStart) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DAY_MONTH_YEAR, Locale.getDefault());
        Date d = null;
        try {
            d = sdf.parse(currentCalDate);
            String dayNumberSuffix = getDayNumberSuffix(d.getDate());
            final String NEW_FORMAT = isStart ? " d'" + dayNumberSuffix + "' MMMM " : " d'" + dayNumberSuffix + "' MMMM yyyy";

            sdf.applyPattern(NEW_FORMAT);

        } catch (ParseException e) {
            Log.e("TAG", e.getMessage());
        }

        return sdf.format(d);
    }

    private String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }

        switch (day % 10) {
            case 1:
                return "st";

            case 2:
                return "nd";

            case 3:
                return "rd";

            default:
                return "th";
        }
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
        dialog.show();      // if decline button is clicked, close the custom dialog
    }

    @Override
    public void onFailureListener(String requestID,String message) {
        if (getActivity() != null) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), message,
                    Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void onErrorListener(String requestID,VolleyError error) {
        if (getActivity() != null) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), error.getMessage(),
                    Snackbar.LENGTH_LONG);
        }
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onSuccessListener(String requestID,String response) {
        if(requestID.equalsIgnoreCase(LeavesPresenter.POST_USER_DETAILS)) {
            try {
                showAlertDialog(getString(R.string.leave_apply_msg, leaveTypeSelected,
                        getCurrentDateInSpecificFormat(btnStartDate.getText().toString(), true),
                        getCurrentDateInSpecificFormat(btnEndDate.getText().toString(), false)),
                        getString(R.string.leave_apply_msg1), getString(R.string.ok), "");
            } catch (Exception e) {
                Log.e("TAG", "Exception");
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
