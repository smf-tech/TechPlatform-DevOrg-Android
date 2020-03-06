package com.octopusbjsindia.view.fragments.formComponents;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.forms.Elements;
import com.octopusbjsindia.utility.Util;

public class MatrixQuestionFragment extends Fragment implements MatrixQuestionFragmentAdapter.OnRequestItemClicked, View.OnClickListener {
    public RecyclerView rv_matrix_question;
    public MatrixQuestionFragmentAdapter matrixQuestionFragmentAdapter;
    TextView text_title;
    View view;
    private Elements elements;
    private JsonObject requestJsonObject = new JsonObject();
    JsonObject MatrixQuestionRequestJsonObject = new JsonObject();
    private Button btn_loadnext,btn_loadprevious;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_matrix_question, container, false);
        text_title = view.findViewById(R.id.text_title);
        btn_loadnext  = view.findViewById(R.id.btn_loadnext);
        btn_loadprevious = view.findViewById(R.id.btn_loadprevious);

        btn_loadnext.setOnClickListener(this);
        btn_loadprevious.setOnClickListener(this);

        rv_matrix_question = view.findViewById(R.id.rv_matrix_question);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv_matrix_question.setLayoutManager(layoutManager);

        if (getActivity() != null && getArguments() != null) {
            if (getArguments().containsKey("Element")) {
                elements = (Elements) getArguments().getSerializable("Element");
                //  text_one.setText(elements.getColumns().get(0).getName() + " " + elements.getRowsList().get(0));
            }
        }


        matrixQuestionFragmentAdapter = new MatrixQuestionFragmentAdapter(MatrixQuestionFragment.this,getActivity(), elements,
                this);
        rv_matrix_question.setAdapter(matrixQuestionFragmentAdapter);

        text_title.setText(elements.getName());

       /* for (int i = 0; i <elements.getRowsList().size(); i++) {
            JsonObject ColomJsonObject = new JsonObject();
            for (int j = 0; j <elements.getColumns().size(); j++) {
                ColomJsonObject.addProperty(elements.getColumns().get(j).getName(),false);
            }
            requestJsonObject.add(elements.getRowsList().get(i),ColomJsonObject);
        }

        Log.d("JsonObject->",new Gson().toJson(requestJsonObject));*/

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onItemClicked(int pos) {
        Log.d("onItemClickedfragment", "onItemClicked-->" + pos);
    }

    public void receiveAnswerJson(String receivedJsonObjectString){
        MatrixQuestionRequestJsonObject =new Gson().fromJson(receivedJsonObjectString,JsonObject.class);
        Log.d("ReceivedJsonObj->",new Gson().toJson(MatrixQuestionRequestJsonObject));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_loadnext:
                Log.d("ReceivedJsonObj->","send to activity here");
                Log.d("ReceivedJsonObj->",new Gson().toJson(MatrixQuestionRequestJsonObject));
                break;
            case R.id.btn_loadprevious:
                Util.showToast("Go to previous fragment if available",getActivity());
                break;
        }
    }
}