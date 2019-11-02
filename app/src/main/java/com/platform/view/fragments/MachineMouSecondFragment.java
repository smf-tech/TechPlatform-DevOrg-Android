package com.platform.view.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

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
import com.platform.utility.Constants;
import com.platform.utility.Permissions;
import com.platform.utility.Util;
import com.platform.view.activities.MachineMouActivity;
import com.platform.view.customs.CustomSpinnerDialogClass;
import com.soundcloud.android.crop.Crop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class MachineMouSecondFragment extends Fragment implements View.OnClickListener, CustomSpinnerListener {
    private View machineMouSecondFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private Button btnSecondPartMou, btnPreviousMou;
    private EditText etProviderFirstName, etProviderLastName, etProviderContact, etMachineMobile,
            etOwnership, etTradeName, etTurnover, etGstRegNo, etPanNo, etBankName, etBranch, etIfsc,
            etAccountNo, etConfirmAccountNo, etAccountHolderName, etAccountType;
    private ImageView imgAccount;
    private String selectedOwnership, isTurnoverBelow, selectedAccountType;
    private ArrayList<CustomSpinnerObject> ownershipList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> isTurnOverAboveList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> accountTypesList = new ArrayList<>();
    private Uri outputUri;
    private Uri finalUri;
    private final String TAG = MachineMouSecondFragment.class.getName();

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
        imgAccount = machineMouSecondFragmentView.findViewById(R.id.img_account);
        etAccountHolderName = machineMouSecondFragmentView.findViewById(R.id.et_account_holder_name);
        etAccountType = machineMouSecondFragmentView.findViewById(R.id.et_account_type);

        etOwnership.setOnClickListener(this);
        etTurnover.setOnClickListener(this);
        etAccountType.setOnClickListener(this);
        imgAccount.setOnClickListener(this);

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

        CustomSpinnerObject accountSavings = new CustomSpinnerObject();
        accountSavings.setName("Savings Account");
        accountSavings.set_id("1");
        accountSavings.setSelected(false);
        accountTypesList.add(accountSavings);

        CustomSpinnerObject accountCurrent = new CustomSpinnerObject();
        accountCurrent.setName("Current Account");
        accountCurrent.set_id("2");
        accountCurrent.setSelected(false);
        accountTypesList.add(accountCurrent);
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
                CustomSpinnerDialogClass cdd1 = new CustomSpinnerDialogClass(getActivity(), this, "Select option", isTurnOverAboveList,
                        false);
                cdd1.show();
                cdd1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_account_type:
                CustomSpinnerDialogClass cdd2 = new CustomSpinnerDialogClass(getActivity(), this, "Select Account Type",
                        accountTypesList,
                        false);
                cdd2.show();
                cdd2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.img_account:
                onAddImageClick();
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

    private void onAddImageClick() {
        if (Permissions.isCameraPermissionGranted(getActivity(), this)) {
            showPictureDialog();
        }
    }

    private void showPictureDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(getString(R.string.title_choose_picture));
        String[] items = {getString(R.string.label_gallery), getString(R.string.label_camera)};

        dialog.setItems(items, (dialog1, which) -> {
            switch (which) {
                case 0:
                    choosePhotoFromGallery();
                    break;

                case 1:
                    takePhotoFromCamera();
                    break;
            }
        });

        dialog.show();
    }

    private void choosePhotoFromGallery() {
        try {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, Constants.CHOOSE_IMAGE_FROM_GALLERY);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_error_in_photo_gallery),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void takePhotoFromCamera() {
        try {
            //use standard intent to capture an image
            String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/Octopus/Image/picture.jpg";

            File imageFile = new File(imageFilePath);
            outputUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName()
                    + ".file_provider", imageFile);

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(takePictureIntent, Constants.CHOOSE_IMAGE_FROM_CAMERA);
        } catch (ActivityNotFoundException e) {
            //display an error message
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_image_capture_not_support),
                    Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_take_photo_error),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.CHOOSE_IMAGE_FROM_CAMERA && resultCode == RESULT_OK) {
            try {
                String imageFilePath = getImageName();
                if (imageFilePath == null) return;
                finalUri = Util.getUri(imageFilePath);
                Crop.of(outputUri, finalUri).start(getContext(), this);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else if (requestCode == Constants.CHOOSE_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    String imageFilePath = getImageName();
                    if (imageFilePath == null) return;
                    outputUri = data.getData();
                    finalUri = Util.getUri(imageFilePath);
                    Crop.of(outputUri, finalUri).start(getContext(), this);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            try {
                final File imageFile = new File(Objects.requireNonNull(finalUri.getPath()));
                if (Util.isConnected(getActivity())) {
                    if (Util.isValidImageSize(imageFile)) {
                        imgAccount.setImageURI(finalUri);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), finalUri);
                        ((MachineMouActivity) getActivity()).getImageHashmap().put("accountImage", bitmap);
                        //imageHashmap.put("accountImage", bitmap);
                    } else {
                        Util.showToast(getString(R.string.msg_big_image), this);
                    }
                } else {
                    Util.showToast(getResources().getString(R.string.msg_no_network), this);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private String getImageName() {
        long time = new Date().getTime();
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + Constants.Image.IMAGE_STORAGE_DIRECTORY);
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                Log.e(TAG, "Failed to create directory!");
                return null;
            }
        }
        return Constants.Image.IMAGE_STORAGE_DIRECTORY + Constants.Image.FILE_SEP
                + Constants.Image.IMAGE_PREFIX + time + Constants.Image.IMAGE_SUFFIX;
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
            case "Select option":
                for (CustomSpinnerObject isTurnover : isTurnOverAboveList) {
                    if (isTurnover.isSelected()) {
                        isTurnoverBelow = isTurnover.getName();
                        break;
                    }
                }
                etTurnover.setText(isTurnoverBelow);
                break;
            case "Select Account Type":
                for (CustomSpinnerObject accountType : accountTypesList) {
                    if (accountType.isSelected()) {
                        selectedAccountType = accountType.getName();
                        break;
                    }
                }
                etAccountType.setText(selectedAccountType);
                break;
        }
    }
}
