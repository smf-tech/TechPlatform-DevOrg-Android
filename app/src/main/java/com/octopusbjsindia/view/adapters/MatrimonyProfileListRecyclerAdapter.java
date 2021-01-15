package com.octopusbjsindia.view.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.Matrimony.UserProfileList;

import java.util.ArrayList;
import java.util.List;

public class MatrimonyProfileListRecyclerAdapter extends RecyclerView.Adapter<MatrimonyProfileListRecyclerAdapter.EmployeeViewHolder> {
    private List<UserProfileList> dataList;
    private Context mContext;
    private RequestOptions requestOptions;
    private OnRequestItemClicked clickListener;
    private OnApproveRejectClicked buttonClickListner;

    public MatrimonyProfileListRecyclerAdapter(Context context, List<UserProfileList> dataListreceived,
                                               final OnRequestItemClicked clickListener,
                                               final OnApproveRejectClicked approveRejectClickedListner) {
            mContext = context;
            this.dataList = dataListreceived;
            this.clickListener =clickListener;
            this.buttonClickListner = approveRejectClickedListner;
        requestOptions = new RequestOptions().placeholder(R.drawable.ic_no_image);
        requestOptions = requestOptions.apply(RequestOptions.noTransformation());
        }

        @Override
        public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.row_matrimony_profilelist_item, parent, false);
            return new EmployeeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(EmployeeViewHolder holder, int position) {
            holder.txtTitle.setText(dataList.get(position).getMatrimonial_profile().
                    getPersonal_details().getFirst_name() + " " + dataList.get(position).
                    getMatrimonial_profile().getPersonal_details().getLast_name());
            String s = new StringBuffer().append(String.valueOf(dataList.get(position).getMatrimonial_profile().getPersonal_details().getAge()+" Years, "))
                    .append(dataList.get(position).getMatrimonial_profile().getEducational_details().getQualification_degree()+", ")
                    .append(dataList.get(position).getMatrimonial_profile().getPersonal_details().getMarital_status()+", ")
                    .append(dataList.get(position).getMatrimonial_profile().getPersonal_details().getSect()).toString();
                    /*.append(dataList.get(position).getMatrimonial_profile().getResidential_details().getCity()+",")
                    .append(dataList.get(position).getMatrimonial_profile().getResidential_details().getCountry()).toString();*/
            holder.txtValue.setText(s);

            //sectionType.equalsIgnoreCase("MeetUserList")
            if (!TextUtils.isEmpty(dataList.get(position).getUserMeetStatus())) {
                //holder.tv_approval_status.setText(dataList.get(position).getUserMeetStatus());
                holder.lyMeetApproved.setVisibility(View.VISIBLE);
                if (dataList.get(position).getUserMeetStatus().equalsIgnoreCase("pending")) {
                    holder.ivMeetApproved.setImageResource(R.drawable.ic_meet_pending);
                    holder.tvMeetApproved.setText("Pending in meet");
                } else if (dataList.get(position).getUserMeetStatus().equalsIgnoreCase("approved")) {
                    holder.ivMeetApproved.setImageResource(R.drawable.ic_meet_approved);
                    holder.tvMeetApproved.setText("Approved in meet");
                } else if (dataList.get(position).getUserMeetStatus().equalsIgnoreCase("rejected")) {
                    holder.ivMeetApproved.setImageResource(R.drawable.ic_meet_rejected);
                    holder.tvMeetApproved.setText("Rejected in meet");
                }

            } else {
                holder.lyMeetApproved.setVisibility(View.GONE);
            }

            if (dataList.get(position).isPaid()) {
                holder.lyPremium.setVisibility(View.VISIBLE);
            }else {
                holder.lyPremium.setVisibility(View.GONE);
            }
            if (dataList.get(position).getBlockCount()!=0) {
                holder.ly_blocked_count.setVisibility(View.VISIBLE);
                holder.tv_blocked.setText(""+dataList.get(position).getBlockCount());
            }else {
                holder.ly_blocked_count.setVisibility(View.GONE);
            }

            if (dataList.get(position).getMatrimonial_profile().getOther_marital_information().getProfile_image() != null
                    && dataList.get(position).getMatrimonial_profile().getOther_marital_information().getProfile_image().size() > 0) {
                Glide.with(mContext)
                        .applyDefaultRequestOptions(requestOptions)
                        .load(dataList.get(position).getMatrimonial_profile().getOther_marital_information().getProfile_image().get(0))
                        .into(holder.user_profile_pic);
            }
        }

        @Override
        public int getItemCount() {
            return this.dataList.size();
        }

    public void updateList(ArrayList<UserProfileList> temp) {
            dataList =temp;
            notifyDataSetChanged();
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle, txtValue, tv_approval_status, tv_premium, tv_payment_status, tvPremium,
                tvMeetApproved, tv_blocked, tv_blocked_count;
        ImageView user_profile_pic, ivMeetApproved;
        LinearLayout lyPremium, lyMeetApproved,ly_blocked_count;
//            Button btn_reject,btn_approve;

            EmployeeViewHolder(View itemView) {
                super(itemView);
                lyPremium = itemView.findViewById(R.id.ly_premium);
                tvPremium = itemView.findViewById(R.id.tv_premium);
                lyMeetApproved = itemView.findViewById(R.id.ly_meet_approved);
                ivMeetApproved = itemView.findViewById(R.id.iv_meet_approved);
                tvMeetApproved = itemView.findViewById(R.id.tv_meet_approved);
                txtTitle = itemView.findViewById(R.id.tv_title);
                txtValue = itemView.findViewById(R.id.tv_value);
                tv_blocked = itemView.findViewById(R.id.tv_blocked);
                tv_blocked_count = itemView.findViewById(R.id.tv_blocked_count);
                ly_blocked_count = itemView.findViewById(R.id.ly_blocked_count);
                user_profile_pic = itemView.findViewById(R.id.user_profile_pic);

                ly_blocked_count.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tv_blocked_count.getVisibility() == View.VISIBLE)
                            tv_blocked_count.setVisibility(View.GONE);
                        else
                            tv_blocked_count.setVisibility(View.VISIBLE);
                    }
                });
                lyPremium.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvPremium.getVisibility() == View.VISIBLE)
                            tvPremium.setVisibility(View.GONE);
                        else
                            tvPremium.setVisibility(View.VISIBLE);
                    }
                });

                lyMeetApproved.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvMeetApproved.getVisibility() == View.VISIBLE)
                            tvMeetApproved.setVisibility(View.GONE);
                        else
                            tvMeetApproved.setVisibility(View.VISIBLE);
                    }
                });

                itemView.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));
            }
        }
    public interface OnRequestItemClicked {
        void onItemClicked(int pos);
    }
    public interface OnApproveRejectClicked{
        void onApproveClicked(int pos);
        void onRejectClicked(int pos);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
