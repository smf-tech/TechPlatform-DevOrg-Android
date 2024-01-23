package com.octopusbjsindia.view.activities;


import static com.octopusbjsindia.utility.Util.getUserObjectFromPref;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.appconfig.AppConfigResponseModel;
import com.octopusbjsindia.models.home.Modules;
import com.octopusbjsindia.models.user.UserInfo;
import com.octopusbjsindia.receivers.ConnectivityReceiver;
import com.octopusbjsindia.syncAdapter.SyncAdapterUtils;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.ForceUpdateChecker;
import com.octopusbjsindia.utility.PreferenceHelper;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.fragments.HomeFragment;
import com.octopusbjsindia.view.fragments.StoriesFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

public class HomeActivity extends BaseActivity implements ForceUpdateChecker.OnUpdateNeededListener,
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {

    private Toolbar toolbar;
    private OnSyncClicked clickListener;
    private ActionBarDrawerToggle toggle;
    private boolean doubleBackToExitPressedOnce = false;
    private final String TAG = this.getClass().getSimpleName();
    private BroadcastReceiver mMessageReceiver;
    private BroadcastReceiver connectionReceiver;
    private String toOpen;
    View headerLayout;

    public LinearLayout lytProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Log.d("Rolecode","" + Util.getUserObjectFromPref().getRoleCode());
        if (getUserObjectFromPref().getRoleCode() == Constants.SSModule.ROLE_CODE_SS_OPERATOR) {
            Intent intent = new Intent(HomeActivity.this, OperatorActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        lytProgress = findViewById(R.id.lyt_progress);

        initMenuView();
        initBrodcastResiver();
        subscribedToFirebaseTopics();
        initConnectivityReceiver();

        toOpen = getIntent().getStringExtra("toOpen");
        if (toOpen != null) {
            Intent intent;
            switch (toOpen) {
                case "formApproval":
                    intent = new Intent(this, TMFiltersListActivity.class);
                    intent.putExtra("filter_type", "forms");
                    this.startActivity(intent);
                    break;
                case "userApproval":
                    intent = new Intent(this, TMFiltersListActivity.class);
                    intent.putExtra("filter_type", "userapproval");
                    this.startActivity(intent);
                    break;
                case "leaveApproval":
                    intent = new Intent(this, TMFiltersListActivity.class);
                    intent.putExtra("filter_type", "leave");
                    this.startActivity(intent);
                    break;
                case "attendanceApproval":
                    intent = new Intent(this, TMFiltersListActivity.class);
                    intent.putExtra("filter_type", "attendance");
                    this.startActivity(intent);
                    break;
                case "compoffApproval":
                    intent = new Intent(this, TMFiltersListActivity.class);
                    intent.putExtra("filter_type", "compoff");
                    this.startActivity(intent);
                    break;
                case "event":
                    intent = new Intent(this, PlannerDetailActivity.class);
                    intent.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.EVENTS_LABEL);
                    this.startActivity(intent);
                    break;
                case "task":
                    intent = new Intent(this, PlannerDetailActivity.class);
                    intent.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.TASKS_LABEL);
                    this.startActivity(intent);
                    break;
                case "leave":
                    intent = new Intent(this, GeneralActionsActivity.class);
                    intent.putExtra("title", this.getString(R.string.leave));
                    intent.putExtra("switch_fragments", "LeaveDetailsFragment");
                    this.startActivity(intent);
                    break;
                case "structure":
                    intent = new Intent(this, SSActionsActivity.class);
                    intent.putExtra("SwitchToFragment", "StructureMachineListFragment");
                    intent.putExtra("viewType", 1);
                    intent.putExtra("title", "Waterbody List");
                    this.startActivity(intent);
                    break;
                case "machine":
                    intent = new Intent(this, SSActionsActivity.class);
                    intent.putExtra("SwitchToFragment", "StructureMachineListFragment");
                    intent.putExtra("viewType", 2);
                    intent.putExtra("title", "Machine List");
                    this.startActivity(intent);
                    break;
            }
        }

        boolean showNotificationDialog = Platform.getInstance().getSharedPreferences(Constants.App.FIRST_TIME_KEY, Context.MODE_PRIVATE).getBoolean("notificationPermissionDialogShown", false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                findViewById(R.id.unread_notification_count).setVisibility(View.VISIBLE);
                if (!showNotificationDialog) {
                    showNotificationPermissionNeededDialog();
                    Platform.getInstance().getSharedPreferences(Constants.App.FIRST_TIME_KEY,
                            Context.MODE_PRIVATE).edit().putBoolean("notificationPermissionDialogShown", true).apply();
                }
            } else {
                findViewById(R.id.unread_notification_count).setVisibility(View.GONE);
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted) {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                        showManualNotificationPermissionRequiredDialog();
                    }
                }
            });

    private void showManualNotificationPermissionRequiredDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Notification permission request")
                .setMessage("Since you have denied the notification permission earlier, now you have to enable it manually from app settings. Click on open settings to enable permissions manually.")
                .setCancelable(false)
                .setPositiveButton("Open settings", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }


    void showNotificationPermissionNeededDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = LayoutInflater.from(this).inflate(R.layout.notification_permission_dialog, null);

        bottomSheetDialog.setCancelable(false);
        MaterialButton btNext = bottomSheetView.findViewById(R.id.bt_next);
        MaterialButton btSkip = bottomSheetView.findViewById(R.id.bt_skip);
        ImageView close = bottomSheetView.findViewById(R.id.ic_close);
        close.setOnClickListener(v1 -> bottomSheetDialog.dismiss());

        btNext.setOnClickListener(v1 -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                }
            }
            bottomSheetDialog.dismiss();
        });

        btSkip.setOnClickListener(v1 -> {
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }


    private void initConnectivityReceiver() {
        connectionReceiver = new ConnectivityReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        Platform.getInstance().registerReceiver(connectionReceiver, filter);
    }

    private void initBrodcastResiver() {
        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadNotificationsCount();
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(Constants.PUSH_NOTIFICATION));
    }

    private void subscribedToFirebaseTopics() {
        FirebaseMessaging.getInstance().subscribeToTopic("Test");
        String userProject = getUserObjectFromPref().getProjectIds().get(0).getName();
        String userRoll = getUserObjectFromPref().getRoleNames();
        userProject = userProject.replaceAll(" ", "_");
        userRoll = userRoll.replaceAll(" ", "_");

        if ((userProject).equals(Util.getStringFromPref(Constants.App.FirebaseTopicProjectWise))
                || Util.getStringFromPref(Constants.App.FirebaseTopicProjectWise).equals("")) {
            Util.setStringInPref(Constants.App.FirebaseTopicProjectWise, userProject);
            FirebaseMessaging.getInstance().subscribeToTopic(userProject);
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(Util.getStringFromPref(Constants.App.FirebaseTopicProjectWise));
            FirebaseMessaging.getInstance().subscribeToTopic(userProject);
            Util.setStringInPref(Constants.App.FirebaseTopicProjectWise, userProject);
        }

        if ((userProject + "_" + userRoll).equals(Util.getStringFromPref(Constants.App.FirebaseTopicProjectRoleWise))
                || Util.getStringFromPref(Constants.App.FirebaseTopicProjectRoleWise).equals("")) {
            Util.setStringInPref(Constants.App.FirebaseTopicProjectRoleWise, userProject + "_" + userRoll);
            FirebaseMessaging.getInstance().subscribeToTopic(userProject + "_" + userRoll);
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(Util.getStringFromPref(Constants.App.FirebaseTopicProjectRoleWise));
            FirebaseMessaging.getInstance().subscribeToTopic(userProject + "_" + userRoll);
            Util.setStringInPref(Constants.App.FirebaseTopicProjectRoleWise, userProject + "_" + userRoll);
        }
        PreferenceHelper preferenceHelper = new PreferenceHelper(Platform.getInstance());
        if (preferenceHelper.getCheckOutStatus(PreferenceHelper.TOKEN_KEY)) {
            String token = preferenceHelper.getString(PreferenceHelper.TOKEN);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("actionType", "updateFirebaseId");
                jsonObject.put("firebase_id", token);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (jsonObject != null) {
                Util.updateFirebaseIdRequests(jsonObject);
            }
        }
    }

    public void setActionBarTitle(String name) {
        toolbar = findViewById(R.id.home_toolbar);
        if (toolbar != null) {
            TextView title = toolbar.findViewById(R.id.home_toolbar_title);
            title.setText(name);
        }
    }

    public void showBackArrow() {
        if (toggle != null) {
            toggle.setDrawerIndicatorEnabled(false);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24);
            toolbar.setNavigationOnClickListener(view -> {
                if (toggle.isDrawerIndicatorEnabled()) {
                    DrawerLayout drawer = findViewById(R.id.home_drawer_layout);
                    drawer.openDrawer(GravityCompat.START);
                } else {
                    onBackPressed();
                    toggle.setDrawerIndicatorEnabled(true);
                    //setSyncButtonVisibility(true);
                }
            });
        }
    }

    public void setSyncButtonVisibility(boolean flag) {
        ImageView sync = findViewById(R.id.home_sync_icon);
        if (sync != null) {
            if (flag) {
                sync.setVisibility(View.VISIBLE);
                sync.setOnClickListener(this);
            } else {
                sync.setVisibility(View.GONE);
                sync.setOnClickListener(null);
            }
        }
    }

    private void initMenuView() {
        setActionBarTitle(getResources().getString(R.string.app_name_ss));
        DrawerLayout drawer = findViewById(R.id.home_drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        findViewById(R.id.home_bell_icon).setOnClickListener(this);
        findViewById(R.id.unread_notification_count).setOnClickListener(this);

        NavigationView navigationView = findViewById(R.id.home_menu_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerLayout = navigationView.getHeaderView(0);
        LinearLayout profileView = headerLayout.findViewById(R.id.menu_user_profile_layout);
        TextView userName = headerLayout.findViewById(R.id.menu_user_name);
        ImageView userPic = headerLayout.findViewById(R.id.menu_user_profile_photo);

        UserInfo user = getUserObjectFromPref();
        if (user != null) {
            if (!TextUtils.isEmpty(user.getProfilePic())) {
                loadProfileImage(userPic, user.getProfilePic());
            }
            userName.setText(String.format("%s", user.getUserName()));
        }
        profileView.setOnClickListener(this);
        TextView tvAppVersion = headerLayout.findViewById(R.id.menu_app_version);
        String appVersion;
        try {
            appVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            tvAppVersion.setText("Version" + " " + appVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        loadHomePage();
    }

    private void updateUnreadNotificationsCount() {
        int notificationsCount = Util.getUnreadNotificationsCount();
        if (notificationsCount > 0) {
            findViewById(R.id.unread_notification_count).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.unread_notification_count))
                    .setText(String.valueOf(notificationsCount));
        } else {
            findViewById(R.id.unread_notification_count).setVisibility(View.GONE);
        }
    }

    private void loadProfileImage(final ImageView userPic, final String profileUrl) {
        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_user_avatar);
        if (!TextUtils.isEmpty(profileUrl)) {
            requestOptions = requestOptions.apply(RequestOptions.circleCropTransform());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            userPic.setLayoutParams(params);
        }
        loadFromSDCard(userPic, profileUrl);
        Glide.with(this)
                .load(profileUrl)
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable final GlideException e, final Object model,
                                                final Target<Drawable> target, final boolean isFirstResource) {
                        Log.e(TAG, "onLoadFailed: ");

//                        return !loadFromSDCard(userPic);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(final Drawable resource, final Object model,
                                                   final Target<Drawable> target, final DataSource dataSource,
                                                   final boolean isFirstResource) {
                        return false;
                    }
                })
                .into(userPic);
    }

    private void loadFromSDCard(final ImageView userPic, final String profileUrl) {
        if (TextUtils.isEmpty(profileUrl)) {
            return;
        }
        String[] split = profileUrl.split("/");
        String url = split[split.length - 1];
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Octopus/Image/profile");
        if (!dir.exists()) {
            Log.e(TAG, "Failed to load image from SD card");
            return;
        }
        Uri uri = Uri.fromFile(new File(dir.getPath() + "/" + url));
        runOnUiThread(() -> userPic.setImageURI(uri));
    }

    private void loadHomePage() {
        if (findViewById(R.id.home_bell_icon).getVisibility() == View.GONE) {
            findViewById(R.id.home_bell_icon).setVisibility(View.VISIBLE);
            updateUnreadNotificationsCount();
        }
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            getSupportFragmentManager().popBackStack();
        }
        Util.launchFragment(new HomeFragment(), this,
                getString(R.string.app_name_ss), false);
    }

    private void loadStories(){
        /*for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            getSupportFragmentManager().popBackStack();
        }*/
        Util.launchFragment(new StoriesFragment(), this,
                getString(R.string.menu_stories), true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ForceUpdateChecker.with(this).onUpdateNeeded(this).check();
        Platform.getInstance().setConnectivityListener(this);
        updateUnreadNotificationsCount();
        TextView userProject = headerLayout.findViewById(R.id.menu_user_project);
        userProject.setText(getUserObjectFromPref().getProjectIds().get(0).getName());
        TextView userRole = headerLayout.findViewById(R.id.menu_user_role);
        userRole.setText(getUserObjectFromPref().getRoleNames());

        // Start data sync
        SyncAdapterUtils.manualRefresh();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                findViewById(R.id.unread_notification_count).setVisibility(View.GONE);
            } else findViewById(R.id.unread_notification_count).setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_home_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onUpdateNeeded(final String updateUrl, boolean forcefulUpdate) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_new_version_available))
                .setPositiveButton(getString(R.string.update),
                        (dialog1, which) -> redirectStore(updateUrl));

        if (forcefulUpdate) {
            dialog.setMessage(getString(R.string.msg_need_force_update));
            dialog.setCancelable(false);
        } else {
            dialog.setMessage(getString(R.string.msg_update_app));
            dialog.setCancelable(true);
            dialog.setNegativeButton(getString(R.string.no_thanks),
                    (dialog12, which) -> {
                    });
        }

        dialog.create();
        dialog.show();
    }

    private void redirectStore(String updateUrl) {
        try {
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void handleMenuItems(int menuId) {
        switch (menuId) {
            case R.id.action_menu_home:
                loadHomePage();
                break;

            case R.id.action_menu_stories:
                loadStories();
                break;

            case R.id.action_home_sync:
                showUpdateDataPopup();
                break;

            case R.id.action_menu_change_language:
                showLanguageChangeDialog();
                break;

            case R.id.action_menu_share_app:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Octopus share");
                    String shareMessage = "\nPlease checkout the Octopus app from Bhartiya Jain Sanghatana\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
                break;

            case R.id.action_menu_rate_us:
                rateTheApp();
                break;

            case R.id.action_menu_call_us:
                try {
                    AppConfigResponseModel obj = new Gson().fromJson(
                            Util.getStringFromPref(Constants.OperatorModule.APP_CONFIG_RESPONSE),
                            AppConfigResponseModel.class);

                    Intent dial = new Intent();
                    dial.setAction("android.intent.action.DIAL");

                    if (obj != null && obj.getAppConfigResponse().getAppUpdate().getSupport() != null) {
                        dial.setData(Uri.parse("tel:" + obj.getAppConfigResponse().getAppUpdate().getSupport()));
                    } else {
                        dial.setData(Uri.parse("tel:" + Constants.callUsNumber));
                    }

                    startActivity(dial);
                } catch (Exception e) {
                    Log.e("Calling Phone", "" + e.getMessage());
                }
                break;

            case R.id.action_menu_settings:
                break;

            case R.id.action_menu_logout:
                showLogoutPopUp();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.home_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        handleMenuItems(menuItem.getItemId());

        DrawerLayout drawer = findViewById(R.id.home_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        handleMenuItems(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    private void showLanguageChangeDialog() {
        int checkId = 0;
        if (Util.getLocaleLanguageCode().equalsIgnoreCase(Constants.App.LANGUAGE_MARATHI)) {
            checkId = 1;
        } else if (Util.getLocaleLanguageCode().equalsIgnoreCase(Constants.App.LANGUAGE_HINDI)) {
            checkId = 2;
        }

        new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.select_lang))
                .setCancelable(true)
                .setSingleChoiceItems(Constants.App.APP_LANGUAGE, checkId, (dialogInterface, i) -> {
                })
                .setPositiveButton(R.string.ok, (dialog, id) -> {
                    ListView listView = ((AlertDialog) dialog).getListView();
                    switch (listView.getCheckedItemPosition()) {
                        case 0:
                            Util.setLocaleLanguageCode(Constants.App.LANGUAGE_ENGLISH);
                            break;

                        case 1:
                            Util.setLocaleLanguageCode(Constants.App.LANGUAGE_MARATHI);
                            break;

                        case 2:
                            Util.setLocaleLanguageCode(Constants.App.LANGUAGE_HINDI);
                            break;
                    }

                    Util.setFirstTimeLaunch(false);
                    dialog.dismiss();
                    finish();
                    startActivity(getIntent());
                }).show();
    }

    private void showProfileScreen() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivityForResult(intent, Constants.IS_ROLE_CHANGE);
    }

    private void showUpdateDataPopup() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        // Setting Dialog Title
        alertDialog.setTitle(getString(R.string.app_name_ss));
        // Setting Dialog Message
        alertDialog.setMessage(getString(R.string.update_data_string));
        // Setting Icon to Dialog
        alertDialog.setIcon(R.mipmap.ic_launcher);
        // Setting CANCEL Button
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel),
                (dialog, which) -> alertDialog.dismiss());
        // Setting OK Button
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok),
                (dialog, which) -> {
                    if (Util.isConnected(HomeActivity.this)) {
                        clickListener.onSyncButtonClicked();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }

    private void showLogoutPopUp() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        // Setting Dialog Title
        alertDialog.setTitle(getString(R.string.app_name_ss));
        // Setting Dialog Message
        alertDialog.setMessage(getString(R.string.msg_logout));
        // Setting Icon to Dialog
        alertDialog.setIcon(R.mipmap.ic_launcher);
        // Setting CANCEL Button
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel),
                (dialog, which) -> alertDialog.dismiss());
        // Setting OK Button
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok),
                (dialog, which) -> Util.logOutUser(this));

        // Showing Alert Message
        alertDialog.show();
    }

    /*private void logOutUser() {
        //before logout, we should remove firebase_id from backend.
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("actionType", "removeFirebaseId");
            jsonObject.put("user_id", getUserObjectFromPref().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject != null) {
            Util.updateFirebaseIdRequests(jsonObject);

        }
        // remove user related shared pref data
        Util.saveLoginObjectInPref("");
        Util.saveUserObjectInPref("");
        Util.setSubmittedFormsLoaded(false);
        Util.removeDatabaseRecords(false);
        try {
            Intent startMain = new Intent(HomeActivity.this, LoginActivity.class);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(startMain);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }*/

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                try {
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(startMain);
                    System.exit(0);
                } catch (Exception e) {
                    Log.e(TAG, "Exception :: LoginActivity : onBackPressed");
                }
                finish();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, getString(R.string.back_string), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        } else {
            try {
                getSupportFragmentManager().popBackStackImmediate(null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);

                String tag = null;
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    for (final Fragment fragment : getSupportFragmentManager().getFragments()) {
                        if (fragment instanceof HomeFragment) {
                            tag = fragment.getTag();
                        }
                    }
                    if (TextUtils.isEmpty(tag)) {
                        tag = getString(R.string.app_name_ss);
                    }
                }
                setActionBarTitle(tag);

                if (tag != null && tag.equals(getString(R.string.app_name_ss))) {
                    if (findViewById(R.id.home_bell_icon).getVisibility() == View.GONE) {
                        findViewById(R.id.home_bell_icon).setVisibility(View.VISIBLE);
                        updateUnreadNotificationsCount();
                    }
                }

                if (!toggle.isDrawerIndicatorEnabled()) {
                    toggle.setDrawerIndicatorEnabled(true);
                    //setSyncButtonVisibility(true);
                }

            } catch (Exception e) {
                Log.e(TAG, "Exception :: HomeActivity : onBackPressed");
            }
        }
    }

    private void rateTheApp() {
        try {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_user_profile_layout:
                showProfileScreen();
                DrawerLayout drawer = findViewById(R.id.home_drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.home_bell_icon:
            case R.id.unread_notification_count:
              /*  Intent intent = new Intent(this, NotificationsActivity.class);
                this.startActivityForResult(intent, Constants.Home.NEVIGET_TO);*/

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        showNotificationPermissionNeededDialog();
                    } else {
                        Intent intent = new Intent(this, NotificationsActivity.class);
                        this.startActivityForResult(intent, Constants.Home.NEVIGET_TO);
                    }
                } else {
                    Intent intent = new Intent(this, NotificationsActivity.class);
                    this.startActivityForResult(intent, Constants.Home.NEVIGET_TO);
                }


                break;

//            case R.id.home_sync_icon:
//                showUpdateDataPopup();
//                AppEvents.trackAppEvent(getString(R.string.event_sync_button_click));
//                break;
        }
    }

    public void setSyncClickListener(OnSyncClicked listener) {
        clickListener = listener;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        ImageView sync = findViewById(R.id.home_sync_icon);
        if (isConnected) {
            sync.setImageResource(R.drawable.ic_wifi_24);
//            Util.snackBarToShowMsg(getWindow().getDecorView()
//                            .findViewById(android.R.id.content), "Internet connection is available.",
//                    Snackbar.LENGTH_LONG);
        } else {
            sync.setImageResource(R.drawable.ic_wifi_off_24);
            Util.snackBarToShowMsg(getWindow().getDecorView()
                            .findViewById(android.R.id.content), "No internet connection.",
                    Snackbar.LENGTH_LONG);
        }
    }

    public interface OnSyncClicked {
        void onSyncButtonClicked();
    }

    public void hideItem(List<Modules> tabNames) {
        try {
            NavigationView navigationView = findViewById(R.id.home_menu_view);
            if (navigationView != null) {
                Menu navMenu = navigationView.getMenu();
                if (navMenu != null) {
                    for (Modules m : tabNames) {
                        navMenu.findItem(m.getResId()).setVisible(m.isActive());
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
