package com.octopus.view.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octopus.R;
import com.octopus.models.Matrimony.MatrimonyUserDetails;
import com.octopus.utility.Permissions;

import java.util.ArrayList;

public class MeetContactsListAdapter extends RecyclerView.Adapter<MeetContactsListAdapter.ViewHolder> {

    private ArrayList<MatrimonyUserDetails> usersList;
    private Activity activity;

    public MeetContactsListAdapter(final ArrayList<MatrimonyUserDetails> usersList, Activity activity) {
        this.usersList = usersList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_meet_contacts,
                parent, false);
        return new MeetContactsListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(usersList.get(position).getName());
        holder.tvContact.setText(usersList.get(position).getPhone());
        holder.tvMailId.setText(usersList.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvContact, tvMailId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvContact = itemView.findViewById(R.id.tv_contact);
            tvContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Permissions.isCallPermission(activity, this)) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + tvContact.getText().toString()));
                        activity.startActivity(callIntent);
                    }
                }
            });
            tvMailId = itemView.findViewById(R.id.tv_mail_id);
        }
    }
}
