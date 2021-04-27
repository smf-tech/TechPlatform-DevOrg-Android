package com.octopusbjsindia.view.adapters.mission_rahat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.MissionRahat.SearchListData;
import com.octopusbjsindia.view.activities.MissionRahat.SearchListActivity;

import java.util.ArrayList;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {
    ArrayList<SearchListData> list;
    SearchListActivity context;

    public SearchListAdapter(ArrayList<SearchListData> list, SearchListActivity context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_itam,
                parent, false);
        return new SearchListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchListAdapter.ViewHolder holder, int position) {
        holder.tvValue.setText(list.get(position).getValue());
        holder.tvAddValue.setText(list.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvValue, tvAddValue;
        RelativeLayout lyMain;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvValue = itemView.findViewById(R.id.tvValue);
            tvAddValue = itemView.findViewById(R.id.tvAddValue);
            lyMain = itemView.findViewById(R.id.lyMain);
            lyMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.onSelected(getAdapterPosition());
                }
            });
        }
    }
}
