package com.platform.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.platform.R;

import static com.platform.utility.Constants.Form.FORM_STATUS_ALL;
import static com.platform.utility.Constants.Form.FORM_STATUS_COMPLETED;
import static com.platform.utility.Constants.Form.FORM_STATUS_PENDING;

public class FormCategoryAdapter extends RecyclerView.Adapter<FormCategoryAdapter.ViewHolder> {

    private Context mContext;
    private String mStatus;
    private String mCategoryName;

    public FormCategoryAdapter(Context context, String mStatus, final String categoryName) {
        this.mContext = context;
        this.mStatus = mStatus;
        mCategoryName = categoryName;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        FloatingActionButton syncButton;
        FloatingActionButton addButton;
        RecyclerView recyclerView;
        FormsAdapter adapter;
        TextView categoryName;

        @SuppressLint("RestrictedApi")
        ViewHolder(@NonNull View itemView) {
            super(itemView);

            syncButton = itemView.findViewById(R.id.sync_button);
            addButton = itemView.findViewById(R.id.add_button);
            categoryName = itemView.findViewById(R.id.form_title);
            recyclerView = itemView.findViewById(R.id.forms);

        }
    }

    @NonNull
    @Override
    public FormCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.form_item, viewGroup, false);
        return new FormCategoryAdapter.ViewHolder(v);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull FormCategoryAdapter.ViewHolder viewHolder, int i) {
        viewHolder.syncButton.setOnClickListener(v ->
                Toast.makeText(mContext, "Sync clicked!", Toast.LENGTH_SHORT).show());
        viewHolder.syncButton.setBackgroundColor(mContext.getResources().getColor(R.color.red));
        viewHolder.syncButton.setRippleColor(mContext.getResources().getColor(R.color.red));
        viewHolder.syncButton.setImageDrawable(mContext.getResources().getDrawable(android.R.drawable.stat_notify_sync));
        viewHolder.addButton.setColorFilter(mContext.getResources().getColor(R.color.white));

        viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        viewHolder.categoryName.setText(mCategoryName);

        switch (mStatus) {
            case FORM_STATUS_COMPLETED:
                viewHolder.adapter = new FormsAdapter(mContext, FORM_STATUS_COMPLETED);
                viewHolder.addButton.setVisibility(View.GONE);
                viewHolder.syncButton.setVisibility(View.GONE);
                break;
            case FORM_STATUS_PENDING:
                viewHolder.adapter = new FormsAdapter(mContext, FORM_STATUS_PENDING);
                break;
            default:
                viewHolder.adapter = new FormsAdapter(mContext, FORM_STATUS_ALL);
                viewHolder.addButton.setVisibility(View.GONE);
                viewHolder.syncButton.setVisibility(View.GONE);
                break;
        }
        viewHolder.recyclerView.setAdapter(viewHolder.adapter);
    }

    @Override
    public int getItemCount() {
        // FIXME: 29-01-2019 Remove hardcoded count
        return 1;
    }
}