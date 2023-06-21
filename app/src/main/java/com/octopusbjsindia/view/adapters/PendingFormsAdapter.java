package com.octopusbjsindia.view.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.models.forms.FormResult;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.FormActivity;
import com.octopusbjsindia.view.fragments.PMFragment;

import java.util.List;

@SuppressWarnings({"CanBeFinal", "unused"})
public class PendingFormsAdapter extends RecyclerView.Adapter<PendingFormsAdapter.TMViewHolder> {

    private Context context;
    private List<FormResult> savedFormList;
    private PMFragment mFragment;

    public PendingFormsAdapter(Context context, List<FormResult> savedFormList, PMFragment fragment) {
        this.context = context;
        this.savedFormList = savedFormList;
        this.mFragment = fragment;
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
        holder.txtCreatedAt.setText(String.format("%s",
                Util.getDateTimeFromTimestamp(savedForm.getCreatedAt())));

        holder.delete.setOnClickListener(v -> showFormDeletePopUp(savedForm));

        holder.mRootView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FormActivity.class);
            intent.putExtra(Constants.PM.PROCESS_ID, savedForm.get_id());
            intent.putExtra(Constants.PM.FORM_ID, savedForm.getFormId());
            intent.putExtra(Constants.PM.EDIT_MODE, true);
            intent.putExtra(Constants.PM.PARTIAL_FORM, true);
            context.startActivity(intent);
        });
    }

    private void deleteSavedForm(FormResult savedForm) {
        DatabaseManager.getDBInstance(context).deleteFormResult(savedForm);
        savedFormList.remove(savedForm);
        notifyDataSetChanged();
        Util.showToast(context.getString(R.string.form_deleted), context);

        //mFragment.onFormDeletedListener();
    }

    private void showFormDeletePopUp(FormResult savedForm) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        // Setting Dialog Title
        alertDialog.setTitle(context.getString(R.string.app_name_ss));
        // Setting Dialog Message
        alertDialog.setMessage(context.getString(R.string.msg_delete_saved_form));
        // Setting Icon to Dialog
        alertDialog.setIcon(R.mipmap.ic_launcher);
        // Setting CANCEL Button
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.cancel),
                (dialog, which) -> alertDialog.dismiss());
        // Setting OK Button
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.ok),
                (dialog, which) -> deleteSavedForm(savedForm));

        // Showing Alert Message
        alertDialog.show();
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
