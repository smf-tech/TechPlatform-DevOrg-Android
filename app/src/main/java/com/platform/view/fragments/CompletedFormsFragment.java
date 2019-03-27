package com.platform.view.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.platform.presenter.PMFragmentPresenter.getAllNonSyncedSavedForms;
import static com.platform.utility.Constants.DATE_FORMAT;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompletedFormsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings({"CanBeFinal", "unused"})
public class CompletedFormsFragment extends Fragment implements FormStatusCallListener {
    private static final String TAG = CompletedFormsFragment.class.getSimpleName();

    private TextView mNoRecordsView;
    private List<ProcessData> mDataList = new ArrayList<>();
    private Map<String, List<ProcessDemoObject>> mFormList = new HashMap<>();
    private FormCategoryAdapter adapter;

    public CompletedFormsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CompletedFormsFragment.
     */
    private static CompletedFormsFragment newInstance() {
        return new CompletedFormsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNoRecordsView = view.findViewById(R.id.no_records_view);
        /*final RecyclerView recyclerView = view.findViewById(R.id.forms_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));*/

        adapter = new FormCategoryAdapter(getContext(), mFormList);
//        recyclerView.setAdapter(adapter);

        setPendingForms();

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

        if (!mFormList.isEmpty()) {
            mNoRecordsView.setVisibility(View.GONE);
        } else {
            mNoRecordsView.setVisibility(View.VISIBLE);
        }
    }

    private void setPendingForms() {
        List<com.platform.models.forms.FormResult> savedForms = getAllNonSyncedSavedForms(getContext());
        if (savedForms != null && !savedForms.isEmpty()) {
            List<ProcessDemoObject> list = new ArrayList<>();
            for (com.platform.models.forms.FormResult form : savedForms) {
                list.add(new ProcessDemoObject(form.get_id(),
                        form.getFormId(), form.getCreatedAt(), ""));
            }
            mFormList.put(getString(R.string.syncing_pending), list);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFailureListener(String message) {
        if (!TextUtils.isEmpty(message)) {
            Log.e(TAG, "onFailureListener :" + message);
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {
        Log.e(TAG, "onErrorListener: " + error.getMessage());
        Util.showToast(error.getMessage(), getContext());
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

            List<String> response = DatabaseManager.getDBInstance(Objects.requireNonNull(getContext())
                    .getApplicationContext())
                    .getAllFormResults(id, SyncAdapterUtils.FormStatus.SYNCED);

            response.addAll(DatabaseManager.getDBInstance(Objects.requireNonNull(getContext())
                    .getApplicationContext())
                    .getAllFormResults(id, SyncAdapterUtils.FormStatus.UN_SYNCED));

            if (!response.isEmpty()) {
                processFormResultResponse(response);
            } else {
                if (Util.isConnected(getContext())) {
                    String url;
                    if (data.getMicroservice() != null
                            && !TextUtils.isEmpty(data.getMicroservice().getBaseUrl())
                            && !TextUtils.isEmpty(data.getMicroservice().getRoute())) {
                        url = getResources().getString(R.string.form_field_mandatory, data.getMicroservice().getBaseUrl(),
                                data.getMicroservice().getRoute());

                        presenter.getSubmittedForms(id, url);
                    }
                }
            }
        }
    }

    @Override
    public void onMastersFormsLoaded(final String response, final String formId) {
        processFormResultResponse(response);
    }

    @Override
    public void onFormResultDeleted(final String formId) {
    }

    private void processFormResultResponse(final String response) {
        ArrayList<ProcessDemoObject> list = new ArrayList<>();
        String formID = "";

        try {
            if (new JSONObject(response).has(Constants.FormDynamicKeys.VALUES)) {
                JSONArray values = new JSONObject(response).getJSONArray(Constants.FormDynamicKeys.VALUES);
                for (int i = 0; i < values.length(); i++) {

                    FormResult formResult = new Gson().fromJson(String.valueOf(values.get(i)), FormResult.class);

                    Long date = formResult.updatedDateTime;
                    if (date == null) {
                        JSONObject object = new JSONObject(response);
                        JSONObject metadata = (JSONObject) object.getJSONArray("metadata").get(0);
                        if (metadata != null && metadata.getJSONObject("form") != null) {
                            date = (Long) metadata.getJSONObject("form").get("createdDateTime");
                        } else {
                            date = Util.getCurrentTimeStamp();
                        }
                    }

                    String uuid = UUID.randomUUID().toString();
                    formID = formResult.formID;
                    list.add(new ProcessDemoObject(uuid, formID, date, formResult.formTitle));

                    com.platform.models.forms.FormResult result = new com.platform.models.forms.FormResult();
                    result.set_id(uuid);
                    result.setFormId(formID);
                    result.setFormStatus(SyncAdapterUtils.FormStatus.SYNCED);

                    JSONObject obj = (JSONObject) values.get(i);
                    if (obj == null) {
                        return;
                    }

                    List<String> localFormResults = DatabaseManager.getDBInstance(getActivity())
                            .getAllFormResults(formID, SyncAdapterUtils.FormStatus.SYNCED);

                    if (!localFormResults.contains(obj.toString())) {
                        result.setResult(obj.toString());
                        DatabaseManager.getDBInstance(getActivity()).insertFormResult(result);
                    }
                }

                if (formID == null) {
                    JSONArray metadata = (JSONArray) new JSONObject(response).get(Constants.FormDynamicKeys.METADATA);
                    if (metadata != null && metadata.length() > 0) {
                        JSONObject metadataObj = metadata.getJSONObject(0);
                        formID = metadataObj.getJSONObject(Constants.FormDynamicKeys.FORM)
                                .getString(Constants.FormDynamicKeys.FORM_ID);
                    }
                }
            } else {
                list.add(new ProcessDemoObject("No Forms available", "0", null, ""));
            }

            for (final ProcessData data : mDataList) {
                if (data.getId().equals(formID)) {
                    mFormList.put(data.getName().getLocaleValue(), list);
                }
            }

            setPendingForms();

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
                result.setFormTitle(formResult.formTitle);
                result.setResult(obj.toString());

                formID = formResult.formID;

                if (formResult.updatedDateTime != null) {
                    if (isFormOneMonthOld(formResult.updatedDateTime)) {
                        continue;
                    }
                }

                list.add(new ProcessDemoObject(uuid,
                        formID, formResult.updatedDateTime, formResult.formTitle));
            }

            for (final ProcessData data : mDataList) {
                if (data.getId().equals(formID)) {
                    mFormList.put(data.getName().getLocaleValue(), list);
                }
            }

            setPendingForms();

            if (!mFormList.isEmpty()) {
                adapter.notifyDataSetChanged();
                mNoRecordsView.setVisibility(View.GONE);
            } else {
                mNoRecordsView.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private boolean isFormOneMonthOld(final Long updatedAt) {
        if (updatedAt != null) {
            Date eventStartDate;
            DateFormat inputFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            try {
                eventStartDate = inputFormat.parse(Util.getDateFromTimestamp(updatedAt));
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, -30);
                Date days30 = calendar.getTime();
                return eventStartDate.before(days30);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return false;
    }

    public static class ProcessDemoObject {
        String name;
        String id;
        String formTitle;
        Long date;

        private ProcessDemoObject(String name, String id, final Long date, String formTitle) {
            this.name = name;
            this.id = id;
            this.date = date;
            this.formTitle = formTitle;
        }

        public Long getDate() {
            return date;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public String getFormTitle() {
            return formTitle;
        }
    }

    @SuppressWarnings("unused")
    static class FormResult {
        @SerializedName("form_title")
        String formTitle;

        @SerializedName("form_id")
        String formID;

        @SerializedName("updatedDateTime")
        Long updatedDateTime;
    }
}
