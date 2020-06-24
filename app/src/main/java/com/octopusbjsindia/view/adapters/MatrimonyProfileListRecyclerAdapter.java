package com.octopusbjsindia.view.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
            if (dataList.get(position).isPaymentDone()){
                holder.tv_payment_status.setVisibility(View.VISIBLE);
            }

            if (!TextUtils.isEmpty(dataList.get(position).getUserMeetStatus())) {
                holder.tv_approval_status.setText(dataList.get(position).getUserMeetStatus());
            }else {
                holder.tv_approval_status.setVisibility(View.GONE);
            }
//            if (dataList.get(position).getIsApproved().toLowerCase().startsWith("p")){
//
//            }else if (dataList.get(position).getIsApproved().toLowerCase().startsWith("r")){
//                holder.btn_reject.setVisibility(View.GONE);
//                holder.btn_approve.setVisibility(View.VISIBLE);
//            }else if (dataList.get(position).getIsApproved().toLowerCase().startsWith("a")){
//                holder.btn_approve.setVisibility(View.GONE);
//                holder.btn_reject.setVisibility(View.VISIBLE);
//            }
            if (dataList.get(position).isIsPremium()){
                holder.tv_premium.setVisibility(View.VISIBLE);
                holder.tv_premium.setText("Premium");
            }else {
                holder.tv_premium.setVisibility(View.GONE);
            }



            if (dataList.get(position).getMatrimonial_profile().getOther_marital_information().getProfile_image()!=null&&dataList.get(position).getMatrimonial_profile().getOther_marital_information().getProfile_image().size()>0){
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

            TextView txtTitle, txtValue,tv_approval_status,tv_premium,tv_payment_status;
            ImageView user_profile_pic;
//            Button btn_reject,btn_approve;

            EmployeeViewHolder(View itemView) {
                super(itemView);
                txtTitle = itemView.findViewById(R.id.tv_title);
                txtValue = itemView.findViewById(R.id.tv_value);
                tv_approval_status = itemView.findViewById(R.id.tv_approval_status);
                tv_payment_status = itemView.findViewById(R.id.tv_payment_status);
                tv_premium = itemView.findViewById(R.id.tv_premium);
                user_profile_pic = itemView.findViewById(R.id.user_profile_pic);

//                btn_reject = itemView.findViewById(R.id.btn_reject);
//                btn_approve = itemView.findViewById(R.id.btn_approve);
//                btn_approve.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        buttonClickListner.onApproveClicked(getAdapterPosition());
//                    }
//                });
//                btn_reject.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        buttonClickListner.onRejectClicked(getAdapterPosition());
//                    }
//                });

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
