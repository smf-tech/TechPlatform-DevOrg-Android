package com.octopus.view.customs;

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.material.textfield.TextInputLayout;
import com.octopus.R;
import com.octopus.listeners.TextValueChangeListener;
import com.octopus.models.forms.Elements;
import com.octopus.models.forms.Validator;
import com.octopus.utility.Constants;
import com.octopus.utility.Util;
import com.octopus.view.fragments.FormFragment;

import java.lang.ref.WeakReference;

@SuppressWarnings("unused")
class TextInputTemplate {

    private final String TAG = this.getClass().getSimpleName();
    private final Elements formData;
    private final WeakReference<FormFragment> context;
    private final TextValueChangeListener textValueChangeListener;

    TextInputTemplate(Elements formData, FormFragment context, TextValueChangeListener textValueChangeListener) {
        this.formData = formData;
        this.context = new WeakReference<>(context);
        this.textValueChangeListener = textValueChangeListener;
    }

    synchronized View textInputView() {
        RelativeLayout textTemplateView = (RelativeLayout) View.inflate(
                context.get().getContext(), R.layout.form_text_template, null);

        EditText textInputField = textTemplateView.findViewById(R.id.edit_form_text_template);
        if (formData != null) {
            if (formData.getValidators() != null && !formData.getValidators().isEmpty()) {
                //set input type
                for (Validator validator :
                        formData.getValidators()) {
                    if (!TextUtils.isEmpty(validator.getType())) {
                        Util.setInputType(context.get().getContext(), validator.getType(), textInputField);
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
                    textInputField.setText(formData.getAnswer());
                }
            }

            if (formData.getTitle() != null && !TextUtils.isEmpty(formData.getTitle().getLocaleValue())) {
                textInputField.setTag(formData.getTitle().getLocaleValue());
            }

            if (!TextUtils.isEmpty(formData.getInputType())) {
                //set input type
                Util.setInputType(context.get().getContext(), formData.getInputType(), textInputField);
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
                    if (!TextUtils.isEmpty(formData.getName()) && !TextUtils.isEmpty(charSequence.toString())) {
                        if (!TextUtils.isEmpty(formData.getInputType()) &&
                                formData.getInputType().equalsIgnoreCase(Constants.FormInputType.INPUT_TYPE_DATE)) {
                            textValueChangeListener.onTextValueChanged(formData.getName(), ("" + Util.getDateInLong(charSequence.toString())).trim());
                        } else {
                            textValueChangeListener.onTextValueChanged(formData.getName(), charSequence.toString().trim());
                        }
                    } else {
                        textValueChangeListener.onTextValueChanged(formData.getName(), "");
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

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
}
