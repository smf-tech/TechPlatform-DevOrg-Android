package com.octopusbjsindia.view.adapters.ssgp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.ssgp.StructureListData;

import java.util.ArrayList;
import java.util.Objects;

public class GPStructureListAdapter extends RecyclerView.Adapter<GPStructureListAdapter.ViewHolder> {

    final String STRUCTURE_DATA = "StructureData";
    final String STRUCTURE_STATUS = "StructureStatus";

    private ArrayList<StructureListData> ssDataList;
    Activity activity;
//    boolean isSaveOfflineStructure, isStructurePreparation, isCommunityMobilization,
//            isVisitMonitoring, isStructureComplete, isStructureClose, isStructureBoundary;

    public GPStructureListAdapter(FragmentActivity activity, ArrayList<StructureListData> ssStructureListData) {
        this.ssDataList = ssStructureListData;
        this.activity = activity;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gp_structure_item_layout,
                parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvStatus.setText(ssDataList.get(position).getStructureStatus());


        holder.tvStructureCode.setText(ssDataList.get(position).getStructureCode());
        holder.tvStructureType.setText(ssDataList.get(position).getStructureType());
        holder.tvInterventionType.setText(ssDataList.get(position).getStructureinterventionType());
        holder.tvMachineCount.setText(""+ssDataList.get(position).getMachineCnt());
        holder.tvTaluka.setText(ssDataList.get(position).getTaluka());
        holder.tvVillage.setText(ssDataList.get(position).getVillage());

    }

    @Override
    public int getItemCount() {
        return ssDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvStatus, tvStructureCode, tvStructureType, tvInterventionType,
                tvMachinCount, tvTaluka, tvVillage, tvMachineCount;
        ImageView btnPopupMenu;
        LinearLayout lyStartData;
        PopupMenu popup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lyStartData = itemView.findViewById(R.id.ly_start_data);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvStructureCode = itemView.findViewById(R.id.tv_structure_code);
            tvStructureType = itemView.findViewById(R.id.tv_structure_type);
            tvInterventionType = itemView.findViewById(R.id.tv_intervention_type);
//            tvStructureName = itemView.findViewById(R.id.tv_structure_name);
            tvMachinCount = itemView.findViewById(R.id.tv_machin_count);
            tvTaluka = itemView.findViewById(R.id.tv_taluka);
            tvVillage = itemView.findViewById(R.id.tv_village);
            tvMachineCount = itemView.findViewById(R.id.tv_machine_count);

        }
    }
    public void showDialog(Context context, String dialogTitle, String message, String btn1String, String
            btn2String, int adapterPosition, int flag) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(context));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogs_leave_layout);

        if (!TextUtils.isEmpty(dialogTitle)) {
            TextView title = dialog.findViewById(R.id.tv_dialog_title);
            title.setText(dialogTitle);
            title.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(message)) {
            TextView text = dialog.findViewById(R.id.tv_dialog_subtext);
            text.setText(message);
            text.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(btn1String)) {
            Button button = dialog.findViewById(R.id.btn_dialog);
            button.setText(btn1String);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(v -> {

                //Close dialog
                dialog.dismiss();
            });
        }

        if (!TextUtils.isEmpty(btn2String)) {
            Button button1 = dialog.findViewById(R.id.btn_dialog_1);
            button1.setText(btn2String);
            button1.setVisibility(View.VISIBLE);
            button1.setOnClickListener(v -> {
                //Close dialog
                dialog.dismiss();
            });
        }

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

}
