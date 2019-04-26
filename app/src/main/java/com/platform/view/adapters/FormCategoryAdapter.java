package com.platform.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.platform.R;
import com.platform.models.LocaleData;
import com.platform.models.pm.ProcessData;
import com.platform.syncAdapter.SyncAdapterUtils;
import com.platform.utility.Util;
import com.platform.view.fragments.CompletedFormsFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        boolean pendingFormCategory = false;
        if (category.equals(mContext.getString(R.string.syncing_pending))) {
            pendingFormCategory = true;
            viewHolder.syncButton.show();
            viewHolder.syncButton.setOnClickListener(v -> {
                if (Util.isConnected(mContext)) {
                    Toast.makeText(mContext, R.string.sync_started, Toast.LENGTH_SHORT).show();
                    SyncAdapterUtils.manualRefresh();
                } else {
                    Toast.makeText(mContext, R.string.msg_no_network, Toast.LENGTH_SHORT).show();
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
            LocaleData localeData = new LocaleData(object.getName());
            localeData.setLocaleValue(object.getName());
            data.setName(localeData);
            data.setId(object.getId());
            data.setFormTitle(object.getFormTitle());
            dataList.add(data);
        }

        viewHolder.categoryName.setText(category);
        viewHolder.adapter = new FormsAdapter(mContext, dataList, pendingFormCategory);
        viewHolder.addButton.hide();
        viewHolder.recyclerView.setAdapter(viewHolder.adapter);
    }

    @Override
    public int getItemCount() {
        return mFormData.size();
    }
}