package com.octopusbjsindia.view.adapters;

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
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.CommunityMobilizationActivity;
import com.octopusbjsindia.view.activities.SSActionsActivity;
import com.octopusbjsindia.view.activities.StructureBoundaryActivity;
import com.octopusbjsindia.view.activities.StructureCompletionActivity;
import com.octopusbjsindia.view.activities.StructurePreparationActivity;
import com.octopusbjsindia.view.activities.StructureVisitMonitoringActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SSStructureListAdapter extends RecyclerView.Adapter<SSStructureListAdapter.ViewHolder> {

    final String STRUCTURE_DATA = "StructureData";
    final String STRUCTURE_STATUS = "StructureStatus";

    private ArrayList<StructureData> ssDataList;
    Activity activity;
    boolean isSave, isSaveOfflineStructure, isStructurePreparation, isCommunityMobilization,
            isVisitMonitoring, isStructureComplete, isStructureClose, isStructureBoundary;

    public SSStructureListAdapter(FragmentActivity activity, ArrayList<StructureData> ssStructureListData,
                                  boolean isSave) {
        this.ssDataList = ssStructureListData;
        this.activity = activity;
        this.isSave = isSave;

        RoleAccessAPIResponse roleAccessAPIResponse = Util.getRoleAccessObjectFromPref();
        RoleAccessList roleAccessList = roleAccessAPIResponse.getData();

        if (roleAccessList != null) {
            List<RoleAccessObject> roleAccessObjectList = roleAccessList.getRoleAccess();
            for (RoleAccessObject roleAccessObject : roleAccessObjectList) {
                if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_SAVE_OFFLINE_STRUCTURE)) {
                    isSaveOfflineStructure = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_PREPARED_STRUCTURE)) {
                    isStructurePreparation = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_COMMUNITY_MOBILISATION)) {
                    isCommunityMobilization = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_VISIT_MONITORTNG)) {
                    isVisitMonitoring = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_STRUCTURE_COMPLETE)) {
                    isStructureComplete = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_STRUCTURE_CLOSE)) {
                    isStructureClose = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_STRUCTURE_BOUNDARY)) {
                    isStructureBoundary = true;
                    continue;
                }
            }
        }
    }

    @NonNull
    @Override
    public SSStructureListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ss_structure_item_layout,
                parent, false);
        return new SSStructureListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SSStructureListAdapter.ViewHolder holder, int position) {
        holder.tvStatus.setText(ssDataList.get(position).getStructureStatus());
        holder.tvStructureCode.setText(ssDataList.get(position).getStructureCode());
        holder.tvStructureType.setText(ssDataList.get(position).getStructureType());
        holder.tvWorkType.setText(ssDataList.get(position).getStructureWorkType());
        holder.tvStructureName.setText(ssDataList.get(position).getStructureName());
        holder.tvStructureOwnerDepartment.setText(ssDataList.get(position).getStructureDepartmentName());
        holder.tvTaluka.setText(ssDataList.get(position).getTaluka());
        holder.tvVillage.setText(ssDataList.get(position).getVillage());
        holder.tvUpdated.setText(ssDataList.get(position).getUpdatedDate());
//        if (isSave) {
//            if (ssDataList.get(position).getDeployedMachineDetails().size() == 0) {
//                holder.tvMachinCount.setText("None");
//            } else if (ssDataList.get(position).getDeployedMachineDetails().size() > 1) {
//                holder.tvMachinCount.setText(ssDataList.get(position).getDeployedMachineDetails().size() + " Machines");
//            } else {
//                holder.tvMachinCount.setText(ssDataList.get(position).getDeployedMachineDetails().size() + " Machine");
//            }
//        }
        // commented for RWB
//        if (!isSaveOfflineStructure) {
//            holder.btSave.setVisibility(View.INVISIBLE);
//        } else {
//            // save button visibal
//            if (ssDataList.get(position).isSavedOffine()) {
//                holder.btSave.setVisibility(View.INVISIBLE);
//                DatabaseManager.getDBInstance(Platform.getInstance()).getStructureDataDao()
//                        .insert(ssDataList.get(position));
//            } else {
//                holder.btSave.setTextColor(activity.getApplication().getResources().getColor(R.color.colorPrimaryDark));
//                holder.btSave.setVisibility(View.VISIBLE);
//            }
//        }
//        if (ssDataList.get(position).isStructureBoundary()) {
//            holder.tvBoundary.setVisibility(View.VISIBLE);
//        } else {
//            holder.tvBoundary.setVisibility(View.GONE);
//        }
        if (ssDataList.get(position).getWorkStartDate() != null && !TextUtils.isEmpty(ssDataList.get(position).getWorkStartDate())) {
            holder.lyStartData.setVisibility(View.VISIBLE);
            holder.tvStartDate.setText(ssDataList.get(position).getWorkStartDate());
            if (ssDataList.get(position).getWorkCompletedDate() != null) {
                holder.tvEndDate.setText(ssDataList.get(position).getWorkCompletedDate());
            }
        } else {
            holder.lyStartData.setVisibility(View.GONE);
        }

//        holder.tvReason.setVisibility(View.GONE);
//        holder.tvContact.setText(ssDataList.get(position).get());
    }

    @Override
    public int getItemCount() {
        return ssDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvStatus, tvReason, tvStructureCode, tvStructureType, tvWorkType, tvStructureName,
                tvStructureOwnerDepartment, tvContact, tvUpdated, tvMachinCount, tvTaluka, tvVillage,
                tvBoundary, tvStartDate, tvEndDate;
        ImageView btnPopupMenu;
        LinearLayout lyStartData;
        PopupMenu popup;
        Button btSave;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lyStartData = itemView.findViewById(R.id.ly_start_data);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvReason = itemView.findViewById(R.id.tv_reason);
            tvStructureCode = itemView.findViewById(R.id.tv_structure_code);
            tvStructureType = itemView.findViewById(R.id.tv_structure_type);
            tvWorkType = itemView.findViewById(R.id.tv_work_type);
            tvStructureName = itemView.findViewById(R.id.tv_structure_name);
            tvStructureOwnerDepartment = itemView.findViewById(R.id.tv_structure_owner_department);
            tvContact = itemView.findViewById(R.id.tv_contact);
            tvUpdated = itemView.findViewById(R.id.tv_updated);
            tvMachinCount = itemView.findViewById(R.id.tv_machin_count);
            tvTaluka = itemView.findViewById(R.id.tv_taluka);
            tvVillage = itemView.findViewById(R.id.tv_village);
            tvStartDate = itemView.findViewById(R.id.tv_start_date);
            tvEndDate = itemView.findViewById(R.id.tv_end_date);
            btSave = itemView.findViewById(R.id.bt_save);
            tvBoundary = itemView.findViewById(R.id.tv_boundary);
//            if (isSave) {
//                btSave.setText("Save Offline");
//            } else {
//                btSave.setText("Remove from Offline");
//            }

            btnPopupMenu = itemView.findViewById(R.id.btn_popmenu);
            btnPopupMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup = new PopupMenu((activity), v);
                    popup.inflate(R.menu.structure_popup_menu);
                    popup.show();

//                    if (isCommunityMobilization) {
//                        if (isSave) {
//                            popup.getMenu().findItem(R.id.action_mobilization).setVisible(true);
//                        } else {
//                            popup.getMenu().findItem(R.id.action_mobilization).setVisible(false);
//                        }
//                    } else {
//                        popup.getMenu().findItem(R.id.action_mobilization).setVisible(false);
//                    }

//                    if (isStructureBoundary) {
//                        popup.getMenu().findItem(R.id.action_structure_boundary).setVisible(true);
//                    } else {
//                        popup.getMenu().findItem(R.id.action_structure_boundary).setVisible(false);
//                    }
//                    if (isStructurePreparation) {
//                        if (ssDataList.get(getAdapterPosition()).getStructureStatusCode() == Constants.SSModule.STRUCTURE_APPROVED
//                                || ssDataList.get(getAdapterPosition()).getStructureStatusCode() == Constants.SSModule.STRUCTURE_NON_COMPLIANT
//                                && isVisitMonitoring) {
//                            popup.getMenu().findItem(R.id.action_preparation).setVisible(true);
//                        } else {
//                            popup.getMenu().findItem(R.id.action_preparation).setVisible(false);
//                        }
//                    } else {
//                        popup.getMenu().findItem(R.id.action_preparation).setVisible(false);
//                    }

//                    if (isVisitMonitoring) {
//                        if (ssDataList.get(getAdapterPosition()).getStructureStatusCode() == Constants.SSModule.STRUCTURE_PARTIALLY_COMPLETED
//                                || ssDataList.get(getAdapterPosition()).getStructureStatusCode() == Constants.SSModule.STRUCTURE_COMPLETED
//                                || ssDataList.get(getAdapterPosition()).getStructureStatusCode() == Constants.SSModule.STRUCTURE_CLOSED) {
//                            popup.getMenu().findItem(R.id.action_visit_monitoring).setVisible(false);
//                        } else {
//                            popup.getMenu().findItem(R.id.action_visit_monitoring).setVisible(true);
//                        }
//                    } else {
//                        popup.getMenu().findItem(R.id.action_visit_monitoring).setVisible(false);
//                    }
                    if (isStructureComplete) {
                        if (ssDataList.get(getAdapterPosition()).getStructureStatusCode() == Constants.SSModule.STRUCTURE_APPROVED
                                || ssDataList.get(getAdapterPosition()).getStructureStatusCode() == Constants.SSModule.STRUCTURE_PREPARED
                                || ssDataList.get(getAdapterPosition()).getStructureStatusCode() == Constants.SSModule.STRUCTURE_PARTIALLY_COMPLETED
                                || ssDataList.get(getAdapterPosition()).getStructureStatusCode() == Constants.SSModule.STRUCTURE_COMPLETED
                                || ssDataList.get(getAdapterPosition()).getStructureStatusCode() == Constants.SSModule.STRUCTURE_CLOSED
                                || ssDataList.get(getAdapterPosition()).getStructureStatusCode() == Constants.SSModule.STRUCTURE_PARTIALLY_CLOSED) {
                            popup.getMenu().findItem(R.id.action_structure_completion).setVisible(false);
                        } else {
                            //if (isSave) {
                                popup.getMenu().findItem(R.id.action_structure_completion).setVisible(true);
//                            } else {
//                                popup.getMenu().findItem(R.id.action_structure_completion).setVisible(false);
//                            }
                        }
                    } else {
                        popup.getMenu().findItem(R.id.action_structure_completion).setVisible(false);
                    }

                    if (isStructureClose) {
                        if (ssDataList.get(getAdapterPosition()).getStructureStatusCode() == Constants.SSModule.STRUCTURE_COMPLETED
                                || ssDataList.get(getAdapterPosition()).getStructureStatusCode() == Constants.SSModule.STRUCTURE_PARTIALLY_COMPLETED) {
                            popup.getMenu().findItem(R.id.action_structure_close).setVisible(true);
                        } else {
                            popup.getMenu().findItem(R.id.action_structure_close).setVisible(false);
                        }
                    } else {
                        popup.getMenu().findItem(R.id.action_structure_close).setVisible(false);
                    }

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Intent intent;
                            switch (item.getItemId()) {
                                case R.id.action_silt_transportation_record:
                                    Intent siltTransportationIntent = new Intent(activity, SSActionsActivity.class);
                                    siltTransportationIntent.putExtra("SwitchToFragment", "SiltTransportationRecordFragment");
                                    siltTransportationIntent.putExtra("title", "Silt Transportation Record");
                                    siltTransportationIntent.putExtra("type", "siltTransportRecord");
//                                    siltTransportationIntent.putExtra("machineId",
//                                            ssDataList.get(getAdapterPosition()).getDeployedMachineDetails().get(0).getCode());
                                    siltTransportationIntent.putExtra("structureId", ssDataList.get
                                            (getAdapterPosition()).getStructureId());
                                    activity.startActivity(siltTransportationIntent);
                                    break;
                                case R.id.action_preparation:
                                    showDialog(activity, "Alert", "Are you sure, want to prepare structure?",
                                            "Yes", "No", getAdapterPosition(), 1);
                                    break;
                                case R.id.action_mobilization:
                                    if (Util.isConnected(activity)) {
                                        intent = new Intent(activity, CommunityMobilizationActivity.class);
                                        intent.putExtra(STRUCTURE_DATA, ssDataList.get(getAdapterPosition()));
                                        activity.startActivity(intent);
                                    } else {
                                        Util.showToast(activity.getString(R.string.msg_no_network), activity);
                                    }
                                    break;
                                case R.id.action_visit_monitoring:
//                                    if(ssDataList.get(getAdapterPosition()).getStructureStatus().equalsIgnoreCase("Approved")){
                                    intent = new Intent(activity, StructureVisitMonitoringActivity.class);
                                    intent.putExtra(STRUCTURE_DATA, ssDataList.get(getAdapterPosition()));
                                    activity.startActivity(intent);
//                                    }
                                    break;
                                case R.id.action_structure_completion:
                                    if (ssDataList.get(getAdapterPosition()).isStructureComplete()) {
                                        showDialog(activity, "Alert", "Are you sure, want to Complete Structure?",
                                                "Yes", "No", getAdapterPosition(), 2);
                                    } else {
                                        Util.showToast("Please release all the machine from Structure", activity);
                                    }
                                    break;
                                case R.id.action_structure_close:
                                    showDialog(activity, "Alert", "Are you sure, want to Close Structure?",
                                            "Yes", "No", getAdapterPosition(), 2);
                                    break;
                                case R.id.action_structure_boundary:
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        //noinspection MissingPermission
                                        if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                                && activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                            ActivityCompat.requestPermissions(activity,
                                                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                                            Manifest.permission.ACCESS_FINE_LOCATION},
                                                    Constants.GPS_REQUEST);
                                        } else {
                                            LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
                                            boolean gps_enabled = false;

                                            try {
                                                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                                            } catch (Exception ex) {
                                            }
                                            if (gps_enabled) {
                                                if (ssDataList.get(getAdapterPosition()).isStructureBoundary()) {
                                                    showDialog(activity, "Alert",
                                                            "Structure boundary already recorded, do you want record?",
                                                            "Yes",
                                                            "No",
                                                            getAdapterPosition(), 3);
                                                } else {
                                                    intent = new Intent(activity, StructureBoundaryActivity.class);
                                                    intent.putExtra(STRUCTURE_DATA, ssDataList.get(getAdapterPosition()));
                                                    activity.startActivity(intent);
                                                }
                                            } else {
                                                new AlertDialog.Builder(activity)
                                                        .setTitle("Alert")
                                                        .setMessage("Your GPS Location is OFF, Please turn it ON")
                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                                                activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                                            }
                                                        })
                                                        .setNegativeButton("Cancel", null)
                                                        .show();
                                            }
                                        }
                                    }
                                    break;
                            }
                            return false;
                        }
                    });

//                    Menu menu = popup.getMenu();
//                    for(int i = 0; i<menu.size();i++) {
//                        MenuItem mi = menu.getItem(i);
//                        applyFontToMenuItem(mi);
//                    }
                }
            });


//            lyStructure = itemView.findViewById(R.id.rl_machine);
//            lyStructure.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                }
//            });
            btSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isSave) {
                        DatabaseManager.getDBInstance(Platform.getInstance()).getStructureDataDao()
                                .insert(ssDataList.get(getAdapterPosition()));
                        ssDataList.get(getAdapterPosition()).setSavedOffine(true);
                        notifyItemChanged(getAdapterPosition());
                        Util.showToast("Structure Saved Offline", activity);
                    } else {
                        DatabaseManager.getDBInstance(Platform.getInstance()).getStructureDataDao()
                                .delete(ssDataList.get(getAdapterPosition()).getStructureId());
                        ssDataList.remove(getAdapterPosition());
                        notifyDataSetChanged();
                        Util.showToast("Structure Removed from Offline ", activity);
                    }
                }
            });

            tvMachinCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ssDataList.get(getAdapterPosition()).getDeployedMachineDetails().size() > 0)
                        dialogMachinList(getAdapterPosition());
                }
            });
        }
    }

//    private void applyFontToMenuItem(MenuItem mi) {
//        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/YOUR_FONT.ttf");
//        SpannableString mNewTitle = new SpannableString(mi.getTitle());
//        mNewTitle.setSpan(new CustomTypeFaceSpan("", font,Color.WHITE), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        mi.setTitle(mNewTitle);
//    }

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
//                if (flag == 1) {
//                    Intent intent = new Intent(activity, StructurePreparationActivity.class);
//                    intent.putExtra(STRUCTURE_DATA, ssDataList.get(adapterPosition));
//                    activity.startActivity(intent);
//                } else
                if (flag == 2) {
                    Intent intent = new Intent(activity, StructureCompletionActivity.class);
                    intent.putExtra(STRUCTURE_DATA, ssDataList.get(adapterPosition));
                    intent.putExtra(STRUCTURE_STATUS, ssDataList.get(adapterPosition).getStructureStatusCode());
                    activity.startActivity(intent);
                }
//                    else if (flag == 3) {
//                    Intent intent = new Intent(activity, StructureBoundaryActivity.class);
//                    intent.putExtra(STRUCTURE_DATA, ssDataList.get(adapterPosition));
//                    activity.startActivity(intent);
//                }
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

    void dialogMachinList(int adapterPosition) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(activity));
        dialog.setContentView(R.layout.dialog_deployed_machine_list);

        TextView title = dialog.findViewById(R.id.toolbar_title);
        title.setText("Deployed Machine List");

        RecyclerView rvDeployedMachin = dialog.findViewById(R.id.rv_deployed_machin);
        rvDeployedMachin.setLayoutManager(new LinearLayoutManager(activity));
        DeployedMachineListAdapter adapter = new DeployedMachineListAdapter(
                ssDataList.get(adapterPosition).getDeployedMachineDetails(), activity);
        rvDeployedMachin.setAdapter(adapter);

        ImageView ivClose = dialog.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
}
