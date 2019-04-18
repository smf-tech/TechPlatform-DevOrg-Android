package com.platform.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.platform.R;
import com.platform.models.events.Member;
import com.platform.view.adapters.AddMembersListAdapter;

import java.util.ArrayList;

public class AddMembersListActivity extends AppCompatActivity {

    private AddMembersListAdapter addMembersListAdapter;
    private ArrayList<Member> membersList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members_list);
        initViews();
    }

    private void initViews() {
        membersList.add(new Member("1", "Sagar Mahajan", "DM",true));
        membersList.add(new Member("2", "Kishor Shevkar", "TC",false));
        membersList.add(new Member("3", "Jagruti Devare", "MT",true));
        membersList.add(new Member("4", "Sachin Kakade", "FA",false));

        RecyclerView rvMembers = findViewById(R.id.rv_members);
        addMembersListAdapter = new AddMembersListAdapter(AddMembersListActivity.this, membersList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvMembers.setLayoutManager(mLayoutManager);
        rvMembers.setAdapter(addMembersListAdapter);
    }
}
