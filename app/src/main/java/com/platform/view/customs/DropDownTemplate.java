package com.platform.view.customs;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.platform.Platform;
import com.platform.R;
import com.platform.models.forms.Elements;
import com.platform.view.adapters.FormSpinnerAdapter;
import com.platform.view.fragments.FormFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class DropDownTemplate implements AdapterView.OnItemSelectedListener {

    private final String TAG = this.getClass().getSimpleName();
    private Elements formData;
    private Spinner spinner;
    private WeakReference<FormFragment> context;

    DropDownTemplate(Elements formData, FormFragment context) {
        this.formData = formData;
        this.context = new WeakReference<>(context);
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

        spinner = baseLayout.findViewById(R.id.sp_single_select);

        String label = formData.getTitle() + mandatory;
        ((TextView) baseLayout.findViewById(R.id.dropdown_label)).setText(label);

        FormSpinnerAdapter adapter = new FormSpinnerAdapter(context.get().getContext(),
                R.layout.layout_spinner_item, R.id.dropdown_list_item, new ArrayList<>());
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        if (!formData.isRequired()) {
            spinner.setEnabled(false);
        }

        return baseLayout;
    }

    @SuppressWarnings("unchecked")
    void setListData(List<String> valueList) {
        if (valueList != null) {
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
            adapter.addAll(valueList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        TextView tv = (TextView) adapterView.getSelectedView();
        if (tv != null) {
            if (i == 0) {
                tv.setTextColor(ContextCompat.getColor(Platform.getInstance(), R.color.dark_blue));
            } else {
                tv.setTextColor(ContextCompat.getColor(Platform.getInstance(), R.color.colorPrimaryDark));
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
