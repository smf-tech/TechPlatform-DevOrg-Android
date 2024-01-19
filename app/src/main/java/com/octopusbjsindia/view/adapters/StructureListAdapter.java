package com.octopusbjsindia.view.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.SujalamSuphalam.StructureData;
import com.octopusbjsindia.view.fragments.MachineDeployStructureListFragment;

import java.util.ArrayList;

public class StructureListAdapter extends RecyclerView.Adapter<StructureListAdapter.ViewHolder> {
    private ArrayList<StructureData> ssStructureList;
    Activity activity;
    MachineDeployStructureListFragment fragment;
    String type;

    public StructureListAdapter(Activity activity, MachineDeployStructureListFragment fragment,
                                ArrayList<StructureData> ssDataList, String type){
        this.ssStructureList = ssDataList;
        this.activity = activity;
        this.fragment = fragment;
        this.type = type;
    }
    @NonNull
    @Override
    public StructureListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.structure_data_item_layout, parent, false);
        return new StructureListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StructureListAdapter.ViewHolder holder, int position) {
        StructureData structureData = ssStructureList.get(position);
        holder.tvStatus.setText(structureData.getStructureStatus());
        holder.tvStructureCode.setText(structureData.getStructureCode());
        holder.tvType.setText(structureData.getStructureType());
        //holder.tvWorkType.setText(structureData.getStructureWorkType());
        holder.tvStructureName.setText(structureData.getStructureName());
        holder.tvDepartment.setText(structureData.getStructureDepartmentName());
        holder.tvLastUpdatedTime.setText(structureData.getUpdatedDate());
        if(structureData.isSelectedForDeployment()) {
            holder.rlStructure.setStrokeWidth(8);
            holder.rlStructure.setStrokeColor(ResourcesCompat.getColor(activity.getResources(),
                    R.color.colorPrimary,null));
           /* holder.rlStructure.setCardBackgroundColor(ResourcesCompat.getColor(activity.getResources(),
                    R.color.color_primary_level3,null));*/
        } else {
            holder.rlStructure.setStrokeWidth(0);
           /* holder.rlStructure.setCardBackgroundColor(ResourcesCompat.getColor(activity.getResources(),
                    R.color.white,null));*/
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatus, tvReason, tvStructureCode, tvType, tvWorkType,
                tvStructureName, tvStructureDepartment,tvDepartment, tvLastUpdatedTime;
        MaterialCardView rlStructure;
        ViewHolder(View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvStructureCode = itemView.findViewById(R.id.tv_structure_code);
            tvType = itemView.findViewById(R.id.tv_type);
            tvWorkType = itemView.findViewById(R.id.tv_work_type);
            tvStructureName = itemView.findViewById(R.id.tv_structure_name);
            tvStructureDepartment = itemView.findViewById(R.id.tv_structure_department);
            tvDepartment = itemView.findViewById(R.id.tv_department);
            tvLastUpdatedTime = itemView.findViewById(R.id.tv_last_updated_time);
            rlStructure = itemView.findViewById(R.id.rl_structure);
            rlStructure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(type.equalsIgnoreCase("deployMachine")) {
                        for (int i = 0; i<ssStructureList.size(); i++) {
                            if(i == getAdapterPosition()) {
                                ssStructureList.get(i).setSelectedForDeployment(true);
                            } else {
                                ssStructureList.get(i).setSelectedForDeployment(false);
                            }
                        }
                        fragment.deployMachine(getAdapterPosition());
                        notifyDataSetChanged();
                    } else if(type.equalsIgnoreCase("shiftMachine")) {
                        fragment.ShiftMachine(getAdapterPosition());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return ssStructureList.size();
    }
}
