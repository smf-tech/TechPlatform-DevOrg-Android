package com.platform.view.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.models.SujalamSuphalam.StructureData;
import com.platform.view.fragments.MachineDeployStructureListFragment;

import java.util.ArrayList;

public class StructureListAdapter extends RecyclerView.Adapter<StructureListAdapter.ViewHolder>  {
    private ArrayList<StructureData> ssStructureList;
    Activity activity;
    MachineDeployStructureListFragment fragment;

    public StructureListAdapter(Activity activity, MachineDeployStructureListFragment fragment, ArrayList<StructureData> ssDataList){
        this.ssStructureList = ssDataList;
        this.activity = activity;
        this.fragment = fragment;
    }
    @NonNull
    @Override
    public StructureListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.structure_data_item_layout, parent, false);
        return new StructureListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StructureListAdapter.ViewHolder holder, int position) {
        StructureData machineData = ssStructureList.get(position);
//        holder.tvStatus.setText(machineData.getStatus());
//        holder.tvMachineCode.setText(machineData.getMachineCode());
//        holder.tvCapacity.setText(machineData.getDiselTankCapacity());
//        holder.tvProvider.setText(machineData.getProviderName());
//        holder.tvMachineModel.setText(machineData.getMakeModel());
//        holder.tvContact.setText(machineData.getProviderContactNumber());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatus, tvReason, tvMachineCode, tvCapacity, tvProvider, tvMachineModel, tvContact;
        Button btnDetails;
        RelativeLayout rlMachine;

        ViewHolder(View itemView) {
            super(itemView);
//            tvStatus = itemView.findViewById(R.id.tv_status);
//            tvMachineCode = itemView.findViewById(R.id.tv_machine_code);
//            tvCapacity = itemView.findViewById(R.id.tv_capacity);
//            tvProvider = itemView.findViewById(R.id.tv_provider);
//            tvMachineModel = itemView.findViewById(R.id.tv_machine_model);
//            tvContact = itemView.findViewById(R.id.tv_contact);
//            rlMachine = itemView.findViewById(R.id.rl_machine);
//            rlMachine.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });
        }
    }

    @Override
    public int getItemCount() {
        return ssStructureList.size();
    }
}
