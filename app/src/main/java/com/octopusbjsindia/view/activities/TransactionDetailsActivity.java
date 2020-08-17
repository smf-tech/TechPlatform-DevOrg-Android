package com.octopusbjsindia.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.Matrimony.SubordinateResponse;
import com.octopusbjsindia.models.Matrimony.TransectionDetailsResponse;
import com.octopusbjsindia.presenter.TransactionDetailsActivityPresenter;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.SubordinateListAdapter;
import com.octopusbjsindia.view.adapters.TransactionDetailsAdapter;

public class TransactionDetailsActivity extends AppCompatActivity implements APIDataListener {

    private TransactionDetailsActivityPresenter presenter;
    private RecyclerView rvTransactionDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);

        presenter = new TransactionDetailsActivityPresenter(this);
        presenter.getTransactionDetails(getIntent().getStringExtra("MeetId"));

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Transaction Details");
        findViewById(R.id.toolbar_back_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rvTransactionDetails = findViewById(R.id.rvTransactionDetails);

    }

    @Override
    public void onFailureListener(String requestID, String message) {
        Util.showToast(message,this);
    }


    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        TransectionDetailsResponse responseOBJ = new Gson().fromJson(response, TransectionDetailsResponse.class);
        if(responseOBJ.getCode() == 200){
            TransactionDetailsAdapter adapter;
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            adapter = new TransactionDetailsAdapter(this, responseOBJ.getData());
            rvTransactionDetails.setLayoutManager(mLayoutManager);
            rvTransactionDetails.setAdapter(adapter);
            findViewById(R.id.ly_no_data).setVisibility(View.GONE);
        } else {

            findViewById(R.id.ly_no_data).setVisibility(View.VISIBLE);
            Util.showToast(responseOBJ.getMessage(),this);
        }
    }

    @Override
    public void showProgressBar() {
        findViewById(R.id.lyProgressBar).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        findViewById(R.id.lyProgressBar).setVisibility(View.GONE);
    }


    @Override
    public void closeCurrentActivity() {

    }
}
