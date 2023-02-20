package com.octopusbjsindia.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.pm.ProcessData;

import java.util.List;
import java.util.Map;

@SuppressWarnings("CanBeFinal")
public class PMAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader;
    private Map<String, List<ProcessData>> listDataChild;
    private OnProcessListItemClickListener listener;

    public PMAdapter(Context context, List<String> headerList, Map<String,
            List<ProcessData>> childDataList, OnProcessListItemClickListener listener) {

        this.context = context;
        this.listDataHeader = headerList;
        this.listDataChild = childDataList;
        this.listener = listener;
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int childPosition) {
        List<ProcessData> processData = this.listDataChild.get(this.listDataHeader.get(childPosition));
        if (processData != null) {
            return processData.size();
        }

        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<ProcessData> processData = this.listDataChild.get(this.listDataHeader.get(groupPosition));
        if (processData != null) {
            return processData.get(childPosition);
        }

        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater =
                    (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater != null ?
                    layoutInflater.inflate(R.layout.layout_group_list, parent, false) : null;
        }

        if (convertView != null) {
            ImageView arrowIndicator = convertView.findViewById(R.id.group_list_arrow);
            if (isExpanded) {
                arrowIndicator.setImageResource(R.drawable.ic_down_arrow_light_blue);
            } else {
                arrowIndicator.setImageResource(R.drawable.ic_arrow_forward_24);
            }

            String headerTitle = (String) getGroup(groupPosition);
            TextView txtName = convertView.findViewById(R.id.group_list_header);
            txtName.setText(headerTitle);
        }

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        ProcessData process = (ProcessData) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater =
                    (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater != null ?
                    layoutInflater.inflate(R.layout.layout_each_process, parent, false) : null;
        }

        if (convertView != null) {
            TextView processName = convertView.findViewById(R.id.pm_process_name_text);
            processName.setText(process.getName().getLocaleValue());
            processName.setOnClickListener(view ->
                    listener.onProcessListItemClickListener(process));
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public interface OnProcessListItemClickListener {
        void onProcessListItemClickListener(ProcessData process);
    }
}
