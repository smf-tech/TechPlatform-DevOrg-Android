package com.octopusbjsindia.view.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.octopusbjsindia.models.content.DownloadLanguageSelection;
import com.octopusbjsindia.models.content.LanguageDetail;
import com.octopusbjsindia.presenter.ContentFragmentPresenter;
import com.octopusbjsindia.utility.AppEvents;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.HomeActivity;
import com.octopusbjsindia.view.activities.LoginActivity;
import com.octopusbjsindia.view.activities.StoredContentActivity;
import com.octopusbjsindia.view.adapters.ContentLanguageSelectionAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.DOWNLOAD_SERVICE;
import static com.octopusbjsindia.utility.Util.getUserObjectFromPref;

public class ContentManagementFragment extends Fragment implements APIDataListener, View.OnClickListener {

    private View contentFragmentview;
    private FloatingActionButton fbSelect;
    private ExpandableListView expListView;
    private List<String> listDataHeader = new ArrayList<>();
    private HashMap<String, List<ContentData>> listDataChild = new HashMap<>();
    private long downloadID;
    private String TAG = ContentManagementFragment.class.getSimpleName();
    private ExpandableListAdapter expandableListAdapter;
    private String filename;
    private RelativeLayout progressBarLayout;
    private ProgressBar progressBar;
    private List<Long> downloadIdList = new ArrayList<>();
    DownloadManager downloadmanager;
    private Activity activity;
    private ContentFragmentPresenter presenter;
    private ArrayList<ContentData> contentDataList = new ArrayList<>();
    ContentDataDao contentDataDao;
    private int downloadPosition = -1;
    private int groupPosition = -1, childPosition = -1;
    private int lastExpandedPosition = -1;

    public void setDownloadPosition(int downloadPosition) {
        this.downloadPosition = downloadPosition;
    }

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

        initViews();
    }

    private void initViews() {
        progressBarLayout = contentFragmentview.findViewById(R.id.profile_act_progress_bar);
        progressBar = contentFragmentview.findViewById(R.id.pb_profile_act);
        //txt_noData = contentFragmentview.findViewById(R.id.textNoData);
        fbSelect = contentFragmentview.findViewById(R.id.fb_select);
        fbSelect.setOnClickListener(this);
        contentDataDao = DatabaseManager.getDBInstance(Platform.getInstance()).getContentDataDao();
        expListView = contentFragmentview.findViewById(R.id.lvExp);
        expandableListAdapter = new ExpandableListAdapter(ContentManagementFragment.this,
                listDataHeader, listDataChild, getContext());
        expListView.setAdapter(expandableListAdapter);

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                if (lastExpandedPosition != -1
                        && i != lastExpandedPosition) {
                    expListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = i;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.isConnected(getContext())) {
            presenter = new ContentFragmentPresenter(this);
            presenter.getContentData();
        } else {
            updateListView();
            Util.showToast(getString(R.string.msg_no_network), this);
        }
        if (expandableListAdapter != null) {
            expandableListAdapter.notifyDataSetChanged();
        }
        getActivity().registerReceiver(onDownloadComplete, new IntentFilter
                (DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(onDownloadComplete);
    }

    private void updateListView() {
        listDataHeader.clear();
        listDataChild.clear();
        List<String> categoryNames = contentDataDao.getDistinctCategories(getUserObjectFromPref().getProjectIds().get(0).getId());
        listDataHeader.addAll(categoryNames);
        for (String categoryName : listDataHeader) {
            listDataChild.put(categoryName, contentDataDao.getCategoryContent(
                    categoryName, getUserObjectFromPref().getProjectIds().get(0).getId()));
        }
        expandableListAdapter.notifyDataSetChanged();
        if (listDataHeader.size() > 0) {
            contentFragmentview.findViewById(R.id.ly_no_data).setVisibility(View.GONE);
        } else {
            contentFragmentview.findViewById(R.id.ly_no_data).setVisibility(View.VISIBLE);
        }
    }

    public void showDownloadPopup(ArrayList<LanguageDetail> languageDetailsList, int groupPosition,
                                  int childPosition, String contentId) {
        ArrayList<DownloadLanguageSelection> list = new ArrayList<>();
        for (LanguageDetail languageDetail : languageDetailsList) {
            DownloadLanguageSelection downloadLanguageSelection = new DownloadLanguageSelection();
            downloadLanguageSelection.setLanguage(languageDetail.getLanguage());
            list.add(downloadLanguageSelection);
        }
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        final View customLayout = getLayoutInflater().inflate(R.layout.content_language_selection_layout, null);
        alertDialog.setView(customLayout);
        alertDialog.setCancelable(false);

        TextView title = customLayout.findViewById(R.id.tv_dialog_title);
        title.setText(getActivity().getString(R.string.title_select_content_language));

        TextView subtitle = customLayout.findViewById(R.id.tv_dialog_subtext);
        subtitle.setText(getActivity().getString(R.string.msg_content_download_languages));

        ContentLanguageSelectionAdapter contentLanguageSelectionAdapter = new
                ContentLanguageSelectionAdapter(this, list);
        RecyclerView rvLanguageSelection = customLayout.findViewById(R.id.rv_language_selection);
        rvLanguageSelection.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvLanguageSelection.setAdapter(contentLanguageSelectionAdapter);

        AlertDialog dialog = alertDialog.create();

        Button button = customLayout.findViewById(R.id.btn_dialog);
        button.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
        button.setTextColor(getActivity().getResources().getColor(R.color.white));
        button.setOnClickListener(v -> {
            if (downloadPosition > -1) {
                if (Util.isConnected(getContext())) {
                    beginDownload(languageDetailsList.get(downloadPosition).getDownloadUrl(),
                            groupPosition, childPosition, contentId);
                } else {
                    Util.showToast(getString(R.string.msg_no_network), this);
                }
                downloadPosition = -1;
                dialog.dismiss();
            } else {
                Util.showToast(getActivity(), "Please select language.");
            }
        });

        Button button1 = customLayout.findViewById(R.id.btn_dialog_1);
        button1.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }


    public void beginDownload(String url, int groupCount, int childCount, String contentId) {
        groupPosition = groupCount;
        childPosition = childCount;
        listDataChild.get(listDataHeader.get(groupCount)).get(childCount).setDawnloadSatrted(true);
        downloadmanager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        File file = new File(uri.getPath());
        filename = file.getName();

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("Octopus");
        request.setDescription("Downloading");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        downloadID = downloadmanager.enqueue(request);
        downloadIdList.add(downloadID);

        Cursor cursor = downloadmanager.query(new DownloadManager.Query().setFilterById(downloadID));

        if (cursor != null && cursor.moveToNext()) {
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            cursor.close();

            if (status == DownloadManager.STATUS_FAILED) {
                // do something when failed
                listDataChild.get(listDataHeader.get(groupCount)).get(childCount).setDawnloadSatrted(false);
            } else if (status == DownloadManager.STATUS_PENDING || status == DownloadManager.STATUS_PAUSED) {
                // do something pending or paused
                listDataChild.get(listDataHeader.get(groupCount)).get(childCount).setDawnloadSatrted(true);
            } else if (status == DownloadManager.STATUS_SUCCESSFUL) {
                // do something when successful
            } else if (status == DownloadManager.STATUS_RUNNING) {
                // do something when running
                listDataChild.get(listDataHeader.get(groupCount)).get(childCount).setDawnloadSatrted(true);
            }
        }
        expandableListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    // broadcast receiver for download a file
    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent data = intent;
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "Download completed.", Toast.LENGTH_LONG).show();
                }
                // call api to update backend about downloaded file with content_id for this user.
                if(context instanceof HomeActivity) {
                    presenter.sendDownloadedContentDetails(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));
                }
                if (groupPosition != -1 && childPosition != -1) {
                    listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setDawnloadSatrted(false);
                }
                expandableListAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        showEmptyResponse(getResources().getString(R.string.msg_something_went_wrong));
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        showEmptyResponse(getResources().getString(R.string.msg_something_went_wrong));
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
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

    public void showEmptyResponse(String responseStatus) {
        listDataHeader.clear();
        listDataChild.clear();
        expandableListAdapter.notifyDataSetChanged();
        contentFragmentview.findViewById(R.id.ly_no_data).setVisibility(View.VISIBLE);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fb_select) {
                Intent storedContentIntent = new Intent(getActivity(), StoredContentActivity.class);
                startActivity(storedContentIntent);
        }
    }
}
