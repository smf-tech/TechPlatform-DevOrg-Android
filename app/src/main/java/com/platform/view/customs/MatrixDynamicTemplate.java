package com.platform.view.customs;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.platform.R;
import com.platform.listeners.MatrixDynamicValueChangeListener;
import com.platform.models.forms.Column;
import com.platform.models.forms.Elements;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.fragments.FormFragment;

import java.lang.ref.WeakReference;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

class MatrixDynamicTemplate {
    private final String TAG = this.getClass().getSimpleName();
    private Elements elements;
    private WeakReference<FormFragment> context;
    private List<HashMap<String, String>> matrixDynamicValuesList;
    private MatrixDynamicValueChangeListener matrixDynamicValueChangeListener;

    MatrixDynamicTemplate(Elements elements, FormFragment context, MatrixDynamicValueChangeListener matrixDynamicValueChangeListener) {
        this.context = new WeakReference<>(context);
        this.elements = elements;
        this.matrixDynamicValueChangeListener = matrixDynamicValueChangeListener;
    }

    synchronized View matrixDynamicView() {
        if (context == null || context.get() == null) {
            Log.e(TAG, "WeakReference returned null");
            return null;
        }

        final LinearLayout matrixDynamicView = (LinearLayout) View.inflate(
                context.get().getContext(), R.layout.row_matrix_dynamic, null);

        addTitle(elements, matrixDynamicView);

        matrixDynamicValuesList = new ArrayList<>();
        if (elements.getColumns() != null && !elements.getColumns().isEmpty()) {
            addRow(elements, matrixDynamicView, Constants.Action.ACTION_ADD);
        }

        return matrixDynamicView;
    }

    private void addTitle(Elements elements, LinearLayout matrixDynamicView) {
        TextView txtName = (TextView) View.inflate(context.get().getContext(), R.layout.item_matrix_dynamic_title, null);
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textViewParams.setMargins(62, 16, 16, 16);
        txtName.setLayoutParams(textViewParams);
        txtName.setText(elements.getTitle().getLocaleValue());
        matrixDynamicView.addView(txtName);
    }

    private void addRow(Elements elements, LinearLayout matrixDynamicView, String action) {
        LinearLayout innerLinearLayout = createInnerLinearLayout();
        HashMap<String, String> matrixDynamicMap = new HashMap<>();

        for (int currentColumn = 0; currentColumn < elements.getColumns().size(); currentColumn++) {
            if (!TextUtils.isEmpty(elements.getColumns().get(currentColumn).getCellType())) {
                switch (elements.getColumns().get(currentColumn).getCellType()) {
                    case Constants.FormsFactory.TEXT_TEMPLATE:
                        View view = matrixDynamicTextTemplate(elements.getColumns().get(currentColumn), elements, matrixDynamicMap);
                        innerLinearLayout.addView(view);
                        break;
                }
            }
        }

        switch (action) {
            case Constants.Action.ACTION_ADD:
                LinearLayout addLnr = createAddImageView(elements, matrixDynamicView);
                innerLinearLayout.addView(addLnr);
                break;

            case Constants.Action.ACTION_DELETE:
                LinearLayout deleteLnr = createDeleteImageView(innerLinearLayout, matrixDynamicView, matrixDynamicMap);
                innerLinearLayout.addView(deleteLnr);
                break;
        }

        matrixDynamicView.addView(innerLinearLayout);
    }

    private LinearLayout createInnerLinearLayout() {
        LinearLayout innerLinearLayout = (LinearLayout) View.inflate(context.get().getContext(), R.layout.row_inner_matrix_dynamic, null);
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayoutParams.setMargins(62, 20, 20, 0);
        innerLinearLayout.setLayoutParams(linearLayoutParams);
        return innerLinearLayout;
    }

    private LinearLayout createAddImageView(Elements elements, LinearLayout matrixDynamicView) {
        LinearLayout addLnr = (LinearLayout) View.inflate(context.get().getContext(), R.layout.item_matrix_dynamic_add_image, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 0.10f;
        addLnr.setLayoutParams(layoutParams);
        ImageButton addImg = addLnr.findViewById(R.id.iv_matrix_dynamic_add);
        addImg.setOnClickListener(v -> addRow(elements, matrixDynamicView, Constants.Action.ACTION_DELETE));
        return addLnr;
    }

    private LinearLayout createDeleteImageView(LinearLayout innerLinearLayout, LinearLayout matrixDynamicView, HashMap<String, String> matrixDynamicMap) {
        LinearLayout deleteLnr = (LinearLayout) View.inflate(context.get().getContext(), R.layout.item_matrix_dynamic_delete_image, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 0.10f;
        deleteLnr.setLayoutParams(layoutParams);
        ImageButton deleteImg = deleteLnr.findViewById(R.id.iv_matrix_dynamic_delete);
        deleteImg.setOnClickListener(v -> {
            innerLinearLayout.removeAllViewsInLayout();
            matrixDynamicView.removeView(innerLinearLayout);
            matrixDynamicValuesList.remove(matrixDynamicMap);
        });
        return deleteLnr;
    }

    @SuppressWarnings("deprecation")
    private View matrixDynamicTextTemplate(final Column column, final Elements elements, final HashMap<String, String> matrixDynamicMap) {

        if (context == null || context.get() == null) {
            Log.e(TAG, "View returned null");
            return null;
        }

        EditText textInputField = new EditText(context.get().getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 0.45f;
        textInputField.setLayoutParams(layoutParams);
        textInputField.setBackground(context.get().getResources().getDrawable(R.drawable.bg_blue_box));

        if (column.getTitle() != null && !TextUtils.isEmpty(column.getTitle().getLocaleValue())) {
            textInputField.setHint(column.getTitle().getLocaleValue());
        }

        if (!TextUtils.isEmpty(column.getInputType())) {
            setInputType(column.getInputType(), textInputField);
        }

        textInputField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(column.getName()) && !TextUtils.isEmpty(charSequence.toString())) {
                    if (!TextUtils.isEmpty(column.getInputType()) &&
                            column.getInputType().equalsIgnoreCase(Constants.FormInputType.INPUT_TYPE_DATE)) {
                        matrixDynamicMap.put(column.getName(), ("" + Util.getDateInLong(charSequence.toString())).trim());
                    } else {
                        matrixDynamicMap.put(column.getName(), charSequence.toString().trim());
                    }
                    if (!matrixDynamicValuesList.contains(matrixDynamicMap)) {
                        matrixDynamicValuesList.add(matrixDynamicMap);
                    }
                    if (matrixDynamicMap.size() == elements.getColumns().size()) {
                        matrixDynamicValueChangeListener.onValueChanged(elements.getName(), matrixDynamicValuesList);
                    } else {
                        matrixDynamicValuesList.remove(matrixDynamicMap);
                    }
                } else {
                    matrixDynamicMap.clear();
                    matrixDynamicValuesList.remove(matrixDynamicMap);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return textInputField;
    }

    private void setInputType(String type, EditText textInputField) {
        if (!TextUtils.isEmpty(type)) {
            switch (type) {
                case Constants.FormInputType.INPUT_TYPE_DATE:
                    textInputField.setFocusable(false);
                    textInputField.setClickable(false);
                    textInputField.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
                    textInputField.setOnClickListener(view -> showDateDialog(context.get().getContext(), textInputField));
                    break;

                case Constants.FormInputType.INPUT_TYPE_TIME:
                    textInputField.setFocusable(false);
                    textInputField.setClickable(false);
                    textInputField.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);
                    textInputField.setOnClickListener(view -> showTimeDialog(context.get().getContext(), textInputField));
                    break;

                case Constants.FormInputType.INPUT_TYPE_TELEPHONE:
                    textInputField.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;

                case Constants.FormInputType.INPUT_TYPE_NUMERIC:
                case Constants.FormInputType.INPUT_TYPE_NUMBER:
                case Constants.FormInputType.INPUT_TYPE_DECIMAL:
                    textInputField.setInputType(InputType.TYPE_CLASS_NUMBER |
                            InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    break;

                case Constants.FormInputType.INPUT_TYPE_ALPHABETS:
                case Constants.FormInputType.INPUT_TYPE_TEXT:
                    textInputField.setMaxLines(3);
                    textInputField.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
            }
        }
    }

    private void showDateDialog(Context context, final EditText editText) {
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dateDialog = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
            String date = year + "-" + Util.getTwoDigit(monthOfYear + 1) + "-" + Util.getTwoDigit(dayOfMonth);
            editText.setText(date);
        }, mYear, mMonth, mDay);

        dateDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dateDialog.show();
    }

    private void showTimeDialog(Context context, final EditText editText) {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePicker = new TimePickerDialog(context,
                (timePicker1, selectedHour, selectedMinute) -> editText.setText(
                        MessageFormat.format("{0}:{1}", selectedHour, selectedMinute)),
                hour, minute, false);
        timePicker.setTitle("Select Time");
        timePicker.show();
    }

}
