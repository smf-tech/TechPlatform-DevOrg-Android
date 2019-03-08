package com.platform.view.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.listeners.FormDataTaskListener;
import com.platform.models.LocaleData;
import com.platform.models.forms.Choice;
import com.platform.models.forms.Components;
import com.platform.models.forms.Elements;
import com.platform.models.forms.Form;
import com.platform.models.forms.FormData;
import com.platform.models.forms.FormResult;
import com.platform.presenter.FormActivityPresenter;
import com.platform.syncAdapter.SyncAdapterUtils;
import com.platform.utility.AppEvents;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.adapters.LocaleDataAdapter;
import com.platform.view.customs.FormComponentCreator;
import com.soundcloud.android.crop.Crop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static android.app.Activity.RESULT_OK;
import static com.platform.view.fragments.FormsFragment.viewPager;

@SuppressWarnings("ConstantConditions")
public class FormFragment extends Fragment implements FormDataTaskListener, View.OnClickListener {

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
    private JSONObject mFormJSONObject = null;
    private List<Elements> mElementsListFromDB;
    private boolean mIsInEditMode;
    private String processId;
    private boolean mIsPartiallySaved;
    private String oid;

    private Uri outputUri;
    private Uri finalUri;
    private ImageView mFileImageView;
    private String mFormName;

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

        if (getArguments() != null) {
            processId = getArguments().getString(Constants.PM.PROCESS_ID);
            mIsInEditMode = getArguments().getBoolean(Constants.PM.EDIT_MODE, false);
            FormData formData = DatabaseManager.getDBInstance(getActivity()).getFormSchema(processId);
            mIsPartiallySaved = getArguments().getBoolean(Constants.PM.PARTIAL_FORM);
            if (mIsPartiallySaved) {
                String formId = getArguments().getString(Constants.PM.FORM_ID);
                formData = DatabaseManager.getDBInstance(getActivity()).getFormSchema(formId);
            }

            if (formData == null) {
                if (Util.isConnected(getContext())) {
                    formPresenter.getProcessDetails(processId);
                }
            } else {
                formModel = new Form();
                formModel.setData(formData);
                initViews();

                if (mIsInEditMode) {
                    List<String> formResults;
                    if (mIsPartiallySaved) {
                        String formId = getArguments().getString(Constants.PM.FORM_ID);
                        formResults = DatabaseManager.getDBInstance(getActivity())
                                .getAllFormResults(formId, SyncAdapterUtils.FormStatus.PARTIAL);
                    } else {
                        formResults = DatabaseManager.getDBInstance(getActivity())
                                .getAllFormResults(processId, SyncAdapterUtils.FormStatus.SYNCED);
                    }
                    if (formResults != null && !formResults.isEmpty()) {
                        getFormDataAndParse(formResults);
                    } else {
                        if (Util.isConnected(getContext()) && !mIsPartiallySaved) {
                            formPresenter.getFormResults(processId);
                        }
                    }
                }
            }
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
                renderFormView(formDataArrayList);
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

    private void renderFormView(final List<Elements> formDataArrayList) {
        customFormView = formFragmentView.findViewById(R.id.ll_form_container);

        getActivity().runOnUiThread(() -> customFormView.removeAllViews());
        formComponentCreator.clearOldComponents();

        for (Elements elements : formDataArrayList) {
            if (elements != null && !elements.getType().equals("")) {

                String formDataType = elements.getType();
                switch (formDataType) {
                    case Constants.FormsFactory.TEXT_TEMPLATE:
                        Log.d(TAG, "TEXT_TEMPLATE");
                        addViewToMainContainer(formComponentCreator.textInputTemplate(elements));
                        break;

                    case Constants.FormsFactory.DROPDOWN_TEMPLATE:
                        if (elements.getChoicesByUrl() == null) {
                            Log.d(TAG, "DROPDOWN_CHOICES_TEMPLATE");
                            addViewToMainContainer(formComponentCreator.dropDownTemplate(elements));
                            formComponentCreator.updateDropDownValues(elements, elements.getChoices());
                        } else if (elements.getChoicesByUrl() != null) {
                            addViewToMainContainer(formComponentCreator.dropDownTemplate(elements));
                            if (elements.getChoicesByUrlResponse() != null) {
                                showChoicesByUrl(elements.getChoicesByUrlResponse(), elements);
                            }
                        }
                        break;

                    case Constants.FormsFactory.RADIO_GROUP_TEMPLATE:
                        Log.d(TAG, "RADIO_GROUP_TEMPLATE");
                        addViewToMainContainer(formComponentCreator.radioGroupTemplate(elements));
                        break;

                    case Constants.FormsFactory.FILE_TEMPLATE:
                        Log.d(TAG, "FILE_TEMPLATE");
                        addViewToMainContainer(formComponentCreator.fileTemplate(elements));
                        break;
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
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public <T> void showNextScreen(T data) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocaleData.class, new LocaleDataAdapter());
        Gson gson = builder.create();

        formModel = gson.fromJson((String) data, Form.class);
        initViews();

        if (mIsInEditMode) {
            List<String> formResults = DatabaseManager.getDBInstance(getActivity())
                    .getAllFormResults(processId, SyncAdapterUtils.FormStatus.UN_SYNCED);

            if (formResults != null && !formResults.isEmpty()) {
                getFormDataAndParse(formResults);
            } else {
                if (Util.isConnected(getContext())) {
                    formPresenter.getFormResults(processId);
                }
            }
        }
    }

    @Override
    public void showErrorMessage(String result) {
        Log.d(TAG, "FORM_FRAGMENT_ERROR:" + result);
    }

    @Override
    public void showChoicesByUrl(String result, Elements elements) {
        try {
            Log.d(TAG, "DROPDOWN_CHOICES_BY_URL_TEMPLATE");
            List<Choice> choiceValues = new ArrayList<>();
            LocaleData text;
            String value;

            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(LocaleData.class, new LocaleDataAdapter());
            Gson gson = builder.create();

            JsonObject outerObj = gson.fromJson(result, JsonObject.class);
            JsonArray dataArray = outerObj.getAsJsonArray(Constants.RESPONSE_DATA);
            for (int index = 0; index < dataArray.size(); index++) {
                JsonObject innerObj = dataArray.get(index).getAsJsonObject();
                if (elements.getChoicesByUrl() != null && !TextUtils.isEmpty(elements.getChoicesByUrl().getTitleName())) {
                    if (elements.getChoicesByUrl().getTitleName().contains(Constants.KEY_SEPARATOR)) {
                        StringTokenizer titleTokenizer = new StringTokenizer(elements.getChoicesByUrl().getTitleName(), Constants.KEY_SEPARATOR);
                        StringTokenizer valueTokenizer = new StringTokenizer(elements.getChoicesByUrl().getValueName(), Constants.KEY_SEPARATOR);
                        JsonObject obj = innerObj.getAsJsonObject(titleTokenizer.nextToken());

                        String title = titleTokenizer.nextToken();
                        try {
                            text = gson.fromJson(obj.get(title).getAsString(), LocaleData.class);
                        } catch (Exception e) {
                            text = new LocaleData(obj.get(title).getAsString());
                        }
                        //Ignore first value of valueToken
                        valueTokenizer.nextToken();
                        value = obj.get(valueTokenizer.nextToken()).getAsString();
                    } else {
                        try {
                            text = gson.fromJson(innerObj.get(elements.getChoicesByUrl().getTitleName()).getAsString(), LocaleData.class);
                        } catch (Exception e) {
                            text = new LocaleData(innerObj.get(elements.getChoicesByUrl().getTitleName()).getAsString());
                        }
                        value = innerObj.get(elements.getChoicesByUrl().getValueName()).getAsString();
                    }

                    Choice choice = new Choice();
                    choice.setText(text);
                    choice.setValue(value);

                    if (choiceValues.size() == 0) {
                        choiceValues.add(choice);
                    } else {
                        boolean isFound = false;
                        for (int choiceIndex = 0; choiceIndex < choiceValues.size(); choiceIndex++) {
                            if (choiceValues.get(choiceIndex).getValue().equals(choice.getValue())) {
                                isFound = true;
                                break;
                            }
                        }
                        if (!isFound) {
                            choiceValues.add(choice);
                        }
                    }
                }
            }
            elements.setChoices(choiceValues);
            formComponentCreator.updateDropDownValues(elements, choiceValues);
        } catch (Exception e) {
            Log.e(TAG, "Exception in showChoicesByUrl()");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back_action:
                showConfirmPopUp();
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
                        formPresenter.setRequestedObject(formComponentCreator.getRequestObject());

                        String url = null;
                        if (formModel.getData() != null && formModel.getData().getMicroService() != null
                                && !TextUtils.isEmpty(formModel.getData().getMicroService().getBaseUrl())
                                && !TextUtils.isEmpty(formModel.getData().getMicroService().getRoute())) {
                            url = getResources().getString(R.string.form_field_mandatory, formModel.getData().getMicroService().getBaseUrl(),
                                    formModel.getData().getMicroService().getRoute());
                        }

                        if (mIsInEditMode) {
                            formPresenter.onSubmitClick(Constants.ONLINE_UPDATE_FORM_TYPE, url,
                                    formModel.getData().getId(), oid);
                        } else {
                            formPresenter.onSubmitClick(Constants.ONLINE_SUBMIT_FORM_TYPE, url,
                                    formModel.getData().getId(), null);
                        }
                    } else {
                        if (formModel.getData() != null) {

                            saveFormToLocalDatabase();

                            if (mIsInEditMode) {
                                formPresenter.onSubmitClick(Constants.OFFLINE_UPDATE_FORM_TYPE,
                                        null, formModel.getData().getId(), null);
                            } else {
                                formPresenter.onSubmitClick(Constants.OFFLINE_SUBMIT_FORM_TYPE,
                                        null, formModel.getData().getId(), null);
                            }

                            Intent intent = new Intent(SyncAdapterUtils.EVENT_FORM_ADDED);
                            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

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
        result.setFormCategory(formData.getCategory().getName().getLocaleValue());
        result.setFormName(formData.getName().getLocaleValue());
        result.setFormStatus(SyncAdapterUtils.FormStatus.PARTIAL);
        result.setCreatedAt(Util.getFormattedDate(formData.getMicroService().getCreatedAt()));

        if (formData.getCategory() != null) {
            String category = formData.getCategory().getName().getLocaleValue();
            if (formData.getCategory() != null && !TextUtils.isEmpty(category)) {
                result.setFormCategory(category);
            }
        }

        if (formComponentCreator != null && formComponentCreator.getRequestObject() != null) {
            result.setRequestObject(new Gson().toJson(formComponentCreator.getRequestObject()));
        }

        if (formComponentCreator != null && formComponentCreator.getRequestObject() != null) {
            JSONObject obj = new JSONObject(formComponentCreator.getRequestObject());
            if (obj != null) {
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
            AppEvents.trackAppEvent(getString(R.string.event_form_saved, formModel.getData().getName()));
            if (formFragmentView.findViewById(R.id.btn_submit).getVisibility() == View.VISIBLE) {
                storePartiallySavedForm();
            }
            getActivity().finish();
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private void saveFormToLocalDatabase() {
        FormData formData = formModel.getData();
        FormResult result = new FormResult();
        result.setFormId(formData.getId());
        result.setFormName(formData.getName().getLocaleValue());
        result.setCreatedAt(Util.getFormattedDate(new Date().toString()));

        result.setFormStatus(SyncAdapterUtils.FormStatus.UN_SYNCED);

        if (formData.getCategory() != null) {
            String category = formData.getCategory().getName().getLocaleValue();
            if (formData.getCategory() != null && !TextUtils.isEmpty(category)) {
                result.setFormCategory(category);
            }
        }

        if (formComponentCreator != null && formComponentCreator.getRequestObject() != null) {
            result.setRequestObject(new Gson().toJson(formComponentCreator.getRequestObject()));
        }
        String locallySavedFormID = UUID.randomUUID().toString();
        result.set_id(locallySavedFormID);

        if (formComponentCreator != null && formComponentCreator.getRequestObject() != null) {
            JSONObject obj = new JSONObject(formComponentCreator.getRequestObject());
            if (obj != null) {
                result.setResult(obj.toString());
                formPresenter.setSavedForm(result);
                if (mIsPartiallySaved) {
                    String processId = getArguments().getString(Constants.PM.PROCESS_ID);
                    FormResult form = DatabaseManager.getDBInstance(getActivity())
                            .getPartiallySavedForm(processId);
                    locallySavedFormID = form.get_id();
                    result.set_id(locallySavedFormID);

                    DatabaseManager.getDBInstance(getActivity()).updateFormResult(result);
                } else {
                    DatabaseManager.getDBInstance(getActivity()).insertFormResult(result);
                }
            }
        }

        // TODO: 01-03-2019 Update submitted count also
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

        mElementsListFromDB = formData.getComponents().getPages().get(0).getElements();
        Log.e(TAG, "Form schema fetched from database.");

        try {
            JSONObject object = new JSONObject(response);
            JSONArray values = object.getJSONArray("values");
            for (int i = 0; i < values.length(); i++) {
                mFormJSONObject = new JSONObject(String.valueOf(values.get(i)));
                oid = (String) mFormJSONObject.getJSONObject("_id").get("$oid");
                if (oid.equals(formId)) {
                    Log.e(TAG, "Form result\n" + mFormJSONObject.toString());
                    break;
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        if (formComponentCreator != null)
            parseSchemaAndFormDetails(mFormJSONObject, mElementsListFromDB);
    }

    private void getFormDataAndParse(final List<String> response) {
        String processId = getArguments().getString(Constants.PM.PROCESS_ID);
        String formId = getArguments().getString(Constants.PM.FORM_ID);
        FormData formData;
        if (mIsPartiallySaved) {
            formData = DatabaseManager.getDBInstance(
                    Objects.requireNonNull(getActivity()).getApplicationContext())
                    .getFormSchema(formId);
        } else {
            formData = DatabaseManager.getDBInstance(
                    Objects.requireNonNull(getActivity()).getApplicationContext())
                    .getFormSchema(processId);
        }
        if (formData == null || formData.getComponents() == null) {
            if (Util.isConnected(getContext())) {
                formPresenter.getProcessDetails(processId);
            }
            return;
        }

        mElementsListFromDB = formData.getComponents().getPages().get(0).getElements();
        Log.e(TAG, "Form schema fetched from database.");

        try {
            for (final String s : response) {
                mFormJSONObject = new JSONObject(s);
                oid = (String) mFormJSONObject.getJSONObject("_id").get("$oid");
                if (oid.equals(formId)) {
                    Log.e(TAG, "Form result\n" + mFormJSONObject.toString());
                    break;
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        if (formComponentCreator != null)
            parseSchemaAndFormDetails(mFormJSONObject, mElementsListFromDB);
    }

    private void parseSchemaAndFormDetails(final JSONObject object,
                                           final List<Elements> elements) {
        if (object == null || elements == null || elements.size() == 0) return;

        HashMap<String, String> requestedObject = new HashMap<>();
        for (final Elements element : elements) {
            if (object.has(element.getName())) {
                try {
                    String type = element.getType();
                    switch (type) {
                        case Constants.FormsFactory.TEXT_TEMPLATE:
                        case Constants.FormsFactory.DROPDOWN_TEMPLATE:
                        case Constants.FormsFactory.RADIO_GROUP_TEMPLATE:
                        case Constants.FormsFactory.FILE_TEMPLATE:
                            element.setAnswer(object.getString(element.getName()));
                            requestedObject.put(element.getName(), element.getAnswer());
                            break;
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }

        formComponentCreator.setRequestObject(requestedObject);
        renderFormView(elements);
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
                if (imageFilePath == null) return;

                finalUri = Util.getUri(imageFilePath);
                Crop.of(outputUri, finalUri).asSquare().start(getContext(), this);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else if (requestCode == Constants.CHOOSE_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    String imageFilePath = getImageName();
                    if (imageFilePath == null) return;

                    outputUri = data.getData();
                    finalUri = Util.getUri(imageFilePath);
                    Crop.of(outputUri, finalUri).asSquare().start(getContext(), this);
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
                    Util.showToast(getResources().getString(R.string.no_internet), this);
                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
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

    public void onDeviceBackButtonPressed() {
        showConfirmPopUp();
    }
}
