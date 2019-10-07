package com.platform.view.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.JsonObject;
import com.platform.R;
import com.platform.models.Matrimony.MatrimonyMasterRequestModel;
import com.platform.models.Matrimony.MatrimonyUserRegRequestModel;
import com.platform.presenter.UserRegistrationMatrimonyActivityPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.adapters.SmartFragmentStatePagerAdapter;
import com.platform.view.fragments.UserRegistrationMatrimonyAboutmeFragment;
import com.platform.view.fragments.UserRegistrationMatrimonyFamilyFragment;
import com.platform.view.fragments.UserRegistrationMatrimonyFragmentOne;
import com.platform.view.fragments.UserRegistrationMatrimonyResidenceFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserRegistrationMatrimonyActivity extends AppCompatActivity {
    private RelativeLayout pbLayout;
    private String meetIdReceived;
    private ViewPager viewPager;
    private MatrimonyViewPagerAdapter matrimonyViewPagerAdapter;
    public List<MatrimonyMasterRequestModel.DataList.Master_data> MasterDataArrayList;
    public static UserRegistrationMatrimonyActivityPresenter userRegistrationMatrimonyActivityPresenter;
    public static MatrimonyUserRegRequestModel matrimonyUserRegRequestModel = new MatrimonyUserRegRequestModel();
    public static MatrimonyUserRegRequestModel.Personal_details personalDetails = new MatrimonyUserRegRequestModel.Personal_details();
    public static MatrimonyUserRegRequestModel.Educational_details educationalDetails = new MatrimonyUserRegRequestModel.Educational_details();
    public static MatrimonyUserRegRequestModel.Family_details familyDetails = new MatrimonyUserRegRequestModel.Family_details();
    public static MatrimonyUserRegRequestModel.Occupational_details occupationalDetails = new MatrimonyUserRegRequestModel.Occupational_details();
    public static MatrimonyUserRegRequestModel.Residential_details residentialDetails = new MatrimonyUserRegRequestModel.Residential_details();
    public static MatrimonyUserRegRequestModel.Other_maritial_information otherMaritialInformation = new MatrimonyUserRegRequestModel.Other_maritial_information();

    public static MatrimonyUserRegRequestModel.Family_details.Gotra gotra = new MatrimonyUserRegRequestModel.Family_details.Gotra();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration_matrimony);
        if (savedInstanceState == null) {
           /* getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, UserRegistrationMatrimonyFragmentOne.newInstance())
                    .commitNow();*/
        }

        meetIdReceived = getIntent().getStringExtra("meetid");
        pbLayout = findViewById(R.id.progress_bar);
        viewPager = findViewById(R.id.approval_cat_view_pager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);
        userRegistrationMatrimonyActivityPresenter = new UserRegistrationMatrimonyActivityPresenter(this);
        userRegistrationMatrimonyActivityPresenter.getAllPendingRequests();
        MasterDataArrayList = new ArrayList<>();
        //matrimonyUserRegRequestModel = new MatrimonyUserRegRequestModel();

    }


    private void setupViewPager(ViewPager viewPager) {
        matrimonyViewPagerAdapter = new MatrimonyViewPagerAdapter(getSupportFragmentManager());

        UserRegistrationMatrimonyFragmentOne userRegistrationMatrimonyFragmentOne = new UserRegistrationMatrimonyFragmentOne();
        Bundle b = new Bundle();
        b.putBoolean("SHOW_ALL", false);
        userRegistrationMatrimonyFragmentOne.setArguments(b);
        matrimonyViewPagerAdapter.addFragment(userRegistrationMatrimonyFragmentOne);


        UserRegistrationMatrimonyFamilyFragment userRegistrationMatrimonyFamilyFragment = new UserRegistrationMatrimonyFamilyFragment();
        matrimonyViewPagerAdapter.addFragment(userRegistrationMatrimonyFamilyFragment);
        viewPager.setAdapter(matrimonyViewPagerAdapter);

        UserRegistrationMatrimonyResidenceFragment userRegistrationMatrimonyResidenceFragment = new UserRegistrationMatrimonyResidenceFragment();
        matrimonyViewPagerAdapter.addFragment(userRegistrationMatrimonyResidenceFragment);
        viewPager.setAdapter(matrimonyViewPagerAdapter);

        UserRegistrationMatrimonyAboutmeFragment userRegistrationMatrimonyAboutmeFragment = new UserRegistrationMatrimonyAboutmeFragment();
        matrimonyViewPagerAdapter.addFragment(userRegistrationMatrimonyAboutmeFragment);
        viewPager.setAdapter(matrimonyViewPagerAdapter);

    }


    //load next fragment for registration
    public void loadNextScreen(int next) {
        switch (next) {
            case 1:
                /*getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, UserRegistrationMatrimonyFragmentOne.newInstance())
                        .commitNow();*/
                viewPager.setCurrentItem(0);
                break;
            case 2:
                viewPager.setCurrentItem(1);
                break;
            case 3:
                viewPager.setCurrentItem(2);
                break;
            case 4:
                viewPager.setCurrentItem(3);
                break;
            default:
                break;

        }


    }

    public void saveMasterData(List<MatrimonyMasterRequestModel.DataList.Master_data> data) {
        MasterDataArrayList =data;
        //MasterDataArrayList.get(0).getValues()
    }


    class MatrimonyViewPagerAdapter extends SmartFragmentStatePagerAdapter {

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

    public void submitUserRegistrationRequest(){
        matrimonyUserRegRequestModel.setMeet_id(meetIdReceived);
        userRegistrationMatrimonyActivityPresenter.UserRegistrationRequests(matrimonyUserRegRequestModel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void imageUploadedSuccessfully(String response,String type)
    {

        if (Constants.Image.IMAGE_TYPE_PROFILE.equalsIgnoreCase(type)) {
            otherMaritialInformation.getProfile_image().add(response);
            //img_user_profle.setImageURI(finalUri);

            if(otherMaritialInformation.getProfile_image()!=null && otherMaritialInformation.getProfile_image().size()>0){
                List<String> temp =otherMaritialInformation.getProfile_image();
                temp.clear();
                temp.add(response);
                otherMaritialInformation.setProfile_image((ArrayList<String>) temp);
            } else {
                List<String> temp =new ArrayList<String>();
                temp.add(response);
                otherMaritialInformation.setProfile_image((ArrayList<String>) temp);
            }

        } else if (Constants.Image.IMAGE_TYPE_ADHARCARD.equalsIgnoreCase(type)) {
            //img_adhar.setImageURI(finalUri);
            otherMaritialInformation.setAadhar_url(response);
        }
        if (Constants.Image.IMAGE_TYPE_EDUCATION.equalsIgnoreCase(type)) {
            otherMaritialInformation.setEducational_url(response);
            //img_education_cert.setImageURI(finalUri);
        }
        hideProgressBar();
    }

    public void profileCreatedSuccessfully(String response)
    {
        String status =response;
        try {
            JSONObject jsonObject = new JSONObject(response);
             status = jsonObject.optString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Util.showToast(status,this);
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        String message = "Profile registration is incomplete,\ndata will be discarded on exit.\nDo you want to Exit? ";
        showApproveRejectDialog(this, 1, " ", message);
    }


    //ApproveReject Confirm dialog
    public void showApproveRejectDialog(final Activity context, int pos, String approvalType, String dialogMessage) {
        Dialog dialog;
        Button btnSubmit, btn_cancel;
        EditText edt_reason;
        TextView tv_message;
        Activity activity = context;

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_exit_confirm_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tv_message = dialog.findViewById(R.id.tv_message);
        edt_reason = dialog.findViewById(R.id.edt_reason);
        btn_cancel = dialog.findViewById(R.id.btn_cancel);
        btnSubmit = dialog.findViewById(R.id.btn_submit);

        tv_message.setText(dialogMessage);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (approvalType.equalsIgnoreCase(Constants.APPROVE)) {
                    //callApproveAPI(pos);
                }
                if (approvalType.equalsIgnoreCase(Constants.REJECT)) {
                    //callRejectAPI(pos);
                }
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();

    }

    public void showProgressBar() {
        if (pbLayout != null && pbLayout.getVisibility() == View.GONE) {
            pbLayout.setVisibility(View.VISIBLE);
        }
    }


    public void hideProgressBar() {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                // Stuff that updates the UI
                if (pbLayout != null && pbLayout.getVisibility() == View.VISIBLE) {
                    pbLayout.setVisibility(View.GONE);
                }

            }
        });
    }
}
