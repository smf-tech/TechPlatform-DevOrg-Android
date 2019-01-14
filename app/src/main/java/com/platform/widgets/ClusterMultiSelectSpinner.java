package com.platform.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

import com.platform.models.profile.JurisdictionLevel;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class ClusterMultiSelectSpinner extends android.support.v7.widget.AppCompatSpinner implements
        DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    private List<String> clusterNames;
    private boolean[] selectedValues;
    private String defaultText;
    private ClusterMultiSpinnerListener listener;

    public ClusterMultiSelectSpinner(Context context) {
        super(context);
    }

    public ClusterMultiSelectSpinner(Context context, int mode) {
        super(context, mode);
    }

    public ClusterMultiSelectSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClusterMultiSelectSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ClusterMultiSelectSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        selectedValues[which] = isChecked;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // refresh text on spinner
        StringBuilder spinnerSelectedText = new StringBuilder();
        for (int i = 0; i < clusterNames.size(); i++) {
            if (selectedValues[i]) {
                spinnerSelectedText.append(clusterNames.get(i));
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
        listener.onClustersSelected(selectedValues);
    }

    @SuppressLint("ClickableViewAccessibility")
    @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMultiChoiceItems(
                clusterNames.toArray(new CharSequence[clusterNames.size()]), selectedValues, this);
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.cancel());
        builder.setOnCancelListener(this);
        builder.setCancelable(false);
        builder.show();
        return true;
    }

    public void setItems(List<JurisdictionLevel> items, String allText,
                         ClusterMultiSpinnerListener listener) {

        this.clusterNames = new ArrayList<>();
        for (JurisdictionLevel jurisdictionLevel : items) {
            this.clusterNames.add(jurisdictionLevel.getJurisdictionLevelName());
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

    public interface ClusterMultiSpinnerListener {
        void onClustersSelected(boolean[] selected);
    }
}
