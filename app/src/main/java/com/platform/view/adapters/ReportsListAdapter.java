package com.platform.view.adapters;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.reports.ReportData;

import java.util.List;
import java.util.Map;

public class ReportsListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private CustomTabsClient mClient;
    private List<String> mListDataHeader;
    private Map<String, List<ReportData>> mListDataChild;

    private static final String EXTRA_CUSTOM_TABS_TOOLBAR_COLOR = "android.support.customtabs.extra.TOOLBAR_COLOR";
    private static final String PACKAGE_NAME = "com.android.chrome";

    public ReportsListAdapter(Context context, List<String> headerList,
                              Map<String, List<ReportData>> childDataList) {

        this.mContext = context;
        this.mListDataHeader = headerList;
        this.mListDataChild = childDataList;

        CustomTabsServiceConnection service = new CustomTabsServiceConnection() {

            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                mClient = client;
                mClient.warmup(0);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mClient = null;
            }
        };

        CustomTabsClient.bindCustomTabsService(mContext, PACKAGE_NAME, service);
    }

    @Override
    public int getGroupCount() {
        return this.mListDataHeader.size();
    }

    @Override
    public int getChildrenCount(int childCount) {
        List<ReportData> reports = this.mListDataChild.get(this.mListDataHeader.get(childCount));
        if (reports != null) {
            return reports.size();
        }

        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mListDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<ReportData> reports = this.mListDataChild.get(this.mListDataHeader.get(groupPosition));
        if (reports != null) {
            return reports.get(childPosition);
        }

        return null;
    }

    @Override
    public long getGroupId(int groupId) {
        return groupId;
    }

    @Override
    public long getChildId(int groupId, int childId) {
        return childId;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater =
                    (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater != null ? layoutInflater.inflate(R.layout.layout_list_group,
                    null) : null;
        }

        if (convertView != null) {
            ImageView imgGroup = convertView.findViewById(R.id.header_down_arrow);
            if (isExpanded) {
                imgGroup.setImageResource(R.drawable.ic_down_arrow_light_blue);
            } else {
                imgGroup.setImageResource(R.drawable.ic_right_arrow_light_blue);
            }

            String headerTitle = (String) getGroup(groupPosition);
            TextView txtName = convertView.findViewById(R.id.report_category_name);
            txtName.setText(headerTitle);
        }

        return convertView;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflaterLayout = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflaterLayout != null ? inflaterLayout.inflate(R.layout.layout_report_item,
                    null) : null;
        }

        if (convertView != null) {
            ReportData report = (ReportData) getChild(groupPosition, childPosition);
            RelativeLayout layoutMain = convertView.findViewById(R.id.report_list_item);
            layoutMain.setOnClickListener(v -> startWebView(report.getUrl()));

            TextView reportName = convertView.findViewById(R.id.report_name);
            reportName.setText(report.getName());

            TextView reportDescription = convertView.findViewById(R.id.report_description);
            reportDescription.setText(report.getDescription());
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @SuppressWarnings("deprecation")
    private void startWebView(String url) {
        Uri uri =  Uri.parse(url);
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
