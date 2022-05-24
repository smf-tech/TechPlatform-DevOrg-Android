package com.octopusbjsindia.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.stories.CommentData;
import com.octopusbjsindia.models.stories.FeedData;
import com.octopusbjsindia.presenter.CommentActivityPresenter;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.CommentsAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommentActivity extends AppCompatActivity implements APIDataListener {

    private final String FEED_DATA = "FeedData";
    private final String COMMENT = "Comment";

    private CommentActivityPresenter presentr;
    private RelativeLayout pbLayout;

    ArrayList<CommentData> commentList;

    CommentsAdapter adapter;
    String feedId;

    RecyclerView rvComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        presentr = new CommentActivityPresenter(this);
        pbLayout = findViewById(R.id.progress_bar);

        setToolbar("Comments");
        commentList = new ArrayList<CommentData>();


        feedId = getIntent().getStringExtra(FEED_DATA);

        if (Util.isConnected(this)) {
            presentr.getCommentList(feedId);
        } else {
            Util.showToast(getResources().getString(R.string.msg_no_network), this);
            findViewById(R.id.ly_no_data).setVisibility(View.VISIBLE);
        }

//        etComment = findViewById(R.id.et_comment);
//        findViewById(R.id.iv_comment).setOnClickListener(this);
        rvComments = findViewById(R.id.rv_comments);
//        rvComments.setNestedScrollingEnabled(false);

        adapter = new CommentsAdapter(this, commentList, presentr);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.VERTICAL, false);
//        mLayoutManager.setStackFromEnd(true);
        rvComments.setLayoutManager(mLayoutManager);
        rvComments.setAdapter(adapter);

    }

    public void setToolbar(String title) {
        TextView tvTitle = findViewById(R.id.toolbar_title);
        tvTitle.setText(title);
        findViewById(R.id.toolbar_back_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void onCommentFetched(List<CommentData> data) {
        commentList.clear();
        Collections.reverse(data);
        commentList.addAll(data);
//        rvComments.smoothScrollToPosition(commentList.size()-1);
        adapter.notifyDataSetChanged();
        if (commentList.size() > 0) {
            findViewById(R.id.ly_no_data).setVisibility(View.GONE);
        } else {
            findViewById(R.id.ly_no_data).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        if (TextUtils.isEmpty(message)) {
            Util.showToast(getResources().getString(R.string.msg_something_went_wrong), this);
        } else {
            Util.showToast(message, this);
        }
        if (commentList.size() > 0) {
            findViewById(R.id.ly_no_data).setVisibility(View.GONE);
        } else {
            findViewById(R.id.ly_no_data).setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {

    }

    @Override
    public void showProgressBar() {
        findViewById(R.id.ly_progress_bar).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        findViewById(R.id.ly_progress_bar).setVisibility(View.GONE);
    }

    @Override
    public void closeCurrentActivity() {

    }
}
