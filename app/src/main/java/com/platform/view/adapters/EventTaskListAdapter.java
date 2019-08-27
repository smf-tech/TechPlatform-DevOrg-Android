package com.platform.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.models.events.EventTask;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.EventDetailActivity;

import java.util.ArrayList;

@SuppressWarnings("CanBeFinal")
public class EventTaskListAdapter extends RecyclerView.Adapter<EventTaskListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<EventTask> eventsList;
    private String type;

    public EventTaskListAdapter(Context mContext, ArrayList<EventTask> eventsList, String type) {
        this.mContext = mContext;
        this.eventsList = eventsList;
        this.type = type;
    }

    @NonNull
    @Override
    public EventTaskListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_each_event, parent, false);
        return new EventTaskListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventTaskListAdapter.ViewHolder holder, int position) {

        EventTask eventTask = eventsList.get(position);
        holder.tvTitle.setText(eventTask.getTitle());
        holder.tvTime.setText(Util.getDateFromTimestamp(eventTask.getSchedule().getStartdatetime(),Constants.TIME_FORMAT));
        holder.tvDate.setText(Util.getDateFromTimestamp(eventTask.getSchedule().getStartdatetime(),Constants.EVENT_DATE_FORMAT));
        holder.tvOwner.setText(eventTask.getOwnername());
        holder.tvAddress.setText(eventTask.getAddress());
        holder.tvOwner.setText(eventTask.getOwnername());
        holder.tvStatus.setText(eventTask.getEventStatus());
        if(eventTask.getEventStatus().equalsIgnoreCase("Active")){
            holder.tvStatus.setBackground(mContext.getDrawable(R.drawable.bg_task_status_active));
        } else {
            holder.tvStatus.setBackground(mContext.getDrawable(R.drawable.bg_task_status_active));
        }
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout lyEventTask,lySchedule;
        TextView tvTitle;
        TextView tvTime,tvDate;
        TextView tvAddress;
        TextView tvOwner;
        TextView tvStatus;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            lyEventTask = itemView.findViewById(R.id.ly_event_task);
            lySchedule = itemView.findViewById(R.id.ly_schedule);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvOwner = itemView.findViewById(R.id.tv_owner);
            tvStatus = itemView.findViewById(R.id.tv_status);

            if (type.equalsIgnoreCase(Constants.Planner.TASKS_LABEL)) {
//                task
                lySchedule.setBackground(mContext.getDrawable(R.drawable.bg_circular_rect_pink));
                tvStatus.setVisibility(View.VISIBLE);
            } else {
//                event
                lySchedule.setBackground(mContext.getDrawable(R.drawable.bg_circular_rect_pink));
                tvOwner.setVisibility(View.VISIBLE);
            }

            lyEventTask.setOnClickListener(v -> {
                Intent intentEventDetailActivity = new Intent(mContext, EventDetailActivity.class);
                intentEventDetailActivity.putExtra(Constants.Planner.EVENT_DETAIL, eventsList.get(getAdapterPosition()));
                intentEventDetailActivity.putExtra(Constants.Planner.TO_OPEN, type);
                mContext.startActivity(intentEventDetailActivity);
            });
        }
    }
}
