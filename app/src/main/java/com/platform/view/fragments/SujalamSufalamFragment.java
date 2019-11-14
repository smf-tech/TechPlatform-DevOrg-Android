package com.platform.view.fragments;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.platform.Platform;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.listeners.APIDataListener;
import com.platform.models.SujalamSuphalam.MasterDataList;
import com.platform.models.SujalamSuphalam.MasterDataResponse;
import com.platform.models.SujalamSuphalam.SSAnalyticsAPIResponse;
import com.platform.models.SujalamSuphalam.SSAnalyticsData;
import com.platform.models.SujalamSuphalam.SSMasterDatabase;
import com.platform.models.home.RoleAccessAPIResponse;
import com.platform.models.home.RoleAccessList;
import com.platform.models.home.RoleAccessObject;
import com.platform.presenter.SujalamSuphalamFragmentPresenter;
import com.platform.utility.AppEvents;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.CreateStructureActivity;
import com.platform.view.activities.HomeActivity;
import com.platform.view.activities.MachineMouActivity;
import com.platform.view.activities.SSActionsActivity;
import com.platform.view.adapters.SSAnalyticsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SujalamSufalamFragment extends Fragment implements  View.OnClickListener , APIDataListener {

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
    private boolean isStructureAdd, isMachineAdd, isStructureView, isMachineView;

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
        rvSSAnalytics = sujalamSufalamFragmentView.findViewById(R.id.rv_ss_analytics);
        rvSSAnalytics.setLayoutManager(new GridLayoutManager(getContext(), 2));

        structureAnalyticsAdapter = new SSAnalyticsAdapter(structureAnalyticsDataList);
        machineAnalyticsAdapter = new SSAnalyticsAdapter(machineAnalyticsDataList);
        List<SSMasterDatabase> ssMasterDatabaseList = DatabaseManager.getDBInstance(Platform.getInstance()).
                getSSMasterDatabaseDao().getSSMasterData();

        sujalamSuphalamFragmentPresenter = new SujalamSuphalamFragmentPresenter(this);
        sujalamSuphalamFragmentPresenter.getAnalyticsData(sujalamSuphalamFragmentPresenter.GET_STRUCTURE_ANALYTICS);
        sujalamSuphalamFragmentPresenter.getAnalyticsData(sujalamSuphalamFragmentPresenter.GET_MACHINE_ANALYTICS);
        //DatabaseManager.getDBInstance(Platform.getInstance()).getSSMasterDatabaseDao().deleteSSMasterData();
        if(ssMasterDatabaseList.size() == 0) {
            sujalamSuphalamFragmentPresenter.getSSMasterData();
        }
        RoleAccessAPIResponse roleAccessAPIResponse = Util.getRoleAccessObjectFromPref();
        RoleAccessList roleAccessList = roleAccessAPIResponse.getData();
        List<RoleAccessObject> roleAccessObjectList = roleAccessList.getRoleAccess();
        for (RoleAccessObject roleAccessObject: roleAccessObjectList) {
            if(roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_VIEW_STRUCTURES)) {
                isStructureView = true;
                continue;
            } else if(roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_VIEW_MACHINES)) {
                isMachineView = true;
            }
        }
        setStructureView();
    }

    @Override
    public void onResume() {
        super.onResume();
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
            btnSsView.setText("Machine View >");
        } else {
            btnSsView.setVisibility(View.INVISIBLE);
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
            btnSsView.setText("Structure View >");
        } else {
            btnSsView.setVisibility(View.INVISIBLE);
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
                rvSSAnalytics.setAdapter(structureAnalyticsAdapter);
                structureAnalyticsAdapter.notifyDataSetChanged();
            } else if(requestCode.equals(sujalamSuphalamFragmentPresenter.GET_MACHINE_ANALYTICS)) {
                machineAnalyticsDataList.clear();
                for (SSAnalyticsData data : analyticsData.getData()) {
                    if (data != null) {
                        machineAnalyticsDataList.add(data);
                    }
                }
            }
        }
    }

    public void setMasterData(MasterDataResponse masterDataResponse) {
        if(masterDataResponse.getStatus()==1000){
            Util.logOutUser(getActivity());
        } else {
            JSONObject json = new JSONObject();
//            try {
//                json.put("masterData", masterDataResponse.getData());
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            //String ssMasterDataList = json.toString();
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
}
