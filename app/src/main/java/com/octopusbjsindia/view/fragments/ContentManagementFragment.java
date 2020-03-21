package com.octopusbjsindia.view.fragments;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.adapter.ExpandableListAdapter;
import com.octopusbjsindia.dao.ContentDataDao;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.content.ContentData;
import com.octopusbjsindia.models.content.Datum;
import com.octopusbjsindia.models.content.Datum_;
import com.octopusbjsindia.models.content.DownloadContent;
import com.octopusbjsindia.models.content.DownloadInfo;
import com.octopusbjsindia.presenter.ContentFragmentPresenter;
import com.octopusbjsindia.services.ShowTimerService;
import com.octopusbjsindia.utility.AppEvents;
import com.octopusbjsindia.utility.PreferenceHelper;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.HomeActivity;
import com.octopusbjsindia.view.activities.LoginActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.DOWNLOAD_SERVICE;

public class ContentManagementFragment extends Fragment implements APIDataListener {

    private View contentFragmentview;
    private ImageView backButton;
    private FloatingActionButton btn_floating_download;
    private ExpandableListView expListView;
    private List<String> listDataHeader = new ArrayList<>();
    private List<DownloadContent> listDownloadContent;
    private HashMap<String, List<ContentData>> listDataChild = new HashMap<>();
    private TextView txt_noData;
    private File f;
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Octopus";
    private long downloadID;
    private String TAG = ContentManagementFragment.class.getSimpleName();
    private List<Datum> contentList;
    //private List<ContentDatum> contentDataList;
    private List<Datum_> contentCategoryList;
    private DownloadContent downloadContent;
    private ExpandableListAdapter expandableListAdapter;
    private String downloadFilePath = "";
    private String filename;

    private ImageView imgDwn, imgShare;
    private RelativeLayout progressBarLayout;
    private ProgressBar progressBar, mProgressBar;
    private ShowTimerService showTimerService;
    boolean mBound = false;
    private Handler handler = new Handler();
    private List<Long> downloadIdList = new ArrayList<>();
    DownloadInfo downloadInfo;
    DownloadManager downloadmanager;
    private Activity activity;
    private PreferenceHelper preferenceHelper;
    private ContentFragmentPresenter presenter;
    private ArrayList<ContentData> contentDataList = new ArrayList<>();
    ContentDataDao contentDataDao;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null && getArguments() != null) {
            String title = (String) getArguments().getSerializable("TITLE");
            ((HomeActivity) getActivity()).setActionBarTitle(title);
            ((HomeActivity) getActivity()).setSyncButtonVisibility(false);

            if ((boolean) getArguments().getSerializable("SHOW_BACK")) {
                ((HomeActivity) getActivity()).showBackArrow();
            }
        }
        AppEvents.trackAppEvent(getString(R.string.event_content_screen_visit));
        getActivity().registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentFragmentview = inflater.inflate(R.layout.fragment_content_management, container, false);
        return contentFragmentview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preferenceHelper = new PreferenceHelper(activity);
        initViews();
    }

    private void initViews() {
        progressBarLayout = contentFragmentview.findViewById(R.id.profile_act_progress_bar);
        progressBar = contentFragmentview.findViewById(R.id.pb_profile_act);
        txt_noData = contentFragmentview.findViewById(R.id.textNoData);
        contentDataDao = DatabaseManager.getDBInstance(Platform.getInstance()).getContentDataDao();
        //btn_floating_download = contentFragmentview.findViewById(R.id.btn_floating_content);
        expListView = contentFragmentview.findViewById(R.id.lvExp);
        expandableListAdapter = new ExpandableListAdapter(ContentManagementFragment.this,
                listDataHeader, listDataChild, getContext());
        expListView.setAdapter(expandableListAdapter);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                //expandableListAdapter.notifyDataSetChanged();
                return false;

            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {

            }
        });

        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                ContentData dwncontent = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                return false;
            }
        });
//        btn_floating_download.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Permissions.isReadExternalStotagePermission(getActivity(), this)) {
//                    Intent callDownloadActivity = new Intent(getActivity(), ContentDownloadedActivity.class);
//                    startActivity(callDownloadActivity);
//                }
//
//            }
//        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.isConnected(getContext())) {
            presenter = new ContentFragmentPresenter(this);
            presenter.getContentData();
        } else {
            Util.showToast(getString(R.string.msg_no_network), this);
        }
        if (expandableListAdapter != null) {
            expandableListAdapter.notifyDataSetChanged();
        }
    }

    private void updateListView() {
        listDataHeader.clear();
        listDataChild.clear();
        if (listDataHeader.size() > 0) {
            txt_noData.setVisibility(View.GONE);
        } else {
            txt_noData.setVisibility(View.VISIBLE);
        }
        List<String> categoryNames = contentDataDao.getDistinctCategories();
        listDataHeader.addAll(categoryNames);
        for (String categoryName : listDataHeader) {
            listDataChild.put(categoryName, contentDataDao.getCategoryContent(
                    categoryName));
        }
        expandableListAdapter.notifyDataSetChanged();
    }

    public boolean beginDownload(String url) {
        boolean isRunning = false;
        downloadmanager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        File file = new File(uri.getPath());
        filename = file.getName();

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("Octopus");
        request.setDescription("Downloading");
        request.setDestinationInExternalPublicDir("/Octopus", filename);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

//        if (Permissions.isCameraPermissionGranted(getActivity(), getActivity())) {
//            request.setDestinationInExternalPublicDir("/Octopus", filename);
//        } else {
//
//        }

        //downloadFilePath = path + filename;
        downloadID = downloadmanager.enqueue(request);
        downloadIdList.add(downloadID);

        Cursor cursor = downloadmanager.query(new DownloadManager.Query().setFilterById(downloadID));

        if (cursor != null && cursor.moveToNext()) {
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            cursor.close();

            if (status == DownloadManager.STATUS_FAILED) {
                // do something when failed
            } else if (status == DownloadManager.STATUS_PENDING || status == DownloadManager.STATUS_PAUSED) {
                // do something pending or paused
            } else if (status == DownloadManager.STATUS_SUCCESSFUL) {
                // do something when successful
            } else if (status == DownloadManager.STATUS_RUNNING) {
                // do something when running
                isRunning = true;
            }
        }
        return isRunning;
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        if (presenter != null) {
//            presenter.clearData();
//            presenter = null;
//        }
    }

    // broadcast receiver for download a file
    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent data = intent;
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                Toast.makeText(getContext(), "Download completed.", Toast.LENGTH_LONG).show();
                //mProgressBar.setVisibility(View.GONE);

                expandableListAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        //getActivity().unregisterReceiver(onDownloadComplete);
    }


    @Override
    public void onFailureListener(String requestID, String message) {
        showResponse(getResources().getString(R.string.msg_something_went_wrong));
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        showResponse(getResources().getString(R.string.msg_something_went_wrong));
    }

    @Override
    public void onSuccessListener(String requestID, String response) {

    }

    public void saveContentData(List<ContentData> contentDataList) {
        this.contentDataList.clear();
        this.contentDataList.addAll(contentDataList);
        contentDataDao.deleteContentData();
        for (ContentData contentData : contentDataList) {
            Gson gson = new GsonBuilder().create();
            JsonArray languageDetailsArray = gson.toJsonTree(contentData.getLanguageDetails()).getAsJsonArray();
            contentData.setLanguageDetailsString(languageDetailsArray.toString());
            contentDataDao.insert(contentData);
        }
        updateListView();
    }

    public void logOutUser() {
        // remove user related shared pref data
        Util.saveLoginObjectInPref("");
        try {
            Intent startMain = new Intent(activity, LoginActivity.class);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(startMain);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void showResponse(String responseStatus) {
        Util.snackBarToShowMsg(activity.getWindow().getDecorView()
                        .findViewById(android.R.id.content), responseStatus,
                Snackbar.LENGTH_LONG);
    }

    public void showProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

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

    }
}
