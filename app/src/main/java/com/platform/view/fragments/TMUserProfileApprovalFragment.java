package com.platform.view.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.platform.Platform;
import com.platform.R;
import com.platform.models.tm.TMApprovalRequestModel;
import com.platform.models.tm.TMUserProfileApprovalRequest;
import com.platform.models.user.UserInfo;
import com.platform.presenter.TMUserProfileApprovalFragmentPresenter;
import com.platform.utility.AppEvents;
import com.platform.utility.PreferenceHelper;
import com.platform.utility.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TMUserProfileApprovalFragment extends Fragment {

    private View approvalsFragmentView;
    private Toolbar toolbar;
    private TextView tv_name_title, tv_role_title, tv_mobile_title, tv_email_title;
    private Button btn_approve, btn_reject;
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
            Util.logger(getActivity().getLocalClassName().toString(), "000000--" + requetsObject.toString());
            Util.logger(getActivity().getLocalClassName().toString(), "!!!!!!!--" + filterTypeRequest);
        }

        AppEvents.trackAppEvent(getString(R.string.event_approvals_screen_visit));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        approvalsFragmentView = inflater.inflate(R.layout.fragment_user_profile_approval, container, false);
        toolbar = approvalsFragmentView.findViewById(R.id.toolbar);

        tv_name_title = approvalsFragmentView.findViewById(R.id.tv_name_value);
        tv_role_title = approvalsFragmentView.findViewById(R.id.tv_role_value);
        tv_mobile_title = approvalsFragmentView.findViewById(R.id.tv_mobile_value);
        tv_email_title = approvalsFragmentView.findViewById(R.id.tv_email_value);
        linear_dynamic_textview = approvalsFragmentView.findViewById(R.id.linear_dynamic_textview);
        btn_approve = approvalsFragmentView.findViewById(R.id.btn_approve);
        btn_reject = approvalsFragmentView.findViewById(R.id.btn_reject);
        PreferenceHelper preferenceHelper = new PreferenceHelper(Platform.getInstance());
        String ispending = preferenceHelper.getString(PreferenceHelper.IS_PENDING);
        if (ispending.equalsIgnoreCase(PreferenceHelper.IS_PENDING)){
            btn_reject.setVisibility(View.VISIBLE);
            btn_approve.setVisibility(View.VISIBLE);
        }else {
            btn_reject.setVisibility(View.GONE);
            btn_approve.setVisibility(View.GONE);
        }


        if (toolbar != null) {
            TextView title = toolbar.findViewById(R.id.toolbar_title);
            title.setText(strTitle);
        }

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
        tv_name_title.setText(data.get(0).getName());
        tv_role_title.setText(data.get(0).getRole_id().getName());
        tv_email_title.setText(data.get(0).getEmail());
        tv_mobile_title.setText(data.get(0).getPhone());
        Util.logger("email", data.get(0).getEmail());

        Util.logger("uname", data.get(0).getUsername());
        Util.logger("mobile", data.get(0).getPhone());

        Util.logger("mobile", "Size -"+data.get(0).getLocation().size());
        //add project to layout if available
        ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tvProject = new TextView(getActivity());
        tvProject.setLayoutParams(lparams);
        tvProject.setText("Org"+": "+data.get(0).getOrg_id().getName());
        tvProject.setTextSize(16);
        tvProject.setTypeface(null, Typeface.BOLD);
        linear_dynamic_textview.addView(tvProject);

        for (int i = 0; i <data.get(0).getLocation().size() ; i++) {
            /*ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);*/
            TextView tv = new TextView(getActivity());
            tv.setLayoutParams(lparams);
            tv.setText(data.get(0).getLocation().get(i).getDisplay_name()+": ");
            tv.setTextSize(16);
            tv.setTypeface(null, Typeface.BOLD);
            TextView tv2=new TextView(getActivity());
            tv2.setLayoutParams(lparams);
            tv2.setText(data.get(0).getLocation().get(i).getValue().get(0));
            tv2.setTextSize(16);
            Util.logger("mobile", "Size -"+i+"-"+data.get(0).getLocation().get(i).getValue().get(0));
            Util.logger("mobile", "Size -"+i+"-"+data.get(0).getLocation().size());
            linear_dynamic_textview.addView(tv);
            linear_dynamic_textview.addView(tv2);
        }


    }

    public void updateRequestStatus(String response, int position) {
        Util.showToast("Thank You", getActivity());
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
            Util.showToast("Please enter reason to reject.",getActivity());
        }
    }
    // adding dynamic textviews below

}
