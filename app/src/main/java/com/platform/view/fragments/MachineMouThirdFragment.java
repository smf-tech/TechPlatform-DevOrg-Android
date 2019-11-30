package com.platform.view.fragments;

import android.content.Context;
import android.net.Uri;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;
import com.platform.R;
import com.platform.models.SujalamSuphalam.MouDetails;
import com.platform.models.SujalamSuphalam.RateDetail;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.MachineMouActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.platform.utility.Constants.DAY_MONTH_YEAR;

public class MachineMouThirdFragment extends Fragment implements View.OnClickListener {
    private View machineMouThirdFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private Button btnThirdPartMou, btnPreviousMou;
    private EditText edtContractDate, edtMouExpiryDate, edtRate1, edtRate1StartDate, edtRate1EndDate, edtRate2,
            edtRate2StartDate, edtRate2EndDate, edtRate3, edtRate3StartDate, edtRate3EndDate;
    private int statusCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        machineMouThirdFragmentView = inflater.inflate(R.layout.fragment_machine_mou_third, container, false);
        return machineMouThirdFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        if(((MachineMouActivity) getActivity()).getMachineDetailData().
                getMachine().getOwnedBy().equalsIgnoreCase("BJS")) {
            MouDetails mouDetails = new MouDetails();
            ((MachineMouActivity) getActivity()).getMachineDetailData().setMouDetails(mouDetails);
            Date d = new Date();
            ((MachineMouActivity) getActivity()).getMachineDetailData().getMouDetails().setDateOfSigning(d.getTime());
            ((MachineMouActivity) getActivity()).getMachineDetailData().getMouDetails().setDateOfMouExpiry
                    (Util.dateTimeToTimeStamp("2099-12-31", "23:59"));

            ((MachineMouActivity) getActivity()).openFragment("MachineMouFourthFragment");
        } else {
            statusCode = getActivity().getIntent().getIntExtra("statusCode",0);
            progressBarLayout = machineMouThirdFragmentView.findViewById(R.id.profile_act_progress_bar);
            progressBar = machineMouThirdFragmentView.findViewById(R.id.pb_profile_act);
            btnThirdPartMou = machineMouThirdFragmentView.findViewById(R.id.btn_third_part_mou);
            btnThirdPartMou.setOnClickListener(this);
            btnPreviousMou = machineMouThirdFragmentView.findViewById(R.id.btn_previous_mou);
            btnPreviousMou.setOnClickListener(this);
            edtContractDate = machineMouThirdFragmentView.findViewById(R.id.edt_contract_date);
            edtContractDate.setOnClickListener(this);
            edtMouExpiryDate = machineMouThirdFragmentView.findViewById(R.id.edt_mou_expiry_date);
            edtMouExpiryDate.setOnClickListener(this);
            edtRate1 = machineMouThirdFragmentView.findViewById(R.id.edt_rate1);
            edtRate1StartDate = machineMouThirdFragmentView.findViewById(R.id.edt_rate1_start_date);
            edtRate1StartDate.setOnClickListener(this);
            edtRate1EndDate = machineMouThirdFragmentView.findViewById(R.id.edt_rate1_end_date);
            edtRate1EndDate.setOnClickListener(this);
            edtRate2 = machineMouThirdFragmentView.findViewById(R.id.edt_rate2);
            edtRate2StartDate = machineMouThirdFragmentView.findViewById(R.id.edt_rate2_start_date);
            edtRate2StartDate.setOnClickListener(this);
            edtRate2EndDate = machineMouThirdFragmentView.findViewById(R.id.edt_rate2_end_date);
            edtRate2EndDate.setOnClickListener(this);
            edtRate3 = machineMouThirdFragmentView.findViewById(R.id.edt_rate3);
            edtRate3StartDate = machineMouThirdFragmentView.findViewById(R.id.edt_rate3_start_date);
            edtRate3StartDate.setOnClickListener(this);
            edtRate3EndDate = machineMouThirdFragmentView.findViewById(R.id.edt_rate3_end_date);
            edtRate3EndDate.setOnClickListener(this);
            if(((MachineMouActivity) getActivity()).getMachineDetailData().
                    getMouDetails()!=null) {
                setUIvalues();
            }
        }
    }

    private void setUIvalues() {
        edtContractDate.setText(Util.getDateFromTimestamp(((MachineMouActivity) getActivity()).getMachineDetailData().
                getMouDetails().getDateOfSigning(), DAY_MONTH_YEAR));
        edtMouExpiryDate.setText(Util.getDateFromTimestamp(((MachineMouActivity) getActivity()).getMachineDetailData().
                getMouDetails().getDateOfMouExpiry(), DAY_MONTH_YEAR));
        if(!((MachineMouActivity) getActivity()).getMachineDetailData().
                getMachine().getOwnedBy().equalsIgnoreCase("BJS")) {
            List<RateDetail> rateDetailsList = ((MachineMouActivity) getActivity()).getMachineDetailData().
                    getMouDetails().getRateDetails();
            edtRate1StartDate.setText(Util.getDateFromTimestamp(rateDetailsList.get(0).getFromDate(), DAY_MONTH_YEAR));
            edtRate1EndDate.setText(Util.getDateFromTimestamp(rateDetailsList.get(0).getToDate(), DAY_MONTH_YEAR));
            edtRate1.setText(rateDetailsList.get(0).getValue());
            edtRate2StartDate.setText(Util.getDateFromTimestamp(rateDetailsList.get(1).getFromDate(), DAY_MONTH_YEAR));
            edtRate2EndDate.setText(Util.getDateFromTimestamp(rateDetailsList.get(1).getToDate(), DAY_MONTH_YEAR));
            edtRate2.setText(rateDetailsList.get(1).getValue());
            edtRate3StartDate.setText(Util.getDateFromTimestamp(rateDetailsList.get(2).getFromDate(), DAY_MONTH_YEAR));
            edtRate3EndDate.setText(Util.getDateFromTimestamp(rateDetailsList.get(2).getToDate(), DAY_MONTH_YEAR));
            edtRate3.setText(rateDetailsList.get(2).getValue());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.edt_contract_date:
                Util.showAllDateDialog(getActivity(), edtContractDate);
                break;
            case R.id.edt_mou_expiry_date:
                if(edtContractDate.getText().toString().length()>0) {
                    Util.showDateDialogEnableAfterMin(getActivity(), edtMouExpiryDate,
                            edtContractDate.getText().toString());
                } else  {
                    Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                    .findViewById(android.R.id.content), getString(R.string.enter_mou_start_date),
                            Snackbar.LENGTH_LONG);
                }
                break;
            case R.id.edt_rate1_start_date:
                if(edtContractDate.getText().toString().length()>0) {
                    if(edtMouExpiryDate.getText().toString().length()>0) {
                        Util.showDateDialogEnableBetweenMinMax(getActivity(), edtRate1StartDate,
                                edtContractDate.getText().toString(), edtMouExpiryDate.getText().toString());
                    } else {
                        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                        .findViewById(android.R.id.content), getString(R.string.enter_expiry_date),
                                Snackbar.LENGTH_LONG);
                    }
                } else {
                    Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                    .findViewById(android.R.id.content), getString(R.string.enter_mou_start_date),
                            Snackbar.LENGTH_LONG);
                }
                break;
            case R.id.edt_rate1_end_date:
                if(edtRate1StartDate.getText().toString().length()>0) {
                    if(edtMouExpiryDate.getText().toString().length()>0) {
                        Util.showDateDialogEnableBetweenMinMax(getActivity(), edtRate1EndDate,
                                edtRate1StartDate.getText().toString(), edtMouExpiryDate.getText().toString());
                    } else {
                        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                        .findViewById(android.R.id.content), getString(R.string.enter_expiry_date),
                                Snackbar.LENGTH_LONG);
                    }
                } else  {
                    Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                    .findViewById(android.R.id.content), getString(R.string.enter_rate1_start_date),
                            Snackbar.LENGTH_LONG);
                }
                break;
            case R.id.edt_rate2_start_date:
                if(edtRate1EndDate.getText().toString().length()>0) {
                    if(edtMouExpiryDate.getText().toString().length()>0) {
                        Util.showDateDialogEnableBetweenMinMax(getActivity(), edtRate2StartDate,
                                edtRate1EndDate.getText().toString(), edtMouExpiryDate.getText().toString());
                    } else {
                        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                        .findViewById(android.R.id.content), getString(R.string.enter_expiry_date),
                                Snackbar.LENGTH_LONG);
                    }

                } else  {
                    Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                    .findViewById(android.R.id.content), getString(R.string.enter_rate1_end_date),
                            Snackbar.LENGTH_LONG);
                }
                break;
            case R.id.edt_rate2_end_date:
                if(edtRate2StartDate.getText().toString().length()>0) {
                    if(edtMouExpiryDate.getText().toString().length()>0) {
                        Util.showDateDialogEnableBetweenMinMax(getActivity(), edtRate2EndDate,
                                edtRate2StartDate.getText().toString(), edtMouExpiryDate.getText().toString());
                    } else {
                        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                        .findViewById(android.R.id.content), getString(R.string.enter_expiry_date),
                                Snackbar.LENGTH_LONG);
                    }
                } else  {
                    Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                    .findViewById(android.R.id.content), getString(R.string.enter_rate2_start_date),
                            Snackbar.LENGTH_LONG);
                }
                break;
            case R.id.edt_rate3_start_date:
                if(edtRate2EndDate.getText().toString().length()>0) {
                    if(edtMouExpiryDate.getText().toString().length()>0) {
                        Util.showDateDialogEnableBetweenMinMax(getActivity(), edtRate3StartDate,
                                edtRate2EndDate.getText().toString(), edtMouExpiryDate.getText().toString());
                    } else {
                        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                        .findViewById(android.R.id.content), getString(R.string.enter_expiry_date),
                                Snackbar.LENGTH_LONG);
                    }
                } else  {
                    Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                    .findViewById(android.R.id.content), getString(R.string.enter_rate2_start_date),
                            Snackbar.LENGTH_LONG);
                }
                break;
            case R.id.edt_rate3_end_date:
                if(edtRate3StartDate.getText().toString().length()>0) {
                    if(edtMouExpiryDate.getText().toString().length()>0) {
                        Util.showDateDialogEnableBetweenMinMax(getActivity(), edtRate3EndDate,
                                edtRate3StartDate.getText().toString(), edtMouExpiryDate.getText().toString());
                    } else {
                        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                        .findViewById(android.R.id.content), getString(R.string.enter_expiry_date),
                                Snackbar.LENGTH_LONG);
                    }
                } else  {
                    Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                    .findViewById(android.R.id.content), getString(R.string.enter_rate3_start_date),
                            Snackbar.LENGTH_LONG);
                }
                break;
            case R.id.btn_third_part_mou:
                if(isAllDataValid()){
                    setMachineThirdData();
                    ((MachineMouActivity) getActivity()).openFragment("MachineMouFourthFragment");
                }
                break;
            case R.id.btn_previous_mou:
                setMachineThirdData();
                getActivity().onBackPressed();
                break;
        }
    }

    private void setMachineThirdData() {
        MouDetails mouDetails = new MouDetails();
        ((MachineMouActivity) getActivity()).getMachineDetailData().setMouDetails(mouDetails);
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMouDetails().setDateOfSigning
                (Util.dateTimeToTimeStamp(edtContractDate.getText().toString(), "00:00"));
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMouDetails().setDateOfMouExpiry
                (Util.dateTimeToTimeStamp(edtMouExpiryDate.getText().toString(), "23:59"));
        RateDetail rateDetail = new RateDetail();
        rateDetail.setFromDate(Util.dateTimeToTimeStamp(edtRate1StartDate.getText().toString(), "00:00"));
        rateDetail.setToDate(Util.dateTimeToTimeStamp(edtRate1EndDate.getText().toString(), "23:59"));
        rateDetail.setValue(edtRate1.getText().toString().trim());
        RateDetail rateDetail1 = new RateDetail();
        rateDetail1.setFromDate(Util.dateTimeToTimeStamp(edtRate2StartDate.getText().toString(), "00:00"));
        rateDetail1.setToDate(Util.dateTimeToTimeStamp(edtRate2EndDate.getText().toString(), "23:59"));
        rateDetail1.setValue(edtRate2.getText().toString().trim());
        RateDetail rateDetail2 = new RateDetail();
        rateDetail2.setFromDate(Util.dateTimeToTimeStamp(edtRate3StartDate.getText().toString(), "00:00"));
        rateDetail2.setToDate(Util.dateTimeToTimeStamp(edtRate3EndDate.getText().toString(), "23:59"));
        rateDetail2.setValue(edtRate3.getText().toString().trim());
        List<RateDetail> rateDetailsList = new ArrayList();
        rateDetailsList.add(rateDetail);
        rateDetailsList.add(rateDetail1);
        rateDetailsList.add(rateDetail2);
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMouDetails().setRateDetails(rateDetailsList);
//        List mouList = new ArrayList();
//        mouList.add("www.google.com");
//        ((MachineMouActivity) getActivity()).getMachineDetailData().getMouDetails().setMOUImages(mouList);
        //((MachineMouActivity) getActivity()).getMachineDetailData().getMouDetails().setIsMOUCancelled("NO");
    }

    public boolean isAllDataValid() {
        if (TextUtils.isEmpty(edtContractDate.getText().toString().trim())){
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                    getString(R.string.select_contract_date), Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(edtRate1.getText().toString().trim())){
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                    getString(R.string.enter_rate1_value), Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(edtRate1StartDate.getText().toString().trim())){
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                    getString(R.string.enter_rate1_start_date), Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(edtRate1EndDate.getText().toString().trim())){
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                    getString(R.string.enter_rate1_end_date), Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(edtRate2.getText().toString().trim())){
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                    getString(R.string.enter_rate2_value), Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(edtRate2StartDate.getText().toString().trim())){
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                    getString(R.string.enter_rate2_start_date), Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(edtRate2EndDate.getText().toString().trim())){
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                    getString(R.string.enter_rate2_end_date), Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(edtRate3.getText().toString().trim())){
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                    getString(R.string.enter_rate3_value), Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(edtRate3StartDate.getText().toString().trim())){
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                    getString(R.string.enter_rate3_start_date), Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(edtRate3EndDate.getText().toString().trim())) {
        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                getString(R.string.enter_rate3_end_date), Snackbar.LENGTH_LONG);
            return false;
        }
        return true;
    }
}
