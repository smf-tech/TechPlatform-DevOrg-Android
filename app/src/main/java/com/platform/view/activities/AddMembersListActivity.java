package com.platform.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.models.events.Participant;
import com.platform.utility.Constants;
import com.platform.view.adapters.AddMembersListAdapter;

import java.util.ArrayList;
import java.util.Locale;

public class AddMembersListActivity extends BaseActivity implements SearchView.OnQueryTextListener,
        View.OnClickListener {

    private AddMembersListAdapter addMembersListAdapter;
    private ArrayList<Participant> membersList = new ArrayList<>();
    private final ArrayList<Participant> filterMembersList = new ArrayList<>();
    private SearchView editSearch;
    private CheckBox cbSelectAllMembers;
    private ImageView toolbarAction;
    private ImageView ivBackIcon;
    private Button btAddMembers;
    private boolean isNewMembersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members_list);
        initViews();
    }

    private void initViews() {
        setActionbar(getResources().getString(R.string.task_add_members));
        cbSelectAllMembers = findViewById(R.id.cb_select_all_members);
        ivBackIcon = findViewById(R.id.toolbar_back_action);
        toolbarAction = findViewById(R.id.toolbar_edit_action);
        editSearch = findViewById(R.id.search_view);

        membersList = (ArrayList<Participant>) getIntent().getSerializableExtra(Constants.Planner.MEMBERS_LIST);
        boolean isNewMembersList = getIntent().getBooleanExtra(Constants.Planner.IS_NEW_MEMBERS_LIST, false);

        LinearLayout lyAttendedTab = findViewById(R.id.ly_attended_tab);
        TextView tvInfoLabel = findViewById(R.id.tv_info_label);
        btAddMembers=findViewById(R.id.bt_add_members);
        if (isNewMembersList) {
            toolbarAction.setVisibility(View.GONE);
            lyAttendedTab.setVisibility(View.GONE);
            tvInfoLabel.setVisibility(View.VISIBLE);
        } else {
            toolbarAction.setVisibility(View.VISIBLE);
            toolbarAction.setImageResource(R.drawable.ic_check_white);
            setActionbar(getResources().getString(R.string.mark_attendance));
            btAddMembers.setVisibility(View.GONE);
            tvInfoLabel.setVisibility(View.GONE);
            lyAttendedTab.setVisibility(View.VISIBLE);
            TextView tvAttended = findViewById(R.id.tv_attended);
            TextView tvNotAttended = findViewById(R.id.tv_not_attended);

            int attendedCount = 0;
            for (Participant m : membersList) {
                if (m.getAttended()) {
                    attendedCount++;
                }
            }

            tvAttended.setText(String.format(Locale.getDefault(), "%d%s", attendedCount,
                    getString(R.string.attended_label)));
            tvNotAttended.setText(String.format(Locale.getDefault(), "%d%s",
                    membersList.size() - attendedCount, getString(R.string.not_attended_label)));
        }

        filterMembersList.addAll(membersList);
        checkAllSelected(membersList);
        RecyclerView rvMembers = findViewById(R.id.rv_members);
        addMembersListAdapter = new AddMembersListAdapter(AddMembersListActivity.this,
                membersList, true, isNewMembersList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvMembers.setLayoutManager(mLayoutManager);
        rvMembers.setAdapter(addMembersListAdapter);
        setListeners();
    }

    private void setListeners() {
        ivBackIcon.setOnClickListener(this);
        cbSelectAllMembers.setOnClickListener(this);
        toolbarAction.setOnClickListener(this);
        editSearch.setOnQueryTextListener(this);
        btAddMembers.setOnClickListener(this);
    }

    public void checkAllSelected(ArrayList<Participant> membersList) {
        boolean allCheck = true;
        for (int i = 0; i < membersList.size(); i++) {
            if (membersList.get(i).getMemberSelected() != null && !membersList.get(i).getMemberSelected()) {
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
        membersList.clear();
        if (searchText.length() > 0) {
            for (Participant member : filterMembersList) {
                if (member.getName().toLowerCase(Locale.getDefault()).contains(searchText)) {
                    membersList.add(member);
                }
            }
        } else {
            membersList.addAll(filterMembersList);
        }

        addMembersListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back_action:
                finish();
                break;

            case R.id.toolbar_edit_action:
                //Submit attendance

                break;

            case R.id.bt_add_members:
                addMembersToEvent();
                break;

            case R.id.cb_select_all_members:
                if (((CheckBox) v).isChecked()) {
                    for (Participant participant : membersList) {
                        participant.setAttended(true);
                    }
                } else {
                    for (Participant participant : membersList) {
                        participant.setAttended(false);
                    }
                }
                addMembersListAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void addMembersToEvent() {
        ArrayList<Participant> list = new ArrayList<>();
        for(Participant p:filterMembersList){
            if(p.getMemberSelected()!= null && p.getMemberSelected()){
                list.add(p);
            }
        }
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.Planner.MEMBER_LIST_DATA, list);
        setResult(Constants.Planner.MEMBER_LIST, returnIntent);
        finish();
    }

    private void setActionbar(String title) {
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);
    }
}
