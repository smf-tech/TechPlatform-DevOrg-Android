package com.platform.view.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
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
import com.platform.utility.Util;
import com.platform.view.adapters.PendingFormCategoryAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static com.platform.syncAdapter.SyncAdapterUtils.EVENT_FORM_ADDED;
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
    private PendingFormCategoryAdapter mPendingFormCategoryAdapter;
    private List<FormResult> mSavedForms;
    ExpandableListView expandableListView;

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

        expandableListView = view.findViewById(R.id.forms_expandable_list);
        expandableListView.setGroupIndicator(null);

        getPendingFormsFromDB();

//        expandableListView.setAdapter(new SavedFormsListAdapter(getContext(), map));

        IntentFilter filter = new IntentFilter();
        filter.addAction(EVENT_SYNC_COMPLETED);
        filter.addAction(EVENT_SYNC_FAILED);
        filter.addAction(EVENT_FORM_ADDED);
        filter.addAction(PARTIAL_FORM_ADDED);
        filter.addAction(PARTIAL_FORM_REMOVED);
        filter.addAction(EVENT_FORM_SUBMITTED);

        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                String action = Objects.requireNonNull(intent.getAction());
                switch (action) {
                    case EVENT_SYNC_COMPLETED:
                        updateAdapter(context);
                        Toast.makeText(context, "Sync completed.", Toast.LENGTH_SHORT).show();
                        break;

                    case PARTIAL_FORM_ADDED:
                        updateAdapter(context);
                        Toast.makeText(context, "Partial Form Added.", Toast.LENGTH_SHORT).show();
                        break;

                    case EVENT_FORM_ADDED:
                    case PARTIAL_FORM_REMOVED:
                    case EVENT_FORM_SUBMITTED:
                        updateAdapter(context);
                        break;

                    case EVENT_SYNC_FAILED:
                        Log.e("PendingForms", "Sync failed!");
                        Toast.makeText(context, "Sync failed!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, filter);
    }

    private void updateAdapter(final Context context) {
        try {
            if (mSavedForms == null || mSavedForms.isEmpty()) {
                mSavedForms = new ArrayList<>();
                if (mPendingFormCategoryAdapter != null)
                    mPendingFormCategoryAdapter =
                            new PendingFormCategoryAdapter(getContext(), mSavedForms);
            }

            List<FormResult> list = DatabaseManager.getDBInstance(context)
                    .getAllPartiallySavedForms();

            list = Util.sortFormResultListByCreatedDate(list);

            mSavedForms.clear();
            mSavedForms.addAll(list);

            if (mSavedForms != null && !mSavedForms.isEmpty()) {
                mNoRecordsView.setVisibility(View.GONE);
                if (mPendingFormCategoryAdapter != null) {
                    mPendingFormCategoryAdapter.notifyDataSetChanged();
                }
            } else {
//                mRecyclerView.setVisibility(View.GONE);
                mNoRecordsView.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Log.e("PendingForms", e.getMessage() + "");
        }
    }

    private void getPendingFormsFromDB() {
        List<FormResult> partialSavedForms = DatabaseManager.getDBInstance(getContext())
                .getAllPartiallySavedForms();
        if (partialSavedForms != null) {
            mSavedForms = new ArrayList<>(partialSavedForms);

            mSavedForms = Util.sortFormResultListByCreatedDate(mSavedForms);

            Map<String, List<FormResult>> map = new HashMap<>();
            List<String> categoryList = new ArrayList<>();
            for (final FormResult form : mSavedForms) {
                List<FormResult> forms = new ArrayList<>();
                if (!categoryList.contains(form.getFormCategory())) {
                    categoryList.add(form.getFormCategory());
                    forms.add(form);
                    map.put(form.getFormCategory(), forms);
                } else {
                    List<FormResult> formResults = map.get(form.getFormCategory());
                    formResults.add(form);
                    map.put(form.getFormCategory(), formResults);
                }
            }

            expandableListView.setAdapter(new SavedFormsListAdapter(getContext(), map));


            if (!mSavedForms.isEmpty()) {
//                mPendingFormCategoryAdapter = new PendingFormCategoryAdapter(getContext(), mSavedForms);
//                mRecyclerView.setAdapter(mPendingFormCategoryAdapter);
                mNoRecordsView.setVisibility(View.GONE);
            } else {
                mNoRecordsView.setVisibility(View.VISIBLE);
            }
        }
    }

    private class SavedFormsListAdapter extends BaseExpandableListAdapter {

        Context mContext;
        private Map<String, List<FormResult>> mMap;

        private SavedFormsListAdapter(final Context context, final Map<String, List<FormResult>> map) {
            mContext = context;
            mMap = map;
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
        public View getGroupView(final int groupPosition, final boolean isExpanded, final View convertView, final ViewGroup parent) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_all_forms_item, parent, false);

            ArrayList<String> list = new ArrayList<>(mMap.keySet());
            String cat = list.get(groupPosition);

            List<FormResult> processData = mMap.get(cat);
            int size = 0;
            if (processData != null) {
                size = processData.size();
            }

            ((TextView) view.findViewById(R.id.form_title)).setText(cat);
            ((TextView) view.findViewById(R.id.form_count)).setText(String.format("%s Forms", String.valueOf(size)));

            return view;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, final boolean isLastChild, final View convertView, final ViewGroup parent) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.form_sub_item,
                    parent, false);

            ArrayList<String> list = new ArrayList<>(mMap.keySet());
            String cat = list.get(groupPosition);

            List<FormResult> processData = mMap.get(cat);
            if (processData != null) {
                FormResult data = processData.get(childPosition);

                ((TextView) view.findViewById(R.id.form_title)).setText(data.getFormName().trim());

//                if (groupPosition < mCountList.size()) {
//                    String count = mCountList.get(data.getId());
//                    if (count == null) count = "0";
//
//                    ((TextView) view.findViewById(R.id.submitted_count_label))
//                            .setText(mContext.getString(R.string.submitted_form_count, count));
//                } else {
//                ((TextView) view.findViewById(R.id.submitted_count_label))
//                        .setText(mContext.getString(R.string.submitted_form_count, "0"));
//                }

            }

            ColorStateList tintColor = ColorStateList.valueOf(mContext.getResources()
                    .getColor(R.color.submitted_form_color));

            FormResult formResult = mSavedForms.get(childPosition);

            Drawable drawable = mContext.getDrawable(R.drawable.form_status_indicator_completed);

            ImageView formImage = view.findViewById(R.id.form_image);
            formImage.setImageTintList(tintColor);

            view.findViewById(R.id.form_status_indicator).setBackground(drawable);
            ((TextView) view.findViewById(R.id.form_title)).setText(formResult.getFormName());
            return view;
        }

        @Override
        public boolean isChildSelectable(final int groupPosition, final int childPosition) {
            return false;
        }
    }
}
