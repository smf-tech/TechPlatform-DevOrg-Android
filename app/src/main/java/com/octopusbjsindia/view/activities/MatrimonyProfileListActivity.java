package com.octopusbjsindia.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.VolleyError;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.Matrimony.MatrimonyUserFilterData;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.view.adapters.MatrimonyProfileListRecyclerAdapter;
import com.octopusbjsindia.view.fragments.MatrimonyProfileListFragment;
import com.octopusbjsindia.widgets.SingleSelectBottomSheet;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CanBeFinal")
public class MatrimonyProfileListActivity extends BaseActivity implements View.OnClickListener,
        MatrimonyProfileListRecyclerAdapter.OnRequestItemClicked,
        MatrimonyProfileListRecyclerAdapter.OnApproveRejectClicked,
        SingleSelectBottomSheet.MultiSpinnerListener,
        SearchView.OnQueryTextListener,
        APIDataListener {
    private String toOpen = "";
    private String meetId;
    private boolean isFilterApplied = false;
    private MatrimonyUserFilterData matrimonyUserFilterData;
    private String minAge, maxAge;
    // FilterCandidateFragment arraylists
    private List<String> minMaxAgeList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> stateList = new ArrayList<>();

    public ArrayList<CustomSpinnerObject> getMeetStatusList() {
        return meetStatusList;
    }

    public void setMeetStatusList(ArrayList<CustomSpinnerObject> meetStatusList) {
        this.meetStatusList = meetStatusList;
    }

    public ArrayList<CustomSpinnerObject> getVerificationStatusList() {
        return verificationStatusList;
    }

    public void setVerificationStatusList(ArrayList<CustomSpinnerObject> verificationStatusList) {
        this.verificationStatusList = verificationStatusList;
    }

    private ArrayList<CustomSpinnerObject> meetStatusList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> verificationStatusList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> qualificationList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> genderList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> sectList = new ArrayList<>();

    public ArrayList<CustomSpinnerObject> getPaidOrFreeList() {
        return paidOrFreeList;
    }

    public void setPaidOrFreeList(ArrayList<CustomSpinnerObject> paidOrFreeList) {
        this.paidOrFreeList = paidOrFreeList;
    }

    private ArrayList<CustomSpinnerObject> paidOrFreeList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> maritalStatusList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mprofilelist_layout);
        //receive intent data

        //initViews();
        toOpen = getIntent().getStringExtra("toOpean");
        if (toOpen.equalsIgnoreCase("MeetUserList")) {
            meetId = getIntent().getStringExtra("meetid");
        }
        initViews();
    }

    public void openFragment(String switchToFragment) {
        FragmentManager fManager = getSupportFragmentManager();
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        if (!TextUtils.isEmpty(switchToFragment)) {
            switch (switchToFragment) {
                case "profile_list_fragment":
                    fragment = new MatrimonyProfileListFragment();
                    bundle.putString("toOpen", toOpen);
                    fragment.setArguments(bundle);
                    break;
                case "filter_fragment":
                    fragment = new MatrimonyUsersFilterFragment();
                    fragment.setArguments(bundle);
                    break;
            }
        }
        // Begin transaction.
        //getSupportFragmentManager().popBackStack();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.replace(R.id.ly_main, fragment).addToBackStack(null)
                .commit();
    }

    public String getMeetId() {
        return meetId;
    }

    public void setMeetId(String meetId) {
        this.meetId = meetId;
    }

    public boolean isFilterApplied() {
        return isFilterApplied;
    }

    public void setFilterApplied(boolean filterApplied) {
        isFilterApplied = filterApplied;
    }

    public MatrimonyUserFilterData getMatrimonyUserFilterData() {
        return matrimonyUserFilterData;
    }

    public void clearFilterCandidtaesData() {
        this.matrimonyUserFilterData = null;
    }

    public void setMatrimonyUserFilterData(MatrimonyUserFilterData matrimonyUserFilterData) {
        this.matrimonyUserFilterData = matrimonyUserFilterData;
    }

    public String getMinAge() {
        return minAge;
    }

    public void setMinAge(String minAge) {
        this.minAge = minAge;
    }

    public String getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(String maxAge) {
        this.maxAge = maxAge;
    }

    public List<String> getMinMaxAgeList() {
        return minMaxAgeList;
    }

    public void setMinMaxAgeList(List<String> minMaxAgeList) {
        this.minMaxAgeList = minMaxAgeList;
    }

    public ArrayList<CustomSpinnerObject> getStateList() {
        return stateList;
    }

    public void setStateList(ArrayList<CustomSpinnerObject> stateList) {
        this.stateList = stateList;
    }

    public ArrayList<CustomSpinnerObject> getQualificationList() {
        return qualificationList;
    }

    public void setQualificationList(ArrayList<CustomSpinnerObject> qualificationList) {
        this.qualificationList = qualificationList;
    }

    public ArrayList<CustomSpinnerObject> getGenderList() {
        return genderList;
    }

    public void setGenderList(ArrayList<CustomSpinnerObject> genderList) {
        this.genderList = genderList;
    }

    public ArrayList<CustomSpinnerObject> getSectList() {
        return sectList;
    }

    public void setSectList(ArrayList<CustomSpinnerObject> sectList) {
        this.sectList = sectList;
    }


    public ArrayList<CustomSpinnerObject> getMaritalStatusList() {
        return maritalStatusList;
    }

    public void setMaritalStatusList(ArrayList<CustomSpinnerObject> maritalStatusList) {
        this.maritalStatusList = maritalStatusList;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initViews() {
        matrimonyUserFilterData = new MatrimonyUserFilterData();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        MatrimonyProfileListFragment matrimonyProfileListFragment = new MatrimonyProfileListFragment();
        bundle.putString("toOpen", toOpen);
        matrimonyProfileListFragment.setArguments(bundle);
        transaction.replace(R.id.ly_main, matrimonyProfileListFragment).addToBackStack(null);
        transaction.commit();

    }

    public void handleBackPressed(Fragment f) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment instanceof MatrimonyUsersFilterFragment) {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    @Override
    public void onBackPressed() {

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment instanceof MatrimonyUsersFilterFragment) {
                getSupportFragmentManager().popBackStack();
                break;
            }else if (fragment != null && fragment instanceof MatrimonyProfileListFragment) {
                finish();
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClicked(int pos) {
    }

    @Override
    public void onApproveClicked(int pos) {
    }

    @Override
    public void onRejectClicked(int pos) {

    }

    @Override
    public void onValuesSelected(int selectedPosition, String spinnerName, String selectedValues) {
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        //Util.showToast(message, this);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
    }

    @Override
    public void onSuccessListener(String requestID, String response) {

    }

    @Override
    public void showProgressBar() {
    }

    @Override
    public void hideProgressBar() {
    }

    @Override
    public void closeCurrentActivity() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
