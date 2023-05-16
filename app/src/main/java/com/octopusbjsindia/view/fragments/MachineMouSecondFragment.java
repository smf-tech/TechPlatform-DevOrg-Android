package com.octopusbjsindia.view.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataList;
import com.octopusbjsindia.models.SujalamSuphalam.MouDetails;
import com.octopusbjsindia.models.SujalamSuphalam.ProviderInformation;
import com.octopusbjsindia.models.SujalamSuphalam.SSMasterDatabase;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.MachineMouActivity;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;
import com.octopusbjsindia.view.customs.TextViewSemiBold;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class MachineMouSecondFragment extends Fragment implements View.OnClickListener, CustomSpinnerListener {
    private View machineMouSecondFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private Button btnSecondPartMou, btnPreviousMou;
    private EditText etProviderFirstName, etProviderLastName, etProviderContact,
            etOperatorFName, etOperatorLName, etOperatorMobile;
    //etSupervisorFName, etSupervisorLName ,etMachineMobile,

//            etOwnership, etTradeName, etTurnover, etGstRegNo, etPanNo, etBankName, etBranch, etIfsc,
//            etAccountNo, etConfirmAccountNo, etAccountHolderName, etAccountType;
    //private ImageView imgAccount;
    //private TextViewSemiBold eventPicLabel;
    //private String selectedOwnership, selectedOwnershipId, isTurnoverBelow, selectedAccountType;
//    private ArrayList<CustomSpinnerObject> ownershipList = new ArrayList<>();
//    private ArrayList<CustomSpinnerObject> isTurnOverAboveList = new ArrayList<>();
//    private ArrayList<CustomSpinnerObject> accountTypesList = new ArrayList<>();
//    private Uri outputUri;
//    private Uri finalUri;
    private final String TAG = MachineMouSecondFragment.class.getName();
    //private File imageFile;
    private int statusCode;
    //private boolean isBJSMachine;
    //private int imgCount = 0;
    //private String currentPhotoPath = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        machineMouSecondFragmentView = inflater.inflate(R.layout.fragment_machine_mou_second, container, false);
        return machineMouSecondFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        statusCode = getActivity().getIntent().getIntExtra("statusCode",0);
        progressBarLayout = machineMouSecondFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = machineMouSecondFragmentView.findViewById(R.id.pb_profile_act);
        btnSecondPartMou = machineMouSecondFragmentView.findViewById(R.id.btn_second_part_mou);
        btnSecondPartMou.setOnClickListener(this);
        btnPreviousMou = machineMouSecondFragmentView.findViewById(R.id.btn_previous_mou);
        btnPreviousMou.setOnClickListener(this);
        etProviderFirstName = machineMouSecondFragmentView.findViewById(R.id.et_provider_first_name);
        etProviderLastName = machineMouSecondFragmentView.findViewById(R.id.et_provider_last_name);
        etProviderContact = machineMouSecondFragmentView.findViewById(R.id.et_provider_contact);
//        etSupervisorFName = machineMouSecondFragmentView.findViewById(R.id.et_supervisor_first_name);
//        etSupervisorLName = machineMouSecondFragmentView.findViewById(R.id.et_supervisor_last_name);
//        etMachineMobile = machineMouSecondFragmentView.findViewById(R.id.et_machine_mobile);
        etOperatorFName = machineMouSecondFragmentView.findViewById(R.id.et_operator_first_name);
        etOperatorLName = machineMouSecondFragmentView.findViewById(R.id.et_operator_last_name);
        etOperatorMobile = machineMouSecondFragmentView.findViewById(R.id.et_operator_mobile);

        //etOwnership = machineMouSecondFragmentView.findViewById(R.id.et_ownership);
//        etTradeName = machineMouSecondFragmentView.findViewById(R.id.et_trade_name);
//        etTurnover = machineMouSecondFragmentView.findViewById(R.id.et_turnover);
//        etGstRegNo = machineMouSecondFragmentView.findViewById(R.id.et_gst_reg_no);
//        etPanNo = machineMouSecondFragmentView.findViewById(R.id.et_pan_no);
//        etBankName = machineMouSecondFragmentView.findViewById(R.id.et_bank_name);
//        etBranch = machineMouSecondFragmentView.findViewById(R.id.et_branch);
//        etIfsc = machineMouSecondFragmentView.findViewById(R.id.et_ifsc);
//        etAccountNo = machineMouSecondFragmentView.findViewById(R.id.et_account_no);
//        etConfirmAccountNo = machineMouSecondFragmentView.findViewById(R.id.et_confirm_account_no);
//        imgAccount = machineMouSecondFragmentView.findViewById(R.id.img_account);
        //eventPicLabel = machineMouSecondFragmentView.findViewById(R.id.event_pic_label);
//        etAccountHolderName = machineMouSecondFragmentView.findViewById(R.id.et_account_holder_name);
//        etAccountType = machineMouSecondFragmentView.findViewById(R.id.et_account_type);
//        if(((MachineMouActivity) getActivity()).getMachineDetailData().
//                getMachine().getOwnedBy().equalsIgnoreCase("BJS")) {
//            isBJSMachine = true;
//        }
       // if(isBJSMachine) {

//            etOwnership.setVisibility(View.GONE);
//            etTradeName.setVisibility(View.GONE);
//            etTurnover.setVisibility(View.GONE);
//            etGstRegNo.setVisibility(View.GONE);
//            etPanNo.setVisibility(View.GONE);
//            etBankName.setVisibility(View.GONE);
//            etBranch.setVisibility(View.GONE);
//            etIfsc.setVisibility(View.GONE);
//            etAccountNo.setVisibility(View.GONE);
//            etConfirmAccountNo.setVisibility(View.GONE);
//            imgAccount.setVisibility(View.GONE);
//            eventPicLabel.setVisibility(View.GONE);
//            etAccountHolderName.setVisibility(View.GONE);
//            etAccountType.setVisibility(View.GONE);
//         } else {
//            etOwnership.setOnClickListener(this);
//            etTurnover.setOnClickListener(this);
//            etAccountType.setOnClickListener(this);
//            imgAccount.setOnClickListener(this);

//            List<SSMasterDatabase> list = DatabaseManager.getDBInstance(Platform.getInstance()).
//                    getSSMasterDatabaseDao().getSSMasterData("SS");
//            String masterDbString = list.get(0).getData();
//
//            Gson gson = new Gson();
//            TypeToken<ArrayList<MasterDataList>> token = new TypeToken<ArrayList<MasterDataList>>() {};
//            ArrayList<MasterDataList> masterDataList = gson.fromJson(masterDbString, token.getType());
//
//            for(int i = 0; i<masterDataList.size(); i++) {
//                if(masterDataList.get(i).getForm().equals("machine_mou") && masterDataList.get(i).
//                        getField().equals("ownerType")) {
//                    for(int j = 0; j<masterDataList.get(i).getData().size(); j++) {
//                        CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
//                        customSpinnerObject.setName(masterDataList.get(i).getData().get(j).getValue());
//                        customSpinnerObject.set_id(masterDataList.get(i).getData().get(j).getId());
//                        customSpinnerObject.setSelected(false);
//                        ownershipList.add(customSpinnerObject);
//                    }
//                }
//            }
//            CustomSpinnerObject turnoverYes = new CustomSpinnerObject();
//            turnoverYes.setName("Yes");
//            turnoverYes.set_id("1");
//            turnoverYes.setSelected(false);
//            isTurnOverAboveList.add(turnoverYes);
//
//            CustomSpinnerObject turnoverNo = new CustomSpinnerObject();
//            turnoverNo.setName("No");
//            turnoverNo.set_id("2");
//            turnoverNo.setSelected(false);
//            isTurnOverAboveList.add(turnoverNo);
//
//            CustomSpinnerObject accountSavings = new CustomSpinnerObject();
//            accountSavings.setName("Savings Account");
//            accountSavings.set_id("1");
//            accountSavings.setSelected(false);
//            accountTypesList.add(accountSavings);
//
//            CustomSpinnerObject accountCurrent = new CustomSpinnerObject();
//            accountCurrent.setName("Current Account");
//            accountCurrent.set_id("2");
//            accountCurrent.setSelected(false);
//            accountTypesList.add(accountCurrent);
        //}

        etProviderContact.setText(((MachineMouActivity) getActivity()).getMachineDetailData().
                getMachine().getProviderContactNumber());
        if(statusCode == Constants.SSModule.MACHINE_MOU_EXPIRED_STATUS_CODE) {
            setUIForMouUpdate();
        }
        if(((MachineMouActivity) getActivity()).getMachineDetailData().
                getProviderInformation()!=null) {
            setUIForMouUpdate();
        }
    }

    private void setUIForMouUpdate() {
        etProviderFirstName.setText(((MachineMouActivity) getActivity()).getMachineDetailData().
                getProviderInformation().getFirstName());
        etProviderFirstName.setFocusable(false);
        etProviderFirstName.setLongClickable(false);
        etProviderLastName.setText(((MachineMouActivity) getActivity()).getMachineDetailData().
                getProviderInformation().getLastName());
        etProviderLastName.setFocusable(false);
        etProviderLastName.setLongClickable(false);

//        etSupervisorFName.setText(((MachineMouActivity) getActivity()).getMachineDetailData().
//                getProviderInformation().getFirstName());
//        etSupervisorFName.setFocusable(false);
//        etSupervisorFName.setLongClickable(false);
//        etSupervisorLName.setText(((MachineMouActivity) getActivity()).getMachineDetailData().
//                getProviderInformation().getLastName());
//        etSupervisorLName.setFocusable(false);
//        etSupervisorLName.setLongClickable(false);
//        etMachineMobile.setText(((MachineMouActivity) getActivity()).getMachineDetailData().
//                getMachine().getMachineMobileNumber());

        etOperatorFName.setText(((MachineMouActivity) getActivity()).getMachineDetailData().
                getProviderInformation().getFirstName());
        etOperatorFName.setFocusable(false);
        etOperatorFName.setLongClickable(false);
        etOperatorLName.setText(((MachineMouActivity) getActivity()).getMachineDetailData().
                getProviderInformation().getLastName());
        etOperatorLName.setFocusable(false);
        etOperatorLName.setLongClickable(false);
        etOperatorMobile.setText(((MachineMouActivity) getActivity()).getMachineDetailData().
                getMachine().getMachineMobileNumber());

//        if(((MachineMouActivity) getActivity()).chequeImageUri!= null) {
//            imgAccount.setImageURI(((MachineMouActivity) getActivity()).chequeImageUri);
//        }
//        if(!isBJSMachine) {
//            isTurnoverBelow = ((MachineMouActivity) getActivity()).getMachineDetailData().
//                    getProviderInformation().getIsTurnover();
//            etTurnover.setText(isTurnoverBelow);
//            if(((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().
//                    getOwnership()!= null && ((MachineMouActivity) getActivity()).getMachineDetailData().
//                    getProviderInformation().getOwnership().length()>0) {
//                for (CustomSpinnerObject ownershipType : ownershipList) {
//                        if(((MachineMouActivity) getActivity()).getMachineDetailData().
//                                getProviderInformation().getOwnership().equalsIgnoreCase(ownershipType.get_id())) {
//                            selectedOwnership = ownershipType.getName();
//                            selectedOwnershipId = ownershipType.get_id();
//                            break;
//                        }
//                }
//                etOwnership.setVisibility(View.VISIBLE);
//                etOwnership.setText(selectedOwnership);
//            }
//            if(((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().
//                    getTradeName()!= null && ((MachineMouActivity) getActivity()).getMachineDetailData().
//                    getProviderInformation().getTradeName().length()>0) {
//                etTradeName.setVisibility(View.VISIBLE);
//                etTradeName.setText(((MachineMouActivity) getActivity()).getMachineDetailData().
//                        getProviderInformation().getTradeName());
//            } else {
//                //etTradeName.setVisibility(View.GONE);
//            }
//            etGstRegNo.setText(((MachineMouActivity) getActivity()).getMachineDetailData().
//                    getProviderInformation().getGSTNumber());
//            etPanNo.setText(((MachineMouActivity) getActivity()).getMachineDetailData().
//                    getProviderInformation().getPANNumber());
//            etBankName.setText(((MachineMouActivity) getActivity()).getMachineDetailData().
//                    getProviderInformation().getBankName());
//            etIfsc.setText(((MachineMouActivity) getActivity()).getMachineDetailData().
//                    getProviderInformation().getIFSC());
//            etBranch.setText(((MachineMouActivity) getActivity()).getMachineDetailData().
//                    getProviderInformation().getBranch());
//            etAccountNo.setText(((MachineMouActivity) getActivity()).getMachineDetailData().
//                    getProviderInformation().getAccountNo());
//            etConfirmAccountNo.setText(((MachineMouActivity) getActivity()).getMachineDetailData().
//                    getProviderInformation().getAccountNo());
//            etAccountHolderName.setText(((MachineMouActivity) getActivity()).getMachineDetailData().
//                    getProviderInformation().getAccountName());
//            etAccountType.setText(((MachineMouActivity) getActivity()).getMachineDetailData().
//                    getProviderInformation().getAccountType());
//        }
    }
    public boolean isAllDataValid() {
        if (TextUtils.isEmpty(etProviderFirstName.getText().toString().trim())){
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                    getString(R.string.enter_provider_first_name), Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etProviderLastName.getText().toString().trim())){
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                    getString(R.string.enter_provider_last_name), Snackbar.LENGTH_LONG);
            return false;
        }
//        else if (TextUtils.isEmpty(etProviderContact.getText().toString().trim())){
//            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
//                    getString(R.string.enter_provider_contact), Snackbar.LENGTH_LONG);
//            return false;
//        }
//        else if (TextUtils.isEmpty(etSupervisorFName.getText().toString().trim())){
//            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
//                    getString(R.string.enter_supervisor_first_name), Snackbar.LENGTH_LONG);
//            return false;
//        } else if (TextUtils.isEmpty(etSupervisorLName.getText().toString().trim())){
//            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
//                    getString(R.string.enter_supervisor_last_name), Snackbar.LENGTH_LONG);
//            return false;
//        } else if (etMachineMobile.getText().toString().trim().length() != 10){
//            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
//                    getString(R.string.enter_proper_machine_mobile), Snackbar.LENGTH_LONG);
//            return false;
//        }
        else if (TextUtils.isEmpty(etOperatorFName.getText().toString().trim())){
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                    getString(R.string.enter_operator_first_name), Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etOperatorLName.getText().toString().trim())){
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                    getString(R.string.enter_operator_last_name), Snackbar.LENGTH_LONG);
            return false;
        } else if (etOperatorMobile.getText().toString().trim().length() != 10){
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                    getString(R.string.enter_proper_operator_contact), Snackbar.LENGTH_LONG);
            return false;
        }
        //if(!isBJSMachine) {

//            if (selectedOwnershipId == null){
//                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
//                        getString(R.string.select_ownership_field), Snackbar.LENGTH_LONG);
//                return false;
//            } else if (TextUtils.isEmpty(etGstRegNo.getText().toString().trim())){
//                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
//                        getString(R.string.enter_gst_no), Snackbar.LENGTH_LONG);
//                return false;
//            } else if (TextUtils.isEmpty(etPanNo.getText().toString().trim())){
//                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
//                        getString(R.string.enter_pan_no), Snackbar.LENGTH_LONG);
//                return false;
//            } else if (TextUtils.isEmpty(etBankName.getText().toString().trim())){
//                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
//                        getString(R.string.enter_bank_name), Snackbar.LENGTH_LONG);
//                return false;
//            } else if (TextUtils.isEmpty(etBranch.getText().toString().trim())){
//                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
//                        getString(R.string.enter_branch), Snackbar.LENGTH_LONG);
//                return false;
//            } else if (TextUtils.isEmpty(etIfsc.getText().toString().trim())){
//                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
//                        getString(R.string.enter_ifsc), Snackbar.LENGTH_LONG);
//                return false;
//            } else if (TextUtils.isEmpty(etAccountNo.getText().toString().trim())){
//                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
//                        getString(R.string.enter_account_number), Snackbar.LENGTH_LONG);
//                return false;
//            } else if (TextUtils.isEmpty(etConfirmAccountNo.getText().toString().trim())){
//                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
//                        getString(R.string.enter_account_number), Snackbar.LENGTH_LONG);
//                return false;
//            } else if (TextUtils.isEmpty(etAccountHolderName.getText().toString().trim())){
//                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
//                        getString(R.string.enter_account_holder_name), Snackbar.LENGTH_LONG);
//                return false;
//            } else if (TextUtils.isEmpty(etAccountType.getText().toString().trim())){
//                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
//                        getString(R.string.select_account_type), Snackbar.LENGTH_LONG);
//                return false;
//            } else if (isTurnoverBelow == null) {
//                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
//                        getString(R.string.select_turnover), Snackbar.LENGTH_LONG);
//                return false;
//            }
//            if(!etAccountNo.getText().toString().trim().equals(etConfirmAccountNo.getText().
//                    toString().trim())) {
//                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
//                                .findViewById(android.R.id.content), getString(R.string.error_confirm_account_no),
//                        Snackbar.LENGTH_LONG);
//                return false;
//            }
//            if(selectedOwnership.equalsIgnoreCase("Firm")) {
//                if (TextUtils.isEmpty(etTradeName.getText().toString().trim())){
//                    Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
//                            getString(R.string.enter_trade_name), Snackbar.LENGTH_LONG);
//                    return false;
//                }
//            }
//            if(((MachineMouActivity) getActivity()).chequeImageUri == null) {
//                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
//                        getString(R.string.select_image), Snackbar.LENGTH_LONG);
//                return false;
//            }
////            if (etProviderContact.getText().toString().trim().length() != 10){
////                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
////                        getString(R.string.enter_proper_provider_contact), Snackbar.LENGTH_LONG);
////                return false;
////            }

//            if (etGstRegNo.getText().toString().trim().length() != 15){
//                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
//                        getString(R.string.enter_proper_gst_no), Snackbar.LENGTH_LONG);
//                return false;
//            }
//            if (etPanNo.getText().toString().trim().length() != 10){
//                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
//                        getString(R.string.enter_proper_pan_no), Snackbar.LENGTH_LONG);
//                return false;
//            }
////            if(imgCount == 0) {
////                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
////                        getString(R.string.select_image), Snackbar.LENGTH_LONG);
////                return false;
////            }
        //}
        return true;
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
        switch (view.getId()){
            case R.id.btn_second_part_mou:
                if(isAllDataValid()){
                    setMachineSecondData();
//                    if(((MachineMouActivity) getActivity()).getMachineDetailData().
//                            getMachine().getOwnedBy().equalsIgnoreCase("BJS")) {
                        MouDetails mouDetails = new MouDetails();
                        ((MachineMouActivity) getActivity()).getMachineDetailData().setMouDetails(mouDetails);
                        Date d = new Date();
                        ((MachineMouActivity) getActivity()).getMachineDetailData().getMouDetails().setDateOfSigning(d.getTime());
                        ((MachineMouActivity) getActivity()).getMachineDetailData().getMouDetails().setDateOfMouExpiry
                                (Util.dateTimeToTimeStamp("2099-12-31", "23:59"));

                        //((MachineMouActivity) getActivity()).openFragment("MachineMouFourthFragment");
                        ((MachineMouActivity) getActivity()).uploadData();
//                    } else {
//                        ((MachineMouActivity) getActivity()).openFragment("MachineMouThirdFragment");
//                    }
                }
                break;
            case R.id.btn_previous_mou:
                setMachineSecondData();
                getActivity().onBackPressed();
                break;
//            case R.id.et_ownership:
//                CustomSpinnerDialogClass cdd = new CustomSpinnerDialogClass(getActivity(), this, "Select Ownership Type", ownershipList,
//                        false);
//                cdd.show();
//                cdd.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT);
//                break;
//            case R.id.et_turnover:
//                CustomSpinnerDialogClass cdd1 = new CustomSpinnerDialogClass(getActivity(), this, "Select option", isTurnOverAboveList,
//                        false);
//                cdd1.show();
//                cdd1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT);
//                break;
//            case R.id.et_account_type:
//                CustomSpinnerDialogClass cdd2 = new CustomSpinnerDialogClass(getActivity(), this, "Select Account Type",
//                        accountTypesList,
//                        false);
//                cdd2.show();
//                cdd2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT);
//                break;
//            case R.id.img_account:
//                onAddImageClick();
//                break;
        }
    }

    private void setMachineSecondData() {
        ProviderInformation providerInformation = new ProviderInformation();
        ((MachineMouActivity) getActivity()).getMachineDetailData().setProviderInformation(providerInformation);
        ((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setFirstName
                (etProviderFirstName.getText().toString().trim());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setLastName
                (etProviderLastName.getText().toString().trim());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setProviderContactNumber
                (etProviderContact.getText().toString().trim());

//        ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setSupervisorName(
//                (etSupervisorFName.getText().toString().trim() + " " + etSupervisorLName.getText().
//                        toString().trim()));
//        ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setMachineMobileNumber
//                (etMachineMobile.getText().toString().trim());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setOperatorName(
                (etOperatorFName.getText().toString().trim() + " " + etOperatorLName.getText().
                        toString().trim()));
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setOperatorContactNumber
                (etOperatorMobile.getText().toString().trim());

//        ((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setOwnership
//                ((selectedOwnershipId));
        //((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setMachineMeterWorking("Yes");
        //((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setIsTurnover(isTurnoverBelow);
        ((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setMachineId(((MachineMouActivity)
                getActivity()).getMachineDetailData().getMachine().getMachineId());
//        ((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setTradeName
//                (etTradeName.getText().toString().trim());
//        ((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setGSTNumber
//                (etGstRegNo.getText().toString().trim());
//        ((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setPANNumber(
//                etPanNo.getText().toString().trim());
//        ((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setBankName
//                (etBankName.getText().toString().trim());
//        ((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setIFSC
//                (etIfsc.getText().toString().trim());
//        ((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setBranch
//                (etBranch.getText().toString().trim());
        //((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setBankAddress("Kothrud, Pune");
//        ((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setAccountNo
//                (etAccountNo.getText().toString().trim());
//        ((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setAccountName
//                (etAccountHolderName.getText().toString().trim());
//        ((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setAccountType
//                (etAccountType.getText().toString().trim());
    }

//    private void onAddImageClick() {
//        if (Permissions.isCameraPermissionGranted(getActivity(), this)) {
//            showPictureDialog();
//        }
//    }

//    private void showPictureDialog() {
//        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
//        dialog.setTitle(getString(R.string.title_choose_picture));
//        String[] items = {getString(R.string.label_gallery), getString(R.string.label_camera)};
//
//        dialog.setItems(items, (dialog1, which) -> {
//            switch (which) {
//                case 0:
//                    choosePhotoFromGallery();
//                    break;
//
//                case 1:
//                    takePhotoFromCamera();
//                    break;
//            }
//        });
//
//        dialog.show();
//    }

//    private void choosePhotoFromGallery() {
//        try {
//            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(i, Constants.CHOOSE_IMAGE_FROM_GALLERY);
//        } catch (ActivityNotFoundException e) {
//            Toast.makeText(getActivity(), getResources().getString(R.string.msg_error_in_photo_gallery),
//                    Toast.LENGTH_SHORT).show();
//        }
//    }

//    private void takePhotoFromCamera() {
//        try {
//            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            File file = getImageFile(); // 1
//            Uri uri;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) // 2
//                uri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID.concat(".file_provider"), file);
//            else
//                uri = Uri.fromFile(file); // 3
//            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri); // 4
//            startActivityForResult(pictureIntent, Constants.CHOOSE_IMAGE_FROM_CAMERA);
//        } catch (ActivityNotFoundException e) {
//            //display an error message
//            Toast.makeText(getActivity(), getResources().getString(R.string.msg_image_capture_not_support),
//                    Toast.LENGTH_SHORT).show();
//        } catch (SecurityException e) {
//            Toast.makeText(getActivity(), getResources().getString(R.string.msg_take_photo_error),
//                    Toast.LENGTH_SHORT).show();
//        }
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == Constants.CHOOSE_IMAGE_FROM_CAMERA && resultCode == RESULT_OK) {
//            try {
////                String imageFilePath = Util.getImageName();
////                if (imageFilePath == null) return;
////                finalUri = Util.getUri(imageFilePath);
////                Crop.of(outputUri, finalUri).start(getContext(), this);
//                finalUri=Uri.fromFile(new File(currentPhotoPath));
//                Crop.of(finalUri, finalUri).start(getContext(),this);
//            } catch (Exception e) {
//                Log.e(TAG, e.getMessage());
//            }
//        } else if (requestCode == Constants.CHOOSE_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {
//            if (data != null) {
//                try {
////                    String imageFilePath = Util.getImageName();
////                    if (imageFilePath == null) return;
////                    outputUri = data.getData();
////                    finalUri = Util.getUri(imageFilePath);
////                    Crop.of(outputUri, finalUri).start(getContext(), this);
//                    getImageFile();
//                    outputUri = data.getData();
//                    finalUri=Uri.fromFile(new File(currentPhotoPath));
//                    Crop.of(outputUri, finalUri).start(getContext(),this);
//                } catch (Exception e) {
//                    Log.e(TAG, e.getMessage());
//                }
//            }
//        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
//            try {
//                imageFile = new File(Objects.requireNonNull(finalUri.getPath()));
//                imgAccount.setImageURI(finalUri);
//                ((MachineMouActivity) getActivity()).chequeImageUri = finalUri;
//                Bitmap bitmap = Util.compressImageToBitmap(imageFile);
//                if (Util.isValidImageSize(imageFile)) {
//                    imgCount++;
//                    ((MachineMouActivity) getActivity()).getImageHashmap().put("accountImage", bitmap);
//                } else {
//                    Util.showToast(getString(R.string.msg_big_image), this);
//                }
//            } catch (Exception e) {
//                Log.e(TAG, e.getMessage());
//            }
//        }
//    }

//    private File getImageFile() {
//        // External sdcard location
//        File mediaStorageDir = new File(
//                Environment
//                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//                Constants.Image.IMAGE_STORAGE_DIRECTORY);
//        // Create the storage directory if it does not exist
//        if (!mediaStorageDir.exists()) {
//            if (!mediaStorageDir.mkdirs()) {
//                return null;
//            }
//        }
//        // Create a media file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
//                Locale.getDefault()).format(new Date());
//        File file;
//        file = new File(mediaStorageDir.getPath() + File.separator
//                + "IMG_" + timeStamp + ".jpg");
//        currentPhotoPath = file.getPath();
//        return file;
//    }

    @Override
    public void onCustomSpinnerSelection(String type) {
//        switch (type) {
//            case "Select Ownership Type":
//                for (CustomSpinnerObject ownershipType : ownershipList) {
//                    if (ownershipType.isSelected()) {
//                        selectedOwnership = ownershipType.getName();
//                        selectedOwnershipId = ownershipType.get_id();
//                        break;
//                    }
//                }
//                etOwnership.setText(selectedOwnership);
//                break;
//            case "Select option":
//                for (CustomSpinnerObject isTurnover : isTurnOverAboveList) {
//                    if (isTurnover.isSelected()) {
//                        isTurnoverBelow = isTurnover.getName();
//                        break;
//                    }
//                }
//                etTurnover.setText(isTurnoverBelow);
//                break;
//            case "Select Account Type":
//                for (CustomSpinnerObject accountType : accountTypesList) {
//                    if (accountType.isSelected()) {
//                        selectedAccountType = accountType.getName();
//                        break;
//                    }
//                }
//                etAccountType.setText(selectedAccountType);
//                break;
//        }
    }
}
