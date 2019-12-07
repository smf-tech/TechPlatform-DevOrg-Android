package com.octopusbjsindia.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.forms.FormStatusCountData;
import com.octopusbjsindia.utility.Constants;

import java.util.ArrayList;

public class FormsDashboardAdapter extends RecyclerView.Adapter<FormsDashboardAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<FormStatusCountData> formStatusCountDataList;

    public FormsDashboardAdapter(final Context context, final ArrayList<FormStatusCountData> formStatusCountDataList) {
        this.mContext = context;
        this.formStatusCountDataList = formStatusCountDataList;
    }

    @NonNull
    @Override
    public FormsDashboardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.form_dashboard_item_layout, parent, false);
        return new FormsDashboardAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FormsDashboardAdapter.ViewHolder holder, int position) {
        FormStatusCountData formStatusCountData = formStatusCountDataList.get(position);
        if(formStatusCountData.getType().equalsIgnoreCase(Constants.PM.PENDING_STATUS)){
            holder.imgIcon.setImageResource(R.drawable.ic_pending_icon_db);
        }else if(formStatusCountData.getType().equalsIgnoreCase(Constants.PM.APPROVED_STATUS)){
            holder.imgIcon.setImageResource(R.drawable.ic_approved_icon_db);
        }else if(formStatusCountData.getType().equalsIgnoreCase(Constants.PM.REJECTED_STATUS)){
            holder.imgIcon.setImageResource(R.drawable.ic_rejected_icon_db);
        }else if(formStatusCountData.getType().equalsIgnoreCase(Constants.PM.UNSYNC_STATUS)){
            holder.imgIcon.setImageResource(R.drawable.ic_unsync_icon_db);
        }else if(formStatusCountData.getType().equalsIgnoreCase(Constants.PM.SAVED_STATUS)){
            holder.imgIcon.setImageResource(R.drawable.ic_saved_icon_db);
        }
        holder.txtStatus.setText(formStatusCountData.getType());
        holder.txtCount.setText(String.valueOf(formStatusCountData.getCount()));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgIcon;
        TextView txtStatus;
        TextView txtCount;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgIcon = itemView.findViewById(R.id.img_status_icon);
            txtStatus = itemView.findViewById(R.id.text_status);
            txtCount = itemView.findViewById(R.id.text_status_count);

        }
    }

    @Override
    public int getItemCount() {
        return formStatusCountDataList.size();
    }
}
