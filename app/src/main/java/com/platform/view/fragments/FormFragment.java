package com.platform.view.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.listeners.FormDataTaskListener;
import com.platform.models.LocaleData;
import com.platform.models.forms.Choice;
import com.platform.models.forms.Column;
import com.platform.models.forms.Components;
import com.platform.models.forms.Elements;
import com.platform.models.forms.Form;
import com.platform.models.forms.FormData;
import com.platform.models.forms.FormResult;
import com.platform.models.forms.Page;
import com.platform.presenter.FormActivityPresenter;
import com.platform.syncAdapter.SyncAdapterUtils;
import com.platform.utility.AppEvents;
import com.platform.utility.Constants;
import com.platform.utility.GPSTracker;
import com.platform.utility.PlatformGson;
import com.platform.utility.Util;
import com.platform.view.activities.FormActivity;
import com.platform.view.customs.FormComponentCreator;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.platform.view.fragments.FormsFragment.viewPager;

@SuppressWarnings({"ConstantConditions", "CanBeFinal"})
public class FormFragment extends Fragment implements FormDataTaskListener,
        View.OnClickListener, FormActivity.DeviceBackButtonListener {

    private final String TAG = this.getClass().getSimpleName();

    private View formFragmentView;
    private LinearLayout customFormView;
    private ProgressBar progressBar;

    private Form formModel;
    private RelativeLayout progressBarLayout;
    private List<Elements> formDataArrayList;
    private FormComponentCreator formComponentCreator;
    private FormActivityPresenter formPresenter;

    private String errorMsg = "";
    private JsonObject mFormJSONObject = null;
    private List<Elements> mElementsListFromDB;
    private boolean mIsInEditMode;
    private String processId;
    private boolean mIsPartiallySaved;

    private Uri outputUri;
    private Uri finalUri;
    private ImageView mFileImageView;
    private String mFormName;
    private GPSTracker gpsTracker;
    private List<Map<String, String>> mUploadedImageUrlList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gpsTracker = new GPSTracker(getActivity());
        if (gpsTracker.isGPSEnabled(getActivity(), this)) {
            if (!gpsTracker.canGetLocation()) {
                gpsTracker.showSettingsAlert();
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        formFragmentView = inflater.inflate(R.layout.fragment_gen_form, container, false);
        return formFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        formPresenter = new FormActivityPresenter(this);
        view.findViewById(R.id.no_offline_form).setVisibility(View.GONE);

        if (getArguments() != null) {
            processId = getArguments().getString(Constants.PM.PROCESS_ID);
            mIsInEditMode = getArguments().getBoolean(Constants.PM.EDIT_MODE, false);
            String formId = getArguments().getString(Constants.PM.FORM_ID);
            FormData formData = DatabaseManager.getDBInstance(getActivity()).getFormSchema(formId);

            mIsPartiallySaved = getArguments().getBoolean(Constants.PM.PARTIAL_FORM);
            if (mIsPartiallySaved) {
                formData = DatabaseManager.getDBInstance(getActivity()).getFormSchema(formId);
            }

            if (formData == null) {
                if (Util.isConnected(getContext())) {
                    formPresenter.getProcessDetails(formId);
                } else {
                    view.findViewById(R.id.no_offline_form).setVisibility(View.VISIBLE);
                    setActionbar("");
                }
            } else {
                formModel = new Form();
                formModel.setData(formData);
                initViews();

                if (mIsInEditMode) {
                    FormResult formResult = DatabaseManager.getDBInstance(getActivity())
                            .getFormResult(processId);
                    if (formResult != null) {
                        getFormDataAndParse(formResult);
                    } else {
                        if (Util.isConnected(getContext()) && !mIsPartiallySaved && !TextUtils.isEmpty(processId)) {
                            String url;
                            if (formModel.getData() != null && formModel.getData().getMicroService() != null
                                    && !TextUtils.isEmpty(formModel.getData().getMicroService().getBaseUrl())
                                    && !TextUtils.isEmpty(formModel.getData().getMicroService().getRoute())) {
                                url = getResources().getString(R.string.form_field_mandatory, formModel.getData().getMicroService().getBaseUrl(),
                                        formModel.getData().getMicroService().getRoute());

                                formPresenter.getFormResults(url);
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetDataFromDBTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            showChoicesByUrl(params[0],
                    PlatformGson.getPlatformGsonInstance().fromJson(params[1], Elements.class));
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            hideProgressBar();
        }

        @Override
        protected void onPreExecute() {
            showProgressBar();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetDataFromDBTaskMD extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            showChoicesByUrlMD(params[0],
                    PlatformGson.getPlatformGsonInstance().fromJson(params[1], Column.class),
                    PlatformGson.getPlatformGsonInstance().<HashMap<String, String>>fromJson(params[2], HashMap.class), Long.parseLong(params[3]));
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            hideProgressBar();
        }

        @Override
        protected void onPreExecute() {
            showProgressBar();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    @Override
    public void onDestroy() {
        if (formDataArrayList != null) {
            formDataArrayList.clear();
            formDataArrayList = null;
        }

        if (mElementsListFromDB != null) {
            mElementsListFromDB.clear();
            mElementsListFromDB = null;
        }

        if (formPresenter != null) {
            formPresenter = null;
        }

        if (formComponentCreator != null) {
            formComponentCreator = null;
        }

        super.onDestroy();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        setActionbar(formModel.getData().getName().getLocaleValue());
        progressBarLayout = formFragmentView.findViewById(R.id.gen_frag_progress_bar);
        progressBar = formFragmentView.findViewById(R.id.pb_gen_form_fragment);

        Components components = formModel.getData().getComponents();
        if (components == null) {
            return;
        }

        formDataArrayList = components.getPages().get(0).getElements();

        if (formDataArrayList != null) {
            formComponentCreator = new FormComponentCreator(this);
            if (!mIsInEditMode) {
                renderFormView(formDataArrayList, formModel.getData().getId());
            }
        }

        ImageView editButton = formFragmentView.findViewById(R.id.toolbar_edit_action);
        boolean isFormEditable = Boolean.parseBoolean(formModel.getData().getEditable());

        if (mIsInEditMode && !mIsPartiallySaved) {
            if (mIsPartiallySaved || isFormEditable) {
                editButton.setVisibility(View.VISIBLE);
                editButton.setOnClickListener(this);
            } else {
                editButton.setVisibility(View.GONE);
                editButton.setOnClickListener(null);
            }

            View layer = formFragmentView.findViewById(R.id.read_only_view);
            layer.setVisibility(View.VISIBLE);
            layer.setOnClickListener(this);

            layer.setOnTouchListener((v, event) -> {
                ScrollView root = formFragmentView.findViewById(R.id.sv_form_view);
                root.onTouchEvent(event);
                return true;
            });
        } else {
            enableEditMode();
        }
    }

    private void enableEditMode() {
        View layer = formFragmentView.findViewById(R.id.read_only_view);
        layer.setVisibility(View.GONE);
        layer.setOnClickListener(null);

        Button submit = formFragmentView.findViewById(R.id.btn_submit);
        submit.setVisibility(View.VISIBLE);
        submit.setOnClickListener(this);
    }

    private void setActionbar(String Title) {
        TextView toolbar_title = formFragmentView.findViewById(R.id.toolbar_title);
        toolbar_title.setText(Title);

        ImageView img_back = formFragmentView.findViewById(R.id.toolbar_back_action);
        img_back.setVisibility(View.VISIBLE);
        img_back.setOnClickListener(this);
    }

    private void renderFormView(final List<Elements> formDataArrayList, String formId) {
        customFormView = formFragmentView.findViewById(R.id.ll_form_container);

        getActivity().runOnUiThread(() -> customFormView.removeAllViews());
        formComponentCreator.clearOldComponents();

        for (Elements elements : formDataArrayList) {
            if (elements != null && !elements.getType().equals("")) {

                String formDataType = elements.getType();
                switch (formDataType) {
                    case Constants.FormsFactory.COMMENT_TEMPLATE:
                    case Constants.FormsFactory.TEXT_TEMPLATE:
                        addViewToMainContainer(formComponentCreator.textInputTemplate(elements));
                        break;

                    case Constants.FormsFactory.DROPDOWN_TEMPLATE:
                        addViewToMainContainer(formComponentCreator.dropDownTemplate(elements, formId,
                                mIsInEditMode, mIsPartiallySaved));

                        if (elements.getChoicesByUrl() == null) {
                            Collections.sort(elements.getChoices(),
                                    (o1, o2) -> o1.getText().getLocaleValue().compareTo(o2.getText().getLocaleValue()));
                            formComponentCreator.updateDropDownValues(elements, elements.getChoices());
                        } else if (elements.getChoicesByUrl() != null) {
                            //Online
                            if (Util.isConnected(getContext())) {
                                //Opened submitted/partially or offline saved form
                                if (mIsInEditMode) {
                                    //Partially saved form
                                    if (mIsPartiallySaved) {
                                        callChoicesAPI(elements.getName());
                                    }
                                    //Submitted form
                                    else {
                                        //Editable submitted form
                                        if (!TextUtils.isEmpty(formModel.getData().getEditable())
                                                && Boolean.parseBoolean(formModel.getData().getEditable())) {
                                            callChoicesAPI(elements.getName());
                                        }
                                        //Non editable submitted form
                                        else {
                                            String response = Util.readFromInternalStorage(this.getContext(),
                                                    formId + "_" + elements.getName());
                                            if (!TextUtils.isEmpty(response)) {
                                                showChoicesByUrlAsync(response, elements);
                                            } else {
                                                callChoicesAPI(elements.getName());
                                            }
                                        }
                                    }
                                }
                                //Opened new form
                                else {
                                    callChoicesAPI(elements.getName());
                                }
                            }
                            //Offline
                            else {
                                String response = Util.readFromInternalStorage(this.getContext(),
                                        formId + "_" + elements.getName());
                                if (!TextUtils.isEmpty(response)) {
                                    showChoicesByUrlAsync(response, elements);
                                }
                            }
                        }
                        break;

                    case Constants.FormsFactory.RADIO_GROUP_TEMPLATE:
                        addViewToMainContainer(formComponentCreator.radioGroupTemplate(elements));
                        break;

                    case Constants.FormsFactory.FILE_TEMPLATE:
                        addViewToMainContainer(formComponentCreator.fileTemplate(elements));
                        break;

                    case Constants.FormsFactory.PANEL:
                        addViewToMainContainer(formComponentCreator.panelTemplate(elements));
                        break;

                    case Constants.FormsFactory.MATRIX_DYNAMIC:
                        addViewToMainContainer(formComponentCreator.matrixDynamicTemplate(formModel.getData(), elements,
                                mIsInEditMode, mIsPartiallySaved, formPresenter));
                        break;
                }
            }
        }
    }

    private void callChoicesAPI(String name) {
        List<Page> pages = formModel.getData().getComponents().getPages();
        for (int pageIndex = 0; pageIndex < pages.size(); pageIndex++) {

            if (pages.get(pageIndex).getElements() != null &&
                    !pages.get(pageIndex).getElements().isEmpty()) {

                for (int elementIndex = 0; elementIndex < pages.get(pageIndex).getElements().size(); elementIndex++) {

                    if (pages.get(pageIndex).getElements().get(elementIndex) != null
                            && pages.get(pageIndex).getElements().get(elementIndex).getChoicesByUrl() != null &&
                            pages.get(pageIndex).getElements().get(elementIndex).getName().equals(name) &&
                            !TextUtils.isEmpty(pages.get(pageIndex).getElements().get(elementIndex).getChoicesByUrl().getUrl()) &&
                            !TextUtils.isEmpty(pages.get(pageIndex).getElements().get(elementIndex).getChoicesByUrl().getTitleName())) {

                        formPresenter.getChoicesByUrl(pages.get(pageIndex).getElements().get(elementIndex),
                                pageIndex, elementIndex, -1, -1, formModel.getData(),
                                pages.get(pageIndex).getElements().get(elementIndex).getChoicesByUrl().getUrl(),
                                new HashMap<>());
                        break;
                    }
                }
            }
        }
    }

    synchronized
    private void addViewToMainContainer(final View view) {
        getActivity().runOnUiThread(() -> {
            if (view != null) {
                customFormView.addView(view);
            }
        });
    }

    @Override
    public void showProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        if (getActivity() == null) return;

        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null && progressBar.isShown()) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public <T> void showNextScreen(T data) {
        formModel = PlatformGson.getPlatformGsonInstance().fromJson((String) data, Form.class);
        initViews();

        if (mIsInEditMode) {
            FormResult formResult = DatabaseManager.getDBInstance(getActivity()).getFormResult(processId);
            if (formResult != null) {
                getFormDataAndParse(formResult);
            } else {
                if (Util.isConnected(getContext())) {
                    String url;
                    if (formModel.getData() != null && formModel.getData().getMicroService() != null
                            && !TextUtils.isEmpty(formModel.getData().getMicroService().getBaseUrl())
                            && !TextUtils.isEmpty(formModel.getData().getMicroService().getRoute())) {

                        url = getResources().getString(R.string.form_field_mandatory, formModel.getData().getMicroService().getBaseUrl(),
                                formModel.getData().getMicroService().getRoute());

                        formPresenter.getFormResults(url);
                    }
                }
            }
        }
    }

    @Override
    public void showErrorMessage(String result) {
        Log.d(TAG, "FORM_FRAGMENT_ERROR:" + result);
    }

    @Override
    public void showChoicesByUrlAsync(String result, Elements elements) {
        new GetDataFromDBTask().execute(result, PlatformGson.getPlatformGsonInstance().toJson(elements));
    }

    public void showChoicesByUrlAsyncMD(String result, Column column, HashMap<String, String> matrixDynamicInnerMap, long rowIndex) {
        new GetDataFromDBTaskMD().execute(result, PlatformGson.getPlatformGsonInstance().toJson(column),
                PlatformGson.getPlatformGsonInstance().toJson(matrixDynamicInnerMap),
                String.valueOf(rowIndex));
    }

    private void showChoicesByUrl(String result, Elements elements) {
        List<Choice> choiceValues = new ArrayList<>();
        try {
            LocaleData text;
            String value;

            JsonObject outerObj = PlatformGson.getPlatformGsonInstance().fromJson(result, JsonObject.class);
            JsonArray dataArray = outerObj.getAsJsonArray(Constants.RESPONSE_DATA);
            for (int index = 0; index < dataArray.size(); index++) {
                JsonObject innerObj = dataArray.get(index).getAsJsonObject();
                if (elements.getChoicesByUrl() != null && !TextUtils.isEmpty(elements.getChoicesByUrl().getTitleName())) {
                    if (elements.getChoicesByUrl().getTitleName().contains(Constants.KEY_SEPARATOR)) {
                        StringTokenizer titleTokenizer
                                = new StringTokenizer(elements.getChoicesByUrl().getTitleName(), Constants.KEY_SEPARATOR);
                        StringTokenizer valueTokenizer
                                = new StringTokenizer(elements.getChoicesByUrl().getValueName(), Constants.KEY_SEPARATOR);
                        JsonObject obj = innerObj.getAsJsonObject(titleTokenizer.nextToken());

                        String title = titleTokenizer.nextToken();
                        try {
                            text = PlatformGson.getPlatformGsonInstance()
                                    .fromJson(obj.get(title).getAsString(), LocaleData.class);
                        } catch (Exception e) {
                            text = new LocaleData(obj.get(title).getAsString());
                        }
                        //Ignore first value of valueToken
                        valueTokenizer.nextToken();
                        value = obj.get(valueTokenizer.nextToken()).getAsString();
                    } else {
                        try {
                            text = PlatformGson.getPlatformGsonInstance()
                                    .fromJson(innerObj.get(elements.getChoicesByUrl().getTitleName()).getAsString(), LocaleData.class);
                        } catch (Exception e) {
                            text = new LocaleData(innerObj.get(elements.getChoicesByUrl().getTitleName()).getAsString());
                        }
                        value = innerObj.get(elements.getChoicesByUrl().getValueName()).getAsString();
                    }

                    Choice choice = new Choice();
                    choice.setText(text);
                    choice.setValue(value);

                    if (!choiceValues.contains(choice)) {
                        choiceValues.add(choice);
                    }
                }
            }

            Collections.sort(choiceValues,
                    (o1, o2) -> o1.getText().getLocaleValue().compareTo(o2.getText().getLocaleValue()));
        } catch (Exception e) {
            Log.e(TAG, "Exception in showChoicesByUrl()" + result);
        }
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> formComponentCreator.updateDropDownValues(elements, choiceValues));
        }
    }

    private void showChoicesByUrlMD(String result, Column column, HashMap<String, String> matrixDynamicInnerMap, final long rowIndex) {
        List<Choice> choiceValues = new ArrayList<>();
        try {
            LocaleData text;
            String value;

            JsonObject outerObj = PlatformGson.getPlatformGsonInstance().fromJson(result, JsonObject.class);
            JsonArray dataArray = outerObj.getAsJsonArray(Constants.RESPONSE_DATA);
            for (int index = 0; index < dataArray.size(); index++) {
                JsonObject innerObj = dataArray.get(index).getAsJsonObject();
                if (column.getChoicesByUrl() != null && !TextUtils.isEmpty(column.getChoicesByUrl().getTitleName())) {
                    if (column.getChoicesByUrl().getTitleName().contains(Constants.KEY_SEPARATOR)) {
                        StringTokenizer titleTokenizer
                                = new StringTokenizer(column.getChoicesByUrl().getTitleName(), Constants.KEY_SEPARATOR);
                        StringTokenizer valueTokenizer
                                = new StringTokenizer(column.getChoicesByUrl().getValueName(), Constants.KEY_SEPARATOR);
                        JsonObject obj = innerObj.getAsJsonObject(titleTokenizer.nextToken());

                        String title = titleTokenizer.nextToken();
                        try {
                            text = PlatformGson.getPlatformGsonInstance()
                                    .fromJson(obj.get(title).getAsString(), LocaleData.class);
                        } catch (Exception e) {
                            text = new LocaleData(obj.get(title).getAsString());
                        }
                        //Ignore first value of valueToken
                        valueTokenizer.nextToken();
                        value = obj.get(valueTokenizer.nextToken()).getAsString();
                    } else {
                        try {
                            text = PlatformGson.getPlatformGsonInstance()
                                    .fromJson(innerObj.get(column.getChoicesByUrl().getTitleName()).getAsString(), LocaleData.class);
                        } catch (Exception e) {
                            text = new LocaleData(innerObj.get(column.getChoicesByUrl().getTitleName()).getAsString());
                        }
                        value = innerObj.get(column.getChoicesByUrl().getValueName()).getAsString();
                    }

                    Choice choice = new Choice();
                    choice.setText(text);
                    choice.setValue(value);

                    if (!choiceValues.contains(choice)) {
                        choiceValues.add(choice);
                    }
                }
            }

            // This code will add submitted value in list and update the adapter, in API response
            // submitted value is not coming hence this is workaround.
            Choice ch = new Choice();
            LocaleData ld = new LocaleData(matrixDynamicInnerMap.get(column.getName()));
            ch.setText(ld);
            ch.setValue(ld.getLocaleValue());

            if (!TextUtils.isEmpty(matrixDynamicInnerMap.get(column.getName())) && !choiceValues.contains(ch)) {
                choiceValues.add(ch);
            }

            Collections.sort(choiceValues,
                    (o1, o2) -> o1.getText().getLocaleValue().compareTo(o2.getText().getLocaleValue()));
        } catch (Exception e) {
            Log.e(TAG, "Exception in showChoicesByUrlMD()" + result);
        }
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> formComponentCreator
                    .updateMatrixDynamicDropDownValues(column, choiceValues, matrixDynamicInnerMap, rowIndex));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back_action:
                if (formFragmentView.findViewById(R.id.btn_submit).getVisibility() == View.VISIBLE) {
                    showConfirmPopUp();
                } else {
                    getActivity().finish();
                }
                break;

            case R.id.toolbar_edit_action:
                enableEditMode();
                break;

            case R.id.read_only_view:
                break;

            case R.id.btn_submit:
                if (!formComponentCreator.isValid()) {
                    Util.showToast(errorMsg, this);
                } else {
                    if (Util.isConnected(getActivity())) {
                        Location location = gpsTracker.getLocation();
                        String strLat, strLong;
                        if (location != null) {
                            strLat = String.valueOf(location.getLatitude());
                            strLong = String.valueOf(location.getLongitude());
                        } else {
                            strLat = gpsTracker.getLatitude();
                            strLong = gpsTracker.getLongitude();
                        }

                        HashMap<String, String> requestObject = formComponentCreator.getRequestObject();
                        requestObject.put(Constants.Location.LATITUDE, strLat);
                        requestObject.put(Constants.Location.LONGITUDE, strLong);

                        formPresenter.setRequestedObject(requestObject);
                        formPresenter.setMatrixDynamicValuesMap(formComponentCreator.getMatrixDynamicValuesMap());

                        String url = null;
                        if (formModel.getData() != null && formModel.getData().getMicroService() != null
                                && !TextUtils.isEmpty(formModel.getData().getMicroService().getBaseUrl())
                                && !TextUtils.isEmpty(formModel.getData().getMicroService().getRoute())) {
                            url = getResources().getString(R.string.form_field_mandatory, formModel.getData().getMicroService().getBaseUrl(),
                                    formModel.getData().getMicroService().getRoute());
                        }

                        if (mIsInEditMode && !mIsPartiallySaved) {
                            formPresenter.onSubmitClick(Constants.ONLINE_UPDATE_FORM_TYPE, url,
                                    formModel.getData().getId(), processId, mUploadedImageUrlList);
                        } else {
                            formPresenter.onSubmitClick(Constants.ONLINE_SUBMIT_FORM_TYPE, url,
                                    formModel.getData().getId(), processId, mUploadedImageUrlList);
                        }
                    } else {
                        if (formModel.getData() != null) {

                            saveFormToLocalDatabase();

                            if (mIsInEditMode) {
                                formPresenter.onSubmitClick(Constants.OFFLINE_UPDATE_FORM_TYPE,
                                        null, formModel.getData().getId(), processId, null);
                            } else {
                                formPresenter.onSubmitClick(Constants.OFFLINE_SUBMIT_FORM_TYPE,
                                        null, formModel.getData().getId(), null, null);
                            }

                            Intent intent = new Intent(SyncAdapterUtils.PARTIAL_FORM_ADDED);
                            LocalBroadcastManager.getInstance(getContext().getApplicationContext())
                                    .sendBroadcast(intent);

                            AppEvents.trackAppEvent(getString(R.string.event_form_saved_offline,
                                    formModel.getData().getName()));

                            Util.showToast(getResources().getString(R.string.form_saved_offline), getActivity());
                            Log.d(TAG, "Form saved " + formModel.getData().getId());
                            Objects.requireNonNull(getActivity()).onBackPressed();
                        }
                    }
                }
                break;
        }
    }

    private void storePartiallySavedForm() {
        FormData formData = formModel.getData();
        FormResult result = new FormResult();
        result.set_id(UUID.randomUUID().toString());

        result.setFormId(formData.getId());
        result.setFormCategoryLocale(formData.getCategory().getName());
        result.setFormNameLocale(formData.getName());
        result.setFormStatus(SyncAdapterUtils.FormStatus.PARTIAL);
        result.setCreatedAt(Util.getCurrentTimeStamp());

        if (formData.getCategory() != null) {
            LocaleData category = formData.getCategory().getName();
            if (category != null) {
                result.setFormCategoryLocale(category);
            }
        }

        //Save normal values to JsonObject
        if (formComponentCreator != null && formComponentCreator.getRequestObject() != null) {
            String json = PlatformGson.getPlatformGsonInstance().toJson(formComponentCreator.getRequestObject());
            JsonObject obj = PlatformGson.getPlatformGsonInstance().fromJson(json, JsonObject.class);
            if (obj != null) {
                if (mUploadedImageUrlList != null && !mUploadedImageUrlList.isEmpty()) {
                    for (final Map<String, String> map : mUploadedImageUrlList) {
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            obj.addProperty(entry.getKey(), entry.getValue());
                        }
                    }
                }

                //Save matrix dynamic values to JsonObject
                if (formComponentCreator.getMatrixDynamicValuesMap() != null &&
                        !formComponentCreator.getMatrixDynamicValuesMap().isEmpty()) {

                    HashMap<String, List<HashMap<String, String>>> matrixDynamicValuesMap
                            = formComponentCreator.getMatrixDynamicValuesMap();

                    for (Map.Entry<String, List<HashMap<String, String>>> entry : matrixDynamicValuesMap.entrySet()) {
                        String elementName = entry.getKey();
                        List<HashMap<String, String>> matrixDynamicValuesList = matrixDynamicValuesMap.get(elementName);
                        JsonArray jsonArray = new JsonArray();

                        for (HashMap<String, String> matrixDynamicInnerMap : matrixDynamicValuesList) {
                            JsonObject jsonObject = new JsonObject();
                            for (Map.Entry<String, String> valueEntry : matrixDynamicInnerMap.entrySet()) {
                                jsonObject.addProperty(valueEntry.getKey(), valueEntry.getValue());
                            }
                            jsonArray.add(jsonObject);
                        }
                        obj.add(elementName, jsonArray);
                    }
                }
                result.setResult(obj.toString());
            }
        }

        if (mIsPartiallySaved) {
            String processId = getArguments().getString(Constants.PM.PROCESS_ID);
            FormResult form = DatabaseManager.getDBInstance(getActivity()).getPartiallySavedForm(processId);
            if (form != null) {
                result.set_id(form.get_id());
            }
            DatabaseManager.getDBInstance(getActivity()).updateFormResult(result);
        } else {
            DatabaseManager.getDBInstance(getActivity()).insertFormResult(result);
        }

        Intent intent = new Intent(SyncAdapterUtils.PARTIAL_FORM_ADDED);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

        if (viewPager != null) {
            viewPager.getAdapter().getItemPosition(null);
            viewPager.getAdapter().notifyDataSetChanged();
        }
    }

    private void showConfirmPopUp() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        // Setting Dialog Title
        alertDialog.setTitle(getString(R.string.app_name_ss));
        // Setting Dialog Message
        alertDialog.setMessage(getString(R.string.msg_confirm));
        // Setting Icon to Dialog
        alertDialog.setIcon(R.mipmap.app_logo);
        // Setting CANCEL Button
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no),
                (dialogInterface, i) -> getActivity().finish());

        // Setting OK Button
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes), (dialogInterface, i) -> {
            if (formModel != null && formModel.getData() != null) {
                AppEvents.trackAppEvent(getString(R.string.event_form_saved, formModel.getData().getName()));
                if (formFragmentView.findViewById(R.id.btn_submit).getVisibility() == View.VISIBLE) {
                    storePartiallySavedForm();
                }
            }
            getActivity().finish();
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private void saveFormToLocalDatabase() {
        FormData formData = formModel.getData();

        FormResult result;
        if (mIsPartiallySaved || mIsInEditMode) {
            result = DatabaseManager.getDBInstance(getActivity()).getFormResult(processId);
        } else {
            result = new FormResult();
            result.setFormId(formData.getId());
            result.setFormNameLocale(formData.getName());
            result.setCreatedAt(Util.getCurrentTimeStamp());
            String locallySavedFormID = UUID.randomUUID().toString();
            result.set_id(locallySavedFormID);
        }
        result.setFormStatus(SyncAdapterUtils.FormStatus.UN_SYNCED);

        if (formData.getCategory() != null) {
            LocaleData category = formData.getCategory().getName();
            if (category != null) {
                result.setFormCategoryLocale(category);
            }
        }

        if (formComponentCreator != null && formComponentCreator.getRequestObject() != null) {
            String json = PlatformGson.getPlatformGsonInstance().toJson(formComponentCreator.getRequestObject());
            JsonObject obj = PlatformGson.getPlatformGsonInstance().fromJson(json, JsonObject.class);

            //Save matrix dynamic values to JsonObject
            if (formComponentCreator.getMatrixDynamicValuesMap() != null &&
                    !formComponentCreator.getMatrixDynamicValuesMap().isEmpty()) {

                HashMap<String, List<HashMap<String, String>>> matrixDynamicValuesMap
                        = formComponentCreator.getMatrixDynamicValuesMap();

                for (Map.Entry<String, List<HashMap<String, String>>> entry : matrixDynamicValuesMap.entrySet()) {
                    String elementName = entry.getKey();
                    List<HashMap<String, String>> matrixDynamicValuesList = matrixDynamicValuesMap.get(elementName);
                    JsonArray jsonArray = new JsonArray();

                    for (HashMap<String, String> matrixDynamicInnerMap : matrixDynamicValuesList) {
                        JsonObject jsonObject = new JsonObject();
                        for (Map.Entry<String, String> valueEntry : matrixDynamicInnerMap.entrySet()) {
                            jsonObject.addProperty(valueEntry.getKey(), valueEntry.getValue());
                        }
                        jsonArray.add(jsonObject);
                    }
                    obj.add(elementName, jsonArray);
                }

                result.setRequestObject(json + PlatformGson.getPlatformGsonInstance()
                        .toJson(formComponentCreator.getMatrixDynamicValuesMap()));
            } else {
                result.setRequestObject(json);
            }

            if (obj != null) {
                result.setResult(obj.toString());
                formPresenter.setSavedForm(result);
                if (mIsPartiallySaved || mIsInEditMode) {
                    DatabaseManager.getDBInstance(getActivity()).updateFormResult(result);
                } else {
                    DatabaseManager.getDBInstance(getActivity()).insertFormResult(result);
                }
            }
        }
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        Log.d(TAG, errorMsg);
    }

    public void getFormDataAndParse(final String response) {

        String processId = getArguments().getString(Constants.PM.PROCESS_ID);
        String formId = getArguments().getString(Constants.PM.FORM_ID);
        FormData formData = DatabaseManager.getDBInstance(
                Objects.requireNonNull(getActivity()).getApplicationContext())
                .getFormSchema(processId);

        if (formData != null) {
            mElementsListFromDB = formData.getComponents().getPages().get(0).getElements();
            Log.e(TAG, "Form schema fetched from database.");
        }

        JsonObject object = PlatformGson.getPlatformGsonInstance().fromJson(response, JsonObject.class);
        JsonArray values = object.getAsJsonArray("values");
        for (int i = 0; i < values.size(); i++) {
            mFormJSONObject = PlatformGson.getPlatformGsonInstance()
                    .fromJson(String.valueOf(values.get(i)), JsonObject.class);

            String oid;
            try {
                oid = mFormJSONObject.get("_id").getAsJsonObject().get("$oid").getAsString();
            } catch (Exception e) {
                oid = mFormJSONObject.get("_id").getAsString();
            }

            if (oid.equals(formId)) {
                Log.e(TAG, "Form result\n" + mFormJSONObject.toString());
                break;
            }
        }

        if (formComponentCreator != null) {
            parseSchemaAndFormDetails(mFormJSONObject, mElementsListFromDB, formId);
        }
    }

    private void getFormDataAndParse(final FormResult response) {
        String formId = getArguments().getString(Constants.PM.FORM_ID);
        FormData formData;
        if (formModel.getData() == null) {

            formData = DatabaseManager.getDBInstance(
                    Objects.requireNonNull(getActivity()).getApplicationContext())
                    .getFormSchema(formId);

            if (formData == null || formData.getComponents() == null) {
                if (Util.isConnected(getContext())) {
                    formPresenter.getProcessDetails(formId);
                }
                return;
            }
        } else {
            formData = formModel.getData();
        }

        mElementsListFromDB = formData.getComponents().getPages().get(0).getElements();
        Log.e(TAG, "Form schema fetched from database.");

        mFormJSONObject = PlatformGson.getPlatformGsonInstance().fromJson(response.getResult(), JsonObject.class);

        if (formComponentCreator != null) {
            parseSchemaAndFormDetails(mFormJSONObject, mElementsListFromDB, formId);
        }
    }

    private void parseSchemaAndFormDetails(final JsonObject object, final List<Elements> elements, String formId) {
        if (object == null || elements == null || elements.size() == 0) {
            return;
        }

        HashMap<String, String> requestedObject = new HashMap<>();
        HashMap<String, List<HashMap<String, String>>> matrixDynamicValuesMap = new HashMap<>();
        for (final Elements element : elements) {
            if (object.has(element.getName())) {

                String type = element.getType();
                switch (type) {
                    case Constants.FormsFactory.TEXT_TEMPLATE:
                    case Constants.FormsFactory.COMMENT_TEMPLATE:
                    case Constants.FormsFactory.DROPDOWN_TEMPLATE:
                    case Constants.FormsFactory.RADIO_GROUP_TEMPLATE:
                    case Constants.FormsFactory.FILE_TEMPLATE:
                        element.setAnswer(object.get(element.getName()).getAsString());
                        requestedObject.put(element.getName(), element.getAnswer());
                        break;

                    case Constants.FormsFactory.MATRIX_DYNAMIC:
                        JsonArray valuesArray = object.get(element.getName()).getAsJsonArray();
                        List<HashMap<String, String>> valuesList = new ArrayList<>();

                        for (int valuesArrayIndex = 0; valuesArrayIndex < valuesArray.size(); valuesArrayIndex++) {
                            JsonObject jsonObject = valuesArray.get(valuesArrayIndex).getAsJsonObject();
                            HashMap<String, String> valuesMap = new HashMap<>();

                            for (int columnIndex = 0; columnIndex < element.getColumns().size(); columnIndex++) {
                                JsonElement jsonElement = jsonObject.get(element.getColumns().get(columnIndex).getName());
                                valuesMap.put(element.getColumns().get(columnIndex).getName(),
                                        jsonElement != null ? jsonElement.getAsString() : "");
                            }
                            valuesList.add(valuesMap);
                        }

                        element.setAnswerArray(valuesList);
                        matrixDynamicValuesMap.put(element.getName(), element.getAnswerArray());
                        break;
                }
            }
        }

        formComponentCreator.setRequestObject(requestedObject);
        formComponentCreator.setMatrixDynamicValuesMap(matrixDynamicValuesMap);
        renderFormView(elements, formId);
    }

    public void choosePhotoFromGallery(final View view, final String name) {
        mFileImageView = (ImageView) view;
        mFormName = name;
        try {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, Constants.CHOOSE_IMAGE_FROM_GALLERY);
        } catch (ActivityNotFoundException e) {
            Util.showToast(getString(R.string.msg_error_in_photo_gallery), this);
        }
    }

    public void takePhotoFromCamera(final View view, final String name) {
        mFileImageView = (ImageView) view;
        mFormName = name;
        try {
            //use standard intent to capture an image
            String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/MV/Image/picture.jpg";

            File imageFile = new File(imageFilePath);
            outputUri = FileProvider.getUriForFile(getContext(), getContext().getPackageName()
                    + ".file_provider", imageFile);

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(takePictureIntent, Constants.CHOOSE_IMAGE_FROM_CAMERA);
        } catch (ActivityNotFoundException e) {
            Util.showToast(getString(R.string.msg_image_capture_not_support), this);
        } catch (SecurityException e) {
            Util.showToast(getString(R.string.msg_take_photo_error), this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.CHOOSE_IMAGE_FROM_CAMERA && resultCode == RESULT_OK) {
            try {
                String imageFilePath = getImageName();
                if (imageFilePath == null) {
                    return;
                }

                finalUri = Util.getUri(imageFilePath);
                Crop.of(outputUri, finalUri).start(getContext(), this);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else if (requestCode == Constants.CHOOSE_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    String imageFilePath = getImageName();
                    if (imageFilePath == null) {
                        return;
                    }

                    outputUri = data.getData();
                    finalUri = Util.getUri(imageFilePath);
                    Crop.of(outputUri, finalUri).start(getContext(), this);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            try {
                mFileImageView.setImageURI(finalUri);
                final File imageFile = new File(Objects.requireNonNull(finalUri.getPath()));

                if (Util.isConnected(getContext())) {
                    formPresenter.uploadProfileImage(imageFile, Constants.Image.IMAGE_TYPE_FILE, mFormName);
                } else {
                    Util.showToast(getResources().getString(R.string.msg_no_network), this);
                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.CAMERA_REQUEST:
                Log.e(TAG, "Camera Permission Granted");
                formComponentCreator.showPictureDialog();
                break;

            case Constants.GPS_REQUEST:
                if (!gpsTracker.canGetLocation()) {
                    gpsTracker.showSettingsAlert();
                }
                break;
        }
    }

    private String getImageName() {
        long time = new Date().getTime();
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + Constants.Image.IMAGE_STORAGE_DIRECTORY);
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                Log.e(TAG, "Failed to create directory!");
                return null;
            }
        }
        return Constants.Image.IMAGE_STORAGE_DIRECTORY + Constants.Image.FILE_SEP
                + Constants.Image.IMAGE_PREFIX + time + Constants.Image.IMAGE_SUFFIX;
    }

    @Override
    public void onDeviceBackButtonPressed() {
        if (formFragmentView.findViewById(R.id.no_offline_form).getVisibility() == View.VISIBLE ||
                formFragmentView.findViewById(R.id.btn_submit).getVisibility() != View.VISIBLE) {
            getActivity().finish();
        } else {
            showConfirmPopUp();
        }
    }

    public List<Map<String, String>> getUploadedImages() {
        return mUploadedImageUrlList;
    }

    public void onImageUploaded(final Map<String, String> uploadedImageUrlList) {
        mUploadedImageUrlList.add(uploadedImageUrlList);
    }
}
