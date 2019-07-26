package com.platform.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.models.leaves.YearlyHolidayData;

import java.util.List;

import static com.platform.utility.Constants.DAY_MONTH_YEAR;
import static com.platform.utility.Util.getDateFromTimestamp;

public class YearlyHolidaysAdapter extends RecyclerView.Adapter<YearlyHolidaysAdapter.ViewHolder> {
    private List<YearlyHolidayData> yearlyHolidayList;

    public YearlyHolidaysAdapter(final List<YearlyHolidayData> yearlyHolidayList) {
        this.yearlyHolidayList = yearlyHolidayList;
    }

    @NonNull
    @Override
    public YearlyHolidaysAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_yearly_holidays_row,
                parent, false);
        return new YearlyHolidaysAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvHolidayDate.setText(getDateFromTimestamp(yearlyHolidayList.get(position).getHolidayDate(),DAY_MONTH_YEAR));
        holder.tvHolidayText.setText(yearlyHolidayList.get(position).getName());
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvHolidayDate, tvHolidayText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHolidayDate = itemView.findViewById(R.id.tv_holiday_date);
            tvHolidayText = itemView.findViewById(R.id.tv_holiday_text);
        }
    }

    @Override
    public int getItemCount() {
        return yearlyHolidayList.size();
    }
}
