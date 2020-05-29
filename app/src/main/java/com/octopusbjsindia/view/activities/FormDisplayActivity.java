package com.octopusbjsindia.view.activities;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.LocaleData;
import com.octopusbjsindia.models.appoval_forms_detail.FeedbackFormHistoryData;
import com.octopusbjsindia.models.forms.Elements;
import com.octopusbjsindia.models.forms.FormData;
import com.octopusbjsindia.models.forms.FormResult;
import com.octopusbjsindia.models.forms.VisibleIf;
import com.octopusbjsindia.models.forms.VisibleIfListObject;
import com.octopusbjsindia.presenter.FormDisplayActivityPresenter;
import com.octopusbjsindia.syncAdapter.SyncAdapterUtils;
import com.octopusbjsindia.utility.AppEvents;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.GPSTracker;
import com.octopusbjsindia.utility.PlatformGson;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.ViewPagerAdapter;
import com.octopusbjsindia.view.fragments.formComponents.CheckboxFragment;
import com.octopusbjsindia.view.fragments.formComponents.FileQuestionFragment;
import com.octopusbjsindia.view.fragments.formComponents.LocationFragment;
import com.octopusbjsindia.view.fragments.formComponents.MatrixQuestionFragment;
import com.octopusbjsindia.view.fragments.formComponents.RadioButtonFragment;
import com.octopusbjsindia.view.fragments.formComponents.RatingQuestionFragment;
import com.octopusbjsindia.view.fragments.formComponents.TextFragment;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class FormDisplayActivity extends BaseActivity implements APIDataListener {

    private FormData formData;
    private ViewPager vpFormElements;
    private ViewPagerAdapter adapter;
    private List<Elements> formDataArrayList = new ArrayList<Elements>();
    private FormDisplayActivityPresenter presenter;
    public HashMap<String, String> formAnswersMap = new HashMap<>();
    private String processId;
    public List<Map<String, String>> mUploadedImageUrlList = new ArrayList<>();
    public HashMap<String, String> selectedImageUriList = new HashMap<>();
    private GPSTracker gpsTracker;
    private final String TAG = this.getClass().getSimpleName();
    private boolean mIsPartiallySaved;
    private RelativeLayout progressBarLayout;
    private ProgressBar progressBar;
    private ArrayList<String> jurisdictions = new ArrayList<>();
    private TextView tvTitle;
    private ImageView toolbar_back_action,toolbar_edit_action;
    private boolean isImageFileAvailable = false;
    public boolean isImageUploadPending = false;
    public boolean isFromApproval = false;
    private FormResult formResult;
    public boolean isEditable = true;
    private ImageView imgNoData;

    private String batchId = "";
    private String trainerId = "";
    private String beneficiaryId = "";
    private String formStatus = "";
    private String workshopId = "";
    public boolean isForSmartGirl = false;
    public boolean isForWorkshop = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_display);

        tvTitle = findViewById(R.id.toolbar_title);
        vpFormElements = findViewById(R.id.viewpager);
        imgNoData = findViewById(R.id.img_no_data);
        toolbar_edit_action = findViewById(R.id.toolbar_edit_action);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        vpFormElements.setAdapter(adapter);
        presenter = new FormDisplayActivityPresenter(this);
        if (getIntent().getExtras() != null) {
            processId = getIntent().getExtras().getString(Constants.PM.PROCESS_ID);

            String formId = getIntent().getExtras().getString(Constants.PM.FORM_ID);

            String formResultString = getIntent().getExtras().getString("formData");
            String fromHistory = getIntent().getExtras().getString("fromHistory");
            if (fromHistory != null && !TextUtils.isEmpty(fromHistory)) {
                isFromApproval = true;
                isEditable = false;
            }

            FormData formData = DatabaseManager.getDBInstance(this).getFormSchema(formId);

            //mIsPartiallySaved = getIntent().getExtras().getBoolean(Constants.PM.PARTIAL_FORM);
            trainerId = getIntent().getExtras().getString(Constants.SmartGirlModule.TRAINER_ID);
            formStatus = getIntent().getExtras().getString(Constants.SmartGirlModule.FORM_STATUS);
            batchId = getIntent().getExtras().getString(Constants.SmartGirlModule.BATCH_ID);
            if (batchId!=null && !TextUtils.isEmpty(batchId)) {
                isForSmartGirl = true;
                toolbar_edit_action.setVisibility(View.GONE);
            }
            workshopId = getIntent().getExtras().getString(Constants.SmartGirlModule.WORKSHOP_ID);
            if (workshopId!=null && !TextUtils.isEmpty(workshopId)) {
                isForWorkshop = true;
            }


            mIsPartiallySaved = getIntent().getExtras().getBoolean(Constants.PM.PARTIAL_FORM);
            //boolean readOnly = getIntent().getExtras().getBoolean(Constants.PM.EDIT_MODE);

//            if (mIsPartiallySaved) {
//                formData = DatabaseManager.getDBInstance(this).getFormSchema(formId);
//            }
            if (isFromApproval){
                try {
                    FeedbackFormHistoryData feedbackFormHistoryData = new Gson().fromJson(formResultString, FeedbackFormHistoryData.class);
                    FormResult formResult =  new Gson().fromJson(new Gson().toJson(feedbackFormHistoryData.getHistoryData().getValues().get(0)), FormResult.class);
                    //jsonToMap(formResultString);
                    jsonToMap(new Gson().toJson(formResult));
                    //presenter.getFormSchema(formId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if (processId != null && processId != "") {
                formResult = DatabaseManager.getDBInstance(this).getFormResult(processId);
                if (formResult != null) {
                    try {
                        jsonToMap(new Gson().toJson(formResult));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (formData == null) {
                if (Util.isConnected(this)) {
                    presenter.getFormSchema(formId);
                } else {
                    tvTitle.setText("Forms");
                    imgNoData.setVisibility(View.VISIBLE);
                    Util.showToast(getString(R.string.offline_no_form_schema_msg), this);
                }
            } else {
                this.formData = formData;
                parseFormSchema(formData);
            }
        }
        initView();
    }

    public void jsonToMap(String str) throws JSONException {

        JsonObject jObject = PlatformGson.getPlatformGsonInstance().fromJson(str, JsonObject.class);
        Iterator iterator = jObject.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, JsonElement> entry = (Map.Entry<String, JsonElement>) iterator.next();
            if (entry.getKey().equalsIgnoreCase("result")) {
                Gson gson = new Gson();
                formAnswersMap.clear();
                formAnswersMap.putAll(gson.fromJson(entry.getValue().getAsString(),
                        new TypeToken<HashMap<String, String>>() {
                        }.getType()));

            }
        }
    }

    private void initView() {
        progressBarLayout = findViewById(R.id.gen_frag_progress_bar);
        progressBar = findViewById(R.id.pb_gen_form_fragment);
        vpFormElements.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        gpsTracker = new GPSTracker(this);
        if (gpsTracker.isGPSEnabled(this, this)) {
            if (!gpsTracker.canGetLocation()) {
                gpsTracker.showSettingsAlert();
            }
        }

        findViewById(R.id.toolbar_back_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

//        toolbar_edit_action.setVisibility(View.VISIBLE);
//        toolbar_edit_action.setImageResource(R.drawable.ic_saved_icon_db);
//        toolbar_edit_action.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setupFormElements(final List<Elements> formDataArrayList) {
        for (Elements element : formDataArrayList) {
            boolean isFirstpage = false;
            if (element != null && !element.getType().equals("")) {
                if (formDataArrayList.indexOf(element) == 0) {
                    isFirstpage = true;
                }
                String formDataType = element.getType();
                Bundle bundle = new Bundle();
                switch (formDataType) {
                    case Constants.FormsFactory.LOCATION_TEMPLATE:
                        Fragment locationFragment = new LocationFragment();
                        bundle.putSerializable("Element", element);
                        bundle.putSerializable("jurisdictions", jurisdictions);
                        bundle.putBoolean("isFirstpage", isFirstpage);
                        locationFragment.setArguments(bundle);
                        adapter.addFragment(locationFragment, "");
                        break;

                    case Constants.FormsFactory.RATING_TEMPLATE:
                        Fragment ratingQuestionFragment = new RatingQuestionFragment();
                        bundle.putSerializable("Element", element);
                        bundle.putBoolean("isFirstpage", isFirstpage);
                        ratingQuestionFragment.setArguments(bundle);
                        adapter.addFragment(ratingQuestionFragment, "Question Rating");
                        break;

                    case Constants.FormsFactory.TEXT_TEMPLATE:
                        Fragment textFragment = new TextFragment();
                        bundle.putSerializable("Element", element);
                        bundle.putBoolean("isFirstpage", isFirstpage);
                        textFragment.setArguments(bundle);
                        adapter.addFragment(textFragment, "Question 1");
                        break;

                    case Constants.FormsFactory.RADIO_GROUP_TEMPLATE:
                        Fragment radioButtonFragment = new RadioButtonFragment();
                        bundle.putSerializable("Element", element);
                        bundle.putBoolean("isFirstpage", isFirstpage);
                        radioButtonFragment.setArguments(bundle);
                        adapter.addFragment(radioButtonFragment, "Question 1");
                        break;

                    case Constants.FormsFactory.CHECKBOX_TEMPLATE:
                        Fragment checkboxFragment = new CheckboxFragment();
                        bundle.putSerializable("Element", element);
                        bundle.putBoolean("isFirstpage", isFirstpage);
                        checkboxFragment.setArguments(bundle);
                        adapter.addFragment(checkboxFragment, "Question 2");
                        break;

                    case Constants.FormsFactory.MATRIX_DROPDOWN:
                        Fragment matrixQuestionFragment = new MatrixQuestionFragment();
                        bundle.putSerializable("Element", element);
                        bundle.putBoolean("isFirstpage", isFirstpage);
                        matrixQuestionFragment.setArguments(bundle);
                        adapter.addFragment(matrixQuestionFragment, "Question Title");
                        break;

                    case Constants.FormsFactory.FILE_TEMPLATE:
                        isImageFileAvailable = true;
                        Fragment fileFragment = new FileQuestionFragment();
                        bundle.putSerializable("Element", element);
                        bundle.putBoolean("isFirstpage", isFirstpage);
                        fileFragment.setArguments(bundle);
                        adapter.addFragment(fileFragment, "Question Title");
                        break;

                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void parseFormSchema(FormData formData) {
        if (formData == null) {
            imgNoData.setVisibility(View.VISIBLE);
            return;
        }
        this.formData = new FormData();
        this.formData = formData;

        if (formResult != null) {
            if (formResult.getFormStatus() == SyncAdapterUtils.FormStatus.SYNCED) {
                if (formResult.getFormApprovalStatus().equalsIgnoreCase(Constants.PM.REJECTED_STATUS)) {
                    if (formData.getEditable().equalsIgnoreCase("false")) {
                        isEditable = false;
                    }
                } else {
                    isEditable = false;
                }
            } else if (formResult.getFormStatus() == SyncAdapterUtils.FormStatus.UN_SYNCED) {
                isEditable = false;
            }
        }

//        if (formResult != null && (formResult.getFormStatus() == SyncAdapterUtils.FormStatus.SYNCED
//                || formResult.getFormStatus() == SyncAdapterUtils.FormStatus.UN_SYNCED)) {
//            if (formData.getEditable().equalsIgnoreCase("false")) {
//                isEditable = false;
//            }
//        }

        if (isEditable) {
            toolbar_edit_action = findViewById(R.id.toolbar_edit_action);
            toolbar_edit_action.setVisibility(View.VISIBLE);
            toolbar_edit_action.setImageResource(R.drawable.ic_saved_icon_db);
            toolbar_edit_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }

        TextView tvFormTitle = findViewById(R.id.tv_form_title);
        tvFormTitle.setText(formData.getName().getLocaleValue());
        if (formData.getLocationRequired()) {
            Elements locationElement = new Elements();
            locationElement.setType("location");
            locationElement.setName(formData.getLocationRequiredLevel());
            locationElement.setLocationRequiredLevel(formData.getLocationRequiredLevel());

            Gson gson = new Gson();
            ArrayList<String> listFromGson = gson.fromJson(formData.getJurisdictions_(),
                    new TypeToken<ArrayList<String>>() {
                    }.getType());

            jurisdictions.clear();
            jurisdictions.addAll(listFromGson);

            formDataArrayList.add(locationElement);
        }
        formDataArrayList.addAll(formData.getComponents().getPages().get(0).getElements());
        if (formDataArrayList != null) {
            tvTitle.setText(1 + "/" + formDataArrayList.size());
            setupFormElements(formDataArrayList);
        }
    }

    public void goNext(HashMap<String, String> hashMap) {
        Util.hideKeyboard(tvTitle);
        formAnswersMap.putAll(hashMap);
        if (formDataArrayList.size() == vpFormElements.getCurrentItem() + 1) {
            formAnswersMap.put("Lang", Util.getLocaleLanguageCode());
            if (isEditable) {
                showDialog(this, "Alert", "Do you want to submit?",
                        "Save", "Submit", false);
            } else {
                showDialog(this, "Alert", "Do you want to close the form?",
                        "OK", "Cancel");
            }
        } else {
            if (formDataArrayList.get((vpFormElements.getCurrentItem() + 1)).getVisibleIf() == null) {
                tvTitle.setText((vpFormElements.getCurrentItem() + 2) + "/" + formDataArrayList.size());
                vpFormElements.setCurrentItem((vpFormElements.getCurrentItem() + 1));
            } else {
                isQuestionVisible(1);
            }
        }
    }

    public void goPrevious() {
        if (formDataArrayList.get((vpFormElements.getCurrentItem() - 1)).getVisibleIf() == null) {
            tvTitle.setText((vpFormElements.getCurrentItem()) + "/" + formDataArrayList.size());
            vpFormElements.setCurrentItem((vpFormElements.getCurrentItem() - 1));
        } else {
            isPreviousQuestionVisible(1);
        }
    }

    private void isQuestionVisible(int i) {
        String elementKey = formDataArrayList.get((vpFormElements.getCurrentItem() + i)).getName();
        VisibleIf visibleIf = formDataArrayList.get((vpFormElements.getCurrentItem() + i)).getVisibleIf();
        String conditionType = visibleIf.getConditionType();
        List<VisibleIfListObject> conditionsList = visibleIf.getConditionsList();

        if (conditionType.equalsIgnoreCase(Constants.PM.VISIBLE_IF_NO_CONDITION)) {
            for (VisibleIfListObject object : conditionsList) {
                String question = object.getQuestionKey();
                String answer = object.getAnswer();
                if (formAnswersMap.containsKey(question) && formAnswersMap.get(question).equalsIgnoreCase(answer)) {
                    tvTitle.setText((vpFormElements.getCurrentItem() + (i + 1)) + "/" + formDataArrayList.size());
                    vpFormElements.setCurrentItem((vpFormElements.getCurrentItem() + i));
                } else {
                    formAnswersMap.remove(elementKey);
                    if (formDataArrayList.size() == vpFormElements.getCurrentItem() + (i + 1)) {
                        formAnswersMap.put("Lang", Util.getLocaleLanguageCode());
                        if (isEditable) {
                            showDialog(this, "Alert", "Do you want to submit?",
                                    "Save", "Submit", false);
                        } else {
                            showDialog(this, "Alert", "Do you want to close the form?",
                                    "OK", "Cancel");
                        }
                    } else {
                        if (formDataArrayList.get((vpFormElements.getCurrentItem() + (i + 1))).getVisibleIf() == null) {
                            tvTitle.setText((vpFormElements.getCurrentItem() + (i + 2)) + "/" + formDataArrayList.size());
                            vpFormElements.setCurrentItem((vpFormElements.getCurrentItem() + (i + 1)));
                        } else {
                            isQuestionVisible(i + 1);
                        }
                    }
                }
            }
        } else if (conditionType.equalsIgnoreCase(Constants.PM.VISIBLE_IF_OR_CONDITION)) {
            boolean isConditionMatched = false;
            for (VisibleIfListObject object : conditionsList) {
                String question = object.getQuestionKey();
                String answer = object.getAnswer();
                if (formAnswersMap.containsKey(question) && formAnswersMap.get(question).equalsIgnoreCase(answer)) {
                    isConditionMatched = true;
                    break;
                }
            }
            if (isConditionMatched) {
                tvTitle.setText((vpFormElements.getCurrentItem() + (i + 1)) + "/" + formDataArrayList.size());
                vpFormElements.setCurrentItem((vpFormElements.getCurrentItem() + i));
            } else {
                formAnswersMap.remove(elementKey);
                if (formDataArrayList.size() == vpFormElements.getCurrentItem() + (i + 1)) {
                    formAnswersMap.put("Lang", Util.getLocaleLanguageCode());
                    if (isEditable) {
                        showDialog(this, "Alert", "Do you want to submit?",
                                "Save", "Submit", false);
                    } else {
                        showDialog(this, "Alert", "Do you want to close the form?",
                                "OK", "Cancel");
                    }
                } else {
                    if (formDataArrayList.get((vpFormElements.getCurrentItem() + (i + 1))).getVisibleIf() == null) {
                        tvTitle.setText((vpFormElements.getCurrentItem() + (i + 2)) + "/" + formDataArrayList.size());
                        vpFormElements.setCurrentItem((vpFormElements.getCurrentItem() + (i + 1)));
                    } else {
                        isQuestionVisible(i + 1);
                    }
                }
            }
        } else if (conditionType.equalsIgnoreCase(Constants.PM.VISIBLE_IF_AND_CONDITION)) {
            boolean isConditionMatched = false;
            for (VisibleIfListObject object : conditionsList) {
                String question = object.getQuestionKey();
                String answer = object.getAnswer();
                if (formAnswersMap.containsKey(question) && formAnswersMap.get(question).equalsIgnoreCase(answer)) {
                    isConditionMatched = true;
                } else {
                    isConditionMatched = false;
                    break;
                }
            }
            if (isConditionMatched) {
                tvTitle.setText((vpFormElements.getCurrentItem() + (i + 1)) + "/" + formDataArrayList.size());
                vpFormElements.setCurrentItem((vpFormElements.getCurrentItem() + i));
            } else {
                formAnswersMap.remove(elementKey);
                if (formDataArrayList.size() == vpFormElements.getCurrentItem() + (i + 1)) {
                    formAnswersMap.put("Lang", Util.getLocaleLanguageCode());
                    if (isEditable) {
                        showDialog(this, "Alert", "Do you want to submit?",
                                "Save", "Submit", false);
                    } else {
                        showDialog(this, "Alert", "Do you want to close the form?",
                                "OK", "Cancel");
                    }
                } else {
                    if (formDataArrayList.get((vpFormElements.getCurrentItem() + (i + 1))).getVisibleIf() == null) {
                        tvTitle.setText((vpFormElements.getCurrentItem() + (i + 2)) + "/" + formDataArrayList.size());
                        vpFormElements.setCurrentItem((vpFormElements.getCurrentItem() + (i + 1)));
                    } else {
                        isQuestionVisible(i + 1);
                    }
                }
            }
        }
    }

    private void isPreviousQuestionVisible(int i) {
        VisibleIf visibleIf = formDataArrayList.get((vpFormElements.getCurrentItem() - i)).getVisibleIf();
        String conditionType = visibleIf.getConditionType();
        List<VisibleIfListObject> conditionsList = visibleIf.getConditionsList();

        if (conditionType.equalsIgnoreCase(Constants.PM.VISIBLE_IF_NO_CONDITION)) {
            for (VisibleIfListObject object : conditionsList) {
                String question = object.getQuestionKey();
                String answer = object.getAnswer();
                if (formAnswersMap.containsKey(question) && formAnswersMap.get(question).equalsIgnoreCase(answer)) {
                    tvTitle.setText((vpFormElements.getCurrentItem() - (i + 1)) + "/" + formDataArrayList.size());
                    vpFormElements.setCurrentItem((vpFormElements.getCurrentItem() - i));
                } else {
                    if (formDataArrayList.get((vpFormElements.getCurrentItem() - (i + 1))).getVisibleIf() == null) {
                        tvTitle.setText((vpFormElements.getCurrentItem() - i) + "/" + formDataArrayList.size());
                        vpFormElements.setCurrentItem((vpFormElements.getCurrentItem() - (i + 1)));
                    } else {
                        isPreviousQuestionVisible(i + 1);
                    }
                }
            }
        } else if (conditionType.equalsIgnoreCase(Constants.PM.VISIBLE_IF_OR_CONDITION)) {
            boolean isConditionMatched = false;
            for (VisibleIfListObject object : conditionsList) {
                String question = object.getQuestionKey();
                String answer = object.getAnswer();
                if (formAnswersMap.containsKey(question) && formAnswersMap.get(question).equalsIgnoreCase(answer)) {
                    isConditionMatched = true;
                    break;
                }
            }
            if (isConditionMatched) {
                tvTitle.setText((vpFormElements.getCurrentItem() - (i - 1)) + "/" + formDataArrayList.size());
                vpFormElements.setCurrentItem((vpFormElements.getCurrentItem() - i));
            } else {
                if (formDataArrayList.get((vpFormElements.getCurrentItem() - (i + 1))).getVisibleIf() == null) {
                    tvTitle.setText((vpFormElements.getCurrentItem() - i) + "/" + formDataArrayList.size());
                    vpFormElements.setCurrentItem((vpFormElements.getCurrentItem() - (i + 1)));
                } else {
                    isPreviousQuestionVisible(i + 1);
                }
            }
        } else if (conditionType.equalsIgnoreCase(Constants.PM.VISIBLE_IF_AND_CONDITION)) {
            boolean isConditionMatched = false;
            for (VisibleIfListObject object : conditionsList) {
                String question = object.getQuestionKey();
                String answer = object.getAnswer();
                if (formAnswersMap.containsKey(question) && formAnswersMap.get(question).equalsIgnoreCase(answer)) {
                    isConditionMatched = true;
                } else {
                    isConditionMatched = false;
                    break;
                }
            }
            if (isConditionMatched) {
                tvTitle.setText((vpFormElements.getCurrentItem() - (i - 1)) + "/" + formDataArrayList.size());
                vpFormElements.setCurrentItem((vpFormElements.getCurrentItem() - i));
            } else {
                if (formDataArrayList.get((vpFormElements.getCurrentItem() - (i + 1))).getVisibleIf() == null) {
                    tvTitle.setText((vpFormElements.getCurrentItem() - i) + "/" + formDataArrayList.size());
                    vpFormElements.setCurrentItem((vpFormElements.getCurrentItem() - (i + 1)));
                } else {
                    isPreviousQuestionVisible(i + 1);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (formData == null) {
            finish();
            return;
        }

        if (isForSmartGirl){
            showDialog(this, "Alert", "Do you want to discard the form?", "Save", "Discard", true);
        }else if (isEditable) {
            showDialog(this, "Alert", "Do you want to save the form?",
                    "Save", "Discard", true);
        } else {
            showDialog(this, "Alert", "Do you want to close the form?",
                    "OK", "Cancel");
        }
    }

    public void submitForm() {
        if (formResult != null && formResult.getOid() != null) {
            formAnswersMap.put(Constants.PM.PROCESS_ID, formResult.getOid());
        }
        Location location = gpsTracker.getLocation();
        String strLat, strLong;
        if (location != null) {
            strLat = String.valueOf(location.getLatitude());
            strLong = String.valueOf(location.getLongitude());
        } else {
            strLat = gpsTracker.getLatitude();
            strLong = gpsTracker.getLongitude();
        }
        //enableSubmitButton(false);
        formAnswersMap.put(Constants.Location.LATITUDE, strLat);
        formAnswersMap.put(Constants.Location.LONGITUDE, strLong);
        if (isForSmartGirl) {
            if (isForWorkshop){
                formAnswersMap.put(Constants.SmartGirlModule.WORKSHOP_ID, batchId);
                formAnswersMap.put(Constants.SmartGirlModule.BENEFICIARY_ID, null);
            }else {
                formAnswersMap.put(Constants.SmartGirlModule.BATCH_ID, batchId);
                formAnswersMap.put(Constants.SmartGirlModule.TRAINER_ID, trainerId);

            }

            formAnswersMap.put("role_code", String.valueOf(Util.getUserObjectFromPref().getRoleCode()));
            formAnswersMap.put(Constants.SmartGirlModule.FORM_STATUS, formStatus);

        }


        if (Util.isConnected(this)) {
            presenter.setRequestedObject(formAnswersMap);
            String url = null;
            //if (formModel.getData() != null && formModel.getData().getApi_url() != null) {
//                url = formModel.getData().getApi_url() + "/" + formModel.getData().getId();
            //url = BuildConfig.BASE_URL + Urls.PM.SET_PROCESS_RESULT + "/" + formModel.getData().getId();
            //}
            url = BuildConfig.BASE_URL + String.format(Urls.PM.SET_PROCESS_RESULT, formData.getId());

            presenter.onSubmitClick(Constants.ONLINE_SUBMIT_FORM_TYPE, url,
                    formData.getId(), processId, mUploadedImageUrlList);
        } else {
            if (formData != null) {
                saveOfflineFormToLocalDb();

                Intent intent = new Intent(SyncAdapterUtils.PARTIAL_FORM_ADDED);
                LocalBroadcastManager.getInstance(getApplicationContext())
                        .sendBroadcast(intent);

                AppEvents.trackAppEvent(getString(R.string.event_form_saved_offline,
                        formData.getName()));

                Util.showToast(getResources().getString(R.string.form_saved_offline), this);
                Log.d(TAG, "Form saved " + formData.getId());
                //Objects.requireNonNull(this).onBackPressed();
            }
        }
    }

    private void saveOfflineFormToLocalDb() {
        //FormData formData = this.formData;
        FormResult result;
        //if (mIsPartiallySaved) {
            result = DatabaseManager.getDBInstance(this).getFormResult(processId);
        //} else {
        if (result == null) {
            result = new FormResult();
            result.setFormId(formData.getId());
            String locallySavedFormID = UUID.randomUUID().toString();
            result.set_id(locallySavedFormID);
        }
        //}
        result.setCreatedAt(Util.getCurrentTimeStamp());
        result.setFormNameLocale(formData.getName());
        result.setFormStatus(SyncAdapterUtils.FormStatus.UN_SYNCED);

        if (formData.getCategory() != null) {
            LocaleData category = formData.getCategory().getName();
            if (category != null) {
                result.setFormCategoryLocale(category);
            }
        }

        if (formAnswersMap != null) {
            String json = PlatformGson.getPlatformGsonInstance().toJson(formAnswersMap);
            JsonObject obj = PlatformGson.getPlatformGsonInstance().fromJson(json, JsonObject.class);

            if (mUploadedImageUrlList != null && !mUploadedImageUrlList.isEmpty()) {
                for (final Map<String, String> map : mUploadedImageUrlList) {
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        obj.addProperty(entry.getKey(), entry.getValue());
                    }
                }
            }
            //result.setRequestObject(json);
            if (obj != null) {
                result.setResult(obj.toString());
                //presenter.setSavedForm(result);
//                if (mIsPartiallySaved) {
//                    DatabaseManager.getDBInstance(this).updateFormResult(result);
//                } else {
                    DatabaseManager.getDBInstance(this).insertFormResult(result);
                //}
            }
        }
    }

    public void savePartialForm() {
        //FormData formData = formModel.getData();
        FormResult result;//= new FormResult();
        result = DatabaseManager.getDBInstance(this).getFormResult(processId);
        if (result == null) {
            result = new FormResult();
            result.set_id(UUID.randomUUID().toString());
            result.setFormId(formData.getId());
        }
        result.setCreatedAt(Util.getCurrentTimeStamp());
        result.setFormNameLocale(formData.getName());
        result.setFormStatus(SyncAdapterUtils.FormStatus.PARTIAL);

        if (formData.getCategory() != null) {
            LocaleData category = formData.getCategory().getName();
            if (category != null) {
                result.setFormCategoryLocale(category);
            }
        }
        //Save normal values to JsonObject
        if (formAnswersMap != null) {
            String json = PlatformGson.getPlatformGsonInstance().toJson(formAnswersMap);
            JsonObject obj = PlatformGson.getPlatformGsonInstance().fromJson(json, JsonObject.class);
            if (obj != null) {
                if (mUploadedImageUrlList != null && !mUploadedImageUrlList.isEmpty()) {
                    for (final Map<String, String> map : mUploadedImageUrlList) {
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            obj.addProperty(entry.getKey(), entry.getValue());
                        }
                    }
                }
                result.setResult(obj.toString());
            }
        }
        //if (mIsPartiallySaved) {
            //String processId = getArguments().getString(Constants.PM.PROCESS_ID);
        //FormResult form = DatabaseManager.getDBInstance(this).getPartiallySavedForm(processId);

//        FormResult form = DatabaseManager.getDBInstance(this).getFormResult(processId);
//            if (form != null) {
//                result.set_id(form.get_id());
//            }

        //DatabaseManager.getDBInstance(this).updateFormResult(result);
        //} else {
            DatabaseManager.getDBInstance(this).insertFormResult(result);
        //}

        Intent intent = new Intent(SyncAdapterUtils.PARTIAL_FORM_ADDED);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        this.finish();
    }

    public void enableSubmitButton(boolean isEnable) {
        findViewById(R.id.btn_submit).setEnabled(isEnable);
    }

    @Override
    public void onFailureListener(String requestID, String message) {

    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {

    }

    @Override
    public void showProgressBar() {
        this.runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBarLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        if (this == null) return;

        this.runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null && progressBar.isShown()) {
                progressBarLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void closeCurrentActivity() {

    }

    public <T> void showNextScreen(T data) {
    }

    public void uploadImage(File file, String type, final String formName) {
        presenter.uploadImage(file,
                Constants.Image.IMAGE_TYPE_FILE, formName);
    }

    public void onImageUploaded(final Map<String, String> uploadedImageUrlList) {
        mUploadedImageUrlList.add(uploadedImageUrlList);
    }

    public List<Map<String, String>> getUploadedImages() {
        return mUploadedImageUrlList;
    }

    public void showDialog(Context context, String dialogTitle, String message, String btn1String, String
            btn2String, boolean discardFlag) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(context));
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
            if (isForSmartGirl) {
                button.setText(R.string.cancel);
            }else {
                button.setText(btn1String);
            }
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(v -> {
                // Close dialog
                if (isForSmartGirl) {
                    dialog.dismiss();
                }else {
                    savePartialForm();
                    dialog.dismiss();
                }
            });
        }

        if (!TextUtils.isEmpty(btn2String)) {
            Button button1 = dialog.findViewById(R.id.btn_dialog_1);
            button1.setText(btn2String);
            button1.setVisibility(View.VISIBLE);
            button1.setOnClickListener(v -> {
                // Close dialog

                if (discardFlag) {
                    dialog.dismiss();
                    finish();
                } else {
                    if (isImageUploadPending) {
                        Util.showDialog(this, "Alert", "You can not submit this form as " +
                                "image selection is pending. Now, you can save this form and again complete " +
                                "image question while you are connected to network and then submit form.", "OK", "");
                        return;
                    }
                    submitForm();
                    dialog.dismiss();
                    finish();
                }
            });
        }

        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    public void showDialog(Context context, String dialogTitle, String message, String btn1String, String
            btn2String) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(context));
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
                finish();
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
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
}
