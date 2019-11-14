package com.platform.view.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.platform.R;
import com.platform.models.SujalamSuphalam.MachineData;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.MachineMouActivity;
import com.platform.view.activities.SSActionsActivity;
import com.platform.view.fragments.StructureMachineListFragment;

import java.util.ArrayList;

import static com.platform.utility.Constants.DAY_MONTH_YEAR;

public class SSMachineListAdapter extends RecyclerView.Adapter<SSMachineListAdapter.ViewHolder> {
    private ArrayList<MachineData> ssDataList;
    private Activity activity;
    private StructureMachineListFragment fragment;
    private boolean isSettingsRequired;

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
        holder.tvCapacity.setText(machineData.getDiselTankCapacity());
        holder.tvProvider.setText(machineData.getProviderName());
        holder.tvMachineModel.setText(machineData.getMakeModel());
        holder.tvContact.setText(machineData.getProviderContactNumber());
        holder.tvLastUpdatedTime.setText(machineData.getLastUpdatedTime());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatus, tvMachineCode, tvCapacity, tvProvider, tvMachineModel, tvContact, tvLastUpdatedTime;
        ImageView btnPopupMenu;
        RelativeLayout rlMachine;
        PopupMenu popup;

        ViewHolder(View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvMachineCode = itemView.findViewById(R.id.tv_machine_code);
            tvCapacity = itemView.findViewById(R.id.tv_capacity);
            tvProvider = itemView.findViewById(R.id.tv_provider);
            tvMachineModel = itemView.findViewById(R.id.tv_machine_model);
            tvContact = itemView.findViewById(R.id.tv_contact);
            tvLastUpdatedTime = itemView.findViewById(R.id.tv_last_updated_time);
            btnPopupMenu = itemView.findViewById(R.id.btn_popmenu);

            if(fragment.isSiltTransportForm) {
                isSettingsRequired = true;
            } else if(fragment.isDieselRecordForm) {
                isSettingsRequired = true;
            } else if(fragment.isMachineVisitValidationForm) {
                isSettingsRequired = true;
            } else if(fragment.isMachineShiftForm) {
                isSettingsRequired = true;
            } else if(fragment.isMachineRelease) {
                isSettingsRequired = true;
            }
            if(isSettingsRequired) {
                btnPopupMenu.setVisibility(View.VISIBLE);
            } else {
                btnPopupMenu.setVisibility(View.INVISIBLE);
            }
            btnPopupMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup = new PopupMenu((activity), v);
                    popup.inflate(R.menu.machine_forms_menu);
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
                            switch (item.getItemId()) {
                                case R.id.action_machine_shifting:
                                    Intent intent = new Intent(activity, SSActionsActivity.class);
                                    intent.putExtra("SwitchToFragment", "MachineDeployStructureListFragment");
                                    intent.putExtra("title", "Select Structure");
                                    intent.putExtra("type", "shiftMachine");
                                    intent.putExtra("machineId", ssDataList.get(getAdapterPosition()).getId());
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
                                    dieselRecordIntent.putExtra("currentStructureId", ssDataList.get
                                            (getAdapterPosition()).getDeployedStrutureId());
                                    activity.startActivity(dieselRecordIntent);
                                    break;
                                case R.id.action_machine_non_utilization:
                                    Intent machineNonUtilizationIntent = new Intent(activity, SSActionsActivity.class);
                                    machineNonUtilizationIntent.putExtra("SwitchToFragment", "MachineNonUtilizationFragment");
                                    machineNonUtilizationIntent.putExtra("title", "Machine Non-utilization Record");
                                    machineNonUtilizationIntent.putExtra("type", "machineNonUtilization");
                                    machineNonUtilizationIntent.putExtra("machineId", ssDataList.get(getAdapterPosition()).getId());
                                    machineNonUtilizationIntent.putExtra("currentStructureId", ssDataList.get
                                            (getAdapterPosition()).getDeployedStrutureId());
                                    activity.startActivity(machineNonUtilizationIntent);
                                    break;
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
                                    fragment.releaseMachine(getAdapterPosition());
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
                    if (ssDataList.get(getAdapterPosition()).getStatusCode() == Constants.SSModule.
                            MACHINE_ELIGIBLE_STATUS_CODE ||
                            ssDataList.get(getAdapterPosition()).getStatusCode() == Constants.SSModule.
                                    MACHINE_NEW_STATUS_CODE) {
                        Intent mouIntent = new Intent(activity, MachineMouActivity.class);
                        mouIntent.putExtra("SwitchToFragment", "MachineMouFirstFragment");
                        mouIntent.putExtra("machineId", ssDataList.get(getAdapterPosition()).
                                getId());
                        if (ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                Constants.SSModule.MACHINE_ELIGIBLE_STATUS_CODE) {
                            mouIntent.putExtra("statusCode", Constants.SSModule.MACHINE_ELIGIBLE_STATUS_CODE);
                        } else {
                            mouIntent.putExtra("statusCode", Constants.SSModule.MACHINE_NEW_STATUS_CODE);
                        }
                        activity.startActivity(mouIntent);
                    } else if (ssDataList.get(getAdapterPosition()).getStatusCode() ==
                            Constants.SSModule.MACHINE_NON_ELIGIBLE_STATUS_CODE) {
                        Intent mouIntent = new Intent(activity, MachineMouActivity.class);
                        mouIntent.putExtra("SwitchToFragment", "MachineMouFirstFragment");
                        mouIntent.putExtra("machineId", ssDataList.get(getAdapterPosition()).
                                getId());
                        mouIntent.putExtra("statusCode", Constants.SSModule.MACHINE_NON_ELIGIBLE_STATUS_CODE);
                        activity.startActivity(mouIntent);
                    } else if (ssDataList.get(getAdapterPosition()).getStatusCode() ==
                            Constants.SSModule.MACHINE_MOU_DONE_STATUS_CODE ||
                            ssDataList.get(getAdapterPosition()).getStatusCode() ==
                                    Constants.SSModule.MACHINE_REALEASED_STATUS_CODE) {
                        fragment.takeMouDoneAction(getAdapterPosition());
                    } else if (ssDataList.get(getAdapterPosition()).getStatusCode() ==
                            Constants.SSModule.MACHINE_AVAILABLE_STATUS_CODE) {
                        if (fragment.isMachineDepoly) {
                            Intent intent = new Intent(activity, SSActionsActivity.class);
                            intent.putExtra("SwitchToFragment", "MachineDeployStructureListFragment");
                            intent.putExtra("type", "deployMachine");
                            intent.putExtra("title", "Select Structure");
                            intent.putExtra("machineId", ssDataList.get(getAdapterPosition()).getId());
                            activity.startActivity(intent);
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
