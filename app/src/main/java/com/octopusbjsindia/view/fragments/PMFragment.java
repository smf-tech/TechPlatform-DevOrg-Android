package com.octopusbjsindia.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.PlatformTaskListener;
import com.octopusbjsindia.models.forms.FormStatusCount;
import com.octopusbjsindia.models.forms.FormStatusCountData;
import com.octopusbjsindia.models.user.UserInfo;
import com.octopusbjsindia.presenter.PMFragmentPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.HomeActivity;
import com.octopusbjsindia.view.adapters.FormsDashboardAdapter;

import java.util.ArrayList;
import java.util.Objects;

@SuppressWarnings("CanBeFinal")
// We have updated this fragment. We are not going to show forms section over here.So all the respective api and Ui code has been commented.
// Now we are showing forms dashboard data.
public class PMFragment extends Fragment implements View.OnClickListener, PlatformTaskListener {

    private View pmFragmentView;
    private int unsynchCount, savedCount;
    private ArrayList<FormStatusCountData> formStatusCountDataList = new ArrayList<>();
    private RecyclerView rvFormsStatusCount;
    private FormsDashboardAdapter formsDashboardAdapter;
    private FloatingActionButton fbSelect;
    private ExtendedFloatingActionButton fbNewForm, fbUserForms;
    private boolean isFABOpen = false;
    private Animation animFadeIn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null) {
            ((HomeActivity) getActivity()).setSyncButtonVisibility(true);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        pmFragmentView = inflater.inflate(R.layout.fragment_dashboard_forms, container, false);
        return pmFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        fbSelect = pmFragmentView.findViewById(R.id.fb_select);
        fbSelect.setOnClickListener(this);
        fbUserForms = pmFragmentView.findViewById(R.id.fb_user_forms);
        fbUserForms.setOnClickListener(this);
        fbNewForm = pmFragmentView.findViewById(R.id.fb_new_form);
        fbNewForm.setOnClickListener(this);
        rvFormsStatusCount = pmFragmentView.findViewById(R.id.rv_forms_dashboard);
        formsDashboardAdapter = new FormsDashboardAdapter(this, formStatusCountDataList);
        rvFormsStatusCount.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvFormsStatusCount.setAdapter(formsDashboardAdapter);

        rvFormsStatusCount.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy < -5 && fbSelect.getVisibility() != View.VISIBLE) {
                    fbSelect.setVisibility(View.VISIBLE);
                } else if (dy > 5 && fbSelect.getVisibility() == View.VISIBLE) {
                    fbSelect.setVisibility(View.INVISIBLE);
                }
            }
        });
        animFadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
    }

    @Override
    public void onResume() {
        super.onResume();
        closeFABMenu();
        if (Util.isConnected(getContext())) {
            UserInfo user = Util.getUserObjectFromPref();
            PMFragmentPresenter pmFragmentPresenter = new PMFragmentPresenter(this);
            pmFragmentPresenter.getAllFormsCount(user);
        } else {
            formStatusCountDataList.clear();
            populateOfflineData();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fb_select) {
            if (!isFABOpen) {
                showFabMenu();
            } else {
                closeFABMenu();
            }
        } else if (v.getId() == R.id.fb_user_forms) {
            Util.launchFragment(new FormsFragment(), getContext(),
                    getString(R.string.forms), true);
        } else if (v.getId() == R.id.fb_new_form) {
            Util.launchFragment(new AllFormsFragment(), getContext(),
                    getString(R.string.forms), true);
        }
    }

    private void showFabMenu() {
        fbNewForm.animate().translationY(-getResources().getDimension(R.dimen.standard_60));
        fbUserForms.animate().translationY(-getResources().getDimension(R.dimen.standard_120));
        fbNewForm.show();
        fbUserForms.show();
        fbNewForm.startAnimation(animFadeIn);
        fbUserForms.startAnimation(animFadeIn);
        fbSelect.setRotation(45);
        isFABOpen = true;
    }

    private void closeFABMenu() {
        fbNewForm.animate().translationY(0);
        fbUserForms.animate().translationY(0);
        fbNewForm.hide();
        fbUserForms.hide();
        fbSelect.setRotation(0);
        isFABOpen = false;
    }

    public void navigateToScreen(String status, int count) {
        if (count > 0) {
            Bundle b = new Bundle();
            b.putSerializable("TITLE", getString(R.string.forms));
            b.putBoolean("SHOW_BACK", true);
            b.putString("NAVIGATE_TO", status);

            FormsFragment fragment = new FormsFragment();
            fragment.setArguments(b);
            //FragmentManager fragmentManager = getFragmentManager();
            //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FragmentTransaction fragmentTransaction = ((HomeActivity) Objects
                    .requireNonNull(getContext()))
                    .getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.home_page_container, fragment);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.addToBackStack(fragment.getTag());
            fragmentTransaction.commit();
        } else {
            Util.showToast("There are no " + status + " forms available.", this);
        }
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public <T> void showNextScreen(T data) {
        populateData((FormStatusCount) data);
    }

    @Override
    public void showErrorMessage(String result) {

    }

    private void populateData(FormStatusCount formCount) {
        if (formCount != null) {
            formStatusCountDataList.clear();
            for (FormStatusCountData data : formCount.getData()) {
                if (data != null && data.getType() != null) {
                    formStatusCountDataList.add(data);
                }
            }
            populateOfflineData();
        }
    }

    private void populateOfflineData() {
        unsynchCount = DatabaseManager.getDBInstance(this.getActivity()).getNonSyncedPendingForms().size();
        savedCount = DatabaseManager.getDBInstance(this.getActivity()).getAllPartiallySavedForms().size();
        FormStatusCountData unsyncCountData = new FormStatusCountData();
        FormStatusCountData savedCountData = new FormStatusCountData();
        unsyncCountData.setType(Constants.PM.UNSYNC_STATUS);
        unsyncCountData.setCount(unsynchCount);
        savedCountData.setType(Constants.PM.SAVED_STATUS);
        savedCountData.setCount(savedCount);
        formStatusCountDataList.add(unsyncCountData);
        formStatusCountDataList.add(savedCountData);
        formsDashboardAdapter.notifyDataSetChanged();
    }
}
