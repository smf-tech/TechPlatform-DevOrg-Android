package com.platform.view.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.platform.R;
import com.platform.view.adapters.FormCategoryAdapter;

import static com.platform.utility.Constants.Form.FORM_STATUS_ALL;
import static com.platform.utility.Constants.Form.FORM_STATUS_COMPLETED;
import static com.platform.utility.Constants.Form.FORM_STATUS_PENDING;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FormStatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FormStatusFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    private String mFormStatus;


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

        RecyclerView recyclerView = view.findViewById(R.id.forms_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FormCategoryAdapter adapter;

        switch (mFormStatus) {
            case FORM_STATUS_COMPLETED:
                adapter = new FormCategoryAdapter(getContext(), FORM_STATUS_COMPLETED);
                break;
            case FORM_STATUS_PENDING:
                adapter = new FormCategoryAdapter(getContext(), FORM_STATUS_PENDING);
                break;
            default:
                adapter = new FormCategoryAdapter(getContext(), FORM_STATUS_ALL);
                break;
        }
        recyclerView.setAdapter(adapter);

    }

}
