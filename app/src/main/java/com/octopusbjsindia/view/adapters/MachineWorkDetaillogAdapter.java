package com.octopusbjsindia.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.Operator.MachineWorklogDetail;
import com.octopusbjsindia.models.Operator.MachineWorklogDetailModel;
import com.octopusbjsindia.utility.PreferenceHelper;

import java.util.List;

public class MachineWorkDetaillogAdapter extends RecyclerView.Adapter<MachineWorkDetaillogAdapter.EmployeeViewHolder> {

    Context mContext;
    private List<MachineWorklogDetail> dataList;
    private OnRequestItemClicked clickListener;
    private RequestOptions requestOptions;
    private PreferenceHelper preferenceHelper;

    public MachineWorkDetaillogAdapter(Context context, List<MachineWorklogDetail> dataList, final OnRequestItemClicked clickListener) {
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
        View view = layoutInflater.inflate(R.layout.row_machine_workdetails_item, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {


        /*holder.tv_work_date_title.setText(dataList.get(position).getStartDate());
        holder.tv_workhours_title.setText(dataList.get(position).getTotalHrsCunt());
        holder.tv_machine_state_title.setText(dataList.get(position).getStartReading());*/

        holder.tv_work_date_value.setText(dataList.get(position).getWorkDate());
        holder.tv_workhours_value.setText(dataList.get(position).getTotalHours());
        holder.tv_machine_state_value.setText(dataList.get(position).getStatus());

/*        if (dataList.get(position).getStartMeterReadingImage() != null && !dataList.get(position).getStartMeterReadingImage().equals("")) {
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

    public interface OnRequestItemClicked {
        void onItemClicked(int pos);
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder {

        TextView tv_work_date_title, tv_workhours_title, tv_machine_state_title;
        TextView tv_work_date_value, tv_workhours_value, tv_machine_state_value;


        EmployeeViewHolder(View itemView) {
            super(itemView);
            tv_work_date_title = itemView.findViewById(R.id.tv_work_date_title);
            tv_workhours_title = itemView.findViewById(R.id.tv_workhours_title);
            tv_machine_state_title = itemView.findViewById(R.id.tv_machine_state_title);

            tv_work_date_value  = itemView.findViewById(R.id.tv_work_date_value);
            tv_workhours_value  = itemView.findViewById(R.id.tv_workhours__value);
            tv_machine_state_value  = itemView.findViewById(R.id.tv_machine_state_value);


            itemView.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));


        }
    }


}
