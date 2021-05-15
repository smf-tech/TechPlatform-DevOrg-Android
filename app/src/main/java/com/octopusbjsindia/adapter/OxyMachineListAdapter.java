package com.octopusbjsindia.adapter;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.databinding.RowOxymachineListBinding;
import com.octopusbjsindia.models.MissionRahat.OxygenMachineList;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.CommunityMobilizationActivity;
import com.octopusbjsindia.view.activities.MissionRahat.OxyMachineListActivity;
import com.octopusbjsindia.view.activities.StructureBoundaryActivity;
import com.octopusbjsindia.view.activities.StructureVisitMonitoringActivity;

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
        holder.rowOxymachineListBinding.txtStatus.setText(oxygenMachineLists.get(position).getStatus());
        if(oxygenMachineLists.get(position).getStatus().equalsIgnoreCase("Deployed")){
            holder.rowOxymachineListBinding.txtStatus.setTextColor(mContext.getResources().getColor(R.color.dark_green));
        }else {
            holder.rowOxymachineListBinding.txtStatus.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }

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
        PopupMenu popup;
        public OxyMachineViewHolder(@NonNull RowOxymachineListBinding itemView) {
            super(itemView.getRoot());
            
            rowOxymachineListBinding = itemView;

            rowOxymachineListBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // onRequestItemClicked.onItemClicked(getAdapterPosition());
                }
            });
            if (Util.getUserObjectFromPref().getRoleCode() == Constants.SSModule.ROLE_CODE_MR_HOSPITAL_INCHARGE) {
                rowOxymachineListBinding.btnPopmenu.setVisibility(View.VISIBLE);
            }else {
                rowOxymachineListBinding.btnPopmenu.setVisibility(View.GONE);
            }
            rowOxymachineListBinding.btnPopmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup = new PopupMenu((mContext), v);
                    popup.inflate(R.menu.machinelist_popup_menu);
                    popup.show();
                    if (Util.getUserObjectFromPref().getRoleCode() == Constants.SSModule.ROLE_CODE_MR_HOSPITAL_INCHARGE) {
                        popup.getMenu().findItem(R.id.action_daily_report).setVisible(true);
                        popup.getMenu().findItem(R.id.action_patient_info).setVisible(true);
                    }

                    //------
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Intent intent;
                            switch (item.getItemId()) {
                                case R.id.action_daily_report:
                                    //machine daily report
                                    //onRequestItemClicked.onItemClicked(getAdapterPosition());
                                    ((OxyMachineListActivity)mContext).callToAddDailyReportorPatients(getAdapterPosition(),1);
                                    break;
                                case R.id.action_patient_info:
                                    //Add patient information
                                    ((OxyMachineListActivity)mContext).callToAddDailyReportorPatients(getAdapterPosition(),2);
                                    break;
                            }
                            return false;
                        }
                    });


                    //---------
                }
            });
        }
    }

    public interface OnRequestItemClicked {
        void onItemClicked(int pos);
    }
}
