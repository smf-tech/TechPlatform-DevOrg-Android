package com.platform.view.fragments;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.android.volley.VolleyError;
import com.platform.R;
import com.platform.listeners.APIDataListener;
import com.platform.listeners.PlatformTaskListener;
import com.platform.models.leaves.YearlyHolidayData;
import com.platform.presenter.CreateMeetSecondFragmentPresenter;
import com.platform.utility.Constants;
import com.platform.view.activities.CreateMatrimonyMeetActivity;
import com.platform.view.adapters.MeetOrganizersReferencesAdapter;
import com.platform.widgets.MultiSelectSpinner;

import java.util.ArrayList;
import java.util.List;

public class CreateMeetSecondFragment extends Fragment implements View.OnClickListener, APIDataListener,
        AdapterView.OnItemSelectedListener, MultiSelectSpinner.MultiSpinnerListener {

    private CreateMeetSecondFragmentPresenter createMeetSecondFragmentPresenter;
    // private Spinner spinnerOrganizer;
    private MultiSelectSpinner spinnerOrganizer, spinnerReferences;
    List<String> meetOrganizers = new ArrayList<>();
    List<String> meetReferences = new ArrayList<>();
    List<String> selectedMeetReferences = new ArrayList<>();
    private ArrayAdapter<String> meetOragnizersAdapter, meetReferencesAdapter;
    private Button btnSecondPartMeet;
    RecyclerView rvMeetOrganizer, rvMeetReference;
    private MeetOrganizersReferencesAdapter meetOrganizersListAdapter, meetReferencesListAdapter;
    private final List<YearlyHolidayData> meetOrganizersList = new ArrayList<>();
    private final List<YearlyHolidayData> meetReferencesList = new ArrayList<>();
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private final String TAG = CreateMeetSecondFragment.class.getName();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_meet_second, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setMeetData();
    }

    private void init(View view) {
        meetOrganizers.add("State President");
        meetOrganizers.add("District President");
        meetOrganizers.add("Chapter President");
        spinnerOrganizer = view.findViewById(R.id.spinner_meet_organizer);
        spinnerOrganizer.setSpinnerName(Constants.Matrimony.ORGANIZERS_LABEL);
        spinnerOrganizer.setItems(meetOrganizers, "Organizers", this);
        //spinnerOrganizer.setOnItemSelectedListener(this);
        //meetOragizers.add("Select Organizer");
//        meetOragnizersAdapter = new ArrayAdapter<>(getActivity(),
//                android.R.layout.simple_spinner_item, meetOragizers);
//        meetOragnizersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerOrganizer.setAdapter(meetOragnizersAdapter);

        meetReferences.add("State President");
        meetReferences.add("District President");
        meetReferences.add("Chapter President");
        spinnerReferences = view.findViewById(R.id.spinner_meet_references);
        spinnerReferences.setSpinnerName(Constants.Matrimony.REFERENCES_LABEL);
        spinnerReferences.setItems(meetReferences, "References", this);

        progressBarLayout = view.findViewById(R.id.profile_act_progress_bar);
        progressBar = view.findViewById(R.id.pb_profile_act);
        btnSecondPartMeet = view.findViewById(R.id.btn_second_part_meet);
        btnSecondPartMeet.setOnClickListener(this);

        rvMeetOrganizer = view.findViewById(R.id.rv_meet_organizer);
        meetOrganizersListAdapter = new MeetOrganizersReferencesAdapter(meetOrganizersList);
        rvMeetOrganizer.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMeetOrganizer.setAdapter(meetOrganizersListAdapter);

        rvMeetReference = view.findViewById(R.id.rv_meet_organizer);
        meetReferencesListAdapter = new MeetOrganizersReferencesAdapter(meetReferencesList);
        rvMeetReference.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMeetReference.setAdapter(meetReferencesListAdapter);

        rvMeetReference = view.findViewById(R.id.rv_meet_reference);
        createMeetSecondFragmentPresenter = new CreateMeetSecondFragmentPresenter(this);
        createMeetSecondFragmentPresenter.getMeetOrganizersList();
    }

    private void setMeetData() {
        //((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setMeetDateTime("12/08/2019");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_second_part_meet:
                createMeetSecondFragmentPresenter.submitMeet(((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet());
                break;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (createMeetSecondFragmentPresenter != null) {
            createMeetSecondFragmentPresenter.clearData();
            createMeetSecondFragmentPresenter = null;
        }
    }

    @Override
    public void onFailureListener(String requestID, String message) {

    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        ((CreateMatrimonyMeetActivity) getActivity()).openFragment("CreateMeetThirdFragment");
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onValuesSelected(boolean[] selected, String spinnerName) {
        try {
            switch (spinnerName) {
                case Constants.Matrimony.REFERENCES_LABEL:
                    selectedMeetReferences.clear();
                    for (int i = 0; i < selected.length; i++) {
                        if (selected[i]) {
                            selectedMeetReferences.add(meetReferences.get(i));
                        }
                    }
                    if(selectedMeetReferences.size()>0){
                        createMeetSecondFragmentPresenter.getMeetReferencesList();
                    }
                    break;
            }
        } catch (Exception e){
            Log.e(TAG, "EXCEPTION_IN_ON_VALUE_SELECTED");
        }
    }
}
