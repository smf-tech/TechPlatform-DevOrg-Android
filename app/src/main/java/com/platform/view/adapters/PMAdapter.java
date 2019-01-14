package com.platform.view.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.pm.ProcessData;

import java.util.List;

public class PMAdapter extends RecyclerView.Adapter<PMAdapter.PMViewHolder> {

    private List<ProcessData> processDataList;
    private OnProcessListItemClickListener listener;

    public PMAdapter(List<ProcessData> processList, OnProcessListItemClickListener listener) {
        this.processDataList = processList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PMViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_each_process, parent, false);

        return new PMViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PMViewHolder holder, int i) {
        ProcessData processData = processDataList.get(i);
        holder.processName.setText(processData.getName());

        holder.processName.setOnClickListener(view ->
                listener.onProcessListItemClickListener(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return processDataList.size();
    }

    class PMViewHolder extends RecyclerView.ViewHolder {

        TextView processName;

        PMViewHolder(View view) {
            super(view);
            processName = view.findViewById(R.id.pm_process_name_text);
        }
    }

    public interface OnProcessListItemClickListener {
        void onProcessListItemClickListener(int position);
    }
}
