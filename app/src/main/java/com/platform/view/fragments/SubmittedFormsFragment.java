package com.platform.view.fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.listeners.FormStatusCallListener;
import com.platform.models.LocaleData;
import com.platform.models.common.Microservice;
import com.platform.models.pm.ProcessData;
import com.platform.models.pm.Processes;
import com.platform.presenter.FormStatusFragmentPresenter;
import com.platform.syncAdapter.SyncAdapterUtils;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.FormActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.platform.presenter.PMFragmentPresenter.getAllNonSyncedSavedForms;
import static com.platform.utility.Constants.DATE_FORMAT;
import static com.platform.utility.Constants.FORM_DATE_FORMAT;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubmittedFormsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("CanBeFinal")
public class SubmittedFormsFragment extends Fragment implements FormStatusCallListener {
    private static final String TAG = SubmittedFormsFragment.class.getSimpleName();

    private LinearLayout lnrOuter;
    private ArrayList<String> processCategoryList = new ArrayList<>();
    private HashMap<String, List<ProcessData>> processMap = new HashMap<>();

    public SubmittedFormsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CompletedFormsFragment.
     */
    static SubmittedFormsFragment newInstance() {
        return new SubmittedFormsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lnrOuter = view.findViewById(R.id.lnr_dashboard_forms_category);

        List<ProcessData> processDataArrayList = DatabaseManager.getDBInstance(getActivity()).getAllProcesses();
        if (processDataArrayList != null && !processDataArrayList.isEmpty()) {
            Processes processes = new Processes();
            processes.setData(processDataArrayList);
            populateData(processes);
        } else {
            if (Util.isConnected(getContext())) {
                FormStatusFragmentPresenter presenter = new FormStatusFragmentPresenter(this);
                presenter.getAllProcesses();
            }
        }
    }

    private void setPendingForms() {
        List<com.platform.models.forms.FormResult> savedForms = getAllNonSyncedSavedForms(getContext());
        if (savedForms != null && !savedForms.isEmpty()) {
            List<ProcessData> list = new ArrayList<>();
            for (com.platform.models.forms.FormResult formResult : savedForms) {
                ProcessData object = new ProcessData();
                object.setId(formResult.getFormId());
                object.setFormTitle(formResult.getFormTitle());
                object.setName(new LocaleData(formResult.getFormName()));
                Microservice microservice = new Microservice();
                microservice.setUpdatedAt(formResult.getCreatedAt());
                object.setMicroservice(microservice);
                list.add(object);
            }

            processMap.put(SyncAdapterUtils.SYNCING_PENDING, list);
            processCategoryList.add(SyncAdapterUtils.SYNCING_PENDING);
        }
        /*else {
            // FIXME: 06-03-2019 Remove this code block
            List<ProcessData> list = new ArrayList<>();
            ProcessData object = new ProcessData();
            object.setId(UUID.randomUUID().toString());
            object.setFormTitle("Title 1");
            LocaleData name = new LocaleData();
            name.setLocaleValue("Title 1");
            object.setName(name);
            Microservice microservice = new Microservice();
            microservice.setUpdatedAt(Util.getFormattedDate(new Date().toString()));
            object.setMicroservice(microservice);
            list.add(object);

            processMap.put(SyncAdapterUtils.SYNCING_PENDING, list);
            processCategoryList.add(SyncAdapterUtils.SYNCING_PENDING);
        }*/
    }

    private void populateData(Processes process) {
        if (process != null) {
            processCategoryList.clear();
            processMap.clear();

            setPendingForms();

            for (ProcessData data : process.getData()) {
                if (data != null && data.getCategory() != null && !TextUtils.isEmpty(data.getCategory().getName().getLocaleValue())) {
                    String categoryName = data.getCategory().getName().getLocaleValue();
                    if (processMap.containsKey(categoryName) && processMap.get(categoryName) != null) {
                        List<ProcessData> processData = processMap.get(categoryName);
                        processData.add(data);
                        processMap.put(categoryName, processData);

//                        addProcessDataInDatabase(data);
                    } else {
                        List<ProcessData> processData = new ArrayList<>();
                        processData.add(data);
                        processMap.put(categoryName, processData);
                        processCategoryList.add(categoryName);

//                        addProcessDataInDatabase(data);
                    }
                }
            }

            for (int index = 0; index < processMap.size(); index++) {
                if (processMap != null && !TextUtils.isEmpty(processCategoryList.get(index)) &&
                        processMap.get(processCategoryList.get(index)) != null) {

                    ProcessData data = processMap.get(processCategoryList.get(index)).get(0);
                    List<String> localFormResults = DatabaseManager.getDBInstance(getActivity())
                            .getAllFormResults(data.getId(), SyncAdapterUtils.FormStatus.SYNCED);

                    if (localFormResults == null || localFormResults.isEmpty()) {
                        if (Util.isConnected(getContext())) {
                            new FormStatusFragmentPresenter(this)
                                    .getSubmittedForms(data.getId());
                        }
                    } else {
                        List<ProcessData> processData = new ArrayList<>();
                        for (final String result : localFormResults) {

                            FormResult formResult = new Gson().fromJson(result, FormResult.class);
                            if (formResult.updatedDateTime != null) {
                                if (isFormOneMonthOld(formResult.updatedDateTime)) {
                                    continue;
                                }
                            }

                            ProcessData object = new ProcessData();
                            object.setId(formResult.formID);
                            object.setFormTitle(formResult.formTitle);
                            object.setName(new LocaleData(formResult.formTitle));
                            Microservice microservice = new Microservice();
                            microservice.setUpdatedAt(formResult.updatedDateTime);
                            object.setMicroservice(microservice);

                            processData.add(object);
                        }

                    /*// FIXME: 06-03-2019 Remove below code block
                    if (processCategoryList.get(index).equals(SyncAdapterUtils.SYNCING_PENDING)) {
                        processData.clear();
                        ProcessData object = new ProcessData();
                        object.setId(UUID.randomUUID().toString());
                        object.setFormTitle("Title 1");
                        object.setName("Title 1");
                        Microservice microservice = new Microservice();
                        microservice.setUpdatedAt(Util.getFormattedDate(new Date().toString()));
                        object.setMicroservice(microservice);
                        processData.add(object);
                    }*/

                        createCategoryLayout(processCategoryList.get(index), processData);
                    }
                }
            }
        }
    }

    private void createCategoryLayout(String categoryName, List<ProcessData> childList) {
        View formTitleView = getLayoutInflater().inflate(R.layout.row_submitted_forms, lnrOuter, false);
        ((TextView) formTitleView.findViewById(R.id.txt_dashboard_form_category_name)).setText(categoryName);
        LinearLayout lnrInner = formTitleView.findViewById(R.id.lnr_inner);

        if (childList == null) return;

        ArrayList<ProcessData> dataList = new ArrayList<>(childList);

        for (final ProcessData data : dataList) {
            addFormItem(categoryName, formTitleView, lnrInner, data);
        }

        lnrOuter.addView(lnrInner);
    }

    private void addFormItem(final String categoryName, final View formTitleView,
                             final LinearLayout lnrInner, final ProcessData data) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.form_sub_item,
                lnrInner, false);

        ColorStateList tintColor = ColorStateList.valueOf(getContext().getResources()
                .getColor(R.color.submitted_form_color, getActivity().getTheme()));
        Drawable drawable = getContext().getDrawable(R.drawable.form_status_indicator_completed);
        FloatingActionButton syncButton = formTitleView.findViewById(R.id.sync_button);

        if (categoryName.equals(SyncAdapterUtils.SYNCING_PENDING)) {
            tintColor = ColorStateList.valueOf(getContext().getResources()
                    .getColor(R.color.red, getActivity().getTheme()));
            drawable = getContext().getDrawable(R.drawable.form_status_indicator_pending_forms);
            syncButton.setVisibility(View.VISIBLE);
            syncButton.setOnClickListener(v -> {
                if (Util.isConnected(getContext())) {
                    Toast.makeText(getContext(), "Sync started...", Toast.LENGTH_SHORT).show();
                    SyncAdapterUtils.manualRefresh();
                } else {
                    Toast.makeText(getContext(), "Internet is not available!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        ImageView formImage = view.findViewById(R.id.form_image);
        formImage.setImageTintList(tintColor);
        view.findViewById(R.id.form_status_indicator).setBackground(drawable);
        ((TextView) view.findViewById(R.id.form_title)).setText(data.getName().getLocaleValue());

        view.setOnClickListener(v -> {
            if (categoryName.equals(SyncAdapterUtils.SYNCING_PENDING)) return;

            Intent intent = new Intent(getContext(), FormActivity.class);
            intent.putExtra(Constants.PM.PROCESS_ID, data.getId());
            intent.putExtra(Constants.PM.FORM_ID, data.getId());
            intent.putExtra(Constants.PM.EDIT_MODE, true);
            intent.putExtra(Constants.PM.PARTIAL_FORM, false);
            getContext().startActivity(intent);
        });

        if (!data.getName().getLocaleValue().equals(getContext().getString(R.string.forms_are_not_available))) {
            if (data.getMicroservice() != null
                    && data.getMicroservice().getUpdatedAt() != null) {
                String formattedDate = Util.getFormattedDate(
                        data.getMicroservice().getUpdatedAt(), FORM_DATE_FORMAT);
                ((TextView) view.findViewById(R.id.form_date))
                        .setText(String.format("on %s", formattedDate));
            }
        } else {
            String formattedDate = Util.getFormattedDate(new Date().toString(), FORM_DATE_FORMAT);
            ((TextView) view.findViewById(R.id.form_date))
                    .setText(String.format("on %s", formattedDate));
        }
        lnrInner.addView(view);
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

            populateData(json);

        }
    }

/*
    private void processResponse(final Processes json) {
        mFormList.clear();
        mDataList.clear();

        FormStatusFragmentPresenter presenter = new FormStatusFragmentPresenter(this);
        for (ProcessData data : json.getData()) {

            String id = data.getId();
            mDataList.add(data);

            DatabaseManager dbInstance = DatabaseManager.getDBInstance(Objects.requireNonNull(getContext()).getApplicationContext());
            List<String> response = dbInstance.getAllFormResults(id, SyncAdapterUtils.FormStatus.SYNCED);
            response.addAll(dbInstance.getAllFormResults(id, SyncAdapterUtils.FormStatus.UN_SYNCED));
            if (!response.isEmpty()) {
                processFormResultResponse(response);
            } else {
                if (Util.isConnected(getContext())) {
                    presenter.getSubmittedForms(id);
                }
            }
        }
    }
*/

    @Override
    public void onMastersFormsLoaded(final String response, final String formId) {
//        processFormResultResponse(response);
    }

/*
    private void processFormResultResponse(final String response) {

        ArrayList<ProcessDemoObject> list = new ArrayList<>();
        String formID = "";

        try {
            if (new JSONObject(response).has(Constants.FormDynamicKeys.VALUES)) {
                JSONArray values = new JSONObject(response).getJSONArray(Constants.FormDynamicKeys.VALUES);
                for (int i = 0; i < values.length(); i++) {

                    FormResult formResult = new Gson().fromJson(String.valueOf(values.get(i)),
                            FormResult.class);

                    String date = formResult.updatedDateTime;
                    if (date == null) {
                        JSONObject object = new JSONObject(response);
                        JSONObject metadata = (JSONObject) object.getJSONArray("metadata").get(0);
                        if (metadata != null && metadata.getJSONObject("form") != null) {
                            date = (String) metadata.getJSONObject("form").get("createdDateTime");
                            date = Util.getFormattedDate(date);
                        } else {
                            date = Util.getFormattedDate(new Date().toString());
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
                    if (obj == null) return;


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
                        formID = metadataObj.getJSONObject(Constants.FormDynamicKeys.FORM).getString(Constants.FormDynamicKeys.FORM_ID);
                    }
                }
            } else {
                list.add(new ProcessDemoObject("No Forms available", "0", "", ""));
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
*/

    private boolean isFormOneMonthOld(final String updatedAt) {
        /*if (Build.VERSION.SDK_INT >= 26) {
            LocalDate formDate = LocalDate.parse(updatedAt);
            LocalDate days30 = LocalDate.now().minusDays(30);

            return formDate.isBefore(days30);

        }*/

        Date eventStartDate;
        DateFormat inputFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        try {
            eventStartDate = inputFormat.parse(updatedAt);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -30);
            Date days30 = calendar.getTime();
            return eventStartDate.before(days30);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

/*
    public static class ProcessDemoObject {
        String name;
        String id;
        String formTitle;
        String date;

        private ProcessDemoObject(String name, String id, final String date, String formTitle) {
            this.name = name;
            this.id = id;
            this.date = date;
            this.formTitle = formTitle;
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

        public String getFormTitle() {
            return formTitle;
        }
    }
*/

    static class FormResult {
        @SerializedName("form_title")
        String formTitle;

        @SerializedName("form_id")
        String formID;

        @SerializedName("updatedDateTime")
        String updatedDateTime;
    }
}
