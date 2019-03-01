package com.platform.view.fragments;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
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
import android.widget.TextView;

import com.google.gson.Gson;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.listeners.FormDataTaskListener;
import com.platform.models.SavedForm;
import com.platform.models.forms.Choice;
import com.platform.models.forms.Components;
import com.platform.models.forms.Elements;
import com.platform.models.forms.Form;
import com.platform.models.forms.FormData;
import com.platform.models.forms.FormResult;
import com.platform.presenter.FormActivityPresenter;
import com.platform.syncAdapter.SyncAdapterUtils;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.customs.FormComponentCreator;
import com.soundcloud.android.crop.Crop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.StringTokenizer;

import static android.app.Activity.RESULT_OK;

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
            String processId = getArguments().getString(Constants.PM.PROCESS_ID);
            FormData formData = DatabaseManager.getDBInstance(getActivity()).getFormSchema(processId);

            if (formData == null) {
                if (Util.isConnected(getContext())) {
                    formPresenter.getProcessDetails(processId);
                }
            } else {
                if (formData.getCategory() == null || formData.getComponents() == null) {
                    if (Util.isConnected(getContext())) {
                        formPresenter.getProcessDetails(processId);
                    }
                } else {
                    formModel = new Form();
                    formModel.setData(formData);
                    initViews();
                }
            }

            mIsInEditMode = getArguments().getBoolean(Constants.PM.EDIT_MODE, false);
            if (mIsInEditMode) {
                List<String> formResults = DatabaseManager.getDBInstance(getActivity()).getAllFormResults(processId);
                if (formResults != null && !formResults.isEmpty()) {
                    getFormDataAndParse(formResults);
                } else {
                    if (Util.isConnected(getContext())) {
                        formPresenter.getFormResults(processId);
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

    private void initViews() {
        setActionbar(formModel.getData().getName());
        progressBarLayout = formFragmentView.findViewById(R.id.gen_frag_progress_bar);
        progressBar = formFragmentView.findViewById(R.id.pb_gen_form_fragment);

        Components components = formModel.getData().getComponents();
        if (components == null) return;

        formDataArrayList = components.getPages().get(0).getElements();

        if (formDataArrayList != null) {
            formComponentCreator = new FormComponentCreator(this);
            renderFormView();
        }

        Button submit = formFragmentView.findViewById(R.id.btn_submit);
        submit.setOnClickListener(this);
    }

    private void setActionbar(String Title) {
        TextView toolbar_title = formFragmentView.findViewById(R.id.toolbar_title);
        toolbar_title.setText(Title);

        ImageView img_back = formFragmentView.findViewById(R.id.toolbar_back_action);
        img_back.setVisibility(View.VISIBLE);
        img_back.setOnClickListener(this);
    }

    private void renderFormView() {
        customFormView = formFragmentView.findViewById(R.id.ll_form_container);

        getActivity().runOnUiThread(() -> customFormView.removeAllViews());

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

    private void renderFilledFormView(final List<Elements> formDataArrayList) {
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
        formModel = new Gson().fromJson((String) data, Form.class);
        initViews();

        Form form = new Gson().fromJson(String.valueOf(data), Form.class);
        if (form != null && form.getData() != null) {
            DatabaseManager.getDBInstance(Objects.requireNonNull(getActivity())
                    .getApplicationContext()).insertFormSchema(form.getData());
        }

        if (mFormJSONObject != null && mElementsListFromDB != null)
            parseSchemaAndFormDetails(mFormJSONObject, mElementsListFromDB);
    }

    @Override
    public void showErrorMessage(String result) {
        Log.d(TAG, "FORM_FRAGMENT_ERROR:" + result);
    }

    @Override
    public void showChoicesByUrl(String result, Elements elements) {
        Log.d(TAG, "DROPDOWN_CHOICES_BY_URL_TEMPLATE");
        try {
            List<Choice> choiceValues = new ArrayList<>();
            String text = "";
            String value = "";
            JSONObject outerObj = new JSONObject(result);
            JSONArray dataArray = outerObj.getJSONArray(Constants.RESPONSE_DATA);
            for (int index = 0; index < dataArray.length(); index++) {
                JSONObject innerObj = dataArray.getJSONObject(index);
                if (elements.getChoicesByUrl() != null && !TextUtils.isEmpty(elements.getChoicesByUrl().getTitleName())) {
                    if (elements.getChoicesByUrl().getTitleName().contains(Constants.KEY_SEPARATOR)) {
                        StringTokenizer titleTokenizer = new StringTokenizer(elements.getChoicesByUrl().getTitleName(), Constants.KEY_SEPARATOR);
                        StringTokenizer valueTokenizer = new StringTokenizer(elements.getChoicesByUrl().getValueName(), Constants.KEY_SEPARATOR);
                        JSONObject obj = innerObj.getJSONObject(titleTokenizer.nextToken());
                        text = obj.getString(titleTokenizer.nextToken());
                        //Ignore first value of valueToken
                        valueTokenizer.nextToken();
                        value = obj.getString(valueTokenizer.nextToken());
                    } else {
                        text = innerObj.getString(elements.getChoicesByUrl().getTitleName());
                        value = innerObj.getString(elements.getChoicesByUrl().getValueName());
                    }
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
            formComponentCreator.updateDropDownValues(elements, choiceValues);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back_action:
                showConfirmPopUp();
                break;

            case R.id.btn_submit:
                if (!formComponentCreator.isValid()) {
                    Util.showToast(errorMsg, this);
                } else {
                    saveFormToLocalDatabase();
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
                            formPresenter.onSubmitClick(Constants.ONLINE_UPDATE_FORM_TYPE, url);
                        } else {
                            formPresenter.onSubmitClick(Constants.ONLINE_SUBMIT_FORM_TYPE, url);
                        }
                    } else {
                        if (formModel.getData() != null) {
                            if (mIsInEditMode) {
                                formPresenter.onSubmitClick(Constants.OFFLINE_UPDATE_FORM_TYPE, null);
                            } else {
                                formPresenter.onSubmitClick(Constants.OFFLINE_SUBMIT_FORM_TYPE, null);
                            }

                            Intent intent = new Intent(SyncAdapterUtils.EVENT_FORM_ADDED);
                            LocalBroadcastManager.getInstance(getContext().getApplicationContext()).sendBroadcast(intent);

                            Util.showToast(getResources().getString(R.string.form_saved_offline), getActivity());
                            Log.d(TAG, "Form saved " + formModel.getData().getId());
                            Objects.requireNonNull(getActivity()).onBackPressed();
                        }
                    }
                }
                break;
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
                (dialog, which) -> alertDialog.dismiss());
        // Setting OK Button
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes),
                (dialog, which) -> getActivity().finish());

        // Showing Alert Message
        alertDialog.show();
    }

    private void saveFormToLocalDatabase() {
        SavedForm savedForm = new SavedForm();
        FormData formData = formModel.getData();
        savedForm.setFormId(formData.getId());
        savedForm.setFormName(formData.getName());
        savedForm.setSynced(false);

        if (formData.getCategory() != null) {
            String category = formData.getCategory().getName();
            if (formData.getCategory() != null && !TextUtils.isEmpty(category)) {
                savedForm.setFormCategory(category);
            }
        }

        if (formComponentCreator != null && formComponentCreator.getRequestObject() != null) {
            savedForm.setRequestObject(new Gson().toJson(formComponentCreator.getRequestObject()));
        }
        SimpleDateFormat createdDateFormat =
                new SimpleDateFormat(Constants.LIST_DATE_FORMAT, Locale.getDefault());
        savedForm.setCreatedAt(createdDateFormat.format(new Date()));

        FormResult result = new FormResult();
        result.setFormId(formData.getId());
        result.setFormName(formData.getName());
        result.setSynced(false);

        if (formData.getCategory() != null) {
            String category = formData.getCategory().getName();
            if (formData.getCategory() != null && !TextUtils.isEmpty(category)) {
                result.setFormCategory(category);
            }
        }

        if (formComponentCreator != null && formComponentCreator.getRequestObject() != null) {
            result.setRequestObject(new Gson().toJson(formComponentCreator.getRequestObject()));
        }
        result.set_id(formData.getId());
        result.setFormId(formData.getId());

        if (formComponentCreator != null && formComponentCreator.getRequestObject() != null) {
            JSONObject obj = new JSONObject(formComponentCreator.getRequestObject());
            if (obj != null) {
                result.setResult(obj.toString());
                formPresenter.setSavedForm(result);
                DatabaseManager.getDBInstance(getActivity()).insertFormResult(result);
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
        if (formData == null) {
            if (Util.isConnected(getContext())) {
                formPresenter.getProcessDetails(processId);
            }
            return;
        }

        mElementsListFromDB = formData.getComponents().getPages().get(0).getElements();
        Log.e(TAG, "Form schema fetched from database.");

        try {
            JSONObject object = new JSONObject(response);
            JSONArray values = object.getJSONArray("values");
            for (int i = 0; i < values.length(); i++) {
                mFormJSONObject = new JSONObject(String.valueOf(values.get(i)));
                String id = (String) mFormJSONObject.getJSONObject("_id").get("$oid");
                if (id.equals(formId)) {
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
        FormData formData = DatabaseManager.getDBInstance(
                Objects.requireNonNull(getActivity()).getApplicationContext())
                .getFormSchema(processId);
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
                String id = (String) mFormJSONObject.getJSONObject("_id").get("$oid");
                if (id.equals(formId)) {
                    Log.e(TAG, "Form result\n" + mFormJSONObject.toString());
                    break;
                }
            }
            /*JSONObject object = new JSONObject(response);
            JSONArray values = object.getJSONArray("values");
            for (int i = 0; i < values.length(); i++) {
                mFormJSONObject = new JSONObject(String.valueOf(values.get(i)));
                String id = (String) mFormJSONObject.getJSONObject("_id").get("$oid");
                if (id.equals(formId)) {
                    Log.e(TAG, "Form result\n" + mFormJSONObject.toString());
                    break;
                }
            }*/
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        if (formComponentCreator != null)
            parseSchemaAndFormDetails(mFormJSONObject, mElementsListFromDB);
    }

    private void parseSchemaAndFormDetails(final JSONObject object, final List<Elements> elements) {
        if (object == null || elements == null || elements.size() == 0) return;

        for (final Elements element : elements) {
            if (object.has(element.getName())) {
                try {
                    String type = element.getType();
                    switch (type) {
                        case Constants.FormsFactory.TEXT_TEMPLATE:
                        case Constants.FormsFactory.DROPDOWN_TEMPLATE:
                        case Constants.FormsFactory.RADIO_GROUP_TEMPLATE:
                            element.setAnswer(object.getString(element.getName()));
                            break;
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }

        renderFilledFormView(elements);
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
                /*LinearLayout fileTemplateView = formComponentCreator.getFileTemplateView();
                ImageView viewWithTag = fileTemplateView.findViewWithTag(mFileImageView.getTag());*/
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
}
