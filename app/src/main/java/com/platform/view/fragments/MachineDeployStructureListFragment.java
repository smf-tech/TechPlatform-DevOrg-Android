package com.platform.view.fragments;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.platform.R;
import com.platform.listeners.APIDataListener;
import com.platform.models.SujalamSuphalam.StructureData;
import com.platform.models.SujalamSuphalam.StructureListAPIResponse;
import com.platform.presenter.MachineDeployStructureListFragmentPresenter;
import com.platform.utility.Util;
import com.platform.view.activities.SSActionsActivity;
import com.platform.view.adapters.SSDataListAdapter;
import com.platform.view.adapters.StructureListAdapter;

import java.util.ArrayList;

public class MachineDeployStructureListFragment extends Fragment  implements APIDataListener{
    private View machineDeployStructureListFragmentView;
    private TextView tvDistrictFilter, tvTalukaFilter;
    private RecyclerView rvStructureList;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private StructureListAdapter structureListAdapter;
    private MachineDeployStructureListFragmentPresenter machineDeployStructureListFragmentPresenter;
    private final ArrayList<StructureData> structureListData = new ArrayList<>();
    String machineId;
    String type, currentStructureId;

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

        //Bundle bundle = this.getArguments();
        machineId = getActivity().getIntent().getStringExtra("machineId");
        currentStructureId = getActivity().getIntent().getStringExtra("currentStructureId");
        type = getActivity().getIntent().getStringExtra("type");
        //viewType = bundle.getInt("viewType");
        init();
    }

    private void init() {
        progressBarLayout = machineDeployStructureListFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = machineDeployStructureListFragmentView.findViewById(R.id.pb_profile_act);
        tvDistrictFilter = machineDeployStructureListFragmentView.findViewById(R.id.tv_district_filter);
        if (Util.getUserObjectFromPref().getUserLocation().getDistrictIds() != null &&
                Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size() > 0) {
            tvDistrictFilter.setText(Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getName());
        }
        tvTalukaFilter = machineDeployStructureListFragmentView.findViewById(R.id.tv_taluka_filter);
        if (Util.getUserObjectFromPref().getUserLocation().getTalukaIds() != null &&
                Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size() > 0) {
            tvTalukaFilter.setText(Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getName());
        }
        rvStructureList = machineDeployStructureListFragmentView.findViewById(R.id.rv_structure_list);
        rvStructureList.setLayoutManager(new LinearLayoutManager(getActivity()));
        structureListAdapter = new StructureListAdapter(getActivity(), this, structureListData, type);
        rvStructureList.setAdapter(structureListAdapter);
        machineDeployStructureListFragmentPresenter = new MachineDeployStructureListFragmentPresenter(this);
        if(type.equalsIgnoreCase("deployMachine")) {
            machineDeployStructureListFragmentPresenter.getDeployableStructuresList("5c669d13c7982d31cc6b86cd",
                    "5c66a468d42f283b440013e3","5c66a588d42f283b44001447",
                    "machineDeployableStructures", currentStructureId);
        } else if(type.equalsIgnoreCase("shiftMachine")) {
            machineDeployStructureListFragmentPresenter.getDeployableStructuresList("5c669d13c7982d31cc6b86cd",
                    "5c66a468d42f283b440013e3","5c66a588d42f283b44001447",
                    "machineShiftStructures", currentStructureId);
        }
    }

    public void populateStructureData(String requestID, StructureListAPIResponse structureList) {
        if (structureList != null) {
            if (requestID.equals(MachineDeployStructureListFragmentPresenter.GET_MACHINE_DEPLOY_STRUCTURE_LIST)) {
                structureListData.clear();
                for (StructureData structureData : structureList.getData()) {
                    if (structureData != null) {
                        structureListData.add(structureData);
                    }
                }
                rvStructureList.setAdapter(structureListAdapter);
                structureListAdapter.notifyDataSetChanged();
            }
        }
    }

    public void deployMachine(int position) {
        machineDeployStructureListFragmentPresenter.deployMachine(structureListData.get(position).getStructureId(), machineId);
    }

    public void ShiftMachine(int position) {
        Intent intent = new Intent(getActivity(), SSActionsActivity.class);
        intent.putExtra("SwitchToFragment", "MachineShiftingFormFragment");
        intent.putExtra("title", "Machine Shifting");
        intent.putExtra("machineId", machineId);
        intent.putExtra("currentStructureId", currentStructureId);
        intent.putExtra("newStructureId", structureListData.get(position).getStructureId());
        startActivity(intent);
    }

    public void showResponse(String responseStatus, String requestId, int status) {
        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                        .findViewById(android.R.id.content), responseStatus,
                Snackbar.LENGTH_LONG);
        if(requestId.equals(MachineDeployStructureListFragmentPresenter.DEPLOY_MACHINE)){
            if(status == 200){
                getActivity().finish();
                Intent intent = new Intent(getActivity(), SSActionsActivity.class);
                intent.putExtra("SwitchToFragment", "StructureMachineListFragment");
                intent.putExtra("viewType", 2);
                intent.putExtra("title", "Machine List");
                getActivity().startActivity(intent);
            }
        }
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
