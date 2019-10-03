package com.platform.view.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.gson.Gson;
import com.platform.R;
import com.platform.models.Matrimony.UserProfileList;
import com.platform.presenter.MatrimonyProfilesDetailsActivityPresenter;
import com.platform.presenter.MatrimonyProfilesListActivityPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.adapters.MatrimonyProfileListRecyclerAdapter;
import com.platform.view.adapters.ProfileImagesPagerAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

@SuppressWarnings("CanBeFinal")
public class MatrimonyProfileDetailsActivity extends BaseActivity implements View.OnClickListener {
    private static final Integer[] IMAGES = {R.drawable.profileimagetest, R.drawable.profileimagetest, R.drawable.profileimagetest, R.drawable.profileimagetest};
    //MatrimonyProfileListRecyclerAdapter.OnRequestItemClicked, MatrimonyProfileListRecyclerAdapter.OnApproveRejectClicked {
    private RequestOptions requestOptions;
    private UserProfileList userProfileList;
    private MatrimonyProfilesDetailsActivityPresenter matrimonyProfilesDetailsActivityPresenter;
    private MatrimonyProfileListRecyclerAdapter matrimonyProfileListRecyclerAdapter;
    private RecyclerView rv_matrimonyprofileview;
    private ArrayList<UserProfileList> userProfileLists;
    private ArrayList<String> ProfileImageList = new ArrayList<>();
    private String approvalType, filterTypeReceived;
    private ViewPager vpProfileImage;
    private CircleIndicator indicator;
    private Toolbar toolbar;
    //personal
    private TextView tv_name, tv_birth_date, tv_birth_time, tv_age,
            tv_birth_place, tv_blood_group, tv_marital_status, tv_height, tv_weight_tile, tv_skin_tone,
            tv_manglik, tv_tv_sampraday, tv_disability, tv_smoke, tv_drink;
    //educational and fammily
    private TextView tv_education, tv_occupation, tv_company, tv_business_job, tv_annual_income,
            tv_family_type, tv_sakha_gotra_1, tv_sakha_gotra_2, tv_sakha_gotra_3, tv_sakha_gotra_4,
            tv_father_fullname, tv_father_occupation, tv_mother_fullname, tv_mother_occupation, tv_family_income,
            tv_brothers_fullname, tv_sisters;
    //Residential
    private TextView tv_address, tv_town_city, tv_state, tv_country, tv_primary_mobile, tv_secondary_mobile, tv_primary_email;
    //other
    private TextView tv_about_me, tv_expectations, tv_activity_chievements, tv_other;
    private ImageView iv_aadhar, iv_education_certificates;
    private Button btn_mark_attendance,btn_interview_done;
    private TextView tv_approval_status,tv_premium;
    private String meetIdReceived;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiledetail_layout);
        //receive intent data
        filterTypeReceived = getIntent().getStringExtra("filter_type");
        if (!TextUtils.isEmpty(filterTypeReceived)) {
            Gson gson = new Gson();
            String jsonInString = gson.toJson(userProfileList);
            userProfileList = gson.fromJson(filterTypeReceived, UserProfileList.class);
        }
        meetIdReceived = getIntent().getStringExtra("meetid");
        requestOptions = new RequestOptions().placeholder(R.drawable.ic_no_image);
        requestOptions = requestOptions.apply(RequestOptions.noTransformation());
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        //initViews();
        //tmFilterListActivityPresenter.getAllFiltersRequests();
    }


    private void initView() {
        matrimonyProfilesDetailsActivityPresenter = new MatrimonyProfilesDetailsActivityPresenter(this);
        //for(int i=0;i<IMAGES.length;i++)
        {
            if (userProfileList.getMatrimonial_profile().getOther_marital_information().getProfile_image().size()>0){
                ProfileImageList.addAll(userProfileList.getMatrimonial_profile().getOther_marital_information().getProfile_image());
            }
           /* ProfileImageList.add("https://mvappimages.s3.amazonaws.com/BJS/Images/profile/picture_1567854788767.jpg");
            ProfileImageList.add("https://mvappimages.s3.amazonaws.com/BJS/Images/profile/picture_1567854788767.jpg");
            ProfileImageList.add("https://mvappimages.s3.amazonaws.com/BJS/Images/profile/picture_1567854788767.jpg");*/
        }
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            TextView title = toolbar.findViewById(R.id.toolbar_title);
            title.setText("Profile Details");
        }
        ImageView img_back = findViewById(R.id.toolbar_back_action);
        img_back.setVisibility(View.VISIBLE);
        img_back.setOnClickListener(this);
        //vpProfileImage = findViewById(R.id.vp_profile_images);
        vpProfileImage = (ViewPager) findViewById(R.id.viewpager);
        indicator = (CircleIndicator) findViewById(R.id.circle_indicator);

        setupMeetsViewPager();

        findViewById(R.id.iv_arrow_personal).setOnClickListener(this);
        findViewById(R.id.iv_arrow_education_occupation).setOnClickListener(this);
        findViewById(R.id.iv_arrow_family).setOnClickListener(this);
        findViewById(R.id.iv_arrow_residential).setOnClickListener(this);
        findViewById(R.id.iv_arrow_other).setOnClickListener(this);

        findViewById(R.id.btn_mark_attendance).setOnClickListener(this);
        findViewById(R.id.btn_interview_done).setOnClickListener(this);


        btn_interview_done = findViewById(R.id.btn_interview_done);
        btn_mark_attendance =  findViewById(R.id.btn_mark_attendance);
        //Personal details -
        tv_name = findViewById(R.id.tv_name);
        tv_birth_date = findViewById(R.id.tv_birth_date);
        tv_birth_time = findViewById(R.id.tv_birth_time);
        tv_age = findViewById(R.id.tv_age);
        tv_birth_place = findViewById(R.id.tv_birth_place);
        tv_blood_group = findViewById(R.id.tv_blood_group);
        tv_marital_status = findViewById(R.id.tv_marital_status);
        tv_height = findViewById(R.id.tv_height);
        tv_weight_tile = findViewById(R.id.tv_weight_tile);
        tv_skin_tone = findViewById(R.id.tv_skin_tone);
        tv_manglik = findViewById(R.id.tv_manglik);
        tv_tv_sampraday = findViewById(R.id.tv_tv_sampraday);
        tv_disability = findViewById(R.id.tv_disability);
        tv_smoke = findViewById(R.id.tv_smoke);
        tv_drink = findViewById(R.id.tv_drink);
        tv_approval_status = findViewById(R.id.tv_approval_status);
        tv_premium  = findViewById(R.id.tv_premium);
        if (userProfileList.isIsPremium()){
            tv_premium.setVisibility(View.VISIBLE);
        }

        tv_approval_status.setText(userProfileList.getIsApproved());

        if (userProfileList.getIsApproved().equalsIgnoreCase("approved")){
            if (userProfileList.isMarkAttendance()){
                btn_interview_done.setVisibility(View.VISIBLE);
                btn_mark_attendance.setVisibility(View.VISIBLE);
                btn_mark_attendance.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.button_gray_color));
                btn_mark_attendance.setEnabled(false);
                if (userProfileList.isInterviewDone())
                {
                    btn_interview_done.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.button_gray_color));
                    btn_interview_done.setEnabled(false);
                }
            }else {
                btn_interview_done.setVisibility(View.VISIBLE);
                btn_interview_done.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.button_gray_color));
                btn_interview_done.setEnabled(false);
                btn_mark_attendance.setVisibility(View.VISIBLE);
            }

        }else {
            btn_interview_done.setVisibility(View.GONE);
            btn_mark_attendance.setVisibility(View.GONE);
        }


        userProfileList.getMatrimonial_profile().getPersonal_details().getFirst_name();
        tv_name.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getFirst_name());
        tv_birth_date.setText(Util.getDateFromTimestamp(userProfileList.getMatrimonial_profile().getPersonal_details().getBirthDate(), Constants.EVENT_DATE_FORMAT));
        tv_birth_time.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getBirth_time());
        tv_age.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getAge());
        tv_birth_place.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getBirth_city());
        tv_blood_group.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getBlood_group());
        tv_marital_status.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getMarital_status());
        tv_height.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getHeight());
        tv_weight_tile.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getWeight());
        tv_skin_tone.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getComplexion());
        tv_manglik.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getIs_manglik());
        tv_tv_sampraday.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getSect());
        tv_disability.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getSpecial_case());
        tv_smoke.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getSmoke());
        tv_drink.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getDrink());

        //educational and Family Details

        tv_education = findViewById(R.id.tv_education);
        tv_occupation = findViewById(R.id.tv_occupation);
        tv_company = findViewById(R.id.tv_company);
        tv_business_job = findViewById(R.id.tv_business_job);
        tv_annual_income = findViewById(R.id.tv_annual_income);
        tv_family_type = findViewById(R.id.tv_family_type);
        tv_sakha_gotra_1 = findViewById(R.id.tv_sakha_gotra_1);
        tv_sakha_gotra_2 = findViewById(R.id.tv_sakha_gotra_2);
        tv_sakha_gotra_3 = findViewById(R.id.tv_sakha_gotra_3);
        tv_sakha_gotra_4 = findViewById(R.id.tv_sakha_gotra_4);
        tv_father_fullname = findViewById(R.id.tv_father_fullname);
        tv_father_occupation = findViewById(R.id.tv_father_occupation);
        tv_mother_fullname = findViewById(R.id.tv_mother_fullname);
        tv_mother_occupation = findViewById(R.id.tv_mother_occupation);
        tv_family_income = findViewById(R.id.tv_family_income);
        tv_brothers_fullname = findViewById(R.id.tv_brothers_fullname);
        tv_sisters = findViewById(R.id.tv_sisters);

        tv_education.setText(userProfileList.getMatrimonial_profile().getEducational_details().getEducation_level());
        tv_occupation.setText(userProfileList.getMatrimonial_profile().getOccupational_details().getOccupation());
        tv_company.setText(userProfileList.getMatrimonial_profile().getOccupational_details().getEmployer_company());
        tv_business_job.setText(userProfileList.getMatrimonial_profile().getOccupational_details().getBusiness_description());
        tv_annual_income.setText(userProfileList.getMatrimonial_profile().getEducational_details().getIncome());
        tv_family_type.setText(userProfileList.getMatrimonial_profile().getFamily_details().getFamily_type());
        tv_sakha_gotra_1.setText(userProfileList.getMatrimonial_profile().getFamily_details().getGotra().getSelf_gotra());
        tv_sakha_gotra_2.setText(userProfileList.getMatrimonial_profile().getFamily_details().getGotra().getDada_gotra());
        tv_sakha_gotra_3.setText(userProfileList.getMatrimonial_profile().getFamily_details().getGotra().getMama_gotra());
        tv_sakha_gotra_4.setText(userProfileList.getMatrimonial_profile().getFamily_details().getGotra().getNana_gotra());
        tv_father_fullname.setText(userProfileList.getMatrimonial_profile().getFamily_details().getFather_name());
        tv_father_occupation.setText(userProfileList.getMatrimonial_profile().getFamily_details().getFather_occupation());
        tv_mother_fullname.setText(userProfileList.getMatrimonial_profile().getFamily_details().getMother_name());
        tv_mother_occupation.setText(userProfileList.getMatrimonial_profile().getFamily_details().getMother_occupation());
        tv_family_income.setText(userProfileList.getMatrimonial_profile().getFamily_details().getFamily_income());
        tv_brothers_fullname.setText(userProfileList.getMatrimonial_profile().getFamily_details().getBrother_count());
        tv_sisters.setText(userProfileList.getMatrimonial_profile().getFamily_details().getSister_count());


        //Residence deatails
        tv_address = findViewById(R.id.tv_address);
        tv_town_city = findViewById(R.id.tv_town_city);
        tv_state = findViewById(R.id.tv_state);
        tv_country = findViewById(R.id.tv_country);
        tv_primary_mobile = findViewById(R.id.tv_primary_mobile);
        tv_secondary_mobile = findViewById(R.id.tv_secondary_mobile);
        tv_primary_email = findViewById(R.id.tv_primary_email);

        tv_address.setText(userProfileList.getMatrimonial_profile().getResidential_details().getAddress());
        tv_town_city.setText(userProfileList.getMatrimonial_profile().getResidential_details().getCity());
        tv_state.setText(userProfileList.getMatrimonial_profile().getResidential_details().getState());
        tv_country.setText(userProfileList.getMatrimonial_profile().getResidential_details().getCountry());
        tv_primary_mobile.setText(userProfileList.getMatrimonial_profile().getResidential_details().getPrimary_phone());
        tv_secondary_mobile.setText(userProfileList.getMatrimonial_profile().getResidential_details().getSecondary_phone());
        tv_primary_email.setText(userProfileList.getMatrimonial_profile().getResidential_details().getPrimary_email_address());

        //other
        tv_about_me = findViewById(R.id.tv_about_me);
        tv_expectations = findViewById(R.id.tv_expectations);
        tv_activity_chievements = findViewById(R.id.tv_activity_chievements);
        tv_other = findViewById(R.id.tv_other);

        tv_about_me.setText(userProfileList.getMatrimonial_profile().getOther_marital_information().getAbout_me());
        tv_expectations.setText(userProfileList.getMatrimonial_profile().getOther_marital_information().getActivity_achievements());
        tv_activity_chievements.setText(userProfileList.getMatrimonial_profile().getOther_marital_information().getActivity_achievements());
        tv_other.setText(userProfileList.getMatrimonial_profile().getOther_marital_information().getOther_remarks());

        //images
        iv_aadhar = findViewById(R.id.iv_aadhar);
        iv_education_certificates = findViewById(R.id.iv_education_certificates);
        iv_aadhar.setOnClickListener(this);
        iv_education_certificates.setOnClickListener(this);
        if (!TextUtils.isEmpty(userProfileList.getMatrimonial_profile().getOther_marital_information().getAadhar_url())) {
            Glide.with(this)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(userProfileList.getMatrimonial_profile().getOther_marital_information().getAadhar_url())
                    .into(iv_aadhar);
        }
        if (!TextUtils.isEmpty(userProfileList.getMatrimonial_profile().getOther_marital_information().getEducational_url())) {
            Glide.with(this)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(userProfileList.getMatrimonial_profile().getOther_marital_information().getEducational_url())
                    .into(iv_education_certificates);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_aadhar:
                enlargePhoto(userProfileList.getMatrimonial_profile().getOther_marital_information().getAadhar_url());
                break;
            case R.id.iv_education_certificates:
                enlargePhoto(userProfileList.getMatrimonial_profile().getOther_marital_information().getEducational_url());
                break;
            case R.id.iv_arrow_personal:
                if (findViewById(R.id.ly_personal_details).getVisibility() != View.VISIBLE) {
                    findViewById(R.id.ly_personal_details).setVisibility(View.VISIBLE);
                    findViewById(R.id.iv_arrow_personal).setRotation(180);
                } else {
                    findViewById(R.id.ly_personal_details).setVisibility(View.GONE);
                    findViewById(R.id.iv_arrow_personal).setRotation(0);
                }

                break;
            case R.id.iv_arrow_education_occupation:
                if (findViewById(R.id.ly_education_occupation_details).getVisibility() != View.VISIBLE) {
                    findViewById(R.id.ly_education_occupation_details).setVisibility(View.VISIBLE);
                    findViewById(R.id.iv_arrow_education_occupation).setRotation(180);
                } else {
                    findViewById(R.id.ly_education_occupation_details).setVisibility(View.GONE);
                    findViewById(R.id.iv_arrow_education_occupation).setRotation(0);
                }
                break;
            case R.id.iv_arrow_family:
                if (findViewById(R.id.ly_family_details).getVisibility() != View.VISIBLE) {
                    findViewById(R.id.ly_family_details).setVisibility(View.VISIBLE);
                    findViewById(R.id.iv_arrow_family).setRotation(180);
                } else {
                    findViewById(R.id.ly_family_details).setVisibility(View.GONE);
                    findViewById(R.id.iv_arrow_family).setRotation(0);
                }
                break;
            case R.id.iv_arrow_residential:
                if (findViewById(R.id.ly_residential_details).getVisibility() != View.VISIBLE) {
                    findViewById(R.id.ly_residential_details).setVisibility(View.VISIBLE);
                    findViewById(R.id.iv_arrow_residential).setRotation(180);
                } else {
                    findViewById(R.id.ly_residential_details).setVisibility(View.GONE);
                    findViewById(R.id.iv_arrow_residential).setRotation(0);
                }
                break;

            case R.id.iv_arrow_other:
                if (findViewById(R.id.ly_other_details).getVisibility() != View.VISIBLE) {
                    findViewById(R.id.ly_other_details).setVisibility(View.VISIBLE);
                    findViewById(R.id.iv_arrow_other).setRotation(180);
                } else {
                    findViewById(R.id.ly_other_details).setVisibility(View.GONE);
                    findViewById(R.id.iv_arrow_other).setRotation(0);
                }
                break;
            case R.id.btn_mark_attendance:
                JSONObject jsonObject = matrimonyProfilesDetailsActivityPresenter.createBodyParams(meetIdReceived, userProfileList.get_id(), Constants.MARK_ATTENDANCE);
                matrimonyProfilesDetailsActivityPresenter.markAttendanceRequest(jsonObject,1,Constants.MARK_ATTENDANCE);

                break;
            case R.id.btn_interview_done:
                JSONObject jsonObj = matrimonyProfilesDetailsActivityPresenter.createBodyParams(meetIdReceived, userProfileList.get_id(), Constants.MARK_INTERVIEW);
                matrimonyProfilesDetailsActivityPresenter.markAttendanceRequest(jsonObj,1,Constants.MARK_INTERVIEW);
                break;
            case R.id.toolbar_back_action:
                finish();
                break;


        }
    }

    private void setupMeetsViewPager() {

        ProfileImagesPagerAdapter adapter = new ProfileImagesPagerAdapter(MatrimonyProfileDetailsActivity.this, ProfileImageList);

        vpProfileImage.setAdapter(adapter);
        indicator.setViewPager(vpProfileImage);
        vpProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        /*String json = null;
        try {
            InputStream is = getApplicationContext().getResources().getAssets().open("recommended.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Gson gson = new Gson();
       *//* RecomandedResponse data = gson.fromJson(json, RecomandedResponse.class);

        for (UserProfile userProfile : data.getData()) {
            VPProfileFragment profileFragment = new VPProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(USER_PROFILE_DATA, userProfile);
            profileFragment.setArguments(bundle);
            adapter.addFragment(profileFragment, "Profile");
        }*//*

        vpProfileImage.setAdapter(adapter);*/

    }


    /*private void setActionbar(String title) {
        if (title.contains("\n")) {
            title = title.replace("\n", " ");
        }

        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);

        ImageView img_back = findViewById(R.id.toolbar_back_action);
        img_back.setVisibility(View.VISIBLE);
        img_back.setOnClickListener(this);
    }
*/

/*    public void showPendingApprovalRequests(List<UserProfileList> pendingRequestList) {
        if (!pendingRequestList.isEmpty()) {
            userProfileLists = (ArrayList<UserProfileList>) pendingRequestList;
            matrimonyProfileListRecyclerAdapter = new MatrimonyProfileListRecyclerAdapter(this, pendingRequestList,
                    this, this);
            rv_matrimonyprofileview.setAdapter(matrimonyProfileListRecyclerAdapter);
        } else {
     
        }
    }

    @Override
    public void onItemClicked(int pos) {
        Util.showToast("item ->" + pos + " Open detais", this);
        UserProfileList userProfileList = userProfileLists.get(pos);
    }

    @Override
    public void onApproveClicked(int pos) {
        Util.showToast("item ->" + pos + " Approve Clicked", this);
        UserProfileList userProfileList = userProfileLists.get(pos);

        JSONObject jsonObject = tmFilterListActivityPresenter.createBodyParams("5d6f90c25dda765c2f0b5dd4", "user", userProfileList.get_id(), Constants.APPROVE);
        tmFilterListActivityPresenter.approveRejectRequest(jsonObject, 0,Constants.APPROVE);
    }

    @Override
    public void onRejectClicked(int pos) {
        Util.showToast("item ->" + pos + " Reject Clicked", this);
        UserProfileList userProfileList = userProfileLists.get(pos);

        JSONObject jsonObject = tmFilterListActivityPresenter.createBodyParams("5d6f90c25dda765c2f0b5dd4", "user", userProfileList.get_id(), Constants.REJECT);
        tmFilterListActivityPresenter.approveRejectRequest(jsonObject, 0,Constants.REJECT);
    }

    public void updateRequestStatus(String response, int position) {
        if (Constants.REJECT.equalsIgnoreCase(approvalType)){
            userProfileLists.get(position).setIsApproved(Constants.APPROVE);
        }
        if (Constants.APPROVE.equalsIgnoreCase(approvalType)){
            userProfileLists.get(position).setIsApproved(Constants.REJECT);
        }
    }*/

    private void enlargePhoto(String photoUrl) {
        // stop the video if playing

        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        LayoutInflater factory = LayoutInflater.from(this);
        final View enlargePhotoView = factory.inflate(
                R.layout.enlarge_photo_layout, null);
        FrameLayout closeOption = (FrameLayout) enlargePhotoView.findViewById(R.id.close_layout);
        closeOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        PhotoView photoView = (PhotoView) enlargePhotoView.findViewById(R.id.img_feed_photo);
        ImageView closeImageView = (ImageView) enlargePhotoView.findViewById(R.id.img_close_dialog);
        closeImageView.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(photoUrl)) {
            try {
                Glide.with(this)
                        .load(photoUrl)
                        /*.placeholder(R.drawable.image_placeholder)*/
                        .into(photoView);
            } catch (Exception e) {
                e.printStackTrace();
                photoView.setImageResource(R.drawable.ic_user_avatar);
            }
        } else {
            photoView.setImageResource(R.drawable.ic_user_avatar);
        }

        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(enlargePhotoView);
        dialog.show();

    }

    public void updateRequestStatus(String response, int position, String requestType) {
        if (Constants.MARK_ATTENDANCE.equalsIgnoreCase(requestType)){
            //Util.showToast(requestType+" Success",this);
            btn_interview_done.setEnabled(true);
            btn_interview_done.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary));
            btn_mark_attendance.setEnabled(false);
            btn_mark_attendance.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.button_gray_color));
            Util.showSuccessFailureToast(response, this, getWindow().getDecorView()
                    .findViewById(android.R.id.content));
        }else if (Constants.MARK_INTERVIEW.equalsIgnoreCase(requestType)){
            //Util.showToast(requestType+" Success",this);
            Util.showSuccessFailureToast(response, this, getWindow().getDecorView()
                    .findViewById(android.R.id.content));
            btn_interview_done.setEnabled(false);
            btn_interview_done.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.button_gray_color));
            btn_mark_attendance.setEnabled(false);
            btn_mark_attendance.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.button_gray_color));
        }
    }
}
