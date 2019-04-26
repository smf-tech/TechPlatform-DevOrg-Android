package com.platform.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.models.events.Member;
import com.platform.view.activities.AddMembersListActivity;

import java.util.ArrayList;

public class AddMembersListAdapter extends RecyclerView.Adapter<AddMembersListAdapter.ViewHolder> {

    private Context mContext;
    private boolean isCheckVisibil;
    private ArrayList<Member> membersList = null;
    private ArrayList<Member> filterMembersList;

    public AddMembersListAdapter(Context mContext, ArrayList<Member> membersList, boolean isCheckVisibil) {
        this.mContext = mContext;
        this.membersList = membersList;
        this.isCheckVisibil = isCheckVisibil;
        this.filterMembersList = new ArrayList<Member>();
        this.filterMembersList = membersList;
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

        if (membersList.get(position).getMemberAttended()) {
            holder.cbMemberSelect.setChecked(true);
        } else {
            holder.cbMemberSelect.setChecked(false);
        }
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMemberName = itemView.findViewById(R.id.tv_member_name);
            tvMemberDesignation = itemView.findViewById(R.id.tv_member_designation);
            cbMemberSelect = itemView.findViewById(R.id.cb_select_member);
//            lyMain = itemView.findViewById(R.id.ly_main);

            if (!isCheckVisibil) {
                cbMemberSelect.setVisibility(View.GONE);
            }

            cbMemberSelect.setOnClickListener(v -> {
                if (membersList.size() > getAdapterPosition()) {
                    if (((CheckBox) v).isChecked()) {
                        membersList.get(getAdapterPosition()).setMemberSelected(true);
                        ((AddMembersListActivity) mContext).checkAllSelected((ArrayList<Member>) membersList);
                    } else {
                        membersList.get(getAdapterPosition()).setMemberSelected(false);
                        ((AddMembersListActivity) mContext).checkAllDeSelected();
                    }
                }
            });

        }
    }

//    public void filter(String searchText) {
//        searchText = searchText.toLowerCase(Locale.getDefault());
//         if(searchText.length()>0) {
//            membersList.clear();
//            for (Member member : filterMembersList) {
//                if (member.getName().toLowerCase(Locale.getDefault()).contains(searchText)) {
//                    membersList.add(member);
//                }
//            }
//             notifyDataSetChanged();
//        }
//    }
}
