package com.octopusbjsindia.view.fragments.formComponents;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.forms.Choice;
import com.octopusbjsindia.models.forms.Column;
import com.octopusbjsindia.view.activities.FormDisplayActivity;
import com.sagar.selectiverecycleviewinbottonsheetdialog.CustomBottomSheetDialogFragment;
import com.sagar.selectiverecycleviewinbottonsheetdialog.interfaces.CustomBottomSheetDialogInterface;
import com.sagar.selectiverecycleviewinbottonsheetdialog.model.SelectionListObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MatrixQuestionColoumnAdapter extends
        RecyclerView.Adapter<MatrixQuestionColoumnAdapter.ColumnViewHolder> {

    private Context mContext;
    private final MatrixQuestionFragment fragment;
    private List<Column> columnList;
    private List<Boolean> columnListAnswers = new ArrayList<>();
    private OnRequestItemClicked clickListener;
    private String RowName;
    private int rowPosition;
    private String cellType;
    private ArrayList<SelectionListObject> dropdownChoicesList = new ArrayList<>();
    private Boolean isMutliselectAllowed;
    private String selectedChoices;
    private ArrayList<String> selectedChoicesList = new ArrayList<>();
    private JsonObject selectedDropdownChoicesJsonObject = new JsonObject();

    public MatrixQuestionColoumnAdapter(MatrixQuestionFragment fragment, Context context,
                                        List<Column> columnList, final OnRequestItemClicked clickListener,
                                        String s, int position, String cellType) {
        mContext = context;
        RowName = s;
        rowPosition = position;
        this.columnList = columnList;
        this.clickListener = clickListener;
        this.cellType = cellType;
        this.fragment = fragment;

        //add prefilled data
        if (fragment.rowMap != null) {
            for (int i = 0; i < columnList.size(); i++) {
                String str = String.valueOf(fragment.rowMap.get(RowName).get(columnList.get(i).getName()));
                if (str.equalsIgnoreCase("true") || str.equalsIgnoreCase("yes")) {
                    columnListAnswers.add(true);
                } else if (str.equalsIgnoreCase("false") || str.equalsIgnoreCase("no")) {
                    columnListAnswers.add(false);
                }
            }
        } else {
            for (int i = 0; i < columnList.size(); i++) {
                columnListAnswers.add(true);
            }
        }
    }

    public MatrixQuestionColoumnAdapter(MatrixQuestionFragment fragment, Context context,
                                        List<Column> columnList, final OnRequestItemClicked clickListener,
                                        String s, int position, String cellType, List<Choice> choicesList) {
        mContext = context;
        RowName = s;
        rowPosition = position;
        this.columnList = columnList;
        this.clickListener = clickListener;
        this.cellType = cellType;
        this.fragment = fragment;

        if (fragment.rowMapDropdown != null) {
            HashMap<String, ArrayList<String>> hashmapAnswers = new HashMap<>();
            for (int i = 0; i < columnList.size(); i++) {
                if (fragment.rowMapDropdown.get(RowName) != null) {
                    if (fragment.rowMapDropdown.get(RowName).get(columnList.get(i).getName()) != null) {
                        hashmapAnswers.put(columnList.get(i).getName(), fragment.rowMapDropdown.get(RowName).
                                get(columnList.get(i).getName()));
                        selectedChoicesList.add(TextUtils.join(",", fragment.rowMapDropdown.
                                get(RowName).get(columnList.get(i).getName())));
                    } else {
                        selectedChoicesList.add("");
                    }
                } else {
                    selectedChoicesList.add("");
                }
            }
        }
        dropdownChoicesList.clear();
        for (Choice choice : choicesList) {
            dropdownChoicesList.add(new SelectionListObject(
                    String.valueOf(choice.getValue()), choice.getText().getDefaultValue(),
                    false, false));
        }
        if (cellType.equalsIgnoreCase("RadioGroup")) isMutliselectAllowed = false;
        else isMutliselectAllowed = true;
    }

    @Override
    public ColumnViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_matrix_coloumn_item, parent, false);
        return new ColumnViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ColumnViewHolder holder, int position) {
        holder.column_name.setText(columnList.get(position).getTitle().getLocaleValue());
        if (cellType.equalsIgnoreCase("Boolean")) {
            if (columnListAnswers.get(position).booleanValue()) {
                holder.toggleGroup.check(R.id.btn_yes);
            } else {
                holder.toggleGroup.check(R.id.btn_no);
            }

            holder.btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.toggleGroup.check(R.id.btn_no);
                    columnListAnswers.set(position, false);
                    clickListener.onItemClicked(rowPosition, columnListAnswers);
                }
            });

            holder.btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.toggleGroup.check(R.id.btn_yes);
                    columnListAnswers.set(position, true);
                    clickListener.onItemClicked(rowPosition, columnListAnswers);
                }
            });

            if (!((FormDisplayActivity) mContext).isEditable) {
                holder.toggleGroup.setEnabled(false);
                holder.btn_yes.setEnabled(false);
                holder.btn_no.setEnabled(false);
            }
        } else {
            if (selectedChoicesList.size()>0) {
                holder.etDropdown.setText(selectedChoicesList.get(position));
                if (!((FormDisplayActivity) mContext).isEditable) {
                    holder.textDropdown.setEnabled(false);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return columnList.size();
    }

    public interface OnRequestItemClicked {
        void onItemClicked(int rowPosition, List<Boolean> columnListAnswers);

        void onDropdownOptionsSelected(int rowPosition, JsonObject jsonObject);
    }

    class ColumnViewHolder extends RecyclerView.ViewHolder implements CustomBottomSheetDialogInterface {
        TextView column_name;
        MaterialButtonToggleGroup toggleGroup;
        Button btn_yes, btn_no;
        TextInputLayout textDropdown;
        TextInputEditText etDropdown;

        ColumnViewHolder(View itemView) {
            super(itemView);

            column_name = itemView.findViewById(R.id.column_name);
            toggleGroup = itemView.findViewById(R.id.toggle_group);
            btn_yes = itemView.findViewById(R.id.btn_yes);
            btn_no = itemView.findViewById(R.id.btn_no);
            textDropdown = itemView.findViewById(R.id.text_dropdown);
            etDropdown = itemView.findViewById(R.id.et_dropdown);

            if (cellType.equalsIgnoreCase("Boolean")) {
                toggleGroup.setVisibility(View.VISIBLE);
                textDropdown.setVisibility(View.GONE);
            } else {
                toggleGroup.setVisibility(View.GONE);
                textDropdown.setVisibility(View.VISIBLE);

                etDropdown.setOnClickListener(v -> {
                    CustomBottomSheetDialogFragment customBottomSheetDialogFragment =
                            new CustomBottomSheetDialogFragment(this, "Select choice", dropdownChoicesList,
                                    isMutliselectAllowed);
                    customBottomSheetDialogFragment.show(fragment.getParentFragmentManager(), CustomBottomSheetDialogFragment.TAG);
                });
            }
        }

        @Override
        public void onCustomBottomSheetSelection(@NonNull String s) {
            switch (s) {
                case "Select choice":
                    selectedChoices = "";
                    JsonArray array = new JsonArray();
                    for (SelectionListObject choice : dropdownChoicesList) {
                        if (choice.isSelected()) {
                            array.add(choice.getValue());
                            if (selectedChoices != "") {
                                selectedChoices = selectedChoices + "," + choice.getValue();
                            } else {
                                selectedChoices = choice.getValue();
                            }
                        }
                    }
                    selectedDropdownChoicesJsonObject.add(columnList.get
                            (getAdapterPosition()).getName(), array);
                    etDropdown.setText(selectedChoices);
                    clickListener.onDropdownOptionsSelected(rowPosition, selectedDropdownChoicesJsonObject);
                    break;
            }
        }
    }
}