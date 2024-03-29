package com.octopusbjsindia.view.fragments.formComponents;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.gson.reflect.TypeToken;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.forms.Elements;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.FormDisplayActivity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class MatrixQuestionFragment extends Fragment implements
        MatrixQuestionFragmentAdapter.OnRequestItemClicked, View.OnClickListener {

    public HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> tempHashMap = new HashMap<>();
    public HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> tempDropdownHashMap = new HashMap<>();
    public HashMap<String, HashMap<String, HashMap<String, String>>> rowMap;
    public HashMap<String, HashMap<String, ArrayList<String>>> rowMapDropdown;
    boolean isFirstpage = false;
    //views
    private RecyclerView rv_matrix_question;
    private MatrixQuestionFragmentAdapter matrixQuestionFragmentAdapter;
    private HashMap<String, String> hashMap = new HashMap<>();
    private JsonObject matrixQuestionRequestJsonObject = new JsonObject();
    private TextView text_title;
    private View view;
    private Elements elements;

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
        Button btn_loadnext = view.findViewById(R.id.btn_loadnext);
        Button btn_loadprevious = view.findViewById(R.id.btn_loadprevious);
        rv_matrix_question = view.findViewById(R.id.rv_matrix_question);

        btn_loadnext.setOnClickListener(this);
        btn_loadprevious.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv_matrix_question.setLayoutManager(layoutManager);

        //check for arguments
        if (getActivity() != null && getArguments() != null) {
            if (getArguments().containsKey("Element")) {
                elements = (Elements) getArguments().getSerializable("Element");
            }
            isFirstpage = getArguments().getBoolean("isFirstpage");
            if (isFirstpage) {
                view.findViewById(R.id.btn_loadprevious).setVisibility(View.INVISIBLE);
            }
            matrixQuestionFragmentAdapter = new MatrixQuestionFragmentAdapter(
                    MatrixQuestionFragment.this, getActivity(), elements, this);
            rv_matrix_question.setAdapter(matrixQuestionFragmentAdapter);
        }

        if (!TextUtils.isEmpty(((FormDisplayActivity) getActivity()).formAnswersMap.get(elements.getName()))) {
            String str = ((FormDisplayActivity) getActivity()).formAnswersMap.get(elements.getName());
            try {
                if (str != null) {
                    jsonToMap(str);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        text_title.setText(elements.getTitle().getLocaleValue());
        return view;
    }

    public void jsonToMap(String str) throws JSONException {
        Gson g = new Gson();
        if (elements.getCellType().equalsIgnoreCase("Boolean")) { // for boolean matrix question
            tempHashMap.clear();
            tempHashMap.putAll(g.fromJson(str,
                    new TypeToken<HashMap<String, HashMap<String, HashMap<String, String>>>>() {
                    }.getType()));

            rowMap = new HashMap<>();
            rowMap.clear();
            rowMap.putAll(tempHashMap.get(elements.getName()));
        } else { // for radioGroup and checkbox matrix question
            tempDropdownHashMap.clear();
            tempDropdownHashMap.putAll(g.fromJson(str,
                    new TypeToken<HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>>>() {
                    }.getType()));
            rowMapDropdown = new HashMap<>();
            rowMapDropdown.clear();
            rowMapDropdown.putAll(tempDropdownHashMap.get(elements.getName()));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onItemClicked(int pos) {
        Log.d("onItemClickedfragment", "onItemClicked-->" + pos);
    }

    public void receiveAnswerJson(JsonObject receivedJsonObjectString) {
        //matrixQuestionRequestJsonObject = new Gson().fromJson(receivedJsonObjectString, JsonObject.class);
        matrixQuestionRequestJsonObject = receivedJsonObjectString;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_loadnext:
                //set json object and go to next fragment
                        if(matrixQuestionRequestJsonObject.size()>0) {
                            hashMap.put(elements.getName(), new Gson().toJson(matrixQuestionRequestJsonObject));
                            ((FormDisplayActivity) requireActivity()).goNext(hashMap);
                        } else {
                            if(elements.isRequired()) {
                                if (elements.getRequiredErrorText() != null) {
                                    Util.showToast(elements.getRequiredErrorText().getLocaleValue(), this);
                                } else {
                                    Util.showToast(getResources().getString(R.string.required_error), this);
                                }
                            } else {
                                ((FormDisplayActivity) requireActivity()).goNext(hashMap);
                            }
                        }
                break;
            case R.id.btn_loadprevious:
                //Go to previous fragment
//                hashMap.put(elements.getName(), new Gson().toJson(MatrixQuestionRequestJsonObject));
//                ((FormDisplayActivity) getActivity()).formAnswersMap.putAll(hashMap);
                ((FormDisplayActivity) requireActivity()).goPrevious();
                break;
        }
    }
}