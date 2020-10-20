package com.octopusbjsindia.view.activities;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.view.fragments.TicketDetailFragment;
import com.octopusbjsindia.view.fragments.TicketListFragment;

public class SupportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);


        TextView tvTitle = findViewById(R.id.tv_toolbar_title);

        String toOpen = getIntent().getStringExtra("toOpen");
        switch (toOpen) {
            case "ticketList":
                tvTitle.setText("Ticket List");
                TicketListFragment listFragment = new TicketListFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.lyMain, listFragment)
                        .commit();
                break;
            case "detailTicket":
                tvTitle.setText("Ticket Details");
                Bundle bundle = new Bundle();
                bundle.putSerializable("data",getIntent().getSerializableExtra("data"));
                TicketDetailFragment detailFragment = new TicketDetailFragment();
                detailFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.lyMain, detailFragment)
                        .commit();
                break;
        }

        findViewById(R.id.iv_toobar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
