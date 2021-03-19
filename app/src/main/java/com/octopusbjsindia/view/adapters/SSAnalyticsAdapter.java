package com.octopusbjsindia.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.SujalamSuphalam.SSAnalyticsData;
import com.octopusbjsindia.view.activities.SSActionsActivity;
import com.octopusbjsindia.view.activities.ssgp.GPActionsActivity;

import java.util.ArrayList;

public class SSAnalyticsAdapter extends RecyclerView.Adapter<SSAnalyticsAdapter.ViewHolder> {

    private ArrayList<SSAnalyticsData> ssAnalyticsDataList;
    int viewType;
    String title;
    Context mContext;
    private String project;

    public SSAnalyticsAdapter(Context mContext, ArrayList<SSAnalyticsData> ssAnalyticsDataList,
                              int viewType, String title, String project) {
        this.ssAnalyticsDataList = ssAnalyticsDataList;
        this.viewType = viewType;
        this.title = title;
        this.mContext = mContext;
        this.project = project;
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
                holder.tvResult.setText(String.valueOf(ssAnalyticsData.getPercentValue()) + "%");
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
        LinearLayout lyMain;
        ViewHolder(View itemView){
            super(itemView);
            tvLabel = itemView.findViewById(R.id.tv_label);
            tvResult = itemView.findViewById(R.id.tv_result);
            //tvValue = itemView.findViewById(R.id.tv_value);
            //tvValueUnit = itemView.findViewById(R.id.tv_value_unit);
            pbSsAnalytics = itemView.findViewById(R.id.pb_ss_analytics);
            lyMain = itemView.findViewById(R.id.ly_main);
            lyMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    if (project.equalsIgnoreCase("SS")) {
                        intent = new Intent(mContext, SSActionsActivity.class);
                    } else {
                        intent = new Intent(mContext, GPActionsActivity.class);
                    }
                    intent.putExtra("SwitchToFragment", "StructureMachineListFragment");
                    intent.putExtra("selectedStatus", ssAnalyticsDataList.get(getAdapterPosition()).getStatusCode());
//                    intent.putExtra("selectedStatus",108);
                    intent.putExtra("viewType", viewType);
                    intent.putExtra("title", title);
                    mContext.startActivity(intent);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return ssAnalyticsDataList.size();
    }
}
