package com.octopusbjsindia.view.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.VolleyError;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.forms.Components;
import com.octopusbjsindia.models.forms.Elements;
import com.octopusbjsindia.models.forms.Form;
import com.octopusbjsindia.presenter.FormDisplayActivityPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.view.adapters.ViewPagerAdapter;
import com.octopusbjsindia.view.fragments.formComponents.CheckboxFragment;
import com.octopusbjsindia.view.fragments.formComponents.RadioButtonFragment;

import java.util.HashMap;
import java.util.List;

public class FormDisplayActivity extends BaseActivity implements APIDataListener {

    private Form formModel;
    private ViewPager vpFormElements;
    private ViewPagerAdapter adapter;
    private List<Elements> formDataArrayList;
    private FormDisplayActivityPresenter presenter;
    HashMap<String, String> formAnswersMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_display);
        //Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            String processId = getIntent().getExtras().getString(Constants.PM.PROCESS_ID);

            String formId = getIntent().getExtras().getString(Constants.PM.FORM_ID);
            //FormData formData = DatabaseManager.getDBInstance(this).getFormSchema(formId);

            boolean isPartialForm = getIntent().getExtras().getBoolean(Constants.PM.PARTIAL_FORM);
            boolean readOnly = getIntent().getExtras().getBoolean(Constants.PM.EDIT_MODE);

//            if (formData == null) {
////                if (Util.isConnected(getContext())) {
////                    formPresenter.getProcessDetails(formId);
////                } else {
////                    view.findViewById(R.id.no_offline_form).setVisibility(View.VISIBLE);
////                    setActionbar("");
////                }
//            } else {
//                formModel = new Form();
//                formModel.setData(formData);
//            }
        }
        initView();
    }

    private void initView() {
        presenter = new FormDisplayActivityPresenter(this);
        vpFormElements = findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        vpFormElements.setAdapter(adapter);
        presenter.getFormSchema();
        //Components components = formModel.getData().getComponents();
//        if (components == null) {
//            return;
//        }
//        formDataArrayList = components.getPages().get(0).getElements();
//        if (formDataArrayList != null) {
//            setupFormElements(formDataArrayList, formModel.getData().getId());
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setupFormElements(final List<Elements> formDataArrayList) {
        for (Elements element : formDataArrayList) {
            if (element != null && !element.getType().equals("")) {
                String formDataType = element.getType();
                Bundle bundle = new Bundle();
                switch (formDataType) {
                    case Constants.FormsFactory.RADIO_GROUP_TEMPLATE:
                        Fragment radioButtonFragment = new RadioButtonFragment();
                        bundle.putSerializable("", element);
                        radioButtonFragment.setArguments(bundle);
                        adapter.addFragment(radioButtonFragment, "Question 1");
                        break;

                    case Constants.FormsFactory.CHECKBOX_TEMPLATE:
                        Fragment checkboxFragment = new CheckboxFragment();
                        bundle.putSerializable("Element", element);
                        checkboxFragment.setArguments(bundle);
                        adapter.addFragment(checkboxFragment, "Question 2");
                        break;

                    case Constants.FormsFactory.MATRIX_DYNAMIC:

                        break;
                }


            }
        }
        adapter.notifyDataSetChanged();
    }

    public void parseFormSchema(Components components) {
        if (components == null) {
            return;
        }
        formDataArrayList = components.getPages().get(0).getElements();
        if (formDataArrayList != null) {
            //setupFormElements(formDataArrayList, formModel.getData().getId());
            setupFormElements(formDataArrayList);
        }
    }

    @Override
    public void onFailureListener(String requestID, String message) {

    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void closeCurrentActivity() {

    }

}
