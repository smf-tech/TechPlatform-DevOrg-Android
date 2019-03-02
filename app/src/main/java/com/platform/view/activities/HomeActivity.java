package com.platform.view.activities;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.platform.R;
import com.platform.models.SavedForm;
import com.platform.models.user.UserInfo;
import com.platform.presenter.PMFragmentPresenter;
import com.platform.utility.Constants;
import com.platform.utility.ForceUpdateChecker;
import com.platform.utility.Util;
import com.platform.view.fragments.FormsFragment;
import com.platform.view.fragments.HomeFragment;
import com.platform.view.fragments.ReportsFragment;
import com.platform.view.fragments.TMFragment;

import java.io.File;
import java.util.List;

public class HomeActivity extends BaseActivity implements ForceUpdateChecker.OnUpdateNeededListener,
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();
    private Toolbar toolbar;
    private OnSyncClicked clickListener;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ForceUpdateChecker.with(this).onUpdateNeeded(this).check();

        initMenuView();
    }

    public void setActionBarTitle(String name) {
        toolbar = findViewById(R.id.home_toolbar);
        TextView title = toolbar.findViewById(R.id.home_toolbar_title);
        title.setText(name);
    }

    public void setSyncButtonVisibility(boolean flag) {
        ImageView sync = findViewById(R.id.home_sync_icon);
        if (flag) {
            sync.setVisibility(View.VISIBLE);
            sync.setOnClickListener(this);
        } else {
            sync.setVisibility(View.GONE);
            sync.setOnClickListener(null);
        }
    }

    @SuppressWarnings("deprecation")
    private void initMenuView() {
        setActionBarTitle(getResources().getString(R.string.app_name_ss));

        DrawerLayout drawer = findViewById(R.id.home_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        NavigationView navigationView = findViewById(R.id.home_menu_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);
        LinearLayout profileView = headerLayout.findViewById(R.id.menu_user_profile_layout);
        TextView userName = headerLayout.findViewById(R.id.menu_user_name);
        ImageView userPic = headerLayout.findViewById(R.id.menu_user_profile_photo);

        UserInfo user = Util.getUserObjectFromPref();
        if (user != null) {
            loadProfileImage(userPic, user.getProfilePic());
            userName.setText(String.format("%s", user.getUserName()));
        }
        profileView.setOnClickListener(this);

        TextView versionName = headerLayout.findViewById(R.id.menu_user_location);
        versionName.setText(String.format(getString(R.string.app_version) + " : %s", Util.getAppVersion()));

        loadHomePage();
    }

    private void loadProfileImage(final ImageView userPic, final String profileUrl) {
        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_profile);
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
        if (TextUtils.isEmpty(profileUrl)) return;

        String[] split = profileUrl.split("/");
        String url = split[split.length - 1];
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/MV/Image/profile");
        if (!dir.exists()) {
            Log.e(TAG, "Failed to load image from SD card");
            return;
        }

        Uri uri = Uri.fromFile(new File(dir.getPath() + "/" + url));

        runOnUiThread(() -> userPic.setImageURI(uri));
    }

    private void loadHomePage() {
        try {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.home_page_container, new HomeFragment(), "homeFragment");
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
        } catch (Exception e) {
            Log.e(TAG, "Exception :: FormActivity : addFragment");
        }
    }

    private void loadFormsPage() {
        Util.launchFragment(new FormsFragment(), this, getString(R.string.forms));
    }

    private void loadTeamsPage() {
        Util.launchFragment(new TMFragment(), this, getString(R.string.team_management));
    }

    private void loadReportsPage() {
        Util.launchFragment(new ReportsFragment(), this, getString(R.string.reports));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_home_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onUpdateNeeded(String updateUrl) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_new_version_available))
                .setMessage(getString(R.string.msg_update_app))
                .setPositiveButton(getString(R.string.update),
                        (dialog1, which) -> redirectStore(updateUrl))
                .setNegativeButton(getString(R.string.no_thanks),
                        (dialog12, which) -> {
                        })
                .create();

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

            case R.id.action_menu_community:
                break;

            case R.id.action_menu_forms:
                loadFormsPage();
                break;

            case R.id.action_menu_teams:
                loadTeamsPage();
                break;

            case R.id.action_menu_calendar:
                break;

            case R.id.action_menu_assets:
                break;

            case R.id.action_menu_reports:
                loadReportsPage();
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
                break;

            case R.id.action_menu_rate_us:
                rateTheApp();
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
        final String[] items = {"English", "मराठी", "हिंदी "};

        int checkId = 0;
        if (Util.getLocaleLanguageCode().equalsIgnoreCase(Constants.App.LANGUAGE_MARATHI)) {
            checkId = 1;
        } else if (Util.getLocaleLanguageCode().equalsIgnoreCase(Constants.App.LANGUAGE_HINDI)) {
            checkId = 2;
        }

        AlertDialog languageSelectionDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.select_lang))
                .setCancelable(true)
                .setSingleChoiceItems(items, checkId, (dialogInterface, i) -> {
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
        intent.putExtra(Constants.Login.ACTION, Constants.Login.ACTION_EDIT);
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

    private void showPendingFormsPopUp() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        // Setting Dialog Title
        alertDialog.setTitle(getString(R.string.app_name_ss));
        // Setting Dialog Message
        alertDialog.setMessage("Pending forms are not synced! Please sync all pending forms to continue logout.");
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
        Util.saveLoginObjectInPref("");

        final List<SavedForm> savedForms = PMFragmentPresenter.getAllNonSyncedSavedForms();
        if (savedForms != null && !savedForms.isEmpty()) {
            showPendingFormsPopUp();
            return;
        }

        Util.removeDatabaseRecords();

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
    }

//    private void callUsDialog() {
//        final String[] items = {getString(R.string.call_on_hangout), getString(R.string.call_on_phone)};
//        final AlertDialog.Builder dialog = new AlertDialog.Builder(this).setTitle(getString(R.string.app_name));
//
//        dialog.setItems(items, (dialogInterface, position) -> {
//            dialogInterface.dismiss();
//
//            switch (position) {
//                case 0:
//                    try {
//                        Uri uri = Uri.parse(Constants.hangoutLink);
//                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                        startActivity(intent);
//                    } catch (Exception e) {
//                        Log.e("Calling Hangout", "" + e.getMessage());
//                    }
//                    break;
//
//                case 1:
//                    try {
//                        Intent dial = new Intent();
//                        dial.setAction("android.intent.action.DIAL");
//                        dial.setData(Uri.parse("tel:" + Constants.callUsNumber));
//                        startActivity(dial);
//                    } catch (Exception e) {
//                        Log.e("Calling Phone", "" + e.getMessage());
//                    }
//                    break;
//            }
//        });
//
//        dialog.show();
//    }

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

            case R.id.home_sync_icon:
                showUpdateDataPopup();
                break;
        }
    }

    public void setSyncClickListener(OnSyncClicked listener) {
        clickListener = listener;
    }

    public interface OnSyncClicked {
        void onSyncButtonClicked();
    }
}
