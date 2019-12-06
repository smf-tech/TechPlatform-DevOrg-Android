package com.octopus.view.adapters;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.octopus.Platform;
import com.octopus.R;
import com.octopus.models.Operator.MachineWorklogList;
import com.octopus.utility.PreferenceHelper;

import java.util.List;

public class MachineWorklogRecyclerAdapter extends RecyclerView.Adapter<MachineWorklogRecyclerAdapter.EmployeeViewHolder> {

    Context mContext;
    private List<MachineWorklogList> dataList;
    private OnRequestItemClicked clickListener;
private RequestOptions requestOptions;
    private PreferenceHelper preferenceHelper;

    public MachineWorklogRecyclerAdapter(Context context, List<MachineWorklogList> dataList, final OnRequestItemClicked clickListener) {
        mContext = context;
        this.dataList = dataList;
        this.clickListener = clickListener;
        preferenceHelper = new PreferenceHelper(Platform.getInstance());

        requestOptions = new RequestOptions().placeholder(R.drawable.ic_img);
        requestOptions = requestOptions.apply(RequestOptions.noTransformation());
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_machine_worklog_item, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {
        holder.tv_work_date.setText(dataList.get(position).getStartDate());
        holder.tv_total_hours_value.setText(dataList.get(position).getTotalHrsCunt());
        holder.tv_value_start.setText(dataList.get(position).getStartReading());
        holder.tv_value_end.setText(dataList.get(position).getEndReading());

        if (dataList.get(position).getStartMeterReadingImage() != null && !dataList.get(position).getStartMeterReadingImage().equals("")) {
            Glide.with(mContext)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(dataList.get(position).getStartMeterReadingImage())
                    .into(holder.img_start_meter);
        }
        if (dataList.get(position).getEndMeterReadingImage() != null && !dataList.get(position).getEndMeterReadingImage().equals("")) {
            Glide.with(mContext)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(dataList.get(position).getEndMeterReadingImage())
                    .into(holder.img_end_meter);
        }

/*
        holder.tv_startdate.setText(Util.getLongDateInString(Long.parseLong(dataList.get(position).getStartdate()), Constants.DAY_MONTH_YEAR));
        holder.tv_enddate.setText(Util.getLongDateInString(Long.parseLong(dataList.get(position).getEnddate()), Constants.DAY_MONTH_YEAR));
        holder.tv_leave_reason.setText(String.valueOf(dataList.get(position).getReason()));
        holder.tv_leave_status.setText(dataList.get(position).getStatus().getStatus());

        if (!TextUtils.isEmpty(dataList.get(position).getReason())){
            holder.tv_leave_reason.setText("Reason:- "+String.valueOf(dataList.get(position).getReason()));
        }
        if (!TextUtils.isEmpty(dataList.get(position).getStatus().getRejection_reason())) {
            holder.tv_leave_reason.setText("Rejected Reason:- "+String.valueOf(dataList.get(position).getStatus().getRejection_reason()));
        }
        String ispending = preferenceHelper.getString(PreferenceHelper.IS_PENDING);
        if (ispending.equalsIgnoreCase(PreferenceHelper.IS_PENDING)) {
            holder.btn_reject.setVisibility(View.VISIBLE);
            holder.btn_approve.setVisibility(View.VISIBLE);
        } else {
            holder.btn_reject.setVisibility(View.GONE);
            holder.btn_approve.setVisibility(View.GONE);
        }*/

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder {

        TextView tv_work_date,tv_total_hours,tv_total_hours_value;
        TextView tv_title_start,tv_value_start;
        TextView tv_title_end,tv_value_end;
        ImageView img_end_meter,img_start_meter;


        EmployeeViewHolder(View itemView) {
            super(itemView);
            tv_work_date = itemView.findViewById(R.id.tv_work_date);
            tv_total_hours = itemView.findViewById(R.id.tv_total_hours);
            tv_total_hours_value = itemView.findViewById(R.id.tv_total_hours_value);

            tv_title_start = itemView.findViewById(R.id.tv_title_start);
            tv_value_start = itemView.findViewById(R.id.tv_value_start);

            tv_title_end = itemView.findViewById(R.id.tv_title_end);

            tv_value_end = itemView.findViewById(R.id.tv_value_end);
            img_end_meter = itemView.findViewById(R.id.img_end_meter);
            img_start_meter = itemView.findViewById(R.id.img_start_meter);
            itemView.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));

            img_end_meter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //buttonClickListner.onApproveClicked(getAdapterPosition());
                    enlargePhoto(dataList.get(getAdapterPosition()).getEndMeterReadingImage());
                }
            });
            img_start_meter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //buttonClickListner.onRejectClicked(getAdapterPosition());
                    enlargePhoto(dataList.get(getAdapterPosition()).getStartMeterReadingImage());
                }
            });
        }
    }

    public interface OnRequestItemClicked {
        void onItemClicked(int pos);
    }



    private void enlargePhoto(String photoUrl) {
        // stop the video if playing

        final Dialog dialog = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        LayoutInflater factory = LayoutInflater.from(mContext);
        final View enlargePhotoView = factory.inflate(
                R.layout.enlarge_photo_layout, null);
        FrameLayout closeOption = (FrameLayout) enlargePhotoView.findViewById(R.id.close_layout);
        closeOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        PhotoView photoView = (PhotoView) enlargePhotoView.findViewById(R.id.img_feed_photo);
        ImageView closeImageView = (ImageView) enlargePhotoView.findViewById(R.id.img_close_dialog);
        closeImageView.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(photoUrl)) {
            try {
                Glide.with(mContext)
                        .load(photoUrl)
                        /*.placeholder(R.drawable.image_placeholder)*/
                        .into(photoView);
            } catch (Exception e) {
                e.printStackTrace();
                photoView.setImageResource(R.drawable.ic_user_avatar);
            }
        } else {
            photoView.setImageResource(R.drawable.ic_user_avatar);
        }

        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(enlargePhotoView);
        dialog.show();

    }

}
