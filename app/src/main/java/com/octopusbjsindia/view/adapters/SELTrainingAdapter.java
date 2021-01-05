package com.octopusbjsindia.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.sel_content.SELAssignmentData;
import com.octopusbjsindia.models.sel_content.SELReadingData;
import com.octopusbjsindia.view.activities.SELTrainingActivity;

import java.util.List;

public class SELTrainingAdapter extends RecyclerView.Adapter<SELTrainingAdapter.ViewHolder> {

    private List<SELReadingData> SELReadingDataList;
    private List<SELAssignmentData> SELAssignmentData;
    private SELTrainingActivity mContext;
    private int type = 0;

    public SELTrainingAdapter(SELTrainingActivity context, final List<SELReadingData> SELReadingDataList, int type) {
        mContext = context;
        this.SELReadingDataList = SELReadingDataList;
        this.type = type;
    }

//    public SELTrainingAdapter(SELTrainingActivity context, final List<SELAssignmentData> SELAssignmentData) {
//        mContext = context;
//        this.SELAssignmentData = SELAssignmentData;
//    }

    @NonNull
    @Override
    public SELTrainingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sel_training_row,
                parent, false);
        return new SELTrainingAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SELTrainingAdapter.ViewHolder holder, int position) {
        //if(type == 0) {
        holder.tvTitle.setText(SELReadingDataList.get(position).getContentName());
//        } else {
//            holder.tvTitle.setText(SELAssignmentData.get(position).getFormName());
//        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, SELTrainingActivity.class);
//                    intent.putExtra("TrainingObject", selContentList.get(getAdapterPosition()));
//                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return SELReadingDataList.size();
    }
}
