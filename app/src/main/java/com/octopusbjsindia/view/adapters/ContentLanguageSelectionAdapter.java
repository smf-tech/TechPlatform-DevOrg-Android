package com.octopusbjsindia.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.content.DownloadLanguageSelection;
import com.octopusbjsindia.view.fragments.ContentManagementFragment;

import java.util.ArrayList;

public class ContentLanguageSelectionAdapter extends RecyclerView.Adapter<ContentLanguageSelectionAdapter.ViewHolder> {

    private ContentManagementFragment mContext;
    private ArrayList<DownloadLanguageSelection> list;


    public ContentLanguageSelectionAdapter(ContentManagementFragment contentManagementFragment,
                                           ArrayList<DownloadLanguageSelection> list) {
        this.mContext = contentManagementFragment;
        this.list = list;
    }

    @NonNull
    @Override
    public ContentLanguageSelectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_each_radiobutton,
                parent, false);
        return new ContentLanguageSelectionAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentLanguageSelectionAdapter.ViewHolder holder, int position) {
        holder.radioButton.setText(list.get(position).getLanguage());
        if (list.get(position).isSelected()) {
            holder.radioButton.setChecked(true);
        } else {
            holder.radioButton.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radio_button);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < list.size(); i++) {
                        if (i == getAdapterPosition())
                            list.get(i).setSelected(true);
                        else
                            list.get(i).setSelected(false);
                    }
                    mContext.setDownloadPosition(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }
    }
}
