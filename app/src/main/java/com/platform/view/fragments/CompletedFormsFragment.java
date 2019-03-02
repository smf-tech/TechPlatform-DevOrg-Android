package com.platform.view.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.listeners.FormStatusCallListener;
import com.platform.models.pm.ProcessData;
import com.platform.models.pm.Processes;
import com.platform.presenter.FormStatusFragmentPresenter;
import com.platform.syncAdapter.SyncAdapterUtils;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.adapters.FormCategoryAdapter;
import com.platform.view.adapters.PendingFormsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.platform.presenter.PMFragmentPresenter.getAllNonSyncedSavedForms;
import static com.platform.utility.Util.saveFormCategoryForSync;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompletedFormsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("CanBeFinal")
public class CompletedFormsFragment extends Fragment implements FormStatusCallListener {
    private static final String TAG = CompletedFormsFragment.class.getSimpleName();

    private TextView mNoRecordsView;
    private List<ProcessData> mDataList = new ArrayList<>();
    private Map<String, List<ProcessDemoObject>> mFormList = new HashMap<>();
    private FormCategoryAdapter adapter;
    private RecyclerView rvPendingForms;
    private RelativeLayout rltPendingForms;
    private View mSubmittedFormsView;

    public CompletedFormsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CompletedFormsFragment.
     */
    public static CompletedFormsFragment newInstance() {
        return new CompletedFormsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSubmittedFormsView = inflater.inflate(R.layout.fragment_form_status, container, false);
        return mSubmittedFormsView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPendingForms = view.findViewById(R.id.rv_dashboard_pending_forms);
        rltPendingForms = view.findViewById(R.id.rlt_pending_forms);

        final RecyclerView recyclerView = view.findViewById(R.id.forms_list);
        mNoRecordsView = view.findViewById(R.id.no_records_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new FormCategoryAdapter(getContext(), mFormList);
        recyclerView.setAdapter(adapter);

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

            PendingFormsAdapter pendingFormsAdapter = new PendingFormsAdapter(getActivity(), savedForms);
            rvPendingForms.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvPendingForms.setAdapter(pendingFormsAdapter);
        } else {
            rltPendingForms.setVisibility(View.GONE);
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
        mFormList.clear();
        mDataList.clear();

        FormStatusFragmentPresenter presenter = new FormStatusFragmentPresenter(this);
        for (ProcessData data : json.getData()) {

            String id = data.getId();
            mDataList.add(data);

            List<String> response = DatabaseManager.getDBInstance(Objects.requireNonNull(getContext()).getApplicationContext())
                    .getAllFormResults(id);
            if (response != null && !response.isEmpty()) {
                processFormResultResponse(response);
            } else {
                if (Util.isConnected(getContext()) && !TextUtils.isEmpty(data.getSubmitCount()) &&
                        Integer.parseInt(data.getSubmitCount()) != 0) {
                    presenter.getSubmittedForms(id);
                }
            }
        }
    }

    @Override
    public void onMastersFormsLoaded(final String response, final String formId) {
        processFormResultResponse(response);
    }

    private void processFormResultResponse(final String response) {

        ArrayList<ProcessDemoObject> list = new ArrayList<>();
        String formID = "";

        try {
            if (new JSONObject(response).has(Constants.FormDynamicKeys.VALUES)) {
                JSONArray values = new JSONObject(response).getJSONArray(Constants.FormDynamicKeys.VALUES);
                for (int i = 0; i < values.length(); i++) {

                    FormResult formResult = new Gson()
                            .fromJson(String.valueOf(values.get(i)), FormResult.class);

                    formID = formResult.formID;
                    list.add(new ProcessDemoObject(formResult.mID.oid,
                            formID, formResult.updatedAt));

                    com.platform.models.forms.FormResult result = new com.platform.models.forms.FormResult();
                    result.set_id(formResult.mID.oid);
                    result.setFormId(formID);
                    result.setFormStatus(SyncAdapterUtils.FormStatus.SYNCED);

                    JSONObject obj = (JSONObject) values.get(i);
                    if (obj == null) return;


                    List<String> localFormResults = DatabaseManager.getDBInstance(getActivity())
                            .getAllFormResults(formID);
                    if (!localFormResults.contains(obj.toString())) {
                        result.setResult(obj.toString());
                        DatabaseManager.getDBInstance(getActivity()).insertFormResult(result);
                    }

                }

                if (formID == null) {
                    JSONArray metadata = (JSONArray) new JSONObject(response).get(Constants.FormDynamicKeys.METADATA);
                    if (metadata != null && metadata.length() > 0) {
                        JSONObject metadataObj = metadata.getJSONObject(0);
                        formID = metadataObj.getJSONObject(Constants.FormDynamicKeys.FORM).getString(Constants.FormDynamicKeys.FORM_ID);
                    }
                }
            } else {
                list.add(new ProcessDemoObject("No Forms available", "0", ""));
            }

            for (final ProcessData data : mDataList) {
                if (data.getId().equals(formID)) {
                    mFormList.put(data.getName(), list);
                }
            }

            if (!mFormList.isEmpty()) {
                adapter.notifyDataSetChanged();
                mNoRecordsView.setVisibility(View.GONE);
            } else {
                mNoRecordsView.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void processFormResultResponse(final List<String> response) {

        ArrayList<ProcessDemoObject> list = new ArrayList<>();
        String formID = "";

        try {
            for (final String s : response) {
                JSONObject obj = new JSONObject(s);
                FormResult formResult = new Gson()
                        .fromJson(String.valueOf(obj), FormResult.class);
                com.platform.models.forms.FormResult result = new com.platform.models.forms.FormResult();
                String uuid = UUID.randomUUID().toString();
                result.set_id(uuid);
                result.setFormId(formResult.formID);
                result.setResult(obj.toString());

                formID = formResult.formID;
                list.add(new ProcessDemoObject(uuid,
                        formID, formResult.updatedAt));
            }

            for (final ProcessData data : mDataList) {
                if (data.getId().equals(formID)) {
                    mFormList.put(data.getName(), list);
                }
            }

            if (!mFormList.isEmpty()) {
                adapter.notifyDataSetChanged();
                mNoRecordsView.setVisibility(View.GONE);
            } else {
                mNoRecordsView.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static class ProcessDemoObject {
        String name;
        String id;
        String date;

        private ProcessDemoObject(String name, String id, final String date) {
            this.name = name;
            this.id = id;
            this.date = date;
        }

        public String getDate() {
            return date;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

    }

    static class FormResult {

        @SerializedName("form_id")
        String formID;

        @SerializedName("updated_at")
        String updatedAt;

        @SerializedName("_id")
        _ID mID;
    }

    static class _ID {
        @SerializedName("$oid")
        String oid;
    }
}
