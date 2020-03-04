package com.octopusbjsindia.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.view.activities.EditProfileActivity;

import java.util.List;

public class DropDownSelectionAdapter extends RecyclerView.Adapter<DropDownSelectionAdapter.ViewHolder> {
    private List<CustomSpinnerObject> dropdownSelectionList;
    private String dropDownType;
    private EditProfileActivity activity;

    public DropDownSelectionAdapter(final List<CustomSpinnerObject> dropdownSelectionList,
                                    String dropDownType, EditProfileActivity activity) {
        this.dropdownSelectionList = dropdownSelectionList;
        this.dropDownType = dropDownType;
        this.activity = activity;
    }

    @NonNull
    @Override
    public DropDownSelectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dropdown_selection_list_item,
                parent, false);
        return new DropDownSelectionAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DropDownSelectionAdapter.ViewHolder holder, int position) {
        holder.tvName.setText(dropdownSelectionList.get(position).getName());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        RelativeLayout rlSelection;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            rlSelection = itemView.findViewById(R.id.rl_selection);
            rlSelection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dropDownType.equals("Organization")) {
                        activity.onOrgSelection(getAdapterPosition());
                    } else {
                        activity.onProjectSelection(getAdapterPosition());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dropdownSelectionList.size();
    }
}
