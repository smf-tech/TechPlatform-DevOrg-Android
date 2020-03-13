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
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.LocaleData;
import com.octopusbjsindia.models.forms.Components;
import com.octopusbjsindia.models.forms.Elements;
import com.octopusbjsindia.models.forms.Form;
import com.octopusbjsindia.models.forms.FormData;
import com.octopusbjsindia.models.forms.FormResult;
import com.octopusbjsindia.presenter.FormDisplayActivityPresenter;
import com.octopusbjsindia.syncAdapter.SyncAdapterUtils;
import com.octopusbjsindia.utility.AppEvents;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.GPSTracker;
import com.octopusbjsindia.utility.PlatformGson;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.ViewPagerAdapter;
import com.octopusbjsindia.view.fragments.formComponents.CheckboxFragment;
import com.octopusbjsindia.view.fragments.formComponents.ImagePickerQuestionFragment;
import com.octopusbjsindia.view.fragments.formComponents.LocationFragment;
import com.octopusbjsindia.view.fragments.formComponents.MatrixQuestionFragment;
import com.octopusbjsindia.view.fragments.formComponents.RadioButtonFragment;
import com.octopusbjsindia.view.fragments.formComponents.RatingQuestionFragment;
import com.octopusbjsindia.view.fragments.formComponents.TextFragment;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class FormDisplayActivity extends BaseActivity implements APIDataListener {

    private Form formModel;
    public ViewPager vpFormElements;
    private ViewPagerAdapter adapter;
    private List<Elements> formDataArrayList = new ArrayList<Elements>();
    private FormDisplayActivityPresenter presenter;
    public HashMap<String, String> formAnswersMap = new HashMap<>();
    private String processId;
    private List<Map<String, String>> mUploadedImageUrlList = new ArrayList<>();
    private GPSTracker gpsTracker;
    private final String TAG = this.getClass().getSimpleName();
    private boolean mIsPartiallySaved;
    private RelativeLayout progressBarLayout;
    private ProgressBar progressBar;

    ArrayList<String> jurisdictions = new ArrayList<>();

    TextView tvTitle;
    ImageView toolbar_back_action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_display);

        tvTitle = findViewById(R.id.toolbar_title);
        vpFormElements = findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        vpFormElements.setAdapter(adapter);

        //Bundle bundle = new Bundle();
        presenter = new FormDisplayActivityPresenter(this);
        if (getIntent().getExtras() != null) {
            processId = getIntent().getExtras().getString(Constants.PM.PROCESS_ID);

            String formId = getIntent().getExtras().getString(Constants.PM.FORM_ID);
            FormData formData = DatabaseManager.getDBInstance(this).getFormSchema(formId);

            mIsPartiallySaved = getIntent().getExtras().getBoolean(Constants.PM.PARTIAL_FORM);
            boolean readOnly = getIntent().getExtras().getBoolean(Constants.PM.EDIT_MODE);

            if (mIsPartiallySaved) {
                formData = DatabaseManager.getDBInstance(this).getFormSchema(formId);
            }

            if (formData == null) {
                if (Util.isConnected(this)) {
                    presenter.getFormSchema(formId);
                } //else {
//                    view.findViewById(R.id.no_offline_form).setVisibility(View.VISIBLE);
//                    setActionbar("");
//                }
            } else {
//                presenter.getFormSchema(formId);
                formModel = new Form();
                formModel.setData(formData);
                Components components = formModel.getData().getComponents();
                parseFormSchema(formData);

                FormResult formResult = DatabaseManager.getDBInstance(this).getFormResult(processId);
                if (formResult != null) {
                    Log.d("Result", new Gson().toJson(formResult));
                    try {
                        jsonToMap(new Gson().toJson(formResult));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        initView();
    }

    public void jsonToMap(String str) throws JSONException {

        HashMap<String, String> map = new HashMap<String, String>();
        Gson g = new Gson();

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

        //System.out.println("json : "+jObject);
        //System.out.println("map : "+map);
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
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setupFormElements(final List<Elements> formDataArrayList) {

        for (Elements element : formDataArrayList) {
            boolean isFirstpage =false;
            if (element != null && !element.getType().equals("")) {
                if (formDataArrayList.indexOf(element)==0){
                    isFirstpage =true;
                }
                String formDataType = element.getType();
                Bundle bundle = new Bundle();
                switch (formDataType) {
                    case Constants.FormsFactory.LOCATION_TEMPLATE:
                        //check if location data of user's lower level is available or not
                        Fragment locationFragment = new LocationFragment();
                        bundle.putSerializable("Element", element);
                        bundle.putSerializable("jurisdictions", jurisdictions);
                        bundle.putBoolean("isFirstpage",isFirstpage);
                        locationFragment.setArguments(bundle);
                        adapter.addFragment(locationFragment, "");
                        break;
                    case Constants.FormsFactory.RATING_TEMPLATE:
                        Fragment ratingQuestionFragment = new RatingQuestionFragment();
                        bundle.putSerializable("Element", element);
                        bundle.putBoolean("isFirstpage",isFirstpage);
                        ratingQuestionFragment.setArguments(bundle);
                        adapter.addFragment(ratingQuestionFragment, "Question Rating");
                        break;
                    case Constants.FormsFactory.TEXT_TEMPLATE:
                        Fragment textFragment = new TextFragment();
                        bundle.putSerializable("Element", element);
                        bundle.putBoolean("isFirstpage",isFirstpage);
                        textFragment.setArguments(bundle);
                        adapter.addFragment(textFragment, "Question 1");
                        break;
                    case Constants.FormsFactory.RADIO_GROUP_TEMPLATE:
                        Fragment radioButtonFragment = new RadioButtonFragment();
                        bundle.putSerializable("Element", element);
                        bundle.putBoolean("isFirstpage",isFirstpage);
                        radioButtonFragment.setArguments(bundle);
                        adapter.addFragment(radioButtonFragment, "Question 1");
                        break;
                    case Constants.FormsFactory.CHECKBOX_TEMPLATE:
                        Fragment checkboxFragment = new CheckboxFragment();
                        bundle.putSerializable("Element", element);
                        bundle.putBoolean("isFirstpage",isFirstpage);
                        checkboxFragment.setArguments(bundle);
                        adapter.addFragment(checkboxFragment, "Question 2");
                        break;
                    case Constants.FormsFactory.MATRIX_DROPDOWN:
                        Fragment matrixQuestionFragment = new MatrixQuestionFragment();
                        bundle.putSerializable("Element", element);
                        bundle.putBoolean("isFirstpage",isFirstpage);
                        matrixQuestionFragment.setArguments(bundle);
                        adapter.addFragment(matrixQuestionFragment, "Question Title");
                        break;

                    /*case Constants.FormsFactory.IMAGE_PICKER:
                        Fragment imagePickerQuestionFragment = new ImagePickerQuestionFragment();
                        bundle.putSerializable("Element", element);
                        bundle.putBoolean("isFirstpage",isFirstpage);
                        imagePickerQuestionFragment.setArguments(bundle);
                        adapter.addFragment(imagePickerQuestionFragment, "Question Title");
                        break;*/


                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void parseFormSchema(FormData formData) {
        if (formData == null) {
            return;
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
        formAnswersMap.putAll(hashMap);
        if (formDataArrayList.size() == vpFormElements.getCurrentItem() + 1) {
            showDialog(this, "Alert", "Do you want to submit?", "Save", "Submit", false);
        } else {
            if (TextUtils.isEmpty(formDataArrayList.get((vpFormElements.getCurrentItem() + 1)).getVisibleIf())) {
                tvTitle.setText((vpFormElements.getCurrentItem() + 2) + "/" + formDataArrayList.size());
                vpFormElements.setCurrentItem((vpFormElements.getCurrentItem() + 1));
            } else {
                String visible = formDataArrayList.get((vpFormElements.getCurrentItem() + 1)).getVisibleIf();
                String quetion = visible.substring(visible.indexOf('{') + 1, visible.indexOf('}'));
                String selection = visible.substring(visible.indexOf("=") + 3, visible.length() - 1);
                if (selection.equalsIgnoreCase(formAnswersMap.get(quetion))) {
                    tvTitle.setText((vpFormElements.getCurrentItem() + 2) + "/" + formDataArrayList.size());
                    vpFormElements.setCurrentItem((vpFormElements.getCurrentItem() + 1));
                } else {
                    if (formDataArrayList.size() == vpFormElements.getCurrentItem() + 2) {
                        showDialog(this, "Alert", "Do you want to submit?", "Save", "Submit", false);
                    } else {
                        tvTitle.setText((vpFormElements.getCurrentItem() + 3) + "/" + formDataArrayList.size());
                        vpFormElements.setCurrentItem((vpFormElements.getCurrentItem() + 2));
                    }
                }
            }
        }
    }

    public void goPrevious() {
        if (TextUtils.isEmpty(formDataArrayList.get((vpFormElements.getCurrentItem() + 1)).getVisibleIf())) {
            tvTitle.setText((vpFormElements.getCurrentItem()) + "/" + formDataArrayList.size());
            vpFormElements.setCurrentItem((vpFormElements.getCurrentItem() - 1));
        } else {
            String visible = formDataArrayList.get((vpFormElements.getCurrentItem() + 1)).getVisibleIf();
            String quetion = visible.substring(visible.indexOf('{') + 1, visible.indexOf('}'));
            String selection = visible.substring(visible.indexOf("=") + 3, visible.length() - 1);
            if (selection.equalsIgnoreCase(formAnswersMap.get(quetion))) {
                tvTitle.setText((vpFormElements.getCurrentItem()) + "/" + formDataArrayList.size());
                vpFormElements.setCurrentItem((vpFormElements.getCurrentItem() - 1));
            } else {
                tvTitle.setText((vpFormElements.getCurrentItem() - 2) + "/" + formDataArrayList.size());
                vpFormElements.setCurrentItem((vpFormElements.getCurrentItem() - 2));
            }
        }
    }


    @Override
    public void onBackPressed() {
        showDialog(this, "Alert", "Do you want to save?", "Save", "Discard", true);
    }

    public void submitForm() {
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

        if (Util.isConnected(this)) {
            presenter.setRequestedObject(formAnswersMap);
            String url = null;
            if (formModel.getData() != null && formModel.getData().getApi_url() != null) {
                url = formModel.getData().getApi_url() + "/" + formModel.getData().getId();
            }
            presenter.onSubmitClick(Constants.ONLINE_SUBMIT_FORM_TYPE, url,
                    formModel.getData().getId(), processId, mUploadedImageUrlList);
        } else {
            if (formModel.getData() != null) {
                saveFormToLocalDatabase();
//                presenter.onSubmitClick(Constants.OFFLINE_SUBMIT_FORM_TYPE,
//                            null, formModel.getData().getId(), null, null);

                Intent intent = new Intent(SyncAdapterUtils.PARTIAL_FORM_ADDED);
                LocalBroadcastManager.getInstance(getApplicationContext())
                        .sendBroadcast(intent);

                AppEvents.trackAppEvent(getString(R.string.event_form_saved_offline,
                        formModel.getData().getName()));

                Util.showToast(getResources().getString(R.string.form_saved_offline), this);
                Log.d(TAG, "Form saved " + formModel.getData().getId());
                Objects.requireNonNull(this).onBackPressed();
            }
        }
    }

    private void saveFormToLocalDatabase() {
        FormData formData = formModel.getData();
        FormResult result;
        if (mIsPartiallySaved) {
            result = DatabaseManager.getDBInstance(this).getFormResult(processId);
        } else {
            result = new FormResult();
            result.setFormId(formData.getId());
            result.setFormNameLocale(formData.getName());
            result.setCreatedAt(Util.getCurrentTimeStamp());
            String locallySavedFormID = UUID.randomUUID().toString();
            result.set_id(locallySavedFormID);
        }
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
            result.setRequestObject(json);
            if (obj != null) {
                result.setResult(obj.toString());
                //presenter.setSavedForm(result);
                if (mIsPartiallySaved) {
                    DatabaseManager.getDBInstance(this).updateFormResult(result);
                } else {
                    DatabaseManager.getDBInstance(this).insertFormResult(result);
                }
            }
        }
    }

    public void savePartialForm() {
        FormData formData = formModel.getData();
        FormResult result = new FormResult();
        result.set_id(UUID.randomUUID().toString());

        result.setFormId(formData.getId());
        result.setFormCategoryLocale(formData.getCategory().getName());
        result.setFormNameLocale(formData.getName());
        result.setFormStatus(SyncAdapterUtils.FormStatus.PARTIAL);
        result.setCreatedAt(Util.getCurrentTimeStamp());

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
        if (mIsPartiallySaved) {
            //String processId = getArguments().getString(Constants.PM.PROCESS_ID);
            FormResult form = DatabaseManager.getDBInstance(this).getPartiallySavedForm(processId);
            if (form != null) {
                result.set_id(form.get_id());
            }
            DatabaseManager.getDBInstance(this).updateFormResult(result);
        } else {
            DatabaseManager.getDBInstance(this).insertFormResult(result);
        }

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
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        if (this == null) return;

        this.runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null && progressBar.isShown()) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void closeCurrentActivity() {

    }

    public <T> void showNextScreen(T data) {
//        formModel = PlatformGson.getPlatformGsonInstance().fromJson((String) data, Form.class);
//        //initViews();
//
//        if (mIsInEditMode) {
//            FormResult formResult = DatabaseManager.getDBInstance(getActivity()).getFormResult(processId);
//            if (formResult != null) {
//                getFormDataAndParse(formResult);
//            } else {
//                if (Util.isConnected(getContext())) {
//                    String url;
//                    if (formModel.getData() != null && formModel.getData().getApi_url() != null) {
//
//                        url = formModel.getData().getApi_url() + "/" + formModel.getData().getId();
//
//                        presenter.getFormResults(url);
//                    }
//                }
//            }
//        }
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
            button.setText(btn1String);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(v -> {
                // Close dialog
                savePartialForm();
                dialog.dismiss();
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

}
