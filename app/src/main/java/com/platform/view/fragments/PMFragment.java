package com.platform.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.platform.R;
import com.platform.listeners.PlatformTaskListener;
import com.platform.models.pm.ProcessData;
import com.platform.models.pm.Processes;
import com.platform.presenter.PMFragmentPresenter;
import com.platform.utility.Constants;
import com.platform.view.activities.FormActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PMFragment extends Fragment implements View.OnClickListener, PlatformTaskListener {
    private final String TAG = this.getClass().getSimpleName();

    private View pmFragmentView;
    private ProgressBar progressBar;
    private ArrayList<String> processCategoryList = new ArrayList<>();
    private HashMap<String, List<ProcessData>> processMap = new HashMap<>();

    private RecyclerView rvDashboardForms;

    private PMFragmentPresenter pmFragmentPresenter;

    private LinearLayout lnrOuter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        pmFragmentView = inflater.inflate(R.layout.fragment_dashboard_pm, container, false);
        return pmFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pmFragmentPresenter = new PMFragmentPresenter(this);
        pmFragmentPresenter.getAllProcess();

        init();
    }

    private void init() {
        rvDashboardForms = pmFragmentView.findViewById(R.id.rv_dashboard_pending_forms);

        lnrOuter = pmFragmentView.findViewById(R.id.lnr_dashboard_forms_category);
    }

    private void populateData(Processes process) {
        if (process != null) {
            processCategoryList.clear();
            processMap.clear();

            for (ProcessData data : process.getData()) {
                if (data != null && data.getCategory() != null) {
                    String categoryName = data.getCategory().getName();
                    if (!TextUtils.isEmpty(categoryName)) {
                        if (processMap.containsKey(categoryName)) {
                            List<ProcessData> processData = processMap.get(categoryName);
                            if (processData != null) {
                                processData.add(data);
                                processMap.put(categoryName, processData);
                            }
                        } else {
                            List<ProcessData> processData = new ArrayList<>();
                            processData.add(data);
                            processMap.put(categoryName, processData);
                            processCategoryList.add(categoryName);
                        }
                    }
                }
            }

            for (int index = 0; index < processMap.size(); index++) {
                if (processMap != null && !TextUtils.isEmpty(processCategoryList.get(index)) && processMap.get(processCategoryList.get(index)) != null) {
                    createCategoryLayout(processCategoryList.get(index), processMap.get(processCategoryList.get(index)));
                }
            }
        }
    }

    private void createCategoryLayout(String categoryName, List<ProcessData> childList) {
        View formTitleView = getLayoutInflater().inflate(R.layout.row_dashboard_forms_category, lnrOuter, false);
        ((TextView) formTitleView.findViewById(R.id.txt_dashboard_form_category_name)).setText(categoryName);
        LinearLayout lnrInner = formTitleView.findViewById(R.id.lnr_inner);

        for (ProcessData data :
                childList) {
            if (!TextUtils.isEmpty(data.getName())) {
                View formTypeView = getLayoutInflater().inflate(R.layout.row_dashboard_forms_category_card_view, lnrInner, false);
                ((TextView) formTypeView.findViewById(R.id.txt_dashboard_form_title)).setText(data.getName());
                if (!TextUtils.isEmpty(data.getId())) {
                    ImageView imgCreateForm = formTypeView.findViewById(R.id.iv_create_form);
                    imgCreateForm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), FormActivity.class);
                            intent.putExtra(Constants.PM.PROCESS_ID, data.getId());
                            startActivity(intent);
                        }
                    });
                }
                lnrInner.addView(formTypeView);
            }
        }
        lnrOuter.addView(lnrInner);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public <T> void showNextScreen(T data) {
        populateData((Processes) data);
    }

    @Override
    public void showErrorMessage(String result) {

    }
}
