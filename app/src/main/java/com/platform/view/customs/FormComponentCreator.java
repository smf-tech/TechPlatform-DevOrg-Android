package com.platform.view.customs;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.platform.R;
import com.platform.listeners.DropDownValueSelectListener;
import com.platform.models.forms.Choice;
import com.platform.models.forms.Elements;
import com.platform.models.profile.JurisdictionLevel;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.fragments.FormFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings({"ConstantConditions", "CanBeFinal"})
public class FormComponentCreator implements DropDownValueSelectListener {

    private final WeakReference<FormFragment> fragment;
    private final String TAG = this.getClass().getSimpleName();
    private HashMap<String, String> requestObjectMap = new HashMap<>();

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
                if (index == 0) {
                    radioButtonForm.setChecked(true);
                }
                radioGroupForm.addView(radioButtonForm);
            }
        }

        return radioTemplateView;
    }

    public synchronized Object[] dropDownTemplate(Elements formData) {
        if (fragment == null || fragment.get() == null) {
            Log.e(TAG, "dropDownTemplate returned null");
            return null;
        }

        DropDownTemplate template = new DropDownTemplate(formData, fragment.get(), this);
        View view = template.init(setFieldAsMandatory(formData.isRequired()));

        if (!TextUtils.isEmpty(formData.getTitle()) &&
                formData.getTitle().equalsIgnoreCase(Constants.Login.USER_LOCATION)) {

            if (Util.getJurisdictionLevelDataFromPref() != null) {
                List<String> locationValues = new ArrayList<>();
                for (JurisdictionLevel jurisdictionLevel :
                        Util.getJurisdictionLevelDataFromPref().getJurisdictionLevelList()) {
                    locationValues.add(jurisdictionLevel.getJurisdictionLevelName());
                }
                template.setListData(locationValues);
            }
        } else if (formData.getChoices() != null) {
            List<String> choiceValues = new ArrayList<>();
            for (Choice choice :
                    formData.getChoices()) {
                choiceValues.add(choice.getText());
            }
            template.setListData(choiceValues);
        }

        return new Object[]{view, formData, Constants.FormsFactory.DROPDOWN_TEMPLATE, template};
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
        textInputField.setTag(formData.getTitle());
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

        TextInputLayout textInputLayout = textTemplateView.findViewById(R.id.text_input_form_text_template);
        textInputLayout.setHint(formData.getTitle());

        return textTemplateView;
    }

    private String setFieldAsMandatory(boolean isRequired) {
        return (isRequired ? " *" : "");
    }

    public boolean isValid() {
        //For all edit texts
        for (EditText inputText :
                editTexts) {
            if (inputText != null && TextUtils.isEmpty(inputText.getText().toString()) && !TextUtils.isEmpty(inputText.getTag().toString())) {
                fragment.get().setErrorMsg(inputText.getTag() + " blank");
                return false;
            }
        }
        //For all radio buttons
        //For all multi selects
        return true;
    }

    public HashMap<String, String> getRequestObject() {
        if (requestObjectMap != null) {
            return requestObjectMap;
        }
        return null;
    }

    @Override
    public void onDropdownValueSelected(String name, String value) {
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(value)) {
            requestObjectMap.put(name, value);
        }
    }
}
