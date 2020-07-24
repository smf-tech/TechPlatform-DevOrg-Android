package com.octopusbjsindia.view.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.Matrimony.AllUserData;
import com.octopusbjsindia.models.Matrimony.MatrimonyUserFilterData;
import com.octopusbjsindia.models.Matrimony.UserProfileList;
import com.octopusbjsindia.presenter.MatrimonyProfilesListFragmentPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.MatrimonyProfileDetailsActivity;
import com.octopusbjsindia.view.activities.MatrimonyProfileListActivity;
import com.octopusbjsindia.view.adapters.MatrimonyProfileListRecyclerAdapter;
import com.octopusbjsindia.widgets.SingleSelectBottomSheet;

import java.util.ArrayList;

public class MatrimonyProfileListFragment extends Fragment implements View.OnClickListener,
        MatrimonyProfileListRecyclerAdapter.OnRequestItemClicked,
        MatrimonyProfileListRecyclerAdapter.OnApproveRejectClicked,
        SingleSelectBottomSheet.MultiSpinnerListener,
        SearchView.OnQueryTextListener,
        APIDataListener {

    private View view;
    private String meetIdReceived;
    ArrayList<String> ListDrink = new ArrayList<>();
    private SearchView editSearch;
    private String currentSelectedFilter = "", toOpen = "", nextPageUrl = "";
    private SingleSelectBottomSheet bottomSheetDialogFragment;
    private MatrimonyProfilesListFragmentPresenter presenter;
    private MatrimonyProfileListRecyclerAdapter matrimonyProfileListRecyclerAdapter;
    private RecyclerView rv_matrimonyprofileview;
    private ArrayList<UserProfileList> userProfileLists = new ArrayList<>();
    private ArrayList<UserProfileList> userProfileListsFiltered = new ArrayList<>();
    private ImageView toolbar_back_action, toolbarFilter, toolbar_action, ivNoData;
    private TextView toolbarTitle, tvNoData;
    private boolean isSearchVisible = false;
    private Button btnClearFilters;
    private RelativeLayout progressBar;
    private String titleStr = "Candidate List";
    //pagination
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    private BroadcastReceiver mMessageReceiver;
    int position=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_matrimony_profile_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
        toOpen = getArguments().getString("toOpen");
        // we have checked toOpen in adapter. so be careful while chaging its values.
        if (toOpen.equals("MeetUserList")) {
            titleStr = "Meet Candidates";
            meetIdReceived = ((MatrimonyProfileListActivity) getActivity()).getMeetId();
            ((MatrimonyProfileListActivity) getActivity()).getMatrimonyUserFilterData().
                    setSection_type(Constants.MatrimonyModule.MEET_USERS_SECTION);
            ((MatrimonyProfileListActivity) getActivity()).getMatrimonyUserFilterData().setMeet_id(meetIdReceived);
        } else if (toOpen.equals("NewUserList")) {
            titleStr = "Recently Joined";
            ((MatrimonyProfileListActivity) getActivity()).getMatrimonyUserFilterData().
                    setSection_type(Constants.MatrimonyModule.NEWLY_JOINED_SECTION);
        } else if (toOpen.equals("UnverifiedUserList")) {
            titleStr = "Verification Pending";
            ((MatrimonyProfileListActivity) getActivity()).getMatrimonyUserFilterData().
                    setSection_type(Constants.MatrimonyModule.VERIFICATION_PENDING_SECTION);
        } else if (toOpen.equals("AllUserList")) {
            titleStr = "All Candidates";
            ((MatrimonyProfileListActivity) getActivity()).getMatrimonyUserFilterData().
                    setSection_type(Constants.MatrimonyModule.ALL_USERS_SECTION);
        } else if (toOpen.equals("BlockedUsers")) {
            titleStr = "Blocked Users";
            ((MatrimonyProfileListActivity) getActivity()).getMatrimonyUserFilterData().
                    setSection_type(Constants.MatrimonyModule.BLOCKED_USER_SECTION);
        } else if (toOpen.equals("BangUsers")) {
            titleStr = "Banged Users";
            ((MatrimonyProfileListActivity) getActivity()).getMatrimonyUserFilterData().
                    setSection_type(Constants.MatrimonyModule.BANGED_USER_SECTION);
        }
        presenter.getAllUserList(((MatrimonyProfileListActivity) getActivity()).getMatrimonyUserFilterData(),
                BuildConfig.BASE_URL + String.format(Urls.Matrimony.ALL_FILTER_USERS));

        //Updating the flags of perticler profile from profile detail.
        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isBanned = intent.getBooleanExtra("isBanned",false);
                if(!isBanned && toOpen.equals("BangUsers")) {
                    if(position<userProfileListsFiltered.size()){
                        userProfileListsFiltered.remove(position);
                    }
                    if(position<userProfileLists.size()){
                        userProfileLists.remove(position);
                    }
                    matrimonyProfileListRecyclerAdapter.notifyDataSetChanged();
                } else if(isBanned && !toOpen.equals("BangUsers")) {
                    if(position<userProfileListsFiltered.size()){
                        userProfileListsFiltered.remove(position);
                    }
                    if(position<userProfileLists.size()){
                        userProfileLists.remove(position);
                    }
                    matrimonyProfileListRecyclerAdapter.notifyDataSetChanged();
                }
            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("PROFILE_UPDATE"));

    }

    private void initViews() {
        progressBar = view.findViewById(R.id.progress_bar);
        toolbar_back_action = view.findViewById(R.id.toolbar_back_action1);
        toolbar_back_action.setVisibility(View.VISIBLE);
        editSearch = view.findViewById(R.id.search_view1);
        toolbarFilter = view.findViewById(R.id.toolbar_filter);
        toolbarTitle = view.findViewById(R.id.toolbar_title1);
        toolbarTitle.setText("Candidate List");
        toolbar_back_action.setOnClickListener(this);
        toolbarFilter.setOnClickListener(this);
        editSearch.setOnQueryTextListener(this);
        btnClearFilters = view.findViewById(R.id.btn_clear_filters);
        btnClearFilters.setOnClickListener(this);
        ivNoData = view.findViewById(R.id.iv_no_data);
        tvNoData = view.findViewById(R.id.tv_no_data);
        presenter = new MatrimonyProfilesListFragmentPresenter(this);
        userProfileLists.clear();
        userProfileListsFiltered.clear();
        rv_matrimonyprofileview = view.findViewById(R.id.rv_matrimonyprofileview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv_matrimonyprofileview.setLayoutManager(layoutManager);
        matrimonyProfileListRecyclerAdapter = new MatrimonyProfileListRecyclerAdapter(getActivity(), userProfileLists,
                this, this);
        rv_matrimonyprofileview.setAdapter(matrimonyProfileListRecyclerAdapter);

        rv_matrimonyprofileview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            if (nextPageUrl != null && !TextUtils.isEmpty(nextPageUrl)) {
                                presenter.getAllUserList(((MatrimonyProfileListActivity) getActivity())
                                        .getMatrimonyUserFilterData(), nextPageUrl);
                            }
                        }
                    }
                }

                if (dy < -5 && ((MatrimonyProfileListActivity) getActivity()).isFilterApplied() &&
                        btnClearFilters.getVisibility() != View.VISIBLE) {
                    btnClearFilters.setVisibility(View.VISIBLE);
                } else if (dy > 5 && (!((MatrimonyProfileListActivity) getActivity()).isFilterApplied()) &&
                        btnClearFilters.getVisibility() == View.VISIBLE) {
                    btnClearFilters.setVisibility(View.GONE);
                }
            }
        });
        CreateFilterList();
    }

    private void CreateFilterList() {
        ListDrink.add("Approved");
        ListDrink.add("Rejected");
        ListDrink.add("Pending");
    }

    void filter(String str) {
        ArrayList<UserProfileList> temp = new ArrayList();
        userProfileLists.clear();
        updateUserListWithFilter(currentSelectedFilter);
        if (!TextUtils.isEmpty(str)) {
            temp.clear();
            for (UserProfileList d : userProfileLists) {
                if (d.getMatrimonial_profile().getPersonal_details().getFirst_name().toLowerCase().contains(str.toLowerCase())) {
                    temp.add(d);
                }
            }
            //update recyclerview
            userProfileLists.clear();
            userProfileLists = temp;
            matrimonyProfileListRecyclerAdapter.updateList(userProfileLists);
        } else {
            temp.clear();
            updateUserListWithFilter(currentSelectedFilter);
            matrimonyProfileListRecyclerAdapter.updateList(userProfileLists);
        }
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_filtertype:
                //onBackPressed();
                break;
            case R.id.img_filter_image:

                break;
            case R.id.toolbar_back_action1:
                getActivity().finish();
                break;
            case R.id.toolbar_filter:
                userProfileLists.clear();
                ((MatrimonyProfileListActivity) getActivity()).openFragment("filter_fragment");
                break;
            case R.id.toolbar_action1:
//                if (isSearchVisible) {
//                    isSearchVisible = false;
//                    editSearch.setVisibility(View.VISIBLE);
//                    editSearch.requestFocus();
//                    toolbar_action.setImageResource(R.drawable.ic_close);
//                } else {
//                    isSearchVisible = true;
//                    editSearch.setVisibility(View.GONE);
//                    toolbar_action.setImageResource(R.drawable.ic_search);
//                    filter("");
//                }
                break;
            case R.id.btn_clear_filters:
                ((MatrimonyProfileListActivity) getActivity()).clearFilterCandidtaesData();
                ((MatrimonyProfileListActivity) getActivity()).setFilterApplied(false);
                btnClearFilters.setVisibility(View.GONE);
                //clear UserProfiles list as we are now calling getAllUserList api and will upadte
                // same list with filtered data.
                userProfileLists.clear();
                MatrimonyUserFilterData matrimonyUserFilterData = new MatrimonyUserFilterData();
                // api call
                if (toOpen.equals("MeetUserList")) {
                    meetIdReceived = ((MatrimonyProfileListActivity) getActivity()).getMeetId();
                    matrimonyUserFilterData.setSection_type(Constants.MatrimonyModule.MEET_USERS_SECTION);
                    matrimonyUserFilterData.setMeet_id(meetIdReceived);
                } else if (toOpen.equals("NewUserList")) {
                    matrimonyUserFilterData.setSection_type(Constants.MatrimonyModule.NEWLY_JOINED_SECTION);
                } else if (toOpen.equals("UnverifiedUserList")) {
                    matrimonyUserFilterData.setSection_type(Constants.MatrimonyModule.VERIFICATION_PENDING_SECTION);
                } else if (toOpen.equals("AllUserList")) {
                    matrimonyUserFilterData.setSection_type(Constants.MatrimonyModule.ALL_USERS_SECTION);
                }
                ((MatrimonyProfileListActivity) getActivity()).setMatrimonyUserFilterData(matrimonyUserFilterData);
                userProfileListsFiltered.clear();
                userProfileLists.clear();
                presenter.getAllUserList(((MatrimonyProfileListActivity) getActivity()).getMatrimonyUserFilterData(),
                        BuildConfig.BASE_URL + String.format(Urls.Matrimony.ALL_FILTER_USERS));
                break;
        }
    }

    public void showUserProfileList(AllUserData userResponse) {
        loading = true;
        nextPageUrl = userResponse.getNextPageUrl();
        if (userResponse.getData().size() > 0) {
            if (toOpen.equals("BlockedUsers") || toOpen.equals("BangUsers")) {
                toolbarFilter.setVisibility(View.INVISIBLE);
            } else {
                toolbarFilter.setVisibility(View.VISIBLE);
            }
            ivNoData.setVisibility(View.GONE);
            tvNoData.setVisibility(View.GONE);
            //userProfileListsFiltered = (ArrayList<UserProfileList>) userResponse.getData();
            userProfileListsFiltered.addAll((ArrayList<UserProfileList>) userResponse.getData());
            userProfileLists.clear();
            userProfileLists.addAll(userProfileListsFiltered);
            matrimonyProfileListRecyclerAdapter.notifyDataSetChanged();
            toolbarTitle.setText(titleStr + "(" + userResponse.getTotal() + ")");
        } else {
            dispayNoData("No Data available.");
        }

        if (((MatrimonyProfileListActivity) getActivity()).isFilterApplied()) {
            btnClearFilters.setVisibility(View.VISIBLE);
        } else {
            btnClearFilters.setVisibility(View.GONE);
        }
    }

    public void dispayNoData(String responseMessage) {
        toolbarFilter.setVisibility(View.INVISIBLE);
        ivNoData.setVisibility(View.VISIBLE);
        tvNoData.setVisibility(View.VISIBLE);
        tvNoData.setText(responseMessage);

        if (((MatrimonyProfileListActivity) getActivity()).isFilterApplied()) {
            btnClearFilters.setVisibility(View.VISIBLE);
        } else {
            btnClearFilters.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filter(newText);
        return false;
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        Util.showToast(message, this);
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
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void closeCurrentActivity() {

    }

    @Override
    public void onItemClicked(int pos) {
        position = pos;
        UserProfileList userProfileList = userProfileLists.get(pos);
        Gson gson = new Gson();
        String jsonInString = gson.toJson(userProfileList);
        Intent startMain1 = new Intent(getActivity(), MatrimonyProfileDetailsActivity.class);
        startMain1.putExtra("filter_type", jsonInString);
        startMain1.putExtra("meetid", meetIdReceived);
        startMain1.putExtra("selectedPos", pos);
        startActivityForResult(startMain1, Constants.MatrimonyModule.FLAG_UPDATE_RESULT);

    }

    @Override
    public void onApproveClicked(int pos) {
    }

    @Override
    public void onRejectClicked(int pos) {
    }

    @Override
    public void onValuesSelected(int selectedPosition, String spinnerName, String selectedValues) {
        userProfileLists.clear();
        currentSelectedFilter = selectedValues;
        updateUserListWithFilter(selectedValues);
    }

    private void updateUserListWithFilter(String selectedValues) {
        //TODO remember to discuss  about getIsApproved() now replacing with
        userProfileLists.clear();
        if ("Approved".equalsIgnoreCase(selectedValues)) {
            for (int i = 0; i < userProfileListsFiltered.size(); i++) {

                if (userProfileListsFiltered.get(i).getUserMeetStatus().equalsIgnoreCase(selectedValues)) {
                    userProfileLists.add(userProfileListsFiltered.get(i));
                }
            }
            matrimonyProfileListRecyclerAdapter.notifyDataSetChanged();

        } else if ("Rejected".equalsIgnoreCase(selectedValues)) {
            for (int i = 0; i < userProfileListsFiltered.size(); i++) {

                if (userProfileListsFiltered.get(i).getUserMeetStatus().equalsIgnoreCase(selectedValues)) {
                    userProfileLists.add(userProfileListsFiltered.get(i));
                }
            }
            matrimonyProfileListRecyclerAdapter.notifyDataSetChanged();

        } else if ("Pending".equalsIgnoreCase(selectedValues)) {
            for (int i = 0; i < userProfileListsFiltered.size(); i++) {

                if (userProfileListsFiltered.get(i).getUserMeetStatus().equalsIgnoreCase(selectedValues)) {
                    userProfileLists.add(userProfileListsFiltered.get(i));
                }
            }
            matrimonyProfileListRecyclerAdapter.notifyDataSetChanged();
        } else {
            for (int i = 0; i < userProfileListsFiltered.size(); i++) {
                userProfileLists.add(userProfileListsFiltered.get(i));
            }
            matrimonyProfileListRecyclerAdapter.notifyDataSetChanged();
        }
        if (userProfileLists.size() > 0) {
            ivNoData.setVisibility(View.GONE);
            tvNoData.setVisibility(View.GONE);
        } else {
            ivNoData.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == Constants.MatrimonyModule.FLAG_UPDATE_RESULT && data != null) {
//            UserProfileList listItem = (UserProfileList) data.getSerializableExtra(Constants.Planner.MEMBER_LIST_DATA);
//            int receivedPos = data.getIntExtra(Constants.Planner.MEMBER_LIST_COUNT, -1);
//            if (receivedPos != -1) {
//                userProfileLists.set(receivedPos, listItem);
//                matrimonyProfileListRecyclerAdapter.notifyItemChanged(receivedPos);
//            }
//        }

    }
}
