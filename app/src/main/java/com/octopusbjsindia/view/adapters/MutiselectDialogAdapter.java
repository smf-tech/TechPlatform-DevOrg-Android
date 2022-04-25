package com.octopusbjsindia.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.common.CustomSpinnerObject;

import java.util.ArrayList;

 public class MutiselectDialogAdapter extends RecyclerView.Adapter<MutiselectDialogAdapter.ViewHolder> {

    private ArrayList<CustomSpinnerObject> dataList;
    private Context mContext;
    private boolean isMultiselectionAllowed;
    //private OnRequestItemClicked clickListener;

    public MutiselectDialogAdapter(Context context, ArrayList<CustomSpinnerObject> dataList, boolean isMultiselectionAllowed){
        //},final OnRequestItemClicked clickListener) {
        mContext = context;
        this.dataList = dataList;
        this.isMultiselectionAllowed = isMultiselectionAllowed;
        //this.clickListener =clickListener;
    }

    @Override
    public MutiselectDialogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_filterdialog_item, parent, false);
        return new MutiselectDialogAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MutiselectDialogAdapter.ViewHolder holder, int position) {
        holder.txtTitle.setText(dataList.get(position).getName());
        if(dataList.get(position).isSelected()){
            holder.btSelectFilter.setBackgroundResource(R.drawable.ic_custom_checked);
        } else {
            holder.btSelectFilter.setBackgroundResource(R.drawable.ic_custom_unchecked);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        ImageView btSelectFilter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
            txtTitle = itemView.findViewById(R.id.tv_filters);
            btSelectFilter = itemView.findViewById(R.id.bt_select_filter);
            btSelectFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isMultiselectionAllowed) {
                        if(dataList.get(getAdapterPosition()).isSelected()){
                            dataList.get(getAdapterPosition()).setSelected(false);
                        } else {
                            dataList.get(getAdapterPosition()).setSelected(true);
                        }
                    } else {
                        for (int i = 0; i < dataList.size(); i++) {
                            if(dataList.get(i).isSelected()) {
                                dataList.get(i).setSelected(false);
                                break;
                            }
                        }
                        dataList.get(getAdapterPosition()).setSelected(true);
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }
}
