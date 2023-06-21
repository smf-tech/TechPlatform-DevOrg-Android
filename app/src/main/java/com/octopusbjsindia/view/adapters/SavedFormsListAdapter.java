package com.octopusbjsindia.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.models.forms.FormResult;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.FormDisplayActivity;
import com.octopusbjsindia.view.fragments.PendingFormsFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("CanBeFinal")
public class SavedFormsListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private Map<String, List<FormResult>> mMap;
    private PendingFormsFragment mFragment;

    public SavedFormsListAdapter(final Context context, final Map<String, List<FormResult>> map,
                                 PendingFormsFragment fragment) {

        this.mContext = context;
        this.mMap = map;
        this.mFragment = fragment;
    }

    @Override
    public int getGroupCount() {
        return mMap.size();
    }

    @Override
    public int getChildrenCount(final int groupPosition) {
        ArrayList<String> list = new ArrayList<>(mMap.keySet());
        String cat = list.get(groupPosition);

        List<FormResult> formResults = mMap.get(cat);
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

        List<FormResult> processData = mMap.get(cat);
        int size = 0;
        if (processData != null) {
            size = processData.size();
        }

        ((TextView) view.findViewById(R.id.form_title)).setText(cat.trim());
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

        View view = LayoutInflater.from(mContext).inflate(R.layout.row_dashboard_pending_forms_card_view,
                parent, false);

        ArrayList<String> list = new ArrayList<>(mMap.keySet());
        String cat = list.get(groupPosition);

        List<FormResult> processData = mMap.get(cat);
        FormResult formResult = null;
        if (processData != null) {
            formResult = processData.get(childPosition);

            ((TextView) view.findViewById(R.id.txt_dashboard_pending_form_title))
                    .setText(formResult.getFormName().trim());
            ((TextView) view.findViewById(R.id.txt_dashboard_pending_form_created_at))
                    .setText(Util.getDateTimeFromTimestamp(formResult.getCreatedAt()));
        }

        final FormResult finalFormResult = formResult;

        view.findViewById(R.id.iv_dashboard_delete_form)
                .setOnClickListener(v -> showFormDeletePopUp(processData, finalFormResult));

        view.setOnClickListener(v -> {
//            if (Util.isUserApproved()) {
                if (finalFormResult != null) {
                    final String formID = finalFormResult.getFormId();
                    final String processID = finalFormResult.get_id();

                    /*Intent intent = new Intent(mContext, FormActivity.class);
                    intent.putExtra(Constants.PM.PROCESS_ID, processID);
                    intent.putExtra(Constants.PM.FORM_ID, formID);
                    intent.putExtra(Constants.PM.EDIT_MODE, true);
                    intent.putExtra(Constants.PM.PARTIAL_FORM, true);
                    mContext.startActivity(intent);*/

                    Intent intent = new Intent(mContext, FormDisplayActivity.class);
                    intent.putExtra(Constants.PM.PROCESS_ID, processID);
                    intent.putExtra(Constants.PM.FORM_ID, formID);
                    //intent.putExtra(Constants.PM.EDIT_MODE, true);
                    //intent.putExtra(Constants.PM.PARTIAL_FORM, true);
                    mContext.startActivity(intent);

                }
//            } else {
//                Util.showToast(mContext.getString(R.string.approve_profile), mContext);
//            }
        });

        Drawable drawable = mContext.getDrawable(R.drawable.form_status_indicator_partial);
        view.findViewById(R.id.form_status_indicator).setBackground(drawable);

        return view;
    }

    private void deleteSavedForm(List<FormResult> processData, FormResult finalFormResult) {
        DatabaseManager.getDBInstance(mContext).deleteFormResult(finalFormResult);
        if (processData != null) {
            processData.remove(finalFormResult);
        }

        notifyDataSetChanged();
        Util.showToast(mContext.getString(R.string.form_deleted), mContext);

        mFragment.onFormDeletedListener();
    }

    private void showFormDeletePopUp(List<FormResult> processData, FormResult finalFormResult) {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        // Setting Dialog Title
        alertDialog.setTitle(mContext.getString(R.string.app_name_ss));
        // Setting Dialog Message
        alertDialog.setMessage(mContext.getString(R.string.msg_delete_saved_form));
        // Setting Icon to Dialog
        alertDialog.setIcon(R.mipmap.ic_launcher);
        // Setting CANCEL Button
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, mContext.getString(R.string.cancel),
                (dialog, which) -> alertDialog.dismiss());
        // Setting OK Button
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, mContext.getString(R.string.ok),
                (dialog, which) -> deleteSavedForm(processData, finalFormResult));

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public boolean isChildSelectable(final int groupPosition, final int childPosition) {
        return false;
    }
}
