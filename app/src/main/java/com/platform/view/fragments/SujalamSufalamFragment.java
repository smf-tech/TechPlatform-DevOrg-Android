package com.platform.view.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.platform.R;
import com.platform.listeners.APIDataListener;
import com.platform.models.SujalamSuphalam.SSAnalyticsAPIResponse;
import com.platform.models.SujalamSuphalam.SSAnalyticsData;
import com.platform.models.forms.FormStatusCount;
import com.platform.models.forms.FormStatusCountData;
import com.platform.presenter.SujalamSuphalamFragmentPresenter;
import com.platform.utility.AppEvents;
import com.platform.view.activities.HomeActivity;
import com.platform.view.adapters.SSAnalyticsAdapter;

import java.util.ArrayList;
import java.util.List;

public class SujalamSufalamFragment extends Fragment implements  View.OnClickListener , APIDataListener {

    private View sujalamSufalamFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private TextView tvStructureView, tvMachineView, tvToggle;
    private RecyclerView rvSSAnalytics;
    private int viewType = 1;
    private SSAnalyticsAdapter ssAnalyticsAdapter;
    private ArrayList<SSAnalyticsData> ssAnalyticsDataList = new ArrayList<>();
    private SujalamSuphalamFragmentPresenter sujalamSuphalamFragmentPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null && getArguments() != null) {
            String title = (String) getArguments().getSerializable("TITLE");
            ((HomeActivity) getActivity()).setActionBarTitle(title);
            ((HomeActivity) getActivity()).setSyncButtonVisibility(false);

            if ((boolean) getArguments().getSerializable("SHOW_BACK")) {
                ((HomeActivity) getActivity()).showBackArrow();
            }
        }
        AppEvents.trackAppEvent(getString(R.string.ss_screen_visit));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sujalamSufalamFragmentView = inflater.inflate(R.layout.fragment_sujalam_sufalam, container, false);
        return sujalamSufalamFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null && getArguments() != null) {
            String title = (String) getArguments().getSerializable("TITLE");
            ((HomeActivity) getActivity()).setActionBarTitle(title);
        }
        init();
    }

    private void init(){
        progressBarLayout = sujalamSufalamFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = sujalamSufalamFragmentView.findViewById(R.id.pb_profile_act);
        tvStructureView = sujalamSufalamFragmentView.findViewById(R.id.tv_structure_view);
        tvMachineView = sujalamSufalamFragmentView.findViewById(R.id.tv_machine_view);
        tvToggle = sujalamSufalamFragmentView.findViewById(R.id.tv_toggle);
        tvStructureView.setOnClickListener(this);
        tvMachineView.setOnClickListener(this);
        rvSSAnalytics = sujalamSufalamFragmentView.findViewById(R.id.rv_ss_analytics);
        ssAnalyticsAdapter = new SSAnalyticsAdapter(ssAnalyticsDataList);
        rvSSAnalytics.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvSSAnalytics.setAdapter(ssAnalyticsAdapter);
        sujalamSuphalamFragmentPresenter = new SujalamSuphalamFragmentPresenter(this);
        sujalamSuphalamFragmentPresenter.getAnalyticsData(sujalamSuphalamFragmentPresenter.GET_STRUCTURE_ANALYTICS);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_structure_view:
                viewType = 1;
                tvStructureView.setTextColor(getResources().getColor(R.color.dark_grey));
                tvStructureView.setTypeface(tvStructureView.getTypeface(), Typeface.BOLD);
                tvMachineView.setTextColor(getResources().getColor(R.color.text_lite_grey));
                tvMachineView.setTypeface(tvMachineView.getTypeface(), Typeface.NORMAL);
                tvToggle.setBackgroundResource(R.drawable.ic_toggle_structure_view);
                break;
            case R.id.tv_machine_view:
                viewType = 2;
                tvMachineView.setTextColor(getResources().getColor(R.color.dark_grey));
                tvMachineView.setTypeface(tvMachineView.getTypeface(), Typeface.BOLD);
                tvStructureView.setTextColor(getResources().getColor(R.color.text_lite_grey));
                tvStructureView.setTypeface(tvStructureView.getTypeface(), Typeface.NORMAL);
                tvToggle.setBackgroundResource(R.drawable.ic_toggle_machine_view);
                break;
        }
    }

    public void populateAnalyticsData(SSAnalyticsAPIResponse analyticsData) {
        if (analyticsData != null) {
            ssAnalyticsDataList.clear();
            for (SSAnalyticsData data : analyticsData.getData()) {
                if (data != null) {
                    ssAnalyticsDataList.add(data);
                }
            }
            ssAnalyticsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFailureListener(String requestID, String message) {

    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {

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
}
