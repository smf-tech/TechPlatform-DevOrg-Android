package com.octopusbjsindia.view.fragments.formComponents;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.forms.Elements;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.FormDisplayActivity;
import com.octopusbjsindia.view.adapters.formComponents.ChechBoxAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CheckboxFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    View view;
    private Elements element;
    public boolean isNone = false, isOther = false;
    public CheckBox cbNone, cbOther;
    public TextInputLayout tiOther;
    private RecyclerView rvCheckbox;
    private ChechBoxAdapter adapter;
    EditText etOther;

    public ArrayList<String> selectedList = new ArrayList<String>();
    HashMap<String, String> hashMap = new HashMap<String, String>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_checkbox, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        element = (Elements) getArguments().getSerializable("Element");

        TextView tvQuetion = view.findViewById(R.id.tv_question);
        cbNone = view.findViewById(R.id.cb_none);
        cbOther = view.findViewById(R.id.cb_other);
        rvCheckbox = view.findViewById(R.id.rv_checkbox);
        tiOther = view.findViewById(R.id.ti_other);
        etOther = view.findViewById(R.id.et_other);

        if (element.getHasNone() != null && element.getHasNone()) {
            cbNone.setVisibility(View.VISIBLE);
        } else {
            cbNone.setVisibility(View.GONE);
        }
        if (element.getHasOther() != null && element.getHasOther()) {
            cbOther.setVisibility(View.VISIBLE);
        } else {
            cbOther.setVisibility(View.GONE);
        }

        tvQuetion.setText(element.getTitle().getDefaultValue());

        // set data logic
        selectedList.clear();
        if(!TextUtils.isEmpty(((FormDisplayActivity)getActivity()).formAnswersMap.get(element.getName()))){
            JSONArray arr= null;
            try {
                arr = new JSONArray(((FormDisplayActivity)getActivity()).formAnswersMap.get(element.getName()));
                for(int i=0;i<arr.length();i++){
                    String item=arr.get(i).toString();
                    selectedList.add(item);
                    
                    if (item.equals("none")) {
                        isNone = true;
                        cbNone.setChecked(true);
                    } else if (item.equals("other")) {
                        isOther = true;
                        cbOther.setChecked(true);
                        etOther.setText(((FormDisplayActivity)getActivity()).formAnswersMap.get(element.getName() + "-Comment"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        adapter = new ChechBoxAdapter(this, element.getChoices());

        rvCheckbox.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCheckbox.setAdapter(adapter);

        cbOther.setOnCheckedChangeListener(this);
        cbNone.setOnCheckedChangeListener(this);
        view.findViewById(R.id.bt_previous).setOnClickListener(this);
        view.findViewById(R.id.bt_next).setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_none:
                if (isChecked) {
                    isNone = true;
                    cbOther.setChecked(false);
                    tiOther.setVisibility(View.GONE);
                    selectedList.clear();
                    adapter.notifyDataSetChanged();
                } else {
                    isNone = false;
                }
                break;
            case R.id.cb_other:
                if (isChecked) {
                    isOther = true;
//                    selectedList.clear();
//                    adapter.notifyDataSetChanged();
                    cbNone.setChecked(false);
                    view.findViewById(R.id.ti_other).setVisibility(View.VISIBLE);
                } else {
                    isOther = false;
                    view.findViewById(R.id.ti_other).setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_previous:
                ((FormDisplayActivity) getActivity()).goPrevious();
                break;
            case R.id.bt_next:
                if (element.getValidators() != null && element.getValidators().size() > 0) {
                    int min = 1, max = element.getChoices().size();
                    if (element.getValidators().get(0).getMinCount() != null) {
                        min = element.getValidators().get(0).getMinCount();
                    }
                    if (element.getValidators().get(0).getMaxCount() != null) {
                        max = element.getValidators().get(0).getMaxCount();
                    }

                    if (selectedList.size() <= max && selectedList.size() >= min) {
                        createResponse();
                    } else {
                        Util.showToast("Please select minimum " + min +
                                        "and maximum " + max + "options.",
                                this);
                        return;
                    }
                } else {
                    createResponse();
                }
                ((FormDisplayActivity) getActivity()).goNext(hashMap);
                break;
        }
    }

    public void createResponse() {
        if (isNone) {
            selectedList.add("none");
            hashMap.put(element.getName(), new Gson().toJson(selectedList));
        } else if (isOther) {

            selectedList.add("other");
            hashMap.put(element.getName(), new Gson().toJson(selectedList));
            hashMap.put(element.getName() + "-Comment", etOther.getText().toString());
        } else {
            hashMap.put(element.getName(), new Gson().toJson(selectedList));
        }
    }
}
