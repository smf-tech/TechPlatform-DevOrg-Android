package com.octopusbjsindia.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataList;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataResponse;
import com.octopusbjsindia.models.SujalamSuphalam.SSAnalyticsAPIResponse;
import com.octopusbjsindia.models.SujalamSuphalam.SSAnalyticsData;
import com.octopusbjsindia.models.SujalamSuphalam.SSMasterDatabase;
import com.octopusbjsindia.models.home.RoleAccessAPIResponse;
import com.octopusbjsindia.models.home.RoleAccessList;
import com.octopusbjsindia.models.home.RoleAccessObject;
import com.octopusbjsindia.presenter.SujalamSuphalamFragmentPresenter;
import com.octopusbjsindia.utility.AppEvents;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.HomeActivity;
import com.octopusbjsindia.view.activities.SSActionsActivity;
import com.octopusbjsindia.view.adapters.SSAnalyticsAdapter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SujalamSufalamFragment extends Fragment implements  View.OnClickListener, View.OnLongClickListener, APIDataListener {

    private View sujalamSufalamFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private TextView tvStructureView, tvMachineView, tvToggle;
    private Button btnSsView;
    private RecyclerView rvSSAnalytics;
    private int viewType = 1;
    private SSAnalyticsAdapter structureAnalyticsAdapter, machineAnalyticsAdapter;
    private ArrayList<SSAnalyticsData> structureAnalyticsDataList = new ArrayList<>();
    private ArrayList<SSAnalyticsData> machineAnalyticsDataList = new ArrayList<>();
    private ArrayList<MasterDataList> masterDataList = new ArrayList<>();
    private SujalamSuphalamFragmentPresenter sujalamSuphalamFragmentPresenter;
    private boolean isStructureView, isMachineView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null && getArguments() != null) {
            String title = (String) getArguments().getSerializable("TITLE");
            ((HomeActivity) getActivity()).setActionBarTitle(title);
            ((HomeActivity) getActivity()).setSyncButtonVisibility(false);

            if ((boolean) getArguments().getSerializable("SHOW_BACK")) {
                ((HomeActivity) getActivity()).showBackArrow();
            }
        }
        AppEvents.trackAppEvent(getString(R.string.ss_screen_visit));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sujalamSufalamFragmentView = inflater.inflate(R.layout.fragment_sujalam_sufalam, container, false);
        return sujalamSufalamFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null && getArguments() != null) {
            String title = (String) getArguments().getSerializable("TITLE");
            ((HomeActivity) getActivity()).setActionBarTitle(title);
        }
        init();
    }

    private void init(){
        progressBarLayout = sujalamSufalamFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = sujalamSufalamFragmentView.findViewById(R.id.pb_profile_act);
        tvStructureView = sujalamSufalamFragmentView.findViewById(R.id.tv_structure_view);
        tvMachineView = sujalamSufalamFragmentView.findViewById(R.id.tv_machine_view);
        tvToggle = sujalamSufalamFragmentView.findViewById(R.id.tv_toggle);
        tvStructureView.setOnClickListener(this);
        tvMachineView.setOnClickListener(this);
        btnSsView = sujalamSufalamFragmentView.findViewById(R.id.btn_ss_view);
        btnSsView.setOnClickListener(this);
        btnSsView.setOnLongClickListener(this);
        rvSSAnalytics = sujalamSufalamFragmentView.findViewById(R.id.rv_ss_analytics);
        rvSSAnalytics.setLayoutManager(new GridLayoutManager(getContext(), 2));

        structureAnalyticsAdapter = new SSAnalyticsAdapter(getActivity(),structureAnalyticsDataList,1, "Structure List");
        machineAnalyticsAdapter = new SSAnalyticsAdapter(getActivity(),machineAnalyticsDataList,2,"Machine List");
//        List<SSMasterDatabase> ssMasterDatabaseList = DatabaseManager.getDBInstance(Platform.getInstance()).
//                getSSMasterDatabaseDao().getSSMasterData();


        RoleAccessAPIResponse roleAccessAPIResponse = Util.getRoleAccessObjectFromPref();
        RoleAccessList roleAccessList = roleAccessAPIResponse.getData();
        if(roleAccessList != null) {
            List<RoleAccessObject> roleAccessObjectList = roleAccessList.getRoleAccess();
            for (RoleAccessObject roleAccessObject : roleAccessObjectList) {
                if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_VIEW_STRUCTURES)) {
                    isStructureView = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_VIEW_MACHINES)) {
                    isMachineView = true;
                }
            }
        }
        setStructureView();
    }

    @Override
    public void onResume() {
        super.onResume();
        btnSsView.setEnabled(true);
        sujalamSuphalamFragmentPresenter = new SujalamSuphalamFragmentPresenter(this);
        if(Util.isConnected(getActivity())) {
            sujalamSuphalamFragmentPresenter.getAnalyticsData(sujalamSuphalamFragmentPresenter.GET_STRUCTURE_ANALYTICS);
            sujalamSuphalamFragmentPresenter.getAnalyticsData(sujalamSuphalamFragmentPresenter.GET_MACHINE_ANALYTICS);
            sujalamSuphalamFragmentPresenter.getSSMasterData();
        } else {
            Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_structure_view:
                setStructureView();
                break;
            case R.id.tv_machine_view:
                setMachineView();
                break;
            case R.id.btn_ss_view:
                btnSsView.setEnabled(false);
                intent = new Intent(getActivity(), SSActionsActivity.class);
                intent.putExtra("SwitchToFragment", "StructureMachineListFragment");
                if (viewType == 1) {
                    intent.putExtra("viewType", 1);
                    intent.putExtra("title", "Structure List");
                } else {
                    intent.putExtra("viewType", 2);
                    intent.putExtra("title", "Machine List");
                }
                getActivity().startActivity(intent);
                break;
        }
    }

    private void setMachineView(){
        viewType = 2;
        tvMachineView.setTextColor(getResources().getColor(R.color.dark_grey));
        tvMachineView.setTypeface(tvMachineView.getTypeface(), Typeface.BOLD);
        tvStructureView.setTextColor(getResources().getColor(R.color.text_lite_grey));
        tvStructureView.setTypeface(tvStructureView.getTypeface(), Typeface.NORMAL);
        tvToggle.setBackgroundResource(R.drawable.ic_toggle_machine_view);
        rvSSAnalytics.setAdapter(machineAnalyticsAdapter);
        machineAnalyticsAdapter.notifyDataSetChanged();
        if(isMachineView) {
            btnSsView.setVisibility(View.VISIBLE);
            btnSsView.setText("Machine View");
        } else {
            btnSsView.setVisibility(View.INVISIBLE);
        }
        if(machineAnalyticsDataList.size()>0){
            sujalamSufalamFragmentView.findViewById(R.id.ly_no_data).setVisibility(View.GONE);
        } else {
            sujalamSufalamFragmentView.findViewById(R.id.ly_no_data).setVisibility(View.VISIBLE);
        }
    }

    private void setStructureView(){
        viewType = 1;
        tvStructureView.setTextColor(getResources().getColor(R.color.dark_grey));
        tvStructureView.setTypeface(tvStructureView.getTypeface(), Typeface.BOLD);
        tvMachineView.setTextColor(getResources().getColor(R.color.text_lite_grey));
        tvMachineView.setTypeface(tvMachineView.getTypeface(), Typeface.NORMAL);
        tvToggle.setBackgroundResource(R.drawable.ic_toggle_structure_view);
        rvSSAnalytics.setAdapter(structureAnalyticsAdapter);
        structureAnalyticsAdapter.notifyDataSetChanged();
        if(isStructureView) {
            btnSsView.setVisibility(View.VISIBLE);
            btnSsView.setText("Structure View");
        } else {
            btnSsView.setVisibility(View.INVISIBLE);
        }
        if(structureAnalyticsDataList.size()>0){
            sujalamSufalamFragmentView.findViewById(R.id.ly_no_data).setVisibility(View.GONE);
        } else {
            sujalamSufalamFragmentView.findViewById(R.id.ly_no_data).setVisibility(View.VISIBLE);
        }
    }

    public void populateAnalyticsData(String requestCode, SSAnalyticsAPIResponse analyticsData) {
        if (analyticsData != null) {
            if(requestCode.equals(sujalamSuphalamFragmentPresenter.GET_STRUCTURE_ANALYTICS)) {
                structureAnalyticsDataList.clear();
                for (SSAnalyticsData data : analyticsData.getData()) {
                    if (data != null) {
                        structureAnalyticsDataList.add(data);
                    }
                }

            } else if(requestCode.equals(sujalamSuphalamFragmentPresenter.GET_MACHINE_ANALYTICS)) {
                machineAnalyticsDataList.clear();
                for (SSAnalyticsData data : analyticsData.getData()) {
                    if (data != null) {
                        machineAnalyticsDataList.add(data);
                    }
                }
            }
            if(viewType==1){
                rvSSAnalytics.setAdapter(structureAnalyticsAdapter);
            } else {
                rvSSAnalytics.setAdapter(machineAnalyticsAdapter);
            }
            structureAnalyticsAdapter.notifyDataSetChanged();
        }
        if(structureAnalyticsDataList.size()>0){
            sujalamSufalamFragmentView.findViewById(R.id.ly_no_data).setVisibility(View.GONE);
        } else {
            sujalamSufalamFragmentView.findViewById(R.id.ly_no_data).setVisibility(View.VISIBLE);
        }
    }

    public void setMasterData(MasterDataResponse masterDataResponse) {
        if(masterDataResponse.getStatus()==1000){
            Util.logOutUser(getActivity());
        } else {
            DatabaseManager.getDBInstance(Platform.getInstance()).getSSMasterDatabaseDao().deleteSSMasterData();
            Gson gson = new GsonBuilder().create();
            String ssMasterDataList = gson.toJson(masterDataResponse.getData());
            Date date = Calendar.getInstance().getTime();
            String strDate = Util.getDateFromTimestamp(date.getTime(), Constants.FORM_DATE_FORMAT);
            SSMasterDatabase ssMasterDatabase = new SSMasterDatabase();
            ssMasterDatabase.setDateTime(strDate);
            ssMasterDatabase.setData(ssMasterDataList);
            DatabaseManager.getDBInstance(Platform.getInstance()).getSSMasterDatabaseDao().insert(ssMasterDatabase);
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
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (sujalamSuphalamFragmentPresenter != null) {
            sujalamSuphalamFragmentPresenter.clearData();
            sujalamSuphalamFragmentPresenter = null;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        Util.showToast("long click",this);
        return false;
    }
}
