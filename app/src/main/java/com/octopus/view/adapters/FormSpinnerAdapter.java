package com.octopus.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.octopus.R;
import com.octopus.models.forms.Choice;

import java.util.ArrayList;
import java.util.List;

public class FormSpinnerAdapter extends ArrayAdapter<Choice> implements Filterable {

    private final Context context;
    private final List<Choice> originalObjects;
    private List<Choice> filteredObjects;
    private Filter filter;

    public FormSpinnerAdapter(@NonNull Context context, int resourceId,
                              @NonNull List<Choice> objects) {

        super(context, resourceId, objects);
        this.context = context;
        this.originalObjects = objects;
        this.filteredObjects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater mInflater = ((Activity) context).getLayoutInflater();
            convertView = mInflater.inflate(R.layout.layout_spinner_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.text = convertView.findViewById(R.id.dropdown_list_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.text.setText(filteredObjects.get(position).getText().getLocaleValue());

        ViewGroup.LayoutParams p = convertView.getLayoutParams();
        p.height = 100; // set the height
        convertView.setLayoutParams(p);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater mInflater = ((Activity) context).getLayoutInflater();
            convertView = mInflater.inflate(R.layout.layout_spinner_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.text = convertView.findViewById(R.id.dropdown_list_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.text.setText(filteredObjects.get(position).getText().getLocaleValue());

        ViewGroup.LayoutParams p = convertView.getLayoutParams();
        p.height = 100; // set the height
        convertView.setLayoutParams(p);

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Nullable
    @Override
    public Choice getItem(int position) {
        return filteredObjects.get(position);
    }

    @Override
    public int getCount() {
        if (filteredObjects != null && !filteredObjects.isEmpty()) {
            return filteredObjects.size();
        } else {
            return 0;
        }
    }

    private class ViewHolder {
        private TextView text;
    }

    private class ChoiceFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String keyword = "";
            if (constraint != null && constraint.toString().length() > 0) {
                keyword = constraint.toString().toLowerCase();
            }
            FilterResults results = new FilterResults();
            if (!TextUtils.isEmpty(keyword)) {
                final List<Choice> list = originalObjects;
                int count = list.size();
                final ArrayList<Choice> finalList = new ArrayList<>(count);
                Choice filterableChoice;
                for (int index = 0; index < count; index++) {
                    filterableChoice = list.get(index);
                    if (filterableChoice.getText().getLocaleValue().toLowerCase().contains(keyword)) {
                        finalList.add(filterableChoice);
                    }
                }
                results.values = finalList;
                results.count = finalList.size();
            } else {
                synchronized (this) {
                    results.values = originalObjects;
                    results.count = originalObjects.size();
                }
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredObjects = (ArrayList<Choice>) results.values;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new ChoiceFilter();

        return filter;
    }

    public void setData(List<Choice> filteredObjects) {
        this.filteredObjects = filteredObjects;
    }

    public List<Choice> getOriginalObjects() {
        return originalObjects;
    }
}