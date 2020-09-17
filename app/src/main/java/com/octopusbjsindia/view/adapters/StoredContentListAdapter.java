package com.octopusbjsindia.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.content.ContentData;
import com.octopusbjsindia.view.activities.StoredContentActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StoredContentListAdapter extends RecyclerView.Adapter<StoredContentListAdapter.ViewHolder>{

    Context context;
    /*ArrayList<String> fileName;
    ArrayList<String>itemsFiltered;
    String path;
    public fileFilterListener filterListener;*/
    public OnListTitleClick clickListener;
    ArrayList<ContentData> contentDownloadedList;

    public StoredContentListAdapter(Context context, ArrayList<ContentData> contentDownloadedList,final OnListTitleClick clickListener) {
        this.context=context;
        this.contentDownloadedList = contentDownloadedList;
        this.clickListener = clickListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView share,download;
        public TextView textView,txt_file_type;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            relativeLayout=itemView.findViewById(R.id.rel_lay_dwn_file);
            textView =itemView.findViewById(R.id.txt_file_name);
            txt_file_type =itemView.findViewById(R.id.txt_file_type);
            share=itemView.findViewById(R.id.img_share);
            download=itemView.findViewById(R.id.img_download);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onListTitleClick(getAdapterPosition());
                }
            });

        }
    }
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.row_storedcontent_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;

    }


    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.textView.setText(contentDownloadedList.get(position).getContentTiltle());
        holder.txt_file_type.setText(contentDownloadedList.get(position).getFileType());

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               deleteFileFromPath("",contentDownloadedList.get(position).getDownloadedFileName(),position);

            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //shareFile(path,fileName.get(position),position);

            }
        });
    }

    private void deleteFileFromPath(String path,String name,int pos) {
        File direcPath=new File(path+"/"+name);
        /*Uri imageUriLcl = FileProvider.getUriForFile(context,
                context.getApplicationContext().getPackageName() +
                        ".file_provider", direcPath);
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.delete(imageUriLcl, null, null);
        Toast.makeText(context, "Delete Success", Toast.LENGTH_SHORT).show();*/
        //notifyDataSetChanged();

        /*if(direcPath.exists())
        {
            direcPath.delete();
            Toast.makeText(context, "File  Delete Successfully", Toast.LENGTH_SHORT).show();
            fileName.remove(pos);
            notifyItemRemoved(pos);
            notifyItemRangeChanged(pos,fileName.size());
        }*/


            String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                    + Environment.DIRECTORY_DOWNLOADS;
            File myFile = new File(storagePath + "/" + name);
            if (myFile.exists()) {
                myFile.delete();
                Toast.makeText(context, "Delete Success", Toast.LENGTH_SHORT).show();
                contentDownloadedList.remove(pos);
                notifyItemRemoved(pos);
                notifyDataSetChanged();
                if (contentDownloadedList.size()==0){
                    ((StoredContentActivity)context).showNoData();
                }
            } else {

            }

    }

    private void shareFile(String path,String filename,int pos){

        String sharePath=path+"/"+filename;
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        Uri screenshotUri = Uri.parse(sharePath);
        sharingIntent.setType("*/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        context.startActivity(Intent.createChooser(sharingIntent, "Share File "));

    }

    @Override
    public int getItemCount() {

        return contentDownloadedList.size();
    }
public interface OnListTitleClick{
     public void onListTitleClick(int pos);
    }
}
