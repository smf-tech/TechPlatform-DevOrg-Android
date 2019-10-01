package com.platform.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.models.SujalamSuphalam.MachineData;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.MachineMouActivity;
import com.platform.view.activities.SSActionsActivity;
import com.platform.view.fragments.StructureMachineListFragment;

import java.util.ArrayList;

public class SSDataListAdapter extends RecyclerView.Adapter<SSDataListAdapter.ViewHolder>  {
    private ArrayList<MachineData> ssDataList;
    Activity activity;
    StructureMachineListFragment fragment;

    public SSDataListAdapter(Activity activity, StructureMachineListFragment fragment, ArrayList<MachineData> ssDataList){
        this.ssDataList = ssDataList;
        this.activity = activity;
        this.fragment = fragment;
        if(Util.getUserObjectFromPref().getRoleNames().equals("Operator")){
        }
    }
    @NonNull
    @Override
    public SSDataListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ss_data_item_layout, parent, false);
        return new SSDataListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SSDataListAdapter.ViewHolder holder, int position) {
        MachineData machineData = ssDataList.get(position);
        holder.tvStatus.setText(machineData.getStatus());
        holder.tvMachineCode.setText(machineData.getMachineCode());
        holder.tvCapacity.setText(machineData.getDiselTankCapacity());
        holder.tvProvider.setText(machineData.getProviderName());
        holder.tvMachineModel.setText(machineData.getMakeModel());
        holder.tvContact.setText(machineData.getProviderContactNumber());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatus, tvReason, tvMachineCode, tvCapacity, tvProvider, tvMachineModel, tvContact;
        Button btnDetails;
        RelativeLayout rlMachine;
        ViewHolder(View itemView){
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvMachineCode = itemView.findViewById(R.id.tv_machine_code);
            tvCapacity = itemView.findViewById(R.id.tv_capacity);
            tvProvider = itemView.findViewById(R.id.tv_provider);
            tvMachineModel = itemView.findViewById(R.id.tv_machine_model);
            tvContact = itemView.findViewById(R.id.tv_contact);
            rlMachine = itemView.findViewById(R.id.rl_machine);
//            btnDetails = itemView.findViewById(R.id.btn_details);
//            btnDetails.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(activity, SSActionsActivity.class);
//                    intent.putExtra("switch_fragments", "MachineDetailsFragment");
//                    intent.putExtra("machineId", ssDataList.get(getAdapterPosition()).getId());
//                    activity.startActivity(intent);
//                }
//            });
            rlMachine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    if(ssDataList.get(getAdapterPosition()).getStatus().equalsIgnoreCase("New") ) {
//                        Intent intent = new Intent(activity, SSActionsActivity.class);
//                        intent.putExtra("SwitchToFragment", "MachineDetailsFragment");
//                        intent.putExtra("title", "Machine Details");
//                        intent.putExtra("machineData", ssDataList.get(getAdapterPosition()));
//                        activity.startActivity(intent);
//                    }
                    if(ssDataList.get(getAdapterPosition()).getStatus().equalsIgnoreCase("Eligible") ||
                            ssDataList.get(getAdapterPosition()).getStatus().equalsIgnoreCase("New")){
                        Intent mouIntent = new Intent(activity, MachineMouActivity.class);
                        mouIntent.putExtra("SwitchToFragment", "MachineMouFirstFragment");
                        mouIntent.putExtra("machineId", ssDataList.get(getAdapterPosition()).getId());
                        mouIntent.putExtra("statusCode", Constants.SSModule.MACHINE_NEW_STATUS_CODE);
                        activity.startActivity(mouIntent);
                    } else if(ssDataList.get(getAdapterPosition()).getStatus().equalsIgnoreCase("MOU Done")){
                        fragment.takeMouDoneAction(getAdapterPosition());
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
