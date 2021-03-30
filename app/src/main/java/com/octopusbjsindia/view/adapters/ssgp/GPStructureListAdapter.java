package com.octopusbjsindia.view.adapters.ssgp;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.models.SujalamSuphalam.StructureData;
import com.octopusbjsindia.models.home.RoleAccessAPIResponse;
import com.octopusbjsindia.models.home.RoleAccessList;
import com.octopusbjsindia.models.home.RoleAccessObject;
import com.octopusbjsindia.models.ssgp.StructureListData;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.CommunityMobilizationActivity;
import com.octopusbjsindia.view.activities.StructureBoundaryActivity;
import com.octopusbjsindia.view.activities.StructureCompletionActivity;
import com.octopusbjsindia.view.activities.StructurePripretionsActivity;
import com.octopusbjsindia.view.activities.StructureVisitMonitoringActivity;
import com.octopusbjsindia.view.activities.ssgp.GPActionsActivity;
import com.octopusbjsindia.view.adapters.DeployedMachineListAdapter;
import com.octopusbjsindia.view.fragments.ssgp.VDCDPRFormFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GPStructureListAdapter extends RecyclerView.Adapter<GPStructureListAdapter.ViewHolder> {

    final String STRUCTURE_DATA = "StructureData";
    final String STRUCTURE_STATUS = "StructureStatus";

    private ArrayList<StructureListData> ssDataList;
    Activity activity;
//    boolean isSaveOfflineStructure, isStructurePreparation, isCommunityMobilization,
//            isVisitMonitoring, isStructureComplete, isStructureClose, isStructureBoundary;

    public GPStructureListAdapter(FragmentActivity activity, ArrayList<StructureListData> ssStructureListData) {
        this.ssDataList = ssStructureListData;
        this.activity = activity;

        RoleAccessAPIResponse roleAccessAPIResponse = Util.getRoleAccessObjectFromPref();
        RoleAccessList roleAccessList = roleAccessAPIResponse.getData();

//        if (roleAccessList != null) {
//            List<RoleAccessObject> roleAccessObjectList = roleAccessList.getRoleAccess();
//            for (RoleAccessObject roleAccessObject : roleAccessObjectList) {
//                if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_SAVE_OFFLINE_STRUCTURE)) {
//                    isSaveOfflineStructure = true;
//                    continue;
//                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_PREPARED_STRUCTURE)) {
//                    isStructurePreparation = true;
//                    continue;
//                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_COMMUNITY_MOBILISATION)) {
//                    isCommunityMobilization = true;
//                    continue;
//                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_VISIT_MONITORTNG)) {
//                    isVisitMonitoring = true;
//                    continue;
//                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_STRUCTURE_COMPLETE)) {
//                    isStructureComplete = true;
//                    continue;
//                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_STRUCTURE_CLOSE)) {
//                    isStructureClose = true;
//                    continue;
//                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_STRUCTURE_BOUNDARY)) {
//                    isStructureBoundary = true;
//                    continue;
//                }
//            }
//        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gp_structure_item_layout,
                parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvStatus.setText(ssDataList.get(position).getStructureStatus());


        holder.tvStructureCode.setText(ssDataList.get(position).getStructureCode());
        holder.tvStructureType.setText(ssDataList.get(position).getStructureType());
        holder.tvInterventionType.setText(ssDataList.get(position).getStructureinterventionType());
        holder.tvMachineCount.setText(""+ssDataList.get(position).getMachineCnt());
        holder.tvTaluka.setText(ssDataList.get(position).getTaluka());
        holder.tvVillage.setText(ssDataList.get(position).getVillage());
//        holder.tvUpdated.setText(ssDataList.get(position).getUpdatedDate());
//        if (isSave) {
//            if (ssDataList.get(position).getDeployedMachineDetails().size() == 0) {
//                holder.tvMachinCount.setText("None");
//            } else if (ssDataList.get(position).getDeployedMachineDetails().size() > 1) {
//                holder.tvMachinCount.setText(ssDataList.get(position).getDeployedMachineDetails().size() + " Machines");
//            } else {
//                holder.tvMachinCount.setText(ssDataList.get(position).getDeployedMachineDetails().size() + " Machine");
//            }
//        }

//        holder.tvReason.setVisibility(View.GONE);
//        holder.tvContact.setText(ssDataList.get(position).get());
    }

    @Override
    public int getItemCount() {
        return ssDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvStatus, tvStructureCode, tvStructureType, tvInterventionType,
                tvMachinCount, tvTaluka, tvVillage, tvMachineCount;
        ImageView btnPopupMenu;
        LinearLayout lyStartData;
        PopupMenu popup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lyStartData = itemView.findViewById(R.id.ly_start_data);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvStructureCode = itemView.findViewById(R.id.tv_structure_code);
            tvStructureType = itemView.findViewById(R.id.tv_structure_type);
            tvInterventionType = itemView.findViewById(R.id.tv_intervention_type);
//            tvStructureName = itemView.findViewById(R.id.tv_structure_name);
            tvMachinCount = itemView.findViewById(R.id.tv_machin_count);
            tvTaluka = itemView.findViewById(R.id.tv_taluka);
            tvVillage = itemView.findViewById(R.id.tv_village);
            tvMachineCount = itemView.findViewById(R.id.tv_machine_count);

            btnPopupMenu = itemView.findViewById(R.id.btn_popmenu);
            btnPopupMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    popup = new PopupMenu((activity), v);
//                    popup.inflate(R.menu.gp_structure_popup_menu);
//                    popup.show();
//                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                        @Override
//                        public boolean onMenuItemClick(MenuItem item) {
//                            Intent intent;
//                            switch (item.getItemId()) {
//                                case R.id.action_dpr:
//                            intent = new Intent(activity, GPActionsActivity.class);
//                            intent.putExtra("SwitchToFragment", "VDCDPRFormFragment");
//                            activity.startActivity(intent);
//                                    break;
//                            }
//                            return false;
//                        }
//                    });
                }
            });

        }
    }
    public void showDialog(Context context, String dialogTitle, String message, String btn1String, String
            btn2String, int adapterPosition, int flag) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(context));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogs_leave_layout);

        if (!TextUtils.isEmpty(dialogTitle)) {
            TextView title = dialog.findViewById(R.id.tv_dialog_title);
            title.setText(dialogTitle);
            title.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(message)) {
            TextView text = dialog.findViewById(R.id.tv_dialog_subtext);
            text.setText(message);
            text.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(btn1String)) {
            Button button = dialog.findViewById(R.id.btn_dialog);
            button.setText(btn1String);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(v -> {

                //Close dialog
                dialog.dismiss();
            });
        }

        if (!TextUtils.isEmpty(btn2String)) {
            Button button1 = dialog.findViewById(R.id.btn_dialog_1);
            button1.setText(btn2String);
            button1.setVisibility(View.VISIBLE);
            button1.setOnClickListener(v -> {
                //Close dialog
                dialog.dismiss();
            });
        }

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

//    void dialogMachinList(int adapterPosition) {
//        final Dialog dialog = new Dialog(Objects.requireNonNull(activity));
//        dialog.setContentView(R.layout.dialog_deployed_machine_list);
//
//        TextView title = dialog.findViewById(R.id.toolbar_title);
//        title.setText("Deployed Machine List");
//
//        RecyclerView rvDeployedMachin = dialog.findViewById(R.id.rv_deployed_machin);
//        rvDeployedMachin.setLayoutManager(new LinearLayoutManager(activity));
//        DeployedMachineListAdapter adapter = new DeployedMachineListAdapter(
//                ssDataList.get(adapterPosition).getDeployedMachineDetails(), activity);
//        rvDeployedMachin.setAdapter(adapter);
//
//        ImageView ivClose = dialog.findViewById(R.id.iv_close);
//        ivClose.setOnClickListener(v -> {
//            dialog.dismiss();
//        });
//
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
//        dialog.show();
//    }
}
