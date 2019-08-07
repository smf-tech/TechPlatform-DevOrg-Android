package com.platform.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.Platform;
import com.platform.R;
import com.platform.database.AppDatabase;
import com.platform.database.DatabaseManager;
import com.platform.models.notifications.Notifications;
import com.platform.utility.AppEvents;
import com.platform.utility.Util;
import com.platform.view.activities.HomeActivity;
import com.platform.view.adapters.NotificatioAdapter;
import com.platform.view.adapters.NotificationsAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment {

    private TextView txtNoData;
    private RecyclerView rvNotification;
//    private ProgressBar progressBar;
//    private RelativeLayout progressBarLayout;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null && getArguments() != null) {
            Context context = getActivity();
            String title = (String) getArguments().getSerializable("TITLE");
            if (TextUtils.isEmpty(title)) {
                title = getString(R.string.notifications);
            }

            if ((boolean) getArguments().getSerializable("SHOW_BACK")) {
                ((HomeActivity) getActivity()).showBackArrow();
            }

            ((HomeActivity) context).setActionBarTitle(title);
            ((HomeActivity) context).setSyncButtonVisibility(false);
        }

        AppEvents.trackAppEvent(getString(R.string.event_approvals_screen_visit));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        txtNoData = view.findViewById(R.id.txt_no_data);
//        progressBarLayout = view.findViewById(R.id.notifications_progress_bar);
//        progressBar = view.findViewById(R.id.pb_gen_notifications);

        Util.updateNotificationsCount(true);


        List<Notifications> notificationList=new ArrayList<Notifications>();
//        List<Notifications> notificationList= DatabaseManager.getDBInstance(Platform.getInstance())
//                .getNotifications().getAllNotifications();

        if(!(notificationList.size()>0)){
            view.findViewById(R.id.txt_no_data).setVisibility(View.VISIBLE);
        }

        Collections.reverse(notificationList);

        rvNotification = view.findViewById(R.id.notifications_list);

        NotificatioAdapter adapter = new NotificatioAdapter(this, notificationList);
        rvNotification.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNotification.setItemAnimator(new DefaultItemAnimator());
        rvNotification.setAdapter(adapter);

        //mark all notification as read onens open this activity
        //mark all notification as read onens open this activity
//        List<Notifications> unRearNotificationList=DatabaseManager.getDBInstance(Platform.getInstance())
//                .getNotifications().getUnRearNotifications("Y");
//        for(Notifications obj:unRearNotificationList){
//            obj.setIsNew("N");
//            DatabaseManager.getDBInstance(Platform.getInstance())
//                    .getNotifications().update(obj);
//        }

    }

}
