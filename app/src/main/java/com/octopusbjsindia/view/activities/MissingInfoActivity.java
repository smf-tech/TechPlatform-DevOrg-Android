package com.octopusbjsindia.view.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.Matrimony.MissingInfoMasterData;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.presenter.MissingInfoActivityPresenter;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MissingInfoActivity extends AppCompatActivity implements APIDataListener, View.OnClickListener,
        CustomSpinnerListener {
    private MissingInfoActivityPresenter presenter;
    private ArrayList<CustomSpinnerObject> selectionMasterList = new ArrayList<>();
    private ArrayList<String> selectedList = new ArrayList<>();

    private RelativeLayout progressBar;
    private EditText etFealds, etEmail, etNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing_info);

        TextView tvTitle = findViewById(R.id.toolbar_title);
        tvTitle.setText("Missing Info");
        presenter = new MissingInfoActivityPresenter(this);

        progressBar = findViewById(R.id.progress_bar);

        etFealds = findViewById(R.id.et_fealds);
        etEmail = findViewById(R.id.et_email);
        etNote = findViewById(R.id.et_note);

        etEmail.setText(getIntent().getStringExtra("user_email"));


        findViewById(R.id.toolbar_back_action).setOnClickListener(this);
        etFealds.setOnClickListener(this);
        findViewById(R.id.bt_submit).setOnClickListener(this);

        presenter.getMaster();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_fealds:
                CustomSpinnerDialogClass cddProject = new CustomSpinnerDialogClass(this, this, "Missing Fields",
                        selectionMasterList, true);
                cddProject.show();
                cddProject.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.toolbar_back_action:
                finish();
                break;
            case R.id.bt_submit:

                if (TextUtils.isEmpty(etFealds.getText().toString())) {
                    Util.showToast("Please select the field's.",this);
                } else if (TextUtils.isEmpty(etNote.getText().toString())) {
                    Util.showToast("Please enter the instructions.",this);
                } else {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("user_email", getIntent().getStringExtra("user_email"));
                    map.put("user_id", getIntent().getStringExtra("user_id"));
                    map.put("fields", etFealds.getText().toString());
                    map.put("comment", etNote.getText().toString());
                    String paramjson = new Gson().toJson(map);

                    presenter.sentRequest(paramjson);
                }
                break;
        }

    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        if (type.equals("Missing Fields")) {
            for (CustomSpinnerObject obj : selectionMasterList) {
                if (obj.isSelected()) {
                    selectedList.add(obj.getName());
                }
            }
            etFealds.setText(TextUtils.join(",", selectedList));
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

    public void onMasterFatched(List<MissingInfoMasterData> data) {
        for (MissingInfoMasterData obj : data) {
            CustomSpinnerObject temp = new CustomSpinnerObject();
            temp.set_id(obj.getId());
            temp.setName(obj.getValue());
            selectionMasterList.add(temp);
        }

    }
}
