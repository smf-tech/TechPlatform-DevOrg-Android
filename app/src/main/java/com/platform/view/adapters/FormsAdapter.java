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
import com.platform.models.SavedForm;
import com.platform.models.pm.ProcessData;
import com.platform.utility.Constants;
import com.platform.view.activities.FormActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.platform.utility.Constants.Form.FORM_STATUS_COMPLETED;
import static com.platform.utility.Constants.Form.FORM_STATUS_PENDING;
import static com.platform.utility.Util.getFormattedDate;

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
            ProcessData processData = mProcessData.get(i);
            viewHolder.mName.setText(processData.getName());

            if (!processData.getName().equals(mContext.getString(R.string.forms_are_not_available))) {
                SimpleDateFormat createdDateFormat = getFormattedDate();
                String formattedDate = createdDateFormat.format(new Date());
                viewHolder.mDate.setText(String.format("on %s", formattedDate));
            } else {
                viewHolder.mDate.setVisibility(View.GONE);
            }

        } else {
            if (!mSavedForms.isEmpty()) {
                SavedForm savedForm = mSavedForms.get(i);
                viewHolder.mName.setText(savedForm.getFormName());
                viewHolder.mDate.setText(String.format("%s %s", savedForm.getFormId(), savedForm.getCreatedAt()));
            }

        }

        viewHolder.mRootView.setOnClickListener(v -> {
            if (i >= mProcessData.size() || mProcessData.get(i).getId().equals("0")) return;

            Intent intent = new Intent(mContext, FormActivity.class);
            intent.putExtra(Constants.PM.PROCESS_ID, mProcessData.get(i).getId());
            intent.putExtra(Constants.PM.FORM_ID, mProcessData.get(i).getName());
            intent.putExtra(Constants.PM.EDIT_MODE, true);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (mSavedForms != null && !mSavedForms.isEmpty())
            return mSavedForms.size();
        return mProcessData.size();
    }
}
