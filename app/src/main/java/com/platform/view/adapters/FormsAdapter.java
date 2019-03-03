package com.platform.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.pm.ProcessData;
import com.platform.utility.Constants;
import com.platform.view.activities.FormActivity;

import java.util.List;

import static com.platform.utility.Util.getFormattedDate;

@SuppressWarnings({"CanBeFinal", "SameParameterValue"})
class FormsAdapter extends RecyclerView.Adapter<FormsAdapter.ViewHolder> {

    private Context mContext;
    private List<ProcessData> mProcessData;
    private boolean mPendingFormCategory;

    FormsAdapter(Context context, final List<ProcessData> processData, final boolean pendingFormCategory) {
        this.mContext = context;
        mProcessData = processData;
        mPendingFormCategory = pendingFormCategory;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View mRootView;
        TextView mName;
        TextView mDate;
        View indicatorView;
        ImageButton mPinButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mRootView = itemView;
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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.mPinButton.setVisibility(View.GONE);
        Drawable drawable = mContext.getDrawable(R.drawable.form_status_indicator_completed);
        if (mPendingFormCategory) {
            drawable = mContext.getDrawable(R.drawable.form_status_indicator_pending_forms);
        }
        viewHolder.indicatorView.setBackground(drawable);

        ProcessData processData = mProcessData.get(i);
        viewHolder.mName.setText(processData.getName());

        if (!processData.getName().equals(mContext.getString(R.string.forms_are_not_available))) {
            String formattedDate = getFormattedDate(processData.getMicroservice().getUpdatedAt());
            viewHolder.mDate.setText(String.format("on %s", formattedDate));
        } else {
            viewHolder.mDate.setVisibility(View.GONE);
        }

        viewHolder.mRootView.setOnClickListener(v -> {
            String processID;
            String formID;

            if (mProcessData == null || i >= mProcessData.size() || mProcessData.get(i).getId().equals("0")) {
                return;
            }

            formID = mProcessData.get(i).getName();
            processID = mProcessData.get(i).getId();

            Intent intent = new Intent(mContext, FormActivity.class);
            intent.putExtra(Constants.PM.PROCESS_ID, processID);
            intent.putExtra(Constants.PM.FORM_ID, formID);
            intent.putExtra(Constants.PM.EDIT_MODE, true);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mProcessData.size();
    }
}
