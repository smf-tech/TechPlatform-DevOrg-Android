package com.platform.view.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.platform.Platform;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.models.SujalamSuphalam.StructureData;
import com.platform.view.adapters.SSStructureListAdapter;

import java.util.ArrayList;

public class SavedStructureListFragment extends Fragment {

    View view;

    private SSStructureListAdapter adapter;
    private final ArrayList<StructureData> ssStructureListData = new ArrayList<>();


    public SavedStructureListFragment() {
        // Required empty public constructor
    }

    public static SavedStructureListFragment newInstance(String param1, String param2) {
        SavedStructureListFragment fragment = new SavedStructureListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_saved_structure_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ssStructureListData.addAll(DatabaseManager.getDBInstance(Platform.getInstance()).getStructureDataDao().getAllStructure());
        ssStructureListData.size();


        RecyclerView rvDataList = view.findViewById(R.id.rv_data_list);
        rvDataList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new SSStructureListAdapter(getActivity(), ssStructureListData, false);
        rvDataList.setAdapter(adapter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

 }