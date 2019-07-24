package com.platform.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;

import java.util.ArrayList;
import java.util.List;

public class DownloadListAdapter extends RecyclerView.Adapter<DownloadListAdapter.ViewHolder> {

    Context context;
    ArrayList<String> fileName;
    public DownloadListAdapter(Context context, ArrayList<String> fileName) {
        this.context=context;
        this.fileName=fileName;
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

        }
    }
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.download_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;

    }


    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.textView.setText("File1");
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
