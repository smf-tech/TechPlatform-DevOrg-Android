package com.octopus.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.octopus.R;
import com.octopus.models.SujalamSuphalam.SSAnalyticsData;

import java.util.ArrayList;

public class SSAnalyticsAdapter extends RecyclerView.Adapter<SSAnalyticsAdapter.ViewHolder> {

    private ArrayList<SSAnalyticsData> ssAnalyticsDataList;

    public SSAnalyticsAdapter(ArrayList<SSAnalyticsData> ssAnalyticsDataList){
        this.ssAnalyticsDataList = ssAnalyticsDataList;
    }

    @NonNull
    @Override
    public SSAnalyticsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ss_analytics_item_layout, parent, false);
        return new SSAnalyticsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SSAnalyticsAdapter.ViewHolder holder, int position) {
        SSAnalyticsData ssAnalyticsData = ssAnalyticsDataList.get(position);
            if(ssAnalyticsData.getStatus()!=null){
                holder.tvLabel.setText(ssAnalyticsData.getStatus());
                holder.pbSsAnalytics.setProgress(ssAnalyticsData.getPercentValue());
                holder.tvResult.setText(String.valueOf(ssAnalyticsData.getPercentValue()));
//                holder.tvValue.setVisibility(View.INVISIBLE);
//                holder.tvValueUnit.setVisibility(View.INVISIBLE);
            } else{
                holder.tvLabel.setText(ssAnalyticsData.getTitle());
                holder.pbSsAnalytics.setVisibility(View.INVISIBLE);
                holder.tvResult.setVisibility(View.INVISIBLE);
//                holder.tvValue.setText(ssAnalyticsData.getValue());
//                holder.tvValueUnit.setText(ssAnalyticsData.getUnit());
            }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLabel, tvResult, tvValue, tvValueUnit;
        CircularProgressBar pbSsAnalytics;
        ViewHolder(View itemView){
            super(itemView);
            tvLabel = itemView.findViewById(R.id.tv_label);
            tvResult = itemView.findViewById(R.id.tv_result);
            //tvValue = itemView.findViewById(R.id.tv_value);
            //tvValueUnit = itemView.findViewById(R.id.tv_value_unit);
            pbSsAnalytics = itemView.findViewById(R.id.pb_ss_analytics);
        }
    }

    @Override
    public int getItemCount() {
        return ssAnalyticsDataList.size();
    }
}
