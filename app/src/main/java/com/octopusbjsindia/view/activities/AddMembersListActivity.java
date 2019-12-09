package com.octopusbjsindia.view.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.PlatformTaskListener;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.models.events.Participant;
import com.octopusbjsindia.presenter.AddMembersListPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.AddMembersListAdapter;

import java.util.ArrayList;
import java.util.Locale;

public class AddMembersListActivity extends BaseActivity implements SearchView.OnQueryTextListener,
        View.OnClickListener, PlatformTaskListener {

    private AddMembersListAdapter addMembersListAdapter;
    private ArrayList<Participant> filterMemberList = new ArrayList<>();
    private final ArrayList<Participant> membersList = new ArrayList<>();
    private String eventTaskID;
    private String userId;

    private SearchView editSearch;
    private CheckBox cbSelectAllMembers;
    private ImageView toolbarAction;
    private ImageView ivBackIcon;
    private TextView tvAddMembers;
    private Button btAddMembers,btMembersSubmit;
    private String toOpen;
    private boolean isCheckVisible;
    private boolean isDeleteVisible;
    private boolean isSearchVisible;

    private AddMembersListPresenter presenter;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members_list);
        initViews();
    }

    private void initViews() {
        setActionbar(getResources().getString(R.string.task_add_members));

        progressBarLayout = findViewById(R.id.profile_act_progress_bar);
        progressBar = findViewById(R.id.pb_profile_act);
        presenter = new  AddMembersListPresenter(this);

        isSearchVisible=false;

        cbSelectAllMembers = findViewById(R.id.cb_select_all_members);
        ivBackIcon = findViewById(R.id.toolbar_back_action);
        toolbarAction = findViewById(R.id.toolbar_edit_action);
        editSearch = findViewById(R.id.search_view);

        filterMemberList = (ArrayList<Participant>) getIntent().getSerializableExtra(Constants.Planner.MEMBERS_LIST);
        toOpen = getIntent().getStringExtra(Constants.Planner.TO_OPEN);
        eventTaskID = getIntent().getStringExtra(Constants.Planner.EVENT_TASK_ID);
        isCheckVisible = getIntent().getBooleanExtra(Constants.Planner.IS_NEW_MEMBERS_LIST, false);
        isDeleteVisible = getIntent().getBooleanExtra(Constants.Planner.IS_DELETE_VISIBLE, false);

        TextView tvInfoLabel = findViewById(R.id.tv_info_label);
        btAddMembers = findViewById(R.id.bt_add_members);
        btMembersSubmit = findViewById(R.id.bt_members_submit);
        tvAddMembers = findViewById(R.id.tv_add_members);

        if (isCheckVisible) {
            cbSelectAllMembers.setVisibility(View.VISIBLE);
            tvAddMembers.setVisibility(View.GONE);
            if(eventTaskID!=null && !eventTaskID.isEmpty()){
                btMembersSubmit.setVisibility(View.VISIBLE);
            } else {
                btAddMembers.setVisibility(View.VISIBLE);
            }
        } else {
            tvInfoLabel.setVisibility(View.GONE);
            cbSelectAllMembers.setVisibility(View.INVISIBLE);
            btAddMembers.setVisibility(View.INVISIBLE);
            if(!isDeleteVisible){
                tvAddMembers.setVisibility(View.GONE);
            }
        }

        if(filterMemberList!=null && filterMemberList.size()>0){
            membersList.addAll(filterMemberList);
            checkAllSelected(filterMemberList);
            RecyclerView rvMembers = findViewById(R.id.rv_members);
            addMembersListAdapter = new AddMembersListAdapter(AddMembersListActivity.this,
                    filterMemberList, isDeleteVisible, isCheckVisible);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            rvMembers.setLayoutManager(mLayoutManager);
            rvMembers.setAdapter(addMembersListAdapter);
        }

        setListeners();
    }

    private void setListeners() {
        ivBackIcon.setOnClickListener(this);
        cbSelectAllMembers.setOnClickListener(this);
        toolbarAction.setOnClickListener(this);
        editSearch.setOnQueryTextListener(this);
        btAddMembers.setOnClickListener(this);
        btMembersSubmit.setOnClickListener(this);
        tvAddMembers.setOnClickListener(this);
    }

    public void checkAllSelected(ArrayList<Participant> membersList) {
        boolean allCheck = true;
        for (int i = 0; i < membersList.size(); i++) {
            if (!membersList.get(i).isMemberSelected()) {
                allCheck = false;
                break;
            }
        }

        if (allCheck) {
            cbSelectAllMembers.setChecked(true);
        } else {
            cbSelectAllMembers.setChecked(false);
        }
    }

    // uncheck check all check box
    public void checkAllDeSelected() {
        cbSelectAllMembers.setChecked(false);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filter(newText);
        return false;
    }

    private void filter(String searchText) {
        searchText = searchText.toLowerCase(Locale.getDefault());

        if(filterMemberList!=null) {
            filterMemberList.clear();
            if (searchText.length() > 0 && membersList.size() > 0) {
                for (Participant member : membersList) {
                    if (member.getName().toLowerCase(Locale.getDefault()).contains(searchText)) {
                        filterMemberList.add(member);
                    }
                }
            } else {
                filterMemberList.addAll(membersList);
            }
            checkAllSelected(membersList);
            addMembersListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back_action:
               onBackPressed();
                break;

            case R.id.toolbar_edit_action:
                //Submit attendance
                if(isSearchVisible){
                    isSearchVisible=false;
                    editSearch.setVisibility(View.VISIBLE);
                    toolbarAction.setImageResource(R.drawable.ic_close);
                } else {
                    isSearchVisible=true;
                    editSearch.setVisibility(View.GONE);
                    toolbarAction.setImageResource(R.drawable.ic_search);
                }

                break;

            case R.id.bt_add_members:
                addMembersToEvent();
                break;

            case R.id.bt_members_submit:
                addSubmitMembers();
                break;

            case R.id.cb_select_all_members:
                if (((CheckBox) v).isChecked()) {
                    for (Participant participant : filterMemberList) {
                        participant.setMemberSelected(true);
                    }
                } else {
                    for (Participant participant : filterMemberList) {
                        participant.setMemberSelected(false);
                    }
                }
                addMembersListAdapter.notifyDataSetChanged();
                break;

            case R.id.tv_add_members:
                if (toOpen.equalsIgnoreCase(Constants.Planner.TASKS_LABEL)) {
                    presenter.taskMemberList();
                } else {
                    Intent intent = new Intent(this, AddMembersFilterActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                    intent.putExtra(Constants.Planner.EVENT_TASK_ID, eventTaskID);
                    intent.putExtra(Constants.Planner.MEMBERS_LIST, filterMemberList);
                    this.startActivity(intent);
                    finish();
                }

                break;
        }
    }

    private void addMembersToEvent() {
        ArrayList<Participant> list = new ArrayList<>();
        for(Participant p:membersList){
            if(p.isMemberSelected()){
                list.add(p);
            }
        }
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.Planner.MEMBER_LIST_DATA, list);
        returnIntent.putExtra(Constants.Planner.MEMBER_LIST_COUNT, list.size());
        setResult(Constants.Planner.MEMBER_LIST, returnIntent);
        finish();
    }

    public void addSubmitMembers(){
        ArrayList<Participant> list = new ArrayList<>();
        for(Participant p:membersList){
            if(p.isMemberSelected()){
                list.add(p);
            }
        }
        presenter.addMemberToEventTask(eventTaskID,list);
    }

    private void setActionbar(String title) {
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);
    }

    public void removeMember(String userId) {
        this.userId=userId;

            AlertDialog alertDialog = null;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete alert")
                    .setMessage(getString(R.string.sure_to_delete))
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                            presenter.deleteMember(userId,eventTaskID);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            dialog.dismiss();
                        }
                    });
            // Create the AlertDialog object and return it
            alertDialog = builder.create();
            alertDialog.show();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnIntent = new Intent();
        if(filterMemberList!=null){
            returnIntent.putExtra(Constants.Planner.MEMBER_LIST_COUNT, filterMemberList.size());
        } else {
            returnIntent.putExtra(Constants.Planner.MEMBER_LIST_COUNT, 0);
        }

        setResult(Constants.Planner.MEMBER_LIST, returnIntent);
        finish();
    }

    @Override
    public void showProgressBar() {
        runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public <T> void showNextScreen(T data) {
        addMembersToEvent();
    }

    @Override
    public void showErrorMessage(String result) {
        runOnUiThread(() -> Util.showToast(result, this));
//        Util.snackBarToShowMsg();
    }

    public void onMembersDeleted(CommonResponse response) {
        if (response != null && response.getStatus() ==200) {
            for(int i=0;i<filterMemberList.size();i++){
                if(filterMemberList.get(i).getId().equals(userId)){
                    filterMemberList.remove(i);
                    break;
                }
            }
            addMembersListAdapter.notifyDataSetChanged();
            showErrorMessage(response.getMessage());
        } else {
            showErrorMessage(response.getMessage());
        }
    }
    public void showMemberList(ArrayList<Participant> data) {
        for(int i=0;i<filterMemberList.size();i++){
            for(int j=0;j<data.size();j++){
                if(filterMemberList.get(i).getId().equals(data.get(j).getId())){
                    data.get(j).setMemberSelected(true);
                    break;
                }
            }
        }
        Intent intent = new Intent(this, AddMembersListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        intent.putExtra(Constants.Planner.IS_NEW_MEMBERS_LIST, true);
        intent.putExtra(Constants.Planner.IS_DELETE_VISIBLE, false);
        intent.putExtra(Constants.Planner.EVENT_TASK_ID, eventTaskID);
        intent.putExtra(Constants.Planner.MEMBERS_LIST, data);
        this.startActivity(intent);
        finish();
    }
}
