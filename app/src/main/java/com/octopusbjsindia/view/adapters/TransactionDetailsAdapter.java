package com.octopusbjsindia.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.Matrimony.TransectionDetailsData;
import java.util.List;

public class TransactionDetailsAdapter extends RecyclerView.Adapter<TransactionDetailsAdapter.ViewHolder> {

    private Context mContext;
    private List<TransectionDetailsData> list;
    private RequestOptions requestOptions;

    public TransactionDetailsAdapter(Context mContext, List<TransectionDetailsData> list) {
        this.mContext = mContext;
        this.list = list;
        requestOptions = new RequestOptions().placeholder(R.drawable.ic_user_avatar);
        requestOptions = requestOptions.apply(RequestOptions.circleCropTransform());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_transaction_details_row, parent, false);
        return new TransactionDetailsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        if (list.get(i).getProfileImage().get(0) != null && !list.get(i).getProfileImage().get(0).equals("")) {
            Glide.with(mContext)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(list.get(i).getProfileImage().get(0))
                    .into(holder.ivUserImage);
        } else {
            holder.ivUserImage.setImageResource(R.drawable.ic_user_avatar);
        }
        holder.tvName.setText(list.get(i).getName());
        holder.tvPhone.setText(list.get(i).getPhone());
        holder.tvEmail.setText(list.get(i).getEmail());
        holder.tvPaymentId.setText("PaymentId: "+list.get(i).getRazorpayPaymentId());
        holder.tvAmount.setText("Amount: "+(list.get(i).getAmount()/100));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivUserImage;
        TextView tvName, tvPhone, tvEmail, tvPaymentId, tvAmount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserImage = itemView.findViewById(R.id.iv_user_profile_pic);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvPaymentId = itemView.findViewById(R.id.tvPaymentId);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:"+list.get(getAdapterPosition()).getPhone()));
                    mContext.startActivity(callIntent);
                }
            });
        }
    }
}
