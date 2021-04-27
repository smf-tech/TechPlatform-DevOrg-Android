package com.octopusbjsindia.view.adapters.mission_rahat;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.MissionRahat.RequirementsListData;
import com.octopusbjsindia.view.activities.MissionRahat.ConcentratorApprovalActivity;
import com.octopusbjsindia.view.activities.MissionRahat.OxyMachineMouActivity;
import com.octopusbjsindia.view.activities.MissionRahat.RequirementsListActivity;

import java.util.ArrayList;

public class RequirementsListAdapter extends RecyclerView.Adapter<RequirementsListAdapter.ViewHolder> {
    ArrayList<RequirementsListData> list;
    RequirementsListActivity mContext;
    public RequirementsListAdapter(ArrayList<RequirementsListData> list, RequirementsListActivity context) {
        this.list = list;
        this.mContext = context;
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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHospitalName, tvStatus, tvOwner, tvState, tvMachineRequired, tvContact, tvDistrict, tvInCharge;
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

            lyMain = itemView.findViewById(R.id.lyMain);
            lyMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(list.get(getAdapterPosition()).getStatus().equalsIgnoreCase("pending")){
                        Intent intent = new Intent(mContext, ConcentratorApprovalActivity.class);
                        intent.putExtra("RequestId",list.get(getAdapterPosition()).getId());
                        mContext.startActivity(intent);
                    } else {
                        // Redirect To MOU
                        if(list.get(getAdapterPosition()).getStatus().equalsIgnoreCase("Approved")) {
                            Intent intent = new Intent(mContext, OxyMachineMouActivity.class);
                            intent.putExtra("RequestId", list.get(getAdapterPosition()).getId());
                            mContext.startActivity(intent);
                        }
                    }

                }
            });

        }
    }
}
