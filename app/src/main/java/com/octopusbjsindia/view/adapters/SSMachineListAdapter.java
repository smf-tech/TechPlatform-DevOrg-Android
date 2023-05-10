package com.octopusbjsindia.view.adapters;

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
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.MachineMouActivity;
import com.octopusbjsindia.view.activities.MachineWorkingDataListActivity;
import com.octopusbjsindia.view.activities.OperatorActivity;
import com.octopusbjsindia.view.activities.SSActionsActivity;
import com.octopusbjsindia.view.fragments.StructureMachineListFragment;

import java.util.ArrayList;
import java.util.Objects;

public class SSMachineListAdapter extends RecyclerView.Adapter<SSMachineListAdapter.ViewHolder> {
    private ArrayList<MachineData> ssDataList;
    private Activity activity;
    private StructureMachineListFragment fragment;

    public SSMachineListAdapter(Activity activity, StructureMachineListFragment fragment,
                                ArrayList<MachineData> ssDataList) {
        this.ssDataList = ssDataList;
        this.activity = activity;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public SSMachineListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ss_data_item_layout,
                parent, false);
        return new SSMachineListAdapter.ViewHolder(v);
    }

    private void setTextElseDash(TextView tv, String value) {
        if (value != null && !TextUtils.isEmpty(value)) {
            tv.setText(value);
        } else tv.setText("-");
    }

    @Override
    public void onBindViewHolder(@NonNull SSMachineListAdapter.ViewHolder holder, int position) {
        MachineData machineData = ssDataList.get(position);
        holder.tvStatus.setText(machineData.getStatus());
        holder.tvMachineCode.setText(machineData.getMachineCode());
        //holder.tvSupervisorName.setText(machineData.getSupervisorName());
        setTextElseDash(holder.tvSupervisorName, machineData.getSupervisorName());
        holder.tvMachineModel.setText(machineData.getMakeModel());
        // holder.tvSupervisorContact.setText(machineData.getMachineMobileNumber());
        setTextElseDash(holder.tvSupervisorContact, machineData.getMachineMobileNumber());
        holder.tvTaluka.setText(machineData.getMachineLocation());
        //holder.tvStructureCode.setText(machineData.getDeployedStrutureCode());
        setTextElseDash(holder.tvStructureCode, machineData.getDeployedStructureCode());
        if (machineData.getStatusCode() != Constants.SSModule.MACHINE_NON_ELIGIBLE_STATUS_CODE &&
                machineData.getStatusCode() != Constants.SSModule.MACHINE_ELIGIBLE_STATUS_CODE &&
                machineData.getStatusCode() != Constants.SSModule.MACHINE_NEW_STATUS_CODE &&
                machineData.getStatusCode() != Constants.SSModule.MACHINE_CREATE_STATUS_CODE) {
            holder.tvOperatorName.setVisibility(View.VISIBLE);
            //holder.tvOperatorName.setText(machineData.getOperatorName());
            setTextElseDash(holder.tvOperatorName, machineData.getOperatorName());
            holder.tvOperatorContact.setVisibility(View.VISIBLE);
            // holder.tvOperatorContact.setText(machineData.getOperatorContactNumber());
            setTextElseDash(holder.tvOperatorContact, machineData.getOperatorContactNumber());
        }
        holder.tvLastUpdatedTime.setText(machineData.getLastUpdatedTime());

        if (machineData.getStatusCode() == Constants.SSModule.MACHINE_HALTED_STATUS_CODE) {
            holder.lyReason.setVisibility(View.VISIBLE);
            setTextElseDash(holder.tvReason, machineData.getHaltReason());
            holder.lyAction.setVisibility(View.GONE);
        } else {
            holder.lyReason.setVisibility(View.GONE);
            holder.lyAction.setVisibility(View.VISIBLE);
        }

        if (ssDataList.get(position).getStatusCode() == Constants.SSModule.MACHINE_ELIGIBLE_STATUS_CODE
                || ssDataList.get(position).getStatusCode() == Constants.SSModule.MACHINE_NEW_STATUS_CODE
                || ssDataList.get(position).getStatusCode() == Constants.SSModule.MACHINE_MOU_EXPIRED_STATUS_CODE) {
            if (ssDataList.get(position).getStatusCode() == Constants.SSModule.MACHINE_ELIGIBLE_STATUS_CODE) {
                if (fragment.isMachineMou) {
                    holder.btAction.setVisibility(View.VISIBLE);
                    holder.btAction.setText("Next Step");
                }
            } else if (ssDataList.get(position).getStatusCode() == Constants.SSModule.MACHINE_NEW_STATUS_CODE) {
                if (fragment.isMachineEligible) {
                    holder.btAction.setVisibility(View.VISIBLE);
                    holder.btAction.setText("Set Eligibility");
                }
            } else {
                if (fragment.isMachineMou) {
                    holder.btAction.setVisibility(View.VISIBLE);
                    holder.btAction.setText("Update MOU");
                }
            }
        } else if (ssDataList.get(position).getStatusCode() ==
                Constants.SSModule.MACHINE_NON_ELIGIBLE_STATUS_CODE) {
            if (fragment.isMachineEligible) {
                holder.btAction.setVisibility(View.VISIBLE);
                holder.btAction.setText("Make Eligible");
            }
        } else if (ssDataList.get(position).getStatusCode() == Constants.SSModule.MACHINE_MOU_DONE_STATUS_CODE
                || ssDataList.get(position).getStatusCode() == Constants.SSModule.MACHINE_REALEASED_STATUS_CODE) {
            if (fragment.isMachineTerminate || fragment.isMachineAvailable) {
                holder.btAction.setVisibility(View.VISIBLE);
                holder.btAction.setText("Next Step");
            } else {
                holder.btAction.setVisibility(View.INVISIBLE);
            }
        } else if (ssDataList.get(position).getStatusCode() == Constants.SSModule.MACHINE_AVAILABLE_STATUS_CODE) {
            if (fragment.isMachineDepoly) {
                holder.btAction.setVisibility(View.VISIBLE);
                holder.btAction.setText("Deploy Machine");
            } else {
                holder.btAction.setVisibility(View.INVISIBLE);
            }
        } else {
            holder.btAction.setVisibility(View.INVISIBLE);
        }
        if (ssDataList.get(position).getMachineSignOff() != null && !ssDataList.get(position).getMachineSignOff()) {
            holder.ivSignoff.setVisibility(View.GONE);
        } else {
            holder.ivSignoff.setVisibility(View.VISIBLE);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatus, tvMachineCode, tvSupervisorName, tvMachineModel, tvSupervisorContact, tvStructureCode,
                tvLastUpdatedTime, tvOperatorLabel, tvOperatorName, tvOperatorContactLabel, tvOperatorContact,
                tvTaluka, tvReason;
        Button btAction;
        ImageView btnPopupMenu, ivSignoff;
        LinearLayout rlMachine;
        RelativeLayout lyAction, lyReason;
        PopupMenu popup;

        ViewHolder(View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvMachineCode = itemView.findViewById(R.id.tv_machine_code);
            tvSupervisorName = itemView.findViewById(R.id.tv_supervisor_name);
            tvMachineModel = itemView.findViewById(R.id.tv_machine_model);
            tvTaluka = itemView.findViewById(R.id.tv_taluka);
            tvSupervisorContact = itemView.findViewById(R.id.tv_supervisor_contact);
            btAction = itemView.findViewById(R.id.bt_action);
            lyReason = itemView.findViewById(R.id.ly_reason);
            tvReason = itemView.findViewById(R.id.tv_reason);
            lyAction = itemView.findViewById(R.id.ly_action);
            ivSignoff = itemView.findViewById(R.id.iv_signoff);
            tvSupervisorContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPublishApiDialog("Confirmation",
                            "Are you sure you want to call this number?", "YES",
                            "No", tvSupervisorContact.getText().toString());
                }
            });
            tvStructureCode = itemView.findViewById(R.id.tv_structure_code);
            //tvOperatorLabel = itemView.findViewById(R.id.tv_operator_label);
            tvOperatorName = itemView.findViewById(R.id.tv_operator_name);
            //tvOperatorContactLabel = itemView.findViewById(R.id.tv_operator_contact_label);
            tvOperatorContact = itemView.findViewById(R.id.tv_operator_contact);
            tvOperatorContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showPublishApiDialog("Confirmation",
                            "Are you sure you want to call this number?", "YES", "No", tvOperatorContact.getText().toString());
                }
            });
            tvLastUpdatedTime = itemView.findViewById(R.id.tv_last_updated_time);
            btnPopupMenu = itemView.findViewById(R.id.btn_popmenu);
            btnPopupMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup = new PopupMenu((activity), v);
                    popup.inflate(R.menu.machine_forms_menu);

                    if (fragment.isDailyMachineRecord) {
                        if (ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                Constants.SSModule.MACHINE_DEPLOYED_STATUS_CODE ||
                                ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                        Constants.SSModule.MACHINE_WORKING_STATUS_CODE ||
                                ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                        Constants.SSModule.MACHINE_BREAK_STATUS_CODE ||
                                ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                        Constants.SSModule.MACHINE_STOPPED_STATUS_CODE ||
                                ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                        Constants.SSModule.MACHINE_HALTED_STATUS_CODE ||
                                ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                        Constants.SSModule.MACHINE_PAUSE_STATUS_CODE) {

                            popup.getMenu().findItem(R.id.action_daily_machine_record).setVisible(true);
                        } else {
                            popup.getMenu().findItem(R.id.action_daily_machine_record).setVisible(false);
                        }
                    } else {
                        popup.getMenu().findItem(R.id.action_daily_machine_record).setVisible(false);
                    }

                    if (fragment.isReleaseOperator) {
                        if (ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                Constants.SSModule.MACHINE_DEPLOYED_STATUS_CODE ||
                                ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                        Constants.SSModule.MACHINE_WORKING_STATUS_CODE ||
                                ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                        Constants.SSModule.MACHINE_BREAK_STATUS_CODE ||
                                ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                        Constants.SSModule.MACHINE_STOPPED_STATUS_CODE ||
                                ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                        Constants.SSModule.MACHINE_HALTED_STATUS_CODE ||
                                ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                        Constants.SSModule.MACHINE_PAUSE_STATUS_CODE ||
                                ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                        Constants.SSModule.MACHINE_REALEASED_STATUS_CODE) {

                            if (ssDataList.get(getAdapterPosition()).getOperatorassigned()) {
                                popup.getMenu().findItem(R.id.action_release_supervisor).setVisible(true);
                            } else {
                                popup.getMenu().findItem(R.id.action_release_supervisor).setVisible(false);
                            }
                        } else {
                            popup.getMenu().findItem(R.id.action_release_supervisor).setVisible(false);
                        }
                    } else {
                        popup.getMenu().findItem(R.id.action_release_supervisor).setVisible(false);
                    }

                    if (fragment.isAssignOperator) {
                        if (ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                Constants.SSModule.MACHINE_DEPLOYED_STATUS_CODE ||
                                ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                        Constants.SSModule.MACHINE_WORKING_STATUS_CODE ||
                                ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                        Constants.SSModule.MACHINE_BREAK_STATUS_CODE ||
                                ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                        Constants.SSModule.MACHINE_STOPPED_STATUS_CODE ||
                                ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                        Constants.SSModule.MACHINE_HALTED_STATUS_CODE ||
                                ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                        Constants.SSModule.MACHINE_PAUSE_STATUS_CODE) {
                            if (!ssDataList.get(getAdapterPosition()).getOperatorassigned()) {
                                popup.getMenu().findItem(R.id.action_assign_supervisor).setVisible(true);
                            } else {
                                popup.getMenu().findItem(R.id.action_assign_supervisor).setVisible(false);
                            }
                        } else {
                            popup.getMenu().findItem(R.id.action_assign_supervisor).setVisible(false);
                        }
                    } else {
                        popup.getMenu().findItem(R.id.action_assign_supervisor).setVisible(false);
                    }

                    if (ssDataList.get(getAdapterPosition()).getStatusCode() ==
                            Constants.SSModule.MACHINE_AVAILABLE_STATUS_CODE ||
                            ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                    Constants.SSModule.MACHINE_DEPLOYED_STATUS_CODE ||
                            ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                    Constants.SSModule.MACHINE_WORKING_STATUS_CODE ||
                            ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                    Constants.SSModule.MACHINE_BREAK_STATUS_CODE ||
                            ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                    Constants.SSModule.MACHINE_STOPPED_STATUS_CODE ||
                            ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                    Constants.SSModule.MACHINE_HALTED_STATUS_CODE) {
                        if (fragment.isMachineRelease) {
                            popup.getMenu().findItem(R.id.action_machine_release).setVisible(true);
                        }
                    }
                    popup.show();

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (Util.isConnected(activity)) {
                                switch (item.getItemId()) {
//                                    case R.id.action_machine_shifting:
//                                        if (Util.isConnected(activity)) {
//                                            fragment.shiftMachine(getAdapterPosition());
//                                        } else {
//                                            Util.showToast(activity.getString(R.string.msg_no_network), activity);
//                                        }
//                                        break;
//                                    case R.id.action_machine_visit:
//                                        Intent machineVisitIntent = new Intent(activity, SSActionsActivity.class);
//                                        machineVisitIntent.putExtra("SwitchToFragment", "MachineVisitValidationFragment");
//                                        machineVisitIntent.putExtra("title", "Machine Visit and Validation");
//                                        machineVisitIntent.putExtra("type", "visitMachine");
//                                        machineVisitIntent.putExtra("machineId", ssDataList.get(getAdapterPosition()).getId());
//                                        machineVisitIntent.putExtra("machineCode", ssDataList.get(getAdapterPosition()).getMachineCode());
//                                        machineVisitIntent.putExtra("currentStructureId", ssDataList.get
//                                                (getAdapterPosition()).getDeployedStrutureId());
//                                        activity.startActivity(machineVisitIntent);
//                                        break;
//                                    case R.id.action_diesel_record:
//                                        Intent dieselRecordIntent = new Intent(activity, SSActionsActivity.class);
//                                        dieselRecordIntent.putExtra("SwitchToFragment", "MachineDieselRecordFragment");
//                                        dieselRecordIntent.putExtra("title", "Record of Diesel");
//                                        dieselRecordIntent.putExtra("type", "dieselRecord");
//                                        dieselRecordIntent.putExtra("machineId", ssDataList.get(getAdapterPosition()).getId());
//                                        dieselRecordIntent.putExtra("machineCode", ssDataList.get(getAdapterPosition()).getMachineCode());
//                                        dieselRecordIntent.putExtra("currentStructureId", ssDataList.get
//                                                (getAdapterPosition()).getDeployedStrutureId());
//                                        activity.startActivity(dieselRecordIntent);
//                                        break;
//                                    case R.id.action_silt_transportation_record:
//                                        Intent siltTransportationIntent = new Intent(activity, SSActionsActivity.class);
//                                        siltTransportationIntent.putExtra("SwitchToFragment", "SiltTransportationRecordFragment");
//                                        siltTransportationIntent.putExtra("title", "Silt Transportation Record");
//                                        siltTransportationIntent.putExtra("type", "siltTransportRecord");
//                                        siltTransportationIntent.putExtra("machineId", ssDataList.get(getAdapterPosition()).getId());
//                                        siltTransportationIntent.putExtra("currentStructureId", ssDataList.get
//                                                (getAdapterPosition()).getDeployedStrutureId());
//                                        activity.startActivity(siltTransportationIntent);
//                                        break;
                                    case R.id.action_machine_release:
                                        if (Util.isConnected(activity)) {
                                            fragment.releaseMachine(getAdapterPosition());
                                        } else {
                                            Util.showToast(activity.getString(R.string.msg_no_network), activity);
                                        }
                                        break;
//                                    case R.id.action_machine_worklog:
//                                        if (Util.isConnected(activity)) {
//                                            Intent startMain1 = new Intent(activity, MachineWorkingDataListActivity.class);
//                                            startMain1.putExtra("machineId", ssDataList.get(getAdapterPosition()).getId());
//                                            startMain1.putExtra("machineName", ssDataList.get(getAdapterPosition()).getMachineCode());
//                                            activity.startActivity(startMain1);
//                                        } else {
//                                            Util.showToast(activity.getString(R.string.msg_no_network), activity);
//                                        }
//                                        break;
//                                    case R.id.action_machine_mou_upload:
//                                        Intent mouUploadIntent = new Intent(activity, SSActionsActivity.class);
//                                        mouUploadIntent.putExtra("SwitchToFragment", "MouUploadFragment");
//                                        mouUploadIntent.putExtra("title", "Upload MOU");
//                                        mouUploadIntent.putExtra("machineId", ssDataList.get(getAdapterPosition()).getId());
//                                        mouUploadIntent.putExtra("machineCode", ssDataList.get(getAdapterPosition()).getMachineCode());
//                                        activity.startActivity(mouUploadIntent);
//                                        activity.finish();
//                                        break;
//                                    case R.id.action_machine_signoff:
//                                        if (Util.isConnected(activity)) {
//                                            fragment.sendMachineSignOff(getAdapterPosition());
//                                        } else {
//                                            Util.showToast(activity.getString(R.string.msg_no_network), activity);
//                                        }
//                                        break;
                                    case R.id.action_assign_supervisor:
                                        Intent operatorIntent = new Intent(activity, SSActionsActivity.class);
                                        operatorIntent.putExtra("SwitchToFragment", "SupervisorList");
                                        operatorIntent.putExtra("title", "Field Associates List");
                                        operatorIntent.putExtra("machineId", ssDataList.get(getAdapterPosition()).getMachineId());
                                        operatorIntent.putExtra("machineCode", ssDataList.get(getAdapterPosition()).getMachineCode());
                                        activity.startActivity(operatorIntent);
                                        break;
                                    case R.id.action_release_supervisor:
                                        if (Util.isConnected(activity)) {
                                            fragment.releaseOperator(getAdapterPosition());
                                        } else {
                                            Util.showToast(activity.getString(R.string.msg_no_network), activity);
                                        }
                                        break;
                                    case R.id.action_daily_machine_record:
                                        Intent dailyRecordIntent = new Intent(activity, OperatorActivity.class);
                                        dailyRecordIntent.putExtra("machineId", ssDataList.get(getAdapterPosition()).getMachineId());
                                        dailyRecordIntent.putExtra("machineCode", ssDataList.get(getAdapterPosition()).getMachineCode());
                                        dailyRecordIntent.putExtra("structureId", ssDataList.get(getAdapterPosition()).getDeployedStructureId());
                                        dailyRecordIntent.putExtra("structureCode", ssDataList.get(getAdapterPosition()).getDeployedStructureCode());
                                        activity.startActivity(dailyRecordIntent);
                                }
                            } else {
                                Util.showToast(activity.getString(R.string.msg_no_network), activity);
                            }
                            return false;
                        }
                    });
                }
            });
            btAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ssDataList.get(getAdapterPosition()).getStatusCode() == Constants.SSModule.
                            MACHINE_ELIGIBLE_STATUS_CODE ||
                            ssDataList.get(getAdapterPosition()).getStatusCode() == Constants.SSModule.
                                    MACHINE_NEW_STATUS_CODE || ssDataList.get(getAdapterPosition()).
                            getStatusCode() == Constants.SSModule.
                            MACHINE_MOU_EXPIRED_STATUS_CODE) {
                        if (Util.isConnected(activity)) {
                            Intent mouIntent = new Intent(activity, MachineMouActivity.class);
                            mouIntent.putExtra("SwitchToFragment", "MachineMouFirstFragment");
                            mouIntent.putExtra("machineId", ssDataList.get(getAdapterPosition()).
                                    getMachineId());
                            if (ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                    Constants.SSModule.MACHINE_ELIGIBLE_STATUS_CODE) {
                                mouIntent.putExtra("statusCode", Constants.SSModule.MACHINE_ELIGIBLE_STATUS_CODE);
                            } else if (ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                    Constants.SSModule.MACHINE_NEW_STATUS_CODE) {
                                mouIntent.putExtra("statusCode", Constants.SSModule.MACHINE_NEW_STATUS_CODE);
                            } else {
                                mouIntent.putExtra("statusCode", Constants.SSModule.MACHINE_MOU_EXPIRED_STATUS_CODE);
                            }
                            activity.startActivity(mouIntent);
                        } else {
                            Util.showToast(activity.getString(R.string.msg_no_network), activity);
                        }
                    } else if (ssDataList.get(getAdapterPosition()).getStatusCode() ==
                            Constants.SSModule.MACHINE_NON_ELIGIBLE_STATUS_CODE) {
                        if (Util.isConnected(activity)) {
                            Intent mouIntent = new Intent(activity, MachineMouActivity.class);
                            mouIntent.putExtra("SwitchToFragment", "MachineMouFirstFragment");
                            mouIntent.putExtra("machineId", ssDataList.get(getAdapterPosition()).
                                    getMachineId());
                            mouIntent.putExtra("statusCode", Constants.SSModule.MACHINE_NON_ELIGIBLE_STATUS_CODE);
                            activity.startActivity(mouIntent);
                        } else {
                            Util.showToast(activity.getString(R.string.msg_no_network), activity);
                        }
                    } else if (ssDataList.get(getAdapterPosition()).getStatusCode() ==
                            Constants.SSModule.MACHINE_MOU_DONE_STATUS_CODE ||
                            ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                    Constants.SSModule.MACHINE_REALEASED_STATUS_CODE) {
                        if (Util.isConnected(activity)) {
                            fragment.takeMouDoneAction(getAdapterPosition());
                        } else {
                            Util.showToast(activity.getString(R.string.msg_no_network), activity);
                        }
                    } else if (ssDataList.get(getAdapterPosition()).getStatusCode() ==
                            Constants.SSModule.MACHINE_AVAILABLE_STATUS_CODE) {
                        if (fragment.isMachineDepoly) {
                            if (Util.isConnected(activity)) {
                                fragment.deployMachine(getAdapterPosition());
                            } else {
                                Util.showToast(activity.getString(R.string.msg_no_network), activity);
                            }
                        } else {
                            Util.snackBarToShowMsg(fragment.getActivity().getWindow().getDecorView()
                                            .findViewById(android.R.id.content), "You can not take any action on this machine.",
                                    Snackbar.LENGTH_LONG);
                        }
                    }
                }
            });
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
