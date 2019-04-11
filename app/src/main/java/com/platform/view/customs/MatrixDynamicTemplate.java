package com.platform.view.customs;

import android.text.Editable;
import android.text.InputFilter;
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
import com.platform.listeners.DropDownValueSelectListener;
import com.platform.listeners.MatrixDynamicValueChangeListener;
import com.platform.models.forms.Column;
import com.platform.models.forms.Elements;
import com.platform.models.forms.FormData;
import com.platform.models.forms.Page;
import com.platform.presenter.FormActivityPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.fragments.FormFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

class MatrixDynamicTemplate implements DropDownValueSelectListener {

    private final String TAG = this.getClass().getSimpleName();
    private final Elements elements;
    private final WeakReference<FormFragment> context;
    private List<HashMap<String, String>> matrixDynamicValuesList;
    private final MatrixDynamicValueChangeListener matrixDynamicValueChangeListener;
    private boolean mIsInEditMode;
    private boolean mIsPartiallySaved;
    private FormData formData;
    private FormActivityPresenter formActivityPresenter;

    MatrixDynamicTemplate(FormData formData, Elements elements, FormFragment context,
                          MatrixDynamicValueChangeListener matrixDynamicValueChangeListener,
                          boolean mIsInEditMode, boolean mIsPartiallySaved, FormActivityPresenter formActivityPresenter) {

        this.context = new WeakReference<>(context);
        this.elements = elements;
        this.matrixDynamicValueChangeListener = matrixDynamicValueChangeListener;
        this.mIsInEditMode = mIsInEditMode;
        this.mIsPartiallySaved = mIsPartiallySaved;
        this.formData = formData;
        this.formActivityPresenter = formActivityPresenter;
    }

    public Elements getElements() {
        return elements;
    }

    synchronized View matrixDynamicView() {
        if (context.get() == null) {
            Log.e(TAG, "WeakReference returned null");
            return null;
        }

        final LinearLayout matrixDynamicView = (LinearLayout) View.inflate(
                context.get().getContext(), R.layout.row_matrix_dynamic, null);

        addTitle(elements, matrixDynamicView);

        if (elements.getAnswerArray() != null && !elements.getAnswerArray().isEmpty() &&
                elements.getColumns() != null && !elements.getColumns().isEmpty()) {
            matrixDynamicValuesList = elements.getAnswerArray();
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
        if (elements.isRequired() != null) {
            txtName.setText(context.get().getResources().getString(R.string.form_field_mandatory, elements.getTitle().getLocaleValue(), Util.setFieldAsMandatory(elements.isRequired())));
        } else {
            txtName.setText(context.get().getResources().getString(R.string.form_field_mandatory, elements.getTitle().getLocaleValue(), Util.setFieldAsMandatory(false)));
        }
        matrixDynamicView.addView(txtName);
    }

    private void addRow(Elements elements, LinearLayout matrixDynamicView, HashMap<String,
            String> matrixDynamicInnerMap, String action) {
        int rowCount = elements.getColumns().size() % 2 == 0 ? elements.getColumns().size() / 2 : elements.getColumns().size() / 2 + 1;
        LinearLayout innerLinearLayout = null;
        LinearLayout innerItemLinearLayout = createInnerItemLinearLayout();
        for (int currentColumnIndex = 0; currentColumnIndex < elements.getColumns().size(); currentColumnIndex++) {
            Column currentColumn = elements.getColumns().get(currentColumnIndex);
            if (!TextUtils.isEmpty(currentColumn.getCellType())) {
                if (currentColumnIndex % 2 == 0) {
                    innerLinearLayout = createInnerLinearLayout();
                }
                switch (currentColumn.getCellType()) {
                    case Constants.FormsFactory.TEXT_TEMPLATE:
                        View view = matrixDynamicTextTemplate(currentColumn,
                                elements, matrixDynamicInnerMap);
                        if (innerLinearLayout != null) {
                            innerLinearLayout.addView(view);
                        }
                        break;

                    case Constants.FormsFactory.DROPDOWN_TEMPLATE:
                        MatrixDropDownTemplate template = new MatrixDropDownTemplate(elements, currentColumn, context.get(), this, formData.getId());
                        template.setWeight(0.45f);

                        View dropdownView;
                        if (elements.isRequired() != null) {
                            dropdownView = template.init(Util.setFieldAsMandatory(elements.isRequired()));
                        } else {
                            dropdownView = template.init(Util.setFieldAsMandatory(false));
                        }

                        if (innerLinearLayout != null) {
                            innerLinearLayout.addView(dropdownView);
                        }

                        if (currentColumn.getChoicesByUrl() == null) {
//                            Collections.sort(elements.getChoices(),
//                                    (o1, o2) -> o1.getText().getLocaleValue().compareTo(o2.getText().getLocaleValue()));
                            template.setListData(currentColumn.getChoices());
                        } else if (currentColumn.getChoicesByUrl() != null) {
                            //Online
                            if (Util.isConnected(context.get().getContext())) {
                                //Opened submitted/partially or offline saved form
                                if (mIsInEditMode) {
                                    //Partially saved form
                                    if (mIsPartiallySaved) {
                                        callChoicesAPI(elements.getName(), currentColumnIndex);
                                    }
                                    //Submitted form
                                    else {
                                        //Editable submitted form
                                        if (!TextUtils.isEmpty(formData.getEditable())
                                                && Boolean.parseBoolean(formData.getEditable())) {
                                            callChoicesAPI(elements.getName(), currentColumnIndex);
                                        }
                                        //Non editable submitted form
                                        else {
                                            String response = Util.readFromInternalStorage(Objects.requireNonNull
                                                            (context.get().getContext()),
                                                    formData.getId() + "_" + currentColumn.getName());
                                            if (!TextUtils.isEmpty(response)) {
                                                matrixDynamicValueChangeListener.showChoicesByUrlOffline(response, elements);
                                            } else {
                                                callChoicesAPI(elements.getName(), currentColumnIndex);
                                            }
                                        }
                                    }
                                }
                                //Opened new form
                                else {
                                    callChoicesAPI(elements.getName(), currentColumnIndex);
                                }
                            }
                            //Offline
                            else {
                                String response = Util.readFromInternalStorage(Objects.requireNonNull
                                                (context.get().getContext()),
                                        formData.getId() + "_" + elements.getName());
                                if (!TextUtils.isEmpty(response)) {
                                    matrixDynamicValueChangeListener.showChoicesByUrlOffline(response, elements);
                                }
                            }
                        }


                        break;
                }
                if (currentColumnIndex % 2 != 0) {
                    innerItemLinearLayout.addView(innerLinearLayout);
                    rowCount--;
                }

                if (rowCount == 0) {
                    switch (action) {
                        case Constants.Action.ACTION_ADD:
                            LinearLayout addLnr = createAddImageView(elements, matrixDynamicView);
                            if (innerLinearLayout != null) {
                                innerLinearLayout.addView(addLnr);
                            }
                            break;

                        case Constants.Action.ACTION_DELETE:
                            LinearLayout deleteLnr = createDeleteImageView(innerItemLinearLayout, matrixDynamicView, matrixDynamicInnerMap);
                            if (innerLinearLayout != null) {
                                innerLinearLayout.addView(deleteLnr);
                            }
                            break;
                    }
                }
            }
        }
        matrixDynamicView.addView(innerItemLinearLayout);
    }

    private LinearLayout createInnerLinearLayout() {
        return (LinearLayout) View.inflate(context.get().getContext(),
                R.layout.row_inner_matrix_dynamic, null);
    }

    private LinearLayout createInnerItemLinearLayout() {
        LinearLayout innerItemLinearLayout = (LinearLayout) View.inflate(context.get().getContext(),
                R.layout.row_inner_matrix_dynamic_item, null);
        LinearLayout.LayoutParams linearLayoutParams
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayoutParams.setMargins(62, 20, 20, 0);
        innerItemLinearLayout.setLayoutParams(linearLayoutParams);
        return innerItemLinearLayout;
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

    private LinearLayout createDeleteImageView(LinearLayout innerItemLinearLayout,
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
            innerItemLinearLayout.removeAllViewsInLayout();
            matrixDynamicView.removeView(innerItemLinearLayout);
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

        if (column.getRequired() != null) {
            textInputField.setHint(column.getTitle().getLocaleValue()
                    + Util.setFieldAsMandatory(column.getRequired()));
        } else {
            textInputField.setHint(column.getTitle() == null ?
                    "" : (column.getTitle().getLocaleValue() + Util.setFieldAsMandatory(false)));
        }

        if (!TextUtils.isEmpty(column.getInputType())) {
            Util.setInputType(context.get().getContext(), column.getInputType(), textInputField);
        }

        //set max length allowed
        if (column.getMaxLength() != null) {
            textInputField.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                    column.getMaxLength())});
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
                            matrixDynamicValueChangeListener.onMatrixDynamicValueChanged(elements.getName(),
                                    matrixDynamicValuesList);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return textInputField;
    }

    private void callChoicesAPI(String name, int columnIndex) {
        List<Page> pages = formData.getComponents().getPages();
        for (int pageIndex = 0; pageIndex < pages.size(); pageIndex++) {

            if (pages.get(pageIndex).getElements() != null &&
                    !pages.get(pageIndex).getElements().isEmpty()) {

                for (int elementIndex = 0; elementIndex < pages.get(pageIndex).getElements().size(); elementIndex++) {

                    if (pages.get(pageIndex).getElements().get(elementIndex) != null &&
                            pages.get(pageIndex).getElements().get(elementIndex).getColumns() != null &&
                            !pages.get(pageIndex).getElements().get(elementIndex).getColumns().isEmpty() &&
                            pages.get(pageIndex).getElements().get(elementIndex).getColumns().get(columnIndex) != null
                            && pages.get(pageIndex).getElements().get(elementIndex).getColumns().get(columnIndex).getChoicesByUrl() != null &&
                            pages.get(pageIndex).getElements().get(elementIndex).getName().equals(name) &&
                            !TextUtils.isEmpty(pages.get(pageIndex).getElements().get(elementIndex).getColumns().get(columnIndex).getChoicesByUrl().getUrl()) &&
                            !TextUtils.isEmpty(pages.get(pageIndex).getElements().get(elementIndex).getColumns().get(columnIndex).getChoicesByUrl().getTitleName())) {

                        formActivityPresenter.getChoicesByUrl(pages.get(pageIndex).getElements().get(elementIndex),
                                pageIndex, elementIndex, columnIndex, formData,
                                pages.get(pageIndex).getElements().get(elementIndex).getColumns().get(columnIndex).getChoicesByUrl().getUrl());
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onDropdownValueSelected(Elements formData, String value, String formId) {

    }

    @Override
    public void onEmptyDropdownSelected(Elements formData) {

    }
}
