package com.platform.view.fragments;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.platform.Platform;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.listeners.APIDataListener;
import com.platform.listeners.CustomSpinnerListener;
import com.platform.models.SujalamSuphalam.MachineData;
import com.platform.models.SujalamSuphalam.MachineListAPIResponse;
import com.platform.models.SujalamSuphalam.StructureData;
import com.platform.models.SujalamSuphalam.StructureListAPIResponse;
import com.platform.models.common.CustomSpinnerObject;
import com.platform.models.home.RoleAccessAPIResponse;
import com.platform.models.home.RoleAccessList;
import com.platform.models.home.RoleAccessObject;
import com.platform.models.profile.JurisdictionLocation;
import com.platform.models.user.UserInfo;
import com.platform.presenter.StructureMachineListFragmentPresenter;
import com.platform.utility.Constants;
import com.platform.utility.GPSTracker;
import com.platform.utility.Util;
import com.platform.view.activities.CreateStructureActivity;
import com.platform.view.activities.MachineMouActivity;
import com.platform.view.activities.SSActionsActivity;
import com.platform.view.adapters.MutiselectDialogAdapter;
import com.platform.view.adapters.SSMachineListAdapter;
import com.platform.view.adapters.SSStructureListAdapter;
import com.platform.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class StructureMachineListFragment extends Fragment implements APIDataListener, View.OnClickListener, CustomSpinnerListener {
    private View structureMachineListFragmentView;
    private int viewType;
    private Context context;
    private RecyclerView rvDataList;
    private final ArrayList<MachineData> ssMachineListData = new ArrayList<>();
    private final ArrayList<MachineData> filteredMachineListData = new ArrayList<>();
    private final ArrayList<StructureData> ssStructureListData = new ArrayList<>();
    private final ArrayList<StructureData> filteredStructureListData = new ArrayList<>();
    private SSMachineListAdapter ssMachineListAdapter;
    private SSStructureListAdapter ssStructureListAdapter;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private StructureMachineListFragmentPresenter structureMachineListFragmentPresenter;
    private TextView tvStateFilter, tvDistrictFilter, tvTalukaFilter;
    private ArrayList<CustomSpinnerObject> machineDistrictList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> machineTalukaList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> machineTalukaDeployList = new ArrayList<>();
    private String selectedDistrict, selectedDistrictId, selectedTaluka, selectedTalukaId, selectedDeployTaluka,
            selectedDeployTalukaId;
    private int mouAction = 0;
    private boolean isMachineTerminate, isMachineAvailable;
    public boolean isMachineAdd, isMachineDepoly, isMachineVisitValidationForm, isSiltTransportForm,
            isDieselRecordForm, isMachineShiftForm, isMachineRelease, isStateFilter, isDistrictFilter, isTalukaFilter,
            isVillageFilter, isStructureAdd;
    private FloatingActionButton fbCreate;
    private boolean isTalukaApiFirstCall;
    private TextView tvNoData;
    private ImageView btnFilterClear;

    public static final Integer ACCESS_CODE_VISIT_MONITORTNG = 106;
    public static final Integer ACCESS_CODE_STRUCTURE_COMPLETE = 107;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        structureMachineListFragmentView = inflater.inflate(R.layout.fragment_structure_machine_list, container, false);
        context = getActivity();
        return structureMachineListFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        viewType = bundle.getInt("viewType");
        init();
    }

    private void init() {
        progressBarLayout = structureMachineListFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = structureMachineListFragmentView.findViewById(R.id.pb_profile_act);
        fbCreate = structureMachineListFragmentView.findViewById(R.id.fb_create);
        tvNoData = structureMachineListFragmentView.findViewById(R.id.tv_no_data_msg);
        tvStateFilter = structureMachineListFragmentView.findViewById(R.id.tv_state_filter);
        tvDistrictFilter = structureMachineListFragmentView.findViewById(R.id.tv_district_filter);
        tvTalukaFilter = structureMachineListFragmentView.findViewById(R.id.tv_taluka_filter);
        btnFilterClear = structureMachineListFragmentView.findViewById(R.id.btn_filter_clear);
        btnFilterClear.setOnClickListener(this);
        if (Util.getUserObjectFromPref().getUserLocation().getStateId() != null &&
                Util.getUserObjectFromPref().getUserLocation().getStateId().size() > 0) {
            tvStateFilter.setText(Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getName());
        }
        if (Util.getUserObjectFromPref().getUserLocation().getDistrictIds() != null &&
                Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size() > 0) {
            tvDistrictFilter.setText(Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getName());
        }
        if (Util.getUserObjectFromPref().getUserLocation().getTalukaIds() != null &&
                Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size() > 0) {
            tvTalukaFilter.setText(Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getName());
        }
        RoleAccessAPIResponse roleAccessAPIResponse = Util.getRoleAccessObjectFromPref();
        RoleAccessList roleAccessList = roleAccessAPIResponse.getData();
        List<RoleAccessObject> roleAccessObjectList = roleAccessList.getRoleAccess();
        for (RoleAccessObject roleAccessObject : roleAccessObjectList) {
            if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_ADD_MACHINE)) {
                isMachineAdd = true;
                continue;
            } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_ADD_STRUCTURE)) {
                isStructureAdd = true;
                continue;
            } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_MOU_TERMINATE)) {
                isMachineTerminate = true;
                continue;
            } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_AVAILABLE_MACHINE)) {
                isMachineAvailable = true;
                continue;
            } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_DEPLOY_MACHINE)) {
                isMachineDepoly = true;
                continue;
            } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_MACHINE_VISIT_VALIDATION_FORM)) {
                isMachineVisitValidationForm = true;
                continue;
            } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_SILT_TRANSPORT_FORM)) {
                isSiltTransportForm = true;
                continue;
            } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_DIESEL_RECORD_FORM)) {
                isDieselRecordForm = true;
                continue;
            } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_MACHINE_SHIFT_FORM)) {
                isMachineShiftForm = true;
                continue;
            } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_MACHINE_RELEASE)) {
                isMachineRelease = true;
                continue;
            } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_STATE)) {
                isStateFilter = true;
                continue;
            } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_DISTRICT)) {
                isDistrictFilter = true;
                continue;
            } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_TALUKA)) {
                isTalukaFilter = true;
                continue;
            } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_VILLAGE)) {
                isVillageFilter = true;
            }
        }
        rvDataList = structureMachineListFragmentView.findViewById(R.id.rv_data_list);
        rvDataList.setLayoutManager(new LinearLayoutManager(getActivity()));
        ssMachineListAdapter = new SSMachineListAdapter(getActivity(), this, filteredMachineListData);
        rvDataList.setAdapter(ssMachineListAdapter);
        ssStructureListAdapter = new SSStructureListAdapter(getActivity(), filteredStructureListData, true);
        rvDataList.setAdapter(ssStructureListAdapter);
        structureMachineListFragmentPresenter = new StructureMachineListFragmentPresenter(this);

        if (isStateFilter) {
            tvStateFilter.setOnClickListener(this);
        }
        if (isDistrictFilter) {
            tvDistrictFilter.setOnClickListener(this);
        }
        if (isTalukaFilter) {
            tvTalukaFilter.setOnClickListener(this);
        }
        if (viewType == 1) {
            if (isStructureAdd) {
                fbCreate.setVisibility(View.VISIBLE);
            } else {
                fbCreate.setVisibility(View.GONE);
            }
        } else {
            if (isMachineAdd) {
                fbCreate.setVisibility(View.VISIBLE);
            } else {
                fbCreate.setVisibility(View.INVISIBLE);
            }
        }

        fbCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewType == 1) {
                    if (Util.isConnected(getActivity())) {
                        Intent intent = new Intent(getActivity(), CreateStructureActivity.class);
                        getActivity().startActivity(intent);
                    } else {
                        Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
                    }
                } else {
                    Intent mouIntent = new Intent(getActivity(), MachineMouActivity.class);
                    mouIntent.putExtra("SwitchToFragment", "MachineMouFirstFragment");
                    mouIntent.putExtra("statusCode", Constants.SSModule.MACHINE_CREATE_STATUS_CODE);
                    getActivity().startActivity(mouIntent);
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rvDataList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if(dy > 0){
                        fbCreate.hide();
                    } else{
                        if (viewType == 1) {
                            if (isStructureAdd) {
                                fbCreate.show();
                            }
                        } else {
                            if (isMachineAdd) {
                                fbCreate.show();
                            }
                        }
                    }
                    super.onScrolled(recyclerView, dx, dy);
                }
            });
        }

        final SwipeRefreshLayout pullToRefresh = structureMachineListFragmentView.findViewById(R.id.pull_to_refresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onResume();
                pullToRefresh.setRefreshing(false);
            }
        });

        if (Util.isConnected(getActivity())) {
            if (tvDistrictFilter.getText() != null && tvDistrictFilter.getText().toString().length() > 0) {
                UserInfo userInfo = Util.getUserObjectFromPref();
                isTalukaApiFirstCall = true;
                structureMachineListFragmentPresenter.getJurisdictionLevelData(userInfo.getOrgId(),
                        Util.getUserObjectFromPref().getJurisdictionTypeId(),
                        Constants.JurisdictionLevelName.TALUKA_LEVEL);
            } else {
                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                .findViewById(android.R.id.content), "Your District is not available in your profile." +
                                "Please update your profile.",
                        Snackbar.LENGTH_LONG);
            }
        }
    }

    public void takeMouDoneAction(int position) {
        if (isMachineTerminate || isMachineAvailable) {
            showMouActionPopup(position);
        } else {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), "You can not take any action on this machine.",
                    Snackbar.LENGTH_LONG);
        }
    }

    public void deployMachine(int position) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogs_leave_layout);

        TextView title = dialog.findViewById(R.id.tv_dialog_title);
        title.setText("Sujalam Suphalam");
        title.setVisibility(View.VISIBLE);

        TextView text = dialog.findViewById(R.id.tv_dialog_subtext);
        text.setText(R.string.deploy_machine_alert_message);
        text.setVisibility(View.VISIBLE);

        Button button = dialog.findViewById(R.id.btn_dialog);
        button.setText("Deploy");
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(v -> {
            // Close dialog
            Intent intent = new Intent(getActivity(), SSActionsActivity.class);
            intent.putExtra("SwitchToFragment", "MachineDeployStructureListFragment");
            intent.putExtra("type", "deployMachine");
            intent.putExtra("title", "Select Structure");
            intent.putExtra("machineId", filteredMachineListData.get(position).getId());
            intent.putExtra("machineCode", filteredMachineListData.get(position).getMachineCode());
            getActivity().startActivity(intent);

            dialog.dismiss();
        });

        Button button1 = dialog.findViewById(R.id.btn_dialog_1);
        button1.setText("Cancel");
        button1.setVisibility(View.VISIBLE);
        button1.setOnClickListener(v -> {
            // Close dialog
            dialog.dismiss();
        });

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void releaseMachine(int position) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogs_leave_layout);

        TextView title = dialog.findViewById(R.id.tv_dialog_title);
        title.setText("Sujalam Suphalam");
        title.setVisibility(View.VISIBLE);

        TextView text = dialog.findViewById(R.id.tv_dialog_subtext);
        text.setText(R.string.release_machine_alert_message);
        text.setVisibility(View.VISIBLE);

        Button button = dialog.findViewById(R.id.btn_dialog);
        button.setText("Release");
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(v -> {
            // Close dialog
            structureMachineListFragmentPresenter.updateMachineStatus(filteredMachineListData.get(position).getId(),
                    filteredMachineListData.get(position).getMachineCode(),
                    Constants.SSModule.MACHINE_REALEASED_STATUS_CODE, Constants.SSModule.MACHINE_TYPE);
            dialog.dismiss();
        });

        Button button1 = dialog.findViewById(R.id.btn_dialog_1);
        button1.setText("Cancel");
        button1.setVisibility(View.VISIBLE);
        button1.setOnClickListener(v -> {
            // Close dialog
            dialog.dismiss();
        });

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void showMouActionPopup(int position) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_mou_action_layout);
        TextInputLayout tlyTerminateReason = dialog.findViewById(R.id.tly_terminate_reason);
        EditText editTerminateReason = dialog.findViewById(R.id.edit_terminate_reason);
        RecyclerView rvTaluka = dialog.findViewById(R.id.rv_taluka);

        RadioGroup rgMouAction = dialog.findViewById(R.id.rg_mou_action);
        RadioButton rbTerminate, rbDeploy;
        rbTerminate = dialog.findViewById(R.id.rb_terminate);
        rbDeploy = dialog.findViewById(R.id.rb_deploy);
        if (!isMachineAvailable) {
            rbDeploy.setVisibility(View.GONE);
        }
        if (!isMachineTerminate) {
            rbTerminate.setVisibility(View.GONE);
        }
        rgMouAction.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_terminate:
                        mouAction = 2;
                        rvTaluka.setVisibility(View.GONE);
                        tlyTerminateReason.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_deploy:
                        mouAction = 1;
                        machineTalukaDeployList.clear();
                        machineTalukaDeployList.addAll(machineTalukaList);
                        tlyTerminateReason.setVisibility(View.GONE);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        rvTaluka.setLayoutManager(layoutManager);
                        MutiselectDialogAdapter mutiselectDialogAdapter = new MutiselectDialogAdapter(getActivity(),
                                machineTalukaDeployList, false);
                        rvTaluka.setAdapter(mutiselectDialogAdapter);
                        rvTaluka.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        TextView title = dialog.findViewById(R.id.tv_dialog_title);
        title.setText("What would you like to do?");
        title.setVisibility(View.VISIBLE);
        TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        Button button = dialog.findViewById(R.id.btn_dialog);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(v -> {
            if (mouAction != 0) {
                // Close dialog
                if (mouAction == 1) {
                    for (CustomSpinnerObject mDeployTaluka : machineTalukaDeployList) {
                        if (mDeployTaluka.isSelected()) {
                            selectedDeployTaluka = mDeployTaluka.getName();
                            selectedDeployTalukaId = mDeployTaluka.get_id();
                            break;
                        }
                    }
                    if (selectedDeployTalukaId != null) {
                        structureMachineListFragmentPresenter.terminateSubmitMou(
                                filteredMachineListData.get(position).getId(),
                                filteredMachineListData.get(position).getMachineCode(),
                                Constants.SSModule.MACHINE_AVAILABLE_STATUS_CODE,
                                selectedDeployTalukaId);
                    } else {
                        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                        .findViewById(android.R.id.content), "Please select Taluka for machine deployment.",
                                Snackbar.LENGTH_LONG);
                    }
                } else if (mouAction == 2) {
                    if (editTerminateReason.getText().toString() != null && editTerminateReason.getText().toString().length() > 0) {
                        structureMachineListFragmentPresenter.terminateSubmitMou(
                                filteredMachineListData.get(position).getId(),
                                filteredMachineListData.get(position).getMachineCode(),
                                Constants.SSModule.MACHINE_MOU_TERMINATED_STATUS_CODE,
                                editTerminateReason.getText().toString());
                    } else {
                        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                        .findViewById(android.R.id.content), "Please provide reason for machine Mou termination.",
                                Snackbar.LENGTH_LONG);
                    }
                }
                dialog.dismiss();
            } else {
                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                .findViewById(android.R.id.content), "Please select action.",
                        Snackbar.LENGTH_LONG);
            }
        });

        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewType == 1) {
            if (Util.isConnected(getActivity())) {
                if (Util.getUserObjectFromPref().getRoleCode() == Constants.SSModule.ROLE_CODE_SS_HO_OPS) {
                    //State vise
                    structureMachineListFragmentPresenter.getStrucuresList(
                            Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getId(), "", "");
                } else if (Util.getUserObjectFromPref().getRoleCode() == Constants.SSModule.ROLE_CODE_SS_DM) {
                    //District vise
                    structureMachineListFragmentPresenter.getStrucuresList(
                            Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getId(),
                            Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getId(),
                            "");
                } else if (Util.getUserObjectFromPref().getRoleCode() == Constants.SSModule.ROLE_CODE_SS_TC) {
                    //Taluka vise
                    structureMachineListFragmentPresenter.getStrucuresList(
                            Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getId(),
                            Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getId(),
                            Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getId());
                }
            } else {
                Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
            }
        } else {
            if (Util.isConnected(getActivity())) {
                if (Util.getUserObjectFromPref().getRoleCode() == Constants.SSModule.ROLE_CODE_SS_HO_OPS) {
                    structureMachineListFragmentPresenter.getStateMachinesList(Util.getUserObjectFromPref().
                            getUserLocation().getStateId().get(0).getId());
                } else if (Util.getUserObjectFromPref().getRoleCode() == Constants.SSModule.ROLE_CODE_SS_DM) {
                    structureMachineListFragmentPresenter.getDistrictMachinesList(Util.getUserObjectFromPref().
                                    getUserLocation().getStateId().get(0).getId(),
                            Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getId());
                } else if (Util.getUserObjectFromPref().getRoleCode() == Constants.SSModule.ROLE_CODE_SS_TC) {
                    structureMachineListFragmentPresenter.getTalukaMachinesList(Util.getUserObjectFromPref().
                                    getUserLocation().getStateId().get(0).getId(),
                            Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getId(),
                            Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getId());
                }
            } else {
                Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
            }
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
        Util.showToast(message,this);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        Util.showToast(error.getMessage(),this);
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

    public void populateMachineData(String requestID, MachineListAPIResponse machineListData) {
        tvNoData.setVisibility(View.GONE);
        ssMachineListData.clear();
        filteredMachineListData.clear();
        if (machineListData != null) {
            if (requestID.equals(StructureMachineListFragmentPresenter.GET_MACHINE_LIST)) {
                for (MachineData machineData : machineListData.getData()) {
                    if (machineData != null) {
                        ssMachineListData.add(machineData);
                    }
                }
                filteredMachineListData.addAll(ssMachineListData);
                rvDataList.setAdapter(ssMachineListAdapter);
                ssMachineListAdapter.notifyDataSetChanged();
                ((SSActionsActivity)context).setActivityTitle("Machine List("+filteredMachineListData.size()+")");
            }
        }
    }


    public void populateStructureData(String requestID, StructureListAPIResponse structureListData) {
        tvNoData.setVisibility(View.GONE);
        if (structureListData != null) {
//            if (requestID.equals(StructureMachineListFragmentPresenter.GET_MACHINE_LIST)) {
            ssStructureListData.clear();
             ArrayList<StructureData> offlineStructureListData = new ArrayList<StructureData>();
            offlineStructureListData.addAll(DatabaseManager.getDBInstance(Platform.getInstance()).getStructureDataDao().getAllStructure());

            filteredStructureListData.clear();
            for (StructureData structureData : structureListData.getData()) {
                boolean flag= false;
                for(StructureData obj : offlineStructureListData){
                    if(obj.getStructureCode().equalsIgnoreCase(structureData.getStructureCode())){
                        flag=true;
                        break;
                    }
                }
                if(flag){
                    structureData.setSavedOffine(true);
                    ssStructureListData.add(structureData);
                } else {
                    ssStructureListData.add(structureData);
                }
            }
            filteredStructureListData.addAll(ssStructureListData);
//            rvDataList.setAdapter(ssStructureListAdapter);
            ssStructureListAdapter.notifyDataSetChanged();
            ((SSActionsActivity)context).setActivityTitle("Structure List("+filteredStructureListData.size()+")");
//            }
        }
    }

    public void showNoDataMessage() {
        tvNoData.setVisibility(View.VISIBLE);
    }

    public void showJurisdictionLevel(List<JurisdictionLocation> jurisdictionLevels, String levelName) {
        switch (levelName) {
            case Constants.JurisdictionLevelName.TALUKA_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    machineTalukaList.clear();
                    Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getTaluka().getName().
                            compareTo(j2.getTaluka().getName()));

                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
                        if (tvDistrictFilter.getText().toString().equalsIgnoreCase(location.getDistrict().getName())) {
                            CustomSpinnerObject talukaList = new CustomSpinnerObject();
                            talukaList.set_id(location.getTalukaId());
                            talukaList.setName(location.getTaluka().getName());
                            talukaList.setSelected(false);
                            machineTalukaList.add(talukaList);
                        }
                    }
                }
                if(!isTalukaApiFirstCall) {
                    CustomSpinnerDialogClass cddTaluka = new CustomSpinnerDialogClass(getActivity(),
                            this, "Select Taluka", machineTalukaList,
                            false);
                    cddTaluka.show();
                    cddTaluka.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                }

                break;
            case Constants.JurisdictionLevelName.DISTRICT_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    machineDistrictList.clear();
                    Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getDistrict().getName().
                            compareTo(j2.getDistrict().getName()));

                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
                        if (tvStateFilter.getText().toString().equalsIgnoreCase(location.getState().getName())) {
                            CustomSpinnerObject districtList = new CustomSpinnerObject();
                            districtList.set_id(location.getDistrictId());
                            districtList.setName(location.getDistrict().getName());
                            districtList.setSelected(false);
                            machineDistrictList.add(districtList);
                        }
                    }
                }
                CustomSpinnerDialogClass cddDistrict = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select District", machineDistrictList,
                        false);
                cddDistrict.show();
                cddDistrict.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);

                break;
            default:
                break;
        }
    }

    public void showResponse(String responseStatus, int status) {
        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                        .findViewById(android.R.id.content), responseStatus,
                Snackbar.LENGTH_LONG);
        if (status == 200) {
            if (Util.getUserObjectFromPref().getRoleCode() == Constants.SSModule.ROLE_CODE_SS_HO_OPS) {
                structureMachineListFragmentPresenter.getStateMachinesList(Util.getUserObjectFromPref().
                        getUserLocation().getStateId().get(0).getId());
            } else if (Util.getUserObjectFromPref().getRoleCode() == Constants.SSModule.ROLE_CODE_SS_DM) {
                structureMachineListFragmentPresenter.getDistrictMachinesList(Util.getUserObjectFromPref().
                                getUserLocation().getStateId().get(0).getId(),
                        Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getId());
            } else if (Util.getUserObjectFromPref().getRoleCode() == Constants.SSModule.ROLE_CODE_SS_TC) {
                structureMachineListFragmentPresenter.getTalukaMachinesList(Util.getUserObjectFromPref().
                                getUserLocation().getStateId().get(0).getId(),
                        Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getId(),
                        Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getId());
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_taluka_filter) {
            if (Util.isConnected(getActivity())) {
                if (tvDistrictFilter.getText() != null && tvDistrictFilter.getText().toString().length() > 0) {
                    UserInfo userInfo = Util.getUserObjectFromPref();
                    isTalukaApiFirstCall = false;
                    structureMachineListFragmentPresenter.getJurisdictionLevelData(userInfo.getOrgId(),
                            Util.getUserObjectFromPref().getJurisdictionTypeId(),
                            Constants.JurisdictionLevelName.TALUKA_LEVEL);
                } else {
                    Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "Your District is not available in your profile." +
                                    "Please update your profile.",
                            Snackbar.LENGTH_LONG);
                }
            } else {
                Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
            }
        } else if (view.getId() == R.id.tv_district_filter) {
            if (Util.isConnected(getActivity())) {
                if (tvStateFilter.getText() != null && tvStateFilter.getText().toString().length() > 0) {
                    UserInfo userInfo = Util.getUserObjectFromPref();
                    structureMachineListFragmentPresenter.getJurisdictionLevelData(userInfo.getOrgId(),
                            Util.getUserObjectFromPref().getJurisdictionTypeId(),//5c4ab05cd503a372d0391467
                            Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                } else {
                    Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "Your State is not available in your profile." +
                                    "Please update your profile.",
                            Snackbar.LENGTH_LONG);
                }
            } else {
                Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
            }
        } else if(view.getId() == R.id.btn_filter_clear) {
            if (viewType == 1) {
                filteredStructureListData.clear();
                filteredStructureListData.addAll(ssStructureListData);
                ssStructureListAdapter.notifyDataSetChanged();
                ((SSActionsActivity)context).setActivityTitle("Structure List("+filteredStructureListData.size()+")");
            } else {
                filteredMachineListData.clear();
                filteredMachineListData.addAll(ssMachineListData);
                rvDataList.setAdapter(ssMachineListAdapter);
                ssMachineListAdapter.notifyDataSetChanged();
                ((SSActionsActivity)context).setActivityTitle("Machine List("+filteredMachineListData.size()+")");
            }
            if (Util.getUserObjectFromPref().getUserLocation().getStateId() != null &&
                    Util.getUserObjectFromPref().getUserLocation().getStateId().size() > 0) {
                tvStateFilter.setText("");
                tvStateFilter.setText(Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getName());
            }
            if (Util.getUserObjectFromPref().getUserLocation().getDistrictIds() != null &&
                    Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size() > 0) {
                tvDistrictFilter.setText("");
                tvDistrictFilter.setText(Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getName());
            }
            if (Util.getUserObjectFromPref().getUserLocation().getTalukaIds() != null &&
                    Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size() > 0) {
                tvTalukaFilter.setText(Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getName());
                tvTalukaFilter.setText("");
            }
            btnFilterClear.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        if (type.equals("Select Taluka")) {
            for (CustomSpinnerObject mTaluka : machineTalukaList) {
                if (mTaluka.isSelected()) {
                    selectedTaluka = mTaluka.getName();
                    selectedTalukaId = mTaluka.get_id();
                    break;
                }
            }

            tvTalukaFilter.setText(selectedTaluka);
            if (viewType == 1) {
                filteredStructureListData.clear();
                for (StructureData data : ssStructureListData) {
                    if (data.getTalukaId().equalsIgnoreCase(selectedTalukaId)) {
                        filteredStructureListData.add(data);
                    }
                }
                ssStructureListAdapter.notifyDataSetChanged();
                ((SSActionsActivity)context).setActivityTitle("Structure List("+filteredStructureListData.size()+")");
            } else {
                filteredMachineListData.clear();
                for (MachineData machineData : ssMachineListData) {
                    if (machineData.getTalukaId().equalsIgnoreCase(selectedTalukaId)) {
                        filteredMachineListData.add(machineData);
                    }
                }
                rvDataList.setAdapter(ssMachineListAdapter);
                ssMachineListAdapter.notifyDataSetChanged();
                ((SSActionsActivity)context).setActivityTitle("Machine List("+filteredMachineListData.size()+")");
            }
            btnFilterClear.setVisibility(View.VISIBLE);
        } else if (type.equals("Select District")) {
            for (CustomSpinnerObject mDistrict : machineDistrictList) {
                if (mDistrict.isSelected()) {
                    selectedDistrict = mDistrict.getName();
                    selectedDistrictId = mDistrict.get_id();
                    break;
                }
            }
            tvDistrictFilter.setText(selectedDistrict);
            if (viewType == 1) {
                filteredStructureListData.clear();
                for (StructureData data : ssStructureListData) {
                    if (data.getDistrictId().equalsIgnoreCase(selectedDistrictId)) {
                        filteredStructureListData.add(data);
                    }
                }
                ssStructureListAdapter.notifyDataSetChanged();
                ((SSActionsActivity)context).setActivityTitle("Structure List("+filteredStructureListData.size()+")");
            } else {
                filteredMachineListData.clear();
                for (MachineData machineData : ssMachineListData) {
                    if (machineData.getDistrictId().equalsIgnoreCase(selectedDistrictId)) {
                        filteredMachineListData.add(machineData);
                    }
                }
                rvDataList.setAdapter(ssMachineListAdapter);
                ssMachineListAdapter.notifyDataSetChanged();
                ((SSActionsActivity)context).setActivityTitle("Machine List("+filteredMachineListData.size()+")");
            }
            btnFilterClear.setVisibility(View.VISIBLE);
        }
    }
}
