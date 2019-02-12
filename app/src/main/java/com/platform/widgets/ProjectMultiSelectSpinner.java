package com.platform.widgets;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

import com.platform.R;
import com.platform.models.profile.OrganizationProject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class ProjectMultiSelectSpinner extends android.support.v7.widget.AppCompatSpinner implements
        DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    private List<String> projectNames;
    private boolean[] selectedValues;
    private String defaultText;
    private ProjectMultiSpinnerListener listener;

    public ProjectMultiSelectSpinner(Context context) {
        super(context);
    }

    public ProjectMultiSelectSpinner(Context context, int mode) {
        super(context, mode);
    }

    public ProjectMultiSelectSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProjectMultiSelectSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ProjectMultiSelectSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
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
        for (int i = 0; i < projectNames.size(); i++) {
            if (selectedValues[i]) {
                spinnerSelectedText.append(projectNames.get(i));
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
        listener.onProjectsSelected(selectedValues);
    }

    @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
    @Override
    public boolean performClick() {
        super.performClick();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string.project));
        builder.setMultiChoiceItems(
                projectNames.toArray(new CharSequence[projectNames.size()]), selectedValues, this);
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.cancel());
        builder.setOnCancelListener(this);
        builder.setCancelable(false);
        builder.show();
        return true;
    }

    public void setItems(List<OrganizationProject> items, String allText,
                         ProjectMultiSpinnerListener listener) {

        this.projectNames = new ArrayList<>();
        for (OrganizationProject project : items) {
            this.projectNames.add(project.getOrgProjectName());
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

    public interface ProjectMultiSpinnerListener {
        void onProjectsSelected(boolean[] selected);
    }
}
