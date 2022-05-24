package com.octopusbjsindia.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.view.fragments.MatrimonyMeetDetailFragment;

public class MatrimonyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrimony);

        TextView title = findViewById(R.id.toolbar_title);
        title.setText(getIntent().getStringExtra("ToOpen"));

        ImageView ivBack = findViewById(R.id.toolbar_back_action);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(getIntent().getStringExtra("ToOpen").equals("Meet Detail")){
            Bundle bundle = new Bundle();
            bundle.putSerializable("MeetData", getIntent().getSerializableExtra("MeetData"));

            MatrimonyMeetDetailFragment fragment = new MatrimonyMeetDetailFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.ly_main, fragment, "")
                        .commit();
        }
    }
}
