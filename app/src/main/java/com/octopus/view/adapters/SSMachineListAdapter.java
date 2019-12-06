package com.octopus.view.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.octopus.R;
import com.octopus.models.SujalamSuphalam.MachineData;
import com.octopus.utility.Constants;
import com.octopus.utility.Permissions;
import com.octopus.utility.Util;
import com.octopus.view.activities.MachineMouActivity;
import com.octopus.view.activities.MachineWorkingDataListActivity;
import com.octopus.view.activities.SSActionsActivity;
import com.octopus.view.fragments.StructureMachineListFragment;

import java.util.ArrayList;

public class SSMachineListAdapter extends RecyclerView.Adapter<SSMachineListAdapter.ViewHolder> {
    private ArrayList<MachineData> ssDataList;
    private Activity activity;
    private StructureMachineListFragment fragment;
    //private boolean isSettingsRequired;

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

    @Override
    public void onBindViewHolder(@NonNull SSMachineListAdapter.ViewHolder holder, int position) {
        MachineData machineData = ssDataList.get(position);
        holder.tvStatus.setText(machineData.getStatus());
        holder.tvMachineCode.setText(machineData.getMachineCode());
//        holder.tvCapacity.setText(machineData.getDiselTankCapacity());
        holder.tvProvider.setText(machineData.getProviderName());
        holder.tvMachineModel.setText(machineData.getMakeModel());
        holder.tvContact.setText(machineData.getProviderContactNumber());
        holder.tvLocation.setText(machineData.getMachineLocation());
        holder.tvOwnerValue.setText(machineData.getOwnedBy());
        holder.tvStructureCode.setText(machineData.getDeployedStrutureCode());
        if (machineData.getStatusCode() != Constants.SSModule.MACHINE_NON_ELIGIBLE_STATUS_CODE &&
                machineData.getStatusCode() != Constants.SSModule.MACHINE_ELIGIBLE_STATUS_CODE &&
                machineData.getStatusCode() != Constants.SSModule.MACHINE_NEW_STATUS_CODE &&
                machineData.getStatusCode() != Constants.SSModule.MACHINE_CREATE_STATUS_CODE) {
            holder.tvOperator.setVisibility(View.VISIBLE);
            holder.tvOperator.setText(machineData.getOperatorName());
            holder.tvOperatorContact.setVisibility(View.VISIBLE);
            holder.tvOperatorContact.setText(machineData.getOperatorContactNumber());
        }
        holder.tvLastUpdatedTime.setText(machineData.getLastUpdatedTime());

        if (ssDataList.get(position).getStatusCode() == Constants.SSModule.MACHINE_ELIGIBLE_STATUS_CODE
                || ssDataList.get(position).getStatusCode() == Constants.SSModule.MACHINE_NEW_STATUS_CODE
                || ssDataList.get(position).getStatusCode() == Constants.SSModule.MACHINE_MOU_EXPIRED_STATUS_CODE) {
            if (ssDataList.get(position).getStatusCode() == Constants.SSModule.MACHINE_ELIGIBLE_STATUS_CODE) {
                if (fragment.isMachineMou) {
                    holder.btAction.setVisibility(View.VISIBLE);
                    holder.btAction.setText("Do MOU");
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
//                Util.snackBarToShowMsg(fragment.getActivity().getWindow().getDecorView()
//                                .findViewById(android.R.id.content), "You can not take any action on this machine.",
//                        Snackbar.LENGTH_LONG);
            }
        } else {
            holder.btAction.setVisibility(View.INVISIBLE);
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatus, tvMachineCode, tvProvider, tvMachineModel, tvContact, tvStructureCode,
                tvLastUpdatedTime, tvOperatorLabel, tvOperator, tvOperatorContactLabel, tvOperatorContact,
                tvLocation, tvOwnerValue;
        Button btAction;
        ImageView btnPopupMenu;
        LinearLayout rlMachine;
        PopupMenu popup;

        ViewHolder(View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvMachineCode = itemView.findViewById(R.id.tv_machine_code);
//            tvCapacity = itemView.findViewById(R.id.tv_capacity);
            tvProvider = itemView.findViewById(R.id.tv_provider);
            tvMachineModel = itemView.findViewById(R.id.tv_machine_model);
            tvLocation = itemView.findViewById(R.id.tv_location);
            tvOwnerValue = itemView.findViewById(R.id.tv_owner_value);
            tvContact = itemView.findViewById(R.id.tv_contact);
            btAction = itemView.findViewById(R.id.bt_action);
            tvContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Permissions.isCallPermission(activity, this)) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + tvContact.getText().toString()));
                        activity.startActivity(callIntent);
                    }
                }
            });
            tvStructureCode = itemView.findViewById(R.id.tv_structure_code);
            tvOperatorLabel = itemView.findViewById(R.id.tv_operator_label);
            tvOperator = itemView.findViewById(R.id.tv_operator);
            tvOperatorContactLabel = itemView.findViewById(R.id.tv_operator_contact_label);
            tvOperatorContact = itemView.findViewById(R.id.tv_operator_contact);
            tvOperatorContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Permissions.isCallPermission(activity, this)) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + tvOperatorContact.getText().toString()));
                        activity.startActivity(callIntent);
                    }
                }
            });
            tvLastUpdatedTime = itemView.findViewById(R.id.tv_last_updated_time);
            btnPopupMenu = itemView.findViewById(R.id.btn_popmenu);

//            if (fragment.isSiltTransportForm) {
//                isSettingsRequired = true;
//            } else if (fragment.isDieselRecordForm) {
//                isSettingsRequired = true;
//            } else if (fragment.isMachineVisitValidationForm) {
//                isSettingsRequired = true;
//            } else if (fragment.isMachineShiftForm) {
//                isSettingsRequired = true;
//            } else if (fragment.isMachineRelease) {
//                isSettingsRequired = true;
//            }
//            if (isSettingsRequired) {
//                btnPopupMenu.setVisibility(View.VISIBLE);
//            } else {
//                btnPopupMenu.setVisibility(View.INVISIBLE);
//            }
            btnPopupMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup = new PopupMenu((activity), v);
                    popup.inflate(R.menu.machine_forms_menu);
                    popup.getMenu().findItem(R.id.action_machine_worklog).setVisible(true);
                    if (ssDataList.get(getAdapterPosition()).getStatusCode() ==
                            Constants.SSModule.MACHINE_STOPPED_STATUS_CODE ||
                            ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                    Constants.SSModule.MACHINE_HALTED_STATUS_CODE) {
                        //popup.getMenu().findItem(R.id.action_machine_non_utilization).setVisible(true);
                        if (fragment.isSiltTransportForm) {
                            popup.getMenu().findItem(R.id.action_silt_transportation_record).setVisible(true);
                        }
                        if (fragment.isDieselRecordForm) {
                            popup.getMenu().findItem(R.id.action_diesel_record).setVisible(true);
                        }
                        if (fragment.isMachineVisitValidationForm) {
                            popup.getMenu().findItem(R.id.action_machine_visit).setVisible(true);
                        }
                        if (fragment.isMachineShiftForm) {
                            popup.getMenu().findItem(R.id.action_machine_shifting).setVisible(true);
                        }
                    }
                    if (ssDataList.get(getAdapterPosition()).getStatusCode() ==
                            Constants.SSModule.MACHINE_WORKING_STATUS_CODE ||
                            ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                    Constants.SSModule.MACHINE_BREAK_STATUS_CODE) {
                        if (fragment.isSiltTransportForm) {
                            popup.getMenu().findItem(R.id.action_silt_transportation_record).setVisible(true);
                        }
                        if (fragment.isDieselRecordForm) {
                            popup.getMenu().findItem(R.id.action_diesel_record).setVisible(true);
                        }
                        if (fragment.isMachineVisitValidationForm) {
                            popup.getMenu().findItem(R.id.action_machine_visit).setVisible(true);
                        }
                        if (fragment.isMachineShiftForm) {
                            popup.getMenu().findItem(R.id.action_machine_shifting).setVisible(true);
                        }
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
                    if (ssDataList.get(getAdapterPosition()).getStatusCode() ==
                            Constants.SSModule.MACHINE_MOU_TERMINATED_STATUS_CODE ||
                            ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                    Constants.SSModule.MACHINE_MOU_EXPIRED_STATUS_CODE) {
                        if (fragment.isSiltTransportForm) {
                            popup.getMenu().findItem(R.id.action_silt_transportation_record).setVisible(true);
                        }
                    }
                    popup.show();

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (Util.isConnected(activity)) {
                                switch (item.getItemId()) {
                                    case R.id.action_machine_shifting:
                                        Intent intent = new Intent(activity, SSActionsActivity.class);
                                        intent.putExtra("SwitchToFragment", "MachineDeployStructureListFragment");
                                        intent.putExtra("title", "Select Structure");
                                        intent.putExtra("type", "shiftMachine");
                                        intent.putExtra("machineId", ssDataList.get(getAdapterPosition()).getId());
                                        intent.putExtra("machineCode", ssDataList.get(getAdapterPosition()).getMachineCode());
                                        intent.putExtra("currentStructureId", ssDataList.get
                                                (getAdapterPosition()).getDeployedStrutureId());
                                        activity.startActivity(intent);
                                        break;
                                    case R.id.action_machine_visit:
                                        Intent machineVisitIntent = new Intent(activity, SSActionsActivity.class);
                                        machineVisitIntent.putExtra("SwitchToFragment", "MachineVisitValidationFragment");
                                        machineVisitIntent.putExtra("title", "Machine Visit and Validation");
                                        machineVisitIntent.putExtra("type", "visitMachine");
                                        machineVisitIntent.putExtra("machineId", ssDataList.get(getAdapterPosition()).getId());
                                        machineVisitIntent.putExtra("machineCode", ssDataList.get(getAdapterPosition()).getMachineCode());
                                        machineVisitIntent.putExtra("currentStructureId", ssDataList.get
                                                (getAdapterPosition()).getDeployedStrutureId());
                                        activity.startActivity(machineVisitIntent);
                                        break;
                                    case R.id.action_diesel_record:
                                        Intent dieselRecordIntent = new Intent(activity, SSActionsActivity.class);
                                        dieselRecordIntent.putExtra("SwitchToFragment", "MachineDieselRecordFragment");
                                        dieselRecordIntent.putExtra("title", "Record of Diesel");
                                        dieselRecordIntent.putExtra("type", "dieselRecord");
                                        dieselRecordIntent.putExtra("machineId", ssDataList.get(getAdapterPosition()).getId());
                                        dieselRecordIntent.putExtra("machineCode", ssDataList.get(getAdapterPosition()).getMachineCode());
                                        dieselRecordIntent.putExtra("currentStructureId", ssDataList.get
                                                (getAdapterPosition()).getDeployedStrutureId());
                                        activity.startActivity(dieselRecordIntent);
                                        break;
//                                    case R.id.action_machine_non_utilization:
//                                        Intent machineNonUtilizationIntent = new Intent(activity, SSActionsActivity.class);
//                                        machineNonUtilizationIntent.putExtra("SwitchToFragment", "MachineNonUtilizationFragment");
//                                        machineNonUtilizationIntent.putExtra("title", "Machine Non-utilization Record");
//                                        machineNonUtilizationIntent.putExtra("type", "machineNonUtilization");
//                                        machineNonUtilizationIntent.putExtra("machineId", ssDataList.get(getAdapterPosition()).getId());
//                                        machineNonUtilizationIntent.putExtra("currentStructureId", ssDataList.get
//                                                (getAdapterPosition()).getDeployedStrutureId());
//                                        activity.startActivity(machineNonUtilizationIntent);
//                                        break;
                                    case R.id.action_silt_transportation_record:
                                        Intent siltTransportationIntent = new Intent(activity, SSActionsActivity.class);
                                        siltTransportationIntent.putExtra("SwitchToFragment", "SiltTransportationRecordFragment");
                                        siltTransportationIntent.putExtra("title", "Silt Transportation Record");
                                        siltTransportationIntent.putExtra("type", "siltTransportRecord");
                                        siltTransportationIntent.putExtra("machineId", ssDataList.get(getAdapterPosition()).getId());
                                        siltTransportationIntent.putExtra("currentStructureId", ssDataList.get
                                                (getAdapterPosition()).getDeployedStrutureId());
                                        activity.startActivity(siltTransportationIntent);
                                        break;
                                    case R.id.action_machine_release:
                                        if (Util.isConnected(activity)) {
                                            fragment.releaseMachine(getAdapterPosition());
                                        } else {
                                            Util.showToast(activity.getString(R.string.msg_no_network), activity);
                                        }
                                        break;
                                    case R.id.action_machine_worklog:
                                        if (Util.isConnected(activity)) {
                                            Intent startMain1 = new Intent(activity, MachineWorkingDataListActivity.class);
                                            startMain1.putExtra("machineId",ssDataList.get(getAdapterPosition()).getId());
                                            startMain1.putExtra("machineName",ssDataList.get(getAdapterPosition()).getMachineCode());

                                            activity.startActivity(startMain1);
                                        } else {
                                            Util.showToast(activity.getString(R.string.msg_no_network), activity);
                                        }
                                        break;

                                }
                            } else {
                                Util.showToast(activity.getString(R.string.msg_no_network), activity);
                            }
                            return false;
                        }
                    });
                }
            });
//            rlMachine = itemView.findViewById(R.id.rl_machine);
//            rlMachine.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });
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
                                    getId());
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
                                    getId());
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
    }

    @Override
    public int getItemCount() {
        return ssDataList.size();
    }
}
