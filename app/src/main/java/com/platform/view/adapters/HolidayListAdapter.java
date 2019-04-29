package com.platform.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;

import java.util.List;

@SuppressWarnings("CanBeFinal")
public class HolidayListAdapter extends RecyclerView.Adapter<HolidayListAdapter.ViewHolder> {

    private Context mContext;
    private List<String> leavesList;

    public HolidayListAdapter(final Context context, final List<String> leavesList) {
        this.mContext = context;
        this.leavesList = leavesList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView holDate;
        TextView holDesc;
        //TextView leaveStatus;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            holDate = itemView.findViewById(R.id.tv_holiday_date);
            //leaveStatus = itemView.findViewById(R.id.tv_leave_status);
            holDesc = itemView.findViewById(R.id.tv_holiday_desc);

        }
    }

    @NonNull
    @Override
    public HolidayListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.holiday_list_row, viewGroup, false);
        return new HolidayListAdapter.ViewHolder(v);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBindViewHolder(@NonNull HolidayListAdapter.ViewHolder viewHolder, int i) {

        viewHolder.holDesc.setText("8 March");
        viewHolder.holDesc.setText("Holi");
        //viewHolder.leaveStatus.setText("Request status :"+"Not yet approved");
    }

    @Override
    public int getItemCount() {
        return leavesList.size();
    }
}