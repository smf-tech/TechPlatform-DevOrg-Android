package com.platform.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadListAdapter extends RecyclerView.Adapter<DownloadListAdapter.ViewHolder> implements Filterable {

    Context context;
    ArrayList<String> fileName;
    ArrayList<String>itemsFiltered;
    String path;
    public fileFilterListener filterListener;
    public DownloadListAdapter(Context context, ArrayList<String> fileName,String path,fileFilterListener fileFilterListener) {
        this.context=context;
        this.fileName=fileName;
        this.path=path;
        this.filterListener=fileFilterListener;

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query=charSequence.toString();
                List<String> filtered = new ArrayList<>();

                if(query.isEmpty()){
                    filtered=fileName;
                }else {
                    for(String filename:fileName){
                        if(filename.toLowerCase().contains(query.toLowerCase())){
                            filtered.add(filename);
                        }
                    }
                }

                FilterResults filterResults=new FilterResults();
                filterResults.count=filtered.size();
                filterResults.values=filtered;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                itemsFiltered=(ArrayList<String>)filterResults.values;
                notifyDataSetChanged();

            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView share,download;
        public TextView textView;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            relativeLayout=itemView.findViewById(R.id.rel_lay_dwn_file);
            textView=itemView.findViewById(R.id.txt_file_name);
            share=itemView.findViewById(R.id.img_share);
            download=itemView.findViewById(R.id.img_download);
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    filterListener.onSelected(itemsFiltered.get(getAdapterPosition()));
                }
            })*/;

        }
    }
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.download_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;

    }


    public void onBindViewHolder(ViewHolder holder, int position) {



        holder.textView.setText(fileName.get(position));

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               deleteFileFromPath(path,fileName.get(position),position);

            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shareFile(path,fileName.get(position),position);

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

        if(direcPath.exists())
        {
            direcPath.delete();
            Toast.makeText(context, "File  Delete Successfully", Toast.LENGTH_SHORT).show();
            fileName.remove(pos);
            notifyItemRemoved(pos);
            notifyItemRangeChanged(pos,fileName.size());
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

        return fileName.size();
    }


    public interface fileFilterListener{
        void onSelected(String name);
    }
}
