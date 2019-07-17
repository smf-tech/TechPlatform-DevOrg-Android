package com.platform.adapter;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.platform.R;
import com.platform.models.content.DownloadContent;
import com.platform.models.content.Url;
import com.platform.view.fragments.ContentManagementFragment;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<DownloadContent>> _listDataChild;
    private ContentManagementFragment contentManagementFragment;

    public ExpandableListAdapter(ContentManagementFragment context,List<String> listDataHeader,
                                 HashMap<String,List<DownloadContent>> listChildData,Context _context) {
        this.contentManagementFragment = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this._context=_context;

    }



    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final DownloadContent downloadContent = (DownloadContent)getChild(groupPosition,childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) contentManagementFragment.getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        ImageView imgDownload,imgShare;
        TextView txttitle;

        if(convertView!=null){
            imgDownload=convertView.findViewById(R.id.imgDownload);
            imgShare=convertView.findViewById(R.id.imgshare);
            txttitle=convertView.findViewById(R.id.txtName);
            txttitle.setText(downloadContent.getName());


            if(isFileAvailable(downloadContent)){
                imgDownload.setVisibility(View.GONE);
                imgShare.setVisibility(View.VISIBLE);
            }else {
                imgDownload.setVisibility(View.VISIBLE);
                imgShare.setVisibility(View.GONE);
            }


            imgDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // check file is present in directory or not
                    contentManagementFragment.beginDownload(downloadContent.getDef());

                }
            });

            imgShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String storagePath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/MV/";
                    Uri uri = Uri.parse(downloadContent.getDef());
                    File file=new File(uri.getPath());
                    String fileName=file.getName();
                    String filePath=storagePath+fileName;

                    File pptFile = new File(filePath);
                    Uri outputUri = FileProvider.getUriForFile(contentManagementFragment.getActivity(),
                            contentManagementFragment.getActivity().getPackageName() + ".file_provider", pptFile);
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("application/*");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(Intent.EXTRA_STREAM,outputUri);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    contentManagementFragment.getActivity().startActivity(Intent.createChooser(intent, "Share Content"));
                }
            });
        }
        return convertView;
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) contentManagementFragment.getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
        if (convertView != null) {
            ImageView imgGroup = convertView.findViewById(R.id.imgGroup);
            if (isExpanded) {
                imgGroup.setImageResource(R.drawable.downarrow);
            } else {
                imgGroup.setImageResource(R.drawable.rightarrow);
            }

            TextView txtName = convertView.findViewById(R.id.txtName);
            txtName.setText(headerTitle);
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private boolean isFileAvailable(DownloadContent downloadContent) {


        //File myFile = new File(extStore.getAbsolutePath() +"/MV");
        //Uri uri = Uri.parse(myFile.toString());
        //File file=new File(uri.getPath());
        //String filename=file.getName();

        String storagePath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/MV/";
        Uri uri = Uri.parse(downloadContent.getDef());
        File file=new File(uri.getPath());
        String fileName=file.getName();

        File myFile=new File(storagePath+fileName);


        Log.i("FilePath","111"+myFile);
        if (myFile.exists()) {
            return true;
        }else {
            return  false;
        }

    }



}