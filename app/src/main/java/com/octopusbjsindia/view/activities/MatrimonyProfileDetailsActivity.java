package com.octopusbjsindia.view.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.matrimonyregistration.RegistrationActivity;
import com.octopusbjsindia.matrimonyregistration.model.MatrimonialProfile;
import com.octopusbjsindia.matrimonyregistration.model.ProfileDetailData;
import com.octopusbjsindia.matrimonyregistration.model.ProfileDetailResponse;
import com.octopusbjsindia.models.Matrimony.MatrimonyMeet;
import com.octopusbjsindia.models.Matrimony.UserProfileList;
import com.octopusbjsindia.presenter.MatrimonyProfilesDetailsActivityPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.ProfileImagesPagerAdapter;
import com.octopusbjsindia.widgets.MeetListBottomSheet;
import com.octopusbjsindia.widgets.SingleSelectBottomSheet;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import me.relex.circleindicator.CircleIndicator;

@SuppressWarnings("CanBeFinal")
public class MatrimonyProfileDetailsActivity extends BaseActivity implements View.OnClickListener,
        PopupMenu.OnMenuItemClickListener, APIDataListener,MeetListBottomSheet.MultiSpinnerListener {
    private static final Integer[] IMAGES = {R.drawable.profileimagetest, R.drawable.profileimagetest, R.drawable.profileimagetest, R.drawable.profileimagetest};
    //MatrimonyProfileListRecyclerAdapter.OnRequestItemClicked, MatrimonyProfileListRecyclerAdapter.OnApproveRejectClicked {
    private final String PROFILE_ID = "profileId";
    private RequestOptions docRequestOptions;
    private RequestOptions certificateRequestOptions;
    private UserProfileList userProfileList;
    ProfileDetailData matrimonialProfileReceived ;
    ProfileDetailResponse profileResponse;
    public MatrimonialProfile matrimonialProfile = new MatrimonialProfile();
    private MatrimonyProfilesDetailsActivityPresenter presenter;
    private ArrayList<String> ProfileImageList = new ArrayList<>();
    private String filterTypeReceived;
    private ViewPager vpProfileImage;
    private CircleIndicator indicator;
    private String approvalType;
    private int receivedPos = -1;
    //personal
    private TextView tv_gender, tv_name, tv_birth_date, tv_birth_time, tv_age,
            tv_birth_place, tv_blood_group, tv_marital_status, tv_height, tv_weight_tile, tv_skin_tone,
            tv_manglik, tv_tv_sampraday, tv_disability, tv_smoke, tv_drink;
    //educational and fammily
    private TextView tv_education, tv_occupation, tv_company, tv_business_job, tv_annual_income, tv_degree,
            tv_family_type, tv_sakha_gotra_1, tv_sakha_gotra_2, tv_sakha_gotra_3, tv_sakha_gotra_4,
            tv_father_fullname, tv_father_occupation, tv_mother_fullname, tv_mother_occupation, tv_family_income,
            tv_brothers_fullname, tv_sisters;
    //Residential
    private TextView tv_address, tv_town_city, tv_state, tv_country, tv_primary_mobile, tv_secondary_mobile, tv_primary_email;
    //other
    private TextView tv_about_me, tv_expectations, tv_activity_chievements, tv_other, tv_myproof_title;
    private ImageView iv_aadhar, iv_education_certificates, iv_myproof_certificate, toolbar_edit_action;
    private Button btn_mark_attendance, btn_interview_done, btnReject, btnApprove, btn_verify_ids, btn_verify_edu, btn_verify_profile;
    private TextView tv_approval_status, tv_premium,registered_meet_list,registered_meet_list_title;
    private String meetIdReceived = "";
    private RelativeLayout progressBar, ly_myproof;
    private boolean isBlock;
    private CardView cv_meetlist_details;
    private MeetListBottomSheet bottomSheetDialogFragment;
    private List<MatrimonyMeet> meetListReceived;

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
        receivedPos = getIntent().getIntExtra("selectedPos", -1);
        docRequestOptions = new RequestOptions().placeholder(R.drawable.ic_doc_placeholder);
        docRequestOptions = docRequestOptions.apply(RequestOptions.noTransformation());
        certificateRequestOptions = new RequestOptions().placeholder(R.drawable.ic_certifcate_placeholder);
        certificateRequestOptions = certificateRequestOptions.apply(RequestOptions.noTransformation());
        progressBar = findViewById(R.id.progress_bar);
        //initView();
        presenter = new MatrimonyProfilesDetailsActivityPresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        //initViews();
        //tmFilterListActivityPresenter.getAllFiltersRequests();
        if (Util.isConnected(this)){
            presenter.getProfile(userProfileList.get_id(), "");
        } else {
            //Util.showToast(getString(R.string.msg_no_network), this);
            Util.snackBarToShowMsg(getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.msg_no_network),
                    Snackbar.LENGTH_LONG);
        }
    }
    /*public void onProfileReceived(ProfileDetailData matrimonialProfileReceived) {
        matrimonialProfileReceived = matrimonialProfileReceived;
        matrimonialProfile = matrimonialProfileReceived.getMatrimonialProfile();
        matrimonialProfile.setUserId(matrimonialProfileReceived.getId());
        initView();
    }*/
    public void onProfileReceived(String matrimonialProfileReceived) {
        profileResponse = new Gson().fromJson(matrimonialProfileReceived, ProfileDetailResponse.class);


        matrimonialProfile = profileResponse.getData().getMatrimonialProfile();
        matrimonialProfile.setUserId(profileResponse.getData().getId());
        initView();
    }

    private void initView() {

        //for(int i=0;i<IMAGES.length;i++)
        {
            if (matrimonialProfile.getOtherMaritalInformation().getProfileImage() != null
                    && matrimonialProfile.getOtherMaritalInformation().getProfileImage().size() > 0) {
                ProfileImageList.clear();
                ProfileImageList.addAll(matrimonialProfile.getOtherMaritalInformation().getProfileImage());
            }
        }

        TextView title = findViewById(R.id.toolbar_title);
        title.setText("Profile Details");
        cv_meetlist_details  = findViewById(R.id.cv_meetlist_details);
        registered_meet_list = findViewById(R.id.registered_meet_list);

        if (profileResponse.getData().getMeetList()!=null) {
            if (profileResponse.getData().getMeetList().size()>0) {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < profileResponse.getData().getMeetList().size(); i++) {
                    builder.append((i+1)+". "+profileResponse.getData().getMeetList().get(i));
                    builder.append("\n");
                }
                //registered_meet_list_title.setText("Meets Registered :");
                registered_meet_list.setText(builder.toString().trim());
            }else {
                cv_meetlist_details.setVisibility(View.GONE);
                registered_meet_list.setVisibility(View.GONE);
            }
        }
        /*registered_meet_list = findViewById(R.id.registered_meet_list);
        if (matrimonialProfileReceived.getMeetList().get(0)!=null) {
            registered_meet_list.setText(matrimonialProfileReceived.getMeetList().get(0));
        }*/
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
        findViewById(R.id.iv_arrow_meetlist_details).setOnClickListener(this);

        findViewById(R.id.btn_mark_attendance).setOnClickListener(this);
        findViewById(R.id.btn_interview_done).setOnClickListener(this);
        findViewById(R.id.btn_reject).setOnClickListener(this);
        findViewById(R.id.btn_approve).setOnClickListener(this);
        findViewById(R.id.btn_verify_profile).setOnClickListener(this);
        findViewById(R.id.btn_verify_ids).setOnClickListener(this);
        findViewById(R.id.btn_verify_edu).setOnClickListener(this);
        ImageView popupMenu = findViewById(R.id.toolbar_edit_action);
        popupMenu.setVisibility(View.VISIBLE);
        popupMenu.setImageResource(R.drawable.ic_popup_menu);
        popupMenu.setOnClickListener(this);

        btn_interview_done = findViewById(R.id.btn_interview_done);
        btn_mark_attendance = findViewById(R.id.btn_mark_attendance);
        btn_verify_ids = findViewById(R.id.btn_verify_ids);
        btn_verify_edu = findViewById(R.id.btn_verify_edu);
        //Personal details -
        tv_name = findViewById(R.id.tv_name);
        tv_gender = findViewById(R.id.tv_gender);
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
        //tv_approval_status = findViewById(R.id.tv_approval_status);
        tv_premium = findViewById(R.id.tv_premium);
//        if (userProfileList.isIsPremium()) {
//            tv_premium.setVisibility(View.VISIBLE);
//            findViewById(R.id.ly_premium).setVisibility(View.INVISIBLE);
//        }

        if (userProfileList.isPaid()) {
            findViewById(R.id.ly_premium).setVisibility(View.VISIBLE);
            findViewById(R.id.ly_premium).setOnClickListener(this);
        } else {
            findViewById(R.id.ly_premium).setVisibility(View.GONE);
            //findViewById(R.id.ly_premium).setOnClickListener(this);
        }

        setApprovelFlag();

        TextView txtTitle = findViewById(R.id.tv_title);
        TextView txtValue = findViewById(R.id.tv_value);
        txtTitle.setText(matrimonialProfile.getPersonalDetails().getFirstName()
                + " " + matrimonialProfile.getPersonalDetails().getLastName());
        String s = new StringBuffer().append(String.valueOf(matrimonialProfile.getPersonalDetails().getAge() + " years, "))
                .append(matrimonialProfile.getEducationalDetails().getQualificationDegree() + ", ")
                .append(matrimonialProfile.getPersonalDetails().getMaritalStatus() + ", ")
                .append(matrimonialProfile.getPersonalDetails().getSect()).toString();
        txtValue.setText(s);

        userProfileList.getMatrimonial_profile().getPersonal_details().getFirst_name();
        tv_name.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getFirst_name() + " " + userProfileList.getMatrimonial_profile().getPersonal_details().getMiddle_name()
                + " " + userProfileList.getMatrimonial_profile().getPersonal_details().getLast_name());
        tv_gender.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getGender());
        tv_birth_date.setText(Util.getDateFromTimestamp(userProfileList.getMatrimonial_profile().getPersonal_details().getBirthDate(), Constants.EVENT_DATE_FORMAT));
        tv_birth_time.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getBirth_time());
        tv_age.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getAge());
        tv_birth_place.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getBirth_city());
        tv_blood_group.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getBlood_group());
        tv_marital_status.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getMarital_status());

        if (!userProfileList.getMatrimonial_profile().getPersonal_details().getMarital_status().equalsIgnoreCase("Unmarried")) {
            LinearLayout lyChildrenInfo = findViewById(R.id.ly_children_info);
            lyChildrenInfo.setVisibility(View.VISIBLE);
            RelativeLayout rlChildrenCount = findViewById(R.id.rl_children_count);
            rlChildrenCount.setVisibility(View.VISIBLE);
            TextView tvChildrenCount = findViewById(R.id.tv_children_count);
            if (userProfileList.getMatrimonial_profile().getPersonal_details().getHaveChildren() != null) {
                if (userProfileList.getMatrimonial_profile().getPersonal_details().getHaveChildren().equalsIgnoreCase("Yes")) {
                    tvChildrenCount.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getChildrenCount());
                } else {
                    tvChildrenCount.setText("No");
                }
            } else {
                tvChildrenCount.setText("Not mentioned");
            }
            if (userProfileList.getMatrimonial_profile().getPersonal_details().getMarital_status().equalsIgnoreCase("Divorcee")) {
                RelativeLayout rlLegallyDivorce = findViewById(R.id.rl_legally_divorce);
                rlLegallyDivorce.setVisibility(View.VISIBLE);
                TextView tvLegalDivorce = findViewById(R.id.tv_legal_divorce);
                tvLegalDivorce.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getIsDivorcedLegal());
            }
        }


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
        tv_degree = findViewById(R.id.tv_degree);
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
        if (!TextUtils.isEmpty(userProfileList.getMatrimonial_profile().getEducational_details().getQualification_degree())) {
            tv_degree.setText(userProfileList.getMatrimonial_profile().getEducational_details().getQualification_degree());
        }
        tv_family_type.setText(userProfileList.getMatrimonial_profile().getFamily_details().getFamily_type());
        tv_sakha_gotra_1.setText(userProfileList.getMatrimonial_profile().getFamily_details().getGotra().getSelf_gotra());
        tv_sakha_gotra_2.setText(userProfileList.getMatrimonial_profile().getFamily_details().getGotra().getMama_gotra());
        tv_sakha_gotra_3.setText(userProfileList.getMatrimonial_profile().getFamily_details().getGotra().getDada_gotra());
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
        tv_expectations.setText(userProfileList.getMatrimonial_profile().getOther_marital_information().getExpectation_from_life_partner());
        tv_activity_chievements.setText(userProfileList.getMatrimonial_profile().getOther_marital_information().getActivity_achievements());
        tv_other.setText(userProfileList.getMatrimonial_profile().getOther_marital_information().getOther_remarks());

        //images

        iv_myproof_certificate = findViewById(R.id.iv_myproof_certificate);
        ly_myproof = findViewById(R.id.ly_myproof);


        iv_myproof_certificate.setOnClickListener(this);
        tv_myproof_title = findViewById(R.id.tv_myproof_title);
        tv_myproof_title.setOnClickListener(this);

        iv_aadhar = findViewById(R.id.iv_aadhar);
        iv_education_certificates = findViewById(R.id.iv_education_certificates);
        iv_aadhar.setOnClickListener(this);
        iv_education_certificates.setOnClickListener(this);
        if (!TextUtils.isEmpty(userProfileList.getMatrimonial_profile().getOther_marital_information().getAadhar_url())) {
            Glide.with(this)
                    .applyDefaultRequestOptions(docRequestOptions)
                    .load(userProfileList.getMatrimonial_profile().getOther_marital_information().getAadhar_url())
                    .into(iv_aadhar);
        }
        if (!TextUtils.isEmpty(userProfileList.getMatrimonial_profile().getOther_marital_information().getEducational_url())) {
            Glide.with(this)
                    .applyDefaultRequestOptions(certificateRequestOptions)
                    .load(userProfileList.getMatrimonial_profile().getOther_marital_information().getEducational_url())
                    .into(iv_education_certificates);
        }
        if (userProfileList.getMatrimonial_profile().getOther_marital_information().getSupport_doc() != null) {
            if (!TextUtils.isEmpty(userProfileList.getMatrimonial_profile().getOther_marital_information().getSupport_doc())) {
                ly_myproof.setVisibility(View.VISIBLE);
                tv_myproof_title.setVisibility(View.VISIBLE);
                if (userProfileList.getMatrimonial_profile().getPersonal_details().
                        getMarital_status().equalsIgnoreCase("Divorcee")) {
                    tv_myproof_title.setText("Legal seperation certificate");
                } else {
                    tv_myproof_title.setText("Partner's death certificate");
                }
                Glide.with(this)
                        .applyDefaultRequestOptions(certificateRequestOptions)
                        .load(userProfileList.getMatrimonial_profile().getOther_marital_information().getSupport_doc())
                        .into(iv_myproof_certificate);


            }else {
                if (userProfileList.getMatrimonial_profile().getPersonal_details().getMarital_status().equalsIgnoreCase("Unmarried")) {
                    tv_myproof_title.setVisibility(View.GONE);
                    ly_myproof.setVisibility(View.GONE);
                }
            }

        }

        tv_primary_mobile.setOnClickListener(this);
        tv_secondary_mobile.setOnClickListener(this);
        tv_primary_email.setOnClickListener(this);

    }

    private void initViewOLD() {
        presenter = new MatrimonyProfilesDetailsActivityPresenter(this);
        //for(int i=0;i<IMAGES.length;i++)
        {
            if (userProfileList.getMatrimonial_profile().getOther_marital_information().getProfile_image() != null
                    && userProfileList.getMatrimonial_profile().getOther_marital_information().getProfile_image().size() > 0) {
                ProfileImageList.addAll(userProfileList.getMatrimonial_profile().getOther_marital_information().getProfile_image());
            }
        }

        TextView title = findViewById(R.id.toolbar_title);
        title.setText("Profile Details");

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
        findViewById(R.id.iv_arrow_meetlist_details).setOnClickListener(this);
        findViewById(R.id.btn_mark_attendance).setOnClickListener(this);
        findViewById(R.id.btn_interview_done).setOnClickListener(this);
        findViewById(R.id.btn_reject).setOnClickListener(this);
        findViewById(R.id.btn_approve).setOnClickListener(this);
        findViewById(R.id.btn_verify_profile).setOnClickListener(this);
        findViewById(R.id.btn_verify_ids).setOnClickListener(this);
        findViewById(R.id.btn_verify_edu).setOnClickListener(this);
        ImageView popupMenu = findViewById(R.id.toolbar_edit_action);
        popupMenu.setVisibility(View.VISIBLE);
        popupMenu.setImageResource(R.drawable.ic_popup_menu);
        popupMenu.setOnClickListener(this);

        btn_interview_done = findViewById(R.id.btn_interview_done);
        btn_mark_attendance = findViewById(R.id.btn_mark_attendance);
        btn_verify_ids = findViewById(R.id.btn_verify_ids);
        btn_verify_edu = findViewById(R.id.btn_verify_edu);
        //Personal details -
        tv_name = findViewById(R.id.tv_name);
        tv_gender = findViewById(R.id.tv_gender);
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
        //tv_approval_status = findViewById(R.id.tv_approval_status);
        tv_premium = findViewById(R.id.tv_premium);
//        if (userProfileList.isIsPremium()) {
//            tv_premium.setVisibility(View.VISIBLE);
//            findViewById(R.id.ly_premium).setVisibility(View.INVISIBLE);
//        }

        if (userProfileList.isPaid()) {
            findViewById(R.id.ly_premium).setVisibility(View.VISIBLE);
            findViewById(R.id.ly_premium).setOnClickListener(this);
        } else {
            findViewById(R.id.ly_premium).setVisibility(View.GONE);
            //findViewById(R.id.ly_premium).setOnClickListener(this);
        }

        setApprovelFlag();

        TextView txtTitle = findViewById(R.id.tv_title);
        TextView txtValue = findViewById(R.id.tv_value);
        txtTitle.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getFirst_name()
                + " " + userProfileList.getMatrimonial_profile().getPersonal_details().getLast_name());
        String s = new StringBuffer().append(String.valueOf(userProfileList.getMatrimonial_profile().
                getPersonal_details().getAge() + " years, "))
                .append(userProfileList.getMatrimonial_profile().getEducational_details().getQualification_degree() + ", ")
                .append(userProfileList.getMatrimonial_profile().getPersonal_details().getMarital_status() + ", ")
                .append(userProfileList.getMatrimonial_profile().getPersonal_details().getSect()).toString();
        txtValue.setText(s);

        userProfileList.getMatrimonial_profile().getPersonal_details().getFirst_name();
        tv_name.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getFirst_name() + " " + userProfileList.getMatrimonial_profile().getPersonal_details().getMiddle_name()
                + " " + userProfileList.getMatrimonial_profile().getPersonal_details().getLast_name());
        tv_gender.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getGender());
        tv_birth_date.setText(Util.getDateFromTimestamp(userProfileList.getMatrimonial_profile().getPersonal_details().getBirthDate(), Constants.EVENT_DATE_FORMAT));
        tv_birth_time.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getBirth_time());
        tv_age.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getAge());
        tv_birth_place.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getBirth_city());
        tv_blood_group.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getBlood_group());
        tv_marital_status.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getMarital_status());

        if (!userProfileList.getMatrimonial_profile().getPersonal_details().getMarital_status().equalsIgnoreCase("Unmarried")) {
            LinearLayout lyChildrenInfo = findViewById(R.id.ly_children_info);
            lyChildrenInfo.setVisibility(View.VISIBLE);
            RelativeLayout rlChildrenCount = findViewById(R.id.rl_children_count);
            rlChildrenCount.setVisibility(View.VISIBLE);
            TextView tvChildrenCount = findViewById(R.id.tv_children_count);
            if (userProfileList.getMatrimonial_profile().getPersonal_details().getHaveChildren() != null) {
                if (userProfileList.getMatrimonial_profile().getPersonal_details().getHaveChildren().equalsIgnoreCase("Yes")) {
                    tvChildrenCount.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getChildrenCount());
                } else {
                    tvChildrenCount.setText("No");
                }
            } else {
                tvChildrenCount.setText("Not mentioned");
            }
            if (userProfileList.getMatrimonial_profile().getPersonal_details().getMarital_status().equalsIgnoreCase("Divorcee")) {
                RelativeLayout rlLegallyDivorce = findViewById(R.id.rl_legally_divorce);
                rlLegallyDivorce.setVisibility(View.VISIBLE);
                TextView tvLegalDivorce = findViewById(R.id.tv_legal_divorce);
                tvLegalDivorce.setText(userProfileList.getMatrimonial_profile().getPersonal_details().getIsDivorcedLegal());
            }
        }


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
        tv_degree = findViewById(R.id.tv_degree);
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
        if (!TextUtils.isEmpty(userProfileList.getMatrimonial_profile().getEducational_details().getQualification_degree())) {
            tv_degree.setText(userProfileList.getMatrimonial_profile().getEducational_details().getQualification_degree());
        }
        tv_family_type.setText(userProfileList.getMatrimonial_profile().getFamily_details().getFamily_type());
        tv_sakha_gotra_1.setText(userProfileList.getMatrimonial_profile().getFamily_details().getGotra().getSelf_gotra());
        tv_sakha_gotra_2.setText(userProfileList.getMatrimonial_profile().getFamily_details().getGotra().getMama_gotra());
        tv_sakha_gotra_3.setText(userProfileList.getMatrimonial_profile().getFamily_details().getGotra().getDada_gotra());
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
        tv_expectations.setText(userProfileList.getMatrimonial_profile().getOther_marital_information().getExpectation_from_life_partner());
        tv_activity_chievements.setText(userProfileList.getMatrimonial_profile().getOther_marital_information().getActivity_achievements());
        tv_other.setText(userProfileList.getMatrimonial_profile().getOther_marital_information().getOther_remarks());

        //images

        iv_myproof_certificate = findViewById(R.id.iv_myproof_certificate);
        ly_myproof = findViewById(R.id.ly_myproof);


        iv_myproof_certificate.setOnClickListener(this);
        tv_myproof_title = findViewById(R.id.tv_myproof_title);
        tv_myproof_title.setOnClickListener(this);

        iv_aadhar = findViewById(R.id.iv_aadhar);
        iv_education_certificates = findViewById(R.id.iv_education_certificates);
        iv_aadhar.setOnClickListener(this);
        iv_education_certificates.setOnClickListener(this);
        if (!TextUtils.isEmpty(userProfileList.getMatrimonial_profile().getOther_marital_information().getAadhar_url())) {
            Glide.with(this)
                    .applyDefaultRequestOptions(docRequestOptions)
                    .load(userProfileList.getMatrimonial_profile().getOther_marital_information().getAadhar_url())
                    .into(iv_aadhar);
        }
        if (!TextUtils.isEmpty(userProfileList.getMatrimonial_profile().getOther_marital_information().getEducational_url())) {
            Glide.with(this)
                    .applyDefaultRequestOptions(certificateRequestOptions)
                    .load(userProfileList.getMatrimonial_profile().getOther_marital_information().getEducational_url())
                    .into(iv_education_certificates);
        }
        if (userProfileList.getMatrimonial_profile().getOther_marital_information().getSupport_doc() != null) {
            if (!TextUtils.isEmpty(userProfileList.getMatrimonial_profile().getOther_marital_information().getSupport_doc())) {
                ly_myproof.setVisibility(View.VISIBLE);
                tv_myproof_title.setVisibility(View.VISIBLE);
                if (userProfileList.getMatrimonial_profile().getPersonal_details().
                        getMarital_status().equalsIgnoreCase("Divorcee")) {
                    tv_myproof_title.setText("Legal seperation certificate");
                } else {
                    tv_myproof_title.setText("Partner's death certificate");
                }
                Glide.with(this)
                        .applyDefaultRequestOptions(certificateRequestOptions)
                        .load(userProfileList.getMatrimonial_profile().getOther_marital_information().getSupport_doc())
                        .into(iv_myproof_certificate);


            }else {
                if (userProfileList.getMatrimonial_profile().getPersonal_details().getMarital_status().equalsIgnoreCase("Unmarried")) {
                    tv_myproof_title.setVisibility(View.GONE);
                    ly_myproof.setVisibility(View.GONE);
                }
            }

        }

        tv_primary_mobile.setOnClickListener(this);
        tv_secondary_mobile.setOnClickListener(this);
        tv_primary_email.setOnClickListener(this);

    }

    private void setFlags() {

        if (userProfileList.getMatrimonial_profile().isVerified()) {
            findViewById(R.id.ly_profile_varified).setVisibility(View.VISIBLE);
            findViewById(R.id.ly_profile_varified).setOnClickListener(this);
            findViewById(R.id.btn_verify_profile).setVisibility(View.GONE);
        } else {
//            view.findViewById(R.id.ly_profile_varified).setVisibility(View.GONE);
            findViewById(R.id.ly_profile_varified).setVisibility(View.GONE);
            //findViewById(R.id.ly_profile_varified).setOnClickListener(this);
        }

        if (userProfileList.getMatrimonial_profile().isEducationApproved()) {
            findViewById(R.id.ly_education_varified).setVisibility(View.VISIBLE);
            findViewById(R.id.ly_education_varified).setOnClickListener(this);
            btn_verify_edu.setEnabled(false);
            btn_verify_edu.setText("Verified");
            btn_verify_edu.setVisibility(View.GONE);

        } else {
            findViewById(R.id.ly_education_varified).setVisibility(View.GONE);
        }
        if (userProfileList.getMatrimonial_profile().isIdApproved()) {
            findViewById(R.id.ly_id_varified).setVisibility(View.VISIBLE);
            findViewById(R.id.ly_id_varified).setOnClickListener(this);
            btn_verify_ids.setEnabled(false);
            btn_verify_ids.setText("Verified");
            btn_verify_ids.setVisibility(View.GONE);
        } else {
            findViewById(R.id.ly_id_varified).setVisibility(View.GONE);
        }
    }

    private void setApprovelFlag() {
        setFlags();
        //tv_approval_status.setText(userProfileList.getIsApproved());
//        if(!TextUtils.isEmpty(meetIdReceived)) {
//            tv_approval_status.setText(userProfileList.getUserMeetStatus());
//        }else {
//            tv_approval_status.setVisibility(View.GONE);
//        }

        if (!TextUtils.isEmpty(meetIdReceived)) {
            findViewById(R.id.ly_meet_verified).setVisibility(View.VISIBLE);
            ImageView ivMeetStatus = findViewById(R.id.iv_meet_approved);
            TextView tv = findViewById(R.id.tv_meet_verified);
            if (userProfileList.getUserMeetStatus().equalsIgnoreCase("pending")) {
                ivMeetStatus.setImageResource(R.drawable.ic_meet_pending);
                tv.setText("Pending in meet");
            } else if (userProfileList.getUserMeetStatus().equalsIgnoreCase("approved")) {
                ivMeetStatus.setImageResource(R.drawable.ic_meet_approved);
                tv.setText("Approved in meet");
            } else if (userProfileList.getUserMeetStatus().equalsIgnoreCase("rejected")) {
                ivMeetStatus.setImageResource(R.drawable.ic_meet_rejected);
                tv.setText("Rejected in meet");
            }
            findViewById(R.id.ly_meet_verified).setOnClickListener(this);
        } else {
            findViewById(R.id.ly_meet_verified).setVisibility(View.GONE);
            //findViewById(R.id.ly_meet_verified).setOnClickListener(this);
        }

        if (userProfileList.getIsApproved()!=null
                && userProfileList.getIsApproved().equalsIgnoreCase("approved")) {
            if (!TextUtils.isEmpty(meetIdReceived)) {
                if (userProfileList.isMarkAttendance()) {
                    btn_interview_done.setVisibility(View.VISIBLE);
                    btn_mark_attendance.setVisibility(View.VISIBLE);
                    btn_mark_attendance.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.button_gray_color));
                    btn_mark_attendance.setEnabled(false);
                    if (userProfileList.isInterviewDone()) {
                        btn_interview_done.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.button_gray_color));
                        btn_interview_done.setEnabled(false);
                    }
                } else {
                    btn_interview_done.setVisibility(View.VISIBLE);
                    btn_interview_done.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.button_gray_color));
                    btn_interview_done.setEnabled(false);
                    btn_mark_attendance.setVisibility(View.VISIBLE);
                }
            } else {
                btn_interview_done.setVisibility(View.GONE);
                btn_mark_attendance.setVisibility(View.GONE);
            }
        } else {
            btn_interview_done.setVisibility(View.GONE);
            btn_mark_attendance.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(meetIdReceived)) {
            if (userProfileList.getUserMeetStatus().toLowerCase().startsWith("a")) {
                findViewById(R.id.btn_reject).setVisibility(View.VISIBLE);
                findViewById(R.id.btn_approve).setVisibility(View.GONE);
                if (!userProfileList.getMatrimonial_profile().isVerified()) {
                    findViewById(R.id.btn_verify_profile).setVisibility(View.VISIBLE);
                }
            } else if (userProfileList.getUserMeetStatus().toLowerCase().startsWith("r")) {
                findViewById(R.id.btn_reject).setVisibility(View.GONE);
                findViewById(R.id.btn_approve).setVisibility(View.VISIBLE);
                findViewById(R.id.btn_verify_profile).setVisibility(View.GONE);
            } else {
                findViewById(R.id.btn_verify_profile).setVisibility(View.GONE);
            }
        } else {
            findViewById(R.id.btn_approve).setVisibility(View.GONE);
            findViewById(R.id.btn_reject).setVisibility(View.GONE);
            if (!userProfileList.getMatrimonial_profile().isVerified()) {
                findViewById(R.id.btn_verify_profile).setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //Updating the flags of perticler profile from all profile list.
        Intent updateInfo = new Intent("PROFILE_UPDATE");
        updateInfo.putExtra("isBanned", isBlock);
        LocalBroadcastManager.getInstance(this).sendBroadcast(updateInfo);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ly_meet_verified:
                if (findViewById(R.id.tv_meet_verified).getVisibility() == View.VISIBLE)
                    findViewById(R.id.tv_meet_verified).setVisibility(View.GONE);
                else {
                    TextView tv = findViewById(R.id.tv_meet_verified);
                    tv.setVisibility(View.VISIBLE);
//                    if (userProfileList.getUserMeetStatus().equalsIgnoreCase("pending")) {
//                        tv.setText("Pending in meet");
//                    } else if (userProfileList.getUserMeetStatus().equalsIgnoreCase("approved")) {
//                        tv.setText("Approved in meet");
//                    } else if (userProfileList.getUserMeetStatus().equalsIgnoreCase("rejected")) {
//                        tv.setText("Rejected in meet");
//                    }
                }
                break;
            case R.id.ly_premium:
                if (findViewById(R.id.tv_premium).getVisibility() == View.VISIBLE)
                    findViewById(R.id.tv_premium).setVisibility(View.GONE);
                else
                    findViewById(R.id.tv_premium).setVisibility(View.VISIBLE);
                break;
            case R.id.ly_profile_varified:
                if (findViewById(R.id.tv_profile_varified).getVisibility() == View.VISIBLE)
                    findViewById(R.id.tv_profile_varified).setVisibility(View.GONE);
                else
                    findViewById(R.id.tv_profile_varified).setVisibility(View.VISIBLE);
                break;
            case R.id.ly_education_varified:
                if (findViewById(R.id.tv_education_varified).getVisibility() == View.VISIBLE)
                    findViewById(R.id.tv_education_varified).setVisibility(View.GONE);
                else
                    findViewById(R.id.tv_education_varified).setVisibility(View.VISIBLE);
                break;
            case R.id.ly_id_varified:
                if (findViewById(R.id.tv_id_varified).getVisibility() == View.VISIBLE)
                    findViewById(R.id.tv_id_varified).setVisibility(View.GONE);
                else
                    findViewById(R.id.tv_id_varified).setVisibility(View.VISIBLE);

                break;


            case R.id.iv_aadhar:
                enlargePhoto(userProfileList.getMatrimonial_profile().getOther_marital_information().getAadhar_url());
                break;
            case R.id.iv_education_certificates:
                enlargePhoto(userProfileList.getMatrimonial_profile().getOther_marital_information().getEducational_url());
                break;
            case R.id.iv_myproof_certificate:
                enlargePhoto(userProfileList.getMatrimonial_profile().getOther_marital_information().getSupport_doc());
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
            case R.id.iv_arrow_meetlist_details:
                if (findViewById(R.id.ly_meetlist_details).getVisibility() != View.VISIBLE) {
                    findViewById(R.id.ly_meetlist_details).setVisibility(View.VISIBLE);
                    findViewById(R.id.iv_arrow_meetlist_details).setRotation(180);
                } else {
                    findViewById(R.id.ly_meetlist_details).setVisibility(View.GONE);
                    findViewById(R.id.iv_arrow_meetlist_details).setRotation(0);
                }
                break;
            case R.id.btn_mark_attendance:
                JSONObject jsonObject = presenter.createBodyParams(meetIdReceived, userProfileList.get_id(), Constants.MARK_ATTENDANCE);
                presenter.markAttendanceRequest(jsonObject, 1, Constants.MARK_ATTENDANCE);

                break;
            case R.id.btn_interview_done:
                JSONObject jsonObj = presenter.createBodyParams(meetIdReceived, userProfileList.get_id(), Constants.MARK_INTERVIEW);
                presenter.markAttendanceRequest(jsonObj, 1, Constants.MARK_INTERVIEW);
                break;
            case R.id.toolbar_back_action:
                onBackPressed();
                break;
            case R.id.toolbar_edit_action:
                PopupMenu popup = new PopupMenu(this, v);
                popup.setOnMenuItemClickListener(MatrimonyProfileDetailsActivity.this);
                popup.inflate(R.menu.matrimony_profile_menu);
                if (userProfileList.getMatrimonial_profile().isBan()) {
                    popup.getMenu().findItem(R.id.action_block).setTitle("Remove ban");
                } else {
                    popup.getMenu().findItem(R.id.action_block).setTitle("Ban User");
                }
                popup.getMenu().findItem(R.id.action_edit_profile);

                popup.show();
                break;
            case R.id.btn_approve:
                approvalType = Constants.APPROVE;
                showDialog("Alert", "Are you sure, Do you want to approve?",
                        "YES", "NO", 2);//flag 3 for approve
                break;
            case R.id.btn_reject:
                approvalType = Constants.REJECT;
                showReasonDialog("Alert", "Please write the reason for rejection.",
                        "Submit", "Cancle", 2);
                break;
            case R.id.btn_verify_profile:
                //verifyDocument(3,true);
                showDialog("Alert", "Are you sure, Do you want make this profile as verified?",
                        "YES", "NO", 3);//flag 3 for verify
                break;
            case R.id.btn_verify_ids:
                verifyDocument(1, true);
                /*showDialog("Alert", "Are you sure, Do you want to approve?",
                        "YES", "NO", 2);//flag 3 for approve*/
                break;
            case R.id.btn_verify_edu:
                verifyDocument(2, true);
                /*showReasonDialog("Alert", "Please write the reason for rejection.",
                        "Submit", "Cancle", 2);*/
                break;
            case R.id.tv_primary_mobile:
                showDialog("Alert", "Are you sure, Do you want to call user?",
                        "YES", "NO", 4);//flag 4 for call primary number
                break;
            case R.id.tv_secondary_mobile:
                showDialog("Alert", "Are you sure, Do you want to call user?",
                        "YES", "NO", 5);//flag 5 for call primary number
                break;
            case R.id.tv_primary_email:
                showDialog("Alert", "Are you sure, Do you want to E-mail user?",
                        "YES", "NO", 6);//flag 5 for call primary number
                break;


        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_missing_info:
                Intent intent = new Intent(this, MissingInfoActivity.class);
                intent.putExtra("user_id", userProfileList.get_id());
                intent.putExtra("user_email", userProfileList.getMatrimonial_profile()
                        .getResidential_details().getPrimary_email_address());
                startActivity(intent);
                break;
            case R.id.action_block:
                if (userProfileList.getMatrimonial_profile().isBan()) {
                    showDialog("Alert", "Are you sure you want to remove bane ?",
                            "YES", "NO", 1);//flag 1 for unblock
                } else {
                    showReasonDialog("Alert", "Please write the reason for bane.",
                            "Submit", "Cancle", 1);
//                    showDialog("Alert", "Are you sure you want to Block this user?",
//                            "YES", "NO",2);//flag 2 for block
                }

                break;
            case R.id.action_call:

                showDialog("Alert", "Are you sure, Do you want to call user?",
                        "YES", "NO", 4);//flag 4 for call primary number
                break;
            case R.id.action_register_user:

                showDialog("Alert", "Are you sure, Do you want to register user in meet?",
                        "YES", "NO", 7);//flag 7 for registering user to meet list.
                break;
            case R.id.action_edit_profile:
                if (Util.isConnected(this)) {
                Intent editIntent = new Intent(this, RegistrationActivity.class);
                if (userProfileList.getMatrimonial_profile() != null) {
                    editIntent.putExtra("matrimonialProfile", userProfileList.getMatrimonial_profile());
                    editIntent.putExtra(PROFILE_ID, userProfileList.get_id());
                }
                editIntent.putExtra("Flag", 1);//for matrimonial
                startActivity(editIntent);
                } else {
                    Util.showToast(getString(R.string.msg_no_network), this);
                }

                break;

        }
        return false;
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
    }

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
        if (Constants.MARK_ATTENDANCE.equalsIgnoreCase(requestType)) {
            //Util.showToast(requestType+" Success",this);
            btn_interview_done.setEnabled(true);
            btn_interview_done.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary));
            btn_mark_attendance.setEnabled(false);
            btn_mark_attendance.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.button_gray_color));
            Util.showSuccessFailureToast(response, this, getWindow().getDecorView()
                    .findViewById(android.R.id.content));
        } else if (Constants.MARK_INTERVIEW.equalsIgnoreCase(requestType)) {
            //Util.showToast(requestType+" Success",this);
            Util.showSuccessFailureToast(response, this, getWindow().getDecorView()
                    .findViewById(android.R.id.content));
            btn_interview_done.setEnabled(false);
            btn_interview_done.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.button_gray_color));
            btn_mark_attendance.setEnabled(false);
            btn_mark_attendance.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.button_gray_color));
        }
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        Util.showToast(message, this);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {

    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void closeCurrentActivity() {

    }

    public void updateBlockUnblock(String message) {
        userProfileList.getMatrimonial_profile().setBan(isBlock);
        Util.showToast(message, this);
    }

    public void updateRequestStatus(String response) {
        Util.showSuccessFailureToast(response, this, this.getWindow().getDecorView()
                .findViewById(android.R.id.content));
        if (Constants.REJECT.equalsIgnoreCase(approvalType)) {
            userProfileList.setIsApproved(Constants.REJECT);
            userProfileList.setUserMeetStatus(Constants.REJECT);
            setApprovelFlag();
        }
        if (Constants.APPROVE.equalsIgnoreCase(approvalType)) {
            userProfileList.setIsApproved(Constants.APPROVE);
            userProfileList.setUserMeetStatus(Constants.APPROVE);
            userProfileList.getMatrimonial_profile().setVerified(true);
            setApprovelFlag();
            setFlags();
        }
    }

    public void updateVerificationStatus(int type, String message) {
        if (type == 3) {
            /*btn_verify_ids.setEnabled(false);
            btn_verify_ids.setText("Verified");*/
            userProfileList.getMatrimonial_profile().setVerified(true);
            setFlags();
            Util.showToast(message, this);
        } else if (type == 1) {
            btn_verify_ids.setEnabled(false);
            btn_verify_ids.setText("Verified");
            userProfileList.getMatrimonial_profile().setIdApproved(true);
            setFlags();
            Util.showToast(message, this);

        } else {
            btn_verify_edu.setEnabled(false);
            btn_verify_edu.setText("Verified");
            userProfileList.getMatrimonial_profile().setEducationApproved(true);
            setFlags();
            Util.showToast(message, this);
        }

    }

    public void showReasonDialog(String title, String hint, String txtPositive, String txtNigetive, int flag) {
        Dialog dialog;
        Button btPositive, btNigetive;
        EditText edt_reason;
        TextView tvTitle;

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_reason_layout);

        tvTitle = dialog.findViewById(R.id.txt_dialog_title);
        edt_reason = dialog.findViewById(R.id.edt_reason);
        btPositive = dialog.findViewById(R.id.btn_submit);
        btPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strReason = edt_reason.getText().toString();

                if (TextUtils.isEmpty(strReason)) {
                    Util.snackBarToShowMsg(MatrimonyProfileDetailsActivity.this.getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "Reason Can not be blank",
                            Snackbar.LENGTH_LONG);
                } else {
                    if (flag == 1) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("block_type", true);
                        map.put("user_id", userProfileList.get_id());
                        map.put("ban_reason", strReason);
                        String paramjson = new Gson().toJson(map);
                        presenter.userAction(paramjson);
                        isBlock = true;
                    } else if (flag == 2) {
                        Map<String, String> request = new HashMap<>();
                        if (TextUtils.isEmpty(meetIdReceived)) {
                            request.put("type", " ");
                            request.put("approval", Constants.APPROVE);
                            request.put("user_id", userProfileList.get_id());
                            request.put("rejection_reason", strReason);
                        } else {
                            request.put("meet_id", meetIdReceived);
                            request.put("type", "user");
                            request.put("approval", Constants.REJECT);
                            request.put("user_id", userProfileList.get_id());
                            request.put("rejection_reason", strReason);
                        }
                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(request);
                        presenter.approveRejectRequest(json);
                    }
                    dialog.dismiss();
                }
            }
        });
        btNigetive = dialog.findViewById(R.id.btn_cancel);

        tvTitle.setText(title);
        edt_reason.setHint(hint);
        btPositive.setText(txtPositive);
        btNigetive.setText(txtNigetive);

        btNigetive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void showDialog(String dialogTitle, String message, String btn1String, String
            btn2String, int flag) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(this));
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
                if (flag == 1) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("block_type", false);
                    map.put("user_id", userProfileList.get_id());
                    String paramjson = new Gson().toJson(map);
                    presenter.userAction(paramjson);
                    isBlock = false;
                } else if (flag == 2) {
                    Map<String, String> request = new HashMap<>();
                    if (TextUtils.isEmpty(meetIdReceived)) {
                        request.put("type", " ");
                        request.put("approval", Constants.APPROVE);
                        request.put("user_id", userProfileList.get_id());
                    } else {
                        request.put("meet_id", meetIdReceived);
                        request.put("type", "user");
                        request.put("approval", Constants.APPROVE);
                        request.put("user_id", userProfileList.get_id());
                        request.put("rejection_reason", " ");
                    }
                    Gson gson = new GsonBuilder().create();
                    String json = gson.toJson(request);
                    presenter.approveRejectRequest(json);
                } else if (flag == 4) {
                    if (!TextUtils.isEmpty(tv_primary_mobile.getText()))
                        try {
                            Intent dial = new Intent();
                            dial.setAction("android.intent.action.DIAL");
                            dial.setData(Uri.parse("tel:" + tv_primary_mobile.getText()));
                            startActivity(dial);
                        } catch (Exception e) {
                            Log.e("Calling Phone", "" + e.getMessage());
                        }
                } else if (flag == 5) {
                    if (!TextUtils.isEmpty(tv_secondary_mobile.getText()))
                        try {
                            Intent dial = new Intent();
                            dial.setAction("android.intent.action.DIAL");
                            dial.setData(Uri.parse("tel:" + tv_secondary_mobile.getText()));
                            startActivity(dial);
                        } catch (Exception e) {
                            Log.e("Calling Phone", "" + e.getMessage());
                        }
                } else if (flag == 6) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:" + tv_primary_email.getText())); // only email apps should handle this
                    intent.putExtra(Intent.EXTRA_EMAIL, tv_primary_email.getText());
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                } else if (flag == 3) {
                    verifyDocument(3, true);
                } else if (flag == 7) {
                    presenter.getMatrimonyMeets();
                }

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

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void verifyDocument(int type, boolean approveReject) {
        // type 1 ID  type 2 education type 3 user verification
        //"identification_type":"ID/Education",
        //      "action_type":false/true
        String paramjson = new Gson().toJson(getVerifyDocReqJson(type, approveReject));
        presenter.approveRejectDocumentsRequest(paramjson, type);
    }

    public JsonObject getVerifyDocReqJson(int type, boolean approveReject) {
        JsonObject requestObject = new JsonObject();
        if (type == 3) {
            requestObject.addProperty("identification_type", "verification");
            requestObject.addProperty("user_id", userProfileList.get_id());
            requestObject.addProperty("action_type", approveReject);
        } else if (type == 1) {
            requestObject.addProperty("identification_type", "ID");
            requestObject.addProperty("user_id", userProfileList.get_id());
            requestObject.addProperty("action_type", approveReject);
        } else {
            requestObject.addProperty("identification_type", "Education");
            requestObject.addProperty("user_id", userProfileList.get_id());
            requestObject.addProperty("action_type", approveReject);
        }

        return requestObject;
    }

    @Override
    public void onBackPressed() {
//        Intent returnIntent = new Intent();
//        returnIntent.putExtra(Constants.Planner.MEMBER_LIST_DATA, userProfileList);
//        returnIntent.putExtra(Constants.Planner.MEMBER_LIST_COUNT, receivedPos);
//        setResult(Constants.MatrimonyModule.FLAG_UPDATE_RESULT, returnIntent);
        finish();
    }

    private void showMultiSelectBottomsheet(String Title, String selectedOption, List<MatrimonyMeet> meetList) {

        bottomSheetDialogFragment = new MeetListBottomSheet(this, selectedOption, meetList, this::onValuesSelected);
        bottomSheetDialogFragment.show();
        bottomSheetDialogFragment.toolbarTitle.setText(Title);
        bottomSheetDialogFragment.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onValuesSelected(int selectedPosition, String spinnerName, String selectedValues) {
        Log.e("onMeetSelected","selected meet number is-"+selectedPosition);
        presenter.RegisterUserInMeet(profileResponse.getData().getMatrimonialProfile().getResidentialDetails().getPrimaryPhone(),profileResponse.getData().getId(),meetListReceived.get(selectedPosition).getId());
    }

    public void setMatrimonyMeets(List<MatrimonyMeet> data, String earliestMeetId) {
        meetListReceived = data;
        showMultiSelectBottomsheet("Select Meet","usermeetregistration",data);
    }

    public void showResponseRegisterUser(String message, int status) {
        Util.snackBarToShowMsg(getWindow().getDecorView()
                        .findViewById(android.R.id.content), message,
                Snackbar.LENGTH_LONG);
    }
}
