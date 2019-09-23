package com.platform.view.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.platform.R;
import com.platform.listeners.APIDataListener;
import com.platform.models.SujalamSuphalam.MachineData;
import com.platform.models.SujalamSuphalam.MachineListAPIResponse;
import com.platform.models.leaves.LeaveData;
import com.platform.presenter.StructureMachineListFragmentPresenter;
import com.platform.utility.Util;
import com.platform.view.adapters.SSDataListAdapter;

import java.util.ArrayList;

public class StructureMachineListFragment extends Fragment implements APIDataListener {
    private View structureMachineListFragmentView;
    int viewType;
    private RecyclerView rvDataList;
    private final ArrayList<MachineData> ssMachineListData = new ArrayList<>();
    private SSDataListAdapter ssDataListAdapter;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private StructureMachineListFragmentPresenter structureMachineListFragmentPresenter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        structureMachineListFragmentView = inflater.inflate(R.layout.fragment_structure_machine_list, container, false);
        return structureMachineListFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        viewType = bundle.getInt("viewType");
        init();
    }

    private void init(){
        progressBarLayout = structureMachineListFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = structureMachineListFragmentView.findViewById(R.id.pb_profile_act);
        rvDataList = structureMachineListFragmentView.findViewById(R.id.rv_data_list);
        rvDataList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        ssDataListAdapter = new SSDataListAdapter(ssMachineListData);
        rvDataList.setAdapter(ssDataListAdapter);
        structureMachineListFragmentPresenter = new StructureMachineListFragmentPresenter(this);
        if(viewType == 1){
//            structureMachineListFragmentPresenter.getStrucuresList(Util.getUserObjectFromPref().getUserLocation().getDistrictIds()
//                    .get(0).getId(), Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getId());
        } else {
//            structureMachineListFragmentPresenter.getMachinesList(Util.getUserObjectFromPref().getUserLocation().getDistrictIds()
//                    .get(0).getId(), Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getId());
            structureMachineListFragmentPresenter.getMachinesList("5c66989ec7982d31cc6b86c3","5ced0c27d42f28124c0150ba", "5c66a53cd42f283b440013eb");
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (structureMachineListFragmentPresenter != null) {
            structureMachineListFragmentPresenter.clearData();
            structureMachineListFragmentPresenter = null;
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
//        if (getActivity() != null) {
//            getActivity().onBackPressed();
//        }
        getActivity().finish();
    }


    public void populateMachineData(String requestID, MachineListAPIResponse machineListData) {
        if (machineListData != null) {
            if (requestID.equals(StructureMachineListFragmentPresenter.GET_MACHINE_LIST)) {
                ssMachineListData.clear();
                for (MachineData machineData : machineListData.getData()) {
                    if (machineData != null) {
                        ssMachineListData.add(machineData);
                    }
                }
                rvDataList.setAdapter(ssDataListAdapter);
                ssDataListAdapter.notifyDataSetChanged();
            }
        }
    }
}
