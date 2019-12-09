package com.octopusbjsindia.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.PlatformTaskListener;
import com.octopusbjsindia.models.reports.ReportData;
import com.octopusbjsindia.models.reports.Reports;
import com.octopusbjsindia.presenter.ReportsFragmentPresenter;
import com.octopusbjsindia.utility.AppEvents;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.HomeActivity;
import com.octopusbjsindia.view.adapters.ReportsAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("CanBeFinal")
public class ReportsFragment extends Fragment implements PlatformTaskListener, View.OnClickListener {

    private View reportFragmentView;
    private boolean mShowAllReportsText = true;
    private TextView mNoRecordsView;
    private ExpandableListView mReportsListView;
    private Map<String, List<ReportData>> reportsList = new HashMap<>();

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null && getArguments() != null) {
            String title = (String) getArguments().getSerializable("TITLE");
            ((HomeActivity) getActivity()).setActionBarTitle(title);
            ((HomeActivity) getActivity()).setSyncButtonVisibility(false);

            if ((boolean) getArguments().getSerializable("SHOW_BACK")) {
                ((HomeActivity) getActivity()).showBackArrow();
            }

            mShowAllReportsText = getArguments().getBoolean("SHOW_ALL", true);
        }

        AppEvents.trackAppEvent(getString(R.string.event_reports_screen_visit));
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

        if (getActivity() != null && getArguments() != null) {
            String title = (String) getArguments().getSerializable("TITLE");
            ((HomeActivity) getActivity()).setActionBarTitle(title);
        }

        init();

        List<ReportData> allReports = DatabaseManager.getDBInstance(getContext()).getAllReports();
        if (allReports.isEmpty()) {
            ReportsFragmentPresenter presenter = new ReportsFragmentPresenter(this);
            presenter.getAllReports();
        } else {
            setAdapter(allReports);
        }

    }

    private void init() {
        mNoRecordsView = reportFragmentView.findViewById(R.id.reports_no_data);
        mReportsListView = reportFragmentView.findViewById(R.id.rv_dashboard_reports);

        if (reportsList == null || reportsList.isEmpty()) {
            mNoRecordsView.setVisibility(View.VISIBLE);
        } else {
            mNoRecordsView.setVisibility(View.GONE);
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
            setAdapter(reportData);
        }
    }

    private void setAdapter(final List<ReportData> reportData) {
        reportsList.clear();

        for (int i = 0; i < reportData.size(); i++) {
            ReportData temp = new ReportData();
            temp.setId(reportData.get(i).getId());
            temp.setName(reportData.get(i).getName());
            temp.setDescription(reportData.get(i).getDescription());
            temp.setUrl(reportData.get(i).getUrl());
            temp.setCategory(reportData.get(i).getCategory());

            DatabaseManager.getDBInstance(getContext()).insertReportData(temp);

            if (reportsList.containsKey(reportData.get(i).getCategory().getName().getLocaleValue())) {
                List<ReportData> item = reportsList.get(reportData.get(i).getCategory().getName().getLocaleValue());
                if (item != null) {
                    item.add(temp);
                    reportsList.put(reportData.get(i).getCategory().getName().getLocaleValue(), item);
                }
            } else {
                List<ReportData> item = new ArrayList<>();
                item.add(temp);
                reportsList.put(reportData.get(i).getCategory().getName().getLocaleValue(), item);
            }
        }

        if (reportsList == null || reportsList.isEmpty()) {
            mNoRecordsView.setVisibility(View.VISIBLE);
        } else {
            mNoRecordsView.setVisibility(View.GONE);
        }

        mReportsListView.setAdapter(new ReportsAdapter(getContext(), reportsList));
    }

    @Override
    public void showErrorMessage(String result) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txt_view_all_reports) {
            Util.launchFragment(new ReportsFragment(), getContext(),
                    getString(R.string.reports), true);
        }
    }
}