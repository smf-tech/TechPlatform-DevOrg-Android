package com.octopus.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.octopus.R;
import com.octopus.models.pm.ProcessData;
import com.octopus.utility.Constants;
import com.octopus.utility.Util;
import com.octopus.view.activities.FormActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("CanBeFinal")
public class SubmittedFormsListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private Map<String, List<ProcessData>> mMap;
    //private ArrayList<String> processStatus;
    private HashMap<String, String> processSyncStatusHashmap;

    public SubmittedFormsListAdapter(final Context context, final Map<String, List<ProcessData>> map, HashMap<String, String> processSyncStatusHashmap) {
        this.mContext = context;
        this.mMap = map;
        this.processSyncStatusHashmap = processSyncStatusHashmap;
    }

    @Override
    public int getGroupCount() {
        return mMap.size();
    }

    @Override
    public int getChildrenCount(final int groupPosition) {
        ArrayList<String> list = new ArrayList<>(mMap.keySet());
        String cat = list.get(groupPosition);

        List<ProcessData> formResults = mMap.get(cat);
        if (formResults != null) {
            return formResults.size();
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

        ArrayList<String> list = new ArrayList<>(mMap.keySet());
        String cat = list.get(groupPosition);

        List<ProcessData> processData = mMap.get(cat);
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
        if(processSyncStatusHashmap.get(cat).equalsIgnoreCase(Constants.PM.UNSYNC_STATUS)){
            view.findViewById(R.id.form_status_icon).setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             final boolean isLastChild, final View convertView, final ViewGroup parent) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.row_dashboard_pending_forms_card_view,
                parent, false);

        ArrayList<String> list = new ArrayList<>(mMap.keySet());
        String cat = list.get(groupPosition);
        List<ProcessData> processData = mMap.get(cat);

        ProcessData data = null;
        if (processData != null) {
            data = processData.get(childPosition);

            ((TextView) view.findViewById(R.id.txt_dashboard_pending_form_title))
                    .setText(data.getName().getLocaleValue());

            if (data.getMicroservice() != null && data.getMicroservice().getUpdatedAt() != null) {
                ((TextView) view.findViewById(R.id.txt_dashboard_pending_form_created_at))
                        .setText(Util.getDateTimeFromTimestamp(data.getMicroservice().getUpdatedAt()));
            }
        }

        final ProcessData finalFormResult = data;
        ProcessData finalData = data;
        view.setOnClickListener(v -> {
            if (Util.isUserApproved()) {
                if (finalFormResult != null) {
                    final String formID = finalFormResult.getId();
                    final String processID = finalFormResult.getMicroservice().getId();

                    Intent intent = new Intent(mContext, FormActivity.class);
                    intent.putExtra(Constants.PM.PROCESS_ID, formID);
                    intent.putExtra(Constants.PM.FORM_ID, processID);
                    intent.putExtra(Constants.PM.EDIT_MODE, true);
                    intent.putExtra(Constants.PM.PARTIAL_FORM, false);

//                    if (cat.equals(mContext.getString(R.string.syncing_pending))) {
//                        intent.putExtra(Constants.PM.FORM_ID, formID);
//                        intent.putExtra(Constants.PM.PROCESS_ID, processID);
//                        intent.putExtra(Constants.PM.PARTIAL_FORM, true);
//                    }
                    if(finalData.getFormApprovalStatus()!=null && finalData.getFormApprovalStatus().equalsIgnoreCase(Constants.PM.UNSYNC_STATUS)){
                        intent.putExtra(Constants.PM.FORM_ID, formID);
                        intent.putExtra(Constants.PM.PROCESS_ID, processID);
                        intent.putExtra(Constants.PM.PARTIAL_FORM, true);
                    }
                    mContext.startActivity(intent);
                }
            } else {
                Util.showToast(mContext.getString(R.string.approve_profile), mContext);
            }
        });

//        int bgColor = mContext.getResources().getColor(R.color.submitted_form_color);
//        if (cat.equals(mContext.getString(R.string.syncing_pending))) {
//            bgColor = mContext.getResources().getColor(R.color.red);
//        }

        view.findViewById(R.id.iv_dashboard_delete_form).setVisibility(View.GONE);
        //view.findViewById(R.id.form_status_indicator).setBackgroundColor(bgColor);
        if(data.getFormApprovalStatus()!=null && data.getFormApprovalStatus().equalsIgnoreCase(Constants.PM.UNSYNC_STATUS)){
            view.findViewById(R.id.iv_status_icon).setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(final int groupPosition, final int childPosition) {
        return false;
    }
}
