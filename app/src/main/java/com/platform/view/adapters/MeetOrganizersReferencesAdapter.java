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

public class MeetOrganizersReferencesAdapter extends RecyclerView.Adapter<MeetOrganizersReferencesAdapter.ViewHolder>{
    private List<YearlyHolidayData> meetOrganizersReferencesList;

    public MeetOrganizersReferencesAdapter(final List<YearlyHolidayData> meetOrganizersReferencesList) {
        this.meetOrganizersReferencesList = meetOrganizersReferencesList;
    }
    @NonNull
    @Override
    public MeetOrganizersReferencesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_meet_organizer_reference_row,
                parent, false);
        return new MeetOrganizersReferencesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetOrganizersReferencesAdapter.ViewHolder holder, int position) {
//        holder.tvHolidayDate.setText();
//        holder.tvHolidayText.setText();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        // TextView tvHolidayDate, tvHolidayText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            tvHolidayDate = itemView.findViewById(R.id.tv_holiday_date);
//            tvHolidayText = itemView.findViewById(R.id.tv_holiday_text);
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
