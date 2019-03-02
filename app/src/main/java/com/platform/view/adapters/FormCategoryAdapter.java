package com.platform.view.adapters;

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
import com.platform.models.common.Microservice;
import com.platform.models.pm.ProcessData;
import com.platform.syncAdapter.SyncAdapterUtils;
import com.platform.utility.Util;
import com.platform.view.fragments.CompletedFormsFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.platform.utility.Constants.Form.FORM_STATUS_COMPLETED;

@SuppressWarnings("CanBeFinal")
public class FormCategoryAdapter extends RecyclerView.Adapter<FormCategoryAdapter.ViewHolder> {

    private Context mContext;
    private Map<String, List<CompletedFormsFragment.ProcessDemoObject>> mFormData;

    public FormCategoryAdapter(final Context context, final Map<String, List<CompletedFormsFragment.ProcessDemoObject>> formsData) {
        this.mContext = context;
        mFormData = formsData;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        FloatingActionButton syncButton;
        FloatingActionButton addButton;
        RecyclerView recyclerView;
        FormsAdapter adapter;
        TextView categoryName;

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

    @SuppressWarnings("deprecation")
    @Override
    public void onBindViewHolder(@NonNull FormCategoryAdapter.ViewHolder viewHolder, int i) {

        viewHolder.syncButton.setBackgroundColor(mContext.getResources().getColor(R.color.red));
        viewHolder.syncButton.setRippleColor(mContext.getResources().getColor(R.color.red));
        viewHolder.syncButton.setImageDrawable(mContext.getResources().getDrawable(
                android.R.drawable.stat_notify_sync));
        viewHolder.addButton.setColorFilter(mContext.getResources().getColor(R.color.white));

        viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        String[] objects1 = new String[mFormData.keySet().size()];
        objects1 = mFormData.keySet().toArray(objects1);
        String category = objects1[i];

        if (category.equals("Syncing Pending")) {
            viewHolder.syncButton.show();
            viewHolder.syncButton.setOnClickListener(v -> {
                if (Util.isConnected(mContext)) {
                    Toast.makeText(mContext, "Sync started...", Toast.LENGTH_SHORT).show();
                    SyncAdapterUtils.manualRefresh();
                } else {
                    Toast.makeText(mContext, "Internet is not available!", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            viewHolder.syncButton.hide();
        }

        List<CompletedFormsFragment.ProcessDemoObject> list = mFormData.get(category);
        ArrayList<ProcessData> dataList = new ArrayList<>();

        if (list == null) return;

        for (CompletedFormsFragment.ProcessDemoObject object : list) {
            ProcessData data = new ProcessData();
            data.setName(object.getName());
            data.setId(object.getId());
            Microservice microservice = new Microservice();
            microservice.setUpdatedAt(object.getDate());
            data.setMicroservice(microservice);
            dataList.add(data);
        }

        viewHolder.categoryName.setText(category);
        viewHolder.adapter = new FormsAdapter(mContext, dataList);
        viewHolder.addButton.hide();
        viewHolder.recyclerView.setAdapter(viewHolder.adapter);
    }

    @Override
    public int getItemCount() {
        return mFormData.size();
    }
}