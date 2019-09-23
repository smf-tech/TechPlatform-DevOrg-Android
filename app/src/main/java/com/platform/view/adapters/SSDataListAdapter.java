package com.platform.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.models.SujalamSuphalam.MachineData;
import com.platform.models.SujalamSuphalam.SSAnalyticsData;
import com.platform.models.leaves.LeaveData;

import java.util.ArrayList;

public class SSDataListAdapter extends RecyclerView.Adapter<SSDataListAdapter.ViewHolder>  {
    private ArrayList<MachineData> ssDataList;

    public SSDataListAdapter(ArrayList<MachineData> ssDataList){
        this.ssDataList = ssDataList;
    }
    @NonNull
    @Override
    public SSDataListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ss_data_item_layout, parent, false);
        return new SSDataListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SSDataListAdapter.ViewHolder holder, int position) {
        MachineData machineData = ssDataList.get(position);
//        holder.tvLabel.setText(ssAnalyticsData.getStatus());
//        holder.tvResult.setText(String.valueOf(ssAnalyticsData.getPercentValue()));
//        holder.tvValue.setVisibility(View.INVISIBLE);
//        holder.tvValueUnit.setVisibility(View.INVISIBLE);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLabel, tvResult, tvValue, tvValueUnit;
        ViewHolder(View itemView){
            super(itemView);
//            tvLabel = itemView.findViewById(R.id.tv_label);
//            tvResult = itemView.findViewById(R.id.tv_result);
//            tvValue = itemView.findViewById(R.id.tv_value);
//            tvValueUnit = itemView.findViewById(R.id.tv_value_unit);
        }
    }

    @Override
    public int getItemCount() {
        return ssDataList.size();
    }
}
