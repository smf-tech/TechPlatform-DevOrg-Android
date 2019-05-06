package com.platform.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.models.events.TaskForm;
import com.platform.utility.Constants;

import java.util.ArrayList;

@SuppressWarnings("CanBeFinal")
public class TaskFormsListAdapter extends RecyclerView.Adapter<TaskFormsListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<TaskForm> taskFormsList;

    public TaskFormsListAdapter(Context mContext, ArrayList<TaskForm> taskFormsList) {
        this.mContext = mContext;
        this.taskFormsList = taskFormsList;
    }

    @NonNull
    @Override
    public TaskFormsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_form_item, parent, false);
        return new TaskFormsListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskFormsListAdapter.ViewHolder holder, int position) {
        TaskForm taskForm = taskFormsList.get(position);
        holder.tvFormTitle.setText(taskForm.getTitle());
        if (taskForm.getStatus().equalsIgnoreCase(Constants.Planner.PLANNED_STATUS)) {
            holder.vFormStatusIndicator.setBackgroundColor(mContext.getResources().getColor(R.color.red));
        } else if (taskForm.getStatus().equalsIgnoreCase(Constants.Planner.COMPLETED_STATUS)) {
            holder.vFormStatusIndicator.setBackgroundColor(mContext.getResources().getColor(R.color.green));
        }
    }

    @Override
    public int getItemCount() {
        return this.taskFormsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFormTitle;
        View vFormStatusIndicator;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvFormTitle = itemView.findViewById(R.id.tv_form_title);
            vFormStatusIndicator = itemView.findViewById(R.id.form_status_indicator);
        }
    }
}
