package com.platform.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.events.Event;
import com.platform.models.events.Member;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AddMembersListAdapter extends RecyclerView.Adapter<AddMembersListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Member> membersList;

    public AddMembersListAdapter(Context mContext, ArrayList<Member> membersList) {
        this.mContext = mContext;
        this.membersList = membersList;
    }

    @NonNull
    @Override
    public AddMembersListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_each_add_member_list, parent, false);
        return new AddMembersListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AddMembersListAdapter.ViewHolder holder, int position) {
        Member member = membersList.get(position);
        holder.tvMemberName.setText(member.getName());
        holder.tvMemberDesignation.setText(member.getRole());
        holder.cbMemberSelect.setChecked(membersList.get(position).getMemberSelected());

        if (membersList.get(position).getMemberSelected()) {
            holder.cbMemberSelect.setChecked(true);
        } else {
            holder.cbMemberSelect.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return membersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvMemberName;
        TextView tvMemberDesignation;
        CheckBox cbMemberSelect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMemberName = itemView.findViewById(R.id.tv_member_name);
            tvMemberDesignation = itemView.findViewById(R.id.tv_member_designation);
            cbMemberSelect = itemView.findViewById(R.id.cb_select_member);

            cbMemberSelect.setOnClickListener(v -> {
                if (membersList.size() > getAdapterPosition()) {
                    if (((CheckBox) v).isChecked()) {
                        membersList.get(getAdapterPosition()).setMemberSelected(true);
                    } else {
                        membersList.get(getAdapterPosition()).setMemberSelected(false);
                    }
                }
            });

        }
    }
}
