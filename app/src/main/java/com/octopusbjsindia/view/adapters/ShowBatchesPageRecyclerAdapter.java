package com.octopusbjsindia.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.Matrimony.Group;

import java.util.List;

public class ShowBatchesPageRecyclerAdapter extends RecyclerView.Adapter<ShowBatchesPageRecyclerAdapter.EmployeeViewHolder> {

    private List<Group> dataList;
    private Context mContext;
    private OnRequestItemClicked clickListener;

    public ShowBatchesPageRecyclerAdapter(Context context, List<Group> receiveddataList, final OnRequestItemClicked clickListener) {
        mContext = context;
        this.dataList = receiveddataList;
        this.clickListener = clickListener;
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_show_batchespage_item, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {
        holder.txtTitle.setText("Batch");
        holder.txtValue.setText(dataList.get(position).getTitle().get(0) + "\n" + dataList.get(position).getTitleUnit().get(0));
        //holder.txtValue.setText(String.valueOf(position+1));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle, txtValue;

        EmployeeViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.tv_title);
            txtValue = (TextView) itemView.findViewById(R.id.tv_value);
            itemView.setOnClickListener(v -> {
                clickListener.onItemClicked(getAdapterPosition(),
                        dataList.get(getAdapterPosition()).getTitle().get(0) + " " + dataList.get(getAdapterPosition()).getTitleUnit().get(0));
            });
        }
    }

    public interface OnRequestItemClicked {
        void onItemClicked(int pos, String title);
    }
}
