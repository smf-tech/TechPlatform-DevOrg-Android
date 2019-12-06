package com.octopus.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.octopus.R;
import com.octopus.models.SujalamSuphalam.MachineDieselRecord;

import java.util.List;

import static com.octopus.utility.Constants.DAY_MONTH_YEAR;
import static com.octopus.utility.Util.getDateFromTimestamp;

public class MachineDieselRecordsAdapter extends RecyclerView.Adapter<MachineDieselRecordsAdapter.ViewHolder> {
    private List<MachineDieselRecord> machineDieselRecordsList;
    private Fragment fragment;

    public MachineDieselRecordsAdapter(final List<MachineDieselRecord> machineDieselRecordsList, Fragment fragment) {
        this.machineDieselRecordsList = machineDieselRecordsList;
        this.fragment = fragment;
    }
    @NonNull
    @Override
    public MachineDieselRecordsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_diesel_record_item,
                parent, false);
        return new MachineDieselRecordsAdapter.ViewHolder(v);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate, tvDieselQuantity;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvDieselQuantity = itemView.findViewById(R.id.tv_diesel_quantity);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MachineDieselRecordsAdapter.ViewHolder holder, int position) {
        holder.tvDate.setText(getDateFromTimestamp(machineDieselRecordsList.get(position).
                getDieselDate(), DAY_MONTH_YEAR));
        holder.tvDieselQuantity.setText(machineDieselRecordsList.get(position).getDieselQuantity());
    }

    @Override
    public int getItemCount() {
        return machineDieselRecordsList.size();
    }
}
