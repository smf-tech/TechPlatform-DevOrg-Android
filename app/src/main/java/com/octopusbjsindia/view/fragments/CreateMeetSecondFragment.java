package com.octopusbjsindia.view.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.Matrimony.SubordinateData;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.presenter.CreateMeetSecondFragmentPresenter;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.CreateMatrimonyMeetActivity;
import com.octopusbjsindia.view.adapters.MeetOrganizersReferencesAdapter;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;
import com.sagar.selectiverecycleviewinbottonsheetdialog.CustomBottomSheetDialogFragment;
import com.sagar.selectiverecycleviewinbottonsheetdialog.interfaces.CustomBottomSheetDialogInterface;
import com.sagar.selectiverecycleviewinbottonsheetdialog.model.SelectionListObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateMeetSecondFragment extends Fragment implements View.OnClickListener, APIDataListener, CustomBottomSheetDialogInterface {

    private CreateMeetSecondFragmentPresenter createMeetSecondFragmentPresenter;
    private TextView tvMeetSubordinates; //tvMeetOrganizers,
    private Button btnSecondPartMeet, btnPublishMeet;
    RecyclerView rvMeetSubordiantes;//rvMeetOrganizer,
    private MeetOrganizersReferencesAdapter meetSubordinatesAdapter;//, meetOrganizersListAdapter;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private final String TAG = CreateMeetSecondFragment.class.getName();
    //private ArrayList<CustomSpinnerObject> organizersSpinnerList = new ArrayList<>();
    private ArrayList<SelectionListObject> subordinatesSpinnerList = new ArrayList<>();
    //private ArrayList<MatrimonyUserDetails> organizersList = new ArrayList<>();
    private ArrayList<SubordinateData> subordinatesList = new ArrayList<>();
    //ArrayList<MatrimonyUserDetails> selectedOrganizersList = new ArrayList<>();
    ArrayList<SubordinateData> selectedSubordinatesList = new ArrayList<>();

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
//        tvMeetOrganizers = view.findViewById(R.id.tv_meet_organizers);
//        tvMeetOrganizers.setOnClickListener(this);
        tvMeetSubordinates = view.findViewById(R.id.tv_meet_subordinates);
        tvMeetSubordinates.setOnClickListener(this);
        btnSecondPartMeet = view.findViewById(R.id.btn_save_meet);
        btnSecondPartMeet.setOnClickListener(this);
        btnPublishMeet = view.findViewById(R.id.btn_publish_meet);
        btnPublishMeet.setOnClickListener(this);

//        rvMeetOrganizer = view.findViewById(R.id.rv_meet_organizer);
//        meetOrganizersListAdapter = new MeetOrganizersReferencesAdapter(selectedOrganizersList);
//        rvMeetOrganizer.setLayoutManager(new LinearLayoutManager(getActivity()));
//        rvMeetOrganizer.setAdapter(meetOrganizersListAdapter);

        rvMeetSubordiantes = view.findViewById(R.id.rv_meet_subordinates);
        meetSubordinatesAdapter = new MeetOrganizersReferencesAdapter(selectedSubordinatesList);
        rvMeetSubordiantes.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMeetSubordiantes.setAdapter(meetSubordinatesAdapter);
        createMeetSecondFragmentPresenter = new CreateMeetSecondFragmentPresenter(this);
//        createMeetSecondFragmentPresenter.getMatrimonyUsersList(((CreateMatrimonyMeetActivity) getActivity()).
//                getMatrimonyMeet().getLocation()
//                .getCountry(),((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().getLocation()
//                .getState(),((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().getLocation()
//                .getCity());
        createMeetSecondFragmentPresenter.getMatrimonySubordinatesList();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save_meet:
                if (isAllDataValid()) {
                    updateMeetData(false);
                }
                break;

            case R.id.btn_publish_meet:
                if (isAllDataValid()) {
                    updateMeetData(true);
                }
                break;
/*            case R.id.tv_meet_organizers:
                CustomSpinnerDialogClass cdd = new CustomSpinnerDialogClass(getActivity(), this, "Organizers", organizersSpinnerList,
                        true);
                cdd.show();
                cdd.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;*/
            case R.id.tv_meet_subordinates:

                CustomBottomSheetDialogFragment customBottomsheet = null;
                showSelectiveBottomSheet(customBottomsheet, "Team Members", subordinatesSpinnerList, true);

                break;
        }
    }

    private void showSelectiveBottomSheet(CustomBottomSheetDialogFragment bottomSheet, String Title, ArrayList<SelectionListObject> List, Boolean isMultiSelect) {
        bottomSheet = new CustomBottomSheetDialogFragment(
                this, Title, List, isMultiSelect);
        bottomSheet.show(requireActivity().getSupportFragmentManager(),
                CustomBottomSheetDialogFragment.TAG);
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
        //your background thread is still running. By the time that thread reaches the getActivity().runOnUiThread()
        // code,the activity no longer exists. So check if the activity still exists.
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (progressBarLayout != null && progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                    progressBarLayout.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void closeCurrentActivity() {
//        if (getActivity() != null) {
//            getActivity().onBackPressed();
//        }
        getActivity().finish();
    }

    public void setMatrimonyUsers(List<SubordinateData> matrimonySubordinatesList) {
        if (matrimonySubordinatesList.size() > 0) {

            ArrayList<SelectionListObject> tempList = new ArrayList<>();
            for (SubordinateData subordinateData : matrimonySubordinatesList) {
                tempList.add(new SelectionListObject(subordinateData.getUser_id(),
                        subordinateData.getName(), false, false));
                subordinatesList.add(subordinateData);
            }
            subordinatesSpinnerList.addAll(tempList);

        } else {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), "No members found in your team." +
                            "You need to add members.",
                    Snackbar.LENGTH_LONG);
        }

    }

    public void showDataMessage(String message) {
        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                        .findViewById(android.R.id.content), "No members found in your team." +
                        "You need to add members.",
                Snackbar.LENGTH_LONG);
    }

    private void updateMeetData(boolean isPublish) {
        //((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setMeetOrganizers(selectedOrganizersList);
        ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setMeetSubordinators(selectedSubordinatesList);
        if (isPublish) {
            ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setIs_published(true);
            showPublishApiDialog("Confirmation", "This meet will be visible to users. " +
                    "Are you sure you want to publish?", "YES", "No");
        } else {
            ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setIs_published(false);
            createMeetSecondFragmentPresenter.submitMeet(((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet());
        }
    }

    public boolean isAllDataValid() {
        if (selectedSubordinatesList.size() == 0) {
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
//                MatrimonyMeetDetailFragment.getInstance().updateMeetList();
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

    @Override
    public void onCustomBottomSheetSelection(@NonNull String type) {
        if (type.equalsIgnoreCase("Team Members")) {

            selectedSubordinatesList.clear();
            for (SelectionListObject obj : subordinatesSpinnerList) {
                if (obj.isSelected()) {
                    //selectedQualificationDegreeList.add(obj.getValue());
                    for (SubordinateData mReference : subordinatesList) {
                        if (mReference.getUser_id().equals(obj.getId())) {
                            selectedSubordinatesList.add(mReference);
                        }
                    }
                }
            }
            meetSubordinatesAdapter.notifyDataSetChanged();

        }

    }
}
