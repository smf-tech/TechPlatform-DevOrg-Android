package com.platform.view.customs;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.platform.Platform;
import com.platform.R;
import com.platform.listeners.MatrixDynamicDropDownValueSelectListener;
import com.platform.models.LocaleData;
import com.platform.models.forms.Choice;
import com.platform.models.forms.Column;
import com.platform.models.forms.Elements;
import com.platform.view.adapters.FormSpinnerAdapter;
import com.platform.view.fragments.FormFragment;
import com.platform.widgets.PlatformSpinner;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import androidx.core.content.ContextCompat;

@SuppressWarnings({"CanBeFinal", "WeakerAccess"})
public class MatrixDropDownTemplate implements AdapterView.OnItemSelectedListener {

    private final String TAG = this.getClass().getSimpleName();
    private Elements formData;
    private PlatformSpinner spinner;
    private WeakReference<FormFragment> context;
    private List<Choice> valueList = new ArrayList<>();
    private MatrixDynamicDropDownValueSelectListener dropDownValueSelectListener;
    private String tag;
    private String formId;
    private float weight = 1f;
    private Column column;
    private int rowIndex;
    private MatrixDynamicTemplate mParent;
    private HashMap<String, String> matrixDynamicInnerMap;

    @SuppressWarnings("unused")
    public String getFormId() {
        return formId;
    }

//    public Column getColumn() {
//        return column;
//    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    MatrixDropDownTemplate(MatrixDynamicTemplate parent, Elements formData, Column column,
                           FormFragment context, HashMap<String, String> matrixDynamicInnerMap,
                           MatrixDynamicDropDownValueSelectListener listener, String formId, int index) {

        this.mParent = parent;
        this.formData = formData;
        this.context = new WeakReference<>(context);
        this.dropDownValueSelectListener = listener;
        this.formId = formId;
        this.column = column;
        this.rowIndex = index;
        this.matrixDynamicInnerMap = matrixDynamicInnerMap;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

//    public List<Choice> getValueList() {
//        return valueList;
//    }

    synchronized public View init(String mandatory) {
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

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 0, 0, 0);
        layoutParams.weight = this.weight;
        baseLayout.setLayoutParams(layoutParams);

        spinner = baseLayout.findViewById(R.id.sp_single_select);

        if (column != null && !TextUtils.isEmpty(column.getTitle().getLocaleValue())) {
            String label = column.getTitle().getLocaleValue() + mandatory;
            ((TextView) baseLayout.findViewById(R.id.dropdown_label)).setText(label);
        }

        FormSpinnerAdapter adapter = new FormSpinnerAdapter(context.get().getContext(),
                R.layout.layout_spinner_item, valueList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        return baseLayout;
    }

    @SuppressWarnings("unchecked")
    void setListData(List<Choice> valueList, HashMap<String, String> valuesMap, boolean isInEditMode,
                     boolean isPartiallySaved, int rowIndex) {

        if (this.rowIndex == rowIndex) {
            if (valueList != null) {
                try {
                    boolean isValueSet = false;
                    this.valueList = valueList;
                    FormSpinnerAdapter adapter = (FormSpinnerAdapter) spinner.getAdapter();
                    adapter.clear();
                    adapter.addAll(valueList);
                    adapter.notifyDataSetChanged();

                    if (valueList.size() > 0) {
                        this.setSelectedItem(0);
                    }

                    if (column.getChoices() != null && !column.getChoices().isEmpty()) {
                        for (int index = 0; index < column.getChoices().size(); index++) {
                            if (formData.getAnswerArray() != null &&
                                    column.getChoices().get(index).getText() != null &&
                                    !TextUtils.isEmpty(column.getChoices().get(index).getText().getLocaleValue()) &&
                                    Objects.requireNonNull(valuesMap.get(column.getName()))
                                            .equals(column.getChoices().get(index).getValue())) {
                                isValueSet = true;
                                this.setSelectedItem(index);
                            }
                        }
                    }

                    // This code will add submitted value in list and update the adapter, in API response
                    // submitted value is not coming hence this is workaround.
                    if (isInEditMode && !isPartiallySaved) {
                        if (!isValueSet && !TextUtils.isEmpty(valuesMap.get(column.getName()))) {
                            Choice ch = new Choice();
                            LocaleData ld = new LocaleData(valuesMap.get(column.getName()));
                            ch.setText(ld);

                            this.valueList.add(ch);
                            valueList.add(ch);
                            adapter.clear();
                            adapter.addAll(valueList);
                            adapter.notifyDataSetChanged();
                            this.setSelectedItem(valueList.size() - 1);
                        }
                    }
                } catch (Exception e) {
                    Log.e("TAG", "EXCEPTION_IN_SET_DATA_1");
                }
            }
        }

        if (this.rowIndex == 0 && rowIndex == 1) {
            if (isInEditMode && !isPartiallySaved) {
                try {
                    if (isMachineCodeSpinner(valueList)) {
                        FormSpinnerAdapter adapter = (FormSpinnerAdapter) spinner.getAdapter();
                        HashMap<String, String> formData = mParent.getFormData(this.rowIndex);
                        if (formData != null) {
                            if (!TextUtils.isEmpty(formData.get("machine_code"))) {
                                Choice ch = new Choice();
                                LocaleData ld = new LocaleData(formData.get("machine_code"));
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
                } catch (Exception e) {
                    Log.e("TAG", "EXCEPTION_IN_SET_DATA_2");
                }
            }
        }
    }

    boolean isMachineCodeSpinner(List<Choice> valueList) {
        if (valueList != null && valueList.size() > 0) {
            for (Choice ch : valueList) {
                if (ch.getValue().equalsIgnoreCase("new")) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    void setSelectedItem(int position) {
        if (spinner != null) {
            spinner.setSelection(position, true);
        }
    }

//    int getSelectedItem() {
//        if (spinner != null) {
//            return spinner.getSelectedItemPosition();
//        }
//        return 0;
//    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i != 0) {
            TextView tv = (TextView) adapterView.getSelectedView();
            if (tv != null) {
                tv.setTextColor(ContextCompat.getColor(Platform.getInstance(), R.color.colorPrimaryDark));
            }
            dropDownValueSelectListener.onDropdownValueSelected(matrixDynamicInnerMap, column,
                    valueList.get(i).getValue(), formId);
        } else {
            dropDownValueSelectListener.onEmptyDropdownSelected(matrixDynamicInnerMap, column);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        dropDownValueSelectListener.onEmptyDropdownSelected(matrixDynamicInnerMap, column);
    }
}
