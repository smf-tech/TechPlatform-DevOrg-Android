package com.platform.view.adapters;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.platform.R;
import com.platform.models.LocaleData;
import com.platform.models.pm.ProcessData;
import com.platform.models.reports.ReportData;
import com.platform.syncAdapter.SyncAdapterUtils;
import com.platform.utility.AppEvents;
import com.platform.utility.Util;
import com.platform.view.fragments.CompletedFormsFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

@SuppressWarnings("CanBeFinal")
public class AppliedLeavesAdapter extends RecyclerView.Adapter<AppliedLeavesAdapter.ViewHolder> {

    private Context mContext;
    private List<String> leavesList;

    public AppliedLeavesAdapter(final Context context, final List<String> leavesList) {
        this.mContext = context;
        this.leavesList = leavesList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userImage;
        TextView leaveDesc;
        //TextView leaveStatus;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            leaveDesc = itemView.findViewById(R.id.tv_leave_desc);
            //leaveStatus = itemView.findViewById(R.id.tv_leave_status);
            userImage = itemView.findViewById(R.id.img_user_leaves);

        }
    }

    @NonNull
    @Override
    public AppliedLeavesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_leaves_row, viewGroup, false);
        return new AppliedLeavesAdapter.ViewHolder(v);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBindViewHolder(@NonNull AppliedLeavesAdapter.ViewHolder viewHolder, int i) {

        viewHolder.userImage.setBackgroundResource(R.drawable.add_img);
        viewHolder.leaveDesc.setText("You have applied leaves from 8 March to 11 March");
        //viewHolder.leaveStatus.setText("Request status :"+"Not yet approved");
    }

    @Override
    public int getItemCount() {
        return leavesList.size();
    }
}