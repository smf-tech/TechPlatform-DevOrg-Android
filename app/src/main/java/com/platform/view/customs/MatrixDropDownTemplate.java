package com.platform.view.customs;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.platform.Platform;
import com.platform.R;
import com.platform.listeners.MatrixDynamicDropDownValueSelectListener;
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
    private long rowIndex;
    private HashMap<String, String> matrixDynamicInnerMap;

    public long getRowIndex() {
        return rowIndex;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Elements getFormData() {
        return formData;
    }

    public Column getColumn() {
        return column;
    }

    MatrixDropDownTemplate(FormFragment context, Elements formData, Column column,
                           String formId,
                           HashMap<String, String> matrixDynamicInnerMap,
                           MatrixDynamicDropDownValueSelectListener listener) {

        this.formData = formData;
        this.context = new WeakReference<>(context);
        this.dropDownValueSelectListener = listener;
        this.formId = formId;
        this.column = column;
        this.rowIndex = System.currentTimeMillis();
        this.matrixDynamicInnerMap = matrixDynamicInnerMap;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public HashMap<String, String> getMatrixDynamicInnerMap() {
        return matrixDynamicInnerMap;
    }

    synchronized public View init(String mandatory) {
        return dropDownView(mandatory);
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
        layoutParams.setMargins(10, -60, 0, 0);
        layoutParams.weight = this.weight;
        baseLayout.setLayoutParams(layoutParams);

        spinner = baseLayout.findViewById(R.id.sp_single_select);

        if (column != null && !TextUtils.isEmpty(column.getTitle().getLocaleValue())) {
            String label = column.getTitle().getLocaleValue() + mandatory;
            ((TextView) baseLayout.findViewById(R.id.dropdown_label)).setText(label);
        }

        FormSpinnerAdapter adapter = new FormSpinnerAdapter(context.get().getContext(),
                R.layout.layout_spinner_item, valueList);
        spinner.setTitle(column.getTitle().getLocaleValue());
        spinner.setDialogAdapterFactory(list -> adapter);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        return baseLayout;
    }

    void setListData(List<Choice> valueList, HashMap<String, String> valuesMap) {
        if (valueList != null) {
            try {
                this.valueList = valueList;
                FormSpinnerAdapter adapter = (FormSpinnerAdapter) spinner.getAdapter();
                adapter.clear();
                adapter.addAll(this.valueList);
                adapter.notifyDataSetChanged();

                if (this.valueList.size() > 0) {
                    this.setSelectedItem(0);
                }

                for (int index = 0; index < this.valueList.size(); index++) {
                    if (formData.getAnswerArray() != null &&
                            this.valueList.get(index).getText() != null &&
                            !TextUtils.isEmpty(this.valueList.get(index).getText().getLocaleValue()) &&
                            Objects.requireNonNull(valuesMap.get(column.getName()))
                                    .equals(this.valueList.get(index).getValue())) {
                        this.setSelectedItem(index);
                        break;
                    }
                }
            } catch (Exception e) {
                Log.e("TAG", "EXCEPTION_IN_SET_DATA_1");
            }
        }
    }

    void setSelectedItem(int position) {
        if (spinner != null) {
            spinner.setSelection(position, true);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Choice selectedChoice = ((FormSpinnerAdapter) spinner.getAdapter()).getItem(i);
        if (i != -1) {
            if (selectedChoice != null && !selectedChoice.getValue().equals(context.get().getString(R.string.default_select))) {
                TextView tv = (TextView) adapterView.getSelectedView();
                if (tv != null) {
                    tv.setTextColor(ContextCompat.getColor(Platform.getInstance(), R.color.colorPrimaryDark));
                }
                dropDownValueSelectListener.onDropdownValueSelected(matrixDynamicInnerMap, column,
                        selectedChoice.getValue(), formId);
            } else if (selectedChoice != null && selectedChoice.getValue().equals(context.get().getString(R.string.default_select))) {
                dropDownValueSelectListener.onEmptyDropdownSelected(matrixDynamicInnerMap, column);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        dropDownValueSelectListener.onEmptyDropdownSelected(matrixDynamicInnerMap, column);
    }
}
