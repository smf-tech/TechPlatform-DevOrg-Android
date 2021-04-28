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
        this.isDownloadMOU = isDownloadMOU ;
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
        if(list.get(position).getStatus().equalsIgnoreCase("approved")){
            holder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.dark_green));
        } else {
            holder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        holder.tvStatus.setText(list.get(position).getStatus());
        holder.tvOwner.setText(list.get(position).getOwnerName());
        holder.tvState.setText(list.get(position).getStateName());
        holder.tvMachineRequired.setText(list.get(position).getRequiredMachine().toString());
        holder.tvContact.setText(list.get(position).getOwnerContactDetails());
        holder.tvDistrict.setText(list.get(position).getDistrictName());
        holder.tvInCharge.setText(list.get(position).getUserName());
        if(isApprovalAllowed){
            if(list.get(position).getStatus().equalsIgnoreCase("Pending")){
                holder.tvViewDetails.setVisibility(View.VISIBLE);
                holder.tvViewDetails.setText("View Details");
            }
        } else if(isSubmitMOU){
            if(list.get(position).getStatus().equalsIgnoreCase("Approved")) {
                holder.tvViewDetails.setVisibility(View.VISIBLE);
                holder.tvViewDetails.setText("Waiting for MOU");
            }
        }  else if(isDownloadMOU){
            if(list.get(position).isMOUDone()) {
                holder.tvViewDetails.setVisibility(View.VISIBLE);
                holder.tvViewDetails.setText("Download MOU");
            }
            holder.tvViewDetails.setVisibility(View.VISIBLE);
        } else {
            holder.tvViewDetails.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHospitalName, tvStatus, tvOwner, tvState, tvMachineRequired, tvContact, tvDistrict,
                tvInCharge, tvViewDetails;
        RelativeLayout lyMain;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHospitalName = itemView.findViewById(R.id.tvHospitalName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvOwner = itemView.findViewById(R.id.tvOwner);
            tvState = itemView.findViewById(R.id.tvState);
            tvMachineRequired = itemView.findViewById(R.id.tvMachineRequired);
            tvContact = itemView.findViewById(R.id.tvContact);
            tvDistrict = itemView.findViewById(R.id.tvDistrict);
            tvInCharge = itemView.findViewById(R.id.tvInCharge);
            tvViewDetails = itemView.findViewById(R.id.tvViewDetails);
            lyMain = itemView.findViewById(R.id.lyMain);

            tvContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+list.get(getAdapterPosition()).getOwnerContactDetails()));
                    mContext.startActivity(intent);
                }
            });
            tvViewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(list.get(getAdapterPosition()).getStatus().equalsIgnoreCase("Pending")){
                        Intent intent = new Intent(mContext, ConcentratorApprovalActivity.class);
                        intent.putExtra("position", getAdapterPosition());
                        intent.putExtra("RequestId", list.get(getAdapterPosition()).getId());
//                        mContext.startActivity(intent);
                        mContext.startActivityForResult(intent, 1001);
                    } else if(list.get(getAdapterPosition()).getStatus().equalsIgnoreCase("Approved")) {
                        Intent intent = new Intent(mContext, OxyMachineMouActivity.class);
                        intent.putExtra("position", getAdapterPosition());
                        intent.putExtra("RequestId", list.get(getAdapterPosition()).getId());
                        mContext.startActivity(intent);
                    } else if(list.get(getAdapterPosition()).isMOUDone()){
                        mContext.showEmailDialog("Send MOU on this Email",getAdapterPosition());
                    }
                }
            });

        }
    }
}
