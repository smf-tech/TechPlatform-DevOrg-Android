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
import com.platform.models.forms.ChoicesByUrlMCResponse;
import com.platform.models.forms.ChoicesByUrlSCResponse;
import com.platform.models.forms.Components;
import com.platform.models.forms.Elements;
import com.platform.models.forms.Form;
import com.platform.models.forms.FormData;
import com.platform.models.forms.StructureCode;
import com.platform.presenter.FormActivityPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.customs.FormComponentCreator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    boolean mIsInEditMode;

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
            List<FormData> formDataList = null; /*DatabaseManager.getDBInstance(getActivity()).getFormSchema(processId);*/

            if (formDataList == null || formDataList.isEmpty()) {
                formPresenter.getProcessDetails(processId);
            } else {
                formModel = new Form();
                formModel.setData(formDataList.get(0));
                initViews();
            }

            mIsInEditMode = getArguments().getBoolean(Constants.PM.EDIT_MODE, false);
            if (mIsInEditMode) {
                formPresenter.getFormResults(processId);
            }
        }
    }

    private void initViews() {
        setActionbar(formModel.getData().getName());
        progressBarLayout = formFragmentView.findViewById(R.id.gen_frag_progress_bar);
        progressBar = formFragmentView.findViewById(R.id.pb_gen_form_fragment);

        Components components = formModel.getData().getComponents();
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

        for (Elements formData : formDataArrayList) {
            if (formData != null && !formData.getType().equals("")) {

                String formDataType = formData.getType();
                switch (formDataType) {
                    case Constants.FormsFactory.TEXT_TEMPLATE:
                        Log.d(TAG, "TEXT_TEMPLATE");
                        addViewToMainContainer(formComponentCreator.textInputTemplate(formData));
                        break;

                    case Constants.FormsFactory.DROPDOWN_TEMPLATE:
                        if (formData.getChoicesByUrl() == null) {
                            Log.d(TAG, "DROPDOWN_TEMPLATE");
                            formComponentCreator.setChoicesByUrlSCResponse(null);
                            Object[] objects = formComponentCreator.dropDownTemplate(formData);
                            addViewToMainContainer((View) objects[0]);
                        }
                        break;

                    case Constants.FormsFactory.RADIO_GROUP_TEMPLATE:
                        Log.d(TAG, "RADIO_GROUP_TEMPLATE");
                        addViewToMainContainer(formComponentCreator.radioGroupTemplate(formData));
                        break;

                    case Constants.FormsFactory.FILE_TEMPLATE:
                        Log.d(TAG, "FILE_TEMPLATE");
                        addViewToMainContainer(formComponentCreator.fileTemplate(formData));
                        break;
                }
            }
        }
    }

    private void renderFilledFormView(final List<Elements> formDataArrayList) {
        customFormView = formFragmentView.findViewById(R.id.ll_form_container);

        getActivity().runOnUiThread(() -> customFormView.removeAllViews());

        for (Elements formData : formDataArrayList) {
            if (formData != null && !formData.getType().equals("")) {

                String formDataType = formData.getType();
                switch (formDataType) {
                    case Constants.FormsFactory.TEXT_TEMPLATE:
                        Log.d(TAG, "TEXT_TEMPLATE");
                        addViewToMainContainer(formComponentCreator.textInputTemplate(formData));
                        break;

                    case Constants.FormsFactory.DROPDOWN_TEMPLATE:
                        if (formData.getChoicesByUrl() == null) {
                            Log.d(TAG, "DROPDOWN_TEMPLATE");
                            formComponentCreator.setChoicesByUrlSCResponse(null);
                            Object[] objects = formComponentCreator.dropDownTemplate(formData);
                            addViewToMainContainer((View) objects[0]);
                        }
                        break;

                    case Constants.FormsFactory.RADIO_GROUP_TEMPLATE:
                        Log.d(TAG, "RADIO_GROUP_TEMPLATE");
                        addViewToMainContainer(formComponentCreator.radioGroupTemplate(formData));
                        break;

                    case Constants.FormsFactory.FILE_TEMPLATE:
                        Log.d(TAG, "FILE_TEMPLATE");
                        addViewToMainContainer(formComponentCreator.fileTemplate(formData));
                        break;
                }
            }
        }
    }

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

        if (mFormJSONObject != null && mElementsListFromDB != null)
            parseSchemaAndFormDetails(mFormJSONObject, mElementsListFromDB);
    }

    @Override
    public void showErrorMessage(String result) {

    }

    @Override
    public void showChoicesByUrl(String result, Elements formData) {
        Log.d(TAG, "DROPDOWN_CHOICES_TEMPLATE");
        switch (formData.getName()) {
            case Constants.ChoicesType.CHOICE_STRUCTURE_CODE:
                ChoicesByUrlSCResponse choicesByUrlSCResponse = new Gson().fromJson(result, ChoicesByUrlSCResponse.class);
                if (choicesByUrlSCResponse != null && choicesByUrlSCResponse.getData() != null &&
                        !choicesByUrlSCResponse.getData().isEmpty()) {
                    List<StructureCode> structureCodeList = choicesByUrlSCResponse.getData();

                    if (structureCodeList != null && !structureCodeList.isEmpty()) {
                        formComponentCreator.setChoicesByUrlSCResponse(choicesByUrlSCResponse);
                        Object[] objects = formComponentCreator.dropDownTemplate(formData);
                        addViewToMainContainer((View) objects[0]);
                    }
                }
                break;

            case Constants.ChoicesType.CHOICE_MACHINE_CODE:
                ChoicesByUrlMCResponse choicesByUrlMCResponse = new Gson().fromJson(result, ChoicesByUrlMCResponse.class);
                if (choicesByUrlMCResponse != null && choicesByUrlMCResponse.getData() != null &&
                        !choicesByUrlMCResponse.getData().isEmpty()) {
                    formComponentCreator.setChoicesByUrlMCResponse(choicesByUrlMCResponse);
                }
                break;
        }

    }

    @Override
    public void showMachineCodes(Elements formData, String response) {
        Object[] objects = formComponentCreator.dropDownTemplate(formData);
        addViewToMainContainer((View) objects[0]);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back_action:
                getActivity().finish();
                break;

            case R.id.btn_submit:
                if (!formComponentCreator.isValid()) {
                    Util.showToast(errorMsg, this);
                } else {
                    saveFormToLocalDatabase();
                    if (Util.isConnected(getActivity())) {
                        formPresenter.setFormId(formModel.getData().getId());
                        formPresenter.setRequestedObject(formComponentCreator.getRequestObject());
                        if (mIsInEditMode) {
                            formPresenter.onSubmitClick(Constants.ONLINE_UPDATE_FORM_TYPE);
                        } else {
                            formPresenter.onSubmitClick(Constants.ONLINE_SUBMIT_FORM_TYPE);
                        }
                    } else {
                        if (formModel.getData() != null) {
                            if (mIsInEditMode) {
                                formPresenter.onSubmitClick(Constants.OFFLINE_UPDATE_FORM_TYPE);
                            } else {
                                formPresenter.onSubmitClick(Constants.OFFLINE_SUBMIT_FORM_TYPE);
                            }
                            Util.showToast("Form saved offline ", getActivity());
                            Log.d(TAG, "Form saved " + formModel.getData().getId());
                        }
                    }
                }
                break;
        }
    }

    private void saveFormToLocalDatabase() {
        SavedForm savedForm = new SavedForm();
        savedForm.setFormId(formModel.getData().getId());
        savedForm.setFormName(formModel.getData().getName());
        savedForm.setSynced(false);

        if (formModel.getData().getCategory() != null &&
                !TextUtils.isEmpty(formModel.getData().getCategory().getName())) {
            savedForm.setFormCategory(formModel.getData().getCategory().getName());
        }

        savedForm.setRequestObject(new Gson().toJson(formComponentCreator.getRequestObject()));
        SimpleDateFormat createdDateFormat =
                new SimpleDateFormat(Constants.LIST_DATE_FORMAT, Locale.getDefault());
        savedForm.setCreatedAt(createdDateFormat.format(new Date()));

        formPresenter.setSavedForm(savedForm);
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        Log.d(TAG, errorMsg);
    }

    public void getFormDataAndParse(final String response) {

        String processId = getArguments().getString(Constants.PM.PROCESS_ID);
        String formId = getArguments().getString(Constants.PM.FORM_ID);
        List<FormData> formDataList = DatabaseManager.getDBInstance(getActivity()).getFormSchema(processId);
        if (formDataList == null || formDataList.isEmpty()) {
            formPresenter.getProcessDetails(processId);
            return;
        }

        mElementsListFromDB = formDataList.get(0).getComponents().getPages().get(0).getElements();
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
            e.printStackTrace();
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
                            element.setAnswer(object.getString(element.getName()));
                            break;

                        case Constants.FormsFactory.DROPDOWN_TEMPLATE:

                            break;

                        case Constants.FormsFactory.RADIO_GROUP_TEMPLATE:
                            element.setAnswer(object.getString(element.getName()));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        renderFilledFormView(elements);
    }

}
