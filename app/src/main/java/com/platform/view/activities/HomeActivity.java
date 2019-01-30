package com.platform.view.activities;

import android.accounts.Account;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.platform.R;
import com.platform.listeners.OrgRolesRequestCallListener;
import com.platform.listeners.PlatformTaskListener;
import com.platform.models.UserInfo;
import com.platform.models.home.Home;
import com.platform.models.home.HomeModel;
import com.platform.models.home.Modules;
import com.platform.presenter.HomeActivityPresenter;
import com.platform.request.OrgRolesRequestCall;
import com.platform.syncAdapter.GenericAccountService;
import com.platform.utility.Constants;
import com.platform.utility.ForceUpdateChecker;
import com.platform.utility.Util;
import com.platform.view.adapters.HomeAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.platform.utility.Constants.SyncAdapter.ACCOUNT;
import static com.platform.utility.Constants.SyncAdapter.ACCOUNT_TYPE;
import static com.platform.utility.Constants.SyncAdapter.AUTHORITY;
import static com.platform.utility.Constants.SyncAdapter.COMPLETE;
import static com.platform.utility.Constants.SyncAdapter.ERROR;
import static com.platform.utility.Constants.SyncAdapter.PENDING;
import static com.platform.utility.Constants.SyncAdapter.STARTED;

public class HomeActivity extends BaseActivity implements PlatformTaskListener,
        ForceUpdateChecker.OnUpdateNeededListener,
        NavigationView.OnNavigationItemSelectedListener, OrgRolesRequestCallListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private AlertDialog dialogNotApproved;
    private HomeActivityPresenter presenter;
    private Object mSyncObserverHandle;
    private Button mSyncNowButton;
    public static Handler sHandler;
    private String mSyncStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        presenter = new HomeActivityPresenter(this);
        ForceUpdateChecker.with(this).onUpdateNeeded(this).check();
        dialogNotApproved = new AlertDialog.Builder(this).create();
        initMenuView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Util.isConnected(HomeActivity.this)) {
            getUserData();
        }
    }

    private void getUserData() {
        if (presenter != null) {
            UserInfo user = Util.getUserObjectFromPref();
            presenter.getModules(user);
        }

        mSyncStatusObserver.onStatusChanged(0);

        // Watch for sync state changes
        final int mask = ContentResolver.SYNC_OBSERVER_TYPE_PENDING |
                ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE;
        mSyncObserverHandle = ContentResolver.addStatusChangeListener(mask, mSyncStatusObserver);
    }

    private void initMenuView() {
        Toolbar toolbar = findViewById(R.id.home_toolbar);
        TextView title = toolbar.findViewById(R.id.home_toolbar_title);
        title.setText(R.string.app_name_ss);

        getHandler();

        DrawerLayout drawer = findViewById(R.id.home_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        NavigationView navigationView = findViewById(R.id.home_menu_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);
        TextView versionName = headerLayout.findViewById(R.id.menu_version_name);
        versionName.setText(String.format(getString(R.string.app_version) + " : %s", Util.getAppVersion()));

        mSyncNowButton = findViewById(R.id.sync_roles);
        mSyncNowButton.setOnClickListener(v -> {
            showProgressBar();
            HomeActivity.sHandler.obtainMessage(STARTED).sendToTarget();
            Constants.SyncAdapter.manualRefresh();
        });
    }

    private void getHandler() {
        sHandler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case STARTED:
                        mSyncNowButton.setEnabled(false);
                        mSyncNowButton.setClickable(false);
                        mSyncStatus = "Sync started...";
                        ((TextView) findViewById(R.id.sync_status)).setText(mSyncStatus);
                        break;

                    case PENDING:
                        ((TextView) findViewById(R.id.sync_status)).setText(mSyncStatus);
                        break;

                    case ERROR:
                        if (msg.obj instanceof String) {
                            String res = (String) msg.obj;
                            ((TextView) findViewById(R.id.roles_data)).setText(res);
                        } else if (msg.obj instanceof Exception) {
                            Exception e = (Exception) msg.obj;
                            ((TextView) findViewById(R.id.roles_data)).setText(e.getLocalizedMessage());
                            Toast.makeText(HomeActivity.this, "Sync failed!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        if (!mSyncStatus.isEmpty()) {
                            mSyncStatus += "\nSync failed!";
                        }
                        ((TextView) findViewById(R.id.sync_status)).setText(mSyncStatus);
                        mSyncStatus = "";
                        mSyncNowButton.setEnabled(true);
                        mSyncNowButton.setClickable(true);
                        break;

                    case COMPLETE:
                        if (!mSyncStatus.isEmpty()) {
                            mSyncStatus += "\nSync completed!";
                        }
                        mSyncNowButton.setEnabled(true);
                        mSyncNowButton.setClickable(true);
                        if (msg.obj instanceof String) {
                            String res = (String) msg.obj;
                            ((TextView) findViewById(R.id.roles_data)).setText(res);
                            ((TextView) findViewById(R.id.sync_status)).setText(mSyncStatus);
                        }
                        mSyncStatus = "";
                        break;
                }
            }
        };
    }

    @SuppressWarnings("deprecation")
    private void initViews(Home data) {
        if (data != null) {
            ArrayList<HomeModel> modelItemList = new ArrayList<>();

            if (data.getUserApproveStatus().equalsIgnoreCase(Constants.PENDING)) {
                showApprovedDialog();

                List<Modules> defaultModules = data.getHomeData().getDefaultModules();
                for (Modules modules : defaultModules) {
                    modelItemList.add(setClass(modules, false));
                }
            } else {
                List<Modules> onApproveModules = data.getHomeData().getOnApproveModules();
                for (Modules modules : onApproveModules) {
                    modelItemList.add(setClass(modules, true));
                }
            }

            RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
            itemAnimator.setAddDuration(1000);
            itemAnimator.setRemoveDuration(1000);

            RecyclerView recyclerView = findViewById(R.id.home_tiles_list_view);
            recyclerView.setItemAnimator(itemAnimator);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
            gridLayoutManager.setAutoMeasureEnabled(true);
            recyclerView.setLayoutManager(gridLayoutManager);

            HomeAdapter homeAdapter = new HomeAdapter(modelItemList, HomeActivity.this);
            recyclerView.setAdapter(homeAdapter);
        }
    }

    private void showApprovedDialog() {
        if (dialogNotApproved.isShowing()) {
            dialogNotApproved.dismiss();
        }

        dialogNotApproved.setTitle(getString(R.string.app_name_ss));
        String message = getString(R.string.approve_profile);
        dialogNotApproved.setMessage(message);

        // Setting Icon to Dialog
        dialogNotApproved.setIcon(R.mipmap.app_logo);

        // Setting OK Button
        dialogNotApproved.setButton(Dialog.BUTTON_POSITIVE, getString(android.R.string.ok),
                (dialog, which) -> dialogNotApproved.dismiss());

        try {
            // Showing Alert Message
            dialogNotApproved.show();
        } catch (Exception e) {
            Log.e("Error in showing dialog", e.getMessage());
        }
    }

    private HomeModel setClass(Modules module, Boolean isAccessible) {
        HomeModel homeModel = new HomeModel();
        homeModel.setModuleId(module.getId());
        homeModel.setAccessible(isAccessible);

        switch (module.getName()) {
            case Constants.Home.PROGRAMME_MANAGEMENT:
                homeModel.setModuleName(getString(R.string.programme_management));
                homeModel.setModuleIcon(R.drawable.ic_program_mangement);
                homeModel.setDestination(PMActivity.class);
                break;

            case Constants.Home.TEAM_MANAGEMENT:
                homeModel.setModuleName(getString(R.string.team_management));
                homeModel.setModuleIcon(R.drawable.ic_team_management);
                homeModel.setDestination(TMActivity.class);
                break;
        }

        return homeModel;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.IS_ROLE_CHANGE && resultCode == RESULT_OK) {
            if (dialogNotApproved != null && dialogNotApproved.isShowing()) {
                dialogNotApproved.dismiss();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_home_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (dialogNotApproved != null) {
            dialogNotApproved.dismiss();
        }
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public <T> void showNextScreen(T data) {
        initViews((Home) data);
    }

    @Override
    public void showErrorMessage(String result) {
        Util.showToast(result, this);
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
            e.printStackTrace();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_lang:
                showLanguageChangeDialog();
                break;

            case R.id.action_profile:
                showProfileScreen();
                break;

            case R.id.action_update_user_data:
                showUpdateDataPopup();
                break;

            case R.id.action_forms:
                goToForms();
                break;

            case R.id.action_logout:
                showLogoutPopUp();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.home_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void goToForms() {
        Intent intent = new Intent(this, FormsActivity.class);
        intent.putExtra(Constants.Login.ACTION, Constants.Login.ACTION_EDIT);
        startActivityForResult(intent, Constants.IS_ROLE_CHANGE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_lang:
                showLanguageChangeDialog();
                return true;

            case R.id.action_profile:
                showProfileScreen();
                return true;

            case R.id.action_update_user_data:
                showUpdateDataPopup();
                return true;

            case R.id.action_logout:
                showLogoutPopUp();
                return true;

            default:
                return super.onOptionsItemSelected(item);
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
                        getUserData();
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
        Util.clearAllUserData();

        try {
            Intent startMain = new Intent(HomeActivity.this, LoginActivity.class);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(startMain);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    SyncStatusObserver mSyncStatusObserver = which -> {
        Account account = GenericAccountService.GetAccount(ACCOUNT, ACCOUNT_TYPE);

        boolean syncActive = ContentResolver.isSyncActive(
                account, Constants.SyncAdapter.AUTHORITY);
        boolean syncPending = ContentResolver.isSyncPending(
                account, Constants.SyncAdapter.AUTHORITY);

        if (!syncActive && !syncPending) {
            runOnUiThread(() -> mSyncNowButton.setEnabled(true));
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        if (mSyncObserverHandle != null) {
            ContentResolver.removeStatusChangeListener(mSyncObserverHandle);
            mSyncObserverHandle = null;
        }
    }

    @Override
    public void onFailureListener(String message) {
        hideProgressBar();
        Toast.makeText(this, "Error occurred while fetched roles!", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Roles#Error: \n" + message);
    }

    @Override
    public void onErrorListener(VolleyError error) {
        hideProgressBar();
        Toast.makeText(this, "Error occurred while fetched roles!", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Roles#Error: \n" + error.getMessage());
    }

    @Override
    public void onSuccessListener(String response) {
        hideProgressBar();
        Log.i(TAG, "Roles#Success: \n" + response);
        Toast.makeText(this, "Roles fetched!!!", Toast.LENGTH_SHORT).show();

        ((TextView) findViewById(R.id.roles_data)).setText(response);
    }

}
