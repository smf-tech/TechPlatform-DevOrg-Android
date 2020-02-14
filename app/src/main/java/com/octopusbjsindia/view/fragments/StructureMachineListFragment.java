package com.octopusbjsindia.view.fragments;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.SujalamSuphalam.MachineData;
import com.octopusbjsindia.models.SujalamSuphalam.MachineListAPIResponse;
import com.octopusbjsindia.models.SujalamSuphalam.StructureData;
import com.octopusbjsindia.models.SujalamSuphalam.StructureListAPIResponse;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.home.RoleAccessAPIResponse;
import com.octopusbjsindia.models.home.RoleAccessList;
import com.octopusbjsindia.models.home.RoleAccessObject;
import com.octopusbjsindia.models.profile.JurisdictionLocation;
import com.octopusbjsindia.models.profile.JurisdictionType;
import com.octopusbjsindia.presenter.StructureMachineListFragmentPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.CreateStructureActivity;
import com.octopusbjsindia.view.activities.MachineMouActivity;
import com.octopusbjsindia.view.activities.SSActionsActivity;
import com.octopusbjsindia.view.adapters.MutiselectDialogAdapter;
import com.octopusbjsindia.view.adapters.SSMachineListAdapter;
import com.octopusbjsindia.view.adapters.SSStructureListAdapter;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StructureMachineListFragment extends Fragment implements APIDataListener, View.OnClickListener, CustomSpinnerListener {
    private View structureMachineListFragmentView;
    private int viewType;
    private Context context;
    private RecyclerView rvDataList;
    private final ArrayList<MachineData> ssMachineListData = new ArrayList<>();
    private final ArrayList<MachineData> filteredMachineListData = new ArrayList<>();
    private final ArrayList<MachineData> tempMachineListData = new ArrayList<>();
    private final ArrayList<StructureData> ssStructureListData = new ArrayList<>();
    private final ArrayList<StructureData> filteredStructureListData = new ArrayList<>();
    private final ArrayList<StructureData> tempStructureListData = new ArrayList<>();

    private SSMachineListAdapter ssMachineListAdapter;
    private SSStructureListAdapter ssStructureListAdapter;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private StructureMachineListFragmentPresenter structureMachineListFragmentPresenter;
    private TextView tvStateFilter, tvDistrictFilter, tvTalukaFilter;
    private ArrayList<CustomSpinnerObject> machineStateList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> machineDistrictList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> machineTalukaList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> machineTalukaDeployList = new ArrayList<>();
    //    private String selectedStateId, selectedDistrict, selectedDistrictId, selectedTaluka, selectedTalukaId,
//            selectedDeployTaluka, selectedDeployTalukaId;
    private String selectedDeployTalukaId;
    private ArrayList<CustomSpinnerObject> statusList = new ArrayList<>();
    private int mouAction = 0, selectedStatus = 0, shiftAction = 0;
    public boolean isMachineTerminate, isMachineAvailable;
    public boolean isMachineAdd, isOperatorAdd, isMachineDepoly, isMachineEligible, isMachineMou,
            isMachineVisitValidationForm, isSiltTransportForm, isDieselRecordForm, isMachineShiftForm,
            isMachineRelease, isMouImagesUpload, isMachineSignoff, isStateFilter, isDistrictFilter, isTalukaFilter,
            isVillageFilter, isStructureAdd, isRealiseOperator, isAssignOperator;
    private FloatingActionButton fbSelect, fbCreate, fbCreateOperator;
    private boolean isTalukaApiFirstCall;
    private ImageView btnFilterClear;
    private String userStates = "", userStateIds = "", userDistricts = "", userDistrictIds = "",
            userTalukas = "", userTalukaIds = "";
    private boolean isFABOpen = false;

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
        selectedStatus = bundle.getInt("selectedStatus");
        init();
    }

    private void init() {
        progressBarLayout = structureMachineListFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = structureMachineListFragmentView.findViewById(R.id.pb_profile_act);
        fbSelect = structureMachineListFragmentView.findViewById(R.id.fb_select);
        fbSelect.setOnClickListener(this);
        fbCreate = structureMachineListFragmentView.findViewById(R.id.fb_create);
        fbCreate.setOnClickListener(this);
        fbCreateOperator = structureMachineListFragmentView.findViewById(R.id.fb_create_operator);
        fbCreateOperator.setOnClickListener(this);
        tvStateFilter = structureMachineListFragmentView.findViewById(R.id.tv_state_filter);
        tvDistrictFilter = structureMachineListFragmentView.findViewById(R.id.tv_district_filter);
        tvTalukaFilter = structureMachineListFragmentView.findViewById(R.id.tv_taluka_filter);
        btnFilterClear = structureMachineListFragmentView.findViewById(R.id.btn_filter_clear);
        btnFilterClear.setOnClickListener(this);
        setUserLocation();
        RoleAccessAPIResponse roleAccessAPIResponse = Util.getRoleAccessObjectFromPref();
        RoleAccessList roleAccessList = roleAccessAPIResponse.getData();
        if (roleAccessList != null) {
            List<RoleAccessObject> roleAccessObjectList = roleAccessList.getRoleAccess();
            for (RoleAccessObject roleAccessObject : roleAccessObjectList) {
                if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_ADD_MACHINE)) {
                    isMachineAdd = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_ADD_STRUCTURE)) {
                    isStructureAdd = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_ADD_OPERATOR)) {
                    isOperatorAdd = true;
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
                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_ELIGIBLE_MACHINE)) {
                    isMachineEligible = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_MOU_MACHINE)) {
                    isMachineMou = true;
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
                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_MACHINE_MOU_UPLOAD)) {
                    isMouImagesUpload = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_MACHINE_SIGN_OFF)) {
                    isMachineSignoff = true;
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
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_REALISE_OPERATOR)) {
                    isRealiseOperator = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_ASSIGN_OPERATOR)) {
                    isAssignOperator = true;
                }
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
        } else {
            if (Util.getUserObjectFromPref().getUserLocation().getStateId().size() > 1) {
                tvStateFilter.setOnClickListener(this);
                machineStateList.clear();
                for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getStateId().size(); i++) {
                    CustomSpinnerObject customState = new CustomSpinnerObject();
                    customState.set_id(Util.getUserObjectFromPref().getUserLocation().getStateId().get(i).getId());
                    customState.setName(Util.getUserObjectFromPref().getUserLocation().getStateId().get(i).getName());
                    machineStateList.add(customState);
                }
            }
        }
        if (isDistrictFilter) {
            tvDistrictFilter.setOnClickListener(this);
        } else {
            if (Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size() > 1) {
                tvDistrictFilter.setOnClickListener(this);
                machineDistrictList.clear();
                for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size(); i++) {
                    CustomSpinnerObject customDistrict = new CustomSpinnerObject();
                    customDistrict.set_id(Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(i).getId());
                    customDistrict.setName(Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(i).getName());
                    machineDistrictList.add(customDistrict);
                }
            }
        }
        if (isTalukaFilter) {
            tvTalukaFilter.setOnClickListener(this);
        } else {
            if (Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size() > 1) {
                tvTalukaFilter.setOnClickListener(this);
                machineTalukaList.clear();
                for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size(); i++) {
                    CustomSpinnerObject customTaluka = new CustomSpinnerObject();
                    customTaluka.set_id(Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(i).getId());
                    customTaluka.setName(Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(i).getName());
                    machineTalukaList.add(customTaluka);
                }
            }
        }
        if (viewType == 1) {
            if (isStructureAdd || isOperatorAdd) {
                fbSelect.setVisibility(View.VISIBLE);
            } else {
                fbSelect.setVisibility(View.GONE);
            }
        } else {
            if (isMachineAdd || isOperatorAdd) {
                fbSelect.setVisibility(View.VISIBLE);
            } else {
                fbSelect.setVisibility(View.INVISIBLE);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rvDataList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy > 0) {
                        fbSelect.hide();
                        fbCreate.hide();
                        fbCreateOperator.hide();
                    } else {
                        fbSelect.show();
                        if (isOperatorAdd) {
                            fbCreateOperator.show();
                        }
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
                isTalukaApiFirstCall = true;
                structureMachineListFragmentPresenter.getLocationData(userDistrictIds,
                        Util.getUserObjectFromPref().getJurisdictionTypeId(),
                        Constants.JurisdictionLevelName.TALUKA_LEVEL);
            }
        }
    }

    private void setUserLocation() {
        if (Util.getUserObjectFromPref().getUserLocation().getStateId() != null &&
                Util.getUserObjectFromPref().getUserLocation().getStateId().size() > 0) {
            userStates = "";
            userStateIds = "";
            for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getStateId().size(); i++) {
                JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getStateId().get(i);
                if (i == 0) {
                    userStates = j.getName();
                    userStateIds = j.getId();
                } else {
                    userStates = userStates + "," + j.getName();
                    userStateIds = userStateIds + "," + j.getId();
                }
            }
            tvStateFilter.setText(userStates);

        } else {
            tvStateFilter.setText("");
        }

        if (Util.getUserObjectFromPref().getUserLocation().getDistrictIds() != null &&
                Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size() > 0) {
            userDistricts = "";
            userDistrictIds = "";
            for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size(); i++) {
                JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(i);
                if (i == 0) {
                    userDistricts = j.getName();
                    userDistrictIds = j.getId();
                } else {
                    userDistricts = userDistricts + "," + j.getName();
                    userDistrictIds = userDistrictIds + "," + j.getId();
                }
            }
            tvDistrictFilter.setText(userDistricts);
        } else {
            tvDistrictFilter.setText("");
        }

        if (Util.getUserObjectFromPref().getUserLocation().getTalukaIds() != null &&
                Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size() > 0) {
            userTalukas = "";
            userTalukaIds = "";
            for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size(); i++) {
                JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(i);
                if (i == 0) {
                    userTalukas = j.getName();
                    userTalukaIds = j.getId();
                } else {
                    userTalukas = userTalukas + "," + j.getName();
                    userTalukaIds = userTalukaIds + "," + j.getId();
                }
            }
            tvTalukaFilter.setText(userTalukas);
        } else {
            tvTalukaFilter.setText("");
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
//            intent.putExtra("machineId", filteredMachineListData.get(position).getId());
//            intent.putExtra("machineCode", filteredMachineListData.get(position).getMachineCode());
            intent.putExtra("machine", filteredMachineListData.get(position));
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

    public void releaseOperator(int position) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogs_leave_layout);

        TextView title = dialog.findViewById(R.id.tv_dialog_title);
        title.setText(R.string.alert);
        title.setVisibility(View.VISIBLE);

        TextView text = dialog.findViewById(R.id.tv_dialog_subtext);
        text.setText(R.string.release_opretor_alert_message);
        text.setVisibility(View.VISIBLE);

        Button button = dialog.findViewById(R.id.btn_dialog);
        button.setText(R.string.ok);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(v -> {
            // Close dialog
            structureMachineListFragmentPresenter.releaceOperator(filteredMachineListData.get(position).getOperatorId());
            dialog.dismiss();
        });

        Button button1 = dialog.findViewById(R.id.btn_dialog_1);
        button1.setText(R.string.cancel);
        button1.setVisibility(View.VISIBLE);
        button1.setOnClickListener(v -> {
            // Close dialog
            dialog.dismiss();
        });

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void shiftMachine(int position) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_shift_machine_action_layout);
        RadioGroup rgShiftAction = dialog.findViewById(R.id.rg_shift_action);
        RadioButton rbShiftToStructure, rbShiftToIdeal;
        rbShiftToStructure = dialog.findViewById(R.id.rb_shift_to_structure);
        rbShiftToIdeal = dialog.findViewById(R.id.rb_shift_to_ideal);
        Button btnSubmit = dialog.findViewById(R.id.btn_submit);
        rgShiftAction.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_shift_to_structure:
                        shiftAction = 1;
                        break;
                    case R.id.rb_shift_to_ideal:
                        shiftAction = 2;
                        break;
                }
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shiftAction != 0) {
                    if (shiftAction == 1) {
                        Intent intent = new Intent(getActivity(), SSActionsActivity.class);
                        intent.putExtra("SwitchToFragment", "MachineDeployStructureListFragment");
                        intent.putExtra("title", "Select Structure");
                        intent.putExtra("type", "shiftMachine");
//                        intent.putExtra("machineId", filteredMachineListData.get(position).getId());
//                        intent.putExtra("machineCode", filteredMachineListData.get(position).getMachineCode());
//                        intent.putExtra("currentStructureId", filteredMachineListData.get(position).getDeployedStrutureId());
                        intent.putExtra("machine", filteredMachineListData.get(position));
                        getActivity().startActivity(intent);
                    } else if (shiftAction == 2) {
                        structureMachineListFragmentPresenter.updateMachineStatusToAvailable
                                (filteredMachineListData.get(position).getId(),
                                        filteredMachineListData.get(position).getDeployedStrutureId(),
                                        Constants.SSModule.MACHINE_AVAILABLE_STATUS_CODE);
                    }
                    dialog.dismiss();
                } else {
                    Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "Please select action.",
                            Snackbar.LENGTH_LONG);
                }
            }
        });
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    public void sendMachineSignOff(int position) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogs_leave_layout);

        TextView title = dialog.findViewById(R.id.tv_dialog_title);
        title.setText("Sujalam Suphalam");
        title.setVisibility(View.VISIBLE);

        TextView text = dialog.findViewById(R.id.tv_dialog_subtext);
        text.setText(R.string.machine_signoff_alert_message);
        text.setVisibility(View.VISIBLE);

        Button button = dialog.findViewById(R.id.btn_dialog);
        button.setText("Sign-off");
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(v -> {
            // Close dialog
            structureMachineListFragmentPresenter.sendMachineSignOff(filteredMachineListData.get(position).getId());
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
        title.setVisibility(View.VISIBLE);
        TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(v -> {
            for (CustomSpinnerObject mDeployTaluka : machineTalukaDeployList) {
                if (mDeployTaluka.isSelected()) {
                    mDeployTaluka.setSelected(false);
                    break;
                }
            }
            selectedDeployTalukaId = "";
            dialog.dismiss();
        });
        TextView tvSubmit = dialog.findViewById(R.id.tv_submit);
        tvSubmit.setVisibility(View.VISIBLE);
        tvSubmit.setOnClickListener(v -> {
            if (mouAction != 0) {
                if (mouAction == 1) {
                    for (CustomSpinnerObject mDeployTaluka : machineTalukaDeployList) {
                        if (mDeployTaluka.isSelected()) {
                            //selectedDeployTaluka = mDeployTaluka.getName();
                            selectedDeployTalukaId = mDeployTaluka.get_id();
                            break;
                        }
                    }
                    if (selectedDeployTalukaId != null && selectedDeployTalukaId != "") {
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
        if (Util.isConnected(getActivity())) {
            if (viewType == 1) {
                structureMachineListFragmentPresenter.getStrucuresList(
                        (userStateIds != "") ? userStateIds : "",
                        (userDistrictIds != "") ? userDistrictIds : "",
                        (userTalukaIds != "") ? userTalukaIds : "");
            } else {
                structureMachineListFragmentPresenter.getTalukaMachinesList(
                        (userStateIds != "") ? userStateIds : "",
                        (userDistrictIds != "") ? userDistrictIds : "",
                        (userTalukaIds != "") ? userTalukaIds : "");
            }
        } else {
            Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
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
        Util.showToast(message, this);
        showNoDataMessage();
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        Util.showToast(error.getMessage(), this);
        showNoDataMessage();
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

        ssMachineListData.clear();
        filteredMachineListData.clear();
        if (machineListData != null) {
            if (requestID.equals(StructureMachineListFragmentPresenter.GET_MACHINE_LIST)) {
                ssMachineListData.addAll(machineListData.getData());
                if (selectedStatus != 0) {
                    for (MachineData machineData : ssMachineListData) {
                        if (machineData.getStatusCode() == selectedStatus) {
                            filteredMachineListData.add(machineData);
                        }
                    }
                } else {
                    filteredMachineListData.addAll(ssMachineListData);
                }
                rvDataList.setAdapter(ssMachineListAdapter);
                ssMachineListAdapter.notifyDataSetChanged();
                ((SSActionsActivity) context).setTitle("Machine List (" + filteredMachineListData.size() + ")");
            }
        }
        showNoDataMessage();
    }


    public void populateStructureData(String requestID, StructureListAPIResponse structureListData) {

        if (structureListData != null) {
            ssStructureListData.clear();
            ArrayList<StructureData> offlineStructureListData = new ArrayList<StructureData>();
            offlineStructureListData.addAll(DatabaseManager.getDBInstance(Platform.getInstance()).getStructureDataDao().getAllStructure());

            filteredStructureListData.clear();
            for (StructureData structureData : structureListData.getData()) {
                boolean flag = false;
                for (StructureData obj : offlineStructureListData) {
                    if (obj.getStructureCode().equalsIgnoreCase(structureData.getStructureCode())) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    structureData.setSavedOffine(true);
                    ssStructureListData.add(structureData);
                } else {
                    ssStructureListData.add(structureData);
                }
            }
            if (selectedStatus != 0) {
                for (StructureData structureData : ssStructureListData) {
                    if (structureData.getStructureStatusCode() == selectedStatus) {
                        filteredStructureListData.add(structureData);
                    }
                }
            } else {
                filteredStructureListData.addAll(ssStructureListData);
            }
            ssStructureListAdapter.notifyDataSetChanged();
            ((SSActionsActivity) context).setTitle("Structure List(" + filteredStructureListData.size() + ")");
        }
        showNoDataMessage();
    }

    public void showNoDataMessage() {
        if (viewType == 1) {
            if (filteredStructureListData.size() > 0) {
                structureMachineListFragmentView.findViewById(R.id.ly_no_data).setVisibility(View.GONE);
            } else {
                structureMachineListFragmentView.findViewById(R.id.ly_no_data).setVisibility(View.VISIBLE);
            }
        } else {
            if (filteredMachineListData.size() > 0) {
                structureMachineListFragmentView.findViewById(R.id.ly_no_data).setVisibility(View.GONE);
            } else {
                structureMachineListFragmentView.findViewById(R.id.ly_no_data).setVisibility(View.VISIBLE);
            }
        }
    }

    public void showJurisdictionLevel(List<JurisdictionLocation> jurisdictionLevels, String levelName) {
        switch (levelName) {
            case Constants.JurisdictionLevelName.TALUKA_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    machineTalukaList.clear();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
                        CustomSpinnerObject talukaList = new CustomSpinnerObject();
                        talukaList.set_id(location.getId());
                        talukaList.setName(location.getName());
                        talukaList.setSelected(false);
                        machineTalukaList.add(talukaList);
                    }
                }
                if (!isTalukaApiFirstCall) {
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

                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
                        CustomSpinnerObject districtList = new CustomSpinnerObject();
                        districtList.set_id(location.getId());
                        districtList.setName(location.getName());
                        districtList.setSelected(false);
                        machineDistrictList.add(districtList);
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

    public void showResponse(String requestId, String responseStatus, int status) {
        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                        .findViewById(android.R.id.content), responseStatus,
                Snackbar.LENGTH_LONG);

        structureMachineListFragmentPresenter.getTalukaMachinesList(
                (userStateIds != "") ? userStateIds : "",
                (userDistrictIds != "") ? userDistrictIds : "",
                (userTalukaIds != "") ? userTalukaIds : "");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fb_select) {
            if (!isFABOpen) {
                showFABMenu();
            } else {
                closeFABMenu();
            }
        } else if (view.getId() == R.id.fb_create) {
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
        } else if (view.getId() == R.id.fb_create_operator) {
            Intent machineVisitIntent = new Intent(getActivity(), SSActionsActivity.class);
            machineVisitIntent.putExtra("SwitchToFragment", "CreateOperatorFragment");
            machineVisitIntent.putExtra("title", "Create Operator");
            getActivity().startActivity(machineVisitIntent);

        }
//        else if (view.getId() == R.id.tv_state_filter) {
//            CustomSpinnerDialogClass cdd = new CustomSpinnerDialogClass(getActivity(), this,
//                    "Select State",
//                    machineStateList,
//                    false);
//            cdd.show();
//            cdd.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.MATCH_PARENT);
//
//        }
        else if (view.getId() == R.id.tv_taluka_filter) {
            if (Util.isConnected(getActivity())) {
                if (tvDistrictFilter.getText() != null && tvDistrictFilter.getText().toString().length() > 0) {
                    isTalukaApiFirstCall = false;
                    structureMachineListFragmentPresenter.getLocationData(userDistrictIds,
                            Util.getUserObjectFromPref().getJurisdictionTypeId(),
                            Constants.JurisdictionLevelName.TALUKA_LEVEL);
                } else {
                    Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "Please select District first.",
                            Snackbar.LENGTH_LONG);
                }
            } else {
                Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
            }
        } else if (view.getId() == R.id.tv_district_filter) {
            if (Util.isConnected(getActivity())) {
                if (tvStateFilter.getText() != null && tvStateFilter.getText().toString().length() > 0) {
                    structureMachineListFragmentPresenter.getLocationData(userStateIds,
                            Util.getUserObjectFromPref().getJurisdictionTypeId(),
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
        } else if (view.getId() == R.id.btn_filter_clear) {
            if (viewType == 1) {
                filteredStructureListData.clear();
                filteredStructureListData.addAll(ssStructureListData);
                ssStructureListAdapter.notifyDataSetChanged();
                ((SSActionsActivity) context).setActivityTitle("Structure List(" + filteredStructureListData.size() + ")");
            } else {
                filteredMachineListData.clear();
                filteredMachineListData.addAll(ssMachineListData);
                rvDataList.setAdapter(ssMachineListAdapter);
                ssMachineListAdapter.notifyDataSetChanged();
                ((SSActionsActivity) context).setActivityTitle("Machine List(" + filteredMachineListData.size() + ")");
            }
            tvStateFilter.setText("");
            if (Util.getUserObjectFromPref().getUserLocation().getStateId() != null &&
                    Util.getUserObjectFromPref().getUserLocation().getStateId().size() > 0) {
                tvStateFilter.setText("");
                tvStateFilter.setText(Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getName());
            }
            tvDistrictFilter.setText("");
            if (Util.getUserObjectFromPref().getUserLocation().getDistrictIds() != null &&
                    Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size() > 0) {
                tvDistrictFilter.setText(Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getName());
            }
            tvTalukaFilter.setText("");
            if (Util.getUserObjectFromPref().getUserLocation().getTalukaIds() != null &&
                    Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size() > 0) {
                tvTalukaFilter.setText(Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getName());
            }
            btnFilterClear.setVisibility(View.GONE);
        }
    }

    private void showFABMenu() {
        isFABOpen = true;
        if (viewType == 1) {
            if (isStructureAdd) {
                fbCreate.animate().translationY(-getResources().getDimension(R.dimen.standard_60));
                if (isOperatorAdd) {
                    fbCreateOperator.animate().translationY(-getResources().getDimension(R.dimen.standard_120));
                }
            } else {
                if (isOperatorAdd) {
                    fbCreateOperator.animate().translationY(-getResources().getDimension(R.dimen.standard_60));
                }
            }
        } else {
            if (isMachineAdd) {
                fbCreate.animate().translationY(-getResources().getDimension(R.dimen.standard_60));
                if (isOperatorAdd) {
                    fbCreateOperator.animate().translationY(-getResources().getDimension(R.dimen.standard_120));
                }
            } else {
                if (isOperatorAdd) {
                    fbCreateOperator.animate().translationY(-getResources().getDimension(R.dimen.standard_60));
                }
            }
        }
        fbSelect.setRotation(45);
    }

    private void closeFABMenu() {
        isFABOpen = false;
        fbCreate.animate().translationY(0);
        fbCreateOperator.animate().translationY(0);
        fbSelect.setRotation(0);
    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        if (type.equals("Select State")) {

        } else if (type.equals("Select Taluka")) {
            String selectedTaluka = "";
            String selectedTalukaId = "";
            for (CustomSpinnerObject mTaluka : machineTalukaList) {
                if (mTaluka.isSelected()) {
                    selectedTaluka = mTaluka.getName();
                    selectedTalukaId = mTaluka.get_id();
                    break;
                }
            }
            if (!TextUtils.isEmpty(selectedTalukaId)) {
                tvTalukaFilter.setText(selectedTaluka);
                if (viewType == 1) {
                    filteredStructureListData.clear();
                    for (StructureData data : ssStructureListData) {
                        if (data.getTalukaId().equalsIgnoreCase(selectedTalukaId)) {
                            if (selectedStatus != 0) {
                                if (data.getStructureStatusCode() == selectedStatus) {
                                    filteredStructureListData.add(data);
                                }
                            } else {
                                filteredStructureListData.add(data);
                            }
                        }
                    }
                    ssStructureListAdapter.notifyDataSetChanged();
                    ((SSActionsActivity) context).setTitle("Structure List (" + filteredStructureListData.size() + ")");
                } else {
                    filteredMachineListData.clear();
                    for (MachineData machineData : ssMachineListData) {
                        if (machineData.getTalukaId().equalsIgnoreCase(selectedTalukaId)) {
                            if (selectedStatus != 0) {
                                if (machineData.getStatusCode() == selectedStatus) {
                                    filteredMachineListData.add(machineData);
                                }
                            } else {
                                filteredMachineListData.add(machineData);
                            }
                        }
                    }
                    rvDataList.setAdapter(ssMachineListAdapter);
                    ssMachineListAdapter.notifyDataSetChanged();
                    ((SSActionsActivity) context).setTitle("Machine List (" + filteredMachineListData.size() + ")");
                }
                btnFilterClear.setVisibility(View.VISIBLE);
            }
        } else if (type.equals("Select District")) {
            String selectedDistrict = "";
            String selectedDistrictId = "";
            for (CustomSpinnerObject mDistrict : machineDistrictList) {
                if (mDistrict.isSelected()) {
                    selectedDistrict = mDistrict.getName();
                    selectedDistrictId = mDistrict.get_id();
                    break;
                }
            }
            if (!TextUtils.isEmpty(selectedDistrictId)) {
                tvDistrictFilter.setText(selectedDistrict);
                if (viewType == 1) {
                    filteredStructureListData.clear();
                    for (StructureData data : ssStructureListData) {
                        if (data.getDistrictId().equalsIgnoreCase(selectedDistrictId)) {
                            if (selectedStatus != 0) {
                                if (data.getStructureStatusCode() == selectedStatus) {
                                    filteredStructureListData.add(data);
                                }
                            } else {
                                filteredStructureListData.add(data);
                            }
                        }
                    }
                    ssStructureListAdapter.notifyDataSetChanged();
                    ((SSActionsActivity) context).setTitle("Structure List (" + filteredStructureListData.size() + ")");
                } else {
                    filteredMachineListData.clear();
                    for (MachineData machineData : ssMachineListData) {
                        if (machineData.getDistrictId().equalsIgnoreCase(selectedDistrictId)) {
                            if (selectedStatus != 0) {
                                if (machineData.getStatusCode() == selectedStatus) {
                                    filteredMachineListData.add(machineData);
                                }
                            } else {
                                filteredMachineListData.add(machineData);
                            }
                        }
                    }
                    rvDataList.setAdapter(ssMachineListAdapter);
                    ssMachineListAdapter.notifyDataSetChanged();
                    ((SSActionsActivity) context).setTitle("Machine List (" + filteredMachineListData.size() + ")");
                }
                btnFilterClear.setVisibility(View.VISIBLE);
            }
        }
    }

//    private void setStructureStatusFilterList() {
//        // Machine Status list
//        statusList.clear();
//        CustomSpinnerObject status = new CustomSpinnerObject();
//        status.set_id("" + 000);
//        status.setName("Show All");
//        status.setSelected(false);
//        statusList.add(status);
//        CustomSpinnerObject status15 = new CustomSpinnerObject();
//        status15.set_id("" + 115);
//        status15.setName("Approved");
//        status15.setSelected(false);
//        statusList.add(status15);
//        CustomSpinnerObject status16 = new CustomSpinnerObject();
//        status16.set_id("" + 116);
//        status16.setName("Prepared");
//        status16.setSelected(false);
//        statusList.add(status16);
//        CustomSpinnerObject status17 = new CustomSpinnerObject();
//        status17.set_id("" + 117);
//        status17.setName("In progress");
//        status17.setSelected(false);
//        statusList.add(status17);
//        CustomSpinnerObject status18 = new CustomSpinnerObject();
//        status18.set_id("" + 118);
//        status18.setName("Halted");
//        status18.setSelected(false);
//        statusList.add(status18);
//        CustomSpinnerObject status19 = new CustomSpinnerObject();
//        status19.set_id("" + 119);
//        status19.setName("Partially Completed");
//        status19.setSelected(false);
//        statusList.add(status19);
//        CustomSpinnerObject status20 = new CustomSpinnerObject();
//        status20.set_id("" + 120);
//        status20.setName("Completed");
//        status20.setSelected(false);
//        statusList.add(status20);
//        CustomSpinnerObject status21 = new CustomSpinnerObject();
//        status21.set_id("" + 121);
//        status21.setName("Closed");
//        status21.setSelected(false);
//        statusList.add(status21);
//        CustomSpinnerObject status22 = new CustomSpinnerObject();
//        status22.set_id("" + 122);
//        status22.setName("Non Compliant");
//        status22.setSelected(false);
//        statusList.add(status22);
//        CustomSpinnerObject status23 = new CustomSpinnerObject();
//        status23.set_id("" + 119);
//        status23.setName("Partially Closed");
//        status23.setSelected(false);
//        statusList.add(status23);
//
//    }

//    private void setMachineStatusFilterList() {
//        // Machine Status list
//        statusList.clear();
//        CustomSpinnerObject status = new CustomSpinnerObject();
//        status.set_id("" + 000);
//        status.setName("Show All");
//        status.setSelected(false);
//        statusList.add(status);
//        CustomSpinnerObject status0 = new CustomSpinnerObject();
//        status0.set_id("" + 100);
//        status0.setName("Created");
//        status0.setSelected(false);
//        statusList.add(status0);
//        CustomSpinnerObject status1 = new CustomSpinnerObject();
//        status1.set_id("" + 101);
//        status1.setName("New");
//        status1.setSelected(false);
//        statusList.add(status1);
//        CustomSpinnerObject status2 = new CustomSpinnerObject();
//        status2.set_id("" + 102);
//        status2.setName("Eligible");
//        status2.setSelected(false);
//        statusList.add(status2);
//        CustomSpinnerObject status3 = new CustomSpinnerObject();
//        status3.set_id("" + 103);
//        status3.setName("Non Eligible ");
//        status3.setSelected(false);
//        statusList.add(status3);
//        CustomSpinnerObject status4 = new CustomSpinnerObject();
//        status4.set_id("" + 104);
//        status4.setName("MOU Done");
//        status4.setSelected(false);
//        statusList.add(status4);
//        CustomSpinnerObject status5 = new CustomSpinnerObject();
//        status5.set_id("" + 105);
//        status5.setName("MOU Terminated");
//        status5.setSelected(false);
//        statusList.add(status5);
//        CustomSpinnerObject status6 = new CustomSpinnerObject();
//        status6.set_id("" + 106);
//        status6.setName("Available");
//        status6.setSelected(false);
//        statusList.add(status6);
//        CustomSpinnerObject status7 = new CustomSpinnerObject();
//        status7.set_id("" + 107);
//        status7.setName("Deployed");
//        status7.setSelected(false);
//        statusList.add(status7);
//        CustomSpinnerObject status8 = new CustomSpinnerObject();
//        status8.set_id("" + 108);
//        status8.setName("Working");
//        status8.setSelected(false);
//        statusList.add(status8);
//        CustomSpinnerObject status9 = new CustomSpinnerObject();
//        status9.set_id("" + 109);
//        status9.setName("Break");
//        status9.setSelected(false);
//        statusList.add(status9);
//        CustomSpinnerObject status10 = new CustomSpinnerObject();
//        status10.set_id("" + 110);
//        status10.setName("Stopped");
//        status10.setSelected(false);
//        statusList.add(status10);
//        CustomSpinnerObject status11 = new CustomSpinnerObject();
//        status11.set_id("" + 111);
//        status11.setName("Halted");
//        status11.setSelected(false);
//        statusList.add(status11);
//        CustomSpinnerObject status12 = new CustomSpinnerObject();
//        status12.set_id("" + 112);
//        status12.setName("Start");
//        status12.setSelected(false);
//        statusList.add(status12);
//        CustomSpinnerObject status13 = new CustomSpinnerObject();
//        status13.set_id("" + 113);
//        status13.setName("Pause");
//        status13.setSelected(false);
//        statusList.add(status13);
//        CustomSpinnerObject status14 = new CustomSpinnerObject();
//        status14.set_id("" + 114);
//        status14.setName("MOU  Expired");
//        status14.setSelected(false);
//        statusList.add(status14);
//        CustomSpinnerObject status15 = new CustomSpinnerObject();
//        status15.set_id("" + 115);
//        status15.setName("Free From Taluka");
//        status15.setSelected(false);
//        statusList.add(status15);
//    }

}
