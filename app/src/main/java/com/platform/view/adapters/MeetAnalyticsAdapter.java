package com.platform.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.models.Matrimony.MeetAnalytics;

import java.util.ArrayList;

public class MeetAnalyticsAdapter extends RecyclerView.Adapter<MeetAnalyticsAdapter.ViewHolder>  {

    private ArrayList<MeetAnalytics> meetAnalyticsData = new ArrayList<>();
    private Context context;
    public MeetAnalyticsAdapter(Context context, ArrayList<MeetAnalytics> meetAnalyticsData){
        this.meetAnalyticsData = meetAnalyticsData;
        this.context = context;
    }
    @NonNull
    @Override
    public MeetAnalyticsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_meet_analytics,
                parent, false);
        return new MeetAnalyticsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetAnalyticsAdapter.ViewHolder holder, int position) {
        holder.tvTitle.setText(meetAnalyticsData.get(position).getDisplayLabel());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, true);
        holder.rvMeetModuleAnalytics.setLayoutManager(mLayoutManager);
        MeetModuleAnalyticsAdapter meetModuleAnalyticsAdapter = new MeetModuleAnalyticsAdapter(context, meetAnalyticsData.get(position).getDataModules());
        holder.rvMeetModuleAnalytics.setAdapter(meetModuleAnalyticsAdapter);
    }

    @Override
    public int getItemCount() {
        return meetAnalyticsData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        RecyclerView rvMeetModuleAnalytics;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_module_title);
            rvMeetModuleAnalytics = itemView.findViewById(R.id.rv_meet_module_analytics);
        }
    }
}
