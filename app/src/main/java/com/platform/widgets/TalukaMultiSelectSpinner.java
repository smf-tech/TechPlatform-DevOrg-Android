package com.platform.widgets;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.platform.R;
import com.platform.models.profile.JurisdictionLevel;

import java.util.ArrayList;
import java.util.List;

public class TalukaMultiSelectSpinner extends android.support.v7.widget.AppCompatSpinner implements
        DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    private List<String> talukaNames;
    private boolean[] selectedValues;
    private String defaultText;
    private TalukaMultiSpinnerListener listener;
    private final String TAG = TalukaMultiSelectSpinner.class.getName();

    public TalukaMultiSelectSpinner(Context context) {
        super(context);
    }

    public TalukaMultiSelectSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TalukaMultiSelectSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        selectedValues[which] = isChecked;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // refresh text on spinner
        StringBuilder spinnerSelectedText = new StringBuilder();
        for (int i = 0; i < talukaNames.size(); i++) {
            if (selectedValues[i]) {
                spinnerSelectedText.append(talukaNames.get(i));
                spinnerSelectedText.append(", ");
            }
        }

        String spinnerText;
        if (spinnerSelectedText.length() != 0) {
            spinnerText = spinnerSelectedText.toString();
            if (spinnerText.length() > 2)
                spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
        } else {
            spinnerText = defaultText;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, new String[]{spinnerText});
        setAdapter(adapter);
        listener.onTalukasSelected(selectedValues);
    }

    @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
    @Override
    public boolean performClick() {
        if (talukaNames == null) {
            return false;
        }
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getResources().getString(R.string.taluka));
            builder.setMultiChoiceItems(
                    talukaNames.toArray(new CharSequence[talukaNames.size()]), selectedValues, this);
            builder.setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.cancel());
            builder.setOnCancelListener(this);
            builder.setCancelable(false);
            builder.show();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return true;
    }

    public void setItems(List<JurisdictionLevel> items, String allText,
                         TalukaMultiSpinnerListener listener) {

        this.talukaNames = new ArrayList<>();
        for (JurisdictionLevel jurisdictionLevel : items) {
            this.talukaNames.add(jurisdictionLevel.getJurisdictionLevelName());
        }

        this.defaultText = allText;
        this.listener = listener;

        // all de-selectedValues by default
        selectedValues = new boolean[items.size()];
        for (int i = 0; i < selectedValues.length; i++) {
            selectedValues[i] = false;
        }

        // all text on the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, new String[]{allText});
        setAdapter(adapter);
    }

    public interface TalukaMultiSpinnerListener {
        void onTalukasSelected(boolean[] selected);
    }
}
