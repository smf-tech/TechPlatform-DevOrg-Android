package com.platform.widgets;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

import com.platform.R;
import com.platform.models.profile.JurisdictionLevel;

import java.util.ArrayList;
import java.util.List;

public class VillageMultiSelectSpinner extends android.support.v7.widget.AppCompatSpinner implements
        DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    private String defaultText;
    private List<String> villageNames;
    private boolean[] selectedValues;
    private VillageMultiSpinnerListener listener;

    public VillageMultiSelectSpinner(Context context) {
        super(context);
    }

    public VillageMultiSelectSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VillageMultiSelectSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
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
        for (int i = 0; i < villageNames.size(); i++) {
            if (selectedValues[i]) {
                spinnerSelectedText.append(villageNames.get(i));
                spinnerSelectedText.append(", ");
            }
        }

        String spinnerText;
        if (spinnerSelectedText.length() != 0) {
            spinnerText = spinnerSelectedText.toString();
            if (spinnerText.length() > 2) {
                spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
            }
        } else {
            spinnerText = defaultText;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, new String[]{spinnerText});
        setAdapter(adapter);
        listener.onVillagesSelected(selectedValues);
    }

    @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
    @Override
    public boolean performClick() {
        super.performClick();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string.village));
        builder.setMultiChoiceItems(
                villageNames.toArray(new CharSequence[villageNames.size()]), selectedValues, this);
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.cancel());
        builder.setOnCancelListener(this);
        builder.setCancelable(false);
        builder.show();
        return true;
    }

    public void setItems(List<JurisdictionLevel> items, String allText,
                         VillageMultiSpinnerListener listener) {

        this.villageNames = new ArrayList<>();
        for (JurisdictionLevel jurisdictionLevel : items) {
            this.villageNames.add(jurisdictionLevel.getJurisdictionLevelName());
        }

        this.defaultText = allText;
        this.listener = listener;

        // all de-selectedValues by default
        selectedValues = new boolean[items.size()];
        for (int i = 0; i < selectedValues.length; i++)
            selectedValues[i] = false;

        // all text on the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, new String[]{allText});
        setAdapter(adapter);
    }

    public interface VillageMultiSpinnerListener {
        void onVillagesSelected(boolean[] selected);
    }
}
