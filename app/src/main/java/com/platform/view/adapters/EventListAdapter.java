package com.platform.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.events.Event;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<Event> eventsList;

    public EventListAdapter(Context mContext, ArrayList<Event> eventsList) {
        this.mContext = mContext;
        this.eventsList = eventsList;
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

    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvEventTitle;
        TextView tvEventTime;
        TextView tvEventAddress;
        TextView tvEventOwner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvEventTitle = itemView.findViewById(R.id.tv_event_title);
            tvEventTime = itemView.findViewById(R.id.tv_event_time);
            tvEventAddress = itemView.findViewById(R.id.tv_event_address);
            tvEventOwner = itemView.findViewById(R.id.tv_event_owner);

        }
    }
}
