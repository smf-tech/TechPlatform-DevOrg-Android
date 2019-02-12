package com.platform.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.pm.ProcessData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("CanBeFinal")
public class ExpandableAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private Map<String, List<ProcessData>> mFormsData;

    public ExpandableAdapter(Context context, final Map<String, List<ProcessData>> formsData) {
        this.mContext = context;
        mFormsData = formsData;
    }

    @Override
    public int getGroupCount() {
        return mFormsData.size();
    }

    @Override
    public int getChildrenCount(final int groupPosition) {
        ArrayList<String> list = new ArrayList<>(mFormsData.keySet());
        String cat = list.get(groupPosition);
        List<ProcessData> processData = mFormsData.get(cat);
        return processData.size();
    }

    @Override
    public Object getGroup(final int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(final int groupPosition, final int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(final int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(final int groupPosition, final int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, final View convertView, final ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.all_forms_item, parent, false);

        ArrayList<String> list = new ArrayList<>(mFormsData.keySet());
        String cat = list.get(groupPosition);
        List<ProcessData> processData = mFormsData.get(cat);
        int size = processData.size();

        ((TextView) view.findViewById(R.id.form_title)).setText(cat);
        ((TextView) view.findViewById(R.id.form_count)).setText(String.format("%s Forms", String.valueOf(size)));

        ImageView v = view.findViewById(R.id.form_image);
        if (isExpanded) {
            rotate(90f, v);
        } else {
            rotate(0f, v);
        }

        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, final boolean isLastChild, final View convertView, final ViewGroup parent) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.all_form_sub_item, parent, false);

        ArrayList<String> list = new ArrayList<>(mFormsData.keySet());
        String cat = list.get(groupPosition);
        List<ProcessData> processData = mFormsData.get(cat);
        ProcessData data = processData.get(childPosition);

        ((TextView) view.findViewById(R.id.form_title)).setText(data.getName());

        return view;
    }

    @Override
    public boolean isChildSelectable(final int groupPosition, final int childPosition) {
        return true;
    }

    private void rotate(float degree, ImageView image) {
        final RotateAnimation rotateAnim = new RotateAnimation(0.0f, degree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        rotateAnim.setDuration(0);
        rotateAnim.setFillAfter(true);
        image.startAnimation(rotateAnim);
    }
}
