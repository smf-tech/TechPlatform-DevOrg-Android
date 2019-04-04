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
import java.util.Objects;

class MatrixDynamicTemplate {

    private final String TAG = this.getClass().getSimpleName();
    private final Elements elements;
    private final WeakReference<FormFragment> context;
    private List<HashMap<String, String>> matrixDynamicValuesList;
    private final MatrixDynamicValueChangeListener matrixDynamicValueChangeListener;

    MatrixDynamicTemplate(Elements elements, FormFragment context,
                          MatrixDynamicValueChangeListener matrixDynamicValueChangeListener) {

        this.context = new WeakReference<>(context);
        this.elements = elements;
        this.matrixDynamicValueChangeListener = matrixDynamicValueChangeListener;
    }

    synchronized View matrixDynamicView() {
        if (context.get() == null) {
            Log.e(TAG, "WeakReference returned null");
            return null;
        }

        final LinearLayout matrixDynamicView = (LinearLayout) View.inflate(
                context.get().getContext(), R.layout.row_matrix_dynamic, null);

        addTitle(elements, matrixDynamicView);

        if (elements.getmAnswerArray() != null && !elements.getmAnswerArray().isEmpty() &&
                elements.getColumns() != null && !elements.getColumns().isEmpty()) {
            matrixDynamicValuesList = elements.getmAnswerArray();
            for (int valueListIndex = 0; valueListIndex < matrixDynamicValuesList.size(); valueListIndex++) {
                if (valueListIndex == 0) {
                    addRow(elements, matrixDynamicView,
                            matrixDynamicValuesList.get(valueListIndex), Constants.Action.ACTION_ADD);
                } else {
                    addRow(elements, matrixDynamicView,
                            matrixDynamicValuesList.get(valueListIndex), Constants.Action.ACTION_DELETE);
                }
            }
        } else {
            matrixDynamicValuesList = new ArrayList<>();
            HashMap<String, String> matrixDynamicInnerMap = new HashMap<>();
            addRow(elements, matrixDynamicView, matrixDynamicInnerMap, Constants.Action.ACTION_ADD);
        }

        return matrixDynamicView;
    }

    private void addTitle(Elements elements, LinearLayout matrixDynamicView) {
        TextView txtName = (TextView) View.inflate(context.get().getContext(),
                R.layout.item_matrix_dynamic_title, null);

        LinearLayout.LayoutParams textViewParams
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        textViewParams.setMargins(62, 16, 16, 16);
        txtName.setLayoutParams(textViewParams);
        txtName.setText(elements.getTitle().getLocaleValue());
        matrixDynamicView.addView(txtName);
    }

    private void addRow(Elements elements, LinearLayout matrixDynamicView, HashMap<String,
            String> matrixDynamicInnerMap, String action) {

        LinearLayout innerLinearLayout = createInnerLinearLayout();

        for (int currentColumn = 0; currentColumn < elements.getColumns().size(); currentColumn++) {
            if (!TextUtils.isEmpty(elements.getColumns().get(currentColumn).getCellType())) {
                switch (elements.getColumns().get(currentColumn).getCellType()) {
                    case Constants.FormsFactory.TEXT_TEMPLATE:
                        View view = matrixDynamicTextTemplate(elements.getColumns().get(currentColumn),
                                elements, matrixDynamicInnerMap);
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
                LinearLayout deleteLnr = createDeleteImageView(innerLinearLayout,
                        matrixDynamicView, matrixDynamicInnerMap);
                innerLinearLayout.addView(deleteLnr);
                break;
        }

        matrixDynamicView.addView(innerLinearLayout);
    }

    private LinearLayout createInnerLinearLayout() {
        LinearLayout innerLinearLayout = (LinearLayout) View.inflate(context.get().getContext(),
                R.layout.row_inner_matrix_dynamic, null);
        LinearLayout.LayoutParams linearLayoutParams
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayoutParams.setMargins(62, 20, 20, 0);
        innerLinearLayout.setLayoutParams(linearLayoutParams);
        return innerLinearLayout;
    }

    private LinearLayout createAddImageView(Elements elements, LinearLayout matrixDynamicView) {
        LinearLayout addLnr = (LinearLayout) View.inflate(context.get().getContext(),
                R.layout.item_matrix_dynamic_add_image, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.weight = 0.10f;
        addLnr.setLayoutParams(layoutParams);
        ImageButton addImg = addLnr.findViewById(R.id.iv_matrix_dynamic_add);
        addImg.setOnClickListener(v -> {
            HashMap<String, String> matrixDynamicInnerMap = new HashMap<>();
            addRow(elements, matrixDynamicView, matrixDynamicInnerMap, Constants.Action.ACTION_DELETE);
        });
        return addLnr;
    }

    private LinearLayout createDeleteImageView(LinearLayout innerLinearLayout,
                                               LinearLayout matrixDynamicView,
                                               HashMap<String, String> matrixDynamicInnerMap) {

        LinearLayout deleteLnr = (LinearLayout) View.inflate(context.get().getContext(),
                R.layout.item_matrix_dynamic_delete_image, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.weight = 0.10f;
        deleteLnr.setLayoutParams(layoutParams);
        ImageButton deleteImg = deleteLnr.findViewById(R.id.iv_matrix_dynamic_delete);

        deleteImg.setOnClickListener(v -> {
            innerLinearLayout.removeAllViewsInLayout();
            matrixDynamicView.removeView(innerLinearLayout);
            matrixDynamicValuesList.remove(matrixDynamicInnerMap);
        });
        return deleteLnr;
    }

    @SuppressWarnings("deprecation")
    private View matrixDynamicTextTemplate(final Column column, final Elements elements,
                                           final HashMap<String, String> matrixDynamicInnerMap) {

        if (context.get() == null) {
            Log.e(TAG, "View returned null");
            return null;
        }

        EditText textInputField = new EditText(context.get().getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.weight = 0.45f;
        layoutParams.setMarginEnd(10);
        textInputField.setPadding(15, 25, 10, 25);
        textInputField.setLayoutParams(layoutParams);
        textInputField.setTextColor(context.get().getResources().getColor(R.color.colorPrimaryDark));
        textInputField.setBackground(context.get().getResources().getDrawable(R.drawable.bg_white_box));

        if (column.getTitle() != null && !TextUtils.isEmpty(column.getTitle().getLocaleValue())) {
            textInputField.setHint(column.getTitle().getLocaleValue());
        }

        if (!TextUtils.isEmpty(column.getInputType())) {
            setInputType(column.getInputType(), textInputField);
        }

        if (matrixDynamicInnerMap != null && !matrixDynamicInnerMap.isEmpty()) {
            if (!TextUtils.isEmpty(column.getInputType()) &&
                    column.getInputType().equalsIgnoreCase(Constants.FormInputType.INPUT_TYPE_DATE)) {
                try {
                    textInputField.setText(Util.getLongDateInString(Long.valueOf(
                            Objects.requireNonNull(matrixDynamicInnerMap.get(column.getName()))),
                            Constants.FORM_DATE));
                } catch (Exception e) {
                    Log.e(TAG, "DATE ISSUE");
                    textInputField.setText(Util.getLongDateInString(
                            Util.getCurrentTimeStamp(), Constants.FORM_DATE));
                }
            } else {
                textInputField.setText(matrixDynamicInnerMap.get(column.getName()));
            }
        }

        textInputField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (matrixDynamicInnerMap != null) {
                    if (!TextUtils.isEmpty(column.getName()) && !TextUtils.isEmpty(charSequence.toString())) {
                        if (!TextUtils.isEmpty(column.getInputType()) &&
                                column.getInputType().equalsIgnoreCase(Constants.FormInputType.INPUT_TYPE_DATE)) {
                            matrixDynamicInnerMap.put(column.getName(),
                                    ("" + Util.getDateInLong(charSequence.toString())).trim());
                        } else {
                            matrixDynamicInnerMap.put(column.getName(), charSequence.toString().trim());
                        }
                        if (!matrixDynamicValuesList.contains(matrixDynamicInnerMap)) {
                            matrixDynamicValuesList.add(matrixDynamicInnerMap);
                        }
                        if (matrixDynamicInnerMap.size() == elements.getColumns().size()) {
                            matrixDynamicValueChangeListener.onValueChanged(elements.getName(),
                                    matrixDynamicValuesList);
                        } else {
                            matrixDynamicValuesList.remove(matrixDynamicInnerMap);
                        }
                    } else {
                        matrixDynamicInnerMap.clear();
                        matrixDynamicValuesList.remove(matrixDynamicInnerMap);
                    }
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
                    textInputField.setOnClickListener(
                            view -> showDateDialog(context.get().getContext(), textInputField));
                    break;

                case Constants.FormInputType.INPUT_TYPE_TIME:
                    textInputField.setFocusable(false);
                    textInputField.setClickable(false);
                    textInputField.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);
                    textInputField.setOnClickListener(
                            view -> showTimeDialog(context.get().getContext(), textInputField));
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

        DatePickerDialog dateDialog
                = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
            String date = year + "-"
                    + Util.getTwoDigit(monthOfYear + 1)
                    + "-" + Util.getTwoDigit(dayOfMonth);
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
