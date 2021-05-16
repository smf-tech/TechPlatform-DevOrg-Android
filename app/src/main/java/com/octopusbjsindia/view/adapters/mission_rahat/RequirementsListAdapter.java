package com.octopusbjsindia.view.adapters.mission_rahat;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.MissionRahat.RequirementsListData;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.MissionRahat.ConcentratorApprovalActivity;
import com.octopusbjsindia.view.activities.MissionRahat.OxyMachineMouActivity;
import com.octopusbjsindia.view.activities.MissionRahat.RequirementsListActivity;

import java.util.ArrayList;

public class RequirementsListAdapter extends RecyclerView.Adapter<RequirementsListAdapter.ViewHolder> {
    ArrayList<RequirementsListData> list;
    RequirementsListActivity mContext;
    boolean isDownloadMOU, isSubmitMOU, isApprovalAllowed;

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

        }
    }
}
