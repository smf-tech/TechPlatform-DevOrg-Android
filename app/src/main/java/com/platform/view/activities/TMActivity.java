package com.platform.view.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.models.common.Template;
import com.platform.view.adapters.TMAdapter;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CanBeFinal")
public class TMActivity extends BaseActivity {

    private List<Template> processAllList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_management);
    }

    @Override
    public void onResume() {
        super.onResume();
        initViews();
    }

    private void initViews() {
        setActionbar(getString(R.string.team_management));

        ArrayList<String> menuList = new ArrayList<>();
        menuList.add(getString(R.string.team_user_approval));

        processAllList.clear();
        for (int i = 0; i < menuList.size(); i++) {
            Template processList = new Template();
            processList.setName(menuList.get(i));
            processAllList.add(processList);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        TMAdapter adapter = new TMAdapter(this, processAllList);

        RecyclerView listView = findViewById(R.id.tm_recycler_view);
        listView.setLayoutManager(layoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(adapter);
    }

    private void setActionbar(String title) {
        if (title.contains("\n")) {
            title = title.replace("\n", " ");
        }

        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);

        ImageView img_back = findViewById(R.id.toolbar_back_action);
        img_back.setVisibility(View.VISIBLE);
    }
}
