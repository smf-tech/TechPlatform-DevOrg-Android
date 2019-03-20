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

import com.platform.R;
import com.platform.models.reports.ReportData;
import com.platform.utility.AppEvents;
import com.platform.utility.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.browser.customtabs.CustomTabsIntent;

@SuppressWarnings("CanBeFinal")
public class ReportsAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private Map<String, List<ReportData>> mReportsData;

    public ReportsAdapter(final Context context, final Map<String, List<ReportData>> reportsData) {
        mContext = context;
        mReportsData = reportsData;
    }

    @Override
    public int getGroupCount() {
        return mReportsData.size();
    }

    @Override
    public int getChildrenCount(final int groupPosition) {
        ArrayList<String> list = new ArrayList<>(mReportsData.keySet());
        String cat = list.get(groupPosition);

        List<ReportData> processData = mReportsData.get(cat);
        if (processData != null) {
            return processData.size();
        }

        return 0;
    }

    @Override
    public Object getGroup(final int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(final int groupPosition, final int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(final int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(final int groupPosition, final int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, final View convertView, final ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_all_forms_item, parent, false);

        ArrayList<String> list = new ArrayList<>(mReportsData.keySet());
        String cat = list.get(groupPosition);

        List<ReportData> processData = mReportsData.get(cat);
        int size = 0;
        if (processData != null) {
            size = processData.size();
        }

        ((TextView) view.findViewById(R.id.form_title)).setText(cat);
        ((TextView) view.findViewById(R.id.form_count)).setText(String.format("%s Forms", String.valueOf(size)));

        ImageView v = view.findViewById(R.id.form_image);
        if (isExpanded) {
            Util.rotateImage(180f, v);
        } else {
            Util.rotateImage(0f, v);
        }

        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, final boolean isLastChild, final View convertView, final ViewGroup parent) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.report_item, parent, false);

        ArrayList<String> list = new ArrayList<>(mReportsData.keySet());
        String cat = list.get(groupPosition);

        List<ReportData> processData = mReportsData.get(cat);
        if (processData != null) {
            ReportData data = processData.get(childPosition);

            ((TextView) view.findViewById(R.id.tv_report_title))
                    .setText(data.getName());

            view.setOnClickListener(v -> startWebView(data.getName(), data.getUrl(), mContext));
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(final int groupPosition, final int childPosition) {
        return false;
    }

    @SuppressWarnings("deprecation")
    private void startWebView(String name, String url, Context context) {
        Uri uri = Uri.parse(url);
        if (uri == null) {
            return;
        }

        AppEvents.trackAppEvent(context.getString(R.string.event_report_click, name));
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
        customTabsIntent.intent.setData(uri);
        final String EXTRA_CUSTOM_TABS_TOOLBAR_COLOR = "android.support.customtabs.extra.TOOLBAR_COLOR";
        customTabsIntent.intent.putExtra(EXTRA_CUSTOM_TABS_TOOLBAR_COLOR,
                context.getResources().getColor(R.color.colorPrimary));

        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(
                customTabsIntent.intent, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo resolveInfo : resolveInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            final String PACKAGE_NAME = "com.android.chrome";
            if (TextUtils.equals(packageName, PACKAGE_NAME)) {
                customTabsIntent.intent.setPackage(PACKAGE_NAME);
            }
        }

        customTabsIntent.launchUrl(context, uri);
    }
}