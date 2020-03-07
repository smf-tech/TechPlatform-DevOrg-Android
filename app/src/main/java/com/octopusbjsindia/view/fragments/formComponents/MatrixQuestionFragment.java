package com.octopusbjsindia.view.fragments.formComponents;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.octopusbjsindia.view.activities.FormDisplayActivity;

import java.util.HashMap;

public class MatrixQuestionFragment extends Fragment implements MatrixQuestionFragmentAdapter.OnRequestItemClicked, View.OnClickListener {
    //views
    public RecyclerView rv_matrix_question;
    public MatrixQuestionFragmentAdapter matrixQuestionFragmentAdapter;
    HashMap<String, String> hashMap = new HashMap<>();
    JsonObject MatrixQuestionRequestJsonObject = new JsonObject();
    TextView text_title;
    View view;
    private Elements elements;
    private Button btn_loadnext, btn_loadprevious;

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
        btn_loadnext = view.findViewById(R.id.btn_loadnext);
        btn_loadprevious = view.findViewById(R.id.btn_loadprevious);
        rv_matrix_question = view.findViewById(R.id.rv_matrix_question);

        btn_loadnext.setOnClickListener(this);
        btn_loadprevious.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv_matrix_question.setLayoutManager(layoutManager);

        //check for arguments
        if (getActivity() != null && getArguments() != null) {
            if (getArguments().containsKey("Element")) {
                elements = (Elements) getArguments().getSerializable("Element");
                matrixQuestionFragmentAdapter = new MatrixQuestionFragmentAdapter(MatrixQuestionFragment.this, getActivity(), elements,
                        this);
                rv_matrix_question.setAdapter(matrixQuestionFragmentAdapter);
            }
        }
        // set quetion at top
        text_title.setText(elements.getName());

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

    public void receiveAnswerJson(String receivedJsonObjectString) {
        MatrixQuestionRequestJsonObject = new Gson().fromJson(receivedJsonObjectString, JsonObject.class);
        Log.d("ReceivedJsonObj->", new Gson().toJson(MatrixQuestionRequestJsonObject));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_loadnext:
                //set json object and go to next fragment
                hashMap.put(elements.getName(), new Gson().toJson(MatrixQuestionRequestJsonObject));

                ((FormDisplayActivity) getActivity()).goNext(hashMap);
                break;
            case R.id.btn_loadprevious:
                //Go to previous fragment
                ((FormDisplayActivity) getActivity()).goPrevious();
                break;
        }
    }
}