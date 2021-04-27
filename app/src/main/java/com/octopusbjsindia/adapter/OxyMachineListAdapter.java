package com.octopusbjsindia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.databinding.RowOxymachineListBinding;
import com.octopusbjsindia.models.MissionRahat.OxygenMachineList;

import java.util.ArrayList;

public class OxyMachineListAdapter extends RecyclerView.Adapter<OxyMachineListAdapter.OxyMachineViewHolder> {
    Context mContext;
    private ArrayList<OxygenMachineList> oxygenMachineLists;
    private OnRequestItemClicked onRequestItemClicked;


    public OxyMachineListAdapter(Context mContext, ArrayList<OxygenMachineList> oxygenMachineListsReceived, OnRequestItemClicked clickListner) {
        this.mContext = mContext;
        this.oxygenMachineLists = oxygenMachineListsReceived;
        this.onRequestItemClicked = clickListner;
    }

    @NonNull
    @Override
    public OxyMachineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RowOxymachineListBinding binding = RowOxymachineListBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        /*return new OxyMachineViewHolder(RowOxymachineListBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));*/
        return new OxyMachineViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OxyMachineViewHolder holder, int position) {
        holder.rowOxymachineListBinding.tvMachineCode.setText(oxygenMachineLists.get(position).getCode());
        holder.rowOxymachineListBinding.tvStatus.setText(oxygenMachineLists.get(position).getStatus());


        holder.rowOxymachineListBinding.txtStateName.setText(oxygenMachineLists.get(position).getStateName());
        holder.rowOxymachineListBinding.txtDistrictName.setText(oxygenMachineLists.get(position).getDistrictName());

        holder.rowOxymachineListBinding.txtHospitalName.setText(oxygenMachineLists.get(position).getHospitalName());
        holder.rowOxymachineListBinding.txtInchargeName.setText(oxygenMachineLists.get(position).getInchargeName());

        holder.rowOxymachineListBinding.txtModelName.setText(oxygenMachineLists.get(position).getModel());
        holder.rowOxymachineListBinding.txtCapacity.setText(oxygenMachineLists.get(position).getCapacity());

        holder.rowOxymachineListBinding.txtDeployedDate.setText(oxygenMachineLists.get(position).getDeployDate());
        holder.rowOxymachineListBinding.txtWorkingHours.setText(String.valueOf(oxygenMachineLists.get(position).getWorkingHrsCount()));

        holder.rowOxymachineListBinding.txtDonerName.setText(oxygenMachineLists.get(position).getDonorName());
        holder.rowOxymachineListBinding.txtBenefitedCount.setText(String.valueOf(oxygenMachineLists.get(position).getBenefitedPatientCount()));

    }

    @Override
    public int getItemCount() {
        return oxygenMachineLists.size();
    }

    public class OxyMachineViewHolder extends RecyclerView.ViewHolder {
        RowOxymachineListBinding rowOxymachineListBinding;

        public OxyMachineViewHolder(@NonNull RowOxymachineListBinding itemView) {
            super(itemView.getRoot());
            
            rowOxymachineListBinding = itemView;
            rowOxymachineListBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRequestItemClicked.onItemClicked(getAdapterPosition());
                }
            });

        }
    }

    public interface OnRequestItemClicked {
        void onItemClicked(int pos);
    }
}
