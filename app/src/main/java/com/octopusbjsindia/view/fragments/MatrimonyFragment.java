package com.octopusbjsindia.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.Matrimony.MatrimonyMeet;
import com.octopusbjsindia.models.Matrimony.NewRegisteredUserResponse;
import com.octopusbjsindia.models.Matrimony.UserProfileList;
import com.octopusbjsindia.presenter.MatrimonyFragmentPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.CreateMatrimonyMeetActivity;
import com.octopusbjsindia.view.activities.HomeActivity;
import com.octopusbjsindia.view.activities.MatrimonyProfileListActivity;
import com.octopusbjsindia.view.activities.MyTeamActivity;
import com.octopusbjsindia.view.adapters.UserProfileAdapter;
import com.octopusbjsindia.view.adapters.ViewPagerAdapter;
import com.octopusbjsindia.models.home.RoleAccessAPIResponse;
import com.octopusbjsindia.models.home.RoleAccessList;
import com.octopusbjsindia.models.home.RoleAccessObject;

import java.util.ArrayList;
import java.util.List;

public class MatrimonyFragment extends Fragment implements APIDataListener, View.OnClickListener {

    private final String MEET_DATA = "MeetData";
    private final String MEET_POS = "MeetPos";
    private MatrimonyFragmentPresenter presenter;
    private RelativeLayout pbLayout;
    private List<MatrimonyMeet> matrimonyMeetList = new ArrayList<>();
    private ViewPager vpUpcomingMeets;
    private ArrayList<UserProfileList> newUserList = new ArrayList<UserProfileList>();
    private ArrayList<UserProfileList> unverifiedUserList = new ArrayList<UserProfileList>();
    private RecyclerView rvNewUser, rvVarificationPending;
    private FloatingActionButton fbSelect;
    private ExtendedFloatingActionButton fbCreatMeet, fbAllUser, fbBlockedUsers, fbBangUsers, fbMyTeam;
    private boolean isFABOpen = false;
    private View view;
    boolean visibleCreatMeet = false,visibleMyTeam = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MatrimonyFragmentPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_matrimony, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null && getArguments() != null) {
            String title = (String) getArguments().getSerializable("TITLE");
            ((HomeActivity) getActivity()).setActionBarTitle(title);
            ((HomeActivity) getActivity()).setSyncButtonVisibility(false);

            if ((boolean) getArguments().getSerializable("SHOW_BACK")) {
                ((HomeActivity) getActivity()).showBackArrow();
            }
        }

        pbLayout = view.findViewById(R.id.progress_bar);

        vpUpcomingMeets = view.findViewById(R.id.vp_upcoming_meets);
        vpUpcomingMeets.setClipToPadding(false);
        // set padding manually, the more you set the padding the more you see of prev & next page
        vpUpcomingMeets.setPadding(80, 20, 80, 20);
        // sets a margin b/w individual pages to ensure that there is a gap b/w them
        vpUpcomingMeets.setPageMargin(20);

        rvNewUser = view.findViewById(R.id.rv_new_user);
        rvVarificationPending = view.findViewById(R.id.rv_varification_pending);

        fbMyTeam = view.findViewById(R.id.fbMyTeam);
        fbMyTeam.setOnClickListener(this);
        fbSelect = view.findViewById(R.id.fb_select);
        fbSelect.setOnClickListener(this);
        fbCreatMeet = view.findViewById(R.id.fb_create_meet);
        fbCreatMeet.setOnClickListener(this);
        fbAllUser = view.findViewById(R.id.fb_all_users);
        fbAllUser.setOnClickListener(this);
        fbBlockedUsers = view.findViewById(R.id.fb_blocked_users);
        fbBlockedUsers.setOnClickListener(this);
        fbBangUsers = view.findViewById(R.id.fb_bang_users);
        fbBangUsers.setOnClickListener(this);

        view.findViewById(R.id.tv_see_all_newuser).setOnClickListener(this);
        view.findViewById(R.id.tv_see_all_varification_pending).setOnClickListener(this);

        RoleAccessAPIResponse roleAccessAPIResponse = Util.getRoleAccessObjectFromPref();
        RoleAccessList roleAccessList = roleAccessAPIResponse.getData();
        if (roleAccessList != null) {
            List<RoleAccessObject> roleAccessObjectList = roleAccessList.getRoleAccess();
            for (RoleAccessObject roleAccessObject : roleAccessObjectList) {
                if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_CREATE_MEET)) {
                    visibleCreatMeet = true;

                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_MY_SABORDINET)) {
                    visibleMyTeam = true;
                    continue;
                }
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getMatrimonyMeets();
        presenter.getRecentelyJoinedUsers();
        presenter.getUnVerifiedUsers();
        closeFABMenu();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fb_select) {
            if (!isFABOpen) {
                showFabMenu();
            } else {
                closeFABMenu();
            }
        } else if (v.getId() == R.id.fbMyTeam) {
            Intent myTeamIntent = new Intent(getActivity(), MyTeamActivity.class);
            startActivity(myTeamIntent);
        }  else if (v.getId() == R.id.fb_create_meet) {
            Intent createMatrimonyIntent = new Intent(getActivity(), CreateMatrimonyMeetActivity.class);
            createMatrimonyIntent.putExtra("SwitchToFragment", "CreateMeetFirstFragment");
            startActivity(createMatrimonyIntent);
        } else if (v.getId() == R.id.fb_all_users) {
            Intent startMain = new Intent(getActivity(), MatrimonyProfileListActivity.class);
            startMain.putExtra("toOpean","AllUserList");
            startActivity(startMain);
        } else if (v.getId() == R.id.fb_blocked_users) {
            Intent startMain = new Intent(getActivity(), MatrimonyProfileListActivity.class);
            startMain.putExtra("toOpean","BlockedUsers");
            startActivity(startMain);
        } else if (v.getId() == R.id.fb_bang_users) {
            Intent startMain = new Intent(getActivity(), MatrimonyProfileListActivity.class);
            startMain.putExtra("toOpean","BangUsers");
            startActivity(startMain);
        } else if (v.getId() == R.id.tv_see_all_newuser) {
            if(newUserList.size()>0){
                Intent startMain = new Intent(getActivity(), MatrimonyProfileListActivity.class);
                startMain.putExtra("toOpean","NewUserList");
                //startMain.putExtra("userList",newUserList);
                startActivity(startMain);
            }

        } else if (v.getId() == R.id.tv_see_all_varification_pending) {
            if(unverifiedUserList.size()>0) {
                Intent startMain = new Intent(getActivity(), MatrimonyProfileListActivity.class);
                startMain.putExtra("toOpean", "UnverifiedUserList");
                //startMain.putExtra("userList", unverifiedUserList);
                startActivity(startMain);
            }
        }
    }

    private void showFabMenu() {
        fbAllUser.show();
        fbAllUser.setEnabled(true);
        fbAllUser.animate().translationY(-getResources().getDimension(R.dimen.standard_60));
        fbBlockedUsers.show();
        fbBlockedUsers.setEnabled(true);
        fbBlockedUsers.animate().translationY(-getResources().getDimension(R.dimen.standard_180));
        fbBangUsers.show();
        fbBangUsers.setEnabled(true);
        fbBangUsers.animate().translationY(-getResources().getDimension(R.dimen.standard_120));
        if(visibleCreatMeet) {
            fbCreatMeet.show();
            fbCreatMeet.setEnabled(true);
            fbCreatMeet.animate().translationY(-getResources().getDimension(R.dimen.standard_240));
        }
        if(visibleMyTeam) {
            fbMyTeam.show();
            fbMyTeam.setEnabled(true);
            fbMyTeam.animate().translationY(-getResources().getDimension(R.dimen.standard_300));
        }
        fbSelect.setRotation(45);
        isFABOpen = true;
    }

    private void closeFABMenu() {
        fbAllUser.animate().translationY(0);
        fbAllUser.hide();
        fbAllUser.setEnabled(false);
        fbBlockedUsers.animate().translationY(0);
        fbBlockedUsers.hide();
        fbBlockedUsers.setEnabled(false);
        fbBangUsers.animate().translationY(0);
        fbBangUsers.hide();
        fbBangUsers.setEnabled(false);
        if(visibleCreatMeet) {
            fbCreatMeet.animate().translationY(0);
            fbCreatMeet.hide();
            fbCreatMeet.setEnabled(false);
        }
        if(visibleMyTeam) {
            fbMyTeam.animate().translationY(0);
            fbMyTeam.hide();
            fbMyTeam.setEnabled(false);
        }
        fbSelect.setRotation(0);
        isFABOpen = false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        Util.showToast(message, this);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        Util.showToast(getResources().getString(R.string.msg_something_went_wrong), this);
    }

    @Override
    public void onSuccessListener(String requestID, String response) {

    }

    @Override
    public void showProgressBar() {
        if (pbLayout != null) {
            pbLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgressBar() {
        if (pbLayout != null) {
            pbLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void closeCurrentActivity() {

    }

    public void setMatrimonyMeets(List<MatrimonyMeet> data, String earliestMeetId) {
        matrimonyMeetList.clear();
        if (data.size() > 0) {
            matrimonyMeetList.addAll(data);
            setupViewPager(earliestMeetId);
            //setCurrentMeetData(0);
        } else {
//            rl_meetLayout.setVisibility(View.GONE);
//            rlNoMeet.setVisibility(View.VISIBLE);
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), "No Meet available at your location.",
                    Snackbar.LENGTH_LONG);
        }
    }

    private void setupViewPager(String earliestMeetId) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        if (matrimonyMeetList.size() > 0) {
            for (int i = 0; i < matrimonyMeetList.size(); i++) {
                VPMeetFragment meetFragment = new VPMeetFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(MEET_DATA, matrimonyMeetList.get(i));
                bundle.putSerializable(MEET_POS, i);
                meetFragment.setArguments(bundle);
                adapter.addFragment(meetFragment, "meet");
            }
        }
//        tvMsg.setVisibility(View.GONE);
        vpUpcomingMeets.setVisibility(View.VISIBLE);
        vpUpcomingMeets.setAdapter(adapter);
//        vpUpcomingMeets.setCurrentItem(pos);
    }


    public void onNewProfileFetched(String requestID, NewRegisteredUserResponse newUserResponse) {
        if (newUserResponse != null && newUserResponse.getData() != null
                && newUserResponse.getData().size() > .0) {
            view.findViewById(R.id.ly_no_newuser).setVisibility(View.GONE);
//            view.findViewById(R.id.tv_see_all_newuser).setOnClickListener(this);
            newUserList.clear();
            newUserList.addAll(newUserResponse.getData());
            UserProfileAdapter adapter = new UserProfileAdapter(getContext(), newUserList, false, false);
            rvNewUser.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
            rvNewUser.setAdapter(adapter);
        } else {
            view.findViewById(R.id.ly_no_newuser).setVisibility(View.VISIBLE);
        }
    }

    public void onUnverifiedProfileFetched(String requestID, NewRegisteredUserResponse newUserResponse) {
        if (newUserResponse != null && newUserResponse.getData() != null
                && newUserResponse.getData().size() > .0) {
            view.findViewById(R.id.ly_no_varification).setVisibility(View.GONE);
//            view.findViewById(R.id.tv_see_all_newuser).setOnClickListener(this);
            unverifiedUserList.clear();
            unverifiedUserList.addAll(newUserResponse.getData());
            UserProfileAdapter adapter = new UserProfileAdapter(getContext(), unverifiedUserList, false, false);
            rvVarificationPending.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
            rvVarificationPending.setAdapter(adapter);
        } else {
            view.findViewById(R.id.ly_no_varification).setVisibility(View.VISIBLE);
        }
    }


}
