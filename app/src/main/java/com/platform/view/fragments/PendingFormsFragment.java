package com.platform.view.fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.models.forms.FormResult;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.FormActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static com.platform.syncAdapter.SyncAdapterUtils.EVENT_FORM_SUBMITTED;
import static com.platform.syncAdapter.SyncAdapterUtils.EVENT_SYNC_COMPLETED;
import static com.platform.syncAdapter.SyncAdapterUtils.EVENT_SYNC_FAILED;
import static com.platform.syncAdapter.SyncAdapterUtils.PARTIAL_FORM_ADDED;
import static com.platform.syncAdapter.SyncAdapterUtils.PARTIAL_FORM_REMOVED;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PendingFormsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings({"CanBeFinal", "WeakerAccess"})
public class PendingFormsFragment extends Fragment {

    private TextView mNoRecordsView;
    private ExpandableListView mExpandableListView;
    private Map<String, List<FormResult>> mFormResultMap;

    public PendingFormsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PendingFormsFragment.
     */
    static PendingFormsFragment newInstance() {
        return new PendingFormsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_forms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNoRecordsView = view.findViewById(R.id.no_records_view);

        mExpandableListView = view.findViewById(R.id.forms_expandable_list);
        mExpandableListView.setGroupIndicator(null);

        getPendingFormsFromDB();

        IntentFilter filter = new IntentFilter();
        filter.addAction(EVENT_SYNC_COMPLETED);
        filter.addAction(EVENT_SYNC_FAILED);
        filter.addAction(PARTIAL_FORM_ADDED);
        filter.addAction(PARTIAL_FORM_REMOVED);
        filter.addAction(EVENT_FORM_SUBMITTED);

        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext()))
                .registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                if (context == null) {
                    return;
                }

                try {
                    String action = Objects.requireNonNull(intent.getAction());
                    switch (action) {
                        case EVENT_SYNC_COMPLETED:
                            updateAdapter(context);
                            Util.showToast(getString(R.string.sync_completed), context);
                            break;

                        case PARTIAL_FORM_ADDED:
                            updateAdapter(context);
                            Toast.makeText(context, R.string.partial_form_added, Toast.LENGTH_SHORT).show();
                            break;

                        case PARTIAL_FORM_REMOVED:
                        case EVENT_FORM_SUBMITTED:
                            updateAdapter(context);
                            break;

                        case EVENT_SYNC_FAILED:
                            Log.e("PendingForms", "Sync failed!");
                            Util.showToast(getString(R.string.sync_failed), context);
                            break;
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        }, filter);
    }

    private void updateAdapter(final Context context) {
        try {
            List<FormResult> list = DatabaseManager.getDBInstance(context)
                    .getAllPartiallySavedForms();

            mFormResultMap = new HashMap<>();
            List<String> categoryList = new ArrayList<>();
            for (final FormResult form : list) {
                List<FormResult> forms = new ArrayList<>();
                if (!categoryList.contains(form.getFormCategory())) {
                    categoryList.add(form.getFormCategory());
                    forms.add(form);
                    mFormResultMap.put(form.getFormCategory(), forms);
                } else {
                    List<FormResult> formResults = mFormResultMap.get(form.getFormCategory());
                    if (formResults != null) {
                        formResults.add(form);
                        mFormResultMap.put(form.getFormCategory(), formResults);
                    }
                }
            }

            mExpandableListView.setAdapter(new SavedFormsListAdapter(getContext(), mFormResultMap, this));

            if (!mFormResultMap.isEmpty()) {
                mNoRecordsView.setVisibility(View.GONE);
            } else {
                mNoRecordsView.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Log.e("PendingForms", e.getMessage() + "", e);
        }
    }

    private void getPendingFormsFromDB() {
        List<FormResult> partialSavedForms = DatabaseManager.getDBInstance(getContext())
                .getAllPartiallySavedForms();
        if (partialSavedForms != null) {
            List<FormResult> savedForms = new ArrayList<>(partialSavedForms);

            savedForms = Util.sortFormResultListByCreatedDate(savedForms);

            mFormResultMap = new HashMap<>();
            List<String> categoryList = new ArrayList<>();
            for (final FormResult form : savedForms) {
                List<FormResult> forms = new ArrayList<>();
                if (!categoryList.contains(form.getFormCategory())) {
                    categoryList.add(form.getFormCategory());
                    forms.add(form);
                    mFormResultMap.put(form.getFormCategory(), forms);
                } else {
                    List<FormResult> formResults = mFormResultMap.get(form.getFormCategory());
                    if (formResults != null) {
                        formResults.add(form);
                        mFormResultMap.put(form.getFormCategory(), formResults);
                    }
                }
            }

            mExpandableListView.setAdapter(new SavedFormsListAdapter(getContext(), mFormResultMap, this));

            if (!mFormResultMap.isEmpty()) {
                mNoRecordsView.setVisibility(View.GONE);
            } else {
                mNoRecordsView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void onFormDeletedListener() {
        updateAdapter(getContext());
    }

    private class SavedFormsListAdapter extends BaseExpandableListAdapter {

        private Context mContext;
        private Map<String, List<FormResult>> mMap;
        private PendingFormsFragment mFragment;

        private SavedFormsListAdapter(final Context context, final Map<String,
                List<FormResult>> map, PendingFormsFragment fragment) {

            mContext = context;
            mMap = map;
            mFragment = fragment;
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

            ((TextView) view.findViewById(R.id.form_title)).setText(cat);
            ((TextView) view.findViewById(R.id.form_count))
                    .setText(String.format("%s %s", String.valueOf(size), getString(R.string.forms)));

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
                        .setText(Util.getDateFromTimestamp(formResult.getCreatedAt()));
            }

            final FormResult finalFormResult = formResult;

            view.findViewById(R.id.iv_dashboard_delete_form)
                    .setOnClickListener(v -> showFormDeletePopUp(processData, finalFormResult));

            view.setOnClickListener(v -> {
                if (finalFormResult != null) {
                    final String formID = finalFormResult.getFormId();
                    final String processID = finalFormResult.get_id();

                    Intent intent = new Intent(mContext, FormActivity.class);
                    intent.putExtra(Constants.PM.PROCESS_ID, processID);
                    intent.putExtra(Constants.PM.FORM_ID, formID);
                    intent.putExtra(Constants.PM.EDIT_MODE, true);
                    intent.putExtra(Constants.PM.PARTIAL_FORM, true);
                    mContext.startActivity(intent);
                }
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
            alertDialog.setTitle(getString(R.string.app_name_ss));
            // Setting Dialog Message
            alertDialog.setMessage(getString(R.string.msg_delete_saved_form));
            // Setting Icon to Dialog
            alertDialog.setIcon(R.mipmap.app_logo);
            // Setting CANCEL Button
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel),
                    (dialog, which) -> alertDialog.dismiss());
            // Setting OK Button
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok),
                    (dialog, which) -> deleteSavedForm(processData, finalFormResult));

            // Showing Alert Message
            alertDialog.show();
        }

        @Override
        public boolean isChildSelectable(final int groupPosition, final int childPosition) {
            return false;
        }
    }
}
