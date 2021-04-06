package com.octopusbjsindia.view.adapters.ssgp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.SujalamSuphalam.MachineData;
import com.octopusbjsindia.models.home.RoleAccessAPIResponse;
import com.octopusbjsindia.models.home.RoleAccessList;
import com.octopusbjsindia.models.home.RoleAccessObject;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.MachineMouActivity;
import com.octopusbjsindia.view.activities.MachineWorkingDataListActivity;
import com.octopusbjsindia.view.activities.SSActionsActivity;
import com.octopusbjsindia.view.activities.ssgp.GPActionsActivity;
import com.octopusbjsindia.view.fragments.StructureMachineListFragment;
import com.octopusbjsindia.view.fragments.ssgp.StructureMachineListGPFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GPMachineListAdapter extends RecyclerView.Adapter<GPMachineListAdapter.ViewHolder> {
    private ArrayList<MachineData> ssDataList;
    private Activity activity;
    private boolean isDallyProgress=false;
    private StructureMachineListGPFragment fragment;

    public GPMachineListAdapter(Activity activity, StructureMachineListGPFragment fragment,
                                ArrayList<MachineData> ssDataList) {
        this.ssDataList = ssDataList;
        this.activity = activity;
        this.fragment = fragment;

        RoleAccessAPIResponse roleAccessAPIResponse = Util.getRoleAccessObjectFromPref();
        RoleAccessList roleAccessList = roleAccessAPIResponse.getData();

        if (roleAccessList != null) {
            List<RoleAccessObject> roleAccessObjectList = roleAccessList.getRoleAccess();
            for (RoleAccessObject roleAccessObject : roleAccessObjectList) {
                if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_DALLY_PROGRESS )) {
                    isDallyProgress = true;
                }
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gp_data_item_layout,
                parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MachineData machineData = ssDataList.get(position);
        holder.tvStatus.setText(machineData.getStatus());
        holder.tvMachineCode.setText(machineData.getMachineCode());
        holder.tvMachineModel.setText(machineData.getMakeModel());
        holder.tvLocation.setText(machineData.getMachineLocation());
        holder.tvLastUpdatedTime.setText(machineData.getLastUpdatedTime());

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatus, tvMachineCode, tvMachineModel, tvLastUpdatedTime, tvLocation;
        Button btAction;
        ImageView btnPopupMenu, ivSignoff;
        LinearLayout rlMachine;
        RelativeLayout lyAction, lyReason;
        PopupMenu popup;

        ViewHolder(View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvMachineCode = itemView.findViewById(R.id.tv_machine_code);
            tvMachineModel = itemView.findViewById(R.id.tv_machine_model);
            tvLocation = itemView.findViewById(R.id.tv_location);
//            tvOwnerValue = itemView.findViewById(R.id.tv_owner_value);
            btAction = itemView.findViewById(R.id.bt_action);
            lyReason = itemView.findViewById(R.id.ly_reason);
            lyAction = itemView.findViewById(R.id.ly_action);
            ivSignoff = itemView.findViewById(R.id.iv_signoff);
            tvLastUpdatedTime = itemView.findViewById(R.id.tv_last_updated_time);
            btnPopupMenu = itemView.findViewById(R.id.btn_popmenu);
            if(isDallyProgress){
                btnPopupMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup = new PopupMenu((activity), v);
                        popup.inflate(R.menu.gp_structure_popup_menu);
                        popup.show();
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Intent intent;
//                            switch (item.getItemId()) {
//                                case R.id.action_dpr:
                                intent = new Intent(activity, GPActionsActivity.class);
                                intent.putExtra("SwitchToFragment", "VDCDPRFormFragment");
                                activity.startActivity(intent);
//                                    break;
//                            }
                                return false;
                            }
                        });
                    }
                });
            }

        }

        private void showPublishApiDialog(String dialogTitle, String message, String btn1String, String
                btn2String, String phoneNumber) {
            final Dialog dialog = new Dialog(Objects.requireNonNull(activity));
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
                    // Close dialog

                    if (Permissions.isCallPermission(activity, this)) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + phoneNumber));
                        activity.startActivity(callIntent);
                    }
                    dialog.dismiss();
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
            dialog.show();
        }
    }

    @Override
    public int getItemCount() {
        return ssDataList.size();
    }
}
