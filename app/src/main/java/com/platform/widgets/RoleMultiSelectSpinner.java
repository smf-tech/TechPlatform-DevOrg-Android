package com.platform.widgets;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

import com.platform.models.profile.OrganizationRole;

import java.util.ArrayList;
import java.util.List;

public class RoleMultiSelectSpinner extends android.support.v7.widget.AppCompatSpinner implements
        DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener {
    private List<OrganizationRole> roles;
    private List<String> roleNames;
    private boolean[] selectedValues;
    private String defaultText;
    private RoleMultiSpinnerListener listener;

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
        if (isChecked)
            selectedValues[which] = true;
        else
            selectedValues[which] = false;
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        // refresh text on spinner
        StringBuffer spinnerSelectedText = new StringBuffer();
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
                android.R.layout.simple_spinner_item,
                new String[]{spinnerText});
        setAdapter(adapter);
        listener.onRolesSelected(selectedValues);

    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMultiChoiceItems(
                roleNames.toArray(new CharSequence[roleNames.size()]), selectedValues, this);
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.setOnCancelListener(this);
        builder.setCancelable(false);
        builder.show();
        return true;
    }

    public void setItems(List<OrganizationRole> items, String allText,
                         RoleMultiSpinnerListener listener) {
        this.roles = items;
        roleNames = new ArrayList<>();
        for (OrganizationRole role :
                items) {
            this.roleNames.add(role.getDisplayName());
        }
        this.defaultText = allText;
        this.listener = listener;
        // all de-selectedValues by default
        selectedValues = new boolean[items.size()];
        for (int i = 0; i < selectedValues.length; i++)
            selectedValues[i] = false;

        // all text on the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, new String[]{allText});
        setAdapter(adapter);
    }

    public interface RoleMultiSpinnerListener {
        public void onRolesSelected(boolean[] selected);
    }

}
