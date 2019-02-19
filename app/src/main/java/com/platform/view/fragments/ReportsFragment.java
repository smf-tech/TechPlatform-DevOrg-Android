package com.platform.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.platform.R;
import com.platform.listeners.PlatformTaskListener;
import com.platform.models.reports.ReportData;
import com.platform.models.reports.Reports;
import com.platform.presenter.ReportsFragmentPresenter;
import com.platform.view.activities.HomeActivity;
import com.platform.view.adapters.ReportCategoryAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("CanBeFinal")
public class ReportsFragment extends Fragment implements PlatformTaskListener, View.OnClickListener {

    private boolean mShowAllReportsText;
    private View reportFragmentView;
    private ReportCategoryAdapter adapter;
    private List<String> reportsHeaderList = new ArrayList<>();
    private Map<String, List<ReportData>> reportsList = new HashMap<>();

    public ReportsFragment() {
    }

    public static ReportsFragment newInstance(boolean showAllReportsText) {
        Bundle args = new Bundle();
        args.putBoolean("showAllReportsText", showAllReportsText);
        ReportsFragment fragment = new ReportsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mShowAllReportsText = getArguments().getBoolean("showAllReportsText", true);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        reportFragmentView = inflater.inflate(R.layout.fragment_dashboard_reports, container, false);
        return reportFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            ((HomeActivity) getActivity()).setActionBarTitle(
                    getActivity().getResources().getString(R.string.reports));
        }

        init();
        ReportsFragmentPresenter presenter = new ReportsFragmentPresenter(this);
        presenter.getAllReports();
    }

    private void init() {
        RecyclerView recyclerView = reportFragmentView.findViewById(R.id.rv_dashboard_reports);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ReportCategoryAdapter(getActivity(), reportsHeaderList, reportsList);
        recyclerView.setAdapter(adapter);

        if (reportsList == null || reportsList.isEmpty()) {
            reportFragmentView.findViewById(R.id.reports_no_data).setVisibility(View.VISIBLE);
        } else {
            reportFragmentView.findViewById(R.id.reports_no_data).setVisibility(View.GONE);
        }

        TextView txtViewAllForms = reportFragmentView.findViewById(R.id.txt_view_all_reports);
        if (!mShowAllReportsText) {
            txtViewAllForms.setVisibility(View.GONE);
        } else {
            txtViewAllForms.setVisibility(View.VISIBLE);
        }

        txtViewAllForms.setOnClickListener(this);
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public <T> void showNextScreen(T data) {
        if (data != null) {
            List<ReportData> reportData = ((Reports) data).getData();
            reportsHeaderList.clear();
            reportsList.clear();

            for (int i = 0; i < reportData.size(); i++) {
                ReportData temp = new ReportData();
                temp.setId(reportData.get(i).getId());
                temp.setName(reportData.get(i).getName());
                temp.setDescription(reportData.get(i).getDescription());
                temp.setUrl(reportData.get(i).getUrl());

                if (reportsList.containsKey(reportData.get(i).getCategory())) {
                    List<ReportData> item = reportsList.get(reportData.get(i).getCategory());
                    if (item != null) {
                        item.add(temp);
                        reportsList.put(reportData.get(i).getCategory(), item);
                    }
                } else {
                    List<ReportData> item = new ArrayList<>();
                    item.add(temp);
                    reportsList.put(reportData.get(i).getCategory(), item);
                    reportsHeaderList.add(reportData.get(i).getCategory());
                }
            }

            adapter.notifyDataSetChanged();

            if (reportsList == null || reportsList.isEmpty()) {
                reportFragmentView.findViewById(R.id.reports_no_data).setVisibility(View.VISIBLE);
            } else {
                reportFragmentView.findViewById(R.id.reports_no_data).setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showErrorMessage(String result) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_view_all_reports:
                break;
        }
    }
}
