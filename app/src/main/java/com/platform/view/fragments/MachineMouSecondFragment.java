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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.Platform;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.listeners.CustomSpinnerListener;
import com.platform.models.SujalamSuphalam.MasterDataList;
import com.platform.models.SujalamSuphalam.ProviderInformation;
import com.platform.models.SujalamSuphalam.SSMasterDatabase;
import com.platform.models.common.CustomSpinnerObject;
import com.platform.utility.Util;
import com.platform.view.activities.MachineMouActivity;
import com.platform.view.customs.CustomSpinnerDialogClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MachineMouSecondFragment extends Fragment implements View.OnClickListener, CustomSpinnerListener {
    private View machineMouSecondFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private Button btnSecondPartMou, btnPreviousMou;
    private EditText etProviderFirstName, etProviderLastName, etProviderContact, etMachineMobile,
            etOwnership, etTradeName, etTurnover, etGstRegNo, etPanNo, etBankName, etBranch, etIfsc,
            etAccountNo, etConfirmAccountNo, etAccountHolderName, etAccountType;
    private String selectedOwnership, isTurnoverBelow;
    private ArrayList<CustomSpinnerObject> ownershipList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> isTurnOverAboveList = new ArrayList<>();

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
        progressBarLayout = machineMouSecondFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = machineMouSecondFragmentView.findViewById(R.id.pb_profile_act);
        btnSecondPartMou = machineMouSecondFragmentView.findViewById(R.id.btn_second_part_mou);
        btnSecondPartMou.setOnClickListener(this);
        btnPreviousMou = machineMouSecondFragmentView.findViewById(R.id.btn_previous_mou);
        btnPreviousMou.setOnClickListener(this);
        etProviderFirstName = machineMouSecondFragmentView.findViewById(R.id.et_provider_first_name);
        etProviderLastName = machineMouSecondFragmentView.findViewById(R.id.et_provider_last_name);
        etProviderContact = machineMouSecondFragmentView.findViewById(R.id.et_provider_contact);
        etMachineMobile = machineMouSecondFragmentView.findViewById(R.id.et_machine_mobile);
        etOwnership = machineMouSecondFragmentView.findViewById(R.id.et_ownership);
        etTradeName = machineMouSecondFragmentView.findViewById(R.id.et_trade_name);
        etTurnover = machineMouSecondFragmentView.findViewById(R.id.et_turnover);
        etGstRegNo = machineMouSecondFragmentView.findViewById(R.id.et_gst_reg_no);
        etPanNo = machineMouSecondFragmentView.findViewById(R.id.et_pan_no);
        etBankName = machineMouSecondFragmentView.findViewById(R.id.et_bank_name);
        etBranch = machineMouSecondFragmentView.findViewById(R.id.et_branch);
        etIfsc = machineMouSecondFragmentView.findViewById(R.id.et_ifsc);
        etAccountNo = machineMouSecondFragmentView.findViewById(R.id.et_account_no);
        etConfirmAccountNo = machineMouSecondFragmentView.findViewById(R.id.et_confirm_account_no);
        etAccountHolderName = machineMouSecondFragmentView.findViewById(R.id.et_account_holder_name);
        etAccountType = machineMouSecondFragmentView.findViewById(R.id.et_account_type);

        etOwnership.setOnClickListener(this);
        etTurnover.setOnClickListener(this);

        List<SSMasterDatabase> list = DatabaseManager.getDBInstance(Platform.getInstance()).
                getSSMasterDatabaseDao().getSSMasterData();
        String masterDbString = list.get(0).getData();

        Gson gson = new Gson();
        TypeToken<ArrayList<MasterDataList>> token = new TypeToken<ArrayList<MasterDataList>>() {
        };
        ArrayList<MasterDataList> masterDataList = gson.fromJson(masterDbString, token.getType());
        masterDataList.size();
//        JSONArray jsonArray = null;
//        try {
//            jsonArray = new JSONArray(masterDbString);
//            List<MasterDataList> list1;
////            JSONObject jsnobject = new JSONObject(masterDbString);
////            JSONArray jsonArray = jsnobject.getJSONArray("locations");
//            jsonArray.get(0);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        for(int i = 0; i<masterDataList.size(); i++) {
            if(masterDataList.get(i).getForm().equals("machine_mou") && masterDataList.get(i).
                    getField().equals("ownedBy")) {
                for(int j = 0; j<masterDataList.get(i).getData().size(); j++) {
                    CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
                    customSpinnerObject.setName(masterDataList.get(i).getData().get(j).getValue());
                    customSpinnerObject.set_id(masterDataList.get(i).getData().get(j).getId());
                    customSpinnerObject.setSelected(false);
                    ownershipList.add(customSpinnerObject);
                }
            }
        }
        CustomSpinnerObject turnoverYes = new CustomSpinnerObject();
        turnoverYes.setName("Yes");
        turnoverYes.set_id("1");
        turnoverYes.setSelected(false);
        isTurnOverAboveList.add(turnoverYes);

        CustomSpinnerObject turnoverNo = new CustomSpinnerObject();
        turnoverNo.setName("No");
        turnoverNo.set_id("2");
        turnoverNo.setSelected(false);
        isTurnOverAboveList.add(turnoverNo);
    }

    public boolean isAllDataValid() {
        if (TextUtils.isEmpty(etProviderFirstName.getText().toString().trim())
                || TextUtils.isEmpty(etProviderLastName.getText().toString().trim())
                || TextUtils.isEmpty(etProviderContact.getText().toString().trim())
                || TextUtils.isEmpty(etMachineMobile.getText().toString().trim())
                || TextUtils.isEmpty(etTradeName.getText().toString().trim())
                || TextUtils.isEmpty(etGstRegNo.getText().toString().trim())
                || TextUtils.isEmpty(etPanNo.getText().toString().trim())
                || TextUtils.isEmpty(etBankName.getText().toString().trim())
                || TextUtils.isEmpty(etBranch.getText().toString().trim())
                || TextUtils.isEmpty(etIfsc.getText().toString().trim())
                || TextUtils.isEmpty(etAccountNo.getText().toString().trim())
                || TextUtils.isEmpty(etConfirmAccountNo.getText().toString().trim())
                || TextUtils.isEmpty(etAccountHolderName.getText().toString().trim())
                || TextUtils.isEmpty(etAccountType.getText().toString().trim())
                || selectedOwnership == null) {
        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                        .findViewById(android.R.id.content), getString(R.string.enter_correct_details),
                Snackbar.LENGTH_LONG);
            return false;
        }
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
                    ((MachineMouActivity) getActivity()).openFragment("MachineMouThirdFragment");
                }
                break;
            case R.id.btn_previous_mou:
                break;
            case R.id.et_ownership:
                CustomSpinnerDialogClass cdd = new CustomSpinnerDialogClass(getActivity(), this, "Select Ownership Type", ownershipList,
                        false);
                cdd.show();
                cdd.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_turnover:
                CustomSpinnerDialogClass cdd1 = new CustomSpinnerDialogClass(getActivity(), this, "Is Turnover", isTurnOverAboveList,
                        false);
                cdd1.show();
                cdd1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
        }
    }

    private void setMachineSecondData() {
        ProviderInformation providerInformation = new ProviderInformation();
        ((MachineMouActivity) getActivity()).getMachineDetailData().setProviderInformation(providerInformation);
        ((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setFirstName
                (etProviderFirstName.getText().toString().trim());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setLastName
                (etProviderLastName.getText().toString().trim());
        //((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setAddress("Pune");
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setProviderContactNumber
                (etProviderContact.getText().toString().trim());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setContactNumber
                (etMachineMobile.getText().toString().trim());
        //((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setMachineMeterWorking("Yes");
        ((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setIsTurnover(isTurnoverBelow);
        ((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setMachineId(((MachineMouActivity)
                getActivity()).getMachineDetailData().getMachine().getId());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setTradeName
                (etTradeName.getText().toString().trim());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setGSTNumber
                (etGstRegNo.getText().toString().trim());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setPANNumber(
                etPanNo.getText().toString().trim());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setBankName
                (etBankName.getText().toString().trim());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setIFSC
                (etIfsc.getText().toString().trim());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setBranch
                (etBranch.getText().toString().trim());
        //((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setBankAddress("Kothrud, Pune");
        ((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setAccountNo
                (etAccountNo.getText().toString().trim());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setCheckBookImage
                ("www.google.com");
        ((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setAccountName
                (etAccountHolderName.getText().toString().trim());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getProviderInformation().setAccountType
                (etAccountType.getText().toString().trim());
    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        switch (type) {
            case "Select Ownership Type":
                for (CustomSpinnerObject ownershipType : ownershipList) {
                    if (ownershipType.isSelected()) {
                        selectedOwnership = ownershipType.getName();
                        break;
                    }
                }
                etOwnership.setText(selectedOwnership);
                break;
            case "Is Turnover":
                for (CustomSpinnerObject isTurnover : isTurnOverAboveList) {
                    if (isTurnover.isSelected()) {
                        isTurnoverBelow = isTurnover.getName();
                        break;
                    }
                }
                etTurnover.setText(isTurnoverBelow);
                break;
        }
    }
}
