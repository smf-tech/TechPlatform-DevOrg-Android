package com.octopusbjsindia.view.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
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

import org.json.JSONObject;

import java.util.ArrayList;

public class MatrimonyProfileListFragment extends Fragment implements View.OnClickListener,
        MatrimonyProfileListRecyclerAdapter.OnRequestItemClicked,
        MatrimonyProfileListRecyclerAdapter.OnApproveRejectClicked,
        SingleSelectBottomSheet.MultiSpinnerListener,
        SearchView.OnQueryTextListener,
        APIDataListener {

    private View view;
    public String meetIdReceived;
    ArrayList<String> ListDrink = new ArrayList<>();
    private SearchView editSearch;
    private String currentSelectedFilter = "";
    private SingleSelectBottomSheet bottomSheetDialogFragment;
    private MatrimonyProfilesListFragmentPresenter presenter;
    private MatrimonyProfileListRecyclerAdapter matrimonyProfileListRecyclerAdapter;
    private RecyclerView rv_matrimonyprofileview;
    private ArrayList<UserProfileList> userProfileLists = new ArrayList<>();
    private ArrayList<UserProfileList> userProfileListsFiltered = new ArrayList<>();
    private String approvalType;
    private ImageView toolbar_back_action, toolbarFilter, toolbar_action;
    private TextView toolbar_title;
    private boolean isSearchVisible = false;
    private String toOpen = "";
    private Button btnClearFilters;
    private ImageView ivNoData;
    private RelativeLayout progressBar;
    //paginetion
    private String nextPageUrl;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;

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
        if (toOpen.equals("MeetUserList")) {
            meetIdReceived = getArguments().getString("meetId");
            ((MatrimonyProfileListActivity) getActivity()).getMatrimonyUserFilterData().
                    setSection_type(Constants.MatrimonyModule.MEET_USERS_SECTION);
            ((MatrimonyProfileListActivity) getActivity()).getMatrimonyUserFilterData().setMeet_id(meetIdReceived);
            //presenter.getAllFiltersRequests(meetIdReceived);
        } else if (toOpen.equals("NewUserList")) {
            //showUserProfileList((List<UserProfileList>) getIntent().getSerializableExtra("userList"));
            ((MatrimonyProfileListActivity) getActivity()).getMatrimonyUserFilterData().
                    setSection_type(Constants.MatrimonyModule.NEWLY_JOINED_SECTION);
        } else if (toOpen.equals("UnverifiedUserList")) {
            //showUserProfileList((List<UserProfileList>) getIntent().getSerializableExtra("userList"));
            ((MatrimonyProfileListActivity) getActivity()).getMatrimonyUserFilterData().
                    setSection_type(Constants.MatrimonyModule.VERIFICATION_PENDING_SECTION);
        } else if (toOpen.equals("AllUserList")) {
            ((MatrimonyProfileListActivity) getActivity()).getMatrimonyUserFilterData().
                    setSection_type(Constants.MatrimonyModule.ALL_USERS_SECTION);
        }

        presenter.getAllUserList(((MatrimonyProfileListActivity) getActivity()).getMatrimonyUserFilterData(),
                BuildConfig.BASE_URL + String.format(Urls.Matrimony.ALL_FILTER_USERS));
    }

    private void initViews() {
        progressBar = view.findViewById(R.id.progress_bar);
        toolbar_back_action = view.findViewById(R.id.toolbar_back_action1);
        toolbar_back_action.setVisibility(View.VISIBLE);
        editSearch = view.findViewById(R.id.search_view1);
        toolbarFilter = view.findViewById(R.id.toolbar_filter);
        toolbar_action = view.findViewById(R.id.toolbar_action1);
        toolbar_action.setVisibility(View.VISIBLE);
        toolbar_title = view.findViewById(R.id.toolbar_title1);
        toolbar_title.setText("Candidate List");
        toolbar_back_action.setOnClickListener(this);
        toolbarFilter.setOnClickListener(this);
        toolbar_action.setOnClickListener(this);
        editSearch.setOnQueryTextListener(this);
        btnClearFilters = view.findViewById(R.id.btn_clear_filters);
        btnClearFilters.setOnClickListener(this);
        ivNoData = view.findViewById(R.id.iv_no_data);
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
//                                if (((ProfilesActivity) getActivity()).isFilterApplied() &&
//                                        ((ProfilesActivity) getActivity()).getFilterCandidatesData() != null) {
//                                    presenter.getFilteredUserList(((ProfilesActivity) getActivity()).
//                                            getFilterCandidatesData(), allUserProfileobj.getNext_page_url());
//                                } else {
                                presenter.getAllUserList(((MatrimonyProfileListActivity) getActivity()).getMatrimonyUserFilterData(), nextPageUrl);
//                                }
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
//                else {
//                    showMultiSelectBottomsheet("Filter", "filter", ListDrink);
//                }

                break;
            case R.id.toolbar_action1:
                if (isSearchVisible) {
                    isSearchVisible = false;
                    editSearch.setVisibility(View.VISIBLE);
                    editSearch.requestFocus();
                    toolbar_action.setImageResource(R.drawable.ic_close);
                } else {
                    isSearchVisible = true;
                    editSearch.setVisibility(View.GONE);
                    toolbar_action.setImageResource(R.drawable.ic_search);
                    filter("");
                }
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
                    meetIdReceived = getArguments().getString("meetId");
                    ((MatrimonyProfileListActivity) getActivity()).getMatrimonyUserFilterData().
                            setSection_type(Constants.MatrimonyModule.MEET_USERS_SECTION);
                    matrimonyUserFilterData.setMeet_id(meetIdReceived);
                    //((MatrimonyProfileListActivity) getActivity()).getMatrimonyUserFilterData().setMeet_id(meetIdReceived);
                    //presenter.getAllFiltersRequests(meetIdReceived);
                } else if (toOpen.equals("NewUserList")) {
                    //showUserProfileList((List<UserProfileList>) getIntent().getSerializableExtra("userList"));
                    matrimonyUserFilterData.setSection_type(Constants.MatrimonyModule.NEWLY_JOINED_SECTION);
//                    ((MatrimonyProfileListActivity) getActivity()).getMatrimonyUserFilterData().
//                            setSection_type(Constants.MatrimonyModule.NEWLY_JOINED_SECTION);
                } else if (toOpen.equals("UnverifiedUserList")) {
                    //showUserProfileList((List<UserProfileList>) getIntent().getSerializableExtra("userList"));
                    matrimonyUserFilterData.setSection_type(Constants.MatrimonyModule.VERIFICATION_PENDING_SECTION);
//                    ((MatrimonyProfileListActivity) getActivity()).getMatrimonyUserFilterData().
//                            setSection_type(Constants.MatrimonyModule.VERIFICATION_PENDING_SECTION);
                } else if (toOpen.equals("AllUserList")) {
                    matrimonyUserFilterData.setSection_type(Constants.MatrimonyModule.ALL_USERS_SECTION);
//                    ((MatrimonyProfileListActivity) getActivity()).getMatrimonyUserFilterData().
//                            setSection_type(Constants.MatrimonyModule.ALL_USERS_SECTION);
                }
                ((MatrimonyProfileListActivity) getActivity()).setMatrimonyUserFilterData(matrimonyUserFilterData);
                presenter.getAllUserList(((MatrimonyProfileListActivity) getActivity()).getMatrimonyUserFilterData(),
                        BuildConfig.BASE_URL + String.format(Urls.Matrimony.ALL_FILTER_USERS));
                break;
        }
    }

    public void showUserProfileList(AllUserData userResponse) {
        loading = true;
        nextPageUrl = userResponse.getNextPageUrl();
        if (userResponse.getData().size() > 0) {
            toolbarFilter.setVisibility(View.VISIBLE);
            ivNoData.setVisibility(View.GONE);
            userProfileListsFiltered = (ArrayList<UserProfileList>) userResponse.getData();
            userProfileLists.addAll(userProfileListsFiltered);
            matrimonyProfileListRecyclerAdapter.notifyDataSetChanged();
        } else {
            toolbarFilter.setVisibility(View.INVISIBLE);
            ivNoData.setVisibility(View.VISIBLE);
        }
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
        UserProfileList userProfileList = userProfileLists.get(pos);
        Gson gson = new Gson();
        String jsonInString = gson.toJson(userProfileList);
        Intent startMain1 = new Intent(getActivity(), MatrimonyProfileDetailsActivity.class);
        startMain1.putExtra("filter_type", jsonInString);
        startMain1.putExtra("meetid", meetIdReceived);
        startActivity(startMain1);
    }

    @Override
    public void onApproveClicked(int pos) {
        approvalType = Constants.APPROVE;
        String message = "Do you want to approve?";
        showApproveRejectDialog(getActivity(), pos, approvalType, message);
    }

    @Override
    public void onRejectClicked(int pos) {
        approvalType = Constants.REJECT;
        showReasonDialog(getActivity(), pos);
    }

    public void callRejectAPI(String strReason, int pos) {
        UserProfileList userProfileList = userProfileLists.get(pos);

        JSONObject jsonObject = presenter.createBodyParams(meetIdReceived, "user", userProfileList.get_id(), Constants.REJECT, strReason);
        presenter.approveRejectRequest(jsonObject, pos, Constants.REJECT);
        approvalType = Constants.REJECT;

    }

    public void callApproveAPI(int pos) {
        UserProfileList userProfileList = userProfileLists.get(pos);

        JSONObject jsonObject = presenter.createBodyParams(meetIdReceived, "user", userProfileList.get_id(), Constants.APPROVE, "");
        presenter.approveRejectRequest(jsonObject, pos, Constants.APPROVE);
        approvalType = Constants.APPROVE;

    }

    public void updateRequestStatus(String response, int position) {
        Util.showSuccessFailureToast(response, getActivity(), getActivity().getWindow().getDecorView()
                .findViewById(android.R.id.content));
        if (Constants.REJECT.equalsIgnoreCase(approvalType)) {
            userProfileLists.get(position).setIsApproved(Constants.REJECT);
            matrimonyProfileListRecyclerAdapter.notifyItemChanged(position);
            matrimonyProfileListRecyclerAdapter.notifyDataSetChanged();
        }
        if (Constants.APPROVE.equalsIgnoreCase(approvalType)) {
            userProfileLists.get(position).setIsApproved(Constants.APPROVE);
            matrimonyProfileListRecyclerAdapter.notifyItemChanged(position);
            matrimonyProfileListRecyclerAdapter.notifyDataSetChanged();
        }
    }

    private void showMultiSelectBottomsheet(String Title, String selectedOption, ArrayList<String> List) {
        bottomSheetDialogFragment = new SingleSelectBottomSheet(getActivity(), selectedOption, List, this::onValuesSelected);
        bottomSheetDialogFragment.show();
        bottomSheetDialogFragment.toolbarTitle.setText(Title);
        bottomSheetDialogFragment.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onValuesSelected(int selectedPosition, String spinnerName, String selectedValues) {
        userProfileLists.clear();
        currentSelectedFilter = selectedValues;
        updateUserListWithFilter(selectedValues);
    }

    private void updateUserListWithFilter(String selectedValues) {
        userProfileLists.clear();
        if ("Approved".equalsIgnoreCase(selectedValues)) {
            for (int i = 0; i < userProfileListsFiltered.size(); i++) {

                if (userProfileListsFiltered.get(i).getIsApproved().equalsIgnoreCase(selectedValues)) {
                    userProfileLists.add(userProfileListsFiltered.get(i));
                }
            }
            matrimonyProfileListRecyclerAdapter.notifyDataSetChanged();

        } else if ("Rejected".equalsIgnoreCase(selectedValues)) {
            for (int i = 0; i < userProfileListsFiltered.size(); i++) {

                if (userProfileListsFiltered.get(i).getIsApproved().equalsIgnoreCase(selectedValues)) {
                    userProfileLists.add(userProfileListsFiltered.get(i));
                }
            }
            matrimonyProfileListRecyclerAdapter.notifyDataSetChanged();

        } else if ("Pending".equalsIgnoreCase(selectedValues)) {
            for (int i = 0; i < userProfileListsFiltered.size(); i++) {

                if (userProfileListsFiltered.get(i).getIsApproved().equalsIgnoreCase(selectedValues)) {
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
        } else {
            ivNoData.setVisibility(View.VISIBLE);
        }
    }

    //ApproveReject Confirm dialog
    public void showApproveRejectDialog(final Activity context, int pos, String approvalType, String dialogMessage) {
        Dialog dialog;
        Button btnSubmit, btn_cancel;
        EditText edt_reason;
        TextView tv_message;
        Activity activity = context;

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_approve_reject_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tv_message = dialog.findViewById(R.id.tv_message);
        edt_reason = dialog.findViewById(R.id.edt_reason);
        btn_cancel = dialog.findViewById(R.id.btn_cancel);
        btnSubmit = dialog.findViewById(R.id.btn_submit);

        tv_message.setText(dialogMessage);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (approvalType.equalsIgnoreCase(Constants.APPROVE)) {
                    callApproveAPI(pos);
                }
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void setTxt_no_data() {
        ivNoData.setVisibility(View.VISIBLE);
    }

    public String showReasonDialog(final Activity context, int pos) {
        Dialog dialog;
        Button btnSubmit, btn_cancel;
        EditText edt_reason;
        Activity activity = context;

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_reason_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        edt_reason = dialog.findViewById(R.id.edt_reason);
        btn_cancel = dialog.findViewById(R.id.btn_cancel);
        btnSubmit = dialog.findViewById(R.id.btn_submit);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strReason = edt_reason.getText().toString();

                if (TextUtils.isEmpty(strReason)) {
                    Util.logger("Empty Reason", "Reason Can not be blank");
                    Util.snackBarToShowMsg(activity.getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "Reason Can not be blank",
                            Snackbar.LENGTH_LONG);
                } else {
                    onReceiveReason(strReason, pos);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
        return "";
    }

    public void onReceiveReason(String strReason, int pos) {
        callRejectAPI(strReason, pos);
    }

//    public void onNewProfileFetched(String requestID, AllUserData newUserResponse) {
//        loading = true;
//        nextPageUrl = newUserResponse.getNextPageUrl();
//        //showUserProfileList(newUserResponse.getData());
//    }
}
