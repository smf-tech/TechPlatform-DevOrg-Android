package com.octopusbjsindia.view.adapters.mission_rahat;

import android.content.Intent;
import android.net.Uri;
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

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.MissionRahat.RequirementsListData;
import com.octopusbjsindia.models.home.RoleAccessAPIResponse;
import com.octopusbjsindia.models.home.RoleAccessList;
import com.octopusbjsindia.models.home.RoleAccessObject;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.MissionRahat.ConcentratorApprovalActivity;
import com.octopusbjsindia.view.activities.MissionRahat.ConcentratorTakeOverActivity;
import com.octopusbjsindia.view.activities.MissionRahat.OxyMachineListActivity;
import com.octopusbjsindia.view.activities.MissionRahat.OxyMachineMouActivity;
import com.octopusbjsindia.view.activities.MissionRahat.RequirementsListActivity;

import java.util.ArrayList;
import java.util.List;

public class RequirementsListAdapter extends RecyclerView.Adapter<RequirementsListAdapter.ViewHolder> {
    ArrayList<RequirementsListData> list;
    RequirementsListActivity mContext;
    boolean isDownloadMOU, isSubmitMOU, isApprovalAllowed,isMenuVisible =false;


    public RequirementsListAdapter(ArrayList<RequirementsListData> list, RequirementsListActivity context,
                                   boolean isDownloadMOU, boolean isSubmitMOU, boolean isApprovalAllowed) {
        this.list = list;
        this.mContext = context;
        this.isDownloadMOU = isDownloadMOU;
        this.isSubmitMOU = isSubmitMOU;
        this.isApprovalAllowed = isApprovalAllowed;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_requirement,
                parent, false);
        return new RequirementsListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvHospitalName.setText(list.get(position).getHospitalName());
        if (list.get(position).getStatus().equalsIgnoreCase("approved")) {
            holder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.dark_green));
        } else {
            holder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        holder.tvStatus.setText(list.get(position).getStatus());
        holder.tvOwner.setText(list.get(position).getOwnerName());
        holder.tvState.setText(list.get(position).getStateName());
        holder.tvTaluka.setText(list.get(position).getTalukaName());
        holder.tvMachineRequired.setText(list.get(position).getRequiredMachine().toString());
        holder.tvContact.setText(list.get(position).getOwnerContactDetails());
        holder.tvDistrict.setText(list.get(position).getDistrictName());
        holder.tvInCharge.setText(list.get(position).getUserName());

        if (list.get(position).getStatus().equalsIgnoreCase("Pending")) {
            if (isApprovalAllowed) {
                holder.tvViewDetails.setVisibility(View.VISIBLE);
                holder.tvWaitingMOU.setVisibility(View.GONE);
                holder.tvDownloadMOU.setVisibility(View.GONE);
            } else {
                holder.tvViewDetails.setVisibility(View.GONE);
                holder.tvWaitingMOU.setVisibility(View.GONE);
                holder.tvDownloadMOU.setVisibility(View.GONE);
            }
        } else if (list.get(position).getStatus().equalsIgnoreCase("Approved") && list.get(position).isMOUDone()) {
            if (isDownloadMOU) {
                holder.tvViewDetails.setVisibility(View.GONE);
                holder.tvWaitingMOU.setVisibility(View.GONE);
                holder.tvDownloadMOU.setVisibility(View.VISIBLE);
            }
        } else if (list.get(position).getStatus().equalsIgnoreCase("Approved") && !list.get(position).isMOUDone()) {
            if (isSubmitMOU) {
                holder.tvViewDetails.setVisibility(View.GONE);
                holder.tvWaitingMOU.setVisibility(View.VISIBLE);
                holder.tvDownloadMOU.setVisibility(View.GONE);
            }
        } else {
            holder.tvViewDetails.setVisibility(View.GONE);
            holder.tvWaitingMOU.setVisibility(View.GONE);
            holder.tvDownloadMOU.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHospitalName, tvStatus, tvOwner, tvState, tvTaluka, tvMachineRequired, tvContact, tvDistrict,
                tvInCharge, tvViewDetails, tvWaitingMOU, tvDownloadMOU;
        RelativeLayout lyMain;
        ImageView btn_popmenu;
        PopupMenu popup;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHospitalName = itemView.findViewById(R.id.tvHospitalName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvOwner = itemView.findViewById(R.id.tvOwner);
            tvState = itemView.findViewById(R.id.tvState);
            tvTaluka = itemView.findViewById(R.id.tvTaluka);
            tvMachineRequired = itemView.findViewById(R.id.tvMachineRequired);
            tvContact = itemView.findViewById(R.id.tvContact);
            tvDistrict = itemView.findViewById(R.id.tvDistrict);
            tvInCharge = itemView.findViewById(R.id.tvInCharge);
            tvViewDetails = itemView.findViewById(R.id.tvViewDetails);
            tvWaitingMOU = itemView.findViewById(R.id.tvWaitingMOU);
            tvDownloadMOU = itemView.findViewById(R.id.tvDownloadMOU);
            lyMain = itemView.findViewById(R.id.lyMain);
             btn_popmenu = itemView.findViewById(R.id.btn_popmenu);

            tvContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + list.get(getAdapterPosition()).getOwnerContactDetails()));
                    mContext.startActivity(intent);
                }
            });
            tvViewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ConcentratorApprovalActivity.class);
                    intent.putExtra("position", getAdapterPosition());
                    intent.putExtra("RequestId", list.get(getAdapterPosition()).getId());
                    intent.putExtra("talukaId", list.get(getAdapterPosition()).getTaluka_id());
                    mContext.startActivityForResult(intent, 1001);
                }
            });
            tvWaitingMOU.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, OxyMachineMouActivity.class);
                    intent.putExtra("position", getAdapterPosition());
                    intent.putExtra("RequestId", list.get(getAdapterPosition()).getId());
                    mContext.startActivityForResult(intent, 1001);

                }
            });
            tvDownloadMOU.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.showEmailDialog("Send", getAdapterPosition());
                }
            });

            RoleAccessAPIResponse roleAccessAPIResponse = Util.getRoleAccessObjectFromPref();
            RoleAccessList roleAccessList = roleAccessAPIResponse.getData();
            if (roleAccessList != null) {
                List<RoleAccessObject> roleAccessObjectList = roleAccessList.getRoleAccess();
                for (RoleAccessObject roleAccessObject : roleAccessObjectList) {
                    if (roleAccessObject.getActionCode().equals(Constants.MissionRahat.ACCESS_CODE_MACHINE_TAKEOVER)) {
                        btn_popmenu.setVisibility(View.VISIBLE);
                    } else if (roleAccessObject.getActionCode().equals(Constants.MissionRahat.ACCESS_CODE_MACHINE_HANDOVER)) {
                        btn_popmenu.setVisibility(View.VISIBLE);
                    }
                }
            }





            btn_popmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup = new PopupMenu((mContext), v);
                    popup.inflate(R.menu.requirementlist_popup_menu);


                    RoleAccessAPIResponse roleAccessAPIResponse = Util.getRoleAccessObjectFromPref();
                    RoleAccessList roleAccessList = roleAccessAPIResponse.getData();
                    if (roleAccessList != null) {
                        List<RoleAccessObject> roleAccessObjectList = roleAccessList.getRoleAccess();
                        for (RoleAccessObject roleAccessObject : roleAccessObjectList) {
                            if (roleAccessObject.getActionCode().equals(Constants.MissionRahat.ACCESS_CODE_MACHINE_TAKEOVER)) {
                                popup.getMenu().findItem(R.id.action_takeover).setVisible(true);
                            } else if (roleAccessObject.getActionCode().equals(Constants.MissionRahat.ACCESS_CODE_MACHINE_HANDOVER)) {
                                popup.getMenu().findItem(R.id.action_handover).setVisible(true);
                            }
                        }
                    }


                    popup.show();
                    //------
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Intent intent;
                            switch (item.getItemId()) {
                                case R.id.action_takeover:
                                    if (list.get(getAdapterPosition()).isMOUDone()) {
                                        intent = new Intent(mContext, ConcentratorTakeOverActivity.class);
                                        intent.putExtra("actionType", Constants.MissionRahat.TAKEOVER);
                                        intent.putExtra("RequirementId", list.get(getAdapterPosition()).getId());
                                        mContext.startActivity(intent);
                                    }else {
                                        Util.showToast("Please complete MOU process first.",mContext);
                                    }
                                    break;
                                case R.id.action_handover:
                                    if (list.get(getAdapterPosition()).isMOUDone()) {
                                        if (list.get(getAdapterPosition()).isTakeOver()) {
                                            intent = new Intent(mContext, ConcentratorTakeOverActivity.class);
                                            intent.putExtra("actionType", Constants.MissionRahat.HANDOVER);
                                            intent.putExtra("RequirementId", list.get(getAdapterPosition()).getId());
                                            mContext.startActivity(intent);
                                        }else {
                                            Util.showToast("Take over process is not completed for this requirement.",mContext);
                                        }
                                    }else {
                                        Util.showToast("Please complete MOU process first.",mContext);
                                    }
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
}
