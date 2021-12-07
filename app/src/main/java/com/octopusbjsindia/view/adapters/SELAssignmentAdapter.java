package com.octopusbjsindia.view.adapters;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.sel_content.SELAssignmentData;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.FormDisplayActivity;
import com.octopusbjsindia.view.activities.SELTrainingActivity;

import java.util.List;

public class SELAssignmentAdapter extends RecyclerView.Adapter<SELAssignmentAdapter.ViewHolder> {

    private List<com.octopusbjsindia.models.sel_content.SELAssignmentData> SELAssignmentData;
    private SELTrainingActivity mContext;

    public SELAssignmentAdapter(SELTrainingActivity context, final List<SELAssignmentData> SELAssignmentData) {
        mContext = context;
        this.SELAssignmentData = SELAssignmentData;
    }

    @NonNull
    @Override
    public SELAssignmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sel_assignment_row,
                parent, false);
        return new SELAssignmentAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SELAssignmentAdapter.ViewHolder holder, int position) {
        holder.tvTitle.setText(SELAssignmentData.get(position).getFormName());
        if(SELAssignmentData.get(position).isFormSubmitted()){
            holder.img_download.setVisibility(View.VISIBLE);
        } else {
            holder.img_download.setVisibility(View.GONE);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        RelativeLayout rlAssignmentRow;
        ImageView img_download;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
            rlAssignmentRow = itemView.findViewById(R.id.rl_assignment_row);
            img_download = itemView.findViewById(R.id.img_download);

            rlAssignmentRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (SELAssignmentData.get(getAdapterPosition()).getFormId() != null &&
                            !TextUtils.isEmpty(SELAssignmentData.get(getAdapterPosition()).getFormId())) {
                        mContext.displayForm(SELAssignmentData.get(getAdapterPosition()).getFormId());
                    } else {
                        Util.showToast(mContext, "Something went wrong. Please try again later.");
                    }
                }
            });
            img_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Permissions.isWriteExternalStoragePermission(mContext, mContext)) {
                        //mContext.setDownloadPosition(-1);
                        mContext.showDownloadPopup(SELAssignmentData.get(getAdapterPosition()).getContentUrl(), getAdapterPosition());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return SELAssignmentData.size();
    }
}
