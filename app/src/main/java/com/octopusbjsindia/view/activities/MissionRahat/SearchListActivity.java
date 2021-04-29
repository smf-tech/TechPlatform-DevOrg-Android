package com.octopusbjsindia.view.activities.MissionRahat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.MissionRahat.SearchListData;
import com.octopusbjsindia.view.adapters.mission_rahat.SearchListAdapter;

import java.util.ArrayList;

public class SearchListActivity extends AppCompatActivity {

    RelativeLayout ly_no_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        setTitle("Select Hospital");
        RecyclerView listView = findViewById(R.id.listView);
        ArrayList<SearchListData> list = (ArrayList<SearchListData>) getIntent().getSerializableExtra("List");

        if (list.size() == 0)
            findViewById(R.id.ly_no_data).setVisibility(View.VISIBLE);
        SearchListAdapter adapter = new SearchListAdapter(list, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listView.setLayoutManager(mLayoutManager);
        listView.setAdapter(adapter);
    }

    public void setTitle(String title) {
        TextView tvTitle = findViewById(R.id.toolbar_title);
        ImageView back = findViewById(R.id.toolbar_back_action);
        tvTitle.setText(title);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void onSelected(int position) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", position);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}