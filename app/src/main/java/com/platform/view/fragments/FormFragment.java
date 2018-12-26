package com.platform.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.platform.R;
import com.platform.listeners.FormFragmentListener;
import com.platform.models.forms.FormData;
import com.platform.utility.Constants;
import com.platform.view.customs.FormComponentCreator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class FormFragment extends Fragment implements FormFragmentListener {

    private View viewFormFragment;
    private LinearLayout customFormView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;

    private ArrayList<FormData> formDataArrayList;
    private FormComponentCreator formComponentCreator;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewFormFragment = inflater.inflate(R.layout.fragment_gen_form, container, false);
        return viewFormFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {
        progressBarLayout = viewFormFragment.findViewById (R.id.gen_frag_progress_bar);
        progressBar = viewFormFragment.findViewById(R.id.pb_gen_form_fragment);

        //TODO: Dummy data to test form generation
        formDataArrayList = new ArrayList<>();
        try {
            JSONObject dummyData = new JSONObject("{\"pages\":[{\"name\":\"page1\",\"elements\":[{\"type\":\"text\",\"name\":\"Label-1\",\"isRequired\":true,\"maxLength\":25},{\"type\":\"text\",\"name\":\"Label-2\",\"inputType\":\"password\",\"maxLength\":10}]}]}");
            JSONArray elements = dummyData.getJSONArray("pages").getJSONObject(0).getJSONArray("elements");
            for (int i = 0; i < elements.length(); i++) {
                JSONObject obj = elements.getJSONObject(i);
                FormData fd = new FormData();
                fd.setType(obj.getString("type"));
                fd.setName(obj.getString("name"));
                formDataArrayList.add(fd);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (formDataArrayList != null) {
            formComponentCreator = new FormComponentCreator(this);
            renderFormView();
        }
    }

    private void renderFormView() {
        customFormView = viewFormFragment.findViewById(R.id.ll_form_container);

        for (FormData formData : formDataArrayList) {
            if (formData != null && !formData.getType().equals("")) {

                String formDataType = formData.getType();
                switch (formDataType) {
                    case Constants.FormsFactory.TEXT_TEMPLATE:
                        Log.d(TAG, "TEXT_TEMPLATE");
                        addViewToMainContainer(formComponentCreator.textInputTemplate(formData));
                    break;
                }
            }
        }
    }

    private void addViewToMainContainer(final View view) {
        getActivity().runOnUiThread(() -> {
            if (view != null) {
                customFormView.addView(view);
            }
        });
    }

    @Override
    public void showProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility (View.VISIBLE);
                progressBarLayout.setVisibility (View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility (View.GONE);
                progressBarLayout.setVisibility (View.GONE);
            }
        });
    }
}
