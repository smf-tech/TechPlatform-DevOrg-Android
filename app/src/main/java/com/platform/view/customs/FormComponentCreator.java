package com.platform.view.customs;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.platform.R;
import com.platform.listeners.DropDownValueSelectListener;
import com.platform.models.forms.Choice;
import com.platform.models.forms.Elements;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.utility.Validation;
import com.platform.view.fragments.FormFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

@SuppressWarnings({"ConstantConditions", "CanBeFinal"})
public class FormComponentCreator implements DropDownValueSelectListener {

    private final WeakReference<FormFragment> fragment;
    private final String TAG = this.getClass().getSimpleName();

    private HashMap<String, String> requestObjectMap = new HashMap<>();
    private HashMap<EditText, Elements> editTextElementsHashMap = new HashMap<>();
    private HashMap<String, DropDownTemplate> dependencyMap = new HashMap<>();
    private ArrayList<EditText> editTexts = new ArrayList<>();

    public FormComponentCreator(FormFragment fragment) {
        this.fragment = new WeakReference<>(fragment);
    }

    public View radioGroupTemplate(final Elements formData) {

        if (fragment == null || fragment.get() == null) {
            Log.e(TAG, "View returned null");
            return null;
        }

        RelativeLayout radioTemplateView = (RelativeLayout) View.inflate(
                fragment.get().getContext(), R.layout.form_radio_template, null);

        RadioGroup radioGroupForm = radioTemplateView.findViewById(R.id.rg_form_template);
        TextView txtRadioGroupName = radioTemplateView.findViewById(R.id.txt_form_radio_group_name);
        if (!TextUtils.isEmpty(formData.getTitle())) {
            txtRadioGroupName.setText(formData.getTitle());
        }

        if (formData.getChoices() != null && !formData.getChoices().isEmpty()) {
            for (int index = 0; index < formData.getChoices().size(); index++) {
                RadioButton radioButtonForm = new RadioButton(fragment.get().getContext());
                radioButtonForm.setText(formData.getChoices().get(index).getText());
                radioButtonForm.setId(index);
                radioGroupForm.addView(radioButtonForm);

                radioGroupForm.setOnCheckedChangeListener((radioGroup1, checkedId) -> {
                    if (!TextUtils.isEmpty(formData.getName()) &&
                            !TextUtils.isEmpty(((RadioButton) radioGroupForm.findViewById(
                                    radioGroup1.getCheckedRadioButtonId())).getText())) {

                        requestObjectMap.put(formData.getName(),
                                ((RadioButton) radioGroupForm.findViewById(
                                        radioGroup1.getCheckedRadioButtonId())).getText().toString());
                    } else {
                        requestObjectMap.remove(formData.getName());
                    }
                });

                if (!TextUtils.isEmpty(formData.getAnswer()) && !TextUtils.isEmpty(radioButtonForm.getText())) {
                    if (radioButtonForm.getText().equals(formData.getAnswer())) {
                        radioButtonForm.setChecked(true);
                    }
                } else if (index == 0) {
                    radioButtonForm.setChecked(true);
                }
            }
        }

        return radioTemplateView;
    }

    public synchronized View dropDownTemplate(Elements formData, List<Choice> choiceValues) {
        if (fragment == null || fragment.get() == null) {
            Log.e(TAG, "dropDownTemplate returned null");
            return null;
        }

        DropDownTemplate template = new DropDownTemplate(formData, fragment.get(), this, choiceValues);

        View view;
        if (formData.isRequired() != null) {
            view = template.init(setFieldAsMandatory(formData.isRequired()));
        } else {
            view = template.init(setFieldAsMandatory(false));
        }

        view.setTag(formData.getName());

        if (choiceValues != null && !choiceValues.isEmpty()) {
            for (int index = 0; index < choiceValues.size(); index++) {
                if (!TextUtils.isEmpty(formData.getAnswer()) &&
                        !TextUtils.isEmpty(choiceValues.get(index).getText()) &&
                        formData.getAnswer().equals(choiceValues.get(index).getValue())) {
                    template.setSelectedItem(index);
                }
            }
        }

        if (!TextUtils.isEmpty(formData.getEnableIf())) {
            dependencyMap.put(formData.getEnableIf(), template);
        }

        return view;
    }

    public View textInputTemplate(final Elements formData) {

        if (fragment == null || fragment.get() == null) {
            Log.e(TAG, "View returned null");
            return null;
        }

        RelativeLayout textTemplateView = (RelativeLayout) View.inflate(
                fragment.get().getContext(), R.layout.form_text_template, null);

        EditText textInputField = textTemplateView.findViewById(R.id.edit_form_text_template);
        if (formData != null) {
            if (formData.getValidators() != null && !formData.getValidators().isEmpty()) {
                //set input type
                setInputType(formData.getValidators().get(0).getType(), textInputField);

                //set max length allowed
                if (formData.getValidators().get(0).getMaxLength() != null) {
                    textInputField.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                            formData.getValidators().get(0).getMaxLength())});

                } else if (formData.getValidators().get(0).getMaxValue() != null) {
                    textInputField.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                            formData.getValidators().get(0).getMaxValue())});
                }
            }

            if (!TextUtils.isEmpty(formData.getAnswer())) {
                textInputField.setText(formData.getAnswer());
            }

            if (!TextUtils.isEmpty(formData.getTitle())) {
                textInputField.setTag(formData.getTitle());
            }

            if (!TextUtils.isEmpty(formData.getInputType())) {
                //set input type
                setInputType(formData.getInputType(), textInputField);
            }

            textInputField.setMaxLines(1);

            textInputField.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (!TextUtils.isEmpty(formData.getName()) && !TextUtils.isEmpty(charSequence.toString())) {
                        requestObjectMap.put(formData.getName(), charSequence.toString());
                    } else {
                        requestObjectMap.remove(formData.getName());
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            editTexts.add(textInputField);
            editTextElementsHashMap.put(textInputField, formData);

            TextInputLayout textInputLayout = textTemplateView.findViewById(R.id.text_input_form_text_template);
            textInputLayout.setHint(formData.getTitle());
        }
        return textTemplateView;
    }

    private void setInputType(String type, EditText textInputField) {
        if (!TextUtils.isEmpty(type)) {
            switch (type) {
                case Constants.FormInputType.INPUT_TYPE_DATE:
                    textInputField.setFocusable(false);
                    textInputField.setClickable(false);
                    textInputField.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
                    textInputField.setOnClickListener(view -> showDateDialog(fragment.get().getContext(), textInputField));
                    break;

                case Constants.FormInputType.INPUT_TYPE_NUMBER:

                case Constants.FormInputType.INPUT_TYPE_NUMERIC:
                    textInputField.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;
            }
        }
    }

    public View fileTemplate(final Elements formData) {

        if (fragment == null || fragment.get() == null) {
            Log.e(TAG, "View returned null" + formData);
            return null;
        }

        LinearLayout fileTemplateView = (LinearLayout) View.inflate(
                fragment.get().getContext(), R.layout.row_file_type, null);

        TextView txtFileName = fileTemplateView.findViewById(R.id.txt_file_name);
        if (!TextUtils.isEmpty(formData.getTitle())) {
            txtFileName.setText(formData.getTitle());
        }

        return fileTemplateView;
    }

    private String setFieldAsMandatory(boolean isRequired) {
        return (isRequired ? " *" : "");
    }

    public boolean isValid() {
        fragment.get().setErrorMsg("");
        String errorMsg = "";

        //For all edit texts
        for (EditText editText : editTexts) {
            Elements formData = editTextElementsHashMap.get(editText);
            if (formData.isRequired() != null) {

                errorMsg = Validation.editTextRequiredValidation(editText.getTag().toString(),
                        editText.getText().toString(), formData.isRequired());

                if (!TextUtils.isEmpty(errorMsg)) {
                    fragment.get().setErrorMsg(errorMsg);
                    break;
                }
            } else if (formData.getValidators() != null && !formData.getValidators().isEmpty()) {
                if (!TextUtils.isEmpty(editText.getText().toString())) {

                    errorMsg = Validation.editTextMinMaxValidation(editText.getTag().toString(),
                            editText.getText().toString(), formData.getValidators().get(0));

                    if (!TextUtils.isEmpty(errorMsg)) {
                        fragment.get().setErrorMsg(errorMsg);
                        break;
                    }
                }
            }
        }

        return TextUtils.isEmpty(errorMsg);
    }

    public HashMap<String, String> getRequestObject() {
        if (requestObjectMap != null) {
            return requestObjectMap;
        }
        return null;
    }

    @Override
    public void onDropdownValueSelected(Elements parentElement/*Structure code*/, String value) {
        //It means dependency is there
        String key = "{" + parentElement.getName() + "} notempty";
        if (dependencyMap.get(key) != null) {
            DropDownTemplate dropDownTemplate = dependencyMap.get(key);
            Elements dependentElement = dropDownTemplate.getFormData();
            List<Choice> choiceValues = new ArrayList<>();

            String parentResponse = parentElement.getChoicesByUrlResponse();
            String dependentResponse = dependentElement.getChoicesByUrlResponse();
            try {
                JSONObject dependentOuterObj = new JSONObject(dependentResponse);
                JSONObject parentOuterObj = new JSONObject(parentResponse);
                JSONArray dependentDataArray = dependentOuterObj.getJSONArray(Constants.RESPONSE_DATA);
                JSONArray parentDataArray = parentOuterObj.getJSONArray(Constants.RESPONSE_DATA);

                for (int parentArrayIndex = 0; parentArrayIndex < parentDataArray.length(); parentArrayIndex++) {
                    JSONObject parentInnerObj = parentDataArray.getJSONObject(parentArrayIndex);
                    for (int dependentArrayIndex = 0; dependentArrayIndex < dependentDataArray.length(); dependentArrayIndex++) {
                        JSONObject dependentInnerObj = dependentDataArray.getJSONObject(dependentArrayIndex);

                        if (!TextUtils.isEmpty(dependentInnerObj.getString(parentElement.getName())) &&
                                !TextUtils.isEmpty(parentInnerObj.getString(parentElement.getName())) &&
                                dependentInnerObj.getString(parentElement.getName()).equals(parentInnerObj.getString(parentElement.getName()))) {

                            Log.i(TAG, "Test");
                            String choiceText = "";
                            String choiceValue = "";

                            if (dependentElement.getChoicesByUrl() != null &&
                                    !TextUtils.isEmpty(dependentElement.getChoicesByUrl().getTitleName())) {

                                if (dependentElement.getChoicesByUrl().getTitleName().contains(Constants.KEY_SEPARATOR)) {
                                    StringTokenizer titleTokenizer
                                            = new StringTokenizer(dependentElement.getChoicesByUrl().getTitleName(), Constants.KEY_SEPARATOR);
                                    StringTokenizer valueTokenizer
                                            = new StringTokenizer(dependentElement.getChoicesByUrl().getValueName(), Constants.KEY_SEPARATOR);

                                    JSONObject obj = dependentInnerObj.getJSONObject(titleTokenizer.nextToken());
                                    choiceText = obj.getString(titleTokenizer.nextToken());

                                    //Ignore first value of valueToken
                                    valueTokenizer.nextToken();
                                    choiceValue = obj.getString(valueTokenizer.nextToken());
                                } else {
                                    choiceText = dependentInnerObj.getString(dependentElement.getChoicesByUrl().getTitleName());
                                    choiceValue = dependentInnerObj.getString(dependentElement.getChoicesByUrl().getValueName());
                                }
                            }

                            Choice choice = new Choice();
                            choice.setText(choiceText);
                            choice.setValue(choiceValue);
                            choiceValues.add(choice);
                        }
                    }
                }

                dropDownTemplate.setListData(choiceValues);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (parentElement != null && !TextUtils.isEmpty(parentElement.getName()) && !TextUtils.isEmpty(value)) {
            requestObjectMap.put(parentElement.getName(), value);
        }
    }

    private void showDateDialog(Context context, final EditText editText) {
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dateDialog = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
            String date = year + "-" + Util.getTwoDigit(monthOfYear + 1) + "-" + Util.getTwoDigit(dayOfMonth);
            editText.setText(date);
        }, mYear, mMonth, mDay);

        dateDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dateDialog.show();
    }

    public void clearOldComponents() {
        editTexts.clear();
        editTexts = new ArrayList<>();

        editTextElementsHashMap.clear();
        editTextElementsHashMap = new HashMap<>();
    }
}
