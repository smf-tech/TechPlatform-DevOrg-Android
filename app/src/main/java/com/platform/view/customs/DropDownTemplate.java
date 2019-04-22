package com.platform.view.customs;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.platform.Platform;
import com.platform.R;
import com.platform.listeners.DropDownValueSelectListener;
import com.platform.models.LocaleData;
import com.platform.models.forms.Choice;
import com.platform.models.forms.Elements;
import com.platform.view.adapters.FormSpinnerAdapter;
import com.platform.view.fragments.FormFragment;
import com.platform.widgets.PlatformSpinner;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;

@SuppressWarnings({"CanBeFinal", "WeakerAccess"})
public class DropDownTemplate implements AdapterView.OnItemSelectedListener {

    private final String TAG = this.getClass().getSimpleName();
    private Elements formData;
    private PlatformSpinner spinner;
    private WeakReference<FormFragment> context;
    private List<Choice> valueList = new ArrayList<>();
    private DropDownValueSelectListener dropDownValueSelectListener;
    private String tag;
    private String formId;

    @SuppressWarnings("unused")
    public String getFormId() {
        return formId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    DropDownTemplate(Elements formData, FormFragment context, DropDownValueSelectListener listener, String formId) {
        this.formData = formData;
        this.context = new WeakReference<>(context);
        this.dropDownValueSelectListener = listener;
        this.formId = formId;
    }

    public List<Choice> getValueList() {
        return valueList;
    }

    synchronized View init(String mandatory) {
        return dropDownView(mandatory);
    }

    public Elements getFormData() {
        return formData;
    }

    public void setFormData(Elements formData) {
        this.formData = formData;
    }

    @SuppressWarnings("ConstantConditions")
    private synchronized View dropDownView(String mandatory) {
        if (context == null || context.get() == null) {
            Log.e(TAG, "WeakReference returned null");
            return null;
        }

        LinearLayout baseLayout = (LinearLayout) View.inflate(context.get().getContext(),
                R.layout.form_dropdown_template, null);

        spinner = baseLayout.findViewById(R.id.sp_single_select);

        String label = formData.getTitle().getLocaleValue() + mandatory;
        ((TextView) baseLayout.findViewById(R.id.dropdown_label)).setText(label);

        FormSpinnerAdapter adapter = new FormSpinnerAdapter(context.get().getContext(),
                R.layout.layout_spinner_item, valueList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        return baseLayout;
    }

    @SuppressWarnings("unchecked")
    void setListData(List<Choice> valueList, boolean isInEditMode, boolean isPartiallySaved) {
        if (valueList != null) {
            boolean isValueSet = false;
            this.valueList = valueList;

            FormSpinnerAdapter adapter = (FormSpinnerAdapter) spinner.getAdapter();
            adapter.clear();
            adapter.addAll(valueList);
            adapter.notifyDataSetChanged();

            if (valueList.size() > 0) {
                this.setSelectedItem(0);
            }

            if (formData.getChoices() != null && !formData.getChoices().isEmpty()) {
                for (int index = 0; index < formData.getChoices().size(); index++) {
                    if (!TextUtils.isEmpty(formData.getAnswer()) &&
                            formData.getChoices().get(index).getText() != null &&
                            !TextUtils.isEmpty(formData.getChoices().get(index).getText().getLocaleValue()) &&
                            formData.getAnswer().equals(formData.getChoices().get(index).getValue())) {

                        isValueSet = true;
                        this.setSelectedItem(index);
                    }
                }
            }

            if (isInEditMode && !isPartiallySaved) {
                if (!isValueSet && !TextUtils.isEmpty(formData.getAnswer())) {
                    Choice ch = new Choice();
                    LocaleData ld = new LocaleData(formData.getAnswer());
                    ch.setValue(formData.getAnswer());
                    ch.setText(ld);

                    this.valueList.add(ch);
                    valueList.add(ch);
                    adapter.clear();
                    adapter.addAll(valueList);
                    adapter.notifyDataSetChanged();
                    this.setSelectedItem(valueList.size() - 1);
                }
            }
        }
    }

    void setSelectedItem(int position) {
        if (spinner != null) {
            spinner.setSelection(position, true);
        }
    }

    int getSelectedItem() {
        if (spinner != null) {
            return spinner.getSelectedItemPosition();
        }
        return 0;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i != 0) {
            TextView tv = (TextView) adapterView.getSelectedView();
            if (tv != null) {
                tv.setTextColor(ContextCompat.getColor(Platform.getInstance(), R.color.colorPrimaryDark));
            }
            dropDownValueSelectListener.onDropdownValueSelected(formData, valueList.get(i).getValue(), formId);
        } else {
            dropDownValueSelectListener.onEmptyDropdownSelected(formData);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        dropDownValueSelectListener.onEmptyDropdownSelected(formData);
    }
}
