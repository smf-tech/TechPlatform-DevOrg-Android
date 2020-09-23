package com.octopusbjsindia.adapter;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
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
import java.util.Objects;

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
                    if (Util.isConnected(context)) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse(languageDetailsList.get(0).getDownloadUrl())));
                    } else {
                        Util.showToast(context.getString(R.string.msg_no_network), context);
                    }
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

//               Intent intentEpub = new Intent();
//               intentEpub.setAction(Intent.ACTION_VIEW);
//               intentEpub.setDataAndType(uri, "application/epub+zip");
//               intentEpub.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//               intentEpub.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

               PackageManager packageManager = context.getPackageManager();
               if (intent.resolveActivity(packageManager) != null) {
                   intent.setDataAndType(uri, "application/epub+zip");
                   //context.startActivity(intent);
               } else {
                   showDialog("Alert", "No Application available to open Ebook " +
                           "file, Do you want to install?", "Yes", "No");
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

    public void showDialog(String dialogTitle, String message, String btn1String, String btn2String) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(context));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogs_leave_layout);

        if (!TextUtils.isEmpty(dialogTitle)) {
            TextView title = dialog.findViewById(R.id.tv_dialog_title);
            title.setText(dialogTitle);
            title.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(message)) {
            TextView text = dialog.findViewById(R.id.tv_dialog_subtext);
            text.setText(message);
            text.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(btn1String)) {
            Button button = dialog.findViewById(R.id.btn_dialog);
            button.setText(btn1String);
            button.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            button.setTextColor(context.getResources().getColor(R.color.white));
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(v -> {
                final String appPackageName = context.getPackageName();
                try {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.faultexception.reader&hl=en" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.faultexception.reader&hl=en" + appPackageName)));
                }
                //Close dialog
                dialog.dismiss();
            });
        }

        if (!TextUtils.isEmpty(btn2String)) {
            Button button1 = dialog.findViewById(R.id.btn_dialog_1);
            button1.setText(btn2String);
            button1.setVisibility(View.VISIBLE);
            button1.setOnClickListener(v -> {
                //Close dialog
                dialog.dismiss();
            });
        }

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
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