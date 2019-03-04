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
import android.widget.ImageView;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.pm.ProcessData;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.FormActivity;

import java.util.List;

import static com.platform.utility.Constants.FORM_DATE_FORMAT;

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
        ImageView mFormImage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mRootView = itemView;
            mName = itemView.findViewById(R.id.form_title);
            mDate = itemView.findViewById(R.id.form_date);
            mPinButton = itemView.findViewById(R.id.pin_button);
            indicatorView = itemView.findViewById(R.id.form_status_indicator);
            mFormImage = itemView.findViewById(R.id.form_image);
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

        // Set Indicator and icon
        Drawable drawable = mContext.getDrawable(R.drawable.form_status_indicator_completed);
        if (mPendingFormCategory) {
            viewHolder.mFormImage.setBackgroundResource(R.drawable.ic_pending_forms);
            drawable = mContext.getDrawable(R.drawable.form_status_indicator_pending_forms);
        } else {
            viewHolder.mFormImage.setBackgroundResource(R.drawable.ic_menu_forms);
        }
        viewHolder.indicatorView.setBackground(drawable);

        ProcessData processData = mProcessData.get(i);
        viewHolder.mName.setText(processData.getName());

        if (!processData.getName().equals(mContext.getString(R.string.forms_are_not_available))) {
            if (processData.getMicroservice() != null
                    && processData.getMicroservice().getUpdatedAt() != null) {
                String formattedDate = Util.getFormattedDate(processData.getMicroservice().getUpdatedAt(), FORM_DATE_FORMAT);
                viewHolder.mDate.setText(String.format("on %s", formattedDate));
            }
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
