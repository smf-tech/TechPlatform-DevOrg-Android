package com.platform.view.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.platform.R;

import static com.platform.utility.Constants.Form.FORM_STATUS_COMPLETED;
import static com.platform.utility.Constants.Form.FORM_STATUS_PENDING;

class FormsAdapter extends RecyclerView.Adapter<FormsAdapter.ViewHolder> {

    private Context mContext;
    private String status;

    public FormsAdapter(Context context, String status) {
        this.mContext = context;
        this.status = status;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            View indicatorView = itemView.findViewById(R.id.form_status_indicator);
            Drawable drawable;
            switch (status) {
                case FORM_STATUS_COMPLETED:
                    drawable = mContext.getDrawable(R.drawable.form_status_indicator_completed);
                    break;
                case FORM_STATUS_PENDING:
                    drawable = mContext.getDrawable(R.drawable.form_status_indicator_pending);
                    break;
                default:
                    drawable = mContext.getDrawable(R.drawable.form_status_indicator_completed);
                    break;
            }
            indicatorView.setBackground(drawable);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.form_sub_item, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
