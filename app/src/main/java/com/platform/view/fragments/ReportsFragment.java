package com.platform.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.platform.R;
import com.platform.listeners.PlatformTaskListener;
import com.platform.models.reports.ReportData;
import com.platform.models.reports.Reports;
import com.platform.presenter.ReportsFragmentPresenter;
import com.platform.view.adapters.ReportsListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("CanBeFinal")
public class ReportsFragment extends Fragment implements PlatformTaskListener {

    private Context context;
    private View reportFragmentView;
    private ReportsListAdapter adapter;
    private List<String> reportsHeaderList = new ArrayList<>();
    private Map<String, List<ReportData>> reportsList = new HashMap<>();

    private int lastExpandedPosition = -1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        reportFragmentView = inflater.inflate(R.layout.fragment_dashboard_reports, container, false);
        return reportFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();

        init();
        ReportsFragmentPresenter presenter = new ReportsFragmentPresenter(this);
        presenter.getAllReports();
    }

    private void init() {
        adapter = new ReportsListAdapter(context, reportsHeaderList, reportsList);

        ExpandableListView reportsList = reportFragmentView.findViewById(R.id.elv_dashboard_reports);
        reportsList.setAdapter(adapter);
        reportsList.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> false);
        reportsList.setOnGroupExpandListener(groupPosition -> {
            if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
                reportsList.collapseGroup(lastExpandedPosition);
            }
            lastExpandedPosition = groupPosition;
        });
        reportsList.setOnGroupCollapseListener(groupPosition -> {});
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
        }
    }

    @Override
    public void showErrorMessage(String result) {

    }
}
