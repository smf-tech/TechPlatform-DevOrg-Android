package com.platform.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.models.Matrimony.MatrimonyUserDetails;

import java.util.ArrayList;
import java.util.List;

public class MeetContactsListAdapter extends RecyclerView.Adapter<MeetContactsListAdapter.ViewHolder> {

    private ArrayList<MatrimonyUserDetails> usersList;
    public MeetContactsListAdapter(final ArrayList<MatrimonyUserDetails> usersList){
        this.usersList = usersList;
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

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvContact, tvMailId;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvContact = itemView.findViewById(R.id.tv_contact);
            tvMailId = itemView.findViewById(R.id.tv_mail_id);
        }
    }
}
