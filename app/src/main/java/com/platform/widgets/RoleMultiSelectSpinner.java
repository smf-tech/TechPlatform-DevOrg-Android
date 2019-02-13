package com.platform.widgets;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.platform.R;
import com.platform.models.profile.OrganizationRole;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class RoleMultiSelectSpinner extends android.support.v7.widget.AppCompatSpinner implements
        DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    private List<String> roleNames;
    private boolean[] selectedValues;
    private String defaultText;
    private RoleMultiSpinnerListener listener;
    private final String TAG = RoleMultiSelectSpinner.class.getName();

    public RoleMultiSelectSpinner(Context context) {
        super(context);
    }

    public RoleMultiSelectSpinner(Context context, int mode) {
        super(context, mode);
    }

    public RoleMultiSelectSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoleMultiSelectSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RoleMultiSelectSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
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
        for (int i = 0; i < roleNames.size(); i++) {
            if (selectedValues[i]) {
                spinnerSelectedText.append(roleNames.get(i));
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
        listener.onRolesSelected(selectedValues);
    }

    @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
    @Override
    public boolean performClick() {
        if (roleNames == null) {
            return false;
        }

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getResources().getString(R.string.role));
            builder.setMultiChoiceItems(
                    roleNames.toArray(new CharSequence[roleNames.size()]), selectedValues, this);
            builder.setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.cancel());
            builder.setOnCancelListener(this);
            builder.setCancelable(false);
            builder.show();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return true;
    }

    public void setItems(List<OrganizationRole> items, String allText,
                         RoleMultiSpinnerListener listener) {

        roleNames = new ArrayList<>();
        for (OrganizationRole role : items) {
            this.roleNames.add(role.getDisplayName());
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

    public interface RoleMultiSpinnerListener {
        void onRolesSelected(boolean[] selected);
    }
}
