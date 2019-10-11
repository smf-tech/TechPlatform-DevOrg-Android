package com.platform.view.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.platform.R;
import com.platform.models.Matrimony.Group;
import com.platform.models.tm.LandingPageRequest;
import com.platform.view.adapters.ShowBachesFemaleRecyclerAdapter;
import com.platform.view.adapters.ShowBachesRecyclerAdapter;
import com.platform.view.adapters.TMLandingPageRecyclerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("CanBeFinal")
public class ShowBachesDialogFragment extends DialogFragment implements View.OnClickListener,
        TMLandingPageRecyclerAdapter.OnRequestItemClicked {

    Group group;
    private View tmFragmentView;
    private String bachesObjectString;
    private ExpandableListView rvPendingRequests;
    private RecyclerView rvLandingPageView, rvfemalebatchView;
    private ShowBachesRecyclerAdapter mAdapter;
    private ShowBachesFemaleRecyclerAdapter showBachesFemaleRecyclerAdapter;

    private Map<String, List<LandingPageRequest>> map = new HashMap<>();
    private List<LandingPageRequest> pendingRequestList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null && getArguments() != null) {
            String title = (String) getArguments().getSerializable("TITLE");
            if (TextUtils.isEmpty(title)) {
                title = getString(R.string.approvals);
            }
            // ((HomeActivity) getActivity()).setActionBarTitle(title);
            //   ((HomeActivity) getActivity()).setSyncButtonVisibility(false);

            Bundle bundle = this.getArguments();
            if (!(bundle == null)) {
                bachesObjectString = bundle.getString("bachesObjectString", "");
            }
            Gson gson = new Gson();
            group = gson.fromJson(bachesObjectString, Group.class);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        tmFragmentView = inflater.inflate(R.layout.fragment_showbaches_dialog, container, false);
        return tmFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {

        rvLandingPageView = tmFragmentView.findViewById(R.id.rvLandingPageView);
        rvfemalebatchView = tmFragmentView.findViewById(R.id.rvfemalebatchView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);

        rvLandingPageView.setLayoutManager(layoutManager);
        RecyclerView.LayoutManager layoutManager2 = new GridLayoutManager(getContext(), 1);
        rvfemalebatchView.setLayoutManager(layoutManager2);
        /*rvPendingRequests = tmFragmentView.findViewById(R.id.rv_dashboard_tm);
        rvPendingRequests.setGroupIndicator(null);

        final TextView viewAllApprovals = tmFragmentView.findViewById(R.id.txt_view_all_approvals);
        viewAllApprovals.setOnClickListener(this);*/

        mAdapter = new ShowBachesRecyclerAdapter(getContext(), group.getMale(), this::onItemClicked);
        rvLandingPageView.setAdapter(mAdapter);

        showBachesFemaleRecyclerAdapter = new ShowBachesFemaleRecyclerAdapter(getContext(), group.getFemale(), this::onItemClicked);
        rvfemalebatchView.setAdapter(showBachesFemaleRecyclerAdapter);


    }

    @Override
    public void onClick(View v) {
        /*if (v.getId() == R.id.txt_view_all_approvals) {
         *//*Util.launchFragment(new TMUserApprovalsFragment(), getContext(),
                    getString(R.string.approvals), true);*//*
            Intent startMain = new Intent(getActivity(), TMFiltersListActivity.class);
            startActivity(startMain);
        }*/
    }


    @Override
    public void onItemClicked(int pos) {
        LandingPageRequest pendingRequest = pendingRequestList.get(pos);
        showActionPopUp(pendingRequest);
    }

    private void showActionPopUp(final LandingPageRequest pendingRequest) {
        /*if (getFragmentManager() == null) {
            return;
        }
*//*
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ApprovalDialogFragment approvalDialogFragment =
                ApprovalDialogFragment.newInstance(pendingRequest, mAdapter);
        approvalDialogFragment.show(ft, "dialog");*//*
        if (Util.isConnected(getActivity())) {
            Intent startMain = new Intent(getActivity(), TMFiltersListActivity.class);
            startMain.putExtra("filter_type",pendingRequest.getType());
            startActivity(startMain);
        }else {
            Util.showToast(getString(R.string.msg_no_network), getActivity());

        }*/

    }


}