package com.octopusbjsindia.view.fragments.formComponents;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.forms.Elements;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.FormDisplayActivity;
import com.octopusbjsindia.view.adapters.formComponents.ChechBoxAdapter;

import java.util.HashMap;

public class TextFragment extends Fragment implements View.OnClickListener {

    View view;
    private Elements element;
    private TextView tvQuetion;
    private EditText etAnswer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_text, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        element = (Elements) getArguments().getSerializable("Element");

        tvQuetion = view.findViewById(R.id.tv_question);

        if (TextUtils.isEmpty(element.getInputType())) {
            etAnswer = view.findViewById(R.id.et_answer);
        } else if (element.getInputType().equals("date")) {
            view.findViewById(R.id.ti_answer).setVisibility(View.GONE);
            view.findViewById(R.id.ti_answer_date).setVisibility(View.VISIBLE);
            view.findViewById(R.id.et_answer_date).setOnClickListener(this);
            etAnswer = view.findViewById(R.id.et_answer_date);
        } else if (element.getInputType().equals("number")) {
            etAnswer = view.findViewById(R.id.et_answer);
            etAnswer.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else {
            etAnswer = view.findViewById(R.id.et_answer);
        }

        tvQuetion.setText(element.getTitle().getDe());

        view.findViewById(R.id.bt_previous).setOnClickListener(this);
        view.findViewById(R.id.bt_next).setOnClickListener(this);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_answer_date:
                Util.showDateDialog(getContext(), etAnswer);
                break;
            case R.id.bt_previous:
                ((FormDisplayActivity) getActivity()).goPrevious();
                break;
            case R.id.bt_next:

                HashMap<String, String> hashMap = new HashMap<String, String>();
                if (TextUtils.isEmpty(etAnswer.getText().toString())) {
                    Util.showToast("Please enter some value", this);
                    return;
                } else {
                    hashMap.put(element.getName(), etAnswer.getText().toString());
                }

                ((FormDisplayActivity) getActivity()).goNext(hashMap);
                break;
        }
    }
}
