package com.octopusbjsindia.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.sel_content.SELVideoContent;

import java.util.List;

public class SELFragmentAdapter extends RecyclerView.Adapter<SELFragmentAdapter.ViewHolder> {

    private List<SELVideoContent> selContentList;
    private Context mContext;
    private RequestOptions requestOptions;

    public SELFragmentAdapter(Context context, final List<SELVideoContent> selContentList) {
        mContext = context;
        this.selContentList = selContentList;
        requestOptions = new RequestOptions().placeholder(R.drawable.ic_no_image);
        requestOptions = requestOptions.apply(RequestOptions.noTransformation());
    }

    @NonNull
    @Override
    public SELFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sel_video_row,
                parent, false);
        return new SELFragmentAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (selContentList.get(position).getThumbnailUrl() != null) {
            Glide.with(mContext)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(selContentList.get(position).getThumbnailUrl().replaceAll("//", ""))
                    .into(holder.imgThumbnail);
        }
        holder.tvForm.setText(selContentList.get(position).getTitle());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumbnail;
        TextView tvForm;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumbnail = itemView.findViewById(R.id.img_thumbnail);
            tvForm = itemView.findViewById(R.id.tv_form);
        }
    }

    @Override
    public int getItemCount() {
        return selContentList.size();
    }
}
