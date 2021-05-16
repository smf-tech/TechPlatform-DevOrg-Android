package com.octopusbjsindia.view.activities.MissionRahat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.MissionRahat.SearchListData;
import com.octopusbjsindia.presenter.MissionRahat.ConcentratorRequirementActivityPresenter;
import com.octopusbjsindia.presenter.MissionRahat.SearchListActivityPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.mission_rahat.SearchListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SearchListActivity extends AppCompatActivity implements  APIDataListener {

    RelativeLayout ly_no_data;
    private int actionType = -1;
    private int selectedPosition =  -1;
    private SearchListActivityPresenter presenter;
    private ArrayList<SearchListData> hospitalList = new ArrayList<>();
    private SearchListAdapter adapter;
    private RelativeLayout progressBar;
    private RecyclerView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        progressBar = findViewById(R.id.lyProgressBar);
        presenter = new SearchListActivityPresenter(this);
        if (Util.isConnected(this)) {
            presenter.getHospitals();
        } else {
            Util.showToast(this, getResources().getString(R.string.msg_no_network));
        }
        setTitle("Select Hospital");
        actionType = getIntent().getIntExtra("actionType", -1);
        listView = findViewById(R.id.listView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(mLayoutManager);

        adapter = new SearchListAdapter(hospitalList, SearchListActivity.this);
        listView.setAdapter(adapter);

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

    public void onSelected(int position) {
        selectedPosition = position;
        if (actionType == Constants.MissionRahat.HOSPITAL_SELECTION_FOR_RESULT) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", position);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else if (actionType == Constants.MissionRahat.HOSPITAL_SELECTION_FOR_INCHARGE) {
            // call api to connect in charge to hospital
            showDialog(SearchListActivity.this, "Alert", "Do you want to submit ?"+"\nHospital can not changed once selected.", "No", "Yes");

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
        Util.showToast(this, response);
        finish();
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

    public void setHostpitalList(String get_hospitals, List<SearchListData> data) {
        hospitalList.addAll(data);
        //hospitalList = (ArrayList<SearchListData>) data;
        if (hospitalList.size() == 0)
            findViewById(R.id.ly_no_data).setVisibility(View.VISIBLE);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                adapter.notifyDataSetChanged();

            }
        });

    }

    //assignment confirmation
    public void showDialog(Context context, String dialogTitle, String message, String btn1String, String
            btn2String) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(context));
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
            });
        }

        if (!TextUtils.isEmpty(btn2String)) {
            Button button1 = dialog.findViewById(R.id.btn_dialog_1);
            button1.setText(btn2String);
            button1.setVisibility(View.VISIBLE);
            button1.setOnClickListener(v -> {
                // Close dialog
                try {
                    dialog.dismiss();
                    HashMap request = new HashMap<String, Object>();
                    request.put("hospital_id", hospitalList.get(selectedPosition).getId());
                    presenter.assignHospitaltoIncharge(request);
                } catch (IllegalStateException e) {
                    Log.e("TAG", e.getMessage());
                }
            });
        }

        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
}