package com.octopus.widgets;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.octopus.R;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class MultiSelectSpinner extends androidx.appcompat.widget.AppCompatSpinner implements
        DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    private List<String> stringList;
    private boolean[] selectedValues;
    private String defaultText;
    private MultiSpinnerListener listener;
    private String spinnerName;
    private final String TAG = MultiSelectSpinner.class.getName();

    public MultiSelectSpinner(Context context) {
        super(context);
    }

    public MultiSelectSpinner(Context context, int mode) {
        super(context, mode);
    }

    public MultiSelectSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiSelectSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MultiSelectSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        selectedValues[which] = isChecked;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // refresh text on spinner
        setPreFilledText();
    }

    public void setPreFilledText() {
        StringBuilder spinnerSelectedText = new StringBuilder();
        for (int i = 0; i < stringList.size(); i++) {
            if (selectedValues[i]) {
                spinnerSelectedText.append(stringList.get(i));
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
                R.layout.layout_spinner_item, new String[]{spinnerText});
        setAdapter(adapter);
        listener.onValuesSelected(selectedValues, spinnerName);
    }
    public void setSelectedFilledText() {
        StringBuilder spinnerSelectedText = new StringBuilder();
        for (int i = 0; i < stringList.size(); i++) {
            if (selectedValues[i]) {
                spinnerSelectedText.append(stringList.get(i));
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
                R.layout.layout_spinner_item, new String[]{spinnerText});
        setAdapter(adapter);

    }

    @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
    @Override
    public boolean performClick() {
        if (stringList == null) {
            super.performClick();
            return false;
        }

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            TextView tvTitle = new TextView(getContext());
            tvTitle.setText(spinnerName);
            tvTitle.setBackgroundResource(R.color.colorPrimaryDark);
            tvTitle.setTextColor(getResources().getColor(R.color.white));
            tvTitle.setGravity(Gravity.CENTER);
            tvTitle.setHeight(150);
            tvTitle.setTextSize(16);
//            Typeface face = Typeface.createFromAsset(getContext().getAssets(),
//                    "fonts/opensans_bold.ttf");
//            tvTitle.setTypeface(face);
            tvTitle.setTypeface(tvTitle.getTypeface(), Typeface.BOLD);

            builder.setCustomTitle(tvTitle);
//            builder.setTitle(spinnerName);
            builder.setMultiChoiceItems(
                    stringList.toArray(new CharSequence[stringList.size()]), selectedValues, this);
            builder.setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.cancel());
            builder.setOnCancelListener(this);
            builder.setCancelable(false);
            builder.getContext().getTheme().applyStyle(R.style.textinputstyle,true);
            builder.show();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return true;
    }

    public void setSelectedValues(boolean[] selectedValues) {
        this.selectedValues = selectedValues;
    }

    public void setItems(List<String> items, String allText,
                         MultiSpinnerListener listener) {

        this.stringList = new ArrayList<>();
        this.stringList.addAll(items);

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

    public interface MultiSpinnerListener {
        void onValuesSelected(boolean[] selected, String spinnerName);
    }

    public void setSpinnerName(String spinnerName) {
        this.spinnerName = spinnerName;
    }
}
