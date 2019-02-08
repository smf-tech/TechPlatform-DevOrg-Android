package com.platform.view.fragments;


import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.SavedForm;
import com.platform.view.adapters.PendingFormCategoryAdapter;

import java.util.List;

import static com.platform.presenter.PMFragmentPresenter.getAllNonSyncedSavedForms;
import static com.platform.syncAdapter.SyncAdapterUtils.EVENT_SYNC_COMPLETED;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PendingFormsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("CanBeFinal")
public class PendingFormsFragment extends Fragment {

    private TextView mNoRecordsView;
    private RecyclerView mRecyclerView;

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
        return inflater.inflate(R.layout.fragment_form_status, container, false);
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

        /*LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                if (Objects.requireNonNull(intent.getAction()).equals(EVENT_SYNC_COMPLETED)) {
                    Toast.makeText(context, "Sync completed.", Toast.LENGTH_SHORT).show();

                    if (mPendingFormCategoryAdapter == null) {
                        mPendingFormCategoryAdapter = (PendingFormCategoryAdapter) mRecyclerView.getAdapter();
                    }

                    mSavedForms = getAllNonSyncedSavedForms();
                    mPendingFormCategoryAdapter.notifyDataSetChanged();
                }
            }
        }, filter);*/
    }

    /**
     * This method fetches all the pending forms from DB
     */
    private void getPendingFormsFromDB() {
        final List<SavedForm> savedForms = getAllNonSyncedSavedForms();
        if (savedForms != null && !savedForms.isEmpty()) {
            final PendingFormCategoryAdapter pendingFormCategoryAdapter = new PendingFormCategoryAdapter(getContext(), savedForms);
            mRecyclerView.setAdapter(pendingFormCategoryAdapter);
            mNoRecordsView.setVisibility(View.GONE);
        } else {
            mNoRecordsView.setVisibility(View.VISIBLE);
        }
    }

}
