package com.octopusbjsindia.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.SujalamSuphalam.DeployedMachine;
import com.octopusbjsindia.view.activities.OperatorActivity;

import java.util.ArrayList;
import java.util.List;

public class DeployedMachineListAdapter extends RecyclerView.Adapter<DeployedMachineListAdapter.ViewHolder> {

    private ArrayList<DeployedMachine> dataList;
    private String StructureId,StructureCode;
    private Context mContext;
    private boolean isDailyMachineRecord;

    public DeployedMachineListAdapter(List<DeployedMachine> dataList,String structureId,String structureCode ,Context mContext, boolean isDailyMachineRecord) {
        this.dataList = (ArrayList<DeployedMachine>)dataList;
        this.mContext = mContext;
        this.isDailyMachineRecord = isDailyMachineRecord;
        this.StructureId = structureId;
        this.StructureCode = structureCode;
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
        holder.tvMachineCode.setText(dataList.get(position).getCode());
        holder.tvMachineStatus.setText(dataList.get(position).getStatus());
        holder.tvMachineUpdate.setText(dataList.get(position).getMachineUpdatedDate());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMachineCode, tvMachineStatus, tvMachineUpdate;
        Button btnDailyRecord;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMachineCode = itemView.findViewById(R.id.tv_machin_code);
            tvMachineStatus = itemView.findViewById(R.id.tv_machin_status);
            tvMachineUpdate = itemView.findViewById(R.id.tv_machin_update);
            btnDailyRecord = itemView.findViewById(R.id.btn_daily_record);
            if(isDailyMachineRecord) {
                btnDailyRecord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent dailyRecordIntent = new Intent(mContext, OperatorActivity.class);
                        dailyRecordIntent.putExtra("machineCode", dataList.get(getAdapterPosition()).getCode());
                        dailyRecordIntent.putExtra("machineId", dataList.get(getAdapterPosition()).getMachineId());
                        dailyRecordIntent.putExtra("structureId", StructureId);
                        dailyRecordIntent.putExtra("structureCode", StructureCode);
                        mContext.startActivity(dailyRecordIntent);
                    }
                });
            } else {
                btnDailyRecord.setVisibility(View.INVISIBLE);
            }
        }
    }
}
