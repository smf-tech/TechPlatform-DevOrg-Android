package com.platform.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.models.events.AddForm;
import com.platform.models.events.TaskForm;
import com.platform.utility.Constants;
import com.platform.view.activities.FormActivity;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CanBeFinal")
public class TaskFormsListAdapter extends RecyclerView.Adapter<TaskFormsListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<AddForm> taskFormsList;
    private String CurrentLang;

    public TaskFormsListAdapter(Context mContext, ArrayList<AddForm> taskFormsList,
                                String CurrentLang) {
        this.mContext = mContext;
        this.taskFormsList = taskFormsList;
        this.CurrentLang=CurrentLang;
    }

    @NonNull
    @Override
    public TaskFormsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_form_item, parent, false);
        return new TaskFormsListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskFormsListAdapter.ViewHolder holder, int position) {
        AddForm taskForm = taskFormsList.get(position);
        if (CurrentLang.equalsIgnoreCase("mr"))
            holder.tvFormTitle.setText(taskForm.getName().getMr());
        else if (CurrentLang.equalsIgnoreCase("hi"))
            holder.tvFormTitle.setText(taskForm.getName().getHi());
        else
            holder.tvFormTitle.setText(taskForm.getName().getDefault());

//        if (taskForm.getStatus().equalsIgnoreCase(Constants.Planner.PLANNED_STATUS)) {
//            holder.vFormStatusIndicator.setBackgroundColor(mContext.getResources().getColor(R.color.red));
//        } else if (taskForm.getStatus().equalsIgnoreCase(Constants.Planner.COMPLETED_STATUS)) {
//            holder.vFormStatusIndicator.setBackgroundColor(mContext.getResources().getColor(R.color.green));
//        }
    }

    @Override
    public int getItemCount() {
        return this.taskFormsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFormTitle;
        View vFormStatusIndicator;
        LinearLayout lyMain;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvFormTitle = itemView.findViewById(R.id.tv_form_title);
            vFormStatusIndicator = itemView.findViewById(R.id.form_status_indicator);
            lyMain = itemView.findViewById(R.id.ly_main);
            lyMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskFormsList.get(getAdapterPosition()).getId();
                    Intent intent = new Intent(mContext, FormActivity.class);
                    intent.putExtra(Constants.PM.FORM_ID, taskFormsList.get(getAdapterPosition()).getId());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
