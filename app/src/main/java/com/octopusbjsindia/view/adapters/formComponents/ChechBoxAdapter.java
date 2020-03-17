package com.octopusbjsindia.view.adapters.formComponents;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.forms.Choice;
import com.octopusbjsindia.view.fragments.formComponents.CheckboxFragment;

import java.util.ArrayList;
import java.util.List;

public class ChechBoxAdapter extends RecyclerView.Adapter<ChechBoxAdapter.ViewHolder> {

    CheckboxFragment mContext;
    ArrayList<Choice> list;

    public ChechBoxAdapter(CheckboxFragment mContext, List<Choice> list) {
        this.mContext = mContext;
        this.list = (ArrayList<Choice>) list;
    }

    @NonNull
    @Override
    public ChechBoxAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_each_ceckbox,
                parent, false);
        return new ChechBoxAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChechBoxAdapter.ViewHolder holder, int position) {
        holder.checkBox.setText(list.get(position).getText().getDefaultValue());
        if (mContext.selectedList.size() > 0 && mContext.selectedList.contains(list.get(position).getValue())) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.check_box);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mContext.isNone = false;
                        mContext.cbNone.setChecked(false);
                        if(!mContext.selectedList.contains(list.get(getAdapterPosition()).getValue()))
                            mContext.selectedList.add(list.get(getAdapterPosition()).getValue());
                    } else {
                        mContext.selectedList.remove(list.get(getAdapterPosition()).getValue());
                    }

                }
            });
        }
    }
}
