package com.octopus.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octopus.R;
import com.octopus.models.Matrimony.MatrimonyUserDetails;

import java.util.List;

public class MeetOrganizersReferencesAdapter extends RecyclerView.Adapter<MeetOrganizersReferencesAdapter.ViewHolder>{
    private List<MatrimonyUserDetails> meetOrganizersReferencesList;

    public MeetOrganizersReferencesAdapter(final List<MatrimonyUserDetails> meetOrganizersReferencesList) {
        this.meetOrganizersReferencesList = meetOrganizersReferencesList;
    }
    @NonNull
    @Override
    public MeetOrganizersReferencesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_meet_organizer_reference_row,
                parent, false);
        return new MeetOrganizersReferencesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetOrganizersReferencesAdapter.ViewHolder holder, int position) {
        holder.tvUserName.setText(meetOrganizersReferencesList.get(position).getName());
        holder.tvUserRole.setText(meetOrganizersReferencesList.get(position).getRoleName());
        holder.tvUserMobile.setText(meetOrganizersReferencesList.get(position).getPhone());
        holder.tvUserEmail.setText(meetOrganizersReferencesList.get(position).getEmail());
    }

    class ViewHolder extends RecyclerView.ViewHolder{
         TextView tvUserName, tvUserRole, tvUserMobile, tvUserEmail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvUserRole = itemView.findViewById(R.id.tv_user_role);
            tvUserMobile = itemView.findViewById(R.id.tv_user_mobile);
            tvUserEmail = itemView.findViewById(R.id.tv_user_email);
        }
    }

    @Override
    public int getItemCount() {
        return meetOrganizersReferencesList.size();
    }
}
