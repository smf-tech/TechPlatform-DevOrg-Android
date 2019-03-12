package com.platform.view.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static com.platform.presenter.PMFragmentPresenter.getAllNonSyncedSavedForms;
import static com.platform.syncAdapter.SyncAdapterUtils.EVENT_FORM_ADDED;
import static com.platform.syncAdapter.SyncAdapterUtils.EVENT_SYNC_COMPLETED;
import static com.platform.syncAdapter.SyncAdapterUtils.EVENT_SYNC_FAILED;
import static com.platform.syncAdapter.SyncAdapterUtils.PARTIAL_FORM_REMOVED;
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
    private boolean showNoDataText = true;
    private TextView mNoRecordsView;

    public SubmittedFormsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SubmittedFormsFragment.
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
        mNoRecordsView = view.findViewById(R.id.no_records_view);

        getProcessData();

        IntentFilter filter = new IntentFilter();
        filter.addAction(EVENT_SYNC_COMPLETED);
        filter.addAction(EVENT_SYNC_FAILED);
        filter.addAction(EVENT_FORM_ADDED);
        filter.addAction(PARTIAL_FORM_REMOVED);

        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                if (Objects.requireNonNull(intent.getAction()).equals(EVENT_SYNC_COMPLETED)) {
                    Toast.makeText(context, "Sync completed.", Toast.LENGTH_SHORT).show();
                    getProcessData();
                } else if (Objects.requireNonNull(intent.getAction()).equals(EVENT_FORM_ADDED)) {
                    getProcessData();
                } else if (Objects.requireNonNull(intent.getAction()).equals(PARTIAL_FORM_REMOVED)) {
                    getProcessData();
                } else if (intent.getAction().equals(EVENT_SYNC_FAILED)) {
                    Log.e("PendingForms", "Sync failed!");
                    Toast.makeText(context, "Sync failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }, filter);
    }

    private void getProcessData() {
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
            Map<String, ProcessData> map = new HashMap<>();
            for (com.platform.models.forms.FormResult formResult : savedForms) {
                ProcessData object = new ProcessData();
                object.setId(formResult.getFormId());
                object.setFormTitle(formResult.getFormTitle());
                object.setName(new LocaleData(formResult.getFormName()));
                list.add(object);
                map.put(formResult.get_id(), object);
            }

            Util.sortProcessDataListByCreatedDate(list);
            createCategoryLayout(SyncAdapterUtils.SYNCING_PENDING, list, null, map);
        }
    }

    private void populateData(Processes process) {
        if (process != null) {
            processCategoryList.clear();
            processMap.clear();

            lnrOuter.removeAllViews();

            setPendingForms();

            for (ProcessData data : process.getData()) {
                if (data != null && data.getCategory() != null && !TextUtils.isEmpty(data.getCategory().getName().getLocaleValue())) {
                    String categoryName = data.getCategory().getName().getLocaleValue();
                    if (processMap.containsKey(categoryName) && processMap.get(categoryName) != null) {
                        List<ProcessData> processData = processMap.get(categoryName);
                        if (processData != null) {
                            processData.add(data);
                        }
                        processMap.put(categoryName, processData);
                    } else {
                        List<ProcessData> processData = new ArrayList<>();
                        processData.add(data);
                        processMap.put(categoryName, processData);
                        processCategoryList.add(categoryName);
                    }
                }
            }

            for (int index = 0; index < processMap.size(); index++) {

                List<ProcessData> pData = processMap.get(processCategoryList.get(index));
                if (!TextUtils.isEmpty(processCategoryList.get(index)) && pData != null) {

                    String formID = null;
                    ProcessData data = pData.get(0);
                    List<String> localFormResults = DatabaseManager.getDBInstance(getActivity())
                            .getAllFormResults(data.getId(), SyncAdapterUtils.FormStatus.SYNCED);

                    ProcessData pd = DatabaseManager.getDBInstance(
                            Objects.requireNonNull(getActivity()).getApplicationContext())
                            .getProcessData(data.getId());

                    String submitCount = pd.getSubmitCount();

                    if (submitCount != null && !submitCount.equals("0") && localFormResults.isEmpty()) {
                        if (Util.isConnected(getContext())) {
//                            String url;
//                            if (data.getMicroservice() != null
//                                    && !TextUtils.isEmpty(data.getMicroservice().getBaseUrl())
//                                    && !TextUtils.isEmpty(data.getMicroservice().getRoute())) {
//                                url = getResources().getString(R.string.form_field_mandatory, data.getMicroservice().getBaseUrl(),
//                                        data.getMicroservice().getRoute());
//
//                                new FormStatusFragmentPresenter(this).getSubmittedForms(data.getId(), url);
//                            }
                            new FormStatusFragmentPresenter(this).getSubmittedForms(data.getId());
                        }
                    } else {

                        if (localFormResults == null || localFormResults.isEmpty()) continue;

                        showNoDataText = false;

                        List<ProcessData> processData = new ArrayList<>();

//                        GsonBuilder builder = new GsonBuilder();
//                        builder.registerTypeAdapter(SubmittedFormsFragment.OID.class, new OIDAdapter());

                        for (final String result : localFormResults) {
                            FormResult formResult = new Gson().fromJson(result, FormResult.class);
                            if (formResult.updatedDateTime != null) {
                                if (isFormOneMonthOld(formResult.updatedDateTime)) {
                                    continue;
                                }
                            }

                            formID = formResult.formID;
                            ProcessData object = new ProcessData();
                            if (formResult.mOID != null && formResult.mOID.oid != null) {
                                object.setId(formResult.mOID.oid);
                            }
                            object.setFormTitle(formResult.formTitle);
                            object.setName(new LocaleData(formResult.formTitle));
                            Microservice microservice = new Microservice();
                            microservice.setUpdatedAt(formResult.updatedDateTime);
                            object.setMicroservice(microservice);
                            processData.add(object);
                        }

                        Util.sortProcessDataListByCreatedDate(processData);
                        createCategoryLayout(processCategoryList.get(index), processData, formID, null);
                    }
                }
            }

            mNoRecordsView.setVisibility(showNoDataText ? View.VISIBLE : View.GONE);
        }
    }

    private void createCategoryLayout(String categoryName, List<ProcessData> childList,
                                      String formID, final Map<String, ProcessData> map) {
        if (childList == null) {
            return;
        }

        View formTitleView = getLayoutInflater().inflate(R.layout.row_submitted_forms, lnrOuter, false);
        ((TextView) formTitleView.findViewById(R.id.txt_dashboard_form_category_name)).setText(categoryName);
        LinearLayout lnrInner = formTitleView.findViewById(R.id.lnr_inner);

        ArrayList<ProcessData> dataList = new ArrayList<>(childList);
        for (final ProcessData data : dataList) {
            if (formID == null && map != null) {
                String oid = null;
                for (final Map.Entry<String, ProcessData> entry : map.entrySet()) {
                    if (entry.getValue() == data) {
                        oid = entry.getKey();
                        break;
                    }
                }
                addOfflineFormItem(formTitleView, lnrInner, data, oid);
            } else {
                addFormItem(lnrInner, data, formID);
            }
        }

        lnrOuter.addView(lnrInner);
    }

    private void addFormItem(final LinearLayout lnrInner, final ProcessData data, final String formID) {

        if (getContext() == null) {
            return;
        }

        View view = LayoutInflater.from(getContext()).inflate(R.layout.form_sub_item,
                lnrInner, false);

        ColorStateList tintColor = ColorStateList.valueOf(getContext().getResources()
                .getColor(R.color.submitted_form_color, getContext().getTheme()));

        Drawable drawable = getContext().getDrawable(R.drawable.form_status_indicator_completed);

        ImageView formImage = view.findViewById(R.id.form_image);
        formImage.setImageTintList(tintColor);

        view.findViewById(R.id.form_status_indicator).setBackground(drawable);
        ((TextView) view.findViewById(R.id.form_title)).setText(data.getName().getLocaleValue());

        view.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), FormActivity.class);
            intent.putExtra(Constants.PM.PROCESS_ID, data.getId());
            intent.putExtra(Constants.PM.FORM_ID, formID);
            intent.putExtra(Constants.PM.EDIT_MODE, true);
            intent.putExtra(Constants.PM.PARTIAL_FORM, false);
            getContext().startActivity(intent);
        });

        if (getContext() != null && data.getName() != null
                && data.getName().getLocaleValue() != null && !data.getName().getLocaleValue()
                .equals(getString(R.string.forms_are_not_available))) {

            if (data.getMicroservice() != null && data.getMicroservice().getUpdatedAt() != null) {
                String formattedDate = Util
                        .getDateFromTimestamp(data.getMicroservice().getUpdatedAt());

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

    private void addOfflineFormItem(final View formTitleView,
                                    final LinearLayout lnrInner, final ProcessData data, final String oid) {

        if (getContext() == null) {
            return;
        }

        View view = LayoutInflater.from(getContext()).inflate(R.layout.form_sub_item,
                lnrInner, false);

        FloatingActionButton syncButton = formTitleView.findViewById(R.id.sync_button);
        ColorStateList tintColor = ColorStateList.valueOf(getContext().getResources()
                .getColor(R.color.red, getContext().getTheme()));

        Drawable drawable = getContext().getDrawable(R.drawable.form_status_indicator_pending_forms);
        syncButton.setVisibility(View.VISIBLE);

        syncButton.setOnClickListener(v -> {
            if (Util.isConnected(getContext())) {
                Toast.makeText(getContext(), "Sync started...", Toast.LENGTH_SHORT).show();
                SyncAdapterUtils.manualRefresh();
            } else {
                Toast.makeText(getContext(), "Internet is not available!", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView formImage = view.findViewById(R.id.form_image);
        formImage.setImageTintList(tintColor);

        view.findViewById(R.id.form_status_indicator).setBackground(drawable);
        ((TextView) view.findViewById(R.id.form_title)).setText(data.getName().getLocaleValue());

        view.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), FormActivity.class);
            intent.putExtra(Constants.PM.PROCESS_ID, oid);
            intent.putExtra(Constants.PM.FORM_ID, data.getId());
            intent.putExtra(Constants.PM.EDIT_MODE, true);
            intent.putExtra(Constants.PM.PARTIAL_FORM, false);
            getContext().startActivity(intent);
        });

        if (getContext() != null && data.getName().getLocaleValue() != null &&
                !data.getName().getLocaleValue().equals(getContext().getString(R.string.forms_are_not_available))) {
            String formattedDate = Util.getDateFromTimestamp(
                    Util.getCurrentTimeStamp());

            ((TextView) view.findViewById(R.id.form_date))
                    .setText(String.format("on %s", formattedDate));
        } else {
            String formattedDate = Util.getFormattedDate(new Date().toString(), FORM_DATE_FORMAT);
            ((TextView) view.findViewById(R.id.form_date)).setText(String.format("on %s", formattedDate));
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
            for (ProcessData processData : json.getData()) {
                DatabaseManager.getDBInstance(getContext()).insertProcessData(processData);
            }

            populateData(json);
        }
    }

    @Override
    public void onMastersFormsLoaded(final String response, final String formId) {

    }

    private boolean isFormOneMonthOld(final Long updatedAt) {
        if (updatedAt != null) {
            Date eventStartDate;
            DateFormat inputFormat = new SimpleDateFormat(FORM_DATE_FORMAT, Locale.getDefault());
            try {
                eventStartDate = inputFormat.parse(Util.getDateFromTimestamp(updatedAt));
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, -30);
                Date days30 = calendar.getTime();
                return eventStartDate.before(days30);
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return false;
    }

    static class FormResult {
        @SuppressWarnings("unused")
        @SerializedName("form_title")
        String formTitle;

        @SuppressWarnings("unused")
        @SerializedName("form_id")
        String formID;

        @SuppressWarnings("unused")
        @SerializedName("updatedDateTime")
        Long updatedDateTime;

        @SuppressWarnings("unused")
        @SerializedName("_id")
        OID mOID;
    }

    public static class OID {
        @SuppressWarnings("unused")
        @SerializedName("$oid")
        String oid;

        public OID(String defaultValue) {
            this.oid = defaultValue;
        }

        public String getOID() {
            return oid;
        }
    }
}
