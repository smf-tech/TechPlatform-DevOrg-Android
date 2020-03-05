package com.octopusbjsindia.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.pm.ProcessData;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.FormDisplayActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("CanBeFinal")
public class ExpandableAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private Map<String, List<ProcessData>> mFormsData;
    private Map<String, String> mCountList;

    public ExpandableAdapter(Context context, final Map<String, List<ProcessData>> formsData,
                             final Map<String, String> countList) {

        this.mContext = context;
        this.mFormsData = formsData;
        this.mCountList = countList;
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
        if (processData != null) {
            return processData.size();
        }

        return 0;
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
    public View getGroupView(final int groupPosition, final boolean isExpanded,
                             final View convertView, final ViewGroup parent) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_all_forms_item,
                parent, false);

        ArrayList<String> list = new ArrayList<>(mFormsData.keySet());
        String cat = list.get(groupPosition);

        List<ProcessData> processData = mFormsData.get(cat);
        int size = 0;
        if (processData != null) {
            size = processData.size();
        }

        ((TextView) view.findViewById(R.id.form_title)).setText(cat);
        ((TextView) view.findViewById(R.id.form_count))
                .setText(String.format("%s %s", String.valueOf(size), mContext.getString(R.string.forms)));

        ImageView v = view.findViewById(R.id.form_image);
        if (isExpanded) {
            Util.rotateImage(180f, v);
        } else {
            Util.rotateImage(0f, v);
        }

        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             final boolean isLastChild, final View convertView, final ViewGroup parent) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_all_form_sub_item, parent, false);

        ArrayList<String> list = new ArrayList<>(mFormsData.keySet());
        String cat = list.get(groupPosition);

        List<ProcessData> processData = mFormsData.get(cat);
        if (processData != null) {
            ProcessData data = processData.get(childPosition);

            ((TextView) view.findViewById(R.id.form_title)).setText(data.getName().getLocaleValue().trim());

            if (groupPosition < mCountList.size()) {
                String count = mCountList.get(data.getId());
                if (count == null) count = "0";

                ((TextView) view.findViewById(R.id.submitted_count_label))
                        .setText(mContext.getString(R.string.submitted_form_count, count));
            } else {
                ((TextView) view.findViewById(R.id.submitted_count_label))
                        .setText(mContext.getString(R.string.submitted_form_count, "0"));
            }

            //TODO: Fix submitted count issue and change visibility
            view.findViewById(R.id.submitted_count_label).setVisibility(View.GONE);

            ImageButton addButton = view.findViewById(R.id.add_form_button);
            addButton.setEnabled(Util.isUserApproved());

            addButton.setOnClickListener(v -> {
//                Intent intent = new Intent(mContext, FormActivity.class);
//                intent.putExtra(Constants.PM.FORM_ID, data.getId());
//                mContext.startActivity(intent);

                Intent intent = new Intent(mContext, FormDisplayActivity.class);
                if (childPosition == 0) {
                    intent.putExtra("Weblink", "http://13.235.124.3:8060/f1p1/dc3b0648-97bc-4107-89cc-3a74f1b4260f");
                } else {
                    intent.putExtra("Weblink", "http://13.235.124.3:8060/f2p1/1ae0ed89-adec-43e7-a8bd-eef3912d2afc");
                }
                intent.putExtra(Constants.PM.FORM_ID, data.getId());
                mContext.startActivity(intent);
            });
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(final int groupPosition, final int childPosition) {
        return false;
    }
}
