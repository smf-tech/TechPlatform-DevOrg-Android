package com.octopusbjsindia.view.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.sel_content.SELVideoContent;
import com.octopusbjsindia.view.activities.SELTrainingActivity;
import com.octopusbjsindia.view.fragments.SELFragment;

import java.util.List;

public class SELFragmentAdapter extends RecyclerView.Adapter<SELFragmentAdapter.ViewHolder> {

    private List<SELVideoContent> selContentList;
    private SELFragment mContext;

    public SELFragmentAdapter(SELFragment context, final List<SELVideoContent> selContentList) {
        mContext = context;
        this.selContentList = selContentList;
    }

    @NonNull
    @Override
    public SELFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sel_video_row,
                parent, false);
        return new SELFragmentAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTraining.setText(selContentList.get(position).getTitle());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTraining;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTraining = itemView.findViewById(R.id.tv_training);
            tvTraining.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext.getActivity(), SELTrainingActivity.class);
                    intent.putExtra("TrainingObject", selContentList.get(getAdapterPosition()));
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return selContentList.size();
    }
}
