package com.octopusbjsindia.matrimonyregistration;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.octopusbjsindia.R;
import com.octopusbjsindia.matrimonyregistration.model.EducationalDetails;
import com.octopusbjsindia.matrimonyregistration.model.FamilyDetails;
import com.octopusbjsindia.matrimonyregistration.model.MatrimonialProfile;
import com.octopusbjsindia.matrimonyregistration.model.OccupationalDetails;
import com.octopusbjsindia.matrimonyregistration.model.OtherMaritalInformation;
import com.octopusbjsindia.matrimonyregistration.model.PersonalDetails;
import com.octopusbjsindia.matrimonyregistration.model.ProfileDetailData;
import com.octopusbjsindia.matrimonyregistration.model.ResidentialDetails;
import com.octopusbjsindia.models.Matrimony.MatrimonyMasterRequestModel;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.SmartFragmentStatePagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class RegistrationActivity extends AppCompatActivity implements APIListener {

    public RegistrationActivityPresenter presenter;
    private final String PROFILE_ID = "profileId";
    public MatrimonialProfile matrimonialProfile = new MatrimonialProfile();

    public PersonalDetails personalDetails = new PersonalDetails();
    public String aboutUsStr = "";
    public EducationalDetails educationalDetails = new EducationalDetails();
    public FamilyDetails familyDetails = new FamilyDetails();
    public OccupationalDetails occupationalDetails = new OccupationalDetails();
    public ResidentialDetails residentialDetails = new ResidentialDetails();
    public OtherMaritalInformation otherMaritialInformation = new OtherMaritalInformation();

    public com.octopusbjsindia.matrimonyregistration.model.Gotra gotra = new com.octopusbjsindia.matrimonyregistration.model.Gotra();
    public List<MatrimonyMasterRequestModel.DataList.Master_data> MasterDataArrayList;

    public ArrayList<String> businessImagesList = new ArrayList<>();
    public int businessImagespos = 0;
    public String businessImagesProfile;
    int flag;
    private RelativeLayout pbLayout;
    private ViewPager viewPager;
    private RegistrationActivity.MatrimonyViewPagerAdapter matrimonyViewPagerAdapter;
    private String meetId = "";

    @Override
    protected void onResume() {
        super.onResume();
        if (Util.isConnected(this)){
            //presenter.getProfile(getIntent().getStringExtra(PROFILE_ID), "");
        } else {
            //Util.showToast(getString(R.string.msg_no_network), this);
            Util.snackBarToShowMsg(getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.msg_no_network),
                    Snackbar.LENGTH_LONG);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration_matrimony);

        if (getIntent().hasExtra("matrimonialProfile")) {
            //matrimonialProfile = (MatrimonialProfile) getIntent().getSerializableExtra("matrimonialProfile");
        }
        if (getIntent().hasExtra("MeetId")) {
            meetId = getIntent().getStringExtra("MeetId");
        }

        flag = getIntent().getIntExtra("Flag", 0);

        pbLayout = findViewById(R.id.login_progress_bar);

        /*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ProfileMatrimonyFragmentOne.newInstance())
                    .commitNow();
        }*/
        viewPager = findViewById(R.id.approval_cat_view_pager);
        viewPager.setOffscreenPageLimit(5);

        presenter = new RegistrationActivityPresenter(this);
        if (Util.isConnected(this)) {
            if (flag == 1) {
                presenter.getMatrimonyMaster();


            } else {
                Util.showToast(this, getResources().getString(R.string.msg_no_network));
            }


            MasterDataArrayList = new ArrayList<>();
            //matrimonyUserRegRequestModel = new MatrimonyUserRegRequestModel();
        }
        meetId = getIntent().getStringExtra(PROFILE_ID);
     if (Util.isConnected(this)){
        presenter.getProfile(getIntent().getStringExtra(PROFILE_ID), "");
    } else {
        Util.showToast(getString(R.string.msg_no_network), this);
        finish();
    }
    }

    private void setupViewPager(ViewPager viewPager) {
        matrimonyViewPagerAdapter = new MatrimonyViewPagerAdapter(getSupportFragmentManager());
        Bundle b = new Bundle();
        switch (flag) {
            case 1://matrimonial
                b.putBoolean("SHOW_ALL", false);

                /*ProfilePhotosUploadFragment profilePhotosUploadFragment = new ProfilePhotosUploadFragment();
                matrimonyViewPagerAdapter.addFragment(profilePhotosUploadFragment);*/
                ProfileMatrimonyResidenceFragment profileMatrimonyResidenceFragment = new ProfileMatrimonyResidenceFragment();
                matrimonyViewPagerAdapter.addFragment(profileMatrimonyResidenceFragment);

                ProfileMatrimonyFragmentOne profileMatrimonyFragmentOne = new ProfileMatrimonyFragmentOne();
                profileMatrimonyFragmentOne.setArguments(b);
                matrimonyViewPagerAdapter.addFragment(profileMatrimonyFragmentOne);

                ProfileMatrimonyFamilyFragment profileMatrimonyFamilyFragment = new ProfileMatrimonyFamilyFragment();
                matrimonyViewPagerAdapter.addFragment(profileMatrimonyFamilyFragment);

                /*ProfileMatrimonyResidenceFragment profileMatrimonyResidenceFragment = new ProfileMatrimonyResidenceFragment();
                matrimonyViewPagerAdapter.addFragment(profileMatrimonyResidenceFragment);*/

                ProfileMatrimonyAboutmeFragment profileMatrimonyAboutmeFragment = new ProfileMatrimonyAboutmeFragment();
                matrimonyViewPagerAdapter.addFragment(profileMatrimonyAboutmeFragment);

                ProfilePhotosUploadFragment profilePhotosUploadFragment = new ProfilePhotosUploadFragment();
                matrimonyViewPagerAdapter.addFragment(profilePhotosUploadFragment);

                viewPager.setAdapter(matrimonyViewPagerAdapter);
                break;


        }


    }


    public void ReloadFragmentOnImageUploaad(String response) {
        List fragments = getSupportFragmentManager().getFragments();
        Fragment fragment = (Fragment) fragments.get(4);
        if (4 == viewPager.getCurrentItem() && null != fragment) {
            if (fragment instanceof ProfilePhotosUploadFragment) {
                ((ProfilePhotosUploadFragment) fragment).reloadFragmentData(response);
            }
        }
        /*List fragments = getSupportFragmentManager().getFragments();
        Fragment fragment = (Fragment) fragments.get(0);
        if (0 == viewPager.getCurrentItem() && null != fragment) {
            if (fragment instanceof ProfilePhotosUploadFragment) {
                ((ProfilePhotosUploadFragment) fragment).reloadFragmentData(response);
            }
        }*/

    }

    //load next fragment for registration
    public void loadNextScreen(int next) {
        if (Util.isConnected(this)){

        viewPager.setCurrentItem(next - 1);

        List fragments = getSupportFragmentManager().getFragments();
        Fragment fragment = (Fragment) fragments.get(3);
        if (3 == viewPager.getCurrentItem() && null != fragment) {
            if (fragment instanceof ProfileMatrimonyAboutmeFragment) {
                ((ProfileMatrimonyAboutmeFragment) fragment).reloadFragmentData();
            }
        }
        } else {
            //Util.showToast(getString(R.string.msg_no_network), this);
            Util.snackBarToShowMsg(getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.msg_no_network),
                    Snackbar.LENGTH_LONG);
        }
//        switch (next) {
//            case 1:
//                viewPager.setCurrentItem(0);
//                break;
//            case 2:
//                viewPager.setCurrentItem(1);
//                break;
//            case 3:
//                viewPager.setCurrentItem(2);
//                break;
//            case 4:
//                viewPager.setCurrentItem(3);
//                break;
//            default:
//                break;
//
//        }
    }

    public void saveMasterData(List<MatrimonyMasterRequestModel.DataList.Master_data> data) {
        MasterDataArrayList = data;
        //MasterDataArrayList.get(0).getValues()
    }


    public void submitUserRegistrationRequest() {
        /*if (meetId.length() > 0) {
            matrimonialProfile.setMeet_id(meetId);
        }*/
        //
        //matrimonialProfile.setUserId(Util.getLoginObjectFromPref().getUserDetails().getId());
        //matrimonialProfile.setFirebaseId(Util.getStringPref(Constants.Pref.TOKEN));

        if (Util.isConnected(this)) {
            presenter.UserRegistrationRequests(matrimonialProfile);
        } else {
            Util.showToast(this, getResources().getString(R.string.msg_no_network));
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void imageUploadedSuccessfully(String response, String type) {

        if (Constants.Image.IMAGE_TYPE_PROFILE.equalsIgnoreCase(type)) {
            ReloadFragmentOnImageUploaad(response);
        } else if (Constants.Image.IMAGE_TYPE_ADHARCARD.equalsIgnoreCase(type)) {
            otherMaritialInformation.setAadharUrl(response);
        } else if (Constants.Image.IMAGE_TYPE_EDUCATION.equalsIgnoreCase(type)) {
            otherMaritialInformation.setEducationalUrl(response);
        } else if (Constants.Image.IMAGE_TYPE_MARITAL_CERTIFICATE.equalsIgnoreCase(type)) {
            otherMaritialInformation.setSupport_doc(response);
        }
        //   hideProgressBar();
    }

    public void profileCreatedSuccessfully(String response) {
        String status = response;
        try {
            JSONObject jsonObject = new JSONObject(response);
            status = jsonObject.optString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Util.showToast(this, status);
            /*Login temp = Util.getLoginObjectFromPref();
            temp.getUserDetails().setIsMatrimonialUser(true);
            temp.getUserDetails().setIs_active(true);
            if (TextUtil.isEmpty(temp.getUserDetails().getEmail())) {
                temp.getUserDetails().setEmail(matrimonialProfile.getResidentialDetails().getPrimaryEmailAddress());
            }
            if (TextUtil.isEmpty(temp.getUserDetails().getName())) {
                temp.getUserDetails().setName(matrimonialProfile.getPersonalDetails().getFirstName());
            }
            //        if (temp.getUserDetails().getProfileImage()!=null && TextUtil.isEmpty(temp.getUserDetails().getProfileImage().toString())) {
            temp.getUserDetails().setProfileImage(matrimonialProfile.getOtherMaritalInformation().getProfileImage());
//        }

            Gson gson = new Gson();
            String loin = gson.toJson(temp);
            Util.saveLoginObjectInPref(loin);

            if (Util.getLoginObjectFromPref().getUserDetails().getPaid()) {
                Util.showToast(this, status);
            } else {
                Intent intent = new Intent(this, ProfileMatrimonyPaymentActivity.class);
                intent.putExtra("toOpen", Constants.Payments.PROFILE_REGISTRATION);
                startActivity(intent);
            }*/
        finish();
    }


    @Override
    public void showMessage(String requestID, String message, int code) {
        Util.showToast(this, message);
    }

    @Override
    public void showProgressBar() {
        if (pbLayout != null && pbLayout.getVisibility() == View.GONE) {
            pbLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgressBar() {
        if (pbLayout != null && pbLayout.getVisibility() == View.VISIBLE) {
            pbLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        showDialog(this, "Alert", "Changes you have made will not be saved, Are you sure, want to discard?",
                "Yes", "No");
    }

    public void showDialog(Context context, String dialogTitle, String message, String
            btn1String, String
                                   btn2String) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(context));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_alert_dialog);

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
                finish();
            });
        }

        if (!TextUtils.isEmpty(btn2String)) {
            Button button1 = dialog.findViewById(R.id.btn_dialog_1);
            button1.setText(btn2String);
            button1.setVisibility(View.VISIBLE);
            button1.setOnClickListener(v -> {
                // Close dialog
                dialog.dismiss();
            });
        }

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    public void initView(ProfileDetailData matrimonialProfileReceived) {
        matrimonialProfile = matrimonialProfileReceived.getMatrimonialProfile();
        matrimonialProfile.setUserId(matrimonialProfileReceived.getId());
        setupViewPager(viewPager);
    }


    public class MatrimonyViewPagerAdapter extends SmartFragmentStatePagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();

        MatrimonyViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }
}