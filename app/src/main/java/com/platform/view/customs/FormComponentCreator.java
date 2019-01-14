package com.platform.view.customs;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.platform.R;
import com.platform.models.forms.Elements;
import com.platform.utility.Constants;
import com.platform.view.fragments.FormFragment;

import java.lang.ref.WeakReference;

@SuppressWarnings("ConstantConditions")
public class FormComponentCreator {

    private final WeakReference<FormFragment> fragment;
    private final String TAG = this.getClass().getSimpleName();

    public FormComponentCreator(FormFragment fragment) {
        this.fragment = new WeakReference<>(fragment);
    }

    public View textInputTemplate(final Elements formData) {

        if (fragment == null || fragment.get() == null) {
            Log.e(TAG, "View returned null");
            return null;
        }

        RelativeLayout textTemplateView = (RelativeLayout) View.inflate(
                fragment.get().getContext(), R.layout.form_text_template, null);

        EditText textInputField = textTemplateView.findViewById(R.id.edit_form_text_template);
        textInputField.setMaxLines(1);
        textInputField.setText("");
        textInputField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        TextInputLayout textInputLayout = textTemplateView.findViewById(R.id.text_input_form_text_template);
        textInputLayout.setHint(formData.getTitle());

        return textTemplateView;
    }

    public synchronized Object[] dropDownTemplate(Elements formData) {
        if (fragment == null || fragment.get() == null) {
            Log.e(TAG, "dropDownTemplate returned null");
            return null;
        }

        DropDownTemplate template = new DropDownTemplate(formData, fragment.get());
        View view = template.init(setFieldAsMandatory(formData.isRequired()));

        if (formData.getChoices() != null) {
            template.setListData(formData.getChoices());
        }
        return new Object[]{view, formData, Constants.FormsFactory.DROPDOWN_TEMPLATE, template};
    }

    private String setFieldAsMandatory(boolean isRequired) {
        return (isRequired ? " *" : "");
    }
}
