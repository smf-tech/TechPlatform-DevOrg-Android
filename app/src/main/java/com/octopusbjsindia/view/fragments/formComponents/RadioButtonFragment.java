package com.octopusbjsindia.view.fragments.formComponents;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.form_component.RadioButtonData;
import com.octopusbjsindia.models.forms.Choice;
import com.octopusbjsindia.models.forms.Elements;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.FormDisplayActivity;
import com.octopusbjsindia.view.adapters.formComponents.RadioButtonAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class RadioButtonFragment extends Fragment implements View.OnClickListener {
    private boolean isFirstpage = false;
    private View view;
    private Elements element;
    private RadioButtonAdapter adapter;
    private RecyclerView rvRadiobutton;
    private ArrayList<RadioButtonData> list = new ArrayList<RadioButtonData>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_radio_button, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        element = (Elements) getArguments().getSerializable("Element");
        TextView tvQuetion = view.findViewById(R.id.tv_question);
        isFirstpage = getArguments().getBoolean("isFirstpage");
        rvRadiobutton = view.findViewById(R.id.rv_radiobutton);
        tvQuetion.setText(element.getTitle().getLocaleValue());
        list.clear();
        for (Choice obj : element.getChoices()) {
            RadioButtonData temp = new RadioButtonData(obj.getValue(), obj.getText().getLocaleValue(), false);
            list.add(temp);
        }

        if(!TextUtils.isEmpty(((FormDisplayActivity)getActivity()).formAnswersMap.get(element.getName()))){
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getValue().equals(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName()))) {
                    list.get(i).setSelected(true);
                }
            }
        }

        adapter = new RadioButtonAdapter(this, list);
        rvRadiobutton.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvRadiobutton.setAdapter(adapter);
        view.findViewById(R.id.bt_previous).setOnClickListener(this);
        view.findViewById(R.id.bt_next).setOnClickListener(this);

        if (isFirstpage) {
            view.findViewById(R.id.bt_previous).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_previous:
                ((FormDisplayActivity) getActivity()).goPrevious();
                break;
            case R.id.bt_next:
                String selected = null;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isSelected()) {
                        selected = list.get(i).getValue();
                    }
                }

                HashMap<String, String> hashMap = new HashMap<String, String>();
                if (TextUtils.isEmpty(selected)) {
                    if (element.isRequired()) {
                        if (element.getRequiredErrorText() != null) {
                            Util.showToast(element.getRequiredErrorText().getLocaleValue(), this);
                        } else {
                            Util.showToast(getResources().getString(R.string.required_error), this);
                        }
                        return;
                    }
                } else {
                    hashMap.put(element.getName(), selected);
                }
                ((FormDisplayActivity) getActivity()).goNext(hashMap);
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
}
