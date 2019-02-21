package com.platform.view.customs;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.platform.Platform;
import com.platform.R;
import com.platform.listeners.DropDownValueSelectListener;
import com.platform.models.forms.Choice;
import com.platform.models.forms.Elements;
import com.platform.view.adapters.FormSpinnerAdapter;
import com.platform.view.fragments.FormFragment;

import java.lang.ref.WeakReference;
import java.util.List;

@SuppressWarnings({"CanBeFinal", "WeakerAccess"})
public class DropDownTemplate implements AdapterView.OnItemSelectedListener {

    private final String TAG = this.getClass().getSimpleName();
    private Elements formData;
    private WeakReference<FormFragment> context;
    private List<Choice> valueList;
    private DropDownValueSelectListener dropDownValueSelectListener;

    DropDownTemplate(Elements formData, FormFragment context, DropDownValueSelectListener listener, List<Choice> valueList) {
        this.formData = formData;
        this.context = new WeakReference<>(context);
        this.dropDownValueSelectListener = listener;
        this.valueList = valueList;
    }

    synchronized View init(String mandatory) {
        return dropDownView(mandatory);
    }

    @SuppressWarnings("ConstantConditions")
    private synchronized View dropDownView(String mandatory) {
        if (context == null || context.get() == null) {
            Log.e(TAG, "WeakReference returned null");
            return null;
        }

        RelativeLayout baseLayout = (RelativeLayout) View.inflate(context.get().getContext(),
                R.layout.form_dropdown_template, null);

        Spinner spinner = baseLayout.findViewById(R.id.sp_single_select);

        String label = formData.getTitle() + mandatory;
        ((TextView) baseLayout.findViewById(R.id.dropdown_label)).setText(label);

        FormSpinnerAdapter adapter = new FormSpinnerAdapter(context.get().getContext(),
                R.layout.layout_spinner_item, valueList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

//        if (formData.isRequired() != null) {
//            spinner.setEnabled(formData.isRequired());
//        } else {
//            spinner.setEnabled(false);
//        }

        return baseLayout;
    }

//    void setListData(List<Choice> valueList) {
//        if (valueList != null) {
//            this.valueList = valueList;
//            ArrayAdapter<Choice> adapter = (ArrayAdapter<Choice>) spinner.getAdapter();
//            adapter.addAll(valueList);
//            adapter.notifyDataSetChanged();
//        }
//    }
//
//    void setSelectedItem(int position) {
//        if (spinner != null) {
//            spinner.setSelection(position);
//        }
//    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        TextView tv = (TextView) adapterView.getSelectedView();
        if (tv != null) {
            tv.setTextColor(ContextCompat.getColor(Platform.getInstance(), R.color.colorPrimaryDark));
        }
        dropDownValueSelectListener.onDropdownValueSelected(formData, valueList.get(i).getValue());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
