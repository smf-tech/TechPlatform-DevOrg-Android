package com.platform.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.platform.R;
import com.platform.models.pm.ProcessData;

import java.util.List;

import static com.platform.utility.Constants.Form.FORM_STATUS_COMPLETED;
import static com.platform.utility.Constants.Form.FORM_STATUS_PENDING;

class FormsAdapter extends RecyclerView.Adapter<FormsAdapter.ViewHolder> {

    private Context mContext;
    private String status;
    private List<ProcessData> mProcessData;

    FormsAdapter(Context context, String status, final List<ProcessData> processData) {
        this.mContext = context;
        this.status = status;
        mProcessData = processData;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView mName;
        TextView mDate;
        View indicatorView;
        FloatingActionButton mPinButton;

        @SuppressLint("RestrictedApi")
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.form_title);
            mDate = itemView.findViewById(R.id.form_date);
            mPinButton = itemView.findViewById(R.id.pin_button);
            indicatorView = itemView.findViewById(R.id.form_status_indicator);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.form_sub_item,
                viewGroup, false);
        return new ViewHolder(v);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.mName.setText(mProcessData.get(i).getName());
        viewHolder.mDate.setText(mProcessData.get(i).getMicroservice().getUpdatedAt());


        Drawable drawable;
        switch (status) {
            case FORM_STATUS_COMPLETED:
                drawable = mContext.getDrawable(R.drawable.form_status_indicator_completed);
                break;
            case FORM_STATUS_PENDING:
                viewHolder.mPinButton.setVisibility(View.GONE);
                drawable = mContext.getDrawable(R.drawable.form_status_indicator_pending);
                break;
            default:
                viewHolder.mPinButton.setVisibility(View.GONE);
                drawable = mContext.getDrawable(R.drawable.form_status_indicator_completed);
                break;
        }
        viewHolder.indicatorView.setBackground(drawable);
    }

    @Override
    public int getItemCount() {
        return mProcessData.size();
    }
}
