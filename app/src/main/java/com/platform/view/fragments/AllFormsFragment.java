package com.platform.view.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.listeners.FormStatusCallListener;
import com.platform.models.pm.ProcessData;
import com.platform.models.pm.Processes;
import com.platform.presenter.FormStatusFragmentPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.adapters.ExpandableAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private Map<String, String> mCountList;
    private ExpandableAdapter adapter;

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
        mCountList = new HashMap<>();

        ExpandableListView expandableListView = view.findViewById(R.id.forms_expandable_list);
        adapter = new ExpandableAdapter(getContext(), mChildList, mCountList);
        expandableListView.setAdapter(adapter);

        List<ProcessData> processDataArrayList = DatabaseManager.getDBInstance(getActivity()).getAllProcesses();
        if (processDataArrayList != null && !processDataArrayList.isEmpty()) {
            Processes processes = new Processes();
            processes.setData(processDataArrayList);
            processResponse(processes);
        } else {
            if (Util.isConnected(getContext())) {
                FormStatusFragmentPresenter presenter = new FormStatusFragmentPresenter(this);
                presenter.getAllProcesses();
            }
        }
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
        Processes json = new Gson().fromJson(response, Processes.class);
        if (json != null && json.getData() != null && !json.getData().isEmpty()) {
            for (ProcessData processData :
                    json.getData()) {
                DatabaseManager.getDBInstance(getContext()).insertProcessData(processData);
            }
            processResponse(json);
        }
    }

    private void processResponse(final Processes json) {
        mCountList.clear();
        mChildList.clear();

        FormStatusFragmentPresenter presenter = new FormStatusFragmentPresenter(this);

        for (ProcessData data : json.getData()) {
            String categoryName = data.getCategory().getName();
            if (mChildList.containsKey(categoryName)) {
                List<ProcessData> processData = mChildList.get(categoryName);
                if (processData != null) {
                    processData.add(data);
                    mChildList.put(categoryName, processData);
                }
            } else {
                List<ProcessData> processData = new ArrayList<>();
                processData.add(data);
                mChildList.put(categoryName, processData);
            }


            ProcessData processData = DatabaseManager.getDBInstance(
                    Objects.requireNonNull(getActivity()).getApplicationContext())
                    .getProcessData(data.getId());
            String submitCount = processData.getSubmitCount();
            if (!TextUtils.isEmpty(submitCount)) {
                mCountList.put(data.getId(), submitCount);
            } else if (Util.isConnected(getContext())) {
                presenter.getSubmittedForms(data.getId());
            }
        }

        if (!mChildList.isEmpty()) {
            setAdapter(mChildList);
            mNoRecordsView.setVisibility(View.GONE);
        } else {
            mNoRecordsView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMastersFormsLoaded(final String response, final String formId) {
        try {
            String count;
            if (new JSONObject(response).has(Constants.FormDynamicKeys.METADATA)) {
                JSONArray metadata = (JSONArray) new JSONObject(response).get(Constants.FormDynamicKeys.METADATA);
                if (metadata != null && metadata.length() > 0) {
                    JSONObject metadataObj = metadata.getJSONObject(0);
                    count = metadataObj.getJSONObject(Constants.FormDynamicKeys.FORM).getString(Constants.FormDynamicKeys.SUBMIT_COUNT);
                    String formID = metadataObj.getJSONObject(Constants.FormDynamicKeys.FORM).getString(Constants.FormDynamicKeys.FORM_ID);
                    mCountList.put(formID, count);
                    DatabaseManager.getDBInstance(Objects.requireNonNull(getActivity())
                            .getApplicationContext()).updateProcessSubmitCount(formID, count);
                } else {
                    mCountList.put(formId, String.valueOf(0));
                    DatabaseManager.getDBInstance(Objects.requireNonNull(getActivity())
                            .getApplicationContext()).updateProcessSubmitCount(formId, String.valueOf(0));
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
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
            adapter.notifyDataSetChanged();
            mNoRecordsView.setVisibility(View.GONE);
        }
    }

}
