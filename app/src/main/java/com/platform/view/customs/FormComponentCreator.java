package com.platform.view.customs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.platform.R;
import com.platform.listeners.DropDownValueSelectListener;
import com.platform.listeners.MatrixDynamicValueChangeListener;
import com.platform.listeners.TextValueChangeListener;
import com.platform.models.LocaleData;
import com.platform.utility.MathEval;
import com.platform.models.forms.Choice;
import com.platform.models.forms.Column;
import com.platform.models.forms.Elements;
import com.platform.models.forms.FormData;
import com.platform.models.forms.Validator;
import com.platform.presenter.FormActivityPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Permissions;
import com.platform.utility.PlatformGson;
import com.platform.utility.Util;
import com.platform.utility.Validation;
import com.platform.view.fragments.FormFragment;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

@SuppressWarnings({"ConstantConditions", "CanBeFinal"})
public class FormComponentCreator implements DropDownValueSelectListener, MatrixDynamicValueChangeListener,
        TextValueChangeListener {

    private final WeakReference<FormFragment> fragment;
    private final String TAG = this.getClass().getSimpleName();

    private View mImageView;
    private String mImageName;
    private boolean mIsInEditMode;
    private boolean mIsPartiallySaved;

    private HashMap<String, String> requestObjectMap = new HashMap<>();
    private HashMap<String, List<HashMap<String, String>>> matrixDynamicValuesMap = new HashMap<>();
    private HashMap<EditText, Elements> editTextElementsHashMap = new HashMap<>();
    private HashMap<DropDownTemplate, Elements> dropDownElementsHashMap = new HashMap<>();
    private HashMap<ImageView, Elements> imageViewElementsHashMap = new HashMap<>();
    private HashMap<String, List<DropDownTemplate>> dependencyMap = new HashMap<>();
    private HashMap<List<String>, EditText> defaultValueMap = new HashMap<>();
    private HashMap<String, EditText> editTextWithNameMap = new HashMap<>();

    private ArrayList<EditText> editTexts = new ArrayList<>();
    private ArrayList<DropDownTemplate> dropDowns = new ArrayList<>();
    private ArrayList<MatrixDynamicTemplate> matrixDynamics = new ArrayList<>();
    private ArrayList<ImageView> photos = new ArrayList<>();

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
        if (formData.getTitle() != null) {
            if (!TextUtils.isEmpty(formData.getTitle().getLocaleValue())) {
                if (formData.isRequired() != null) {
                    txtRadioGroupName.setText(fragment.get().getResources().getString(R.string.form_field_mandatory,
                            formData.getTitle().getLocaleValue(), Util.setFieldAsMandatory(formData.isRequired())));
                } else {
                    txtRadioGroupName.setText(fragment.get().getResources().getString(R.string.form_field_mandatory,
                            formData.getTitle().getLocaleValue(), Util.setFieldAsMandatory(false)));
                }
            }
        }

        if (formData.getChoices() != null && !formData.getChoices().isEmpty()) {
            for (int index = 0; index < formData.getChoices().size(); index++) {
                RadioButton radioButtonForm = new RadioButton(fragment.get().getContext());
                radioButtonForm.setText(formData.getChoices().get(index).getText().getLocaleValue());
                radioButtonForm.setTag(formData.getChoices().get(index).getValue());
                radioButtonForm.setId(index);
                radioGroupForm.addView(radioButtonForm);

                radioGroupForm.setOnCheckedChangeListener((radioGroup1, checkedId) -> {
                    if (!TextUtils.isEmpty(formData.getName()) &&
                            radioGroupForm.findViewById(radioGroup1.getCheckedRadioButtonId()).getTag() != null) {

                        requestObjectMap.put(formData.getName(),
                                radioGroupForm.findViewById(radioGroup1.getCheckedRadioButtonId()).getTag().toString());
                    } else {
                        requestObjectMap.remove(formData.getName());
                    }
                });

                if (!TextUtils.isEmpty(formData.getAnswer()) && radioButtonForm.getTag() != null) {
                    if (radioButtonForm.getTag().toString().equals(formData.getAnswer())) {
                        radioButtonForm.setChecked(true);
                    }
                } else if (index == 0) {
                    radioButtonForm.setChecked(true);
                }
            }
        }

        return radioTemplateView;
    }

    public synchronized View dropDownTemplate(Elements formData, String formId, boolean isInEditMode,
                                              boolean isPartiallySaved) {

        if (fragment == null || fragment.get() == null) {
            Log.e(TAG, "dropDownTemplate returned null");
            return null;
        }

        this.mIsInEditMode = isInEditMode;
        this.mIsPartiallySaved = isPartiallySaved;

        DropDownTemplate template = new DropDownTemplate(formData, fragment.get(), this, formId);

        View view;
        if (formData.isRequired() != null) {
            view = template.init(Util.setFieldAsMandatory(formData.isRequired()));
        } else {
            view = template.init(Util.setFieldAsMandatory(false));
        }

        view.setTag(formData.getName());
        template.setTag(formData.getName());

        dropDowns.add(template);
        dropDownElementsHashMap.put(template, formData);

        if (!TextUtils.isEmpty(formData.getEnableIf())) {
            List<DropDownTemplate> dependentDropDowns = dependencyMap.get(formData.getEnableIf());
            if (dependentDropDowns != null && !dependentDropDowns.isEmpty()) {
                dependentDropDowns.add(template);
            } else {
                dependentDropDowns = new ArrayList<>();
                dependentDropDowns.add(template);
            }
            dependencyMap.put(formData.getEnableIf(), dependentDropDowns);
        }

        return view;
    }

    public void updateDropDownValues(Elements elements, List<Choice> choiceValues) {
        Predicate<DropDownTemplate> byTag = dropDownTemplate -> dropDownTemplate.getTag().equals(elements.getName());
        List<DropDownTemplate> matchedTemplates = Stream.of(dropDowns).filter(byTag).collect(Collectors.toList());
        if (matchedTemplates != null && !matchedTemplates.isEmpty()) {
            Choice selectChoice = new Choice();
            selectChoice.setValue(fragment.get().getString(R.string.default_select));
            LocaleData localeData = new LocaleData(fragment.get().getString(R.string.default_select));
            selectChoice.setText(localeData);
            if (!choiceValues.contains(selectChoice)) {
                choiceValues.add(0, selectChoice);
            }

            elements.setChoices(choiceValues);
            matchedTemplates.get(0).setFormData(elements);
            matchedTemplates.get(0).setListData(choiceValues, mIsInEditMode, mIsPartiallySaved);
        }
    }

    public void updateMatrixDynamicDropDownValues(Column column, List<Choice> choiceValues,
                                                  HashMap<String, String> matrixDynamicInnerMap, long rowIndex) {

        if (matrixDynamics != null && !matrixDynamics.isEmpty()) {
            matrixDynamics.get(0).updateDropDownValues(column, choiceValues,
                    matrixDynamicInnerMap, rowIndex);
        }
    }

    public View panelTemplate(final Elements formData) {
        if (fragment == null || fragment.get() == null) {
            Log.e(TAG, "View returned null");
            return null;
        }

        TextView panelTemplate = (TextView) View.inflate(fragment.get().getContext(),
                R.layout.form_panel_template, null);
        panelTemplate.setText(formData.getTitle().getLocaleValue().trim());

        return panelTemplate;
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
                for (Validator validator :
                        formData.getValidators()) {
                    if (!TextUtils.isEmpty(validator.getType())) {
                        Util.setInputType(fragment.get().getContext(), validator.getType(), textInputField);
                        break;
                    }
                }
            }

            //set max length allowed
            if (formData.getMaxLength() != null) {
                textInputField.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                        formData.getMaxLength())});
            } else if (formData.getValidators() != null && !formData.getValidators().isEmpty()) {
                for (Validator validator :
                        formData.getValidators()) {
                    if (validator.getMaxLength() != null) {
                        textInputField.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                                validator.getMaxLength())});
                        break;
                    }
                }

            }

            if (!TextUtils.isEmpty(formData.getAnswer())) {
                if (!TextUtils.isEmpty(formData.getInputType()) &&
                        formData.getInputType().equalsIgnoreCase(Constants.FormInputType.INPUT_TYPE_DATE)) {
                    try {
                        textInputField.setText(Util.getLongDateInString(
                                Long.valueOf(formData.getAnswer()), Constants.FORM_DATE));
                    } catch (Exception e) {
                        Log.e(TAG, "DATE ISSUE");
                        textInputField.setText(Util.getLongDateInString(
                                Util.getCurrentTimeStamp(), Constants.FORM_DATE));
                    }
                } else {
                    textInputField.setText(String.format(Locale.getDefault(), "%s",
                            formData.getAnswer()));
                }
            }

            if (formData.getTitle() != null && !TextUtils.isEmpty(formData.getTitle().getLocaleValue())) {
                textInputField.setTag(formData.getTitle().getLocaleValue());
            }

            if (!TextUtils.isEmpty(formData.getInputType())) {
                //set input type
                Util.setInputType(fragment.get().getContext(), formData.getInputType(), textInputField);
            }

            if (formData.getRows() != null && formData.getRows() > 0) {
                textInputField.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                textInputField.setMaxLines(formData.getRows());
                textInputField.setHorizontallyScrolling(false);
                textInputField.setVerticalScrollBarEnabled(true);
            } else {
                textInputField.setMaxLines(1);
            }

            textInputField.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    for (Map.Entry<List<String>, EditText> entry : defaultValueMap.entrySet()) {
                        List<String> dependentElements = entry.getKey();
                        StringBuilder expression = new StringBuilder();
                        MathEval math = new MathEval();
                        if (dependentElements.contains(formData.getName())) {
                            for (int index = 0; index < dependentElements.size(); index++) {
                                EditText dependentComponent = editTextWithNameMap.get("{" + dependentElements.get(index) + "}");
                                expression.append(dependentElements.get(index));
                                if (dependentComponent != null) {
                                    if (!TextUtils.isEmpty(dependentComponent.getText().toString())) {
                                        math.setVariable(dependentElements.get(index),
                                                Double.parseDouble(dependentComponent.getText().toString()));
                                    } else {
                                        math.setVariable(dependentElements.get(index),
                                                0);
                                    }
                                }
                            }
                            entry.getValue().setText(String.valueOf(math.evaluate(expression.toString())));
                        }
                    }

                    if (!TextUtils.isEmpty(formData.getName()) && !TextUtils.isEmpty(charSequence.toString())) {
                        if (!TextUtils.isEmpty(formData.getInputType()) &&
                                formData.getInputType().equalsIgnoreCase(Constants.FormInputType.INPUT_TYPE_DATE)) {
                            requestObjectMap.put(formData.getName(), ("" + Util.getDateInLong(charSequence.toString())).trim());
                        } else {
                            requestObjectMap.put(formData.getName(), charSequence.toString().trim());
                        }
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
            editTextWithNameMap.put("{" + formData.getName() + "}", textInputField);

            if (!TextUtils.isEmpty(formData.getDefaultValue())) {
                StringTokenizer defaultValueTokenizer = new StringTokenizer(formData.getDefaultValue(), "()");
                List<String> dependentElements = new ArrayList<>();
                while (defaultValueTokenizer.hasMoreTokens()) {
                    dependentElements.add(defaultValueTokenizer.nextToken());
                }
                defaultValueMap.put(dependentElements, textInputField);
            }

            if (formData.getReadOnly() != null && formData.getReadOnly()) {
                textInputField.setEnabled(!formData.getReadOnly());
            } else {
                textInputField.setEnabled(true);
            }

            TextInputLayout textInputLayout = textTemplateView.findViewById(R.id.text_input_form_text_template);
            if (formData.isRequired() != null) {
                textInputLayout.setHint(formData.getTitle().getLocaleValue()
                        + Util.setFieldAsMandatory(formData.isRequired()));
            } else {
                textInputLayout.setHint(formData.getTitle() == null ?
                        "" : (formData.getTitle().getLocaleValue() + Util.setFieldAsMandatory(false)));
            }
        }
        return textTemplateView;
    }

    public View fileTemplate(final Elements formData) {

        if (fragment == null || fragment.get() == null) {
            Log.e(TAG, "View returned null" + formData);
            return null;
        }

        final LinearLayout fileTemplateView = (LinearLayout) View.inflate(
                fragment.get().getContext(), R.layout.row_file_type, null);

        ImageView imageView = fileTemplateView.findViewById(R.id.iv_file);
        imageView.setOnClickListener(v -> onAddImageClick(v, formData.getName()));

        TextView txtFileName = fileTemplateView.findViewById(R.id.txt_file_name);
        if (formData.getTitle() != null) {
            if (!TextUtils.isEmpty(formData.getTitle().getLocaleValue())) {
                if (formData.isRequired() != null) {
                    txtFileName.setText(fragment.get().getResources().getString(R.string.form_field_mandatory,
                            formData.getTitle().getLocaleValue(), Util.setFieldAsMandatory(formData.isRequired())));
                } else {
                    txtFileName.setText(fragment.get().getResources().getString(R.string.form_field_mandatory,
                            formData.getTitle().getLocaleValue(), Util.setFieldAsMandatory(false)));
                }
            }
        }

        photos.add(imageView);
        imageViewElementsHashMap.put(imageView, formData);

        if (!TextUtils.isEmpty(formData.getAnswer())) {
            Glide.with(fragment.get().getContext())
                    .load(formData.getAnswer()) // Remote URL of image.
                    .into(imageView);
        }

        return fileTemplateView;
    }

    public View matrixDynamicTemplate(FormData formData, final Elements elements, boolean isInEditMode,
                                      boolean isPartiallySaved, FormActivityPresenter formActivityPresenter) {

        if (fragment == null || fragment.get() == null) {
            Log.e(TAG, "View returned null");
            return null;
        }

        this.mIsInEditMode = isInEditMode;
        this.mIsPartiallySaved = isPartiallySaved;

        MatrixDynamicTemplate template = new MatrixDynamicTemplate(fragment.get(), formData, elements,
                formActivityPresenter, mIsInEditMode, mIsPartiallySaved, this);

        matrixDynamics.add(template);

        return template.matrixDynamicView();
    }

    public boolean isValid() {
        fragment.get().setErrorMsg("");
        String errorMsg = "";

        //For all edit texts
        for (EditText editText : editTexts) {
            Elements formData = editTextElementsHashMap.get(editText);
            if (formData.isRequired() != null) {

                errorMsg = Validation.requiredValidation(editText.getTag().toString(),
                        editText.getText().toString(), formData.isRequired(), fragment.get().getContext());

                if (!TextUtils.isEmpty(errorMsg)) {
                    fragment.get().setErrorMsg(errorMsg);
                    return false;
                }
            }

            if (formData.getMaxLength() != null) {
                if (!TextUtils.isEmpty(editText.getText().toString())) {
                    errorMsg = Validation.editTextMaxLengthValidation(editText.getTag().toString(),
                            editText.getText().toString(), formData.getMaxLength(), fragment.get().getContext());

                    if (!TextUtils.isEmpty(errorMsg)) {
                        fragment.get().setErrorMsg(errorMsg);
                        return false;
                    }
                }
            }

            if (formData.getValidators() != null && !formData.getValidators().isEmpty()) {
                for (Validator validator :
                        formData.getValidators()) {
                    if (!TextUtils.isEmpty(validator.getType())) {
                        switch (validator.getType()) {
                            case Constants.ValidationType.REGEX_TYPE:
                                if (!TextUtils.isEmpty(editText.getText().toString())) {

                                    errorMsg = Validation.regexValidation(editText.getTag().toString(),
                                            editText.getText().toString(), validator, fragment.get().getContext());

                                    if (!TextUtils.isEmpty(errorMsg)) {
                                        fragment.get().setErrorMsg(errorMsg);
                                        return false;
                                    }
                                }
                                break;

                            case Constants.ValidationType.EXPRESSION_TYPE:
                                if (!TextUtils.isEmpty(editText.getText().toString())) {

                                    if (!TextUtils.isEmpty(validator.getExpression())) {
                                        String expression = validator.getExpression();
                                        String field1Name, field2Name, field3Name;
                                        String field1Value = "", field2Value = "", field3Value = "";
                                        StringTokenizer expressionTokenizer = new StringTokenizer(expression, "><=-");

                                        if (expressionTokenizer.hasMoreTokens()) {
                                            field1Name = expressionTokenizer.nextToken();
                                            field1Value = editTextWithNameMap.get(field1Name).getText().toString();
                                        }

                                        if (expressionTokenizer.hasMoreTokens()) {
                                            field2Name = expressionTokenizer.nextToken();
                                            field2Value = editTextWithNameMap.get(field2Name).getText().toString();
                                        }

                                        if (expressionTokenizer.hasMoreTokens()) {
                                            field3Name = expressionTokenizer.nextToken();
                                            field3Value = editTextWithNameMap.get(field3Name).getText().toString();
                                        }

                                        if (!TextUtils.isEmpty(field1Value) && !TextUtils.isEmpty(field2Value)) {
                                            errorMsg = Validation.expressionValidation(editText.getTag().toString(),
                                                    field1Value, field2Value, field3Value,
                                                    formData.getInputType(), validator, fragment.get().getContext());

                                            if (!TextUtils.isEmpty(errorMsg)) {
                                                fragment.get().setErrorMsg(errorMsg);
                                                return false;
                                            }
                                        }
                                    }
                                }
                                break;

                            default:
                                if (!TextUtils.isEmpty(editText.getText().toString())) {

                                    errorMsg = Validation.editTextMinMaxValueValidation(editText.getTag().toString(),
                                            editText.getText().toString(), validator, fragment.get().getContext());

                                    if (!TextUtils.isEmpty(errorMsg)) {
                                        fragment.get().setErrorMsg(errorMsg);
                                        return false;
                                    }

                                    errorMsg = Validation.editTextMinMaxLengthValidation(editText.getTag().toString(),
                                            editText.getText().toString(), validator, fragment.get().getContext());

                                    if (!TextUtils.isEmpty(errorMsg)) {
                                        fragment.get().setErrorMsg(errorMsg);
                                        return false;
                                    }
                                }
                                break;
                        }
                    }
                }
            }
        }

        //For all drop downs
        for (DropDownTemplate dropDownTemplate : dropDowns) {
            Elements formData = dropDownElementsHashMap.get(dropDownTemplate);
            if (formData.isRequired() != null) {

                if ((dropDownTemplate.getValueList() != null && dropDownTemplate.getValueList().size() == 0) ||
                        (dropDownTemplate.getSelectedItem() == null) ||
                        (dropDownTemplate.getSelectedItem() != null &&
                                dropDownTemplate.getSelectedItem().getValue().equals(fragment.get().getResources().getString(R.string.default_select)))) {
                    errorMsg = Validation.requiredValidation(formData.getTitle().getLocaleValue(),
                            "", formData.isRequired(), fragment.get().getContext());

                    if (!TextUtils.isEmpty(errorMsg)) {
                        fragment.get().setErrorMsg(errorMsg);
                        return false;
                    }
                }
            }
        }

        //For all matrix dynamics
        for (MatrixDynamicTemplate template : matrixDynamics) {
            Elements element = template.getElements();
            List<HashMap<String, String>> valuesList = getMatrixDynamicValuesMap().get(element.getName());

            if (element.isRequired() != null) {
                errorMsg = Validation.matrixDynamicRequiredValidation(element.getTitle().getLocaleValue(),
                        element.getColumns().size(), valuesList, fragment.get().getContext());

                if (!TextUtils.isEmpty(errorMsg)) {
                    fragment.get().setErrorMsg(errorMsg);
                    return false;
                }
            }

            // Check duplicate entry
            if (!TextUtils.isEmpty(element.getKeyName())) {
                if (isDuplicateEntryFound(valuesList, element.getKeyName())) {
                    errorMsg = element.getKeyDuplicationError().getLocaleValue();
                    fragment.get().setErrorMsg(errorMsg);
                    return false;
                }
            }

            if (valuesList != null && !valuesList.isEmpty()) {
                for (HashMap<String, String> valuesMap : valuesList) {
                    for (int columnIndex = 0; columnIndex < element.getColumns().size(); columnIndex++) {
                        if (element.getColumns().get(columnIndex).getValidators() != null &&
                                !element.getColumns().get(columnIndex).getValidators().isEmpty()) {
                            String value = valuesMap.get(element.getColumns().get(columnIndex).getName());

                            for (Validator validator :
                                    element.getColumns().get(columnIndex).getValidators()) {
                                if (!TextUtils.isEmpty(validator.getType())) {
                                    if (Constants.ValidationType.REGEX_TYPE.equals(validator.getType())) {
                                        if (!TextUtils.isEmpty(value)) {

                                            errorMsg = Validation.regexValidation(
                                                    element.getColumns().get(columnIndex)
                                                            .getTitle().getLocaleValue(),
                                                    value, validator, fragment.get().getContext());

                                            if (!TextUtils.isEmpty(errorMsg)) {
                                                fragment.get().setErrorMsg(errorMsg);
                                                return false;
                                            }
                                        }
                                    } else {
                                        if (!TextUtils.isEmpty(value)) {

                                            errorMsg = Validation.editTextMinMaxValueValidation(
                                                    element.getColumns().get(columnIndex)
                                                            .getTitle().getLocaleValue(),
                                                    value, validator, fragment.get().getContext());

                                            if (!TextUtils.isEmpty(errorMsg)) {
                                                fragment.get().setErrorMsg(errorMsg);
                                                return false;
                                            }

                                            errorMsg = Validation.editTextMinMaxLengthValidation(
                                                    element.getColumns().get(columnIndex)
                                                            .getTitle().getLocaleValue(),
                                                    value, validator, fragment.get().getContext());

                                            if (!TextUtils.isEmpty(errorMsg)) {
                                                fragment.get().setErrorMsg(errorMsg);
                                                return false;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //For all photos
        for (ImageView photo : photos) {
            Elements formData = imageViewElementsHashMap.get(photo);

            String imgUrl = "";
            if (TextUtils.isEmpty(formData.getAnswer())) {
                List<Map<String, String>> uploadedImages = fragment.get().getUploadedImages();
                for (Map<String, String> imageObj : uploadedImages) {
                    if (imageObj.containsKey(formData.getName())) {
                        imgUrl = imageObj.get(formData.getName());
                        break;
                    }
                }
            } else {
                imgUrl = formData.getAnswer();
            }

            if (formData.isRequired() != null && TextUtils.isEmpty(imgUrl)) {
                errorMsg = Validation.requiredValidation(formData.getTitle().getLocaleValue(),
                        "", formData.isRequired(), fragment.get().getContext());

                if (!TextUtils.isEmpty(errorMsg)) {
                    fragment.get().setErrorMsg(errorMsg);
                    return false;
                }
            }
        }

        return TextUtils.isEmpty(errorMsg);
    }

    private boolean isDuplicateEntryFound(List<HashMap<String, String>> valuesList, String key) {
        if (valuesList != null && valuesList.size() > 0) {
            for (int i = 0; i < valuesList.size(); i++) {
                HashMap<String, String> valueItem = valuesList.get(i);
                String keyValue = valueItem.get(key);

                for (int j = i + 1; j < valuesList.size(); j++) {
                    HashMap<String, String> nextItem = valuesList.get(j);
                    if (keyValue.equalsIgnoreCase(nextItem.get(key))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public HashMap<String, String> getRequestObject() {
        return requestObjectMap;
    }

    public void setRequestObject(HashMap<String, String> requestObjectMap) {
        if (requestObjectMap != null) {
            this.requestObjectMap = requestObjectMap;
        }
    }

    public void setMatrixDynamicValuesMap(HashMap<String, List<HashMap<String,
            String>>> matrixDynamicValuesMap) {

        if (matrixDynamicValuesMap != null) {
            this.matrixDynamicValuesMap = matrixDynamicValuesMap;
        }
    }

    public HashMap<String, List<HashMap<String, String>>> getMatrixDynamicValuesMap() {
        return matrixDynamicValuesMap;
    }

    @Override
    public void onDropdownValueSelected(Elements parentElement, String value, String formId) {
        new UpdateDropDownValuesTask().execute(PlatformGson.getPlatformGsonInstance().toJson(parentElement), value, formId);
        if (parentElement != null && !TextUtils.isEmpty(parentElement.getName()) && !TextUtils.isEmpty(value)) {
            requestObjectMap.put(parentElement.getName(), value.trim());
        }
    }

    @Override
    public void onEmptyDropdownSelected(Elements parentElement) {
        //It means dependency is there
        String key = "{" + parentElement.getName() + "} notempty";
        if (dependencyMap.get(key) != null) {
            List<DropDownTemplate> dropDownTemplates = dependencyMap.get(key);
            if (dropDownTemplates != null && !dropDownTemplates.isEmpty()) {
                for (DropDownTemplate dropDownTemplate :
                        dropDownTemplates) {
                    Elements dependentElement = dropDownTemplate.getFormData();
                    List<Choice> choiceValues = new ArrayList<>();
                    dependentElement.setChoices(choiceValues);
                    dropDownTemplate.setFormData(dependentElement);
                    dropDownTemplate.setListData(choiceValues, mIsInEditMode, mIsPartiallySaved);
                }
            }
        }

        requestObjectMap.remove(parentElement.getName());
    }

    public void clearOldComponents() {
        editTexts.clear();
        editTexts = new ArrayList<>();

        editTextElementsHashMap.clear();
        editTextElementsHashMap = new HashMap<>();

        dropDowns.clear();
        dropDowns = new ArrayList<>();

        dropDownElementsHashMap.clear();
        dropDownElementsHashMap = new HashMap<>();

        photos.clear();
        photos = new ArrayList<>();

        imageViewElementsHashMap.clear();
        imageViewElementsHashMap = new HashMap<>();

        editTextWithNameMap.clear();
        editTextWithNameMap = new HashMap<>();

        dependencyMap.clear();
        dependencyMap = new HashMap<>();

        matrixDynamics.clear();
        matrixDynamics = new ArrayList<>();
    }

    private void onAddImageClick(final View view, final String name) {
        this.mImageView = view;
        this.mImageName = name;

        if (Permissions.isCameraPermissionGranted(fragment.get().getActivity(), fragment.get())) {
            showPictureDialog();
        }
    }

    public void showPictureDialog() {
        Context context = fragment.get().getContext();
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getString(R.string.title_choose_picture));
        String[] items = {context.getString(R.string.label_gallery), context.getString(R.string.label_camera)};

        dialog.setItems(items, (dialog1, which) -> {
            switch (which) {
                case 0:
                    fragment.get().choosePhotoFromGallery(mImageView, mImageName);
                    break;

                case 1:
                    fragment.get().takePhotoFromCamera(mImageView, mImageName);
                    break;
            }
        });

        dialog.show();
    }

    @Override
    public void onMatrixDynamicValueChanged(String elementName, List<HashMap<String,
            String>> matrixDynamicValuesList) {
        matrixDynamicValuesMap.put(elementName, matrixDynamicValuesList);
    }

    @Override
    public void showChoicesByUrlOffline(String response, Column column, long rowIndex, HashMap<String,
            String> matrixDynamicInnerMap) {
        fragment.get().showChoicesByUrlAsyncMD(response, column, matrixDynamicInnerMap, rowIndex);
    }

    @Override
    public void onTextValueChanged(String elementName, String value) {
        if (!TextUtils.isEmpty(value)) {
            requestObjectMap.put(elementName, value);
        } else {
            requestObjectMap.remove(elementName);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class UpdateDropDownValuesTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                updateValues(PlatformGson.getPlatformGsonInstance().fromJson(params[0],
                        Elements.class), params[1], params[2]);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            fragment.get().hideProgressBar();
        }

        @Override
        protected void onPreExecute() {
            fragment.get().showProgressBar();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private void updateValues(Elements parentElement, String value, String formId) {
        String key = "{" + parentElement.getName() + "} notempty";

        //It means dependency is there
        if (dependencyMap.get(key) != null && !dependencyMap.get(key).isEmpty()) {
            for (DropDownTemplate dropDownTemplate :
                    dependencyMap.get(key)) {
                Elements dependentElement = dropDownTemplate.getFormData();
                List<Choice> choiceValues = new ArrayList<>();
                List<JsonObject> dependentObjectsList = new ArrayList<>();

                String dependentResponse = dependentElement.getChoicesByUrlResponsePath();
                String response = Util.readFromInternalStorage(fragment.get().getContext(),
                        formId + "_" + dependentElement.getName());

                if (!TextUtils.isEmpty(dependentResponse) && !TextUtils.isEmpty(response)) {
                    JsonObject dependentOuterObj
                            = PlatformGson.getPlatformGsonInstance().fromJson(response, JsonObject.class);
                    JsonArray dependentDataArray = dependentOuterObj.getAsJsonArray(Constants.RESPONSE_DATA);

                    //Convert dependentDataArray to List of JsonObject
                    if (dependentDataArray != null) {
                        Type listType = new TypeToken<ArrayList<JsonObject>>() {
                        }.getType();
                        dependentObjectsList = PlatformGson.getPlatformGsonInstance().fromJson(dependentDataArray, listType);
                    }

                    String pValue;
                    Predicate<JsonObject> byParentSelection;
                    //Apply condition to match selected value in dependentObjects
                    //If parent has object in choicesByUrl
                    if (parentElement.getChoicesByUrl().getValueName().contains(Constants.KEY_SEPARATOR)) {
                        StringTokenizer parentValueTokenizer = new StringTokenizer(
                                parentElement.getChoicesByUrl().getValueName(), Constants.KEY_SEPARATOR);

                        //Ignore first value of valueToken
                        String outerObjName = parentValueTokenizer.nextToken();
                        pValue = parentValueTokenizer.nextToken();
                        byParentSelection = innerDependentObject -> innerDependentObject
                                .get(outerObjName).getAsJsonObject().get(pValue).getAsString().equals(value);
                    }
                    //If parent has string in choicesByUrl
                    else {
                        pValue = parentElement.getChoicesByUrl().getValueName();
                        byParentSelection = innerDependentObject -> innerDependentObject.get(pValue).getAsString().equals(value);
                    }

                    //Filter dependentObjects
                    List<JsonObject> filteredDependentObjects
                            = Stream.of(dependentObjectsList).filter(byParentSelection).collect(Collectors.toList());

                    String dTitle, dValue;
                    LocaleData choiceText;
                    String choiceValue;

                    //Fill choiceValues list with filtered object's title and value
                    for (int filteredDependentIndex = 0; filteredDependentIndex < filteredDependentObjects.size(); filteredDependentIndex++) {

                        JsonObject dependentInnerObject = filteredDependentObjects.get(filteredDependentIndex);
                        //If dependent has object in choicesByUrl
                        if (dependentElement.getChoicesByUrl().getValueName().contains(Constants.KEY_SEPARATOR)) {

                            StringTokenizer dependentTitleTokenizer
                                    = new StringTokenizer(dependentElement.getChoicesByUrl().getTitleName(), Constants.KEY_SEPARATOR);
                            StringTokenizer dependentValueTokenizer
                                    = new StringTokenizer(dependentElement.getChoicesByUrl().getValueName(), Constants.KEY_SEPARATOR);

                            //Ignore first value of titleToken
                            dependentTitleTokenizer.nextToken();
                            String outerObjectName = dependentValueTokenizer.nextToken();
                            JsonObject dObj = dependentInnerObject.get(outerObjectName).getAsJsonObject();

                            dTitle = dependentTitleTokenizer.nextToken();
                            dValue = dependentValueTokenizer.nextToken();

                            try {
                                choiceText = PlatformGson.getPlatformGsonInstance()
                                        .fromJson(dObj.get(dTitle).toString(), LocaleData.class);
                            } catch (Exception e) {
                                choiceText = new LocaleData(dObj.get(dTitle).getAsString());
                            }
                            choiceValue = dObj.get(dValue).getAsString();
                        }
                        //If dependent has string in choicesByUrl
                        else {
                            dTitle = dependentElement.getChoicesByUrl().getTitleName();
                            dValue = dependentElement.getChoicesByUrl().getValueName();

                            try {
                                choiceText = PlatformGson.getPlatformGsonInstance()
                                        .fromJson(dependentInnerObject.get(dTitle).toString(), LocaleData.class);
                            } catch (Exception e) {
                                choiceText = new LocaleData(dependentInnerObject.get(dTitle).getAsString());
                            }
                            choiceValue = dependentInnerObject.get(dValue).getAsString();
                        }

                        Choice choice = new Choice();
                        choice.setText(choiceText);
                        choice.setValue(choiceValue);

                        if (!choiceValues.contains(choice)) {
                            choiceValues.add(choice);
                        }
                    }

                    //Sort choices in ascending order
                    Collections.sort(choiceValues, (o1, o2) -> o1.getText().getLocaleValue()
                            .compareTo(o2.getText().getLocaleValue()));

                    //Update UI on UI thread
                    if (fragment.get().getActivity() != null) {
                        fragment.get().getActivity().runOnUiThread(() -> {
                            Choice selectChoice = new Choice();
                            selectChoice.setValue(fragment.get().getString(R.string.default_select));
                            LocaleData localeData = new LocaleData(fragment.get().getString(R.string.default_select));
                            selectChoice.setText(localeData);
                            choiceValues.add(0, selectChoice);

                            dependentElement.setChoices(choiceValues);
                            dropDownTemplate.setFormData(dependentElement);
                            dropDownTemplate.setListData(choiceValues, mIsInEditMode, mIsPartiallySaved);
                        });
                    }
                }
            }
        }
    }
}
