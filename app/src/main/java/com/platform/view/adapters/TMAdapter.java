package com.platform.view.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.models.common.Template;
import com.platform.view.activities.TMActivity;
import com.platform.view.activities.TMUserProfileListActivity;

import java.util.List;

@SuppressWarnings("CanBeFinal")
public class TMAdapter extends RecyclerView.Adapter<TMAdapter.TMViewHolder> {

    private Activity context;
    private List<Template> templateList;

    public TMAdapter(Activity context, List<Template> templateList) {
        this.context = context;
        this.templateList = templateList;
    }

    @NonNull
    @Override
    public TMViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_each_template, parent, false);

        return new TMViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TMViewHolder holder, int position) {
        Template template = templateList.get(position);
        holder.templateName.setText(template.getName());

        if (context instanceof TMUserProfileListActivity) {
            if (template.getStatus() != null) {
                switch (template.getStatus()) {
                    case "true":
                        holder.templateIndicator.setBackgroundColor(
                                context.getResources().getColor(R.color.green));
                        holder.templateIndicator.setVisibility(View.VISIBLE);
                        break;

                    case "false":
                        holder.templateIndicator.setBackgroundColor(
                                context.getResources().getColor(R.color.purple));
                        holder.templateIndicator.setVisibility(View.VISIBLE);
                        break;

                    case "Rejected":
                        holder.templateIndicator.setBackgroundColor(
                                context.getResources().getColor(R.color.red));
                        holder.templateIndicator.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return templateList.size();
    }

    class TMViewHolder extends RecyclerView.ViewHolder {

        TextView templateName;
        TextView templateIndicator;
        LinearLayout templateLayout;

        TMViewHolder(View view) {
            super(view);

            templateName = view.findViewById(R.id.template_name);
            templateIndicator = view.findViewById(R.id.template_color);

            templateLayout = view.findViewById(R.id.template_layout);
            templateLayout.setOnClickListener(view1 -> {
                if (context instanceof TMActivity) {
                    if (getAdapterPosition() == 0) {
                        Intent intent = new Intent(context, TMUserProfileListActivity.class);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
