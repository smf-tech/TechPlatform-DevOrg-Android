package com.platform.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.models.events.Participant;
import com.platform.view.activities.AddMembersListActivity;

import java.util.ArrayList;

@SuppressWarnings("CanBeFinal")
public class AddMembersListAdapter extends RecyclerView.Adapter<AddMembersListAdapter.ViewHolder> {

    private Context mContext;
    private boolean isCheckVisible;
    private boolean isDeleteVisible;
    private ArrayList<Participant> membersList;

    public AddMembersListAdapter(Context mContext, ArrayList<Participant> membersList, boolean isDeleteVisible,
                                 boolean isCheckVisible) {
        this.mContext = mContext;
        this.membersList = membersList;
        this.isDeleteVisible = isDeleteVisible;
        this.isCheckVisible = isCheckVisible;
    }

    @NonNull
    @Override
    public AddMembersListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_each_add_member_list,
                parent, false);
        return new AddMembersListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AddMembersListAdapter.ViewHolder holder, int position) {
        Participant member = membersList.get(position);
        holder.tvMemberName.setText(member.getName());
        holder.tvMemberDesignation.setText(member.getRoleName());
        holder.cbMemberSelect.setChecked(member.isMemberSelected());

        if( member.isAttendedCompleted()){
            holder.lyMain.setBackgroundColor(mContext.getResources().getColor(R.color.green));
        } /*else {
           holder.lyMain.setBackgroundColor(mContext.getResources().getColor(R.color.green));
         }*/

    }

    @Override
    public int getItemCount() {
        return membersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //        RelativeLayout lyMain;
        TextView tvMemberName;
        TextView tvMemberDesignation;
        CheckBox cbMemberSelect;
        RelativeLayout lyMain;
        ImageView ivDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMemberName = itemView.findViewById(R.id.tv_member_name);
            tvMemberDesignation = itemView.findViewById(R.id.tv_member_designation);
            cbMemberSelect = itemView.findViewById(R.id.cb_select_member);
            lyMain = itemView.findViewById(R.id.ly_main);
            ivDelete = itemView.findViewById(R.id.iv_delete);

            if (isCheckVisible) {
                cbMemberSelect.setVisibility(View.VISIBLE);
            }
            if (isDeleteVisible) {
                ivDelete.setVisibility(View.VISIBLE);
            }

            cbMemberSelect.setOnClickListener(v -> {
                if (membersList.size() > getAdapterPosition()) {
                    if (((CheckBox) v).isChecked()) {
                        membersList.get(getAdapterPosition()).setMemberSelected(true);
                        ((AddMembersListActivity) mContext).checkAllSelected(membersList);
                    } else {
                        membersList.get(getAdapterPosition()).setMemberSelected(false);
                        ((AddMembersListActivity) mContext).checkAllDeSelected();
                    }
                }
            });
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((AddMembersListActivity) mContext).removeMember(membersList.get(getAdapterPosition()).getId());
                }
            });
        }
    }
}
