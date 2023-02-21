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

import java.util.ArrayList;
import java.util.HashMap;

public class CheckboxFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private View view;
    private Elements element;
    public boolean isNone = false, isOther = false;
    public CheckBox cbNone, cbOther;
    private TextInputLayout tiOther;
    private RecyclerView rvCheckbox;
    private ChechBoxAdapter adapter;
    private EditText etOther;
    private boolean isFirstpage = false;
    public ArrayList<String> selectedList = new ArrayList<String>();
    private HashMap<String, String> hashMap = new HashMap<String, String>();

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
        isFirstpage = getArguments().getBoolean("isFirstpage");
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
        if (!((FormDisplayActivity) getActivity()).isEditable) {
            cbOther.setEnabled(false);
            cbNone.setEnabled(false);
            etOther.setFocusable(false);
            etOther.setEnabled(false);
        }
        tvQuetion.setText(element.getTitle().getLocaleValue().trim());

        // set data logic
        selectedList.clear();
        if (!TextUtils.isEmpty(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName()))) {
            JSONArray arr = null;
            try {
                arr = new JSONArray(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName()));
                for (int i = 0; i < arr.length(); i++) {
                    String item = arr.get(i).toString();
                    selectedList.add(item);

                    if (item.equals("none")) {
                        isNone = true;
                        cbNone.setChecked(true);
                    } else if (item.equals("other")) {
                        isOther = true;
                        cbOther.setChecked(true);
                        view.findViewById(R.id.ti_other).setVisibility(View.VISIBLE);
                        etOther.setText(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName() + "-Comment"));
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

        if (isFirstpage) {
            view.findViewById(R.id.bt_previous).setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_none:
                if (isChecked) {
                    selectedList.clear();
                    if (!selectedList.contains("none")) {
                        selectedList.add("none");
                    }
                    isNone = true;
                    cbOther.setChecked(false);
                    tiOther.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                } else {
                    if (selectedList.contains("none")) {
                        selectedList.remove("none");
                    }
                    isNone = false;
                }
                break;
            case R.id.cb_other:
                if (isChecked) {
                    if (!selectedList.contains("other")) {
                        selectedList.add("other");
                    }
                    isOther = true;
                    cbNone.setChecked(false);
                    view.findViewById(R.id.ti_other).setVisibility(View.VISIBLE);
                } else {
                    if (selectedList.contains("other")) {
                        selectedList.remove("other");
                    }
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
                        if (selectedList.size() > 0) {
                            if (element.getRequiredErrorText() != null) {
                                Util.showToast(element.getRequiredErrorText().getLocaleValue(), this);
                            } else {
                                Util.showToast(getResources().getString(R.string.required_error), this);
                            }
                            return;
                        } else {
                            if (element.isRequired()) {
                                if (element.getRequiredErrorText() != null) {
                                    Util.showToast(element.getRequiredErrorText().getLocaleValue(), this);
                                } else {
                                    Util.showToast(getResources().getString(R.string.required_error), this);
                                }
                                return;
                            } else {
                                ((FormDisplayActivity) getActivity()).goNext(hashMap);
                            }
                        }
                    }
                } else {
                    if (selectedList.size() == 0) {
                        if (element.isRequired()) {
                            if (element.getRequiredErrorText() != null) {
                                Util.showToast(element.getRequiredErrorText().getLocaleValue(), this);
                            } else {
                                Util.showToast(getResources().getString(R.string.required_error), this);
                            }
                            return;
                        } else {
                            ((FormDisplayActivity) getActivity()).goNext(hashMap);
                        }
                    } else {
                        createResponse();
                    }
                }
//                if(element.isRequired()){
//                    if(hashMap.isEmpty()){
//                        if(element.getRequiredErrorText()!=null){
//                            Util.showToast(element.getRequiredErrorText().getLocaleValue(), this);
//                        } else {
//                            Util.showToast(getResources().getString(R.string.required_error), this);
//                        }
//                    } else {
//                        ((FormDisplayActivity) getActivity()).goNext(hashMap);
//                    }
//                } else {
//                    ((FormDisplayActivity) getActivity()).goNext(hashMap);
//                }
                break;
        }
    }

    public void createResponse() {
        if (isNone) {
            hashMap.put(element.getName(), new Gson().toJson(selectedList));
        } else if (isOther) {
            if (etOther.getText().toString().length() > 0) {
                hashMap.put(element.getName(), new Gson().toJson(selectedList));
                hashMap.put(element.getName() + "-Comment", etOther.getText().toString());
            } else {

                if (element.getOtherErrorText() != null) {
                    Util.showToast(element.getOtherErrorText().getLocaleValue(), this);
                } else {
                    Util.showToast("Please enter other text.", this);
                }
                return;
            }
        } else {
            hashMap.put(element.getName(), new Gson().toJson(selectedList));
        }
        ((FormDisplayActivity) getActivity()).goNext(hashMap);
    }
}
