package com.octopusbjsindia.view.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.adapter.DownloadListAdapter;
import com.octopusbjsindia.dao.ContentDataDao;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.models.content.ContentData;
import com.octopusbjsindia.models.content.LanguageDetail;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.StoredContentListAdapter;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class StoredContentActivity extends AppCompatActivity implements StoredContentListAdapter.OnListTitleClick {

    RecyclerView recyclerView;
    Context context;
    private String path= Environment.getExternalStorageDirectory().getAbsolutePath() + "/MV";
    private StoredContentListAdapter downloadListAdapter;
    private ArrayList<ContentData> contentDataList = new ArrayList<>();
    private ArrayList<ContentData> contentDownloadedList = new ArrayList<>();
    ContentDataDao contentDataDao;
    private ArrayList<LanguageDetail> languageDetailsList = new ArrayList<>();
    Gson gson = new Gson();
    Type type = new TypeToken<List<LanguageDetail>>() {
    }.getType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_downloaded);
        context= StoredContentActivity.this;
        contentDataDao = DatabaseManager.getDBInstance(Platform.getInstance()).getContentDataDao();
        initViews();
        contentDataList.addAll(contentDataDao.getContentData());
        getAllDownlodedfiles();
        setUpRecycleView();


    }

    private void getAllDownlodedfiles() {
        //for (int i = 0; i <contentDataList.size(); i++) {
        for (ContentData contentData : contentDataList) {
            languageDetailsList.clear();
            languageDetailsList = gson.fromJson(contentData.getLanguageDetailsString(), type);
            for (LanguageDetail languageDetail : languageDetailsList) {
                Uri uri = Uri.parse(languageDetail.getDownloadUrl());
                File file = new File(uri.getPath());
                String fileName = file.getName();
                if (isFileAvailable(fileName)) {
                    contentDownloadedList.add(contentData);
                    contentData.setDownloadedFileName(fileName);
                    break;
                }
            }
        }
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
    private void setUpRecycleView() {

        if(contentDownloadedList!=null&&contentDownloadedList.size()>0){
            downloadListAdapter=new StoredContentListAdapter(context,contentDownloadedList,this::onListTitleClick);
            recyclerView.setAdapter(downloadListAdapter);
        }

    }

    private void initViews() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Content Management");

        recyclerView=findViewById(R.id.list_download);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
    }


    @Override
    public void onListTitleClick(int pos) {
        if (contentDownloadedList.get(pos).getDownloadedFileName() != null && contentDownloadedList.get(pos).getDownloadedFileName() != "") {
            String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                    + Environment.DIRECTORY_DOWNLOADS;
            File contentFile = new File(storagePath + "/" + contentDownloadedList.get(pos).getDownloadedFileName());
            openFile(contentFile);
        } else {
            Util.snackBarToShowMsg(getWindow().getDecorView().
                    findViewById(android.R.id.content), "Please download file and then view.", Snackbar.LENGTH_LONG);
        }
    }

    private void openFile(File contentFile) {
        try {
            // Create URI
            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".file_provider", contentFile);

            grantUriPermission(getPackageName(), uri, FLAG_GRANT_READ_URI_PERMISSION);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setData(uri);
            intent.setType(get_mime_type(uri.toString()));
//             Check what kind of file you are trying to open, by comparing the url with extensions.
//             When the if condition is matched, plugin sets the correct intent (mime) type,
//             so Android knew what application to use to open the file
            if (contentFile.toString().contains(".doc") || contentFile.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if (contentFile.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, get_mime_type(uri.toString()));
            } else if (contentFile.toString().contains(".ppt") || contentFile.toString().contains(".pptx")) {
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
            } else if (contentFile.toString().contains(".wav") || contentFile.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav");
            } else if (contentFile.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif");
            } else if (contentFile.toString().contains(".jpg") || contentFile.toString().contains(".jpeg") ||
                    contentFile.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if (contentFile.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            } else if (contentFile.toString().contains(".3gp") || contentFile.toString().contains(".mpg") ||
                    contentFile.toString().contains(".mpeg") || contentFile.toString().contains(".mpe") ||
                    contentFile.toString().contains(".mp4") || contentFile.toString().contains(".avi")) {
                // Video files
                intent.setDataAndType(uri, "video/*");
            } else {
                //if you want you can also define the intent type for any other file
                //additionally use else clause below, to manage other unknown extensions
                //in this case, Android will show all applications installed on the device
                //so you can choose which application to use
                intent.setDataAndType(uri, "*/*");
            }
            startActivity(intent);

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
}
