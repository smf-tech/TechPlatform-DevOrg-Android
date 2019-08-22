package com.platform.view.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.platform.Platform;
import com.platform.R;
import com.platform.models.tm.TMApprovalRequestModel;
import com.platform.models.tm.TMUserProfileApprovalRequest;
import com.platform.models.user.UserInfo;
import com.platform.presenter.TMUserProfileApprovalFragmentPresenter;
import com.platform.utility.AppEvents;
import com.platform.utility.Constants;
import com.platform.utility.PreferenceHelper;
import com.platform.utility.Util;
import com.platform.view.activities.TMUserProfileListActivity;
import com.platform.view.customs.TextViewBold;
import com.platform.view.customs.TextViewRegular;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TMUserProfileApprovalFragment extends Fragment {
    private RequestOptions requestOptions;
    private View approvalsFragmentView;
    private Toolbar toolbar;
    private TextView tv_name_title, tv_role_title, tv_mobile_title, tv_email_title,tv_email_title_text,tv_leave_reason;
    private Button btn_approve, btn_reject;
    private ImageView img_user_profle;
    private LinearLayout linear_dynamic_textview;
    TMUserProfileApprovalFragmentPresenter tmUserProfileApprovalFragmentPresenter;
    //strings
    String strTitle, filterTypeRequest;
    JSONObject requetsObject;
    List<TMUserProfileApprovalRequest> userProfileApprovalRequestList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*if (getActivity() != null && getArguments() != null) {
            Context context = getActivity();
            String title = (String) getArguments().getSerializable("TITLE");
            if (TextUtils.isEmpty(title))
                title = getString(R.string.approvals);

                    ((HomeActivity) context).setActionBarTitle(title);
            ((HomeActivity) context).setSyncButtonVisibility(false);

            if ((boolean) getArguments().getSerializable("SHOW_BACK")) {
                ((HomeActivity) getActivity()).showBackArrow();
            }
        }*/
        Bundle bundle = this.getArguments();
        if (!(bundle == null)) {
            strTitle = bundle.getString("filter_type", "");
            filterTypeRequest = bundle.getString("filter_type_request", "");
            try {
                requetsObject = new JSONObject(filterTypeRequest);
                UserInfo userInfo = Util.getUserObjectFromPref();
                //requetsObject.put("user_id",userInfo.getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Util.logger(getActivity().getLocalClassName(), "000000--" + requetsObject.toString());
            Util.logger(getActivity().getLocalClassName(), "!!!!!!!--" + filterTypeRequest);
        }
        requestOptions = new RequestOptions().placeholder(R.mipmap.app_logo);
        requestOptions = requestOptions.apply(RequestOptions.circleCropTransform());
        AppEvents.trackAppEvent(getString(R.string.event_approvals_screen_visit));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        approvalsFragmentView = inflater.inflate(R.layout.fragment_user_profile_approval, container, false);
        toolbar = approvalsFragmentView.findViewById(R.id.toolbar);
        img_user_profle = approvalsFragmentView.findViewById(R.id.img_user_profle);
        tv_name_title = approvalsFragmentView.findViewById(R.id.tv_name_value);
        tv_role_title = approvalsFragmentView.findViewById(R.id.tv_role_value);
        tv_mobile_title = approvalsFragmentView.findViewById(R.id.tv_mobile_value);
        tv_email_title_text = approvalsFragmentView.findViewById(R.id.tv_email_title);
        tv_email_title = approvalsFragmentView.findViewById(R.id.tv_email_value);
        linear_dynamic_textview = approvalsFragmentView.findViewById(R.id.linear_dynamic_textview);
        btn_approve = approvalsFragmentView.findViewById(R.id.btn_approve);
        btn_reject = approvalsFragmentView.findViewById(R.id.btn_reject);
        tv_leave_reason = approvalsFragmentView.findViewById(R.id.tv_leave_reason);
        PreferenceHelper preferenceHelper = new PreferenceHelper(Platform.getInstance());
        String ispending = preferenceHelper.getString(PreferenceHelper.IS_PENDING);
        if (ispending.equalsIgnoreCase(PreferenceHelper.IS_PENDING)){
            btn_reject.setVisibility(View.VISIBLE);
            btn_approve.setVisibility(View.VISIBLE);
            tv_leave_reason.setVisibility(View.GONE);
        }else {
            btn_reject.setVisibility(View.GONE);
            btn_approve.setVisibility(View.GONE);
            tv_leave_reason.setVisibility(View.VISIBLE);
        }


        if (toolbar != null) {
            TextView title = toolbar.findViewById(R.id.toolbar_title);
            title.setText(strTitle);
        }
        ImageView img_back = toolbar.findViewById(R.id.toolbar_back_action);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();}
                ((TMUserProfileListActivity)getActivity()).finishwithResult();
            }
        });

        btn_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TMApprovalRequestModel tmApprovalRequestModel = new TMApprovalRequestModel();
                try {
                    tmApprovalRequestModel.setType("userapproval");
                    tmApprovalRequestModel.setApprove_type("approved");
                    tmApprovalRequestModel.setReason("");
                    tmApprovalRequestModel.setLeave_type("");
                    tmApprovalRequestModel.setStartdate("");
                    tmApprovalRequestModel.setEnddate("");
                    tmApprovalRequestModel.setId(userProfileApprovalRequestList.get(0).get_id());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                String jsonInString = gson.toJson(tmApprovalRequestModel);
                tmUserProfileApprovalFragmentPresenter.approveRejectRequest(jsonInString, 0);
            }
        });
        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callReasonDialog();
               /* TMApprovalRequestModel tmApprovalRequestModel = new TMApprovalRequestModel();
                try {
                    tmApprovalRequestModel.setType("userapproval");
                    tmApprovalRequestModel.setApprove_type("rejected");
                    tmApprovalRequestModel.setReason("");
                    tmApprovalRequestModel.setLeave_type("");
                    tmApprovalRequestModel.setStartdate("");
                    tmApprovalRequestModel.setEnddate("");
                    tmApprovalRequestModel.setId(userProfileApprovalRequestList.get(0).get_id());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                String jsonInString = gson.toJson(tmApprovalRequestModel);
                tmUserProfileApprovalFragmentPresenter.approveRejectRequest(jsonInString, 0);*/

            }
        });
        //to  call on phone number //create in util
        tv_mobile_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(tv_mobile_title.getText()))
                try {
                    Intent dial = new Intent();
                    dial.setAction("android.intent.action.DIAL");
                    dial.setData(Uri.parse("tel:" + tv_mobile_title.getText()));
                    startActivity(dial);
                } catch (Exception e) {
                    Log.e("Calling Phone", "" + e.getMessage());
                }
            }
        });
        return approvalsFragmentView;
    }

    private void callReasonDialog() {
        String strReason = Util.showReasonDialog(getActivity(),0,this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tmUserProfileApprovalFragmentPresenter = new TMUserProfileApprovalFragmentPresenter(this);
        tmUserProfileApprovalFragmentPresenter.getAllPendingRequests(requetsObject);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void showFetchedUserProfileForApproval(List<TMUserProfileApprovalRequest> data) {
        userProfileApprovalRequestList = data;
        //handle respo
        Util.logger("name", data.get(0).getName());
        if (data.get(0).getStatus()!=null) {
            if (data.get(0).getStatus().getRejection_reason()!=null) {
                tv_leave_reason.setText("Rejected Reason:- "+data.get(0).getStatus().getRejection_reason());
            }else {tv_leave_reason.setVisibility(View.GONE);}
        }else {
            tv_leave_reason.setVisibility(View.GONE);
        }
        tv_name_title.setText(data.get(0).getName());
        tv_role_title.setText(data.get(0).getRole_id().getName());
        if (data.get(0).getEmail()!=null && !TextUtils.isEmpty(data.get(0).getEmail())) {
            tv_email_title.setText(data.get(0).getEmail());
        }else {
            tv_email_title_text.setVisibility(View.GONE);
            tv_email_title.setVisibility(View.GONE);
        }
        tv_mobile_title.setText(data.get(0).getPhone());
        Util.logger("email", data.get(0).getEmail());

        Util.logger("uname", data.get(0).getUsername());
        Util.logger("mobile", data.get(0).getPhone());

        Util.logger("mobile", "Size -"+data.get(0).getLocation().size());
        //add project to layout if available

        //tvProject.setText("Org"+": "+data.get(0).getOrg_id().getName());
        if (data.get(0).getOrg_id()!=null) {
            if (data.get(0).getOrg_id().getName() != null) {
                addDynamicTextsTitels("Organisation");

                addDynamicTextsValues(data.get(0).getOrg_id().getName());
            }
        }
     /*   if (data.get(0).getProject_id()!=null) {
            if (data.get(0).getProject_id().getName() != null) {
                addDynamicTextsTitels("Project");

                addDynamicTextsValues(data.get(0).getOrg_id().getName());
            }
        }*/
        /*if (data.get(0).getOrg_id().getName()!=null) {
            addDynamicTextsTitels(data.get(0).getOrg_id().getName());

            addDynamicTextsValues(data.get(0).getOrg_id().getName());
        }*/



        for (int i = 0; i <data.get(0).getLocation().size() ; i++)
        {
            addDynamicTextsTitels(data.get(0).getLocation().get(i).getDisplay_name());

            addDynamicTextsValues(data.get(0).getLocation().get(i).getValue().get(0));
        }

        if (!TextUtils.isEmpty(data.get(0).getProfile_pic())) {
            Glide.with(getActivity())
                    .applyDefaultRequestOptions(requestOptions)
                    .load(data.get(0).getProfile_pic())
                    .into(img_user_profle);
        }
    }

    public void updateRequestStatus(String response, int position) {

        Util.showSuccessFailureToast(response,getActivity(),getActivity().getWindow().getDecorView()
                .findViewById(android.R.id.content));
        getActivity().finish();
    }

    public void onReceiveReason(String s, int pos) {
        rejectApprovalRequest(s,pos);
    }

    public void rejectApprovalRequest(String strReason,int pos)
    {
        if (!TextUtils.isEmpty(strReason)){
            TMApprovalRequestModel tmApprovalRequestModel = new TMApprovalRequestModel();
            try {
                tmApprovalRequestModel.setType("userapproval");
                tmApprovalRequestModel.setApprove_type("rejected");
                tmApprovalRequestModel.setReason(strReason);
                tmApprovalRequestModel.setLeave_type("");
                tmApprovalRequestModel.setStartdate("");
                tmApprovalRequestModel.setEnddate("");
                tmApprovalRequestModel.setId(userProfileApprovalRequestList.get(0).get_id());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();
            String jsonInString = gson.toJson(tmApprovalRequestModel);
            tmUserProfileApprovalFragmentPresenter.approveRejectRequest(jsonInString, 0);
        }else {

            Util.showSuccessFailureToast("Please enter reason to reject.",getActivity(),getActivity().getWindow().getDecorView()
                    .findViewById(android.R.id.content));
        }
    }
    // adding dynamic textviews below
    public void addDynamicTextsValues(String value){
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lparams.setMargins(5,5,5,5);
        TextView tv = new TextViewRegular(getActivity());
        tv.setLayoutParams(lparams);
        tv.setText(value+"");
        tv.setTextSize(14);

        linear_dynamic_textview.addView(tv);
    }
    public void addDynamicTextsTitels(String strTitle){
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lparams.setMargins(5,50,5,5);
        TextView tvtitle = new TextViewBold(getActivity());
        tvtitle.setLayoutParams(lparams);
        tvtitle.setText(strTitle+" : ");
        tvtitle.setTextSize(14);
        //tvtitle.setTypeface(null, Typeface.BOLD);
        linear_dynamic_textview.addView(tvtitle);
    }


//trial method-

    /*View wizardView = getLayoutInflater()
            .inflate(R.layout.row_tm_formspage_item, linear_dynamic_textview, false);
    TextView textView = wizardView.findViewById(R.id.tv_title);
        textView.setText("test Text");
    TextView textView2 = wizardView.findViewById(R.id.tv_value);
        textView2.setText("test Text2");
        linear_dynamic_textview.addView(wizardView);
        linear_dynamic_textview.addView(wizardView); */

}
