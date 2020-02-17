package com.octopusbjsindia.view.adapters;

import android.app.ActionBar;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.profile.MultyProjectData;
import com.octopusbjsindia.presenter.ProfileActivityPresenter;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.ProfileActivity;

import java.util.ArrayList;
import java.util.Objects;

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
        TextView tvOrganization, tvProject, tvRole;
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

                    if (projectList.size() > 1) {

                        if (Util.getUserObjectFromPref().getProjectIds().get(0).getId()
                                .equals(projectList.get(getAdapterPosition()).getProjectId())) {
                            showDialog("Switch Profile",
                                    "You are in the same project?",
                                    "",
                                    mContext.getResources().getString(R.string.ok),
                                    getAdapterPosition(), 2);
                        } else {
                            showDialog("Switch Profile",
                                    "Are you sure you want to switch your project ?",
                                    mContext.getResources().getString(R.string.yes),
                                    mContext.getResources().getString(R.string.no),
                                    getAdapterPosition(), 1);
                        }

                    } else {
                        showDialog("Switch Profile",
                                "You are in the same project?",
                                "",
                                mContext.getResources().getString(R.string.ok),
                                getAdapterPosition(), 2);
                    }
                }
            });
        }
    }

    public void showDialog(String dialogTitle, String message, String btn1String, String
            btn2String, int adapterPosition, int flag) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(mContext));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogs_leave_layout);

        if (!TextUtils.isEmpty(dialogTitle)) {
            TextView title = dialog.findViewById(R.id.tv_dialog_title);
            title.setText(dialogTitle);
            title.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(message)) {
            TextView text = dialog.findViewById(R.id.tv_dialog_subtext);
            text.setText(message);
            text.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(btn1String)) {
            Button button = dialog.findViewById(R.id.btn_dialog);
            button.setText(btn1String);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(v -> {
                if (flag == 1) {
                    presenter.getUserDetels(projectList.get(adapterPosition));
                }

                //Close dialog
                dialog.dismiss();
            });
        }

        if (!TextUtils.isEmpty(btn2String)) {
            Button button1 = dialog.findViewById(R.id.btn_dialog_1);
            button1.setText(btn2String);
            button1.setVisibility(View.VISIBLE);
            button1.setOnClickListener(v -> {
                //Close dialog
                dialog.dismiss();
            });
        }

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

}
