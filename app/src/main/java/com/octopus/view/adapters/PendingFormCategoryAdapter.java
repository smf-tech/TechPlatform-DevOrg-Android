package com.octopus.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.octopus.R;
import com.octopus.models.forms.FormResult;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"CanBeFinal", "unused"})
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

    class ViewHolder extends RecyclerView.ViewHolder {

        FloatingActionButton syncButton;
        FloatingActionButton addButton;
        RecyclerView recyclerView;
        TextView categoryName;

        @SuppressWarnings("unused")
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

        viewHolder.syncButton.hide();
        viewHolder.syncButton.setBackgroundColor(mContext.getResources().getColor(R.color.red));
        viewHolder.syncButton.setRippleColor(mContext.getResources().getColor(R.color.red));
        viewHolder.syncButton.setImageDrawable(mContext.getResources().getDrawable(android.R.drawable.stat_notify_sync));
        viewHolder.addButton.setColorFilter(mContext.getResources().getColor(R.color.white));

        viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        viewHolder.categoryName.setText(mCategoryList.get(i));

        List<FormResult> list = new ArrayList<>();
        for (final FormResult form : mData) {
            if (form.getFormCategory().equals(mCategoryList.get(i)))
                list.add(form);
        }

        PartiallySavedFormAdapter adapter = new PartiallySavedFormAdapter(mContext, list);
        viewHolder.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        mCategoryList.clear();
        for (final FormResult form : mData) {
            if (!mCategoryList.contains(form.getFormCategory())) {
                mCategoryList.add(form.getFormCategory());
            }
        }
        return mCategoryList.size();
    }

}