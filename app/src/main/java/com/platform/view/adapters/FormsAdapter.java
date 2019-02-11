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

import com.platform.R;
import com.platform.models.SavedForm;
import com.platform.models.pm.ProcessData;

import java.util.List;

import static com.platform.utility.Constants.Form.FORM_STATUS_COMPLETED;
import static com.platform.utility.Constants.Form.FORM_STATUS_PENDING;

@SuppressWarnings({"CanBeFinal", "SameParameterValue"})
class FormsAdapter extends RecyclerView.Adapter<FormsAdapter.ViewHolder> {

    private Context mContext;
    private List<SavedForm> mSavedForms;
    private String status;
    private List<ProcessData> mProcessData;

    FormsAdapter(Context context, String status, final List<ProcessData> processData) {
        this.mContext = context;
        this.status = status;
        mProcessData = processData;
    }

    FormsAdapter(final Context context, final List<SavedForm> savedForms, final String status) {
        this.mContext = context;
        this.status = status;
        mSavedForms = savedForms;
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

        Drawable drawable;
        switch (status) {
            case FORM_STATUS_COMPLETED:
                drawable = mContext.getDrawable(R.drawable.form_status_indicator_completed);
                break;
            case FORM_STATUS_PENDING:
                viewHolder.mPinButton.setVisibility(View.GONE);
                drawable = mContext.getDrawable(R.drawable.form_status_indicator_pending_forms);
                break;
            default:
                viewHolder.mPinButton.setVisibility(View.GONE);
                drawable = mContext.getDrawable(R.drawable.form_status_indicator_completed);
                break;
        }
        viewHolder.indicatorView.setBackground(drawable);

        if (mSavedForms == null) {
            viewHolder.mName.setText(mProcessData.get(i).getName());
            viewHolder.mDate.setText(mProcessData.get(i).getMicroservice().getUpdatedAt());

        } else {
            if (!mSavedForms.isEmpty()) {
                SavedForm savedForm = mSavedForms.get(i);
                viewHolder.mName.setText(savedForm.getFormName());
                viewHolder.mDate.setText(String.format("%s %s", savedForm.getFormId(), savedForm.getCreatedAt()));
            }

        }
    }

    @Override
    public int getItemCount() {
        if (mSavedForms != null && !mSavedForms.isEmpty() )
            return mSavedForms.size();
        return mProcessData.size();
    }
}
