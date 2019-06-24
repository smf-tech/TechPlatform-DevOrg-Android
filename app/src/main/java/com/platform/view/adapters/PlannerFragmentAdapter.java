package com.platform.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.platform.R;
import com.platform.models.planner.SubmoduleData;
import com.platform.utility.Constants;
import com.platform.view.activities.CreateEventActivity;
import com.platform.view.activities.GeneralActionsActivity;
import com.platform.view.activities.PlannerDetailActivity;
import com.platform.view.fragments.AttendancePlannerFragment;
import com.platform.view.fragments.EventsPlannerFragment;
import com.platform.view.fragments.LeavePlannerFragment;
import com.platform.view.fragments.PlannerFragment;
import com.platform.view.fragments.TasksPlannerFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class PlannerFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<SubmoduleData> submoduleList;
    private PlannerFragment mFragment;

    public PlannerFragmentAdapter(Context context, ArrayList<SubmoduleData> submoduleList) {
        this.context = context;
        this.submoduleList = submoduleList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case 1:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_submodule_attendance, parent, false);
                return new ViewHolderAttendace(itemView);
            case 2:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_submodule_event, parent, false);
                return new ViewHolderEvent(itemView);

            case 3:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_submodule_task, parent, false);
                return new ViewHolderTask(itemView);
            case 4:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_submodule_leave, parent, false);
                return new ViewHolderLeave(itemView);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context.getApplicationContext());

        switch (submoduleList.get(position).getSubModule()) {
            case Constants.Planner.ATTENDANCE_KEY:
                submoduleList.get(position).getAttendanceData();
//                ((ViewHolderAttendace)holder).

                break;
            case Constants.Planner.EVENTS_KEY:
                if(submoduleList.get(position).getEventData()!=null &&
                        submoduleList.get(position).getEventData().size()>0){
                    EventListAdapter eventListAdapter = new EventListAdapter(context,
                            submoduleList.get(position).getEventData(), Constants.Planner.EVENTS_LABEL);
                    ((ViewHolderEvent)holder).rvEvents.setLayoutManager(mLayoutManager);
                    ((ViewHolderEvent)holder).rvEvents.setAdapter(eventListAdapter);
                }


                break;
            case Constants.Planner.TASKS_KEY:
                if(submoduleList.get(position).getTaskData()!=null &&
                        submoduleList.get(position).getTaskData().size()>0) {
                    EventListAdapter taskListAdapter = new EventListAdapter(context,
                            submoduleList.get(position).getTaskData(), Constants.Planner.EVENTS_LABEL);
                    ((ViewHolderEvent) holder).rvEvents.setLayoutManager(mLayoutManager);
                    ((ViewHolderTask) holder).rvEvents.setAdapter(taskListAdapter);
                }
                break;
            case Constants.Planner.LEAVES_KEY:
                submoduleList.get(position).getLeave();

                if(submoduleList.get(position).getLeave()!=null &&
                        submoduleList.get(position).getLeave().size()>0){
                    LeaveBalanceAdapter LeaveAdapter = new LeaveBalanceAdapter(context,
                            submoduleList.get(position).getLeave());
                    mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true);
                    ((ViewHolderLeave)holder).rvLeaveBalance.setLayoutManager(mLayoutManager);
                    ((ViewHolderLeave)holder).rvLeaveBalance.setAdapter(LeaveAdapter);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return submoduleList.size();
    }

    @Override
    public int getItemViewType(int position) {

        switch (submoduleList.get(position).getSubModule()) {
            case Constants.Planner.ATTENDANCE_KEY:
                return 1;
            case Constants.Planner.EVENTS_KEY:
                return 2;
            case Constants.Planner.TASKS_KEY:
                return 3;
            case Constants.Planner.LEAVES_KEY:
                return 4;
            default:
                return 0;
        }
    }

    public class ViewHolderAttendace extends RecyclerView.ViewHolder {
        TextView tvLabel,tvCheckInTime,tvCheckOutTime;
        TextView tvAttendanceDetails;
        Button btCheckIn, btCheckout;
        public ViewHolderAttendace(@NonNull View itemView) {
            super(itemView);
            tvLabel = itemView.findViewById(R.id.tv_title);
            btCheckIn = itemView.findViewById(R.id.bt_check_in);
            btCheckout = itemView.findViewById(R.id.bt_checkout);
            tvCheckInTime = itemView.findViewById(R.id.tv_check_in_time);
            tvCheckOutTime = itemView.findViewById(R.id.tv_check_out_time);
            tvAttendanceDetails = itemView.findViewById(R.id.tv_attendance_details);
            btCheckIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvCheckInTime.setText(DateFormat.format(Constants.TIME_FORMAT, new Date().getTime()));
                    tvCheckInTime.setVisibility(View.VISIBLE);
                    btCheckIn.setBackground(Objects.requireNonNull(context)
                            .getResources().getDrawable(R.drawable.bg_grey_box_with_border));
                    btCheckIn.setTextColor(context.getResources().getColor(R.color.attendance_text_color));
                    btCheckIn.setClickable(false);
                }
            });
            btCheckout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvCheckOutTime.setText(DateFormat.format(Constants.TIME_FORMAT, new Date().getTime()));
                    tvCheckOutTime.setVisibility(View.VISIBLE);
                    btCheckout.setBackground(Objects.requireNonNull(context)
                            .getResources().getDrawable(R.drawable.bg_grey_box_with_border));
                    btCheckout.setTextColor(context.getResources().getColor(R.color.attendance_text_color));
                    btCheckout.setClickable(false);
                }
            });

            tvAttendanceDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GeneralActionsActivity.class);
                    intent.putExtra(Constants.Planner.KEY_IS_DASHBOARD, false);
                    intent.putExtra("title", context.getString(R.string.attendance));
                    intent.putExtra("switch_fragments", "AttendancePlannerFragment");
                    context.startActivity(intent);
                }
            });

        }
    }

    public class ViewHolderEvent extends RecyclerView.ViewHolder {
        TextView tvLabel;
        RecyclerView rvEvents;
        TextView tvAllEventsDetail;
        FloatingActionButton btAddEvents;

        public ViewHolderEvent(@NonNull View itemView) {
            super(itemView);
            tvLabel = itemView.findViewById(R.id.tv_title);
            rvEvents = itemView.findViewById(R.id.rv_events);
            tvAllEventsDetail = itemView.findViewById(R.id.tv_all_events_list);
            btAddEvents = itemView.findViewById(R.id.bt_add_events);

            tvAllEventsDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentEventList = new Intent(context, PlannerDetailActivity.class);
                    intentEventList.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.EVENTS_LABEL);
                    context.startActivity(intentEventList);
                }
            });
            btAddEvents.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentCreateEvent = new Intent(context, CreateEventActivity.class);
                    intentCreateEvent.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.EVENTS_LABEL);
                    context.startActivity(intentCreateEvent);
                }
            });


        }
    }

    public class ViewHolderTask extends RecyclerView.ViewHolder {
        TextView tvLabel;
        RecyclerView rvEvents;
        TextView tvAllEventsDetail;
        FloatingActionButton btAddEvents;

        public ViewHolderTask(@NonNull View itemView) {
            super(itemView);
            tvLabel = itemView.findViewById(R.id.tv_title);
            rvEvents = itemView.findViewById(R.id.rv_events);
            tvAllEventsDetail = itemView.findViewById(R.id.tv_all_events_list);
            btAddEvents = itemView.findViewById(R.id.bt_add_events);

            tvAllEventsDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentEventList = new Intent(context, PlannerDetailActivity.class);
                    intentEventList.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.TASKS_LABEL);
                    context.startActivity(intentEventList);
                }
            });
            btAddEvents.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentCreateEvent = new Intent(context, CreateEventActivity.class);
                    intentCreateEvent.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.TASKS_LABEL);
                    context.startActivity(intentCreateEvent);
                }
            });
        }
    }

    public class ViewHolderLeave extends RecyclerView.ViewHolder {
        TextView tvLabel;
        TextView tvCheckLeaveDetailsLink;
        FloatingActionButton imgClickAddLeaves;
        RecyclerView rvLeaveBalance;

        public ViewHolderLeave(@NonNull View itemView) {
            super(itemView);
            tvLabel = itemView.findViewById(R.id.tv_title);
            tvCheckLeaveDetailsLink = itemView.findViewById(R.id.tv_link_check_leaves);
            imgClickAddLeaves = itemView.findViewById(R.id.fab_add_leaves);
            rvLeaveBalance = itemView.findViewById(R.id.rv_leave_balance);

            tvCheckLeaveDetailsLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GeneralActionsActivity.class);
                    intent.putExtra("title", context.getString(R.string.leave));
                    intent.putExtra("switch_fragments", "LeaveDetailsFragment");
//                    intent.putExtra("leaveDetail", serverResponse);
                    context.startActivity(intent);
                }
            });
            imgClickAddLeaves.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GeneralActionsActivity.class);
                    intent.putExtra("title", context.getString(R.string.apply_leave));
                    intent.putExtra("isEdit", false);
                    intent.putExtra("switch_fragments", "LeaveApplyFragment");
//                    intent.putExtra("leaveDetail", serverResponse);
                    context.startActivity(intent);
                }
            });

        }
    }


}
