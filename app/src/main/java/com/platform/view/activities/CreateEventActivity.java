package com.platform.view.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.listeners.PlatformTaskListener;
import com.platform.models.events.AddForm;
import com.platform.models.events.Event;
import com.platform.models.events.EventLocation;
import com.platform.models.events.EventsResponse;
import com.platform.models.events.Participant;
import com.platform.models.events.Recurrence;
import com.platform.models.events.RegistrationSchedule;
import com.platform.models.user.UserInfo;
import com.platform.presenter.CreateEventActivityPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Permissions;
import com.platform.utility.Util;
import com.platform.view.adapters.AddMembersListAdapter;
import com.platform.widgets.MultiSelectSpinner;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CreateEventActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener,View.OnClickListener, PlatformTaskListener, MultiSelectSpinner.MultiSpinnerListener {

    private AddMembersListAdapter addMembersListAdapter;

    private ArrayList<Participant> membersList = new ArrayList<>();
    private Event event;

    private ImageView ivBackIcon;
    private EditText etTitle;
    private EditText etStartDate;
    private EditText etEndDate;
    private EditText etStartTime;
    private EditText etEndTime;
    private EditText etDescription;
    private EditText etAddress;
    private EditText etAddMembers;
    private EditText etRegistrationStartDate;
    private EditText etRegistrationEndDate;
    private Button btEventSubmit;
    private MultiSelectSpinner spAddForms;
    private CheckBox cbIsAttendanceRequired;
    private CheckBox cbIsRegistrationRequired;
    private ImageView eventPic;

    private Uri outputUri;
    private Uri finalUri;
    private boolean mImageUploaded;
    private String mUploadedImageUrl;

    ArrayList<AddForm> formsList;
    private ArrayList<String> selectedForms = new ArrayList<>();
    private RelativeLayout progressBarLayout;
    private ProgressBar progressBar;
    private CreateEventActivityPresenter createEventPresenter;
    private String toOpen;
    private final String TAG = EditProfileActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        initView();
    }

    private void initView() {

        formsList = new ArrayList<AddForm>();

        progressBarLayout = findViewById(R.id.profile_act_progress_bar);
        progressBar = findViewById(R.id.pb_profile_act);
        createEventPresenter = new CreateEventActivityPresenter(this);
        createEventPresenter.getFormData(Util.getUserObjectFromPref().getProjectIds());

        toOpen = getIntent().getStringExtra(Constants.Planner.TO_OPEN);
        event = (Event) getIntent().getSerializableExtra(Constants.Planner.EVENT_DETAIL);

        ivBackIcon = findViewById(R.id.toolbar_back_action);
        etTitle = findViewById(R.id.et_title);
        etStartDate = findViewById(R.id.et_start_date);
        etEndDate = findViewById(R.id.et_end_date);
        etStartTime = findViewById(R.id.et_start_time);
        etEndTime = findViewById(R.id.et_end_time);
        etDescription = findViewById(R.id.et_description);
        etAddress = findViewById(R.id.et_address);
        cbIsAttendanceRequired = findViewById(R.id.cb_is_attendance_required);
        cbIsRegistrationRequired = findViewById(R.id.cb_is_registration_required);
        etAddMembers = findViewById(R.id.et_add_members);
        spAddForms = findViewById(R.id.sp_add_forms);
        spAddForms.setSpinnerName(Constants.Planner.SPINNER_ADD_FORMS);
        etRegistrationStartDate = findViewById(R.id.et_registration_start_date);
        etRegistrationEndDate = findViewById(R.id.et_registration_end_date);
        eventPic = findViewById(R.id.event_pic);
        btEventSubmit = findViewById(R.id.bt_event_submit);
        RecyclerView rvAttendeesList = findViewById(R.id.rv_attendees_list);


        // Task Module UI changes
//        if (toOpen.equalsIgnoreCase(Constants.Planner.TASKS_LABEL)) {
//            TextInputLayout tlyEndDate = findViewById(R.id.tly_end_date);
//            tlyEndDate.setVisibility(View.VISIBLE);
//            TextInputLayout tlyAddress = findViewById(R.id.tly_address);
//            tlyAddress.setVisibility(View.GONE);
//        }
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.category_types, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spCategory.setAdapter(adapter);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spCategory.setAdapter(adapter);

        addMembersListAdapter = new AddMembersListAdapter(CreateEventActivity.this, membersList, false, false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvAttendeesList.setLayoutManager(mLayoutManager);
        rvAttendeesList.setAdapter(addMembersListAdapter);

        if (event != null) {
            if (toOpen.equalsIgnoreCase(Constants.Planner.TASKS_LABEL)) {
                setActionbar(getString(R.string.edit_task));
//                etEndDate.setText(Util.getDateFromTimestamp(event.getEventEndDateTime()));
                findViewById(R.id.rl_add_members).setVisibility(View.GONE);
            } else {
                setActionbar(getString(R.string.edit_event));
            }

            btEventSubmit.setText(getString(R.string.btn_submit));

            setAllData();

            ImageView toolbarAction = findViewById(R.id.toolbar_edit_action);
            toolbarAction.setVisibility(View.VISIBLE);
            toolbarAction.setImageResource(R.drawable.ic_delete);
        } else {
            if (toOpen.equalsIgnoreCase(Constants.Planner.TASKS_LABEL)) {
                setActionbar(getString(R.string.create_task));
                findViewById(R.id.rl_add_members).setVisibility(View.GONE);
                btEventSubmit.setText(getString(R.string.create_task));
            } else {
                setActionbar(getString(R.string.create_event));
            }
        }

        setListeners();
    }

    private void setAllData() {
//        ArrayAdapter myAdapter = (ArrayAdapter) spCategory.getAdapter();
//        int spinnerPosition = myAdapter.getPosition(event.getEventType());
//        spCategory.setSelection(spinnerPosition);
//        etTitle.setText(event.getTitle());
//        etStartDate.setText(Util.getDateFromTimestamp(event.getEventStartDateTime()));
//        etStartTime.setText(Util.getTimeFromTimeStamp(event.getEventStartDateTime()));
//        etEndTime.setText(Util.getTimeFromTimeStamp(event.getEventEndDateTime()));
//        etDescription.setText(event.getEventDescription());
//        etAddress.setText(event.getAddress());
//        if (toOpen.equalsIgnoreCase(Constants.Planner.EVENTS_LABEL)) {
//            setAdapter(event.getMembersList());
//        }
    }

    private void setListeners() {
        ivBackIcon.setOnClickListener(this);
        etStartDate.setOnClickListener(this);
        etEndDate.setOnClickListener(this);
        etStartTime.setOnClickListener(this);
        etEndTime.setOnClickListener(this);
        etAddMembers.setOnClickListener(this);
        btEventSubmit.setOnClickListener(this);
        cbIsAttendanceRequired.setOnCheckedChangeListener(this);
        cbIsRegistrationRequired.setOnCheckedChangeListener(this);
        etRegistrationStartDate.setOnClickListener(this);
        etRegistrationEndDate.setOnClickListener(this);
        eventPic.setOnClickListener(this);
    }

    private void setAdapter(ArrayList<Participant> participants) {
        membersList.addAll(participants);
        addMembersListAdapter.notifyDataSetChanged();
    }

    private void setActionbar(String title) {
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back_action:
                finish();
                break;

            case R.id.et_start_date:
                Util.showDateDialogMin(CreateEventActivity.this, findViewById(R.id.et_start_date));
                break;

            case R.id.et_end_date:
                Util.showDateDialogMin(CreateEventActivity.this, findViewById(R.id.et_end_date));
                break;

            case R.id.et_start_time:
                Util.showTimeDialog(CreateEventActivity.this, findViewById(R.id.et_start_time));
                break;

            case R.id.et_end_time:
                Util.showTimeDialog(CreateEventActivity.this, findViewById(R.id.et_end_time));
                break;

            case R.id.et_add_members:
                Intent intentAddMemberFilerActivity = new Intent(this, AddMembersFilterActivity.class);
                this.startActivityForResult(intentAddMemberFilerActivity, Constants.Planner.MEMBER_LIST);
                break;

            case R.id.et_registration_start_date:
                Util.showDateDialogMin(CreateEventActivity.this, findViewById(R.id.et_registration_start_date));
                break;

            case R.id.et_registration_end_date:
                Util.showDateDialogMin(CreateEventActivity.this, findViewById(R.id.et_registration_end_date));
                break;

            case R.id.event_pic:
                onAddImageClick();
                break;

            case R.id.bt_event_submit:
                submitDetails();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_is_attendance_required:
//                if (isChecked) {
//                    findViewById(R.id.rl_add_members).setVisibility(View.VISIBLE);
//                } else {
//                    findViewById(R.id.rl_add_members).setVisibility(View.GONE);
//                }
                break;
            case R.id.cb_is_registration_required:
                if (isChecked) {
                    findViewById(R.id.tly_registration_start_date).setVisibility(View.VISIBLE);
                    findViewById(R.id.tly_registration_end_date).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.tly_registration_start_date).setVisibility(View.GONE);
                    findViewById(R.id.tly_registration_end_date).setVisibility(View.GONE);
                }

                break;

        }
    }

    @Override
    public void onValuesSelected(boolean[] selected, String spinnerName) {
        switch (spinnerName) {
            case Constants.Planner.SPINNER_ADD_FORMS:
                selectedForms.clear();
                for (int i = 0; i < selected.length; i++) {
                    if (selected[i]) {
                        selectedForms.add(formsList.get(i).getId());
                    }
                }
                break;

        }
    }

    private void submitDetails() {
        if(isAllInputsValid()){
            Event event = new Event();
            event.setType("Event");
            event.setTitle(etTitle.getText().toString());
            event.setDescription(etDescription.getText().toString());
//            event.setStartdatetime(etStartDate.getText().toString() + " " + etStartTime.getText().toString());
            event.setStartdatetime(dateTimeToTimeStamp(etStartDate.getText().toString(), etStartTime.getText().toString()).toString());
//            event.setEnddatetime(etEndDate.getText().toString() + " " + etEndTime.getText().toString());
            event.setEnddatetime(dateTimeToTimeStamp(etEndDate.getText().toString(), etEndTime.getText().toString()).toString());
            event.setAddress(etAddress.getText().toString());
//          event.setOrganizer(Util.getUserObjectFromPref().getId());
            event.setRegistrationRequired(cbIsRegistrationRequired.isChecked());
            event.setMarkAttendanceRequired(cbIsAttendanceRequired.isChecked());

            if(cbIsRegistrationRequired.isChecked()){
                RegistrationSchedule obj = new RegistrationSchedule();
//                obj.setStartdatetime(etRegistrationStartDate.getText().toString());
//                obj.setEnddatetime(etRegistrationEndDate.getText().toString());
                obj.setStartdatetime(dateToTimeStamp(etRegistrationStartDate.getText().toString()).toString());
                obj.setEnddatetime(dateToTimeStamp(etRegistrationEndDate.getText().toString()).toString());
                event.setRegistrationSchedule(obj);
            }

            event.setSelectedForms(selectedForms);
            event.setParticipants(membersList);
            if (mImageUploaded && !TextUtils.isEmpty(mUploadedImageUrl)) {
                event.setThumbnailImage(mUploadedImageUrl);
            } else {
                // Set old profile url if profile unchanged
                if (!TextUtils.isEmpty(event.getThumbnailImage())) {
                    event.setThumbnailImage(event.getThumbnailImage());
                }
            }

            //put in response of above api
            createEventPresenter.submitEvent(event);
        }
    }

    private boolean isAllInputsValid() {
        String msg = "";

        if (etTitle.getText().toString().trim().length() == 0) {
            msg = getResources().getString(R.string.msg_enter_title);
        } else if (etStartDate.getText().toString().trim().length() == 0) {
            msg = getResources().getString(R.string.msg_enter_start_date);
        } else if (etStartTime.getText().toString().trim().length() == 0) {
            msg = getResources().getString(R.string.msg_enter_start_time);
        } else if (etEndDate.getText().toString().trim().length() == 0) {
            msg = getResources().getString(R.string.msg_enter_end_date);
        } else if (etEndTime.getText().toString().trim().length() == 0) {
            msg = getResources().getString(R.string.msg_enter_ned_date);
        } else if (etAddress.getText().toString().trim().length() == 0) {
            msg = getResources().getString(R.string.msg_enter_address);
        } else if (cbIsRegistrationRequired.isChecked()) {
            if (etRegistrationStartDate.getText().toString().trim().length() == 0) {
                msg = getResources().getString(R.string.msg_enter_registration_start_date);
            } else if (etRegistrationEndDate.getText().toString().trim().length() == 0) {
                msg = getResources().getString(R.string.msg_enter_registration_end_date);
            }
        }

        if (TextUtils.isEmpty(msg)) {
            return true;
        }

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        return false;
    }

    private Long dateTimeToTimeStamp(String strDate, String strTime) {
        Date date;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try {
            date = formatter.parse(strDate + " " + strTime);
            return date.getTime()/1000;
        } catch (ParseException e) {
            Log.e("TAG", e.getMessage());
        }

        return 0L;
    }
    private Long dateToTimeStamp(String myDate) {
        Date date;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            date = formatter.parse(myDate);
            return date.getTime()/1000;
        } catch (ParseException e) {
            Log.e("TAG", e.getMessage());
        }

        return 0L;
    }

    private void onAddImageClick() {
        if (Permissions.isCameraPermissionGranted(this, this)) {
            showPictureDialog();
        }
    }

    private void showPictureDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.title_choose_picture));
        String[] items = {getString(R.string.label_gallery), getString(R.string.label_camera)};

        dialog.setItems(items, (dialog1, which) -> {
            switch (which) {
                case 0:
                    choosePhotoFromGallery();
                    break;

                case 1:
                    takePhotoFromCamera();
                    break;
            }
        });

        dialog.show();
    }

    private void choosePhotoFromGallery() {
        try {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, Constants.CHOOSE_IMAGE_FROM_GALLERY);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, getResources().getString(R.string.msg_error_in_photo_gallery),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void takePhotoFromCamera() {
        try {
            //use standard intent to capture an image
            String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/MV/Image/picture.jpg";

            File imageFile = new File(imageFilePath);
            outputUri = FileProvider.getUriForFile(this, getPackageName()
                    + ".file_provider", imageFile);

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(takePictureIntent, Constants.CHOOSE_IMAGE_FROM_CAMERA);
        } catch (ActivityNotFoundException e) {
            //display an error message
            Toast.makeText(this, getResources().getString(R.string.msg_image_capture_not_support),
                    Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(this, getResources().getString(R.string.msg_take_photo_error),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private String getImageName() {
        long time = new Date().getTime();
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + Constants.Image.IMAGE_STORAGE_DIRECTORY);
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                Log.e(TAG, "Failed to create directory!");
                return null;
            }
        }
        return Constants.Image.IMAGE_STORAGE_DIRECTORY + Constants.Image.FILE_SEP
                + Constants.Image.IMAGE_PREFIX + time + Constants.Image.IMAGE_SUFFIX;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.Planner.MEMBER_LIST && data != null) {
            membersList = (ArrayList<Participant>) data.getSerializableExtra(Constants.Planner.MEMBER_LIST_DATA);
            RecyclerView rvAttendeesList = findViewById(R.id.rv_attendees_list);
            addMembersListAdapter = new AddMembersListAdapter(CreateEventActivity.this, membersList, false, false);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            rvAttendeesList.setLayoutManager(mLayoutManager);
            rvAttendeesList.setAdapter(addMembersListAdapter);
        } else if (requestCode == Constants.CHOOSE_IMAGE_FROM_CAMERA && resultCode == Activity.RESULT_OK) {
            try {
                String imageFilePath = getImageName();
                if (imageFilePath == null) return;

                finalUri = Util.getUri(imageFilePath);
                Crop.of(outputUri, finalUri).start(this);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else if (requestCode == Constants.CHOOSE_IMAGE_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {
                    String imageFilePath = getImageName();
                    if (imageFilePath == null) return;

                    outputUri = data.getData();
                    finalUri = Util.getUri(imageFilePath);
                    Crop.of(outputUri, finalUri).start(this);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            try {
                eventPic.setImageURI(finalUri);
                final File imageFile = new File(Objects.requireNonNull(finalUri.getPath()));

                if (Util.isConnected(this)) {
                    createEventPresenter.uploadProfileImage(imageFile, Constants.Image.IMAGE_TYPE_EVENT);
                } else {
                    Util.showToast(getResources().getString(R.string.msg_no_network), this);
                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }



    @Override
    public void showProgressBar() {
        runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public <T> void showNextScreen(T data) {
        finish();
    }

    @Override
    public void showErrorMessage(String result) {
        runOnUiThread(() -> Util.showToast(result, this));
    }

    public void onFormsListFatched(ArrayList<AddForm> formslist) {
        formsList.addAll(formslist);
        ArrayList<String> displayFormList = new ArrayList<>();
        String CurrentLang = Locale.getDefault().getLanguage();
        for (AddForm obj : formsList) {
            if (CurrentLang.equalsIgnoreCase("mr"))
                displayFormList.add(obj.getName().getMr());
            else if (CurrentLang.equalsIgnoreCase("hi"))
                displayFormList.add(obj.getName().getHi());
            else
                displayFormList.add(obj.getName().getDefault());
        }
        spAddForms.setItems(displayFormList, getString(R.string.select_forms), this);
    }

    public void onImageUploaded(String uploadedImageUrl) {
        mImageUploaded = true;
        mUploadedImageUrl = uploadedImageUrl;
    }

}
