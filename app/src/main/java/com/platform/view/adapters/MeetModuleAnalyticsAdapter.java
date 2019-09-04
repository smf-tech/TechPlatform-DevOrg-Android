package com.platform.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.models.Matrimony.MeetAnalyticsDataModule;
import com.platform.models.Matrimony.MeetModuleAnalytics;

import java.util.ArrayList;

public class MeetModuleAnalyticsAdapter extends RecyclerView.Adapter<MeetModuleAnalyticsAdapter.ViewHolder>   {

    private ArrayList<MeetModuleAnalytics> meetModuleAnalyticsData = new ArrayList<>();
    private Context context;
    public MeetModuleAnalyticsAdapter(Context context, ArrayList<MeetModuleAnalytics> meetModuleAnalyticsData){
        this.meetModuleAnalyticsData = meetModuleAnalyticsData;
        this.context = context;
    }

    @NonNull
    @Override
    public MeetModuleAnalyticsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_meet_moule_analytics,
                parent, false);
        return new MeetModuleAnalyticsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetModuleAnalyticsAdapter.ViewHolder holder, int position) {
        //holder.tvTitle.setText(meetAnalyticsData.get(position).getDisplayLabel());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //TextView tvTitle;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
//            tvTitle = itemView.findViewById(R.id.tv_module_title);
        }
    }
}
