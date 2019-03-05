package com.platform.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.forms.FormResult;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.FormActivity;

import java.util.List;

@SuppressWarnings({"CanBeFinal", "SameParameterValue"})
class PartiallySavedFormAdapter extends RecyclerView.Adapter<PartiallySavedFormAdapter.ViewHolder> {

    private Context mContext;
    private List<FormResult> mSavedForms;

    PartiallySavedFormAdapter(final Context context, final List<FormResult> savedForms) {
        this.mContext = context;
        mSavedForms = savedForms;
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

        Drawable drawable = mContext.getDrawable(R.drawable.form_status_indicator_partial);
        viewHolder.indicatorView.setBackground(drawable);
        viewHolder.mPinButton.setVisibility(View.GONE);

        if (!mSavedForms.isEmpty()) {
            FormResult savedForm = mSavedForms.get(i);
            viewHolder.mName.setText(savedForm.getFormName());
            if (!TextUtils.isEmpty(Util.getDateFromTimestamp(savedForm.getCreatedAt()))) {
                String formattedDate = Util.getDateFromTimestamp(savedForm.getCreatedAt());
                viewHolder.mDate.setText(String.format("on %s", formattedDate));
            }
        }

        viewHolder.mRootView.setOnClickListener(v -> {
            String processID;
            String formID;

            if (mSavedForms != null && !mSavedForms.isEmpty()) {
                formID = mSavedForms.get(i).getFormId();
                processID = mSavedForms.get(i).get_id();

                Intent intent = new Intent(mContext, FormActivity.class);
                intent.putExtra(Constants.PM.PROCESS_ID, processID);
                intent.putExtra(Constants.PM.FORM_ID, formID);
                intent.putExtra(Constants.PM.EDIT_MODE, true);
                intent.putExtra(Constants.PM.PARTIAL_FORM, true);
                mContext.startActivity(intent);
            }

        });
    }

    @Override
    public int getItemCount() {
        return mSavedForms.size();
    }
}
