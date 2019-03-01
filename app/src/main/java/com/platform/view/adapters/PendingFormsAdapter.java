package com.platform.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.forms.FormResult;

import java.util.List;

@SuppressWarnings({"CanBeFinal", "unused"})
public class PendingFormsAdapter extends RecyclerView.Adapter<PendingFormsAdapter.TMViewHolder> {

    @SuppressWarnings("FieldCanBeLocal")
    private Context context;
    private List<FormResult> savedFormList;

    public PendingFormsAdapter(Context context, List<FormResult> savedFormList) {
        this.context = context;
        this.savedFormList = savedFormList;
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
        holder.txtCreatedAt.setText(String.format("on %s", savedForm.getCreatedAt()));
    }

    @Override
    public int getItemCount() {
        return savedFormList.size();
    }

    class TMViewHolder extends RecyclerView.ViewHolder {

        TextView txtFormName;
        TextView txtCreatedAt;

        TMViewHolder(View view) {
            super(view);

            txtFormName = view.findViewById(R.id.txt_dashboard_pending_form_title);
            txtCreatedAt = view.findViewById(R.id.txt_dashboard_pending_form_created_at);
        }
    }
}
