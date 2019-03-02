package com.platform.view.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.models.forms.FormResult;
import com.platform.view.adapters.PendingFormCategoryAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.platform.presenter.PMFragmentPresenter.getAllNonSyncedSavedForms;
import static com.platform.syncAdapter.SyncAdapterUtils.EVENT_FORM_ADDED;
import static com.platform.syncAdapter.SyncAdapterUtils.EVENT_SYNC_COMPLETED;
import static com.platform.syncAdapter.SyncAdapterUtils.EVENT_SYNC_FAILED;
import static com.platform.utility.Constants.Form.EXTRA_FORM_ID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PendingFormsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("CanBeFinal")
public class PendingFormsFragment extends Fragment {

    private TextView mNoRecordsView;
    private RecyclerView mRecyclerView;
    private PendingFormCategoryAdapter mPendingFormCategoryAdapter;
    private List<FormResult> mSavedForms;

    public PendingFormsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CompletedFormsFragment.
     */
    public static PendingFormsFragment newInstance() {
        return new PendingFormsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saved_forms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.forms_list);
        mNoRecordsView = view.findViewById(R.id.no_records_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mSavedForms = new ArrayList<>();

        getPendingFormsFromDB();

        IntentFilter filter = new IntentFilter();
        filter.addAction(EVENT_SYNC_COMPLETED);
        filter.addAction(EVENT_SYNC_FAILED);
        filter.addAction(EVENT_FORM_ADDED);

        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                if (Objects.requireNonNull(intent.getAction()).equals(EVENT_SYNC_COMPLETED)) {
                    Toast.makeText(context, "Sync completed.", Toast.LENGTH_SHORT).show();

                    int formID = intent.getIntExtra(EXTRA_FORM_ID, 0);
                    updateAdapter(context, formID);
                } else if (Objects.requireNonNull(intent.getAction()).equals(EVENT_FORM_ADDED)) {

                    updateAdapter(context, 0);
                } else if (intent.getAction().equals(EVENT_SYNC_FAILED)) {
                    Log.e("PendingForms", "Sync failed!");
                    Toast.makeText(context, "Sync failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }, filter);
    }

    private void updateAdapter(final Context context, final int formID) {
        try {
            if (mPendingFormCategoryAdapter == null) {
                mPendingFormCategoryAdapter = (PendingFormCategoryAdapter) mRecyclerView.getAdapter();
            }

            if (formID == 0) {
                mSavedForms.clear();
                mSavedForms.addAll(getAllNonSyncedSavedForms(context));
            /*} else {
                List<FormResult> list = new ArrayList<>(mSavedForms);
                for (final FormResult form : mSavedForms) {
                    if (formID == form.id) {
                        list.remove(form);
                    }
                }
                mSavedForms.clear();
                mSavedForms.addAll(list);*/
            }

            if (mSavedForms != null && !mSavedForms.isEmpty()) {
                mNoRecordsView.setVisibility(View.GONE);
            } else {
                mRecyclerView.setVisibility(View.GONE);
                mNoRecordsView.setVisibility(View.VISIBLE);
            }

            if (mPendingFormCategoryAdapter != null) {
                mPendingFormCategoryAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e("PendingForms", e.getMessage());
        }
    }

    /**
     * This method fetches all the pending forms from DB
     */
    private void getPendingFormsFromDB() {
        List<FormResult> partialSavedForms = DatabaseManager.getDBInstance(getContext())
                .getAllPartiallySavedForms();
        if (partialSavedForms != null) {
            mSavedForms.addAll(partialSavedForms);

            if (mSavedForms != null && !mSavedForms.isEmpty()) {
                mPendingFormCategoryAdapter = new PendingFormCategoryAdapter(getContext(), mSavedForms);
                mRecyclerView.setAdapter(mPendingFormCategoryAdapter);
                mNoRecordsView.setVisibility(View.GONE);
            } else {
                mNoRecordsView.setVisibility(View.VISIBLE);
            }
        }
    }
}
