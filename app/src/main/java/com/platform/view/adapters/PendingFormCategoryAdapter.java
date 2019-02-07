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
import com.platform.models.SavedForm;

import java.util.List;

@SuppressWarnings("CanBeFinal")
public class PendingFormCategoryAdapter extends RecyclerView.Adapter<PendingFormCategoryAdapter.ViewHolder> {

    private Context mContext;
    private List<SavedForm> mData;

    public PendingFormCategoryAdapter(final Context context, final List<SavedForm> data) {
        this.mContext = context;
        mData = data;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        FloatingActionButton syncButton;
        FloatingActionButton addButton;
        RecyclerView recyclerView;
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
    public PendingFormCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.form_item, viewGroup, false);
        return new PendingFormCategoryAdapter.ViewHolder(v);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull PendingFormCategoryAdapter.ViewHolder viewHolder, int i) {

        viewHolder.syncButton.setOnClickListener(v ->
                Toast.makeText(mContext, "Sync clicked!", Toast.LENGTH_SHORT).show());

        viewHolder.syncButton.setBackgroundColor(mContext.getResources().getColor(R.color.red));
        viewHolder.syncButton.setRippleColor(mContext.getResources().getColor(R.color.red));
        viewHolder.syncButton.setImageDrawable(mContext.getResources().getDrawable(android.R.drawable.stat_notify_sync));
        viewHolder.addButton.setColorFilter(mContext.getResources().getColor(R.color.white));

        viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        /*String[] objects1 = new String[mFormsData.keySet().size()];
        objects1 = mFormsData.keySet().toArray(objects1);
        String category = objects1[i];

        List<ProcessData> processData = mFormsData.get(category);

        viewHolder.categoryName.setText(category);

        viewHolder.adapter = new FormsAdapter(mContext, mData);
        viewHolder.addButton.setVisibility(View.GONE);
        viewHolder.recyclerView.setAdapter(viewHolder.adapter);*/

        viewHolder.categoryName.setText("");

        FormsAdapter adapter = new FormsAdapter(mContext, mData);
        viewHolder.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}