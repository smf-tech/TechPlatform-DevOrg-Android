package com.platform.view.adapters;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.Platform;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.models.SujalamSuphalam.StructureData;
import com.platform.models.home.RoleAccessAPIResponse;
import com.platform.models.home.RoleAccessList;
import com.platform.models.home.RoleAccessObject;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.CommunityMobilizationActivity;
import com.platform.view.activities.CreateStructureActivity;
import com.platform.view.activities.StructureCompletionActivity;
import com.platform.view.activities.StructurePripretionsActivity;
import com.platform.view.activities.StructureVisitMonitoringActivity;
import com.platform.view.fragments.StructureMachineListFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SSStructureListAdapter extends RecyclerView.Adapter<SSStructureListAdapter.ViewHolder> {

    final String STRUCTURE_DATA = "StructureData";

    private ArrayList<StructureData> ssDataList;
    Activity activity;
    boolean isSave, isSaveOfflineStructure, isStructurePreparation, isCommunityMobilization,
            isVisitMonitoring, isStructureComplete;

    public SSStructureListAdapter(FragmentActivity activity, ArrayList<StructureData> ssStructureListData,
                                  boolean isSave) {
        this.ssDataList = ssStructureListData;
        this.activity = activity;
        this.isSave = isSave;

        RoleAccessAPIResponse roleAccessAPIResponse = Util.getRoleAccessObjectFromPref();
        RoleAccessList roleAccessList = roleAccessAPIResponse.getData();
        List<RoleAccessObject> roleAccessObjectList = roleAccessList.getRoleAccess();
        for (RoleAccessObject roleAccessObject : roleAccessObjectList) {
            if(roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_SAVE_OFFLINE_STRUCTURE)) {
                isSaveOfflineStructure = true;
                continue;
            } else if(roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_PREPARED_STRUCTURE)) {
                isStructurePreparation = true;
                continue;
            } else if(roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_COMMUNITY_MOBILISATION)) {
                isCommunityMobilization = true;
                continue;
            } else if(roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_VISIT_MONITORTNG)) {
                isVisitMonitoring = true;
                continue;
            } else if(roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_STRUCTURE_COMPLETE)) {
                isStructureComplete = true;
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
        if (false) {
//            holder.tvReason.setText(ssDataList.get(position).get());
        } else {
            holder.tvReason.setVisibility(View.GONE);
        }
        holder.tvStructureCode.setText(ssDataList.get(position).getStructureCode());
        holder.tvStructureType.setText(ssDataList.get(position).getStructureType());
        holder.tvWorkType.setText(ssDataList.get(position).getStructureType());
        holder.tvStructureName.setText(ssDataList.get(position).getStructureStatus());
        holder.tvStructureOwnerDepartment.setText(ssDataList.get(position).getStructureDepartmentName());
//        holder.tvContact.setText(ssDataList.get(position).get());
    }

    @Override
    public int getItemCount() {
        return ssDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvStatus, tvReason, tvStructureCode, tvStructureType, tvWorkType, tvStructureName,
                tvStructureOwnerDepartment, tvContact;
        ImageView btnPopupMenu;
        LinearLayout lyStructure;
        PopupMenu popup;
        Button btSave;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvReason = itemView.findViewById(R.id.tv_reason);
            tvStructureCode = itemView.findViewById(R.id.tv_structure_code);
            tvStructureType = itemView.findViewById(R.id.tv_structure_type);
            tvWorkType = itemView.findViewById(R.id.tv_work_type);
            tvStructureName = itemView.findViewById(R.id.tv_structure_name);
            tvStructureOwnerDepartment = itemView.findViewById(R.id.tv_structure_owner_department);
            tvContact = itemView.findViewById(R.id.tv_contact);
            btSave = itemView.findViewById(R.id.bt_save);
            if (isSave) {
                btSave.setText("Save Offline");
            } else {
                btSave.setText("Remove from Offline");
            }

            if(!isSaveOfflineStructure){
                btSave.setVisibility(View.GONE);
            }

            btnPopupMenu = itemView.findViewById(R.id.btn_popmenu);
            btnPopupMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup = new PopupMenu((activity), v);
                    popup.inflate(R.menu.structure_popup_menu);
                    popup.show();

                    if (isCommunityMobilization) {
                        popup.getMenu().findItem(R.id.action_mobilization).setVisible(true);
                    }
                    if (isVisitMonitoring) {
                        popup.getMenu().findItem(R.id.action_visit_monitoring).setVisible(true);
                    }
                    if (isStructureComplete) {
                        popup.getMenu().findItem(R.id.action_structure_completion).setVisible(true);
                    }

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Intent intent;
                            switch (item.getItemId()) {
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
                                    intent = new Intent(activity, StructureCompletionActivity.class);
                                    intent.putExtra(STRUCTURE_DATA, ssDataList.get(getAdapterPosition()));
                                    activity.startActivity(intent);
                                    break;

                            }
                            return false;
                        }
                    });

                }
            });
            lyStructure = itemView.findViewById(R.id.rl_machine);
            lyStructure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ssDataList.get(getAdapterPosition()).getStructureStatusCode() == 115 && isVisitMonitoring) {
                        showDialog(activity, "Alert", "Are you sure, want to prepare structure?",
                                "Yes", "No", getAdapterPosition());

                    }
                }
            });
            btSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isSave) {
                        DatabaseManager.getDBInstance(Platform.getInstance()).getStructureDataDao()
                                .insert(ssDataList.get(getAdapterPosition()));
                    } else {
                        DatabaseManager.getDBInstance(Platform.getInstance()).getStructureDataDao()
                                .delete(ssDataList.get(getAdapterPosition()).getStructureId());
                        ssDataList.remove(getAdapterPosition());
                        notifyDataSetChanged();
                    }

                }
            });
        }
    }

    public void showDialog(Context context, String dialogTitle, String message, String btn1String, String
            btn2String, int adapterPosition) {
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
                Intent intent = new Intent(activity, StructurePripretionsActivity.class);
                intent.putExtra(STRUCTURE_DATA, ssDataList.get(adapterPosition));
                activity.startActivity(intent);
            });
        }

        if (!TextUtils.isEmpty(btn2String)) {
            Button button1 = dialog.findViewById(R.id.btn_dialog_1);
            button1.setText(btn2String);
            button1.setVisibility(View.VISIBLE);
            button1.setOnClickListener(v -> {
                // Close dialog
                dialog.dismiss();
            });
        }

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
}
