package com.platform.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.models.forms.FormResult;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.FormActivity;
import com.platform.view.fragments.PMFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

@SuppressWarnings({"CanBeFinal", "unused"})
public class PendingFormsAdapter extends RecyclerView.Adapter<PendingFormsAdapter.TMViewHolder> {

    @SuppressWarnings("FieldCanBeLocal")
    private Context context;
    private List<FormResult> savedFormList;
    private PMFragment mFragment;

    public PendingFormsAdapter(Context context, List<FormResult> savedFormList, PMFragment fragment) {
        this.context = context;
        this.savedFormList = savedFormList;
        mFragment = fragment;
    }

    @NonNull
    @Override
    public TMViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_dashboard_pending_forms_card_view, parent, false);

        return new TMViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TMViewHolder holder, int position) {
        FormResult savedForm = savedFormList.get(position);
        holder.txtFormName.setText(savedForm.getFormName());
        holder.txtCreatedAt.setText(String.format("on %s",
                Util.getDateFromTimestamp(savedForm.getCreatedAt())));

        holder.delete.setOnClickListener(v -> {
            DatabaseManager.getDBInstance(context).deleteFormResult(savedForm);
            savedFormList.remove(savedForm);
            notifyDataSetChanged();
            Util.showToast(context.getString(R.string.form_deleted), context);

            mFragment.onFormDeletedListener();
        });

        holder.mRootView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FormActivity.class);
            intent.putExtra(Constants.PM.PROCESS_ID, savedForm.get_id());
            intent.putExtra(Constants.PM.FORM_ID, savedForm.getFormId());
            intent.putExtra(Constants.PM.EDIT_MODE, true);
            intent.putExtra(Constants.PM.PARTIAL_FORM, true);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return savedFormList.size();
    }

    class TMViewHolder extends RecyclerView.ViewHolder {

        TextView txtFormName;
        TextView txtCreatedAt;
        ImageView delete;
        View mRootView;

        TMViewHolder(View view) {
            super(view);

            mRootView = view;
            delete = view.findViewById(R.id.iv_dashboard_delete_form);
            txtFormName = view.findViewById(R.id.txt_dashboard_pending_form_title);
            txtCreatedAt = view.findViewById(R.id.txt_dashboard_pending_form_created_at);
        }
    }

    public interface FormListener {
        void onFormDeletedListener();
    }
}
