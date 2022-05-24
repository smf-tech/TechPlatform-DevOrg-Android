package com.octopusbjsindia.view.adapters.smartGirlAdapters;

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
import com.octopusbjsindia.models.smartgirl.SgDashboardResponseModelList;
import com.octopusbjsindia.view.activities.SSActionsActivity;

import java.util.ArrayList;
import java.util.List;

public class SmartGirlAnalyticsAdapter extends RecyclerView.Adapter<SmartGirlAnalyticsAdapter.ViewHolder> {


    private List<SgDashboardResponseModelList> sgDashboardResponseModellist;
    int viewType;
    String title;
    Context mContext;
private OnRequestItemClicked clickListener;
    public SmartGirlAnalyticsAdapter(Context mContext, ArrayList<SgDashboardResponseModelList> ssAnalyticsDataList, int viewType, String title, final OnRequestItemClicked clickListener) {
        this.sgDashboardResponseModellist = ssAnalyticsDataList;
        this.viewType = viewType;
        this.title = title;
        this.mContext = mContext;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public SmartGirlAnalyticsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sg_analytics_item_layout, parent, false);
        return new SmartGirlAnalyticsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SmartGirlAnalyticsAdapter.ViewHolder holder, int position) {
        SgDashboardResponseModelList ssAnalyticsData = sgDashboardResponseModellist.get(position);
            /*if(ssAnalyticsData.getStatus()!=null)*/
            {
                holder.tvLabel.setText(ssAnalyticsData.getSubModule());
                //holder.pbSsAnalytics.setProgress(ssAnalyticsData.getPercentValue());
                holder.tvResult.setText(String.valueOf(ssAnalyticsData.getCount()));
//                holder.tvValue.setVisibility(View.INVISIBLE);
//                holder.tvValueUnit.setVisibility(View.INVISIBLE);
            }
            /*else{
                holder.tvLabel.setText(ssAnalyticsData.getTitle());
                holder.pbSsAnalytics.setVisibility(View.INVISIBLE);
                holder.tvResult.setVisibility(View.INVISIBLE);
//                holder.tvValue.setText(ssAnalyticsData.getValue());
//                holder.tvValueUnit.setText(ssAnalyticsData.getUnit());
            }*/
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
                    /*Intent intent = new Intent(mContext, SSActionsActivity.class);
                    intent.putExtra("SwitchToFragment", "StructureMachineListFragment");
                    intent.putExtra("selectedStatus", ssAnalyticsDataList.get(getAdapterPosition()).getStatusCode());
//                    intent.putExtra("selectedStatus",108);
                    intent.putExtra("viewType", viewType);
                    intent.putExtra("title", title);

                    mContext.startActivity(intent);*/
                }
            });
            itemView.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));
        }
    }

    @Override
    public int getItemCount()
    {
        return sgDashboardResponseModellist.size();
    }

    public interface OnRequestItemClicked {
        void onItemClicked(int pos);
    }
}
