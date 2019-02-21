package com.platform.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.forms.Choice;

import java.util.List;

public class FormSpinnerAdapter extends ArrayAdapter<Choice> {

    private final Context context;
    private final List<Choice> objects;

    public FormSpinnerAdapter(@NonNull Context context, int resourceId,
                              @NonNull List<Choice> objects) {

        super(context, resourceId, objects);
        this.context = context;
        this.objects = objects;
    }

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

        viewHolder.text.setText(objects.get(position).getText());

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

        viewHolder.text.setText(objects.get(position).getText());

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
        return objects.get(position);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    private class ViewHolder {
        private TextView text;
    }
}
