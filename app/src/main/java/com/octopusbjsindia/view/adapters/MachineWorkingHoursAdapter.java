package com.octopusbjsindia.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.SujalamSuphalam.MachineWorkingHoursRecord;
import com.octopusbjsindia.utility.Util;

import java.util.List;

import static com.octopusbjsindia.utility.Constants.DAY_MONTH_YEAR;

public class MachineWorkingHoursAdapter extends RecyclerView.Adapter<MachineWorkingHoursAdapter.ViewHolder>  {
    private List<MachineWorkingHoursRecord> workingHoursList;
    private Fragment fragment;

    public MachineWorkingHoursAdapter(final List<MachineWorkingHoursRecord> workingHoursList, Fragment fragment) {
        this.workingHoursList = workingHoursList;
        this.fragment = fragment;
    }
    @NonNull
    @Override
    public MachineWorkingHoursAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_working_hours_item,
                parent, false);
        return new MachineWorkingHoursAdapter.ViewHolder(v);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate, tvWorkingHours, tvStatus;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvWorkingHours = itemView.findViewById(R.id.tv_working_hours);
            tvStatus = itemView.findViewById(R.id.tv_status);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MachineWorkingHoursAdapter.ViewHolder viewHolder, int position) {
        viewHolder.tvDate.setText(Util.getDateFromTimestamp(workingHoursList.get(position).getWorkingDate(), DAY_MONTH_YEAR));
        viewHolder.tvWorkingHours.setText(workingHoursList.get(position).getWorkingHours());
        if(workingHoursList.get(position).getWorkingStatus()) {
            viewHolder.tvStatus.setText("Match");
        } else {
            viewHolder.tvStatus.setText("MisMatch");
        }
    }

    @Override
    public int getItemCount() {
        return workingHoursList.size();
    }
}
