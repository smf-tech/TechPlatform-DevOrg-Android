package com.platform.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.models.common.CustomSpinnerObject;
import com.platform.models.tm.SubFilterset;
import com.platform.view.customs.CustomSpinnerDialogClass;

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
        holder.cb_select_filter.setChecked(dataList.get(position).isSelected());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle, txtValue;
        CheckBox cb_select_filter;
        //RadioButton rbSelectFilter;
        int selectedPosition = -1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
            txtTitle = itemView.findViewById(R.id.tv_filters);
            cb_select_filter = itemView.findViewById(R.id.cb_select_filter);
            //rbSelectFilter = itemView.findViewById(R.id.rb_select_filter);

            cb_select_filter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isMultiselectionAllowed) {
                        dataList.get(getAdapterPosition()).setSelected(isChecked);
                    } else{
                        //dataList.get(selectedPosition).setSelected(false);
//                        dataList.get(getAdapterPosition()).setSelected(isChecked);
//                        if(selectedPosition != -1){
//                            dataList.get(selectedPosition).setSelected(false);
//                        }
//                        if(isChecked) {
//                            selectedPosition = getAdapterPosition();
//                        }
                    }
                }
            });
        }
    }
}
