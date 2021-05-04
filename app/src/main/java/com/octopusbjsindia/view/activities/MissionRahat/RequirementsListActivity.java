package com.octopusbjsindia.view.activities.MissionRahat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.MissionRahat.RequirementsListData;
import com.octopusbjsindia.models.MissionRahat.RequirementsListResponse;
import com.octopusbjsindia.models.SujalamSuphalam.Structure;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.models.events.CommonResponseStatusString;
import com.octopusbjsindia.presenter.MissionRahat.RequirementsListActivityPresenter;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.MatrimonyProfileListActivity;
import com.octopusbjsindia.view.adapters.mission_rahat.RequirementsListAdapter;
import com.octopusbjsindia.view.adapters.mission_rahat.SearchListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RequirementsListActivity extends AppCompatActivity implements APIDataListener {

    RequirementsListActivityPresenter presenter;
    RelativeLayout progressBar;
    RecyclerView rvRequestList;
    ArrayList<RequirementsListData> list = new ArrayList<>();
    RequirementsListAdapter adapter;

    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    private String nextPageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirements_list);

        setTitle("Requirements List");

        boolean isDownloadMOU = getIntent().getBooleanExtra("isDownloadMOU", false);
        boolean isSubmitMOU = getIntent().getBooleanExtra("isSubmitMOU", false);
        boolean isApprovalAllowed = getIntent().getBooleanExtra("isApprovalAllowed", false);

        progressBar = findViewById(R.id.lyProgressBar);
        presenter = new RequirementsListActivityPresenter(this);
        if (Util.isConnected(this)) {
            presenter.getRequirementsList(BuildConfig.BASE_URL + Urls.MissionRahat.CONCENTRATOR_REQUEST_LIST);
        } else {
            Util.showToast(this, getResources().getString(R.string.msg_no_network));
        }
        rvRequestList = findViewById(R.id.rvRequestList);
        adapter = new RequirementsListAdapter(list, this, isDownloadMOU, isSubmitMOU, isApprovalAllowed);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvRequestList.setLayoutManager(layoutManager);
        rvRequestList.setAdapter(adapter);

        rvRequestList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();

                    if (loading) {

                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
//                            getData();
                            if (nextPageUrl != null && !TextUtils.isEmpty(nextPageUrl)) {
                                presenter.getRequirementsList(nextPageUrl);
                            }
                        }
                    }
                }

//                if (dy < -5 && (this).isFilterApplied() &&
//                        btnClearFilters.getVisibility() != View.VISIBLE) {
//                    btnClearFilters.setVisibility(View.VISIBLE);
//                } else if (dy > 5 && (!((MatrimonyProfileListActivity) getActivity()).isFilterApplied()) &&
//                        btnClearFilters.getVisibility() == View.VISIBLE) {
//                    btnClearFilters.setVisibility(View.GONE);
//                }
            }
        });

    }

    public void setTitle(String title) {
        TextView tvTitle = findViewById(R.id.toolbar_title);
        ImageView back = findViewById(R.id.toolbar_back_action);
        tvTitle.setText(title);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            if (resultCode == Activity.RESULT_OK) {
                int position = data.getIntExtra("position", -1);
                String status = data.getStringExtra("status");
                if (position != -1) {
                    if (status.equalsIgnoreCase("MOU_DONE")) {
                        list.get(position).setMOUDone(true);
                    } else {
                        list.get(position).setStatus(status);
                    }

                    adapter.notifyItemChanged(position, list.get(position));
                }
            }
        }
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        Util.showToast(this, message);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        Util.showToast(this, error.getMessage());
    }

    @Override
    public void onSuccessListener(String requestID, String response) {

        try {
            if (response != null) {

                if (requestID.equalsIgnoreCase("RequirementsList")) {
                    RequirementsListResponse data = new Gson().fromJson(response, RequirementsListResponse.class);
                    if (data.getCode() == 200) {
                        nextPageUrl = data.getNextPageUrl();
                        list.addAll(data.getData());
                        adapter.notifyDataSetChanged();
                    } else if (data.getCode() == 1000) {
                        onFailureListener(requestID, data.getMessage());
                        Util.logOutUser(this);
                    }
                } else if (requestID.equalsIgnoreCase("MOU_ON_MAIL")) {
                    CommonResponse commonResponse = new Gson().fromJson(response, CommonResponse.class);
                    if (commonResponse.getCode() == 200) {
                        onFailureListener(requestID, commonResponse.getMessage());
                    } else if (commonResponse.getCode() == 1000) {
                        onFailureListener(requestID, commonResponse.getMessage());
                        Util.logOutUser(this);
                    }
                }
            }
        } catch (
                Exception e) {
            onFailureListener(requestID, e.getMessage());
        }
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

    public void showEmailDialog(String btText, int adapterPosition) {
        Dialog dialog;
        Button btn_submit;
        EditText edt_reason;

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_email_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        edt_reason = dialog.findViewById(R.id.edt_reason);
        btn_submit = dialog.findViewById(R.id.btn_submit);
        btn_submit.setText(btText);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmailId = edt_reason.getText().toString();
                if (!TextUtils.isEmpty(strEmailId.toString().trim())
                        && !Patterns.EMAIL_ADDRESS.matcher(strEmailId.toString().trim()).matches()) {
                    Util.showToast(RequirementsListActivity.this, "Please enter valid email.");
                } else {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("email_id", strEmailId.trim());
                    map.put("requirment_id", list.get(adapterPosition).getId());
                    presenter.getMailMOU(map);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

}