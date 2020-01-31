package com.octopusbjsindia.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.profile.MultyProjectData;
import com.octopusbjsindia.presenter.ProfileActivityPresenter;
import com.octopusbjsindia.view.activities.ProfileActivity;

import java.util.ArrayList;

public class MultyProjectAdapter extends RecyclerView.Adapter<MultyProjectAdapter.ViewHolder> {

    ArrayList<MultyProjectData> projectList;
    ProfileActivity mContext;
    ProfileActivityPresenter presenter;

    public MultyProjectAdapter(ArrayList<MultyProjectData> projectList, ProfileActivity mContext, ProfileActivityPresenter presenter) {
        this.projectList = projectList;
        this.mContext = mContext;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_project,
                parent, false);
        return new MultyProjectAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvOrganization.setText(projectList.get(position).getOrgTitle());
        holder.tvProject.setText(projectList.get(position).getProjectTitle());
        holder.tvRole.setText(projectList.get(position).getRoleTitle());

    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrganization,tvProject,tvRole;
        LinearLayout lyMain;
        public ViewHolder(@NonNull View view) {
            super(view);
            tvOrganization = view.findViewById(R.id.tv_organization);
            tvProject = view.findViewById(R.id.tv_project);
            tvRole = view.findViewById(R.id.tv_role);
            lyMain = view.findViewById(R.id.ly_main);
            lyMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.getUserDetels(projectList.get(getAdapterPosition()));
                }
            });
        }
    }
}
