package com.platform.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.platform.R;
import com.platform.models.reports.ReportData;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

@SuppressWarnings("CanBeFinal")
public class ReportCategoryAdapter extends RecyclerView.Adapter<ReportCategoryAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mReportsHeaderList;
    private Map<String, List<ReportData>> mReports;

    public ReportCategoryAdapter(Context context, final List<String> reportsHeaderList,
                                 final Map<String, List<ReportData>> reports) {
        this.mContext = context;
        mReportsHeaderList = reportsHeaderList;
        mReports = reports;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        FloatingActionButton syncButton;
        FloatingActionButton addButton;
        RecyclerView recyclerView;
        ReportsAdapter adapter;
        TextView categoryName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            syncButton = itemView.findViewById(R.id.sync_button);
            addButton = itemView.findViewById(R.id.add_button);
            categoryName = itemView.findViewById(R.id.form_title);
            recyclerView = itemView.findViewById(R.id.forms);
        }
    }

    @NonNull
    @Override
    public ReportCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.form_item,
                viewGroup, false);
        return new ReportCategoryAdapter.ViewHolder(v);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBindViewHolder(@NonNull ReportCategoryAdapter.ViewHolder viewHolder, int i) {

        viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        String category = mReportsHeaderList.get(i);
        viewHolder.categoryName.setText(category);

        List<ReportData> reportData = mReports.get(category);
        viewHolder.adapter = new ReportsAdapter(mContext, reportData);

        viewHolder.addButton.hide();
        viewHolder.syncButton.hide();
        viewHolder.recyclerView.setAdapter(viewHolder.adapter);
    }

    @Override
    public int getItemCount() {
        return mReportsHeaderList.size();
    }
}