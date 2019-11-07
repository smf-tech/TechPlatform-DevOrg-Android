package com.platform.view.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.models.SujalamSuphalam.StructureData;
import com.platform.view.activities.CommunityMobilizationActivity;
import com.platform.view.activities.StructurePripretionsActivity;
import com.platform.view.activities.StructureVisitMonitoringActivity;
import com.platform.view.fragments.StructureMachineListFragment;

import java.util.ArrayList;

public class SSStructureListAdapter extends RecyclerView.Adapter<SSStructureListAdapter.ViewHolder> {

    final String STRUCTURE_DATA = "StructureData";

    private ArrayList<StructureData> ssDataList;
    Activity activity;
    StructureMachineListFragment fragment;

    public SSStructureListAdapter(FragmentActivity activity, StructureMachineListFragment fragment,
                                  ArrayList<StructureData> ssStructureListData) {
        this.ssDataList = ssStructureListData;
        this.activity = activity;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public SSStructureListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ss_structure_item_layout,
                parent, false);
        return new SSStructureListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SSStructureListAdapter.ViewHolder holder, int position) {
        holder.tvStatus.setText(ssDataList.get(position).getStructureStatus());
        if (false) {
//            holder.tvReason.setText(ssDataList.get(position).get());
        } else {
            holder.tvReason.setVisibility(View.GONE);
        }
        holder.tvStructureCode.setText(ssDataList.get(position).getStructureCode());
        holder.tvStructureType.setText(ssDataList.get(position).getStructureType());
        holder.tvWorkType.setText(ssDataList.get(position).getStructureWorkType());
        holder.tvStructureName.setText(ssDataList.get(position).getStructureStatus());
        holder.tvStructureOwnerDepartment.setText(ssDataList.get(position).getStructureDepartmentName());
//        holder.tvContact.setText(ssDataList.get(position).get());
    }

    @Override
    public int getItemCount() {
        return ssDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvStatus, tvReason, tvStructureCode, tvStructureType, tvWorkType, tvStructureName,
                tvStructureOwnerDepartment, tvContact;
        ImageView btnPopupMenu;
        LinearLayout rlMachine;
        PopupMenu popup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvReason = itemView.findViewById(R.id.tv_reason);
            tvStructureCode = itemView.findViewById(R.id.tv_structure_code);
            tvStructureType = itemView.findViewById(R.id.tv_structure_type);
            tvWorkType = itemView.findViewById(R.id.tv_work_type);
            tvStructureName = itemView.findViewById(R.id.tv_structure_name);
            tvStructureOwnerDepartment = itemView.findViewById(R.id.tv_structure_owner_department);
            tvContact = itemView.findViewById(R.id.tv_contact);
            btnPopupMenu = itemView.findViewById(R.id.btn_popmenu);
            btnPopupMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup = new PopupMenu((activity), v);
                    popup.inflate(R.menu.structure_popup_menu);
                    popup.show();

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Intent intent;
                            switch (item.getItemId()) {
                                case R.id.action_mobilization:
                                    intent = new Intent(activity, CommunityMobilizationActivity.class);
                                    intent.putExtra(STRUCTURE_DATA, ssDataList.get(getAdapterPosition()));
                                    activity.startActivity(intent);
                                    break;
                                case R.id.action_visit_monitoring:
//                                    if(ssDataList.get(getAdapterPosition()).getStructureStatus().equalsIgnoreCase("Approved")){
                                    intent = new Intent(activity, StructureVisitMonitoringActivity.class);
                                    intent.putExtra(STRUCTURE_DATA, ssDataList.get(getAdapterPosition()));
                                    activity.startActivity(intent);
//                                    }
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
                    if (ssDataList.get(getAdapterPosition()).getStructureStatusCode()==115) {
                        Intent intent = new Intent(activity, StructurePripretionsActivity.class);
                        intent.putExtra(STRUCTURE_DATA, ssDataList.get(getAdapterPosition()));
                        activity.startActivity(intent);
                    }
                }
            });
        }
    }
}
