package com.octopusbjsindia.matrimonyregistration;

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

import com.octopusbjsindia.R;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.widgets.SingleSelectBottomSheet;

import java.util.ArrayList;

public class ProfileMatrimonyFamilyFragment extends Fragment implements View.OnClickListener, SingleSelectBottomSheet.MultiSpinnerListener {
    private View fragmentview;
    ArrayList<String> ListDrink = new ArrayList<>();
    private SingleSelectBottomSheet bottomSheetDialogFragment;
    private Button btn_load_next, btn_loadprevious;
    private TextView tv_pagetitle;
    private EditText et_family_type, et_Shakha_self, et_shakha_mama, et_shakha_dada, et_shakha_nana, et_father_name, et_father_occupation, et_family_income,
            et_mothers_name, et_mothers_occupation, et_brother, et_sister;


    public static ProfileMatrimonyFamilyFragment newInstance() {
        return new ProfileMatrimonyFamilyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentview = inflater.inflate(R.layout.user_registration_matrimony_fragment_family_new, container, false);
        return fragmentview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {
        createTempArrayList();
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
        et_brother.setOnClickListener(this);
        et_sister.setOnClickListener(this);

        if (((RegistrationActivity) getActivity()).matrimonialProfile != null &&
                ((RegistrationActivity) getActivity()).matrimonialProfile.getFamilyDetails() != null) {
            setData();
        }
        fragmentview.findViewById(R.id.iv_toobar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RegistrationActivity) getActivity()).onBackPressed();
            }
        });
    }

    private void setData() {
        et_family_type.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getFamilyDetails().getFamilyType());
        et_Shakha_self.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getFamilyDetails().getGotra().getSelfGotra());
        et_shakha_mama.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getFamilyDetails().getGotra().getMamaGotra());
        et_shakha_dada.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getFamilyDetails().getGotra().getDadaGotra());
        et_shakha_nana.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getFamilyDetails().getGotra().getNanaGotra());
        et_father_name.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getFamilyDetails().getFatherName());
        et_father_occupation.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getFamilyDetails().getFatherOccupation());
        et_family_income.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getFamilyDetails().getFamilyIncome());
        et_mothers_name.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getFamilyDetails().getMotherName());
        et_mothers_occupation.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getFamilyDetails().getMotherOccupation());
        et_brother.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getFamilyDetails().getBrotherCount());
        et_sister.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getFamilyDetails().getSisterCount());
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
                ((RegistrationActivity) getActivity()).loadNextScreen(1);
                break;

            case R.id.et_family_type:
                for (int i = 0; i < ((RegistrationActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if (((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("family_type")) {
                        showMultiSelectBottomsheet("Family Type", "et_family_type", (ArrayList<String>) ((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;
            case R.id.et_father_occupation:
                for (int i = 0; i < ((RegistrationActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if (((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("parents_occupation")) {
                        showMultiSelectBottomsheet("Fathers Status", "et_father_occupation", (ArrayList<String>) ((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;
            case R.id.et_mothers_occupation:
                for (int i = 0; i < ((RegistrationActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if (((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("parents_occupation")) {
                        showMultiSelectBottomsheet("Mothers Status", "et_mothers_occupation", (ArrayList<String>) ((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;
            case R.id.et_family_income:
                for (int i = 0; i < ((RegistrationActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if (((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("income")) {
                        showMultiSelectBottomsheet("Family Income", "et_family_income", (ArrayList<String>) ((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;
            case R.id.et_brother:
                showMultiSelectBottomsheet("No of brother", "et_brother", ListDrink);
                break;
            case R.id.et_sister:
                showMultiSelectBottomsheet("No of sisters", "et_sister", ListDrink);
                break;

        }

    }

    private void setValuesInModel() {
        if (isAllInputsValid()) {
            if (((RegistrationActivity) getActivity()).matrimonialProfile != null) {
                if (((RegistrationActivity) getActivity()).familyDetails != null) {

                    ((RegistrationActivity) getActivity()).familyDetails.setFamilyType(et_family_type.getText().toString());

                    ((RegistrationActivity) getActivity()).gotra.setSelfGotra(et_Shakha_self.getText().toString());
                    ((RegistrationActivity) getActivity()).gotra.setMamaGotra(et_shakha_mama.getText().toString());
                    ((RegistrationActivity) getActivity()).gotra.setDadaGotra(et_shakha_dada.getText().toString());
                    ((RegistrationActivity) getActivity()).gotra.setNanaGotra(et_shakha_nana.getText().toString());

                    ((RegistrationActivity) getActivity()).familyDetails.setGotra(((RegistrationActivity) getActivity()).gotra);
                    ((RegistrationActivity) getActivity()).familyDetails.setFatherName(et_father_name.getText().toString());
                    ((RegistrationActivity) getActivity()).familyDetails.setFatherOccupation(et_father_occupation.getText().toString());
                    ((RegistrationActivity) getActivity()).familyDetails.setFamilyIncome(et_family_income.getText().toString());
                    ((RegistrationActivity) getActivity()).familyDetails.setMotherName(et_mothers_name.getText().toString());
                    ((RegistrationActivity) getActivity()).familyDetails.setMotherOccupation(et_mothers_occupation.getText().toString());
                    ((RegistrationActivity) getActivity()).familyDetails.setBrotherCount(et_brother.getText().toString());
                    ((RegistrationActivity) getActivity()).familyDetails.setSisterCount(et_sister.getText().toString());


                    ((RegistrationActivity) getActivity())
                            .matrimonialProfile.setFamilyDetails(((RegistrationActivity) getActivity()).familyDetails);

                } else {
                    Util.showToast(getActivity(), "null object getPersonal_details()");
                }
            } else {
                Util.showToast(getActivity(), "null object");
            }
            ((RegistrationActivity) getActivity()).loadNextScreen(3);
        }
    }
    //------------

    private void showMultiSelectBottomsheet(String Title, String selectedOption, ArrayList<String> List) {

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
            case "et_brother":
                et_brother.setText(ListDrink.get(selected));
                break;
            case "et_sister":
                et_sister.setText(ListDrink.get(selected));
                break;


        }

    }

    //Validations
    private boolean isAllInputsValid() {
        String msg = "";

        if (et_family_type.getText().toString().trim().length() == 0) {
            msg = "Please enter family type";
        } else if (et_Shakha_self.getText().toString().trim().length() == 0) {
            msg = "Please enter self shakha";
        } else if (et_father_name.getText().toString().trim().length() == 0) {
            msg = "Please enter fathers name";
        } else if (et_father_occupation.getText().toString().trim().length() == 0) {
            msg = "Please enter fathers status";
        } else if (et_mothers_name.getText().toString().trim().length() == 0) {
            msg = "Please enter mothers name";
        } else if (et_brother.getText().toString().trim().length() == 0) {
            msg = "Please enter number of brothers";
        } else if (et_sister.getText().toString().trim().length() == 0) {
            msg = "Please enter number of sisters";
        }

        if (TextUtils.isEmpty(msg)) {
            return true;
        }

        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
        return false;
    }

    public void createTempArrayList() {

        ListDrink.add("0");
        ListDrink.add("1");
        ListDrink.add("2");
        ListDrink.add("3");
        ListDrink.add("4");
        ListDrink.add("5");
        ListDrink.add("6");
        ListDrink.add("7");
        ListDrink.add("8");
        ListDrink.add("9");
        ListDrink.add("10");
    }

}