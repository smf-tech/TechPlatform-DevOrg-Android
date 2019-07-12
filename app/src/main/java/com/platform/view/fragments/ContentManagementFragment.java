package com.platform.view.fragments;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.platform.R;

import com.platform.services.DownloadService;
import com.platform.utility.Constants;
import com.platform.utility.Permissions;
import com.platform.view.activities.ContentDownloadedActivity;

import java.io.File;
import java.util.ArrayList;
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
    private TextView txt_noData;
    private File f;
    private String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/MV";
    private long downloadID;
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




    }

    private void initViews() {
        //initToolBar();
        txtTtiel=contentview.findViewById(R.id.txt_contentTitle);
        imgDownload=contentview.findViewById(R.id.img_contentDownload);
        imgDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url="http://18.216.227.14/images/SMF%20BOOK%20-CHANGES.pdf";
                beginDownload(url);
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

        txt_noData=contentview.findViewById(R.id.textNoData);
        expListView=contentview.findViewById(R.id.lvExp);
        prepareListData();


    }

    private void beginDownload(String url) {

        getActivity().registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        DownloadManager downloadmanager = (DownloadManager)getActivity().getSystemService(DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        File file=new File(uri.getPath());
        String filename=file.getName();

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("Mulyavardhan");
        request.setDescription("Downloading");

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        if(Permissions.isCameraPermissionGranted(getActivity(),getActivity())){
            request.setDestinationInExternalPublicDir(path,filename);
        }

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
            long id=intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1);
            if(id==downloadID){
                Toast.makeText(getActivity(),"Download Completed",Toast.LENGTH_LONG).show();
            }

        }
    };
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(onDownloadComplete);
    }
}
