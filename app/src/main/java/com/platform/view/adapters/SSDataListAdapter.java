package com.platform.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.models.SujalamSuphalam.MachineData;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.MachineMouActivity;
import com.platform.view.activities.SSActionsActivity;
import com.platform.view.fragments.StructureMachineListFragment;

import java.util.ArrayList;

public class SSDataListAdapter extends RecyclerView.Adapter<SSDataListAdapter.ViewHolder> {
    private ArrayList<MachineData> ssDataList;
    Activity activity;
    StructureMachineListFragment fragment;

    public SSDataListAdapter(Activity activity, StructureMachineListFragment fragment,
                             ArrayList<MachineData> ssDataList){
        this.ssDataList = ssDataList;
        this.activity = activity;
        this.fragment = fragment;
//        if(Util.getUserObjectFromPref().getRoleNames().equals("Operator")){
//        }
    }
    @NonNull
    @Override
    public SSDataListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ss_data_item_layout,
                parent, false);
        return new SSDataListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SSDataListAdapter.ViewHolder holder, int position) {
        MachineData machineData = ssDataList.get(position);
        holder.tvStatus.setText(machineData.getStatus());
        holder.tvMachineCode.setText(machineData.getMachineCode());
        holder.tvCapacity.setText(machineData.getDiselTankCapacity());
        holder.tvProvider.setText(machineData.getProviderName());
        holder.tvMachineModel.setText(machineData.getMakeModel());
        holder.tvContact.setText(machineData.getProviderContactNumber());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatus, tvMachineCode, tvCapacity, tvProvider, tvMachineModel, tvContact;
        ImageView btnPopupMenu;
        RelativeLayout rlMachine;
        PopupMenu popup;
        ViewHolder(View itemView){
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvMachineCode = itemView.findViewById(R.id.tv_machine_code);
            tvCapacity = itemView.findViewById(R.id.tv_capacity);
            tvProvider = itemView.findViewById(R.id.tv_provider);
            tvMachineModel = itemView.findViewById(R.id.tv_machine_model);
            tvContact = itemView.findViewById(R.id.tv_contact);
            btnPopupMenu = itemView.findViewById(R.id.btn_popmenu);
            btnPopupMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup = new PopupMenu((activity), v);
                    popup.inflate(R.menu.machine_forms_menu);
                    popup.show();

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_machine_shifting:
                                    Intent intent = new Intent(activity, SSActionsActivity.class);
                                    intent.putExtra("SwitchToFragment", "MachineDeployStructureListFragment");
                                    intent.putExtra("title", "Shift Machine");
                                    intent.putExtra("type", "shiftMachine");
                                    intent.putExtra("machineId", ssDataList.get(getAdapterPosition()).getId());
                                    intent.putExtra("currentStructureId", ssDataList.get(getAdapterPosition()).getDeployedStrutureId());
                                    activity.startActivity(intent);
                                    break;
                                case R.id.action_machine_visit:
                                    Intent machineVisitIntent = new Intent(activity, SSActionsActivity.class);
                                    machineVisitIntent.putExtra("SwitchToFragment", "MachineVisitValidationFragment");
                                    machineVisitIntent.putExtra("title", "Machine Visit and Validation");
                                    machineVisitIntent.putExtra("type", "visitMachine");
                                    machineVisitIntent.putExtra("machineId", ssDataList.get(getAdapterPosition()).getId());
                                    machineVisitIntent.putExtra("currentStructureId", ssDataList.get(getAdapterPosition()).getDeployedStrutureId());
                                    activity.startActivity(machineVisitIntent);
                                    break;
                                case R.id.action_diesel_record:
                                    //showDialog("Delete Meet", "Are you sure you want to delete this meet?","YES", "NO");
                                    break;
                                case R.id.action_machine_non_utilization:
                                    //showDialog("Delete Meet", "Are you sure you want to delete this meet?","YES", "NO");
                                    break;
                            }
                            return false;
                        }
                    });

                }
            });
            rlMachine = itemView.findViewById(R.id.rl_machine);
            rlMachine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(ssDataList.get(getAdapterPosition()).getStatusCode() == Constants.SSModule.
                            MACHINE_ELIGIBLE_STATUS_CODE ||
                            ssDataList.get(getAdapterPosition()).getStatusCode() == Constants.SSModule.
                                    MACHINE_NEW_STATUS_CODE){
                        Intent mouIntent = new Intent(activity, MachineMouActivity.class);
                        mouIntent.putExtra("SwitchToFragment", "MachineMouFirstFragment");
                        mouIntent.putExtra("machineId", ssDataList.get(getAdapterPosition()).
                                getId());
                        if(ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                Constants.SSModule.MACHINE_ELIGIBLE_STATUS_CODE) {
                            mouIntent.putExtra("statusCode", Constants.SSModule.MACHINE_ELIGIBLE_STATUS_CODE);
                        } else {
                            mouIntent.putExtra("statusCode", Constants.SSModule.MACHINE_NEW_STATUS_CODE);
                        }
                        activity.startActivity(mouIntent);
                    } else if(ssDataList.get(getAdapterPosition()).getStatusCode() ==
                            Constants.SSModule.MACHINE_MOU_DONE_STATUS_CODE){
                        fragment.takeMouDoneAction(getAdapterPosition());
                    } else if(ssDataList.get(getAdapterPosition()).getStatusCode() ==
                            Constants.SSModule.MACHINE_AVAILABLE_STATUS_CODE){
                        Intent intent = new Intent(activity, SSActionsActivity.class);
                        intent.putExtra("SwitchToFragment", "MachineDeployStructureListFragment");
                        intent.putExtra("type", "deployMachine");
                        intent.putExtra("title", "Deploy Machine");
                        intent.putExtra("machineId", ssDataList.get(getAdapterPosition()).getId());
                        activity.startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return ssDataList.size();
    }
}
