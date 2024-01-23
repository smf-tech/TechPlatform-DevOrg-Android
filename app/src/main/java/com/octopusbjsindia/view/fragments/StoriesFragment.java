package com.octopusbjsindia.view.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.home.RoleAccessAPIResponse;
import com.octopusbjsindia.models.home.RoleAccessList;
import com.octopusbjsindia.models.home.RoleAccessObject;
import com.octopusbjsindia.models.stories.FeedData;
import com.octopusbjsindia.models.stories.FeedListResponse;
import com.octopusbjsindia.presenter.StoriesFragmentPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.CreateFeedActivity;
import com.octopusbjsindia.view.activities.HomeActivity;
import com.octopusbjsindia.view.adapters.FeedsAdapter;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"EmptyMethod", "WeakerAccess"})
public class StoriesFragment extends Fragment implements APIDataListener {

    View view;
    Context mContext;
    StoriesFragmentPresenter presentr;
    RelativeLayout progressBar;

    ArrayList<FeedData> feedList;
    FeedsAdapter adapter;

    private RecyclerView rvFeeds;
    int position = 0;

    boolean isCreateFeed = false, isDalete = false;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null && getArguments() != null) {
            String title = (String) getArguments().getSerializable("TITLE");
            ((HomeActivity) getActivity()).setActionBarTitle(title);}
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stories, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presentr = new StoriesFragmentPresenter(this);
        progressBar = view.findViewById(R.id.ly_progress_bar);

        RoleAccessAPIResponse roleAccessAPIResponse = Util.getRoleAccessObjectFromPref();
        RoleAccessList roleAccessList = roleAccessAPIResponse.getData();
        if (roleAccessList != null) {
            List<RoleAccessObject> roleAccessObjectList = roleAccessList.getRoleAccess();
            for (RoleAccessObject roleAccessObject : roleAccessObjectList) {
                if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_CREATE_FEED)) {
                    isCreateFeed = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_DELETE_FEED)) {
                    isDalete = true;
                    continue;
                }
            }
        }


        rvFeeds = view.findViewById(R.id.rv_feeds);
        rvFeeds.setNestedScrollingEnabled(false);

        feedList = new ArrayList<FeedData>();
        adapter = new FeedsAdapter(this, feedList, presentr, isDalete);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        rvFeeds.setLayoutManager(mLayoutManager);
        rvFeeds.setAdapter(adapter);

        if (isCreateFeed) {
            FloatingActionButton fabAddFeed = view.findViewById(R.id.fab_add_feed);
            fabAddFeed.setVisibility(View.VISIBLE);
            fabAddFeed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), CreateFeedActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            view.findViewById(R.id.fab_add_feed).setVisibility(View.GONE);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rvFeeds.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy > 0) {
                        view.findViewById(R.id.fab_add_feed).setVisibility(View.GONE);
                    } else {
                        if (isCreateFeed) {
                            view.findViewById(R.id.fab_add_feed).setVisibility(View.VISIBLE);
                        } else {
                            view.findViewById(R.id.fab_add_feed).setVisibility(View.GONE);
                        }
                    }
                    super.onScrolled(recyclerView, dx, dy);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.isConnected(getContext())) {
            presentr.getFeedLest();
        } else {
            Util.showToast(getResources().getString(R.string.msg_no_network), mContext);
        }
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        if (TextUtils.isEmpty(message)) {
            Util.showToast(getResources().getString(R.string.msg_something_went_wrong), mContext);
        } else {
            Util.showToast(message, mContext);
        }
        if (feedList.size() > 0) {
            view.findViewById(R.id.ly_no_data).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.ly_no_data).setVisibility(View.VISIBLE);
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
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        if (progressBar != null)
            progressBar.setVisibility(View.GONE);
    }

    @Override
    public void closeCurrentActivity() {

    }

    public void setAdapter(FeedListResponse responseData) {
        feedList.clear();
        feedList.addAll(responseData.getData());
        adapter.notifyDataSetChanged();
        if (feedList.size() > 0) {
            view.findViewById(R.id.ly_no_data).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.ly_no_data).setVisibility(View.VISIBLE);
        }
    }

    public void feedDeleted() {
        feedList.remove(position);
        adapter.notifyDataSetChanged();
    }

    public void setPosition(int adapterPosition) {
        position = adapterPosition;
    }
}
