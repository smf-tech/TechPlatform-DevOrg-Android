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
import android.widget.Button;
import android.widget.Toast;

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

        View indicatorView;
        FloatingActionButton mPinButton;

        @SuppressLint("RestrictedApi")
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mPinButton = itemView.findViewById(R.id.pin_button);
            indicatorView = itemView.findViewById(R.id.form_status_indicator);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.form_sub_item, null);
        return new ViewHolder(v);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (i != 0) {
            viewHolder.mPinButton.setVisibility(View.GONE);
        }
        viewHolder.mPinButton.setOnClickListener(v -> {
            Toast.makeText(mContext, "Pin clicked!", Toast.LENGTH_SHORT).show();
        });

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
        return 3;
    }
}
