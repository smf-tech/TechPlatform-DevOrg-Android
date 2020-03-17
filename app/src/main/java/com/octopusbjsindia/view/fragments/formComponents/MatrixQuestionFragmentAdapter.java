package com.octopusbjsindia.view.fragments.formComponents;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.forms.Column;
import com.octopusbjsindia.models.forms.Elements;
import com.octopusbjsindia.utility.PreferenceHelper;

import java.util.List;

public class MatrixQuestionFragmentAdapter extends RecyclerView.Adapter<MatrixQuestionFragmentAdapter.EmployeeViewHolder> implements MatrixQuestionColoumnAdapter.OnRequestItemClicked {

    Context mContext;
    MatrixQuestionColoumnAdapter matrixQuestionFragmentAdapter;
    private Elements dataList;
    private JsonObject requestJsonObject = new JsonObject();
    private JsonObject MatrixQuestionRequestJsonObject = new JsonObject();
    private List<Column> columnList;
    private OnRequestItemClicked clickListener;
    private PreferenceHelper preferenceHelper;
    private MatrixQuestionFragment fragment;

    public MatrixQuestionFragmentAdapter(MatrixQuestionFragment fragment, Context context, Elements elements, final OnRequestItemClicked clickListener) {
        mContext = context;
        this.dataList = elements;
        this.clickListener = clickListener;
        this.fragment = fragment;
        preferenceHelper = new PreferenceHelper(Platform.getInstance());

        for (int i = 0; i < elements.getRowsList().size(); i++) {
            JsonObject ColomJsonObject = new JsonObject();
            for (int j = 0; j < elements.getColumns().size(); j++) {
                ColomJsonObject.addProperty(elements.getColumns().get(j).getName(), true);
            }
            requestJsonObject.add(elements.getRowsList().get(i).getValue(), ColomJsonObject);
        }
        Log.d("JsonObject->", new Gson().toJson(requestJsonObject));
        MatrixQuestionRequestJsonObject.add(this.dataList.getName(), requestJsonObject);
        Log.d("finalJsonObj->", new Gson().toJson(MatrixQuestionRequestJsonObject));
        this.fragment.receiveAnswerJson(new Gson().toJson(MatrixQuestionRequestJsonObject));
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_matrix_quetion_item, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {
        holder.row_title.setText(dataList.getRowsList().get(position).getText().getDefaultValue());

        matrixQuestionFragmentAdapter = new MatrixQuestionColoumnAdapter(fragment, mContext, dataList.getColumns(),
                this, dataList.getRowsList().get(position).getValue(), position);
        holder.rv_matrix_question.setAdapter(matrixQuestionFragmentAdapter);

    }

    @Override
    public int getItemCount() {
        return dataList.getRowsList().size();
    }

    @Override
    public void onItemClicked(int pos, List<Boolean> columnListAnswers) {
        Log.d("onItemClicked", "onItemClicked-->" + pos);
        JsonObject ColomJsonObject = new JsonObject();
        for (int j = 0; j < dataList.getColumns().size(); j++) {
            ColomJsonObject.addProperty(dataList.getColumns().get(j).getName(), columnListAnswers.get(j));
        }
        Log.d("JsonObjectAdapter->", new Gson().toJson(columnListAnswers));
        //clickListener.onItemClicked(pos);

        requestJsonObject.add(this.dataList.getRowsList().get(pos).getValue(), ColomJsonObject);
        Log.d("JsonObjectfinal->", new Gson().toJson(requestJsonObject));
        MatrixQuestionRequestJsonObject.add(this.dataList.getName(), requestJsonObject);
        Log.d("finalJsonObj->", new Gson().toJson(MatrixQuestionRequestJsonObject));
        this.fragment.receiveAnswerJson(new Gson().toJson(MatrixQuestionRequestJsonObject));
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
