package com.platform.view.adapters;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.reports.ReportData;

import java.util.List;

@SuppressWarnings("CanBeFinal")
public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ViewHolder> {

    private Context mContext;
    private List<ReportData> mListDataChild;
    private static final String EXTRA_CUSTOM_TABS_TOOLBAR_COLOR = "android.support.customtabs.extra.TOOLBAR_COLOR";
    private static final String PACKAGE_NAME = "com.android.chrome";

    ReportsAdapter(Context context, List<ReportData> childDataList) {
        this.mContext = context;
        this.mListDataChild = childDataList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        RelativeLayout container;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tv_report_title);
            container = itemView.findViewById(R.id.report_list_item_view);
        }
    }

    @NonNull
    @Override
    public ReportsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.report_item,
                viewGroup, false);
        return new ViewHolder(v);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBindViewHolder(@NonNull ReportsAdapter.ViewHolder viewHolder, int i) {
        ReportData reportData = mListDataChild.get(i);
        viewHolder.title.setText(reportData.getName());
        viewHolder.container.setOnClickListener(v -> startWebView(reportData.getUrl()));
    }

    @Override
    public int getItemCount() {
        return mListDataChild.size();
    }

    @SuppressWarnings("deprecation")
    private void startWebView(String url) {
        Uri uri = Uri.parse(url);
        if (uri == null) {
            return;
        }

        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
        customTabsIntent.intent.setData(uri);
        customTabsIntent.intent.putExtra(EXTRA_CUSTOM_TABS_TOOLBAR_COLOR,
                mContext.getResources().getColor(R.color.colorPrimary));

        PackageManager packageManager = mContext.getPackageManager();
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(
                customTabsIntent.intent, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo resolveInfo : resolveInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            if (TextUtils.equals(packageName, PACKAGE_NAME)) {
                customTabsIntent.intent.setPackage(PACKAGE_NAME);
            }
        }

        customTabsIntent.launchUrl(mContext, uri);
    }
}