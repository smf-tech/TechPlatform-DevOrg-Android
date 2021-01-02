package com.octopusbjsindia.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.sel_content.SELVideoContent;
import com.octopusbjsindia.models.sel_content.VideoContentAPIResponse;
import com.octopusbjsindia.presenter.SELFragmentPresenter;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.SELFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class SELFragment extends Fragment implements APIDataListener {
    private SELFragmentPresenter presenter;
    private final List<SELVideoContent> selContentList = new ArrayList<>();
    private SELFragmentAdapter selFragmentAdapter;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tutorials, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBarLayout = view.findViewById(R.id.profile_act_progress_bar);
        progressBar = view.findViewById(R.id.pb_profile_act);
        presenter = new SELFragmentPresenter(this);
        RecyclerView rvSelContent = view.findViewById(R.id.rv_sel_content);
        presenter.getSelContentData();
        selFragmentAdapter = new SELFragmentAdapter(this, selContentList);
        rvSelContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvSelContent.setAdapter(selFragmentAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        if (getActivity() != null) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.msg_failure),
                    Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (getActivity() != null) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.msg_failure),
                    Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void onSuccessListener(String requestID, String response) {

    }

    public void populateData(VideoContentAPIResponse videoContentAPIResponse) {
        List<SELVideoContent> selVideoContentList = videoContentAPIResponse.getData();
        if (selVideoContentList.size() > 0) {
            selContentList.addAll(selVideoContentList);
            selFragmentAdapter.notifyDataSetChanged();
        } else {

        }
    }

    @Override
    public void showProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void closeCurrentActivity() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }
}