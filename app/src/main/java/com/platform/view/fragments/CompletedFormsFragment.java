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
import com.google.gson.Gson;
import com.platform.R;
import com.platform.listeners.FormStatusCallListener;
import com.platform.models.pm.ProcessData;
import com.platform.models.pm.Processes;
import com.platform.presenter.FormStatusFragmentPresenter;
import com.platform.view.adapters.FormCategoryAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompletedFormsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("CanBeFinal")
public class CompletedFormsFragment extends Fragment implements FormStatusCallListener {
    private static final String TAG = CompletedFormsFragment.class.getSimpleName();

    private TextView mNoRecordsView;
    private Map<String, List<String>> mFormsList = new HashMap<>();
    FormCategoryAdapter adapter;
    Map<String, String> categoryMap = new HashMap<>();

    public CompletedFormsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CompletedFormsFragment.
     */
    public static CompletedFormsFragment newInstance() {
        return new CompletedFormsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final RecyclerView recyclerView = view.findViewById(R.id.forms_list);
        mNoRecordsView = view.findViewById(R.id.no_records_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FormStatusFragmentPresenter presenter = new FormStatusFragmentPresenter(this);
        presenter.getAllFormMasters();

        adapter = new FormCategoryAdapter(getContext(), mFormsList);
        recyclerView.setAdapter(adapter);
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
    public void onFormsLoaded(String response) {
        FormStatusFragmentPresenter presenter = new FormStatusFragmentPresenter(this);

//        mChildList.clear();
        mFormsList.clear();

        Processes json = new Gson().fromJson(response, Processes.class);
        for (ProcessData data : json.getData()) {
//            String categoryName = data.getCategory().getName();
//            if (mChildList.containsKey(categoryName)) {
//                List<ProcessData> processData = mChildList.get(categoryName);
//                if (processData != null) {
//                    processData.add(data);
//                    mChildList.put(categoryName, processData);
//                }
//            } else {
//                List<ProcessData> processData = new ArrayList<>();
//                processData.add(data);
//                mChildList.put(categoryName, processData);
//            }

            String id = data.getId();

            mFormsList.put(data.getName(), null);
            categoryMap.put(data.getName(), data.getId());

            presenter.getSubmittedFormsOfMaster(id);
        }

      /*  if (!mChildList.isEmpty()) {
            setAdapter(mChildList);
            mNoRecordsView.setVisibility(View.GONE);
        } else {
            mNoRecordsView.setVisibility(View.VISIBLE);
        }*/

    }

    @Override
    public void onMastersFormsLoaded(final String response) {
        ArrayList<String> list = new ArrayList<>();
        String formID = "";

        try {
            if (new JSONObject(response).has("values")) {
                JSONArray values = new JSONObject(response).getJSONArray("values");
                for (int i = 0; i < values.length(); i++) {
                    JSONObject object = new JSONObject(String.valueOf(values.get(i)));
                    formID = object.getString("form_id");
                    JSONObject id = object.getJSONObject("_id");
                    String oid = id.getString("$oid");
                    list.add(oid);
                }
            } else {
                list.add("No Forms available");
            }

            for (final String s : mFormsList.keySet()) {
                String id = categoryMap.get(s);
                if (id.equals(formID)) {
                    mFormsList.put(s, list);
                } else {
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add("No Forms available");
                    mFormsList.put(s, arrayList);
                }
            }

            if (!mFormsList.isEmpty()) {
                adapter.notifyDataSetChanged();
                mNoRecordsView.setVisibility(View.GONE);
            } else {
                mNoRecordsView.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static class ProcessDemoObject {
        public String name;
        public String id;

        private ProcessDemoObject(final String name, final String id) {
            this.name = name;
            this.id = id;
        }
    }
}
