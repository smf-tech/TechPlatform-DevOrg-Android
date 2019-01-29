package com.platform.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.platform.R;

import static com.platform.utility.Constants.Form.FORM_STATUS_ALL;
import static com.platform.utility.Constants.Form.FORM_STATUS_COMPLETED;
import static com.platform.utility.Constants.Form.FORM_STATUS_PENDING;

public class FormCategoryAdapter extends RecyclerView.Adapter<FormCategoryAdapter.ViewHolder> {

    private Context mContext;
    private String status;

    public FormCategoryAdapter(Context context, String status) {
        this.mContext = context;
        this.status = status;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            RecyclerView recyclerView = itemView.findViewById(R.id.forms_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.hasFixedSize();
            FormsAdapter adapter;

            switch (status) {
                case FORM_STATUS_COMPLETED:
                    adapter = new FormsAdapter(mContext, FORM_STATUS_COMPLETED);
                    break;
                case FORM_STATUS_PENDING:
                    adapter = new FormsAdapter(mContext, FORM_STATUS_PENDING);
                    break;
                default:
                    adapter = new FormsAdapter(mContext, FORM_STATUS_ALL);
                    break;
            }
            recyclerView.setAdapter(adapter);
        }
    }

    @NonNull
    @Override
    public FormCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.form_item, null);
        return new FormCategoryAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FormCategoryAdapter.ViewHolder viewHolder, int i) {
        // FIXME: 29-01-2019 Add logic here
    }

    @Override
    public int getItemCount() {
        // FIXME: 29-01-2019 Remove hardcoded count
        return 2;
    }
}