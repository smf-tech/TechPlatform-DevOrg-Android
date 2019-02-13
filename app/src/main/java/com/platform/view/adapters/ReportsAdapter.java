package com.platform.view.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.reports.ReportData;

import java.util.List;

@SuppressWarnings("CanBeFinal")
public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ViewHolder> {

    private List<ReportData> mListDataChild;

    ReportsAdapter(List<ReportData> childDataList) {
        this.mListDataChild = childDataList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView mTitle;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.tv_report_title);
        }
    }

    @NonNull
    @Override
    public ReportsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.report_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBindViewHolder(@NonNull ReportsAdapter.ViewHolder viewHolder, int i) {
        viewHolder.mTitle.setText(mListDataChild.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return mListDataChild.size();
    }
}