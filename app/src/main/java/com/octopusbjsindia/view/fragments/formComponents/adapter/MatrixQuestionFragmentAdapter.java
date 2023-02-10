package com.octopusbjsindia.view.fragments.formComponents.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.forms.Elements;
import com.octopusbjsindia.view.fragments.formComponents.MatrixQuestionFragment;

public class MatrixQuestionFragmentAdapter extends RecyclerView.Adapter
        <MatrixQuestionFragmentAdapter.EmployeeViewHolder>
        implements MatrixQuestionColoumnAdapter.OnRequestItemClicked {

    Context mContext;
    MatrixQuestionColoumnAdapter matrixQuestionFragmentAdapter;
    private final Elements dataList;
    private final JsonObject requestJsonObject = new JsonObject();
    private final JsonObject MatrixQuestionRequestJsonObject = new JsonObject();
    private final OnRequestItemClicked clickListener;
    private final MatrixQuestionFragment fragment;

    public MatrixQuestionFragmentAdapter(MatrixQuestionFragment fragment, Context context,
                                         Elements elements, final OnRequestItemClicked clickListener) {
        mContext = context;
        this.dataList = elements;
        this.clickListener = clickListener;
        this.fragment = fragment;
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_matrix_quetion_item, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {
        holder.row_title.setText(dataList.getRowsList().get(position).getText().getLocaleValue());

        if (dataList.getCellType() != null && dataList.getCellType().equalsIgnoreCase("Boolean")) {
            matrixQuestionFragmentAdapter = new MatrixQuestionColoumnAdapter(fragment, mContext, dataList.getColumns(),
                    this, dataList.getRowsList().get(position).getValue(), position, dataList.getCellType());
        } else if (dataList.getCellType() != null) {
            matrixQuestionFragmentAdapter = new MatrixQuestionColoumnAdapter(fragment, mContext, dataList.getColumns(),
                    this, dataList.getRowsList().get(position).getValue(), position, dataList.getCellType(),
                    dataList.getChoices());
        } else {
            matrixQuestionFragmentAdapter = new MatrixQuestionColoumnAdapter(fragment, mContext, dataList.getColumns(),
                    this, dataList.getRowsList().get(position).getValue(), position, "Dropdown",
                    dataList.getChoices());
        }
        holder.rv_matrix_question.setAdapter(matrixQuestionFragmentAdapter);
    }

    @Override
    public int getItemCount() {
        return dataList.getRowsList().size();
    }

  /*  @Override
    public void onItemClicked(int pos, List<Boolean> columnListAnswers) {
        //if (((FormDisplayActivity) mContext).isEditable) {
            JsonObject ColomJsonObject = new JsonObject();
            for (int j = 0; j < dataList.getColumns().size(); j++) {
                if (columnListAnswers.get(j)) {
                    ColomJsonObject.addProperty(dataList.getColumns().get(j).getName(), "Yes");
                } else {
                    ColomJsonObject.addProperty(dataList.getColumns().get(j).getName(), "No");
                }
            }
            requestJsonObject.add(this.dataList.getRowsList().get(pos).getValue(), ColomJsonObject);
            MatrixQuestionRequestJsonObject.add(this.dataList.getName(), requestJsonObject);
            //this.fragment.receiveAnswerJson(new Gson().toJson(MatrixQuestionRequestJsonObject));
            this.fragment.receiveAnswerJson(MatrixQuestionRequestJsonObject);
        //}
    }*/

    @Override
    public void onItemClicked(int pos, JsonObject columnListAnswers) {
        //if (((FormDisplayActivity) mContext).isEditable) {

        requestJsonObject.add(this.dataList.getRowsList().get(pos).getValue(), columnListAnswers);
        MatrixQuestionRequestJsonObject.add(this.dataList.getName(), requestJsonObject);
        //this.fragment.receiveAnswerJson(new Gson().toJson(MatrixQuestionRequestJsonObject));
        this.fragment.receiveAnswerJson(MatrixQuestionRequestJsonObject);
        //}
    }


    @Override
    public void onDropdownOptionsSelected(int rowPosition, JsonObject jsonObject) {
        if (jsonObject.size() > 0) {
            requestJsonObject.add(this.dataList.getRowsList().get(rowPosition).getValue(), jsonObject);
            MatrixQuestionRequestJsonObject.add(this.dataList.getName(), requestJsonObject);
            this.fragment.receiveAnswerJson(MatrixQuestionRequestJsonObject);
        } else {
            if (requestJsonObject.has(this.dataList.getRowsList().get(rowPosition).getValue())) {
                requestJsonObject.remove(this.dataList.getRowsList().get(rowPosition).getValue());
                if (requestJsonObject.size() > 0) {
                    MatrixQuestionRequestJsonObject.add(this.dataList.getName(), requestJsonObject);
                } else {
                    if (MatrixQuestionRequestJsonObject.has(this.dataList.getName())) {
                        MatrixQuestionRequestJsonObject.remove(this.dataList.getName());
                    }
                }
                this.fragment.receiveAnswerJson(MatrixQuestionRequestJsonObject);
            }
        }
    }

    public interface OnRequestItemClicked {
        void onItemClicked(int pos);
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder {

        RecyclerView rv_matrix_question;
        TextView row_title;

        EmployeeViewHolder(View itemView) {
            super(itemView);
            row_title = itemView.findViewById(R.id.row_title);
            rv_matrix_question = itemView.findViewById(R.id.rv_matrix_question);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
            rv_matrix_question.setLayoutManager(layoutManager);
            itemView.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));

        }
    }
}
