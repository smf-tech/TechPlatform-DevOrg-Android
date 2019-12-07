package com.octopusbjsindia.view.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.models.forms.FormResult;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.SavedFormsListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.octopusbjsindia.syncAdapter.SyncAdapterUtils.EVENT_FORM_SUBMITTED;
import static com.octopusbjsindia.syncAdapter.SyncAdapterUtils.EVENT_SYNC_COMPLETED;
import static com.octopusbjsindia.syncAdapter.SyncAdapterUtils.EVENT_SYNC_FAILED;
import static com.octopusbjsindia.syncAdapter.SyncAdapterUtils.PARTIAL_FORM_ADDED;
import static com.octopusbjsindia.syncAdapter.SyncAdapterUtils.PARTIAL_FORM_REMOVED;

@SuppressWarnings({"CanBeFinal", "WeakerAccess"})
public class PendingFormsFragment extends Fragment {

    private TextView mNoRecordsView;
    private ExpandableListView mExpandableListView;
    private Map<String, List<FormResult>> mFormResultMap;

    public PendingFormsFragment() {
        // Required empty public constructor
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
                                    Util.showToast(getString(R.string.partial_form_added), context);
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
                            Log.e("TAG", e.getMessage());
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

            mExpandableListView.setAdapter(
                    new SavedFormsListAdapter(getContext(), mFormResultMap, this));

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
        List<FormResult> partialSavedForms = DatabaseManager
                .getDBInstance(getContext()).getAllPartiallySavedForms();

        if (partialSavedForms != null) {
            List<FormResult> savedForms
                    = Util.sortFormResultListByCreatedDate(new ArrayList<>(partialSavedForms));

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

            mExpandableListView.setAdapter(new SavedFormsListAdapter(getContext(),
                    mFormResultMap, this));

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
}
