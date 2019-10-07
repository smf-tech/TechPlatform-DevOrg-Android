package com.platform.view.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.models.SujalamSuphalam.StructureData;
import com.platform.view.fragments.MachineDeployStructureListFragment;

import java.util.ArrayList;

public class StructureListAdapter extends RecyclerView.Adapter<StructureListAdapter.ViewHolder> {
    private ArrayList<StructureData> ssStructureList;
    Activity activity;
    MachineDeployStructureListFragment fragment;

    public StructureListAdapter(Activity activity, MachineDeployStructureListFragment fragment, ArrayList<StructureData> ssDataList){
        this.ssStructureList = ssDataList;
        this.activity = activity;
        this.fragment = fragment;
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
        holder.tvWorkType.setText(structureData.getStructureWorkType());
        holder.tvStructureName.setText(structureData.getStructureName());
        holder.tvDepartment.setText(structureData.getStructureDepartmentName());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatus, tvReason, tvStructureCode, tvType, tvWorkType,
                tvStructureName, tvStructureDepartment,tvDepartment;
        RelativeLayout rlStructure;
        ViewHolder(View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvStructureCode = itemView.findViewById(R.id.tv_structure_code);
            tvType = itemView.findViewById(R.id.tv_type);
            tvWorkType = itemView.findViewById(R.id.tv_work_type);
            tvStructureName = itemView.findViewById(R.id.tv_structure_name);
            tvStructureDepartment = itemView.findViewById(R.id.tv_structure_department);
            tvDepartment = itemView.findViewById(R.id.tv_department);
            rlStructure = itemView.findViewById(R.id.rl_structure);
            rlStructure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragment.deployMachine(getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return ssStructureList.size();
    }
}
