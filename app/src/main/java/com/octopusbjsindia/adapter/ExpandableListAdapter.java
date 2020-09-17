package com.octopusbjsindia.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.content.ContentData;
import com.octopusbjsindia.models.content.LanguageDetail;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.fragments.ContentManagementFragment;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<ContentData>> _listDataChild;
    public ContentManagementFragment contentManagementFragment;
    private ArrayList<LanguageDetail> languageDetailsList = new ArrayList<>();
    Gson gson = new Gson();
    Type type = new TypeToken<List<LanguageDetail>>() {
    }.getType();

    public ExpandableListAdapter(ContentManagementFragment context, List<String> listDataHeader,
                                 HashMap<String, List<ContentData>> listChildData, Context _context) {
        this.contentManagementFragment = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.context = _context;
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

        final ContentData contentData = (ContentData) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) contentManagementFragment.getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }
        languageDetailsList.clear();
        languageDetailsList = gson.fromJson(contentData.getLanguageDetailsString(), type);

        RelativeLayout rlContent = convertView.findViewById(R.id.rl_content);
        ImageView imgDownload = convertView.findViewById(R.id.img_download);
        ImageView imgShare = convertView.findViewById(R.id.img_share);
        ImageView imgView = convertView.findViewById(R.id.img_view);
        ProgressBar pbDownloading = convertView.findViewById(R.id.pbDownloading);
        TextView txtTitle = convertView.findViewById(R.id.txt_name);
        TextView txtSize = convertView.findViewById(R.id.txt_size);
        TextView txtType = convertView.findViewById(R.id.txt_type);

        txtTitle.setText(contentData.getContentTiltle());
        txtSize.setText(contentData.getFileSize());
        txtType.setText(contentData.getFileType());


        boolean isFileDownloaded = false;
        for (LanguageDetail languageDetail : languageDetailsList) {
            Uri uri = Uri.parse(languageDetail.getDownloadUrl());
            File file = new File(uri.getPath());
            String fileName = file.getName();
            if (isFileAvailable(fileName)) {
                isFileDownloaded = true;
                contentData.setDownloadedFileName(fileName);
                break;
            }
        }

        if(contentData.getFileType().equalsIgnoreCase("youtube")){
            imgView.setVisibility(View.VISIBLE);
            imgShare.setVisibility(View.GONE);
            imgDownload.setVisibility(View.GONE);
            pbDownloading.setVisibility(View.GONE);
        } else if (isFileDownloaded && !contentData.isDawnloadSatrted()) {
            imgView.setVisibility(View.VISIBLE);
            imgShare.setVisibility(View.VISIBLE);
            imgDownload.setVisibility(View.GONE);
            pbDownloading.setVisibility(View.GONE);
        } else {
            if(contentData.isDawnloadSatrted()){
                pbDownloading.setVisibility(View.VISIBLE);
                imgDownload.setVisibility(View.GONE);
                imgShare.setVisibility(View.GONE);
                imgView.setVisibility(View.GONE);
            } else {
                imgDownload.setVisibility(View.VISIBLE);
                imgShare.setVisibility(View.GONE);
                imgView.setVisibility(View.GONE);
                pbDownloading.setVisibility(View.GONE);
            }
        }

        rlContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contentData.getFileType().equalsIgnoreCase("youtube")){
                    context.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(languageDetailsList.get(0).getDownloadUrl())));
                } else if (contentData.getDownloadedFileName() != null && contentData.getDownloadedFileName() != "") {
                    String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                            + Environment.DIRECTORY_DOWNLOADS;
                    File contentFile = new File(storagePath + "/" + contentData.getDownloadedFileName());
                    openFile(contentFile);
                } else {
                    Util.snackBarToShowMsg(contentManagementFragment.getActivity().getWindow().getDecorView().
                            findViewById(android.R.id.content), "Please download file and then view.", Snackbar.LENGTH_LONG);
                }
            }
        });

        imgDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Permissions.isWriteExternalStoragePermission(contentManagementFragment.getActivity(), contentManagementFragment)) {
//                    for (LanguageDetail languageDetail : languageDetailsList) {
//                        if (languageDetail.getLanguageId().equalsIgnoreCase(Util.getLocaleLanguageCode())) {
                    contentManagementFragment.showDownloadPopup(languageDetailsList, groupPosition, childPosition);
                    //contentManagementFragment.beginDownload(languageDetail.getDownloadUrl());
                    //break;
                    //}
                    //}
                }
            }
        });

        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                        + Environment.DIRECTORY_DOWNLOADS;
                File filePath = new File(storagePath + "/" + contentData.getDownloadedFileName());
                shareFile(filePath);
            }
        });
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

        View view = LayoutInflater.from(context).inflate(R.layout.list_group,
                parent, false);

        String headerTitle = (String) getGroup(groupPosition);
        ((TextView) view.findViewById(R.id.txtName)).setText(headerTitle);

        ImageView v = view.findViewById(R.id.imgGroup);
        if (isExpanded) {
            Util.rotateImage(180f, v);
        } else {
            Util.rotateImage(0f, v);
        }

        return view;

//        String headerTitle = (String) getGroup(groupPosition);
//        if (convertView == null) {
//            LayoutInflater infalInflater = (LayoutInflater) contentManagementFragment.getActivity()
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = infalInflater.inflate(R.layout.list_group, null);
//        }
//        if (convertView != null) {
//            ImageView imgGroup = convertView.findViewById(R.id.imgGroup);
//            if (isExpanded) {
//                Util.rotateImage(180f, imgGroup);
//                //imgGroup.setImageResource(R.drawable.ic_shape_down_arrow);
//            } else {
//                Util.rotateImage(0f, imgGroup);
//                //imgGroup.setImageResource(R.drawable.ic_right_arrow_grey);
//            }
//            TextView txtName = convertView.findViewById(R.id.txtName);
//            txtName.setText(headerTitle);
//        }
//        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private boolean isFileAvailable(String fileName) {
        String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                + Environment.DIRECTORY_DOWNLOADS;
        File myFile = new File(storagePath + "/" + fileName);
        if (myFile.exists()) {
            return true;
        } else {
            return false;
        }
    }

    private void openFile(File contentFile) {
        try {
            // Create URI
            Uri uri = FileProvider.getUriForFile(contentManagementFragment.getActivity(),
                    contentManagementFragment.getActivity().getPackageName() + ".file_provider", contentFile);
            contentManagementFragment.getActivity().grantUriPermission
                    (contentManagementFragment.getActivity().getPackageName(), uri, FLAG_GRANT_READ_URI_PERMISSION);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setData(uri);
            intent.setType(get_mime_type(uri.toString()));
//             Check what kind of file you are trying to open, by comparing the url with extensions.
//             When the if condition is matched, plugin sets the correct intent (mime) type,
//             so Android knew what application to use to open the file
           /* if (contentFile.toString().contains(".doc") || contentFile.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else */
           if (contentFile.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, get_mime_type(uri.toString()));
            }/* else if (contentFile.toString().contains(".ppt") || contentFile.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if (contentFile.toString().contains(".xls") || contentFile.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if (contentFile.toString().contains(".zip") || contentFile.toString().contains(".rar")) {
                // WAV audio file
                intent.setDataAndType(uri, "application/x-wav");
            } else if (contentFile.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf");
            }*/ else if (contentFile.toString().contains(".wav") || contentFile.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav");
            } else if (contentFile.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif");
            }/* else if (contentFile.toString().contains(".jpg") || contentFile.toString().contains(".jpeg") ||
                    contentFile.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if (contentFile.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            }*/ else if (contentFile.toString().contains(".3gp") || contentFile.toString().contains(".mpg") ||
                    contentFile.toString().contains(".mpeg") || contentFile.toString().contains(".mpe") ||
                    contentFile.toString().contains(".mp4") || contentFile.toString().contains(".avi")) {
                // Video files
                intent.setDataAndType(uri, "video/*");
            }  else if (contentFile.toString().contains(".epub")) {
               String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                       + "/MV/Zip/" + contentFile.getName() + ".epub";
//               File epubFile = new File(filePath);
//               Uri outputUri = FileProvider.getUriForFile(this,
//                       this.getPackageName() + ".fileprovider", epubFile);

               Intent intentEpub = new Intent();
               intentEpub.setAction(Intent.ACTION_VIEW);
               intentEpub.setDataAndType(uri, "application/epub+zip");
               intentEpub.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               intentEpub.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

               PackageManager packageManager = context.getPackageManager();
               if (intent.resolveActivity(packageManager) != null) {
                   context.startActivity(intent);
               } else {
                   showDialog();
               }
           }else {
                //if you want you can also define the intent type for any other file
                //additionally use else clause below, to manage other unknown extensions
                //in this case, Android will show all applications installed on the device
                //so you can choose which application to use
                intent.setDataAndType(uri, "*/*");
            }
            contentManagementFragment.getActivity().startActivity(intent);

        } catch (Exception e) {
            Log.i("FileView", "222" + e.toString());
        }
    }

    public String get_mime_type(String url) {
        String ext = MimeTypeMap.getFileExtensionFromUrl(url);
        String mime = null;
        if (ext != null) {
            mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
        }
        return mime;
    }

    public void showDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(context.getResources().getString(R.string.alert));
        alertDialog.setMessage("No Application available to open Ebook file, Do you want to install?");
        alertDialog.setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                final String appPackageName = context.getPackageName();
                try {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.faultexception.reader&hl=en" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.faultexception.reader&hl=en" + appPackageName)));
                }
            }
        });
        alertDialog.setNegativeButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void shareFile(File contentFile) {
        // Create URI
        Uri uri = FileProvider.getUriForFile(contentManagementFragment.getActivity(),
                contentManagementFragment.getActivity().getPackageName() + ".file_provider", contentFile);
        contentManagementFragment.getActivity().grantUriPermission
                (contentManagementFragment.getActivity().getPackageName(), uri, FLAG_GRANT_READ_URI_PERMISSION);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("application/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        contentManagementFragment.getActivity().startActivity(Intent.createChooser(intent, "Share Content"));
    }
}