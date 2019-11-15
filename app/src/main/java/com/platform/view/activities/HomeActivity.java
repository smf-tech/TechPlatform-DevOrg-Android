package com.platform.view.activities;


import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.platform.R;
import com.platform.dao.UserAttendanceDao;
import com.platform.database.DatabaseManager;
import com.platform.models.attendance.AttendaceData;
import com.platform.models.home.Modules;
import com.platform.models.user.UserInfo;
import com.platform.receivers.ConnectivityReceiver;
import com.platform.utility.AppEvents;
import com.platform.utility.Constants;
import com.platform.utility.ForceUpdateChecker;
import com.platform.utility.Util;
import com.platform.view.fragments.ContentManagementFragment;
import com.platform.view.fragments.HomeFragment;
import com.platform.view.fragments.PMFragment;
import com.platform.view.fragments.PlannerFragment;
import com.platform.view.fragments.ReportsFragment;
import com.platform.view.fragments.TMUserLandingFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements ForceUpdateChecker.OnUpdateNeededListener,
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        ContentManagementFragment.OnFragmentInteractionListener, ConnectivityReceiver.ConnectivityReceiverListener {

    private Toolbar toolbar;
    private OnSyncClicked clickListener;
    private ActionBarDrawerToggle toggle;
    private boolean doubleBackToExitPressedOnce = false;
    private final String TAG = this.getClass().getSimpleName();
    private BroadcastReceiver mMessageReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (Util.getUserObjectFromPref().getRoleCode()== Constants.SSModule.ROLE_CODE_SS_OPERATOR){
            Intent intent = new Intent(HomeActivity.this, OperatorMeterReadingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        initMenuView();
        initBrodcastResiver();
        subscribedToFirebaseTopics();
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
//        FirebaseMessaging.getInstance().subscribeToTopic("Test");
        String userProject = Util.getUserObjectFromPref().getProjectIds().get(0).getName();
        String userRoll = Util.getUserObjectFromPref().getRoleNames();
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
            toolbar.setNavigationIcon(R.drawable.ic_back_white);
            toolbar.setNavigationOnClickListener(view -> {
                if (toggle.isDrawerIndicatorEnabled()) {
                    DrawerLayout drawer = findViewById(R.id.home_drawer_layout);
                    drawer.openDrawer(GravityCompat.START);
                } else {
                    onBackPressed();
                    toggle.setDrawerIndicatorEnabled(true);
                    setSyncButtonVisibility(true);
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

        View headerLayout = navigationView.getHeaderView(0);
        LinearLayout profileView = headerLayout.findViewById(R.id.menu_user_profile_layout);
        TextView userName = headerLayout.findViewById(R.id.menu_user_name);
        ImageView userPic = headerLayout.findViewById(R.id.menu_user_profile_photo);

        UserInfo user = Util.getUserObjectFromPref();
        if (user != null) {
            if (!TextUtils.isEmpty(user.getProfilePic())) {
                loadProfileImage(userPic, user.getProfilePic());
            }
            userName.setText(String.format("%s", user.getUserName()));
        }
        profileView.setOnClickListener(this);

//        TextView versionName = headerLayout.findViewById(R.id.menu_user_location);
//        versionName.setText(String.format(getString(R.string.app_version) + " : %s", Util.getAppVersion()));
        TextView userProject = headerLayout.findViewById(R.id.menu_user_project);
        userProject.setText(Util.getUserObjectFromPref().getProjectIds().get(0).getName());
        TextView userRole = headerLayout.findViewById(R.id.menu_user_role);
        userRole.setText(Util.getUserObjectFromPref().getRoleNames());
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
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MV/Image/profile");
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

    private void loadFormsPage() {
        setActionBarTitle(getString(R.string.forms));
        Util.launchFragment(new PMFragment(), this,
                getString(R.string.forms), true);
    }

    private void loadMeetingsPage() {
        Util.launchFragment(new PlannerFragment(), this,
                getString(R.string.planner), true);
    }

    private void loadTeamsPage() {
        /*Util.launchFragment(new TMUserLandingFragment(), this,
                getString(R.string.approvals), true);*/
        Intent startMain = new Intent(HomeActivity.this, UserRegistrationMatrimonyActivity.class);
        startActivity(startMain);
        Intent startMain1 = new Intent(HomeActivity.this, MatrimonyProfileListActivity.class);
        startMain.putExtra("meetid", "5d6f90c25dda765c2f0b5dd4");
        startActivity(startMain1);

    }

    private void loadReportsPage() {
        Util.launchFragment(new ReportsFragment(), this,
                getString(R.string.reports), true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ForceUpdateChecker.with(this).onUpdateNeeded(this).check();
        updateUnreadNotificationsCount();
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
                AppEvents.trackAppEvent(getString(R.string.event_menu_home_click));
                break;

            case R.id.action_menu_community:
                break;

            case R.id.action_menu_forms:
                loadFormsPage();
                AppEvents.trackAppEvent(getString(R.string.event_menu_forms_click));
                break;

            case R.id.action_menu_teams:
                loadTeamsPage();
                AppEvents.trackAppEvent(getString(R.string.event_menu_teams_click));
                break;

            case R.id.action_menu_calendar:
                loadMeetingsPage();
                AppEvents.trackAppEvent(getString(R.string.event_menu_meeting_click));
                break;

            case R.id.action_menu_assets:
                break;

            case R.id.action_menu_reports:
                loadReportsPage();
                AppEvents.trackAppEvent(getString(R.string.event_menu_reports_click));
                break;

            case R.id.action_menu_connect:
                break;

            case R.id.action_menu_ho_support:
                break;

            case R.id.action_menu_notice_board:
                break;

            case R.id.action_menu_account:
                break;

            case R.id.action_menu_leaves_attendance:
                break;

            case R.id.action_menu_change_language:
                showLanguageChangeDialog();
                AppEvents.trackAppEvent(getString(R.string.event_menu_change_lang_click));
                break;

            case R.id.action_menu_rate_us:
                rateTheApp();
                AppEvents.trackAppEvent(getString(R.string.event_menu_rate_us_click));
                break;

            case R.id.action_menu_call_us:
                try {
                    Intent dial = new Intent();
                    dial.setAction("android.intent.action.DIAL");
                    dial.setData(Uri.parse("tel:" + Constants.callUsNumber));
                    startActivity(dial);
                } catch (Exception e) {
                    Log.e("Calling Phone", "" + e.getMessage());
                }
                AppEvents.trackAppEvent(getString(R.string.event_menu_call_us_click));
                break;

            case R.id.action_menu_settings:
                break;

            case R.id.action_menu_logout:
                showLogoutPopUp();
                AppEvents.trackAppEvent(getString(R.string.event_menu_logout_click));
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

        AlertDialog languageSelectionDialog = new AlertDialog.Builder(this)
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
                }).create();

        languageSelectionDialog.show();
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
        alertDialog.setIcon(R.mipmap.app_logo);
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
        alertDialog.setIcon(R.mipmap.app_logo);
        // Setting CANCEL Button
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel),
                (dialog, which) -> alertDialog.dismiss());
        // Setting OK Button
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok),
                (dialog, which) -> logOutUser());

        // Showing Alert Message
        alertDialog.show();
    }

    private void logOutUser() {
        // remove user related shared pref data
        Util.saveLoginObjectInPref("");
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
    }

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
                    setSyncButtonVisibility(true);
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
                AppEvents.trackAppEvent(getString(R.string.event_menu_profile_click));
                DrawerLayout drawer = findViewById(R.id.home_drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.home_bell_icon:
            case R.id.unread_notification_count:
                Intent intent = new Intent(this, NotificationsActivity.class);
                this.startActivityForResult(intent, Constants.Home.NEVIGET_TO);
                break;

            case R.id.home_sync_icon:
                showUpdateDataPopup();
                AppEvents.trackAppEvent(getString(R.string.event_sync_button_click));
                break;
        }
    }

    public void setSyncClickListener(OnSyncClicked listener) {
        clickListener = listener;
    }

    @Override
    public void onFragmentInteraction(String uri) {

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(isConnected) {
            Util.showToast("Connected",this);
        } else {
            Util.showToast("Not Connected",this);
        }

    }

   /* @Override
    public void onFragmentInteraction(String uri) {

    }*/

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
                        if (m.isActive()) {
                            navMenu.findItem(m.getResId()).setVisible(true);
                        } else {
                            navMenu.findItem(m.getResId()).setVisible(false);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
