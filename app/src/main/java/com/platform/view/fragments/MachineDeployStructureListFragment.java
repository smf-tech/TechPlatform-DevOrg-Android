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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.platform.R;
import com.platform.listeners.APIDataListener;
import com.platform.models.SujalamSuphalam.StructureData;
import com.platform.presenter.MachineDeployStructureListFragmentPresenter;
import com.platform.view.adapters.SSDataListAdapter;
import com.platform.view.adapters.StructureListAdapter;

import java.util.ArrayList;

public class MachineDeployStructureListFragment extends Fragment  implements APIDataListener{
    private View machineDeployStructureListFragmentView;
    private RecyclerView rvStructureList;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private StructureListAdapter structureListAdapter;
    private MachineDeployStructureListFragmentPresenter machineDeployStructureListFragmentPresenter;
    private final ArrayList<StructureData> structureListData = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        machineDeployStructureListFragmentView = inflater.inflate(R.layout.fragment_machine_deploy_structure_list,
                container, false);
        return machineDeployStructureListFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        //viewType = bundle.getInt("viewType");
        init();
    }

    private void init() {
        progressBarLayout = machineDeployStructureListFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = machineDeployStructureListFragmentView.findViewById(R.id.pb_profile_act);
        rvStructureList = machineDeployStructureListFragmentView.findViewById(R.id.rv_structure_list);
        rvStructureList.setLayoutManager(new LinearLayoutManager(getActivity()));
        structureListAdapter = new StructureListAdapter(getActivity(), this, structureListData);
        rvStructureList.setAdapter(structureListAdapter);
        machineDeployStructureListFragmentPresenter.getDeployableStructuresList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (machineDeployStructureListFragmentPresenter != null) {
            machineDeployStructureListFragmentPresenter.clearData();
            machineDeployStructureListFragmentPresenter = null;
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
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void closeCurrentActivity() {
        getActivity().finish();
    }
}
