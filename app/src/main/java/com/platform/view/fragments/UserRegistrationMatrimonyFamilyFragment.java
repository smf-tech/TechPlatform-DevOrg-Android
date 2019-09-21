package com.platform.view.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.platform.R;
import com.platform.utility.Util;
import com.platform.view.activities.UserRegistrationMatrimonyActivity;
import com.platform.widgets.SingleSelectBottomSheet;

import java.util.ArrayList;

public class UserRegistrationMatrimonyFamilyFragment extends Fragment implements View.OnClickListener, SingleSelectBottomSheet.MultiSpinnerListener {
    private View fragmentview;
    private SingleSelectBottomSheet bottomSheetDialogFragment;
    private Button btn_load_next, btn_loadprevious;
    private TextView tv_pagetitle;
    private EditText et_family_type, et_Shakha_self, et_shakha_mama, et_shakha_dada, et_shakha_nana, et_father_name, et_father_occupation, et_family_income,
            et_mothers_name, et_mothers_occupation, et_brother, et_sister;


    public static UserRegistrationMatrimonyFamilyFragment newInstance() {
        return new UserRegistrationMatrimonyFamilyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentview = inflater.inflate(R.layout.user_registration_matrimony_fragment_family, container, false);
        initViews();
        return fragmentview;
    }

    private void initViews() {
        //views
        tv_pagetitle = fragmentview.findViewById(R.id.tv_pagetitle);
        btn_load_next = fragmentview.findViewById(R.id.btn_loadnext);
        btn_loadprevious = fragmentview.findViewById(R.id.btn_loadprevious);
        //edit text
        et_family_type = fragmentview.findViewById(R.id.et_family_type);
        et_Shakha_self = fragmentview.findViewById(R.id.et_Shakha_self);
        et_shakha_mama = fragmentview.findViewById(R.id.et_shakha_mama);
        et_shakha_dada = fragmentview.findViewById(R.id.et_shakha_dada);
        et_shakha_nana = fragmentview.findViewById(R.id.et_shakha_nana);
        et_father_name = fragmentview.findViewById(R.id.et_father_name);
        et_father_occupation = fragmentview.findViewById(R.id.et_father_occupation);
        et_family_income = fragmentview.findViewById(R.id.et_family_income);
        et_mothers_name = fragmentview.findViewById(R.id.et_mothers_name);
        et_mothers_occupation = fragmentview.findViewById(R.id.et_mothers_occupation);
        et_brother = fragmentview.findViewById(R.id.et_brother);
        et_sister = fragmentview.findViewById(R.id.et_sister);

        //set listeners
        btn_load_next.setOnClickListener(this);
        btn_loadprevious.setOnClickListener(this);

        et_family_type.setOnClickListener(this);
        et_father_occupation.setOnClickListener(this);
        et_family_income.setOnClickListener(this);
        et_mothers_occupation.setOnClickListener(this);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //tvmessage.setText("Fragment ONe");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_loadnext:
                setValuesInModel();

                break;
            case R.id.btn_loadprevious:
                ((UserRegistrationMatrimonyActivity) getActivity()).loadNextScreen(1);
                break;

            case R.id.et_family_type:
                for (int i = 0; i < ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.size(); i++) {
                if (((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("family_type")) {
                    showMultiSelectBottomsheet("Family Type","et_family_type", (ArrayList<String>) ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                    break;
                }
            }
                break;
            case R.id.et_father_occupation:
                for (int i = 0; i < ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if (((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("occupation")) {
                        showMultiSelectBottomsheet("Fathers Occupation","et_father_occupation", (ArrayList<String>) ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;
            case R.id.et_mothers_occupation:
                for (int i = 0; i < ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if (((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("occupation")) {
                        showMultiSelectBottomsheet("Mothers Occupation","et_mothers_occupation", (ArrayList<String>) ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;
            case R.id.et_family_income:
                for (int i = 0; i < ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if (((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("income")) {
                        showMultiSelectBottomsheet("Family Income","et_family_income", (ArrayList<String>) ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;
        }

    }

    private void setValuesInModel() {
        if (isAllInputsValid())
        {
            if (UserRegistrationMatrimonyActivity.matrimonyUserRegRequestModel != null) {
                if (UserRegistrationMatrimonyActivity.familyDetails != null) {

                    UserRegistrationMatrimonyActivity.familyDetails.setFamily_type(et_family_type.getText().toString());

                    UserRegistrationMatrimonyActivity.gotra.setSelf_gotra(et_Shakha_self.getText().toString());
                    UserRegistrationMatrimonyActivity.gotra.setMama_gotra(et_shakha_mama.getText().toString());
                    UserRegistrationMatrimonyActivity.gotra.setDada_gotra(et_shakha_dada.getText().toString());
                    UserRegistrationMatrimonyActivity.gotra.setNana_gotra(et_shakha_nana.getText().toString());

                    UserRegistrationMatrimonyActivity.familyDetails.setGotra(UserRegistrationMatrimonyActivity.gotra);

                    UserRegistrationMatrimonyActivity.familyDetails.setFather_name(et_father_name.getText().toString());
                    UserRegistrationMatrimonyActivity.familyDetails.setFather_occupation(et_father_occupation.getText().toString());
                    UserRegistrationMatrimonyActivity.familyDetails.setFamily_income(et_family_income.getText().toString());
                    UserRegistrationMatrimonyActivity.familyDetails.setMother_name(et_mothers_name.getText().toString());
                    UserRegistrationMatrimonyActivity.familyDetails.setMother_occupation(et_mothers_occupation.getText().toString());
                    UserRegistrationMatrimonyActivity.familyDetails.setBrother_count(et_brother.getText().toString());
                    UserRegistrationMatrimonyActivity.familyDetails.setSister_count(et_sister.getText().toString());


                    UserRegistrationMatrimonyActivity.matrimonyUserRegRequestModel.setFamily_details(UserRegistrationMatrimonyActivity.familyDetails);

                } else {
                    Util.showToast("null object getPersonal_details()", getActivity());
                }
            } else {
                Util.showToast("null object", getActivity());
            }
            ((UserRegistrationMatrimonyActivity) getActivity()).loadNextScreen(3);
        }
    }
    //------------

    private void showMultiSelectBottomsheet(String Title,String selectedOption, ArrayList<String> List) {

        bottomSheetDialogFragment = new SingleSelectBottomSheet(getActivity(), selectedOption, List, this::onValuesSelected);
        bottomSheetDialogFragment.show();
        bottomSheetDialogFragment.toolbarTitle.setText(Title);
        bottomSheetDialogFragment.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onValuesSelected(int selected, String spinnerName, String selectedValues) {
        switch (spinnerName) {
            case "et_family_type":
                et_family_type.setText(selectedValues);
                break;
            case "et_father_occupation":
                et_father_occupation.setText(selectedValues);
                break;
            case "et_mothers_occupation":
                et_mothers_occupation.setText(selectedValues);
                break;
            case "et_family_income":
                et_family_income.setText(selectedValues);
                break;



        }

    }

    //Validations
    private boolean isAllInputsValid() {
        String msg = "";
        if (et_family_type.getText().toString().trim().length() == 0) {
            msg = "Please enter about your birth Place.";
            et_family_type.requestFocus();
        }else if (et_Shakha_self.getText().toString().trim().length() == 0) {
            msg = "Please enter your Shakha/Gotra.";
            et_Shakha_self.requestFocus();
        }else if (et_father_name.getText().toString().trim().length() == 0) {
            msg = "Please enter fathers full name.";
            et_father_name.requestFocus();
        }else if (et_father_occupation.getText().toString().trim().length() == 0) {
            msg = "Please enter fathers occupation.";
            et_father_occupation.requestFocus();
        }else if (et_mothers_name.getText().toString().trim().length() == 0) {
            msg = "Please enter mothers name.";
            et_mothers_name.requestFocus();
        }else if (et_mothers_occupation.getText().toString().trim().length() == 0) {
            msg = "Please enter mothers occupation.";
            et_mothers_occupation.requestFocus();
        }else if (et_brother.getText().toString().trim().length() == 0) {
            msg = "Please enter number of brothers.";
            et_brother.requestFocus();
        }else if (et_sister.getText().toString().trim().length() == 0) {
            msg = "Please enter number of sisters.";
            et_sister.requestFocus();
        }


        if (TextUtils.isEmpty(msg)) {
            return true;
        }

        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
        return false;
    }
}