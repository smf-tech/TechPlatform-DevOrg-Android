package com.octopusbjsindia.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.content.ContentData;
import com.octopusbjsindia.models.content.DownloadContent;
import com.octopusbjsindia.models.content.DownloadInfo;
import com.octopusbjsindia.models.content.LanguageDetail;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.fragments.ContentManagementFragment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<ContentData>> _listDataChild;
    public ContentManagementFragment contentManagementFragment;
    Handler handler = new Handler();
    View convertView;
    ProgressBar progressBar;
    private ArrayList<String> urlListl = new ArrayList<>();
    private String[] urlArray;
    private int position;
    private TextView txt_percentage;
    boolean isDownloading;

    public ExpandableListAdapter(ContentManagementFragment context, List<String> listDataHeader,
                                 HashMap<String, List<ContentData>> listChildData, Context _context) {
        this.contentManagementFragment = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this._context = _context;

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

        position = childPosition;
        urlListl = new ArrayList<>();

        final ContentData contentData = (ContentData) getChild(groupPosition, childPosition);
        //final DownloadInfo info = downloadContent.getInfo();

        //ViewHolder holder;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) contentManagementFragment.getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
//             holder=new ViewHolder();
//             holder.info=info;
//             convertView.setTag(holder);
        }
//        else{
//            holder= (ViewHolder)convertView.getTag();
//            holder.info.setProgressBar(null);
//            holder.info = info;
//            holder.info.setProgressBar(holder.progressBar);
//        }
//        Log.i("Info","111"+info);

        ImageView imgDownload = convertView.findViewById(R.id.img_download);
        ImageView imgShare = convertView.findViewById(R.id.img_share);
        TextView txttitle = convertView.findViewById(R.id.txt_name);
        TextView txtSize = convertView.findViewById(R.id.txt_size);
        TextView txtType = convertView.findViewById(R.id.txt_type);
        //TextView txtpercentage = convertView.findViewById(R.id.txt_count);
        progressBar = convertView.findViewById(R.id.progress_bar);

        txttitle.setText(contentData.getContentTiltle());
        txtSize.setText(contentData.getFileSize());
        txtType.setText(contentData.getFileType());
//        progressBar.setProgress(info.getProgress());
//        progressBar.setMax(100);
//        info.setProgressBar(progressBar);

//        if (isFileAvailable(downloadContent)) {
//            imgDownload.setVisibility(View.GONE);
//            imgShare.setVisibility(View.VISIBLE);
//        } else {
//            imgDownload.setVisibility(View.VISIBLE);
//            imgShare.setVisibility(View.GONE);
//        }

        imgDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //urlListl.add(downloadContent.getDef());
                Gson gson = new Gson();
                Type type = new TypeToken<List<LanguageDetail>>() {
                }.getType();
                List<LanguageDetail> languageDetailsList = gson.fromJson(contentData.getLanguageDetailsString(), type);
                for (LanguageDetail languageDetail : languageDetailsList) {
                    if (languageDetail.getLanguageId().equalsIgnoreCase(Util.getLocaleLanguageCode())) {
                        contentManagementFragment.beginDownload(languageDetail.getDownloadUrl());
                    }
                }

                //contentManagementFragment.beginDownload(contentData.get);
                DownloadImageTask downloadImageTask = new DownloadImageTask();
                downloadImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Octopus";
//                Uri uri = Uri.parse(downloadContent.getDef());
//                File file = new File(uri.getPath());
//                String fileName = file.getName();
//                File filePath = new File(storagePath + "/" + fileName);
//                openFile(contentManagementFragment, filePath);
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
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) contentManagementFragment.getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
        if (convertView != null) {
            ImageView imgGroup = convertView.findViewById(R.id.imgGroup);
            if (isExpanded) {
                imgGroup.setImageResource(R.drawable.ic_shape_down_arrow);
            } else {
                imgGroup.setImageResource(R.drawable.ic_arrow_right);
            }
            TextView txtName = convertView.findViewById(R.id.txtName);
            txtName.setText(headerTitle);
        }
        return convertView;
    }

//    private static class ViewHolder {
//        ImageView imgDownload, imgShare;
//        TextView txttitle, txtpercentage;
//        ProgressBar progressBar = null;
//    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private boolean isFileAvailable(DownloadContent downloadContent) {
        String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Octopus";
        Uri uri = Uri.parse(downloadContent.getDef());
        File file = new File(uri.getPath());
        String fileName = file.getName();

        File myFile = new File(storagePath + "/" + fileName);

        Log.i("FilePath", "111" + myFile);
        if (myFile.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public void openFile(ContentManagementFragment contentManagementFragment, File url) {
        try {
            // Create URI
            Uri uri = FileProvider.getUriForFile(contentManagementFragment.getActivity(),
                    contentManagementFragment.getActivity().getPackageName() + ".file_provider", url);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // Check what kind of file you are trying to open, by comparing the url with extensions.
            // When the if condition is matched, plugin sets the correct intent (mime) type,
            // so Android knew what application to use to open the file
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
                // WAV audio file
                intent.setDataAndType(uri, "application/x-wav");
            } else if (url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf");
            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav");
            } else if (url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif");
            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if (url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                // Video files
                intent.setDataAndType(uri, "video/*");
            } else {
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

    private static class DownloadTask {
        Context context;
        View finalConvertView;
        ArrayList<String> urlList1;

        public DownloadTask(Context context, View finalConvertView, ArrayList<String> urlListl) {
            this.context = context;
            this.finalConvertView = finalConvertView;
            this.urlList1 = urlListl;
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Integer, String> {
        DownloadInfo info;
        private boolean isDownloading;

        public DownloadImageTask(DownloadInfo info) {
            this.info = info;
        }

        public DownloadImageTask() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            if (info != null) {
//                info.getProgressBar().setVisibility(View.VISIBLE);
//                isDownloading = false;
//            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            isDownloading = true;
//            if (info != null) {
//                info.getProgressBar().setVisibility(View.GONE);
//                notifyDataSetChanged();
//            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

//            info.setProgress(values[0]);
//            ProgressBar bar = info.getProgressBar();
//            if (bar != null) {
//                bar.setProgress(info.getProgress());
//                bar.invalidate();
//            }
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;

            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                //output = new FileOutputStream("/sdcard/file_name.extension");

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    //output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }
    }
}