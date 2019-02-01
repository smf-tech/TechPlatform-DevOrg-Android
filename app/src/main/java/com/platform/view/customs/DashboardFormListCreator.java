package com.platform.view.customs;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.pm.ProcessData;
import com.platform.view.fragments.PMFragment;

import java.lang.ref.WeakReference;
import java.util.List;

@SuppressWarnings({"ConstantConditions", "CanBeFinal", "unused"})
public class DashboardFormListCreator {

    private final WeakReference<PMFragment> fragment;
    private final String TAG = this.getClass().getSimpleName();

    public DashboardFormListCreator(PMFragment fragment) {
        this.fragment = new WeakReference<>(fragment);
    }

    public View getFormView(final String categoryName, final List<ProcessData> processData) {

        if (fragment == null || fragment.get() == null) {
            Log.e(TAG, "View returned null");
            return null;
        }

        View fragmentView = View.inflate(
                fragment.get().getContext(), R.layout.fragment_dashboard_pm, null);

        LinearLayout outerLnr = fragmentView.findViewById(R.id.lnr_dashboard_forms_category);

        View formTitleView = View.inflate(
                fragment.get().getContext(), R.layout.row_dashboard_forms_category, outerLnr);

        ((TextView) formTitleView.findViewById(R.id.txt_dashboard_form_category_name)).setText(categoryName);
        LinearLayout lnrInner = formTitleView.findViewById(R.id.lnr_inner);

        for (ProcessData data :
                processData) {
            if (!TextUtils.isEmpty(data.getName())) {
                View formTypeView = View.inflate(
                        fragment.get().getContext(), R.layout.row_dashboard_forms_category, lnrInner);

                ((TextView) formTypeView.findViewById(R.id.txt_dashboard_form_title)).setText(data.getName());
                if (!TextUtils.isEmpty(data.getId())) {
                    ImageView imgCreateForm = formTypeView.findViewById(R.id.iv_create_form);
                    imgCreateForm.setOnClickListener(v -> {

                    });
                }
                lnrInner.addView(formTypeView);
            }
        }
        return lnrInner;
    }
}
