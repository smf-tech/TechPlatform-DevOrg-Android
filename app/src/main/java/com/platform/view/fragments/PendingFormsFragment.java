package com.platform.view.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.platform.syncAdapter.SyncAdapterUtils.EVENT_FORM_ADDED;
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
     * @return A new instance of fragment PendingFormsFragment.
     */
    static PendingFormsFragment newInstance() {
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

        getPendingFormsFromDB();

        IntentFilter filter = new IntentFilter();
        filter.addAction(EVENT_SYNC_COMPLETED);
        filter.addAction(EVENT_SYNC_FAILED);
        filter.addAction(EVENT_FORM_ADDED);
        filter.addAction(PARTIAL_FORM_ADDED);
        filter.addAction(PARTIAL_FORM_REMOVED);

        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                if (Objects.requireNonNull(intent.getAction()).equals(EVENT_SYNC_COMPLETED)) {
                    Toast.makeText(context, "Sync completed.", Toast.LENGTH_SHORT).show();
                    updateAdapter(context);
                } else if (Objects.requireNonNull(intent.getAction()).equals(EVENT_FORM_ADDED)) {
                    updateAdapter(context);
                } else if (Objects.requireNonNull(intent.getAction()).equals(PARTIAL_FORM_REMOVED)) {
                    updateAdapter(context);
                } else if (Objects.requireNonNull(intent.getAction()).equals(PARTIAL_FORM_ADDED)) {
                    Toast.makeText(context, "Partial Form Added.", Toast.LENGTH_SHORT).show();
                    updateAdapter(context);
                } else if (intent.getAction().equals(EVENT_SYNC_FAILED)) {
                    Log.e("PendingForms", "Sync failed!");
                    Toast.makeText(context, "Sync failed!", Toast.LENGTH_SHORT).show();
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

            mSavedForms.clear();
            mSavedForms.addAll(list);

            if (mSavedForms != null && !mSavedForms.isEmpty()) {
                mNoRecordsView.setVisibility(View.GONE);
                if (mPendingFormCategoryAdapter != null) {
                    mPendingFormCategoryAdapter.notifyDataSetChanged();
                }
            } else {
                mRecyclerView.setVisibility(View.GONE);
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

            if (!mSavedForms.isEmpty()) {
                mPendingFormCategoryAdapter = new PendingFormCategoryAdapter(getContext(), mSavedForms);
                mRecyclerView.setAdapter(mPendingFormCategoryAdapter);
                mNoRecordsView.setVisibility(View.GONE);
            } else {
                mNoRecordsView.setVisibility(View.VISIBLE);
            }
        }
    }
}
