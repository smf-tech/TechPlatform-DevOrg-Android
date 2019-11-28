package com.platform.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.models.SujalamSuphalam.DeployedMachine;

import java.util.ArrayList;
import java.util.List;

public class DeployedMachineListAdapter extends RecyclerView.Adapter<DeployedMachineListAdapter.ViewHolder> {

    ArrayList<DeployedMachine> dataList;
    Context mContext;

    public DeployedMachineListAdapter(List<DeployedMachine> dataList, Context mContext) {
        this.dataList = (ArrayList<DeployedMachine>)dataList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_deployed_machine,
                parent, false);
        return new DeployedMachineListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvMachinCode.setText(dataList.get(position).getCode());
        holder.tvMachinStatus.setText(dataList.get(position).getStatus());
        holder.tvMachinUpdate.setText(dataList.get(position).getMachineUpdatedDate());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMachinCode, tvMachinStatus, tvMachinUpdate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMachinCode = itemView.findViewById(R.id.tv_machin_code);
            tvMachinStatus = itemView.findViewById(R.id.tv_machin_status);
            tvMachinUpdate = itemView.findViewById(R.id.tv_machin_update);
        }
    }
}
