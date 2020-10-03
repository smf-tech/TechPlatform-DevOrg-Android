package com.octopusbjsindia.view.fragments;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.matrimonyregistration.APIListener;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.profile.Jurisdiction;
import com.octopusbjsindia.models.profile.OrganizationRole;
import com.octopusbjsindia.models.support.TicketAssingnRequest;
import com.octopusbjsindia.models.support.TicketData;
import com.octopusbjsindia.presenter.TicketDetailFragmentPresenter;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;
import java.util.List;

public class TicketDetailFragment extends Fragment implements APIListener, View.OnClickListener, CustomSpinnerListener {

    private TicketDetailFragmentPresenter presenter;
    private ArrayList<CustomSpinnerObject> rolesList = new ArrayList<CustomSpinnerObject>();
    private ArrayList<CustomSpinnerObject> statusList = new ArrayList<CustomSpinnerObject>();
    private TextView tvAssignedTo,tvChangeStatus;
    private TicketData data;
    private RelativeLayout lyProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CustomSpinnerObject status1= new CustomSpinnerObject();
        status1.set_id("1");
        status1.setName("Open");
        statusList.add(status1);
        CustomSpinnerObject status2= new CustomSpinnerObject();
        status2.set_id("2");
        status2.setName("Resolved");
        statusList.add(status2);
        CustomSpinnerObject status3= new CustomSpinnerObject();
        status3.set_id("3");
        status3.setName("Assigned");
        statusList.add(status3);
        CustomSpinnerObject status4= new CustomSpinnerObject();
        status4.set_id("4");
        status4.setName("Close");
        statusList.add(status4);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ticket_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lyProgressBar = view.findViewById(R.id.lyProgressBar);
        data = new TicketData();
        data = (TicketData) getArguments().getSerializable("data");

        presenter = new TicketDetailFragmentPresenter(this);
        presenter.getRoles(Util.getUserObjectFromPref().getOrgId(),Util.getUserObjectFromPref().getProjectIds().get(0).getId());

        ImageView ivUserPic = view.findViewById(R.id.ivUserPic);
        ImageView ivAttached = view.findViewById(R.id.ivAttached);

//        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_user_avatar);
//        requestOptions = requestOptions.apply(RequestOptions.circleCropTransform());
//        Glide.with(this)
//                .applyDefaultRequestOptions(requestOptions)
//                .load()
//                .into(ivUserPic);

        ((TextView) view.findViewById(R.id.tvUserName)).setText(data.getUserId());
        ((TextView) view.findViewById(R.id.tvTitle)).setText(data.getTicketTitle());
        ((TextView) view.findViewById(R.id.tvTime)).setText(data.getCreatedDatetime());
        ((TextView) view.findViewById(R.id.tvStatus)).setText(data.getStatus());
        ((TextView) view.findViewById(R.id.tvType)).setText(data.getTicketType());
        ((TextView) view.findViewById(R.id.tvDescription)).setText(data.getTicketDesc());
        tvAssignedTo = view.findViewById(R.id.tvAssignedTo);
        tvChangeStatus = view.findViewById(R.id.tvChangeStatus);
        if(TextUtils.isEmpty(data.getTicketAttachment())){
            ivAttached.setVisibility(View.INVISIBLE);
        } else {
            ivAttached.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(data.getTicketAttachment())
                    .into(ivAttached);
        }

        view.findViewById(R.id.lyAssignedTo).setOnClickListener(this);
        view.findViewById(R.id.lyChangeStatus).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lyAssignedTo:
                if (rolesList.size() > 0) {
                    CustomSpinnerDialogClass cddProject = new CustomSpinnerDialogClass(getActivity(),
                            TicketDetailFragment.this, "Select Role", rolesList, false);
                    cddProject.show();
                    cddProject.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                }
                break;
            case R.id.lyChangeStatus:
                if (rolesList.size() > 0) {
                    CustomSpinnerDialogClass cddProject = new CustomSpinnerDialogClass(getActivity(),
                            TicketDetailFragment.this, "Select Status", statusList, false);
                    cddProject.show();
                    cddProject.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                }
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void showMessage(String requestID, String message, int code) {
        Util.showToast(getActivity(),message);
    }

    @Override
    public void showProgressBar() {
        lyProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        lyProgressBar.setVisibility(View.GONE);
    }

    public void setRoles(List<OrganizationRole> data) {
        rolesList.clear();
        for (int i = 0; i < data.size(); i++) {
            CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
            customSpinnerObject.set_id(data.get(i).getId());
            customSpinnerObject.setName(data.get(i).getDisplayName());
            rolesList.add(customSpinnerObject);
        }
    }


    @Override
    public void onCustomSpinnerSelection(String type) {
        if (type.equals("Select Role")) {
            String selectedRoleName = "", selectedRoleId = "";
            for (CustomSpinnerObject customSpinnerObject : rolesList) {
                if (customSpinnerObject.isSelected()) {
                    selectedRoleName = customSpinnerObject.getName();
                    selectedRoleId = customSpinnerObject.get_id();
                    break;
                }
            }
            TicketAssingnRequest request = new TicketAssingnRequest();
            request.setTicketId(data.getId());
            request.setTicketType(data.getTicketType());
            request.setTicketTitle(data.getTicketTitle());
            request.setTicketDesc(data.getTicketDesc());
            request.setTicketAttachment(data.getTicketAttachment());
            request.setComment(data.getId());
            request.setRoleName(selectedRoleName);
            request.setRoleId(selectedRoleId);

            presenter.assinged(request);

        } else if (type.equals("Select Status")) {
            String selectedStatusName = "";
            for (CustomSpinnerObject customSpinnerObject : rolesList) {
                if (customSpinnerObject.isSelected()) {
                    selectedStatusName = customSpinnerObject.getName();
                    break;
                }
            }
            tvChangeStatus.setText(selectedStatusName);
        }
    }

    public void setAssinegdSuccess(String roleName) {
        tvAssignedTo.setText(roleName);
    }
}
