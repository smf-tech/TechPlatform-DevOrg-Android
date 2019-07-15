package com.platform.view.fragments;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Environment;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.platform.R;

import com.platform.adapter.ExpandableListAdapter;
import com.platform.listeners.ContentDataListener;
import com.platform.models.content.ContentDatum;
import com.platform.models.content.ContentModel;
import com.platform.models.content.Datum;
import com.platform.models.content.Datum_;
import com.platform.models.content.DownloadContent;
import com.platform.request.ContentDataRequestCall;
import com.platform.services.DownloadService;
import com.platform.utility.Constants;
import com.platform.utility.Permissions;
import com.platform.utility.Util;
import com.platform.view.activities.ContentDownloadedActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.DOWNLOAD_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContentManagementFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContentManagementFragment#newInstance} factory method to
 * create an instance of this fragment.
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
    private Toolbar toolbar;
    private ImageView backButton;
    private TextView txtTtiel;
    private ImageView imgDownload;
    private ExpandableListView expListView;
    private List<String> listDataHeader = new ArrayList<>();
    private List<DownloadContent>listDownloadContent;
    private HashMap<String,List<DownloadContent>> listDataChild = new HashMap<>();
    private TextView txt_noData;
    private File f;
    private String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/MV";
    private long downloadID;
    private String TAG=ContentManagementFragment.class.getSimpleName();
    private List<Datum> contentList;
    private List<ContentDatum>contentDataList;
    private List<Datum_>contentCategoryList;
    private DownloadContent downloadContent;
    ExpandableListAdapter expandableListAdapter;
    private String downloadFilePath="";
    private String filename;
    private DownloadManager downloadmanager;
    private ImageView imgDwn,imgShare;
    private RelativeLayout progressBarLayout;
    private ProgressBar progressBar;

    public ContentManagementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContentManagementFragment.
     */
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
        getActivity().registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentview= inflater.inflate(R.layout.fragment_content_management, container, false);
        return  contentview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        clearPreviousData();

        if(Util.isConnected(getContext())){
            showProgressBar();
            getContentData();
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
        Log.i(TAG,"Res"+listDataChild);
               expandableListAdapter=new ExpandableListAdapter(ContentManagementFragment.this,listDataHeader,listDataChild,getContext());
                expListView.setAdapter(expandableListAdapter);


        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return false;
            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                Toast.makeText(getContext(),
                        listDataHeader.get(i) + " Expanded",
                        Toast.LENGTH_SHORT).show();

            }
        });

        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub

                DownloadContent dwncontent=listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);

                imgDwn=v.findViewById(R.id.imgDownload);
                imgShare=v.findViewById(R.id.imgshare);

              /*  if(isFileAvailable(dwncontent)){
                    imgDwn.setVisibility(View.GONE);
                    imgShare.setVisibility(View.VISIBLE);
                }else{
                    imgDwn.setVisibility(View.VISIBLE);
                    imgShare.setVisibility(View.GONE);
                }
*/
                imgDwn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                       Intent downloadActivity=new Intent(getActivity(),ContentDownloadedActivity.class);
                       startActivity(downloadActivity);

                    }
                });

                /*Toast.makeText(
                        getContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();

*/
                return false;
            }
        });


    }



    private void getContentData() {

        ContentDataRequestCall contentDataRequestCall=new ContentDataRequestCall();
        contentDataRequestCall.getContentData();
        contentDataRequestCall.setContentDataListener(new ContentDataListener() {
            @Override
            public void onSuccess(String success) {
                Log.i(TAG,"ContentManagemt"+success);
                hideProgressBar();
                ContentModel contentlData=new Gson().fromJson(success,ContentModel.class);
                parseContentJsonResponse(contentlData);

            }

            @Override
            public void onError(String error) {
                Log.i(TAG,"ContentManagemt"+error);
                hideProgressBar();
            }
        });


    }

    private void parseContentJsonResponse(ContentModel contentlData) {
        Log.i(TAG,"ContentResponse"+contentlData);
        String status=contentlData.getStatus();
        if(status.equalsIgnoreCase("200")){
                contentList=contentlData.getData();
                for(int i=0;i<contentList.size();i++){
                    contentDataList=contentList.get(i).getContentData();

                    for(int j=0;j<contentDataList.size();j++){

                        contentCategoryList=contentDataList.get(j).getData();
                        listDataHeader.add(contentDataList.get(j).getTitle());
                        listDownloadContent=new ArrayList<>();

                        for(int k=0;k<contentCategoryList.size();k++){

                            String name=contentCategoryList.get(k).getName();
                            String mr=contentCategoryList.get(k).getUrl().getMr();
                            String hi=contentCategoryList.get(k).getUrl().getHi();
                            String def=contentCategoryList.get(k).getUrl().getDefault();

                            downloadContent=new DownloadContent();
                            downloadContent.setName(name);
                            downloadContent.setMr(mr);
                            downloadContent.setHi(hi);
                            downloadContent.setDef(def);

                            listDownloadContent.add(downloadContent);

                        }
                        listDataChild.put(listDataHeader.get(j),listDownloadContent);


                    }

                }
        }
        updateListView();
        hideProgressBar();
        expandableListAdapter.notifyDataSetChanged();


    }

    private void initViews() {
        //initToolBar();
        txtTtiel=contentview.findViewById(R.id.txt_contentTitle);
        txt_noData=contentview.findViewById(R.id.textNoData);
        expListView=contentview.findViewById(R.id.lvExp);
        progressBarLayout = contentview.findViewById(R.id.profile_act_progress_bar);
        progressBar = contentview.findViewById(R.id.pb_profile_act);

        prepareListData();
        imgDownload=contentview.findViewById(R.id.img_contentDownload);
        imgDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check external storage permission

                if(Permissions.isReadExternalStotagePermission(getActivity(),this)){

                    Intent callDownloadActivity=new Intent(getActivity(),ContentDownloadedActivity.class);
                    startActivity(callDownloadActivity);
                }

                /*String url="http://18.216.227.14/images/SMF%20BOOK%20-CHANGES.pdf";
                beginDownload(url);*/
                /*if(Permissions.isCameraPermissionGranted(getActivity(),getActivity())){

                    //String url="https://drive.google.com/open?id=1D7biC_cf_dupT1l4Ij1hXCGc4lY-v2y2";

                    String fileName = url.substring(url.lastIndexOf('/')+1,url.length());

                    Intent intent=new Intent(getActivity(),DownloadService.class);
                    intent.putExtra("URL",url);
                    intent.putExtra("FILENAME",fileName);
                    intent.putExtra("FILETYPE",".pdf");
                    intent.putExtra("fragment_flag","content fragment");
                    getActivity().startService(intent);

                }else {
                    Toast.makeText(getActivity(), "check permission", Toast.LENGTH_SHORT).show();
                }*/








                /*url = intent.getStringExtra("URL");
                fileName = intent.getStringExtra("FILENAME");
                filetype = intent.getStringExtra("FILETYPE");
                fragment_flag = intent.getStringExtra("fragment_flag");*/


                /*Intent intent=new Intent(getActivity(),ContentDownloadedActivity.class);
                startActivity(intent);*/
            }
        });

    }

    public void beginDownload(String url) {

        DownloadManager downloadmanager = (DownloadManager)getActivity().getSystemService(DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        File file=new File(uri.getPath());
        filename=file.getName();

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("Mulyavardhan");
        request.setDescription("Downloading");

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        if(Permissions.isCameraPermissionGranted(getActivity(),getActivity())){
            request.setDestinationInExternalPublicDir(path,filename);
        }

        downloadFilePath=path+filename;

        downloadID=downloadmanager.enqueue(request);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String uri);
    }

    // broadcast receiver for download a file

    private BroadcastReceiver onDownloadComplete=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent data=intent;
            long id=intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1);
            if(id==downloadID){
                Toast.makeText(getActivity(),"Download Completed",Toast.LENGTH_LONG).show();
                Log.i("Full File path","111"+intent.getDataString());

                String action = intent.getAction();
                String uriString=null;
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {

                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadID);
                    DownloadManager dManager = (DownloadManager)getActivity().getSystemService(DOWNLOAD_SERVICE);
                    Cursor c = dManager.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                            uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                            //TODO : Use this local uri and launch intent to open file
                        }
                    }
                }
                /*Intent shareIntent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("application/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Intent.EXTRA_STREAM,uriString);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                getActivity().startActivity(Intent.createChooser(shareIntent, "Share Content"));*/

                //shareDownloadFile(intent);
                expandableListAdapter.notifyDataSetChanged();



            }

        }
    };

   /* public String shareDownloadFile(Intent intent) {

        return uriString;

    }*/

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



}
