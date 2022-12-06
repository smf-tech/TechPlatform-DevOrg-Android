package com.octopusbjsindia.view.fragments.formComponents;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.forms.Column;
import com.octopusbjsindia.models.forms.Elements;
import com.octopusbjsindia.utility.PreferenceHelper;
import com.octopusbjsindia.view.activities.FormDisplayActivity;

import java.util.ArrayList;
import java.util.List;

public class MatrixQuestionColoumnAdapter extends RecyclerView.Adapter
        <MatrixQuestionColoumnAdapter.EmployeeViewHolder> {

    Context mContext;
    private Elements dataList;
    private List<Column> columnList;
    private List<Boolean> columnListAnswers = new ArrayList<>();
    private OnRequestItemClicked clickListener;
    private PreferenceHelper preferenceHelper;
    private String RowName;
    private int rowPosition;
    private Fragment mfragment;

    public MatrixQuestionColoumnAdapter(MatrixQuestionFragment fragment, Context context, List<Column> columnList,
                                        final OnRequestItemClicked clickListener, String s, int position) {
        mfragment = fragment;
        mContext = context;
        RowName = s;
        rowPosition = position;
        this.columnList = columnList;
        this.clickListener = clickListener;
        preferenceHelper = new PreferenceHelper(Platform.getInstance());

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

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_matrix_coloumn_item, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {
        holder.column_name.setText(columnList.get(position).getTitle().getLocaleValue());

        if (columnListAnswers.get(position).booleanValue()) {
            holder.toggleGroup2.check(R.id.btn_yes);
        } else {
            holder.toggleGroup2.check(R.id.btn_no);
        }

        holder.toggleGroup2.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                //     Log.d("isChecked", "isChecked-->" + RowName + " " + isChecked + "  checkedId " + checkedId);
            }
        });
        holder.btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.toggleGroup2.check(R.id.btn_no);
                Log.d("isChecked", "isChecked-->" + RowName + "-" + "checkedId " + "btn_no" + position);
                columnListAnswers.set(position, false);
                clickListener.onItemClicked(rowPosition, columnListAnswers);
            }
        });
        holder.btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.toggleGroup2.check(R.id.btn_yes);
                Log.d("isChecked", "isChecked-->" + RowName + "-" + "btn_yes" + "checkedId" + "btn_yes" + position);
                columnListAnswers.set(position, true);
                clickListener.onItemClicked(rowPosition, columnListAnswers);
            }
        });

        if (!((FormDisplayActivity) mContext).isEditable) {
            holder.toggleGroup2.setEnabled(false);
            holder.btn_yes.setEnabled(false);
            holder.btn_no.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return columnList.size();
    }

    public interface OnRequestItemClicked {
        void onItemClicked(int rowPosition, List<Boolean> columnListAnswers);
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView column_name;
        MaterialButtonToggleGroup toggleGroup2;
        Button btn_yes, btn_no;
        EmployeeViewHolder(View itemView) {
            super(itemView);
            column_name = itemView.findViewById(R.id.column_name);
            toggleGroup2 = itemView.findViewById(R.id.toggleGroup2);

            btn_yes = itemView.findViewById(R.id.btn_yes);
            btn_no = itemView.findViewById(R.id.btn_no);

            itemView.setOnClickListener(v -> {
                clickListener.onItemClicked(getAdapterPosition(), columnListAnswers);
            });

        }
    }
}
