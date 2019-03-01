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
import com.platform.models.SavedForm;
import com.platform.models.forms.FormResult;
import com.platform.syncAdapter.SyncAdapterUtils;
import com.platform.utility.Util;

import java.util.ArrayList;
import java.util.List;

import static com.platform.utility.Constants.Form.FORM_STATUS_PENDING;
import static com.platform.utility.Util.saveFormCategoryForSync;

@SuppressWarnings("CanBeFinal")
public class PendingFormCategoryAdapter extends RecyclerView.Adapter<PendingFormCategoryAdapter.ViewHolder> {

    private Context mContext;
    private List<FormResult> mData;
    private ArrayList<String> mCategoryList;

    public PendingFormCategoryAdapter(final Context context, final List<FormResult> data) {
        this.mContext = context;
        mData = data;

        mCategoryList = new ArrayList<>();
        for (final FormResult form : mData) {
            if (!mCategoryList.contains(form.getFormCategory())) {
                mCategoryList.add(form.getFormCategory());
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        FloatingActionButton syncButton;
        FloatingActionButton addButton;
        RecyclerView recyclerView;
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
    public PendingFormCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.form_item, viewGroup, false);
        return new PendingFormCategoryAdapter.ViewHolder(v);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBindViewHolder(@NonNull PendingFormCategoryAdapter.ViewHolder viewHolder, int i) {

        viewHolder.syncButton.setOnClickListener(v -> {
            if (Util.isConnected(mContext)) {
                Toast.makeText(mContext, "Sync started...", Toast.LENGTH_SHORT).show();
                saveFormCategoryForSync(mCategoryList.get(i));
                SyncAdapterUtils.manualRefresh();
            } else {
                Toast.makeText(mContext, "Internet is not available!", Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.syncButton.setBackgroundColor(mContext.getResources().getColor(R.color.red));
        viewHolder.syncButton.setRippleColor(mContext.getResources().getColor(R.color.red));
        viewHolder.syncButton.setImageDrawable(mContext.getResources().getDrawable(android.R.drawable.stat_notify_sync));
        viewHolder.addButton.setColorFilter(mContext.getResources().getColor(R.color.white));

        viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        viewHolder.categoryName.setText(mCategoryList.get(i));

        List<FormResult> list = new ArrayList<>();
        for (final FormResult form : mData) {
            if (form.getFormCategory().equals(mCategoryList.get(i))) {
                list.add(form);
            }
        }

        FormsAdapter adapter = new FormsAdapter(mContext, list, FORM_STATUS_PENDING);
        viewHolder.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

}