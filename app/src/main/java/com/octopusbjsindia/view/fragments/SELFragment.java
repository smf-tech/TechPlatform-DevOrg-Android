package com.octopusbjsindia.view.fragments;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.sel_content.SELAssignmentData;
import com.octopusbjsindia.models.sel_content.SELVideoContent;
import com.octopusbjsindia.models.sel_content.VideoContentAPIResponse;
import com.octopusbjsindia.presenter.SELFragmentPresenter;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.SELFragmentAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SELFragment extends Fragment implements APIDataListener, View.OnClickListener {
    private SELFragmentPresenter presenter;
    private final List<SELVideoContent> selContentList = new ArrayList<>();
    private SELFragmentAdapter selFragmentAdapter;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private List<Boolean> isModuleCompleted = new ArrayList<>();
    private boolean isAllModulesCompleted = false;
    private Button btnGetCertificate;
    private ImageView ivNoData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tutorials, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBarLayout = view.findViewById(R.id.profile_act_progress_bar);
        progressBar = view.findViewById(R.id.pb_profile_act);
        presenter = new SELFragmentPresenter(this);
        RecyclerView rvSelContent = view.findViewById(R.id.rv_sel_content);
        ivNoData = view.findViewById(R.id.iv_no_data);
        btnGetCertificate = view.findViewById(R.id.btn_certificate);
        btnGetCertificate.setOnClickListener(this);
        selFragmentAdapter = new SELFragmentAdapter(this, selContentList, isModuleCompleted);
        rvSelContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvSelContent.setAdapter(selFragmentAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.isConnected(getActivity())) {
            presenter.getSelContentData();
            ivNoData.setVisibility(View.GONE);
        } else {
            if (selContentList.size() == 0) {
                Util.showToast(getString(R.string.msg_no_network), getContext());
                ivNoData.setVisibility(View.VISIBLE);
            } else {
                ivNoData.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        if (getActivity() != null) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.msg_failure),
                    Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (getActivity() != null) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.msg_failure),
                    Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void onSuccessListener(String requestID, String response) {

    }

    public void populateData(VideoContentAPIResponse videoContentAPIResponse) {
        List<SELVideoContent> selVideoContentList = videoContentAPIResponse.getData();
        if (selVideoContentList.size() > 0) {
            selContentList.clear();
            selContentList.addAll(selVideoContentList);
            updateModuleAccessList();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateModuleAccessList() {
        isModuleCompleted.clear();
        //isModuleCompleted.add(true);
        for (int i=0; i<selContentList.size(); i++) {
            boolean isAccessible = false;
            if(selContentList.get(i).getAssignmentList()!= null && selContentList.get(i).
                    getAssignmentList().size()>0) {
                for (SELAssignmentData selAssignmentData: selContentList.get(i).getAssignmentList()) {
                    if(selAssignmentData.isFormSubmitted()) {
                        isAccessible = true;
                    } else {
                        isAccessible = false;
                        break;
                    }
                }
                isModuleCompleted.add(isAccessible);
            } else {
                isModuleCompleted.add(true);
            }
        }

        selFragmentAdapter.notifyDataSetChanged();

        for (Boolean isCompleted: isModuleCompleted) {
            if(isCompleted) {
                isAllModulesCompleted = true;
            } else {
                isAllModulesCompleted =  false;
                break;
            }
        }
        if (isAllModulesCompleted) {
            btnGetCertificate.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void closeCurrentActivity() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        // get certificate on mail id
        Util.showEnterEmailDialog(getActivity(), 2, this);
    }

    public void onReceiveEmailId(String strEmailId) {
        if (TextUtils.isEmpty(strEmailId)){
            Util.showToast("Please enter valid email", getActivity());
        } else {
            if(Util.getUserObjectFromPref().getUserLocation().getDistrictIds()!= null) {
                if(Util.getUserObjectFromPref().getUserLocation().getTalukaIds()!= null) {
                    if(Util.getUserObjectFromPref().getUserLocation().getSchoolIds()!= null) {
                        presenter.sendSELCertificateOnMail(strEmailId,
                                Util.getUserObjectFromPref().getUserName(),
                                Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getName(),
                                Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getName(),
                                Util.getUserObjectFromPref().getUserLocation().getSchoolIds().get(0).getName());
                    }
                }
            }
        }
    }

    public void showResponse(String message) {
        if(message!=null) {
            Util.showToast(message, getActivity());
        }
    }
}