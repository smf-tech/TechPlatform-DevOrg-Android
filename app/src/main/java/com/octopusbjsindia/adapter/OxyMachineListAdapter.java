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
        holder.rowOxymachineListBinding.txtMachineCodeValue.setText(oxygenMachineLists.get(position).getCode());
        holder.rowOxymachineListBinding.txtStateName.setText(oxygenMachineLists.get(position).getStateName());
        holder.rowOxymachineListBinding.txtDistricName.setText(oxygenMachineLists.get(position).getDistrictName());
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
