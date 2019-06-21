package com.platform.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.models.attendance.AttendanceStatus;
import com.platform.view.fragments.AttendancePlannerFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@SuppressWarnings("CanBeFinal")
public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    private Context mContext;
    private List<String> leavesList;
    private String type;

    public AttendanceAdapter(final Context context, final List<String> leavesList,String type) {
        this.mContext = context;
        this.leavesList = leavesList;
        this.type=type;

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
            itemView.findViewById(R.id.img_delete).setVisibility(View.GONE);
            itemView.findViewById(R.id.img_edit).setVisibility(View.GONE);

        }
    }

    @NonNull
    @Override
    public AttendanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_leaves_row, viewGroup, false);
        return new AttendanceAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceAdapter.ViewHolder viewHolder, int i) {

        String date=leavesList.get(i);
        Long TimeStamp=Long.parseLong(date);


        Date d1 = new Date(TimeStamp);
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String FinalDate=df.format(d1);

        viewHolder.userImage.setBackgroundResource(R.drawable.ic_add_img);
        viewHolder.leaveDesc.setText("Sample Text"+FinalDate);


       /* Date StringTodate=null;
        try {
            StringTodate=new SimpleDateFormat("dd/MM/YYYY").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        viewHolder.userImage.setBackgroundResource(R.drawable.ic_add_img);
        viewHolder.leaveDesc.setText("Sample Text"+StringTodate);*/

        //viewHolder.userImage.setBackgroundResource(R.drawable.ic_add_img);
        //viewHolder.leaveDesc.setText("You have applied leaves from 8 march to 11 march");
        //viewHolder.leaveStatus.setText("Request status :"+"Not yet approved");
    }

    @Override
    public int getItemCount() {
        return leavesList.size();//leavesList.size();
    }
}