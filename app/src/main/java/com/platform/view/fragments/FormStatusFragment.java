package com.platform.view.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.platform.R;
import com.platform.listeners.FormStatusCallListener;
import com.platform.models.forms.Form;
import com.platform.presenter.FormStatusFragmentPresenter;
import com.platform.view.adapters.FormCategoryAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.platform.utility.Constants.Form.FORM_STATUS_COMPLETED;
import static com.platform.utility.Constants.Form.FORM_STATUS_PENDING;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FormStatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FormStatusFragment extends Fragment implements FormStatusCallListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String TAG = FormStatusFragment.class.getSimpleName();

    private String mFormStatus;
    private TextView mNoRecordsView;
    private RecyclerView mRecyclerView;

    public FormStatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment FormStatusFragment.
     */
    public static FormStatusFragment newInstance(String param1) {
        FormStatusFragment fragment = new FormStatusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFormStatus = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.forms_list);
        mNoRecordsView = view.findViewById(R.id.no_records_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (mFormStatus.equals(FORM_STATUS_PENDING)) {
            getPendingFormsFromDB();
            return;
        }

        FormStatusFragmentPresenter presenter = new FormStatusFragmentPresenter(this);
        String processId = "5c3789e1d503a3376335b271"; // FIXME: 01-02-2019 Replace this hardcoded value
        presenter.getProcessDetails(processId);
    }

    /**
     * This method fetches all the pending forms from DB
     */
    private void getPendingFormsFromDB() {

    }

    private void setAdapter(final String categoryName) {
        final FormCategoryAdapter adapter;
        switch (mFormStatus) {
            case FORM_STATUS_COMPLETED:
                adapter = new FormCategoryAdapter(getContext(), FORM_STATUS_COMPLETED, categoryName);
                mRecyclerView.setAdapter(adapter);
                mNoRecordsView.setVisibility(View.GONE);
                break;
            case FORM_STATUS_PENDING:
                mNoRecordsView.setVisibility(View.VISIBLE);
//                adapter = new FormCategoryAdapter(getContext(), FORM_STATUS_PENDING, categoryName);
                break;
            default: // FORM_STATUS_ALL
                mNoRecordsView.setVisibility(View.VISIBLE);
//                adapter = new FormCategoryAdapter(getContext(), FORM_STATUS_ALL, categoryName);
                break;
        }

    }

    @Override
    public void onFailureListener(String message) {
        Log.e(TAG, "onFailureListener: " + message);
    }

    @Override
    public void onErrorListener(VolleyError error) {
        Log.e(TAG, "onErrorListener: " + error.getMessage());
    }

    @Override
    public void onFormsLoaded(List<Form> formList) {
        Log.i(TAG, "onFormsLoaded: " + formList.toString());
    }

    @Override
    public void onFormsLoaded(String response) {
        try {
            JSONObject object = new JSONObject(response);
            JSONObject category = object.getJSONObject("data").getJSONObject("category");
            String categoryName = category.getString("name");

            setAdapter(categoryName);
            Log.i(TAG, "onFormsLoaded: " + category.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "onFormsLoaded: ", e);
        }

    }
}
