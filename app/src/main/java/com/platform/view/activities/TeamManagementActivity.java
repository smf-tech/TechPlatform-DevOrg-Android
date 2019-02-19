package com.platform.view.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.user.UserInfo;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.fragments.TMFragment;

public class TeamManagementActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reports);

        addFragment();
        initMenuView();
    }

    @SuppressWarnings("deprecation")
    private void initMenuView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.home_toolbar_title);
        title.setText(R.string.team_management);

        DrawerLayout drawer = findViewById(R.id.home_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        NavigationView navigationView = findViewById(R.id.home_menu_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);
        TextView userName = headerLayout.findViewById(R.id.menu_user_name);
        UserInfo user = Util.getUserObjectFromPref();
        if (user != null) {
            userName.setText(String.format("%s %s", user.getUserFirstName(), user.getUserLastName()));
        }

        TextView versionName = headerLayout.findViewById(R.id.menu_user_location);
        versionName.setText(String.format(getString(R.string.app_version) + " : %s", Util.getAppVersion()));
    }

    private void addFragment() {
        try {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.gen_form_container, TMFragment.newInstance(false), "tmFragment");
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
        } catch (Exception e) {
            Log.e(TAG, "Exception :: FormActivity : addFragment");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {
        handleMenuItems(menuItem.getItemId());

        DrawerLayout drawer = findViewById(R.id.home_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_home_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        handleMenuItems(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    private void handleMenuItems(int menuId) {
        switch (menuId) {
            case R.id.action_menu_home:
                Util.start(this, HomeActivity.class, Bundle.EMPTY);
                finish();
                break;

            case R.id.action_menu_community:
                showProfileScreen();
                break;

            case R.id.action_menu_teams:
                break;

            case R.id.action_menu_forms:
                goToForms();
                break;

            case R.id.action_menu_calendar:
                break;

            case R.id.action_menu_assets:
                break;

            case R.id.action_menu_reports:
                Util.start(this, ReportsActivity.class, Bundle.EMPTY);
                finish();
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

            case R.id.action_menu_rate_us:
                break;

            case R.id.action_menu_call_us:
                break;

            case R.id.action_menu_settings:
                break;

            case R.id.action_menu_logout:
                showLogoutPopUp();
                break;
        }
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
        Util.clearAllUserData();

        try {
            Intent startMain = new Intent(this, LoginActivity.class);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(startMain);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void goToForms() {
//        try {
//            Intent intent = new Intent(this, FormsActivity.class);
//            intent.putExtra(Constants.Login.ACTION, Constants.Login.ACTION_EDIT);
//            startActivity(intent);
//            finish();
//        } catch (Exception e) {
//            Log.e(TAG, e.getMessage());
//        }
    }

}