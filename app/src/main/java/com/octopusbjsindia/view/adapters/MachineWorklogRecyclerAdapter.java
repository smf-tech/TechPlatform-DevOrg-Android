package com.octopusbjsindia.view.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.snackbar.Snackbar;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.Operator.MachineWorklogList;
import com.octopusbjsindia.utility.PreferenceHelper;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.MachineWorkingDataListActivity;
import com.octopusbjsindia.view.activities.MatrimonyProfileListActivity;
import com.octopusbjsindia.view.activities.OperatorMeterReadingActivity;
import com.octopusbjsindia.view.fragments.TMUserAttendanceApprovalFragment;
import com.octopusbjsindia.view.fragments.TMUserFormsApprovalFragment;
import com.octopusbjsindia.view.fragments.TMUserLeavesApprovalFragment;
import com.octopusbjsindia.view.fragments.TMUserProfileApprovalFragment;

import java.util.List;

import static com.octopusbjsindia.view.activities.MachineWorkingDataListActivity.isReadingEditAccess;

public class MachineWorklogRecyclerAdapter extends RecyclerView.Adapter<MachineWorklogRecyclerAdapter.EmployeeViewHolder> {

    Context mContext;
    Activity activity;
    private List<MachineWorklogList> dataList;
    private OnRequestItemClicked clickListener;
private RequestOptions requestOptions;
    private PreferenceHelper preferenceHelper;

    public MachineWorklogRecyclerAdapter(Activity activity,Context context, List<MachineWorklogList> dataList, final OnRequestItemClicked clickListener) {
        mContext = context;
        this.activity = activity;
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
        TextView tv_view_activity;
        TextView tv_work_date,tv_total_hours,tv_total_hours_value;
        TextView tv_title_start,tv_value_start;
        TextView tv_title_end,tv_value_end;
        ImageView img_end_meter,img_start_meter;


        EmployeeViewHolder(View itemView) {
            super(itemView);
            tv_view_activity = itemView.findViewById(R.id.tv_view_activity);
            tv_work_date = itemView.findViewById(R.id.tv_work_date);
            tv_total_hours = itemView.findViewById(R.id.tv_total_hours);
            tv_total_hours_value = itemView.findViewById(R.id.tv_total_hours_value);

            tv_title_start = itemView.findViewById(R.id.tv_title_start);
            tv_value_start = itemView.findViewById(R.id.tv_value_start);

            tv_title_end = itemView.findViewById(R.id.tv_title_end);

            tv_value_end = itemView.findViewById(R.id.tv_value_end);
            img_end_meter = itemView.findViewById(R.id.img_end_meter);
            img_start_meter = itemView.findViewById(R.id.img_start_meter);
            tv_view_activity= itemView.findViewById(R.id.tv_view_activity);


        //    itemView.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));

            tv_view_activity.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));

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

            tv_value_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Util.isConnected(mContext)) {
                        if (isReadingEditAccess) {
                            if (dataList.get(getAdapterPosition()).getEndReading() != null) {

                                showReadingDialog(activity, getAdapterPosition(), 1);
                            } else {
                                Util.showToast("Can not edit empty reading.", mContext);
                            }
                        } else {
                            Util.showToast("Please contact MIS department in HO.", mContext);
                        }
                    }else {
                        Util.showToast("No internet connection.", mContext);
                    }

                }
            });
            tv_value_end.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Util.isConnected(mContext)) {
                        if (isReadingEditAccess) {
                            if (dataList.get(getAdapterPosition()).getEndReading() != null) {
                                showReadingDialog(activity, getAdapterPosition(), 2);
                            } else {
                                Util.showToast("Can not edit empty reading.", mContext);
                            }

                        } else {
                            Util.showToast("Please contact MIS department in HO.", mContext);
                        }
                    }else {
                        Util.showToast("No internet connection.", mContext);
                }
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
                photoView.setImageResource(R.drawable.ic_img);
            }
        } else {
            photoView.setImageResource(R.drawable.ic_img);
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


    // input reading dialog
    public String showReadingDialog(final Activity context, int pos,int flagStartEndReading){
        Dialog dialog;
        Button btnSubmit,btn_cancel;
        EditText edt_reason;
        Activity activity =context;

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_meter_reading_input_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        edt_reason = dialog.findViewById(R.id.edt_reason);
        btn_cancel = dialog.findViewById(R.id.btn_cancel);
        btnSubmit = dialog.findViewById(R.id.btn_submit);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   /*Intent loginIntent = new Intent(context, LoginActivity.class);
                   loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                   loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   context.startActivity(loginIntent);*/
                String strReason  = edt_reason.getText().toString();


                if (strReason.trim().length() < 1 ) {
                    String msg = "Please enter the valid meter reading";//getResources().getString(R.string.msg_enter_name);
                    //et_primary_mobile.requestFocus();
                    Util.showToast(msg, activity);
                }else {

                    //-----------------------
                    if (TextUtils.isEmpty(strReason)) {
                        Util.logger("Empty Reading", "Reading Can not be blank");
                        Util.snackBarToShowMsg(activity.getWindow().getDecorView()
                                        .findViewById(android.R.id.content), "Reading Can not be blank",
                                Snackbar.LENGTH_LONG);
                    } else {
                    /*if (fragment instanceof TMUserLeavesApprovalFragment) {
                        ((TMUserLeavesApprovalFragment) fragment).onReceiveReason(strReason, pos);
                    }
                    if (fragment instanceof TMUserAttendanceApprovalFragment) {
                        ((TMUserAttendanceApprovalFragment) fragment).onReceiveReason(strReason, pos);
                    }
                    if (fragment instanceof TMUserProfileApprovalFragment) {
                        ((TMUserProfileApprovalFragment) fragment).onReceiveReason(strReason, pos);
                    }
                    if (fragment instanceof TMUserFormsApprovalFragment) {
                        ((TMUserFormsApprovalFragment) fragment).onReceiveReason(strReason, pos);
                    }*/
                        ((MachineWorkingDataListActivity)mContext).onReceiveEditedReading(strReason, pos,flagStartEndReading);
                        dialog.dismiss();
                    }
                }
            }
        });
        dialog.show();


        return "";
    }

}
