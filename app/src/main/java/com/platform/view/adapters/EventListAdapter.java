package com.platform.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.events.Event;
import com.platform.utility.Constants;
import com.platform.view.activities.AddMemberFilerActivity;
import com.platform.view.activities.EventDetailActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<Event> eventsList;
    private String type;

    public EventListAdapter(Context mContext, ArrayList<Event> eventsList, String type) {
        this.mContext = mContext;
        this.eventsList = eventsList;
        this.type = type;
    }

    @NonNull
    @Override
    public EventListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_each_event, parent, false);
        return new EventListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventListAdapter.ViewHolder holder, int position) {

        Event event = eventsList.get(position);
        holder.tvEventTitle.setText(event.getTital());
        holder.tvEventTime.setText(event.getStarTime());
        holder.tvEventAddress.setText(event.getAddress());
        holder.tvEventOwner.setText(event.getTital());
        if(type.equalsIgnoreCase(Constants.Planner.TASKS_LABEL)){

        }
        holder.lyEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEventDetailActivity = new Intent(mContext, EventDetailActivity.class);
                intentEventDetailActivity.putExtra(Constants.Planner.EVENT_DETAIL,event);
                intentEventDetailActivity.putExtra(Constants.Planner.TO_OPEN,type);
                mContext.startActivity(intentEventDetailActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout lyEvent;
        TextView tvEventTitle;
        TextView tvEventTime;
        TextView tvEventAddress;
        TextView tvEventOwner;
        ImageView imgArrow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lyEvent = itemView.findViewById(R.id.ly_event);
            tvEventTitle = itemView.findViewById(R.id.tv_event_title);
            tvEventTime = itemView.findViewById(R.id.tv_event_time);
            tvEventAddress = itemView.findViewById(R.id.tv_event_address);
            tvEventOwner = itemView.findViewById(R.id.tv_event_owner);
            imgArrow = itemView.findViewById(R.id.iv_left_icon);

        }
    }
}
