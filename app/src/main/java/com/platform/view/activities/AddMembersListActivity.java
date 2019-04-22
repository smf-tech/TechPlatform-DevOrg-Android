package com.platform.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.platform.R;
import com.platform.models.events.Member;
import com.platform.view.adapters.AddMembersListAdapter;

import java.util.ArrayList;
import java.util.Locale;

public class AddMembersListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, View.OnClickListener {

    private AddMembersListAdapter addMembersListAdapter;
    private ArrayList<Member> membersList = new ArrayList<>();
    private ArrayList<Member> filterMembersList = new ArrayList<>();
    SearchView editSearch;
    CheckBox cbSelectAllMembers;
    private ImageView ivBackIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members_list);
        initViews();
    }

    private void initViews() {
        cbSelectAllMembers = (CheckBox)findViewById(R.id.cb_select_all_members);
        ivBackIcon = findViewById(R.id.iv_back_icon);
        editSearch = (SearchView) findViewById(R.id.search_view);

        membersList.add(new Member("1", "Sagar Mahajan", "DM",true));
        membersList.add(new Member("2", "Kishor Shevkar", "TC",false));
        membersList.add(new Member("3", "Jagruti Devare", "MT",true));
        membersList.add(new Member("4", "Sachin Kakade", "FA",false));

        filterMembersList.addAll(membersList);
        checkAllSelected(membersList);
        RecyclerView rvMembers = findViewById(R.id.rv_members);
        addMembersListAdapter = new AddMembersListAdapter(AddMembersListActivity.this, membersList,true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvMembers.setLayoutManager(mLayoutManager);
        rvMembers.setAdapter(addMembersListAdapter);
        setListeners();
    }

    private void setListeners(){
        ivBackIcon.setOnClickListener(this);
        cbSelectAllMembers.setOnClickListener(this);
        editSearch.setOnQueryTextListener(this);
    }

    public void checkAllSelected(ArrayList<Member> membersList) {
        boolean allCheck = true;
        for (int i = 0; i < membersList.size(); i++) {
            if (!membersList.get(i).getMemberSelected()) {
                allCheck = false;
                break;
            }
        }
        if (allCheck)
            cbSelectAllMembers.setChecked(true);
        else
            cbSelectAllMembers.setChecked(false);
    }

    // uncheck chech all check box
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

    public void filter(String searchText) {
        searchText = searchText.toLowerCase(Locale.getDefault());
        membersList.clear();
        if(searchText.length()>0) {
            for (Member member : filterMembersList) {
                if (member.getName().toLowerCase(Locale.getDefault()).contains(searchText)) {
                    membersList.add(member);
                }
            }
        }else{
            membersList.addAll(filterMembersList);
        }
        addMembersListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_icon:
                finish();
                break;
            case R.id.cb_select_all_members:
                if (((CheckBox) v).isChecked()) {
                    if (editSearch.getQuery().toString().trim().length() == 0) {
                        for (Member member : membersList) {
                            member.setMemberSelected(true);
                        }
                    } else {
                        for (Member member : membersList) {
                            member.setMemberSelected(true);
                        }
                    }

                } else {
                    if (editSearch.getQuery().toString().trim().length() == 0) {
                        for (Member member  : membersList) {
                            member.setMemberSelected(false);
                        }
                    } else {
                        for (Member member : membersList) {
                            member.setMemberSelected(false);
                        }
                    }

                }
                addMembersListAdapter.notifyDataSetChanged();
                break;
        }
    }
}
