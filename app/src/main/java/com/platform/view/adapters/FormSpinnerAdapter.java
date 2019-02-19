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

import java.util.List;

public class FormSpinnerAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final List<String> objects;

    public FormSpinnerAdapter(@NonNull Context context, int resourceId, int textViewId,
                              @NonNull List<String> objects) {

        super(context, resourceId, textViewId, objects);
        this.context = context;
        this.objects = objects;
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

        viewHolder.text.setText(objects.get(position));

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
    public String getItem(int position) {
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
