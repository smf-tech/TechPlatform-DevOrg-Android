package com.platform.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.platform.R;
import com.platform.models.Matrimony.UserProfileList;
import com.platform.presenter.MatrimonyProfilesListActivityPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.adapters.MatrimonyProfileListRecyclerAdapter;
import com.platform.widgets.MultiSelectBottomSheet;
import com.platform.widgets.SingleSelectBottomSheet;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CanBeFinal")
public class MatrimonyProfileListActivity extends BaseActivity implements View.OnClickListener, MatrimonyProfileListRecyclerAdapter.OnRequestItemClicked, MatrimonyProfileListRecyclerAdapter.OnApproveRejectClicked,SingleSelectBottomSheet.MultiSpinnerListener{
    private SingleSelectBottomSheet bottomSheetDialogFragment;
    private MatrimonyProfilesListActivityPresenter tmFilterListActivityPresenter;
    private MatrimonyProfileListRecyclerAdapter matrimonyProfileListRecyclerAdapter;
    private RecyclerView rv_matrimonyprofileview;
    private ArrayList<UserProfileList> userProfileLists =  new ArrayList<>();
    private ArrayList<UserProfileList> userProfileListsFiltered =new ArrayList<>();
    private String approvalType;
    private String meetIdReceived;

    private ImageView toolbar_back_action,toolbar_edit_action;
    private TextView toolbar_title,txt_no_data;
    ArrayList<String> ListDrink = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mprofilelist_layout);
        //receive intent data
        meetIdReceived = getIntent().getStringExtra("meetid");
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        //initViews();
        tmFilterListActivityPresenter.getAllFiltersRequests(meetIdReceived);
    }

    private void initViews() {

        txt_no_data = findViewById(R.id.txt_no_data);
        txt_no_data.setVisibility(View.VISIBLE);
        toolbar_back_action= findViewById(R.id.toolbar_back_action);
        toolbar_back_action.setVisibility(View.VISIBLE);

        toolbar_edit_action = findViewById(R.id.toolbar_edit_action);
        toolbar_edit_action.setVisibility(View.INVISIBLE);
        toolbar_edit_action.setImageResource(R.drawable.ic_approved_icon_filter);


        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("Profile");

        toolbar_back_action.setOnClickListener(this);
        toolbar_edit_action.setOnClickListener(this);

        rv_matrimonyprofileview = findViewById(R.id.rv_matrimonyprofileview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        rv_matrimonyprofileview.setLayoutManager(layoutManager);

        tmFilterListActivityPresenter = new MatrimonyProfilesListActivityPresenter(this);

        CreateFilterList();
        matrimonyProfileListRecyclerAdapter = new MatrimonyProfileListRecyclerAdapter(this, userProfileLists,
                this, this);
        rv_matrimonyprofileview.setAdapter(matrimonyProfileListRecyclerAdapter);
    }

    private void CreateFilterList() {
        /*ListDrink.add("Male");
        ListDrink.add("Female");
        ListDrink.add("Paid");
        ListDrink.add("Yet to pay");
        ListDrink.add("Approved");
        ListDrink.add("Rejected");
        ListDrink.add("Deleted");*/

        ListDrink.add("Approved");
        ListDrink.add("Rejected");
        ListDrink.add("Pending");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_filtertype:
                //onBackPressed();
                break;
            case R.id.img_filter_image:

                break;
            case R.id.toolbar_back_action:
                    finish();
                break;
            case R.id.toolbar_edit_action:
                showMultiSelectBottomsheet("Filter","filter",ListDrink);
                break;

        }
    }

    /*private void setActionbar(String title) {
        if (title.contains("\n")) {
            title = title.replace("\n", " ");
        }

        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);

        ImageView img_back = findViewById(R.id.toolbar_back_action);
        img_back.setVisibility(View.VISIBLE);
        img_back.setOnClickListener(this);
    }
*/

    public void showPendingApprovalRequests(List<UserProfileList> pendingRequestList) {
        if (!pendingRequestList.isEmpty()) {
            userProfileListsFiltered = (ArrayList<UserProfileList>) pendingRequestList;
            for (int i = 0; i <userProfileListsFiltered.size(); i++) {
                userProfileLists.add(userProfileListsFiltered.get(i));
            }


            /*matrimonyProfileListRecyclerAdapter = new MatrimonyProfileListRecyclerAdapter(this, userProfileLists,
                    this, this);*/
           // rv_matrimonyprofileview.setAdapter(matrimonyProfileListRecyclerAdapter);
            matrimonyProfileListRecyclerAdapter.notifyDataSetChanged();
            txt_no_data.setVisibility(View.GONE);
            if (pendingRequestList!=null &&pendingRequestList.size()>0) {
                toolbar_edit_action.setVisibility(View.VISIBLE);
            }else {
                toolbar_edit_action.setVisibility(View.GONE);
            }
        } else {
            txt_no_data.setVisibility(View.VISIBLE);
            toolbar_edit_action.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClicked(int pos) {
        //Util.showToast("item ->" + pos + " Open detais", this);
        UserProfileList userProfileList = userProfileLists.get(pos);
        Gson gson = new Gson();
        String jsonInString = gson.toJson(userProfileList);
        Intent startMain1 = new Intent(MatrimonyProfileListActivity.this, MatrimonyProfileDetailsActivity.class);
        startMain1.putExtra("filter_type", jsonInString);
        startMain1.putExtra("meetid",meetIdReceived);
        startActivity(startMain1);
    }

    @Override
    public void onApproveClicked(int pos) {
        Util.showToast("item ->" + pos + " Approve Clicked", this);
        UserProfileList userProfileList = userProfileLists.get(pos);

        JSONObject jsonObject = tmFilterListActivityPresenter.createBodyParams(meetIdReceived, "user", userProfileList.get_id(), Constants.APPROVE);
        tmFilterListActivityPresenter.approveRejectRequest(jsonObject, 0, Constants.APPROVE);
    }

    @Override
    public void onRejectClicked(int pos) {
        Util.showToast("item ->" + pos + " Reject Clicked", this);
        UserProfileList userProfileList = userProfileLists.get(pos);

        JSONObject jsonObject = tmFilterListActivityPresenter.createBodyParams(meetIdReceived, "user", userProfileList.get_id(), Constants.REJECT);
        tmFilterListActivityPresenter.approveRejectRequest(jsonObject, 0, Constants.REJECT);
    }

    public void updateRequestStatus(String response, int position) {
        if (Constants.REJECT.equalsIgnoreCase(approvalType)) {
            userProfileLists.get(position).setIsApproved(Constants.APPROVE);
        }
        if (Constants.APPROVE.equalsIgnoreCase(approvalType)) {
            userProfileLists.get(position).setIsApproved(Constants.REJECT);
        }
    }


    private void showMultiSelectBottomsheet(String Title,String selectedOption, ArrayList<String> List) {

        bottomSheetDialogFragment = new SingleSelectBottomSheet(this, selectedOption, List, this::onValuesSelected);
        bottomSheetDialogFragment.show();
        bottomSheetDialogFragment.toolbarTitle.setText(Title);
        bottomSheetDialogFragment.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }


    @Override
    public void onValuesSelected(int selectedPosition, String spinnerName, String selectedValues) {
        userProfileLists.clear();
        if ("Approved".equalsIgnoreCase(selectedValues)){
            for (int i = 0; i < userProfileListsFiltered.size(); i++) {

                if (userProfileListsFiltered.get(i).getIsApproved().equalsIgnoreCase(selectedValues)){
                    userProfileLists.add(userProfileListsFiltered.get(i));
                }
            }
            matrimonyProfileListRecyclerAdapter.notifyDataSetChanged();

        }else if ("Rejected".equalsIgnoreCase(selectedValues)){
            for (int i = 0; i < userProfileListsFiltered.size(); i++) {

                if (userProfileListsFiltered.get(i).getIsApproved().equalsIgnoreCase(selectedValues)){
                    userProfileLists.add(userProfileListsFiltered.get(i));
                }
            }
            matrimonyProfileListRecyclerAdapter.notifyDataSetChanged();

        }else if ("Pending".equalsIgnoreCase(selectedValues)){
            for (int i = 0; i < userProfileListsFiltered.size(); i++) {

                if (userProfileListsFiltered.get(i).getIsApproved().equalsIgnoreCase(selectedValues)){
                    userProfileLists.add(userProfileListsFiltered.get(i));
                }
            }
            matrimonyProfileListRecyclerAdapter.notifyDataSetChanged();
        }else {
            for (int i = 0; i <userProfileListsFiltered.size(); i++) {
                userProfileLists.add(userProfileListsFiltered.get(i));
            }
            matrimonyProfileListRecyclerAdapter.notifyDataSetChanged();
        }
    }
}
