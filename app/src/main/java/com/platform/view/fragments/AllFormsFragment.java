package com.platform.view.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.R;
import com.platform.listeners.FormStatusCallListener;
import com.platform.models.pm.ProcessData;
import com.platform.models.pm.Processes;
import com.platform.presenter.FormStatusFragmentPresenter;
import com.platform.view.adapters.ExpandableAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllFormsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("EmptyMethod")
public class AllFormsFragment extends Fragment implements FormStatusCallListener {

    private static final String TAG = AllFormsFragment.class.getSimpleName();
    private final Map<String, List<ProcessData>> mChildList = new HashMap<>();
    private TextView mNoRecordsView;
    private ExpandableListView expandableListView;

    public AllFormsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AllFormsFragment.
     */
    public static AllFormsFragment newInstance() {
        return new AllFormsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_forms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNoRecordsView = view.findViewById(R.id.no_records_view);

        FormStatusFragmentPresenter presenter = new FormStatusFragmentPresenter(this);
        presenter.getAllProcesses();

        expandableListView = view.findViewById(R.id.forms_expandable_list);

    }

    @Override
    public void onFailureListener(String message) {
        Log.e(TAG, "onFailureListener: " + message);
    }

    @Override
    public void onErrorListener(VolleyError error) {
        Log.e(TAG, "onErrorListener: " + error.getMessage());
    }

    @Override
    public void onFormsLoaded(String response) {

        mChildList.clear();

        Processes json = new Gson().fromJson(response, Processes.class);
        for (ProcessData data : json.getData()) {
            String categoryName = data.getCategory().getName();
            if (mChildList.containsKey(categoryName)) {
                List<ProcessData> processData = mChildList.get(categoryName);
                if (processData != null) {
                    processData.add(data);
                }
                mChildList.put(categoryName, processData);
            } else {
                List<ProcessData> processData = new ArrayList<>();
                processData.add(data);
                mChildList.put(categoryName, processData);
            }
        }

        if (!mChildList.isEmpty()) {
            setAdapter(mChildList);
            mNoRecordsView.setVisibility(View.GONE);
        } else {
            mNoRecordsView.setVisibility(View.VISIBLE);
        }

    }

    private void setAdapter(final Map<String, List<ProcessData>> data) {
        if (data != null && !data.isEmpty()) {
            ExpandableAdapter adapter = new ExpandableAdapter(getContext(), data);
            expandableListView.setAdapter(adapter);
            mNoRecordsView.setVisibility(View.GONE);
        }
    }

}
