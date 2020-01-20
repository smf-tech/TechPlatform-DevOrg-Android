package com.octopusbjsindia.view.fragments;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.octopusbjsindia.R;
import com.octopusbjsindia.adapter.ExpandableListAdapter;
import com.octopusbjsindia.listeners.ContentDataListener;
import com.octopusbjsindia.models.content.ContentDatum;
import com.octopusbjsindia.models.content.ContentModel;
import com.octopusbjsindia.models.content.Datum;
import com.octopusbjsindia.models.content.Datum_;
import com.octopusbjsindia.models.content.DownloadContent;
import com.octopusbjsindia.models.content.DownloadInfo;
import com.octopusbjsindia.request.ContentDataRequestCall;
import com.octopusbjsindia.services.ShowTimerService;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.ContentDownloadedActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.DOWNLOAD_SERVICE;

/*
import com.platform.adapter.ExpandableListAdapter;
import com.platform.listeners.ContentDataListener;
import com.platform.models.content.ContentDatum;
import com.platform.models.content.ContentModel;
import com.platform.models.content.Datum;
import com.platform.models.content.Datum_;
import com.platform.models.content.DownloadContent;
import com.platform.request.ContentDataRequestCall;
*/

public class ContentManagementFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View contentview;
    private ImageView backButton;
    private TextView txtTtiel;
    private ImageView imgDownload;
    private FloatingActionButton btn_floating_download;
    private ExpandableListView expListView;
    private List<String> listDataHeader = new ArrayList<>();
    private List<DownloadContent> listDownloadContent;
    private HashMap<String, List<DownloadContent>> listDataChild = new HashMap<>();
    private TextView txt_noData;
    private File f;
    //private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MV";
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MV";
    private long downloadID;
    private String TAG = ContentManagementFragment.class.getSimpleName();
    private List<Datum> contentList;
    private List<ContentDatum> contentDataList;
    private List<Datum_> contentCategoryList;
    private DownloadContent downloadContent;
    ExpandableListAdapter expandableListAdapter;
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


    public ContentManagementFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ContentManagementFragment newInstance(String param1, String param2) {
        ContentManagementFragment fragment = new ContentManagementFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        getActivity().registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentview = inflater.inflate(R.layout.fragment_content_management, container, false);
        return contentview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        createDirectory();
        clearPreviousData();

        if (Util.isConnected(getContext())) {
            showProgressBar();
            getContentData();
        }

    }

    private void createDirectory() {
        File direct = new File(path);

        if (!direct.exists()) {
            direct.mkdirs();
        }
    }

    private void clearPreviousData() {
        listDataHeader.clear();
        listDataChild.clear();
    }

    private void updateListView() {
        if (listDataHeader.size() > 0) {
            txt_noData.setVisibility(View.GONE);
        } else {
            txt_noData.setVisibility(View.VISIBLE);
        }
        Log.i(TAG, "Res" + listDataChild);

        expandableListAdapter = new ExpandableListAdapter(ContentManagementFragment.this, listDataHeader, listDataChild, getContext());
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
                DownloadContent dwncontent = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                return false;
            }
        });
    }


    private void getContentData() {

        ContentDataRequestCall contentDataRequestCall = new ContentDataRequestCall();
        contentDataRequestCall.getContentData();
        contentDataRequestCall.setContentDataListener(new ContentDataListener() {
            @Override
            public void onSuccess(String success) {
                Log.i(TAG, "ContentManagemt" + success);
                hideProgressBar();
                ContentModel contentlData = new Gson().fromJson(success, ContentModel.class);
                parseContentJsonResponse(contentlData);

            }

            @Override
            public void onError(String error) {
                Log.i(TAG, "ContentManagemt" + error);
                hideProgressBar();
            }
        });


    }

    private void parseContentJsonResponse(ContentModel contentlData) {
        Log.i(TAG, "ContentResponse" + contentlData);
        String status = contentlData.getStatus();
        if (status.equalsIgnoreCase("200")) {
            contentList = contentlData.getData();
            for (int i = 0; i < contentList.size(); i++) {

                contentDataList = contentList.get(i).getContentData();

                for (int j = 0; j < contentDataList.size(); j++) {

                    contentCategoryList = contentDataList.get(j).getData();
                    listDataHeader.add(contentDataList.get(j).getTitle());
                    listDownloadContent = new ArrayList<>();

                    for (int k = 0; k < contentCategoryList.size(); k++) {

                        String name = contentCategoryList.get(k).getName();
                        String mr = contentCategoryList.get(k).getUrl().getMr();
                        String hi = contentCategoryList.get(k).getUrl().getHi();
                        String def = contentCategoryList.get(k).getUrl().getDefault();

                        downloadContent = new DownloadContent();
                        downloadContent.setName(name);
                        downloadContent.setMr(mr);
                        downloadContent.setHi(hi);
                        downloadContent.setDef(def);

                        downloadInfo = new DownloadInfo(name, 1000);
                        downloadContent.setInfo(downloadInfo);

                        listDownloadContent.add(downloadContent);

                    }
                    listDataChild.put(listDataHeader.get(j), listDownloadContent);


                }

            }
        }
        updateListView();
        hideProgressBar();
        expandableListAdapter.notifyDataSetChanged();


    }

    private void initViews() {
        //initToolBar();
        txtTtiel = contentview.findViewById(R.id.txt_contentTitle);
        txt_noData = contentview.findViewById(R.id.textNoData);
        btn_floating_download = contentview.findViewById(R.id.btn_floating_content);

        expListView = contentview.findViewById(R.id.lvExp);
        progressBarLayout = contentview.findViewById(R.id.profile_act_progress_bar);
        progressBar = contentview.findViewById(R.id.pb_profile_act);


        prepareListData();
        imgDownload = contentview.findViewById(R.id.img_contentDownload);
        imgDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_floating_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Permissions.isReadExternalStotagePermission(getActivity(), this)) {

                    Intent callDownloadActivity = new Intent(getActivity(), ContentDownloadedActivity.class);
                    startActivity(callDownloadActivity);
                }

            }
        });

    }

    public boolean beginDownload(String url) {
        boolean isRunning = false;
        downloadmanager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        File file = new File(uri.getPath());
        filename = file.getName();

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("Mulyavardhan");
        request.setDescription("Downloading");
        //request.setDestinationInExternalPublicDir("/MV",filename);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        if (Permissions.isCameraPermissionGranted(getActivity(), getActivity())) {
            request.setDestinationInExternalPublicDir("/MV", filename);
        }

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


    private void prepareListData() {

        // fill header and child list

        if (listDataHeader.size() > 0) {
            txt_noData.setVisibility(View.GONE);
        } else {
            txt_noData.setVisibility(View.VISIBLE);
        }
    }

    private void initToolBar() {

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String uri);
    }

    // broadcast receiver for download a file

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent data = intent;

            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                Toast.makeText(getContext(), "DownloadComplete", Toast.LENGTH_LONG).show();
                //mProgressBar.setVisibility(View.GONE);
                expandableListAdapter.notifyDataSetChanged();
            }

        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(onDownloadComplete);
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
    public void onResume() {
        super.onResume();
        if (expandableListAdapter != null) {
            expandableListAdapter.notifyDataSetChanged();
        }
    }


}
