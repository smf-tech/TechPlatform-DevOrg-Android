package com.platform.view.fragments;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.platform.R;
import com.platform.listeners.APIDataListener;
import com.platform.listeners.CustomSpinnerListener;
import com.platform.listeners.PlatformTaskListener;
import com.platform.models.Matrimony.MatrimonyRolesUsers;
import com.platform.models.Matrimony.MatrimonyUserDetails;
import com.platform.models.Matrimony.MeetReference;
import com.platform.models.common.CustomSpinnerObject;
import com.platform.models.events.CommonResponse;
import com.platform.models.leaves.YearlyHolidayData;
import com.platform.models.tm.Name_;
import com.platform.models.tm.SubFilterset;
import com.platform.presenter.CreateMeetSecondFragmentPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.CreateMatrimonyMeetActivity;
import com.platform.view.activities.TMFiltersListActivity;
import com.platform.view.adapters.MeetOrganizersReferencesAdapter;
import com.platform.view.customs.CustomSpinnerDialogClass;
import com.platform.widgets.MultiSelectSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateMeetSecondFragment extends Fragment implements View.OnClickListener, APIDataListener,CustomSpinnerListener {

    private CreateMeetSecondFragmentPresenter createMeetSecondFragmentPresenter;
    private TextView tvMeetOrganizers, tvMeetReferences;
    private Button btnSecondPartMeet, btnPublishMeet;
    RecyclerView rvMeetOrganizer, rvMeetReference;
    private MeetOrganizersReferencesAdapter meetOrganizersListAdapter, meetReferencesListAdapter;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private final String TAG = CreateMeetSecondFragment.class.getName();
    private ArrayList<CustomSpinnerObject> organizersSpinnerList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> nonOrganizersSpinnerList = new ArrayList<>();
    private ArrayList<MatrimonyUserDetails> organizersList = new ArrayList<>();
    private ArrayList<MatrimonyUserDetails> nonOrganizersList = new ArrayList<>();
    ArrayList<MatrimonyUserDetails> selectedOrganizersList = new ArrayList<>();
    ArrayList<MatrimonyUserDetails> selectedNonOrganizersList = new ArrayList<>();

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
    }

    private void init(View view) {
        progressBarLayout = view.findViewById(R.id.profile_act_progress_bar);
        progressBar = view.findViewById(R.id.pb_profile_act);
        tvMeetOrganizers = view.findViewById(R.id.tv_meet_organizers);
        tvMeetOrganizers.setOnClickListener(this);
        tvMeetReferences = view.findViewById(R.id.tv_meet_references);
        tvMeetReferences.setOnClickListener(this);
        btnSecondPartMeet = view.findViewById(R.id.btn_save_meet);
        btnSecondPartMeet.setOnClickListener(this);
        btnPublishMeet = view.findViewById(R.id.btn_publish_meet);
        btnPublishMeet.setOnClickListener(this);

        rvMeetOrganizer = view.findViewById(R.id.rv_meet_organizer);
        meetOrganizersListAdapter = new MeetOrganizersReferencesAdapter(selectedOrganizersList);
        rvMeetOrganizer.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMeetOrganizer.setAdapter(meetOrganizersListAdapter);

        rvMeetReference = view.findViewById(R.id.rv_meet_reference);
        meetReferencesListAdapter = new MeetOrganizersReferencesAdapter(selectedNonOrganizersList);
        rvMeetReference.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMeetReference.setAdapter(meetReferencesListAdapter);
        createMeetSecondFragmentPresenter = new CreateMeetSecondFragmentPresenter(this);
        createMeetSecondFragmentPresenter.getMatrimonyUsersList(((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().getLocation()
                .getCountry(),((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().getLocation()
                .getState(),((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().getLocation()
                .getCity());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save_meet:
                if(isAllDataValid()) {
                    updateMeetData(false);
                }
                break;

            case R.id.btn_publish_meet:
                if(isAllDataValid()) {
                    updateMeetData(true);
                }
                break;

            case R.id.tv_meet_organizers:
                CustomSpinnerDialogClass cdd = new CustomSpinnerDialogClass(getActivity(), this, "Organizers", organizersSpinnerList,
                        true);
                cdd.show();
                cdd.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.tv_meet_references:
                CustomSpinnerDialogClass cdd1 = new CustomSpinnerDialogClass(getActivity(), this, "References", nonOrganizersSpinnerList,
                        true);
                cdd1.show();
                cdd1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
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
        if (requestID.equalsIgnoreCase(CreateMeetSecondFragmentPresenter.SUBMIT_MEET)) {
            CommonResponse responseOBJ = new Gson().fromJson(response, CommonResponse.class);
            showResponseDialog("Confirmation", responseOBJ.getMessage(), "OK", "");
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

    public void setMatrimonyUsers(List<MatrimonyRolesUsers> matrimonyRolesUsersList) {
        if (matrimonyRolesUsersList.size() > 0) {
            for (MatrimonyRolesUsers matrimonyRole : matrimonyRolesUsersList) {
                for (MatrimonyUserDetails matrimonyUserDetails : matrimonyRole.getUserDetails()) {
                    matrimonyUserDetails.setRoleName(matrimonyRole.getDisplayName());

                    CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
                    customSpinnerObject.set_id(matrimonyUserDetails.getId());
                    customSpinnerObject.setName(matrimonyUserDetails.getName());
                    customSpinnerObject.setSelected(false);

                    if (matrimonyRole.getId().equals("5d4129ba5dda7642de492a72")) {
                        organizersList.add(matrimonyUserDetails);
                        organizersSpinnerList.add(customSpinnerObject);
                    } else {
                        nonOrganizersList.add(matrimonyUserDetails);
                        nonOrganizersSpinnerList.add(customSpinnerObject);
                    }
                }
            }
        } else {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), "No Organizers or References found at your meet location." +
                            "You need to add Oragnizers and References at meet location.",
                    Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        if (type.equalsIgnoreCase("Organizers")) {
            selectedOrganizersList.clear();
            for (CustomSpinnerObject c : organizersSpinnerList) {
                if (c.isSelected()) {
                    for (MatrimonyUserDetails m : organizersList) {
                        if (m.getId().equals(c.get_id())) {
                            selectedOrganizersList.add(m);
                        }
                    }
                }
            }
            meetOrganizersListAdapter.notifyDataSetChanged();
        } else if (type.equalsIgnoreCase("References")) {
            selectedNonOrganizersList.clear();
            for (CustomSpinnerObject cReference : nonOrganizersSpinnerList) {
                if (cReference.isSelected()) {
                    for (MatrimonyUserDetails mReference : nonOrganizersList) {
                        if (mReference.getId().equals(cReference.get_id())) {
                            selectedNonOrganizersList.add(mReference);
                        }
                    }
                }
            }
            meetReferencesListAdapter.notifyDataSetChanged();
        }
    }

    private void updateMeetData(boolean isPublish) {
        ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setMeetOrganizers(selectedOrganizersList);
        ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setMeetReferences(selectedNonOrganizersList);
        ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setMeetImageUrl("");
        if(isPublish){
            ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setIs_published(true);
            showPublishApiDialog("Confirmation", "This meet will be visible to users. " +
                    "Are you sure you want to publish?", "YES", "No");
        }else{
            ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setIs_published(false);
            createMeetSecondFragmentPresenter.submitMeet(((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet());
        }

    }

    public boolean isAllDataValid() {
        if(selectedOrganizersList.size() == 0){
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.select_meet_organizers_references_msg),
                    Snackbar.LENGTH_LONG);
            return false;
        }
        return true;
    }

    private void showResponseDialog(String dialogTitle, String message, String btn1String, String
            btn2String) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogs_leave_layout);

        if (!TextUtils.isEmpty(dialogTitle)) {
            TextView title = dialog.findViewById(R.id.tv_dialog_title);
            title.setText(dialogTitle);
            title.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(message)) {
            TextView text = dialog.findViewById(R.id.tv_dialog_subtext);
            text.setText(message);
            text.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(btn1String)) {
            Button button = dialog.findViewById(R.id.btn_dialog);
            button.setText(btn1String);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(v -> {
                // Close dialog
                dialog.dismiss();
                closeCurrentActivity();
            });
        }

        if (!TextUtils.isEmpty(btn2String)) {
            Button button1 = dialog.findViewById(R.id.btn_dialog_1);
            button1.setText(btn2String);
            button1.setVisibility(View.VISIBLE);
            button1.setOnClickListener(v -> {
                // Close dialog
            });
        }

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void showPublishApiDialog(String dialogTitle, String message, String btn1String, String
            btn2String) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogs_leave_layout);

        if (!TextUtils.isEmpty(dialogTitle)) {
            TextView title = dialog.findViewById(R.id.tv_dialog_title);
            title.setText(dialogTitle);
            title.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(message)) {
            TextView text = dialog.findViewById(R.id.tv_dialog_subtext);
            text.setText(message);
            text.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(btn1String)) {
            Button button = dialog.findViewById(R.id.btn_dialog);
            button.setText(btn1String);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(v -> {
                // Close dialog
                createMeetSecondFragmentPresenter.submitMeet(((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet());
                dialog.dismiss();
            });
        }

        if (!TextUtils.isEmpty(btn2String)) {
            Button button1 = dialog.findViewById(R.id.btn_dialog_1);
            button1.setText(btn2String);
            button1.setVisibility(View.VISIBLE);
            button1.setOnClickListener(v -> {
                // Close dialog
                dialog.dismiss();
            });
        }

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

}
